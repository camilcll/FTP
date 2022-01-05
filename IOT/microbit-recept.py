from microbit import *
import radio
radio.on()

def value_byte_xor(text: bytes, key:int) -> bytes :
    return bytes([b ^ key for b in text])

message = ""

while True:

    # ip source : 10.4.1.5
    src = bytes([0x0a,0x04,0x01,0x05])

    # ip destination : 10.4.1.4
    dest = bytes([0x0a,0x04,0x01,0x04])

    reception = radio.receive_bytes()

    if (reception):
        message = reception

        if (message):
            recu = value_byte_xor(message,100)
            messagecrypte = str(recu,'utf-8')
            #print(messagecrypte)
            lg=len(messagecrypte)
            DonneeClair=""
            cle=24
            #print(DonneeClair)
            for i in range(lg):
                if messagecrypte[i]==' ':
                    DonneeClair+=' '
                else:
                    asc=ord(messagecrypte[i])-cle
                    #print("ASC: "+str(asc))
                    DonneeClair+=chr(asc)

            if (bytes(DonneeClair[:4],'utf-8')==src):
                print(DonneeClair[8:])
