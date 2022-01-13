from flask import (
    make_response,
    abort,
)
from config import db
from models import Vehicule, VehiculeSchema


def create(vehicule):

    id = vehicule.get("id")

    print(id)

    existing_vehicule = (
        Vehicule.query.filter(Vehicule.id == id)
        .one_or_none()
    )

    # Can we insert this person?
    if existing_vehicule is None:

        # Create a person instance using the schema and the passed in person
        schema = VehiculeSchema()
        new_vehicule = schema.load(vehicule, session=db.session)

        # Add the person to the database
        db.session.add(new_vehicule)
        db.session.commit()

        # Serialize and return the newly created person in the response
        data = schema.dump(new_vehicule)

        return data, 201

    # Otherwise, nope, person exists already
    else:
        abort(
            409,
            "Vehicule {id} exists already".format(id=id),
        )


def read_all():
    """
    This function responds to a request for /api/people
    with the complete lists of people

    :return:        json string of list of people
    """

    #print("BD:",db.engine)

    # Create the list of people from our data
    vehicules = Vehicule.query \
        .order_by(Vehicule.id) \
        .all()

    # Serialize the data for the response
    vehicule_schema = VehiculeSchema(many=True)
    return vehicule_schema.dump(vehicules)


def update(vehicules):
    """
    This function updates an existing person in the people structure
    Throws an error if a person with the name we want to update to
    already exists in the database.
    :param person_id:   Id of the person to update in the people structure
    :param person:      person to update
    :return:            updated person structure
    """

    data = []

    for v in vehicules:
        
        id = v.get("id")

        # Get the person requested from the db into session
        update_vehicule = Vehicule.query.filter(
            Vehicule.id == id
        ).one_or_none()

        # Are we trying to find a person that does not exist?
        if update_vehicule is None:
            abort(
                404,
                "Vehicule not found: {id}".format(id=id),
            )
        # Otherwise go ahead and update!
        else:
            # turn the passed in person into a db object
            schema = VehiculeSchema()
            update = schema.load(v, session=db.session)

            # Set the id to the person we want to update
            update.id = update_vehicule.id

            # merge the new object into the old and commit it to the db
            db.session.merge(update)
            db.session.commit()

            # return updated person in the response
            data.append(schema.dump(update_vehicule))

    return data, 200
