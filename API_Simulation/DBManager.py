from flask import render_template, request, redirect, url_for
import config
import json
import feu


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
                    "intensite": intensite
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
    listMarker = ""

    for f in feux:

        idFeu = f["id"]
        intensite = f["intensite"]
        position = json.loads(str(f["position"]).replace("'",'"'))
        coordX = 4.8+(float(position["x"])*0.0012)
        coordY = 45.720+(float(position["y"])*0.0011)

        listMarker += "L.marker([{latitude}, {longitude}]).addTo(map).bindPopup('Id: {id}<br>intensite: {intensite}');".format(latitude=coordY,longitude=coordX,id=idFeu,intensite=intensite)

        #print("coord:{0},{1}".format(coordX,coordY))

    return render_template("SimulationView.html",markers=listMarker)
    

# If we're running in stand alone mode, run the application
if __name__ == '__main__':
    app.run(debug=True)
