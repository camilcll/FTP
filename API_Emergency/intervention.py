from flask import (
    make_response,
    abort,
)
from config import db
from models import Intervention, InterventionSchema


def create(intervention):

    id = intervention.get("id")

    print(id)

    existing_inter = (
        Intervention.query.filter(Intervention.id == id)
        .one_or_none()
    )

    # Can we insert this person?
    if existing_inter is None:

        # Create a person instance using the schema and the passed in person
        schema = InterventionSchema()
        new_inter = schema.load(intervention, session=db.session)

        # Add the person to the database
        db.session.add(new_inter)
        db.session.commit()

        # Serialize and return the newly created person in the response
        data = schema.dump(new_inter)

        return data, 201

    # Otherwise, nope, person exists already
    else:
        abort(
            409,
            "Intervention {id} exists already".format(id=id),
        )


def read_all():
    """
    This function responds to a request for /api/people
    with the complete lists of people

    :return:        json string of list of people
    """

    #print("BD:",db.engine)

    # Create the list of people from our data
    interventions = Intervention.query \
        .order_by(Intervention.id) \
        .all()

    # Serialize the data for the response
    inter_schema = InterventionSchema(many=True)
    return inter_schema.dump(interventions)


def update(interventions):
    """
    This function updates an existing person in the people structure
    Throws an error if a person with the name we want to update to
    already exists in the database.
    :param person_id:   Id of the person to update in the people structure
    :param person:      person to update
    :return:            updated person structure
    """

    data = []

    for i in interventions:
        
        id = i.get("id")

        # Get the person requested from the db into session
        update_inter = Intervention.query.filter(
            Intervention.id == id
        ).one_or_none()

        # Are we trying to find a person that does not exist?
        if update_inter is None:
            abort(
                404,
                "Intervention not found: {id}".format(id=id),
            )
        # Otherwise go ahead and update!
        else:
            # turn the passed in person into a db object
            schema = InterventionSchema()
            update = schema.load(i, session=db.session)

            # Set the id to the person we want to update
            update.id = update_inter.id

            # merge the new object into the old and commit it to the db
            db.session.merge(update)
            db.session.commit()

            # return updated person in the response
            data.append(schema.dump(update_inter))

    return data, 200

def delete():
    """
    This function deletes a person from the people structure
    :param person_id:   Id of the person to delete
    :return:            200 on successful delete, 404 if not found
    """
    # Get the person requested
    row_delete = Intervention.query.delete()

    # Did we find a person?
    if row_delete > 0:
        db.session.commit()
        return make_response(
            "Intervention has been deleted", 200
        )

    # Otherwise, nope, didn't find that person
    else:
        abort(
            409,
            "Aucun intervention a supprimer",
        )
