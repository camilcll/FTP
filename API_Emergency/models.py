from config import db, ma
from sqlalchemy.dialects.postgresql import JSON
from marshmallow import Schema

class Feu(db.Model):
    __tablename__ = 'feu'
    id = db.Column(db.Integer, primary_key=True)
    position = db.Column(JSON)
    intensite = db.Column(db.Integer)

class FeuSchema(ma.SQLAlchemyAutoSchema):
    class Meta:
        model = Feu
        load_instance = True    
        ordered = True

class Capteur(db.Model):
    __tablename__ = 'capteur'
    id = db.Column(db.Integer, primary_key=True)
    position = db.Column(JSON)
    intensite = db.Column(db.Integer)
    range = db.Column(db.Integer)
    

class CapteurSchema(ma.SQLAlchemyAutoSchema):
    class Meta:
        model = Capteur
        load_instance = True 
        ordered = True 