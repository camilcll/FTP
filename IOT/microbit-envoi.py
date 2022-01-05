from microbit import *
import radio
radio.on()

uart.init(115200)

def value_byte_xor(text: bytes, key:int) -> bytes :
    return bytes([b ^ key for b in text])

while True:

    # ip source : 164.4.1.4
    src = bytes([0x0a,0x04,0x01,0x04])

    # ip destination : 164.4.1.5
    dest = bytes([0x0a,0x04,0x01,0x05])
    #while chr(car) != "]":
        #car = uart.read(1)
        #valeurs += car
    valeurs = uart.read()
    trame = dest+src

    if (valeurs):
        b = bytes(valeurs)
        trame += b
        cle = 24
        cryptage = trame
        lg=len(cryptage)
        DonneeCrypte=""
        print(trame)

        for i in range(lg):
            if cryptage[i]==' ':
                DonneeCrypte+=' '
            else:
                asc=cryptage[i]+cle
                #print("ASC: " + str(asc))
                DonneeCrypte+=chr(asc)


        envoi = value_byte_xor(bytes(DonneeCrypte,'utf-8'),100)
        #print(envoi)
        radio.send_bytes(envoi)