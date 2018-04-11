import gi
gi.require_version('Gst', '1.0')
from gi.repository import Gst, GObject, GLib

pipeline = None
bus = None
message = None

HOST_IP = "192.168.1.83" # <-- ANDROID DEVICE IP
WIDTH = int(640)
HEIGHT = int(480)


# initialize GStreamer
Gst.init(None)

# video pipelines
cam1 = "v4l2src device=/dev/video0 \
        ! image/jpeg ,width={},height={}, framerate=20/1 \
        ! rtpjpegpay \
        ! udpsink host={} port=5000 ".format(WIDTH, HEIGHT, HOST_IP)

cam2 = "v4l2src device=/dev/video1 \
        ! image/jpeg ,width={},height={}, framerate=20/1 \
        ! rtpjpegpay \
        ! udpsink host={} port=5001 ".format(WIDTH, HEIGHT, HOST_IP)

# audio pipelines
mic1 = "alsasrc device=plughw:1,0 \
        ! audio/x-raw \
        ! mulawenc \
        ! rtppcmupay \
        ! udpsink host={} port=5100 ".format(HOST_IP)

mic2 = "alsasrc device=plughw:0,0 \
        ! audio/x-raw \
        ! mulawenc \
        ! rtppcmupay \
        ! udpsink host={} port=5101 ".format(HOST_IP)

# build the pipeline from multiple sources
pipeline = Gst.parse_launch(cam1 + cam2 + mic1 + mic2)

# start playing
pipeline.set_state(Gst.State.PLAYING)

# wait until EOS or error
bus = pipeline.get_bus()
msg = bus.timed_pop_filtered(
    Gst.CLOCK_TIME_NONE,
    Gst.MessageType.ERROR | Gst.MessageType.EOS
)

# free resources
pipeline.set_state(Gst.State.NULL)
