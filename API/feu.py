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

    print("BD:",db.engine)

    # Create the list of people from our data
    feux = Feu.query \
        .order_by(Feu.idfeu) \
        .all()

    # Serialize the data for the response
    feu_schema = FeuSchema(many=True)
    return feu_schema.dump(feux)