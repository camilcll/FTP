from config import db, ma
from sqlalchemy.dialects.postgresql import JSON

class Feu(db.Model):
    __tablename__ = 'feu'
    id = db.Column(db.Integer, primary_key=True)
    position = db.Column(JSON)
    intensite = db.Column(db.Integer)
    detecte = db.Column(db.Boolean)

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

class Caserne(db.Model):
    __tablename__ = 'caserne'
    id = db.Column(db.Integer, primary_key=True)
    position = db.Column(JSON)
    listeVehicule = db.Column(JSON)

class CaserneSchema(ma.SQLAlchemyAutoSchema):
    class Meta:
        model = Caserne
        load_instance = True    
        ordered = True

class Vehicule(db.Model):
    __tablename__ = 'vehicule'
    id = db.Column(db.Integer, primary_key=True)
    type = db.Column(db.String)
    idcaserne = db.Column(db.Integer)
    disponible = db.Column(db.Boolean)

class VehiculeSchema(ma.SQLAlchemyAutoSchema):
    class Meta:
        model = Vehicule
        load_instance = True    
        ordered = True

class Intervention(db.Model):
    __tablename__ = 'intervention'
    id = db.Column(db.Integer, primary_key=True)
    feu = db.Column(JSON)
    listeVehicule = db.Column(JSON)
    etat = db.Column(db.Integer)

class InterventionSchema(ma.SQLAlchemyAutoSchema):
    class Meta:
        model = Intervention
        load_instance = True    
        ordered = True


