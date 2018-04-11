import RPi.GPIO as GPIO

RESOLUTION = {'Full': (0, 0, 0),
              'Half': (1, 0, 0),
              '1/4': (0, 1, 0),
              '1/8': (1, 1, 0),
              '1/16': (0, 0, 1),
              '1/32': (1, 0, 1)}
def setupGPIO(MODE, DIR, STEP, CW, CCW, SPR):
  GPIO.setmode(GPIO.BCM)
  GPIO.setup(DIR, GPIO.OUT)
  GPIO.setup(STEP, GPIO.OUT)
  GPIO.setup(MODE, GPIO.OUT)
  GPIO.output(DIR, CW)
  GPIO.output(MODE, RESOLUTION['1/32'])
