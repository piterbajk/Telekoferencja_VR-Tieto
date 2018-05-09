package com.bibip.sensordatasender;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


import android.media.AudioManager;
import android.net.rtp.AudioCodec;
import android.net.rtp.AudioGroup;
import android.net.rtp.AudioStream;
import android.net.rtp.RtpStream;
import android.util.Log;



//               NIE UZYWANE ALE MOZE SIE JESZCZE PRZYDAC JAKBY TAMTEN SPOSOB PRZESYLANIA DZWIEKU ZAWIODŁ!!!!!!!!!!!!!!!!!!!!!!!

/**
 * Encapsulation of the rtp package from API 12
 * Not working :( Feel free to fix it !
 */
public class GenericAudioStream {

    public final static String TAG = "GenericAudioStream";

    private AudioStream audioStream;
    private AudioGroup audioGroup;
    private InetAddress destination;
    private int port;

    public GenericAudioStream() {
        try {
            Log.d("mama","czesc1");
            //AudioManager AM =new AudioManager();
            audioGroup = new AudioGroup();

            Log.d("mama","czesc2");
            audioGroup.setMode(AudioGroup.MODE_NORMAL);
            audioStream.join(audioGroup);
            Log.d("mama","czesc3");
            audioStream = new AudioStream(InetAddress.getLocalHost());
            Log.d("mama","czesc4");
            audioStream.setCodec(AudioCodec.AMR);
            Log.d("mama","czesc5");
            audioStream.setMode(RtpStream.MODE_SEND_ONLY);
            Log.d("mama","czesc6");
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            Log.d("mama",e.getMessage());
            e.printStackTrace();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            Log.d("mama",e.getMessage());
            e.printStackTrace();
        }
    }

    public void prepare() {

    }

    public void start() throws IllegalStateException {
        //audioStream.join(null);
        audioStream.associate(destination, port);
        audioStream.join(audioGroup);

    }

    public void stop() {
        audioStream.join(null);
        audioGroup.setMode(AudioGroup.MODE_ON_HOLD);
    }

    public void release() {
        audioStream.release();
        audioGroup = null;
    }

    public String generateSessionDescription() throws IllegalStateException, IOException {
        AudioCodec codec = audioStream.getCodec();
        Log.d(TAG, "Codec: rtmap:\""+codec.rtpmap+"\""+" ftmp:\""+codec.fmtp+"\" type:\""+codec.type+"\"");
        return "m=audio "+String.valueOf(getDestinationPort())+" RTP/AVP "+codec.type+"\r\n" +
                "a=rtpmap:"+codec.type+" "+codec.rtpmap+"\r\n" +
                "a=fmtp:"+codec.type+" "+codec.fmtp+";\r\n";
    }

    public void setTimeToLive(int ttl) {
        ;
    }

    public void setDestination(InetAddress dest, int dport) {
        this.destination = dest;
        this.port = dport;
    }

    public int getDestinationPort() {
        return port;
    }

    public int getLocalPort() {
        return audioStream.getLocalPort();
    }

    public int getSSRC() {
        return 0;
    }

    public boolean isStreaming() {
        return audioStream.isBusy();
    }

}