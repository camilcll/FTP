from flask import (
    make_response,
    abort,
)
from config import db
from models import Feu, FeuSchema

def read_all():
    """
    This function responds to a request for /api/people
    with the complete lists of people

    :return:        json string of list of people
    """

    #print("BD:",db.engine)

    # Create the list of people from our data
    feux = Feu.query \
        .order_by(Feu.id) \
        .all()

    # Serialize the data for the response
    feu_schema = FeuSchema(many=True)
    return feu_schema.dump(feux)


def create(feu):
    """
    This function creates a new person in the people structure
    based on the passed in person data
    :param person:  person to create in people structure
    :return:        201 on success, 409 on person exists
    """
    id = feu.get("id")

    print(id)

    existing_feu = (
        Feu.query.filter(Feu.id == id)
        .one_or_none()
    )

    # Can we insert this person?
    if existing_feu is None:

        # Create a person instance using the schema and the passed in person
        schema = FeuSchema()
        new_feu = schema.load(feu, session=db.session)

        # Add the person to the database
        db.session.add(new_feu)
        db.session.commit()

        # Serialize and return the newly created person in the response
        data = schema.dump(new_feu)

        return data, 201

    # Otherwise, nope, person exists already
    else:
        abort(
            409,
            "Feu {id} exists already".format(id=id),
        )

def update(feux):
    """
    This function updates an existing person in the people structure
    Throws an error if a person with the name we want to update to
    already exists in the database.
    :param person_id:   Id of the person to update in the people structure
    :param person:      person to update
    :return:            updated person structure
    """

    data = []

    for feu in feux:
        
        id = feu.get("id")

        # Get the person requested from the db into session
        update_feu = Feu.query.filter(
            Feu.id == id
        ).one_or_none()

        # Are we trying to find a person that does not exist?
        if update_feu is None:
            abort(
                404,
                "Feu not found: {id}".format(id=id),
            )
        # Otherwise go ahead and update!
        else:
            # turn the passed in person into a db object
            schema = FeuSchema()
            update = schema.load(feu, session=db.session)

            # Set the id to the person we want to update
            update.id = update_feu.id

            # merge the new object into the old and commit it to the db
            db.session.merge(update)
            db.session.commit()

            # return updated person in the response
            data.append(schema.dump(update_feu))

    return data, 200

def delete():
    """
    This function deletes a person from the people structure
    :param person_id:   Id of the person to delete
    :return:            200 on successful delete, 404 if not found
    """
    # Get the person requested
    row_delete = Feu.query.delete()

    # Did we find a person?
    if row_delete > 0:
        db.session.commit()
        return make_response(
            "Feu has been deleted", 200
        )

    # Otherwise, nope, didn't find that person
    else:
        abort(
            409,
            "Aucun feux a supprimer",
        )