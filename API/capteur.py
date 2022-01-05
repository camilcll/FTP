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