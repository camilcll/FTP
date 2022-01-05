# Program to control passerelle between Android application
# and micro-controller through USB tty
import time
import argparse
import signal
import sys
import socket
import socketserver
import serial
import threading

HOST = "0.0.0.0"
UDP_PORT = 10000
MICRO_COMMANDS = ["TL", "LT"]
FILENAME = "values.txt"
LAST_VALUE = ""
# dict that holds subsriber's list
SUBSCRIBERS = {}

# send serial message
SERIALPORT = "COM4"
BAUDRATE = 115200
ser = serial.Serial()


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


# Main program logic follows:
if __name__ == '__main__':
    initUART()
    f = open(FILENAME, "a")
    print('Press Ctrl-C to quit.')

    try:
        while ser.isOpen():
            # time.sleep(100)
            if (ser.inWaiting() > 0):  # if incoming bytes are waiting
                # According to the simple protocol temperatureValue;lightValue*, we wait until the stop character '*'
                data_str = ser.read_until(';').decode('utf-8').replace('\n', "").replace('\r', "")
                f.write(data_str)
                print(data_str)

    except (KeyboardInterrupt, SystemExit):
        f.close()
        ser.close()
        exit()
