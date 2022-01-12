from flask import (
    make_response,
    abort,
)
from config import db
from models import Caserne, CaserneSchema


def create(caserne):

    id = caserne.get("id")

    print(id)

    existing_caserne = (
        Caserne.query.filter(Caserne.id == id)
        .one_or_none()
    )

    # Can we insert this person?
    if existing_caserne is None:

        # Create a person instance using the schema and the passed in person
        schema = CaserneSchema()
        new_caserne = schema.load(caserne, session=db.session)

        # Add the person to the database
        db.session.add(new_caserne)
        db.session.commit()

        # Serialize and return the newly created person in the response
        data = schema.dump(new_caserne)

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
    casernes = Caserne.query \
        .order_by(Caserne.id) \
        .all()

    # Serialize the data for the response
    caserne_schema = CaserneSchema(many=True)
    return caserne_schema.dump(casernes)


def update(casernes):
    """
    This function updates an existing person in the people structure
    Throws an error if a person with the name we want to update to
    already exists in the database.
    :param person_id:   Id of the person to update in the people structure
    :param person:      person to update
    :return:            updated person structure
    """

    data = []

    for c in casernes:
        
        id = c.get("id")

        # Get the person requested from the db into session
        update_caserne = Caserne.query.filter(
            Caserne.id == id
        ).one_or_none()

        # Are we trying to find a person that does not exist?
        if update_caserne is None:
            abort(
                404,
                "Caserne not found: {id}".format(id=id),
            )
        # Otherwise go ahead and update!
        else:
            # turn the passed in person into a db object
            schema = CaserneSchema()
            update = schema.load(c, session=db.session)

            # Set the id to the person we want to update
            update.id = update_caserne.id

            # merge the new object into the old and commit it to the db
            db.session.merge(update)
            db.session.commit()

            # return updated person in the response
            data.append(schema.dump(update_caserne))

    return data, 200
