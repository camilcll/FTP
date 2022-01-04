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