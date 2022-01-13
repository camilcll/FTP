from flask import render_template, request, redirect, url_for
import config
import json
import feu
import capteur

import intervention


# Create the application instance
app = config.connex_app
# Read the swagger.yml file to configure the endpoints
app.add_api('swagger.yml')


@app.route('/createFeu', methods=["POST"])
def createFeu():

    if request.method == "POST":

        req = request.form

        id = int(req["id"])
        positionX = int(req["positionX"])
        positionY = int(req["positionY"])
        intensite = int(req["intensite"])

        if (positionX>=0 and positionX<=100 and positionY>=0 and positionY<=60 and intensite>=0 and intensite<=8):

            feuJson = {"id": id,
                    "position":{
                        "x": positionX,
                        "y": positionY
                    },
                    "intensite": intensite,
                    "detecte": False
                    }

            res = feu.create(feuJson)
            if res[1] == 201:
                return redirect(url_for('main'))
            else:
                if(type(res) == tuple):
                    return "Error {0}: {1}".format(res[1],res[0])
                else:
                    return "Internal Error 500 {0}".format(res)


# Create a URL route in our application for "/"
@app.route('/')
def main():

    feux = feu.read_all()
    capteurs = capteur.read_all()
    inter = intervention.read_all()
    listMarker = ""

    for f in feux:

        idFeu = f["id"]
        intensite = int(f["intensite"])
        if(intensite != 0):
            position = json.loads(str(f["position"]).replace("'",'"'))
            coordX = 4.8+(float(position["x"])*0.0012)
            coordY = 45.720+(float(position["y"])*0.0011)

            listMarker += "L.marker([{latitude}, {longitude}],{{icon: iconFeu}}).addTo(map).bindPopup('Id: {id}<br>intensite: {intensite}');".format(latitude=coordY,longitude=coordX,id=idFeu,intensite=intensite)

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


        #print("coord:{0},{1}".format(coordX,coordY))

    return render_template("SimulationView.html",markers=listMarker)
    

# If we're running in stand alone mode, run the application
if __name__ == '__main__':
    app.run(debug=True)
