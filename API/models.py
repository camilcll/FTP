from config import db, ma

class Feu(db.Model):
    __tablename__ = 'feu'
    idfeu = db.Column(db.Integer, primary_key=True)
    position = db.Column(db.String(10))
    intensite = db.Column(db.Integer)

class FeuSchema(ma.SQLAlchemyAutoSchema):
    class Meta:
        model = Feu
        load_instance = True    

class Capteur(db.Model):
    __tablename__ = 'capteur'
    idcapteur = db.Column(db.Integer, primary_key=True)
    position = db.Column(db.String(10))
    range = db.Column(db.Integer)
    intensite = db.Column(db.Integer)

class CapteurSchema(ma.SQLAlchemyAutoSchema):
    class Meta:
        model = Capteur
        load_instance = True  