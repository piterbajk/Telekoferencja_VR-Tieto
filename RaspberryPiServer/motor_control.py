import socket
import threading
import RPi.GPIO as GPIO
from time import sleep
from decimal import Decimal
import setup2

GPIO.setwarnings(False)

# UDP SETUP
UDP_IP = "0.0.0.0"
UDP_PORT = 5100

sock = socket.socket(socket.AF_INET, 	# Internet
                     socket.SOCK_DGRAM) # UDP
sock.bind((UDP_IP, UDP_PORT))

# MOTOR SETUP
DIRX = 20  # Direction GPIO Pin
DIRY = 5   # Direction GPIO Pin
STEPX = 21 # Step GPIO Pin
STEPY = 6  # Step GPIO Pin
MODEX = (14, 15, 18) # Microstep Resolution GPIO Pins
MODEY = (17, 27, 22) # Microstep Resolution GPIO Pins

CW = 1     # Clockwise Rotation
CCW = 0    # Counterclockwise Rotation
SPR = 200  # Steps per Revolution (360 / 7.5)
HYSTERESIS = 15     # Motor movement insensitivity

setup2.setupGPIO(MODEX, MODEY, DIRX, DIRY, STEPX, STEPY, CW, SPR)

# VARIABLES
usteps = 4
step_count = SPR * usteps
delay = 1/step_count

current_angleX = 0 # [0-6400]
current_angleY = 0 # [0-6400]
angleX = 0        # [0-6400]
angleY = 0        # [0-6400]
i=0

def findShortestRoute(current, target):
  if 360-current < current:
        return 360-current+target
  else:
        return current+target

def rotateX():
   global current_angleX, angleX, DIRX, STEPX, delay
   #angleX = 400
   while True:
      #HORIZONTAL AXIS
      print ("X:", angleX)
      if angleX > current_angleX+HYSTERESIS:
            current_angleX += 1
            GPIO.output(DIRX, CW)
            GPIO.output(STEPX, GPIO.HIGH)
      elif angleX < current_angleX-HYSTERESIS:
         current_angleX -= 1
         GPIO.output(DIRX, CCW)
         GPIO.output(STEPX, GPIO.HIGH)
      
      sleep(delay)
      GPIO.output(STEPX,GPIO.LOW)
      sleep(delay)


def rotateY():
   global current_angleY, angleY, DIRY, STEPY, delay
   while True:
      #VERTICAL AXIS
      print ("Y:", angleY)
      if angleY > current_angleY+HYSTERESIS:
            current_angleY += 1
            GPIO.output(DIRY, CW)
            GPIO.output(STEPY, GPIO.HIGH)
      elif angleY < current_angleY-HYSTERESIS:
         current_angleY -= 1
         GPIO.output(DIRY, CCW)
         GPIO.output(STEPY, GPIO.HIGH)

      sleep(delay)
      GPIO.output(STEPX,GPIO.LOW)
      sleep(delay)

	  
def rotateXY():
	global current_angleX, angleX, DIRX, STEPX, delay
	global current_angleY, angleY, DIRY, STEPY
	
	# HORIZONTAL AXIS
	if angleX > current_angleX+HYSTERESIS:
		current_angleX += 1
		GPIO.output(DIRX, 1)
		GPIO.output(STEPX, GPIO.HIGH)
	elif angleX < current_angleX-HYSTERESIS:
		current_angleX -= 1
		GPIO.output(DIRX, 0)
		GPIO.output(STEPX, GPIO.HIGH)

	# VERTICAL AXIS
	if angleY > current_angleY+HYSTERESIS:
		current_angleY += 1
		GPIO.output(DIRY, 1)
		GPIO.output(STEPY, GPIO.HIGH)
	elif angleY < current_angleY-HYSTERESIS:
		current_angleY -= 1
		GPIO.output(DIRY, 0)
		GPIO.output(STEPY, GPIO.HIGH)

	sleep(delay)
	GPIO.output(STEPX, GPIO.LOW)
	GPIO.output(STEPY, GPIO.LOW)
	sleep(delay)

def receivePosition():
	global sock, step_count, angleX, angleY
	while True:
		data, addr = sock.recvfrom(1024) # buffer size is 1024 bytes

		angleX = int(float(data.split()[5])/6.28*step_count)
		angleY = int(float(data.split()[3])/6.28*step_count)

		print (data)
		print ("X: ", angleX)
		print ("Y: ", angleY, "\n")

pos = threading.Thread(target=receivePosition)
pos.Daemon = True
pos.start()

try:
  while True:
    rotateXY()
except (KeyboardInterrupt, SystemExit):
  sock.close()
  GPIO.cleanup()
