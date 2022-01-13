from flask import render_template
import config
import json 

import feu
import intervention
import capteur


# Create the application instance
app = config.connex_app
print(app)
# Read the swagger.yml file to configure the endpoints
app.add_api('swagger.yml')

# Create a URL route in our application for "/"
@app.route('/')
def main():

    feux = feu.read_all()
    listMarker = ""

    capteurs = capteur.read_all()

    inter = intervention.read_all()

    for f in feux:

        idFeu = f["id"]
        intensite = int(f["intensite"])
        if intensite != 0:
            position = json.loads(str(f["position"]).replace("'",'"'))
            coordX = 4.8+(float(position["x"])*0.0012)
            coordY = 45.720+(float(position["y"])*0.0011)

            listMarker += "L.marker([{latitude}, {longitude}],{{icon: iconFeu}}).addTo(map).bindPopup('Id: {id}<br>intensite: {intensite}');".format(latitude=coordY,longitude=coordX,id=idFeu,intensite=intensite)

        #print("coord:{0},{1}".format(coordX,coordY))
    for c in capteurs:

        position = json.loads(str(c["position"]).replace("'",'"'))
        coordX = 4.8+(float(position["x"])*0.0012)
        coordY = 45.720+(float(position["y"])*0.0011)

        listMarker += "L.marker([{latitude}, {longitude}],{{icon: iconCapteur}}).addTo(map);".format(latitude=coordY,longitude=coordX)

    for i in inter:

        etat = int(i["etat"])

        if etat == 0:

            lstVehicule = json.loads(json.dumps(i["listeVehicule"]))
            print(lstVehicule)    
            if (lstVehicule[0]):
                coordX = 4.8+(float(lstVehicule[0]["position"]["x"])*0.0012)
                coordY = 45.720+(float(lstVehicule[0]["position"]["y"])*0.0011)

                listMarker += "L.marker([{latitude}, {longitude}],{{icon: iconCamion}}).addTo(map);".format(latitude=coordY,longitude=coordX)


    

    return render_template("EmergencyView.html",markers=listMarker)

# If we're running in stand alone mode, run the application
if __name__ == '__main__':
    print("in")
    print(app)
    app.run(debug=True)
