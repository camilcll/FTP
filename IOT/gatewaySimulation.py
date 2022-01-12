import json
import serial
import requests

import time


SERIALPORT = "COM3"
BAUDRATE = 115200
ser = serial.Serial()

HOST = "164.4.1.4:5000"

def initUART():
    # ser = serial.Serial(SERIALPORT, BAUDRATE)
    ser.port = SERIALPORT
    ser.baudrate = BAUDRATE
    ser.bytesize = serial.EIGHTBITS  # number of bits per bytes
    ser.parity = serial.PARITY_NONE  # set parity check: no parity
    ser.stopbits = serial.STOPBITS_ONE  # number of stop bits
    ser.timeout = 5  # block read

    # ser.timeout = 0             #non-block read
    # ser.timeout = 2              #timeout block read
    ser.xonxoff = False  # disable software flow control
    ser.rtscts = False  # disable hardware (RTS/CTS) flow control
    ser.dsrdtr = False  # disable hardware (DSR/DTR) flow control
    # ser.writeTimeout = 0     #timeout for write
    print('Starting Up Serial Monitor')
    try:
        ser.open()
    except serial.SerialException:
        print("Serial {} port not available".format(SERIALPORT))
        exit()

def sendUARTMessage(msg):
    ser.write(msg.encode())

def TransformJsonData(jsonData):

    jsonParse = json.loads(jsonData)

    transformedData = ""

    for i in range (60):
        objectParse = []
        objectParse.append(jsonParse[i]["id"])
        objectParse.append(jsonParse[i]["position"]["x"])
        objectParse.append(jsonParse[i]["position"]["y"])
        objectParse.append(jsonParse[i]["intensite"])
        objectParse.append(jsonParse[i]["range"])
        transformedData = "{0};".format(tuple(objectParse))
        print(transformedData)
        sendUARTMessage(transformedData)
        time.sleep(0.05)
        


if __name__ == '__main__':

    initUART()

    data = requests.get(HOST + '/api/simulation/capteur')

    #data = '[{"id":1,"position":{"x":5,"y":5},"intensite":0}, {"id":2,"position":{"x":15,"y":5},"intensite":0}, {"id":3,"position":{"x":25,"y":5},"intensite":0}, {"id":4,"position":{"x":35,"y":5},"intensite":0}, {"id":5,"position":{"x":45,"y":5},"intensite":0}, {"id":6,"position":{"x":55,"y":5},"intensite":0}, {"id":7,"position":{"x":65,"y":5},"intensite":0}, {"id":8,"position":{"x":75,"y":5},"intensite":0}, {"id":9,"position":{"x":85,"y":5},"intensite":0}, {"id":10,"position":{"x":95,"y":5},"intensite":0}, {"id":11,"position":{"x":5,"y":15},"intensite":0}, {"id":12,"position":{"x":15,"y":15},"intensite":0}, {"id":13,"position":{"x":25,"y":15},"intensite":0}, {"id":14,"position":{"x":35,"y":15},"intensite":0}, {"id":15,"position":{"x":45,"y":15},"intensite":0}, {"id":16,"position":{"x":55,"y":15},"intensite":0}, {"id":17,"position":{"x":65,"y":15},"intensite":0}, {"id":18,"position":{"x":75,"y":15},"intensite":0}, {"id":19,"position":{"x":85,"y":15},"intensite":0}, {"id":20,"position":{"x":95,"y":15},"intensite":0}, {"id":21,"position":{"x":5,"y":25},"intensite":0}, {"id":22,"position":{"x":15,"y":25},"intensite":0}, {"id":23,"position":{"x":25,"y":25},"intensite":0}, {"id":24,"position":{"x":35,"y":25},"intensite":0}, {"id":25,"position":{"x":45,"y":25},"intensite":0}, {"id":26,"position":{"x":55,"y":25},"intensite":0}, {"id":27,"position":{"x":65,"y":25},"intensite":0}, {"id":28,"position":{"x":75,"y":25},"intensite":0}, {"id":29,"position":{"x":85,"y":25},"intensite":0}, {"id":30,"position":{"x":95,"y":25},"intensite":0}, {"id":31,"position":{"x":5,"y":35},"intensite":0}, {"id":32,"position":{"x":15,"y":35},"intensite":0}, {"id":33,"position":{"x":25,"y":35},"intensite":0}, {"id":34,"position":{"x":35,"y":35},"intensite":0}, {"id":35,"position":{"x":45,"y":35},"intensite":0}, {"id":36,"position":{"x":55,"y":35},"intensite":0}, {"id":37,"position":{"x":65,"y":35},"intensite":0}, {"id":38,"position":{"x":75,"y":35},"intensite":0}, {"id":39,"position":{"x":85,"y":35},"intensite":0}, {"id":40,"position":{"x":95,"y":35},"intensite":0}, {"id":41,"position":{"x":5,"y":45},"intensite":0}, {"id":42,"position":{"x":15,"y":45},"intensite":0}, {"id":43,"position":{"x":25,"y":45},"intensite":0}, {"id":44,"position":{"x":35,"y":45},"intensite":0}, {"id":45,"position":{"x":45,"y":45},"intensite":0}, {"id":46,"position":{"x":55,"y":45},"intensite":0}, {"id":47,"position":{"x":65,"y":45},"intensite":0}, {"id":48,"position":{"x":75,"y":45},"intensite":0}, {"id":49,"position":{"x":85,"y":45},"intensite":0}, {"id":50,"position":{"x":95,"y":45},"intensite":0}, {"id":51,"position":{"x":5,"y":55},"intensite":0}, {"id":52,"position":{"x":15,"y":55},"intensite":0}, {"id":53,"position":{"x":25,"y":55},"intensite":0}, {"id":54,"position":{"x":35,"y":55},"intensite":0}, {"id":55,"position":{"x":45,"y":55},"intensite":0}, {"id":56,"position":{"x":55,"y":55},"intensite":0}, {"id":57,"position":{"x":65,"y":55},"intensite":0}, {"id":58,"position":{"x":75,"y":55},"intensite":0}, {"id":59,"position":{"x":85,"y":55},"intensite":0}, {"id":60,"position":{"x":95,"y":55},"intensite":0}]'
    print(data.text)

    TransformJsonData(data.text)