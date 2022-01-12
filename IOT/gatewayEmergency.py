# python 3.6
from ctypes import c_char_p
import time
import sys
import serial
import threading
import random
from paho.mqtt import client as mqtt_client
import requests
import ast

import json

# send serial message
SERIALPORT = "/dev/ttyACM0"
BAUDRATE = 115200
ser = serial.Serial()

HOST = "http://localhost:5000" #API HOST

broker = '127.0.0.1'
port = 1883
topic = "python/mqtt"
# generate client ID with pub prefix randomly
client_id = f'python-mqtt-{random.randint(0, 1000)}'
username = 'passerelle'
password = 'passerelle'

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


def connect_mqtt():
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")
        else:
            print("Failed to connect, return code %d\n", rc)

    client = mqtt_client.Client(client_id)
    client.username_pw_set(username, password)
    client.on_connect = on_connect
    client.connect(broker, port)
    return client


def publish(client, message):
    result = client.publish(topic, message)
    # result: [0, 1]
    status = result[0]
    if status == 0:
        print(f"Send `{message}` to topic `{topic}`")
    else:
        print(f"Failed to send message to topic {topic}")

def TransformMatrixData(matrix):
    capteurs = matrix[:-1].split(';')
    jsonData = []

    for capteur in capteurs:
        capteurTuple = ast.literal_eval(capteur)
        jsonstring = { "id": capteurTuple[0],
                        "position" : 
                        {"x": capteurTuple[1],
                        "y": capteurTuple[2]},
                        "intensite": capteurTuple[3],
                        "range": capteurTuple[4]}
        jsonData.append(jsonstring)

    return json.dumps(jsonData)




# Main program logic follows:
if __name__ == '__main__':
    initUART()
    client = connect_mqtt()
    client.loop_start()

    capteurs = ""

    try:
        while ser.isOpen():
            # time.sleep(100)
            if (ser.inWaiting() > 0):  # if incoming bytes are waiting                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
                # According to the simple protocol temperatureValue;lightValue*, we wait until the stop character '*'
                data_str = ser.read_until(';').decode('utf-8').replace('\n', "").replace('\r', "")
                capteurs += data_str
                if("(59") in data_str:
                    headers = {"Content-Type": "application/json"}
                    dataJson = TransformMatrixData(capteurs)
                    publish(client, dataJson)
                    url = HOST+"/api/emergency/capteur"
                    r = requests.put(url, data=dataJson, headers=headers)
                    capteurs = ""
                print(data_str)
                

    except (KeyboardInterrupt, SystemExit):
        ser.close()
        exit()



