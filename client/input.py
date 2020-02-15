import RPi.GPIO as GPIO
import time
client1 = 26
client2 = 21


GPIO.setmode(GPIO.BCM)

GPIO.setup(client1, GPIO.IN, pull_up_down=GPIO.PUD_UP)
GPIO.setup(client2, GPIO.IN, pull_up_down=GPIO.PUD_UP)


state26 = GPIO.input(client1)
state21 = GPIO.input(client2)
if state26 == False:
    print('Client1')
elif state21 == False:
    print('Client2')

