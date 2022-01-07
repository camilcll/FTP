from flask import (
    make_response,
    abort,
)
from config import db
from models import Capteur, CapteurSchema

def read_all():
    """
    This function responds to a request for /api/people
    with the complete lists of people

    :return:        json string of list of people
    """

    # Create the list of people from our data
    capteurs = Capteur.query \
        .order_by(Capteur.id) \
        .all()

    # Serialize the data for the response
    capteur_schema = CapteurSchema(many=True)
    return capteur_schema.dump(capteurs)


def create(capteur):
    """
    This function creates a new person in the people structure
    based on the passed in person data
    :param person:  person to create in people structure
    :return:        201 on success, 409 on person exists
    """
    id = capteur.get("id")

    print(id)

    existing_capteur = (
        Capteur.query.filter(Capteur.id == id)
        .one_or_none()
    )

    # Can we insert this person?
    if existing_capteur is None:

        # Create a person instance using the schema and the passed in person
        schema = CapteurSchema()
        new_capteur = schema.load(capteur, session=db.session)

        # Add the person to the database
        db.session.add(new_capteur)
        db.session.commit()

        # Serialize and return the newly created person in the response
        data = schema.dump(new_capteur)

        return data, 201

    # Otherwise, nope, person exists already
    else:
        abort(
            409,
            "Capteur {id} exists already".format(id=id),
        )


def update(capteurs):
    """
    This function updates an existing person in the people structure
    Throws an error if a person with the name we want to update to
    already exists in the database.
    :param person_id:   Id of the person to update in the people structure
    :param person:      person to update
    :return:            updated person structure
    """

    data = ""

    for capteur in capteurs:
        
        id = capteur.get("id")

        # Get the person requested from the db into session
        update_capteur = Capteur.query.filter(
            Capteur.id == id
        ).one_or_none()

        # Are we trying to find a person that does not exist?
        if update_capteur is None:
            abort(
                404,
                "Capteur not found: {id}".format(id=id),
            )
        # Otherwise go ahead and update!
        else:
            # turn the passed in person into a db object
            schema = CapteurSchema()
            update = schema.load(capteur, session=db.session)

            # Set the id to the person we want to update
            update.id = update_capteur.id

            # merge the new object into the old and commit it to the db
            db.session.merge(update)
            db.session.commit()

            # return updated person in the response
            data += schema.dump(update_capteur)

    return data, 200


def delete():
    """
    This function deletes a person from the people structure
    :param person_id:   Id of the person to delete
    :return:            200 on successful delete, 404 if not found
    """
    # Get the person requested
    row_delete = Capteur.query.delete()

    # Did we find a person?
    if row_delete > 0:
        db.session.commit()
        return make_response(
            "Capteur has been deleted", 200
        )

    # Otherwise, nope, didn't find that person
    else:
        abort(
            409,
            "Aucun capteurs a supprimer",
        )