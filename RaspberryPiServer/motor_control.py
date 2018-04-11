
import socket
import RPi.GPIO as GPIO
from time import sleep
from decimal import Decimal
import setup

# UDP SETUP
UDP_IP = "192.168.1.100" 
UDP_PORT = 5100

sock = socket.socket(socket.AF_INET,    # Internet
                     socket.SOCK_DGRAM) # UDP
sock.bind((UDP_IP, UDP_PORT))

# MOTOR SETUP
DIR = 20   # Direction GPIO Pin
STEP = 21  # Step GPIO Pin
CW = 1     # Clockwise Rotation
CCW = 0    # Counterclockwise Rotation
SPR = 200  # Steps per Revolution (360 / 7.5)
MODE = (14, 15, 18) # Microstep Resolution GPIO Pins
HYSTERESIS = 25     # Motor movement insensitivity

setup.setupGPIO(MODE,DIR,STEP,CW,CCW,SPR)

# VARIABLES
step_count = SPR * 32
delay = .00001 / 32
current_angle = 0 # [0-6400]
angle = 0         # [0-6400]
i=0

while True:
        i+=1
        
        if i%50 == 0: #send every ith sample
          data, addr = sock.recvfrom(1024) # buffer size is 1024 bytes
          angle = int(float(data[15:25])/6.28*step_count) # temporary solution

        if i%50 == 0:
          print "UDP: [0-6.28]", int(float(data[15:25])/6.28*360)
          print "Angle [0-6400]: ", angle
          print "Current [0-6400]: ", current_angle
          print "Diff: [0-6400]", current_angle-angle, '\n'

        if angle > current_angle+HYSTERESIS:
          current_angle += 1
          if current_angle < 6400:
            GPIO.output(DIR, 1)
            GPIO.output(STEP, GPIO.HIGH)
        elif angle < current_angle-HYSTERESIS:
          current_angle -= 1
          if current_angle < 6400:
            GPIO.output(DIR, 0)
            GPIO.output(STEP, GPIO.HIGH)

        sleep(delay)
        GPIO.output(STEP, GPIO.LOW)
        sleep(delay)

GPIO.cleanup()
