package com.bibip.vrplayer_01;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

public class video_viewer extends Activity{


    VideoView videoViewerLeft;
    //VideoView videoViewerRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_viewer);


        videoViewerLeft = (VideoView) this.findViewById(R.id.videoViewerLeft);
        //videoViewerRight = (VideoView) this.findViewById(R.id.videoViewerRight);
        playVideo(getIntent().getStringExtra("leftUrl"));
    }


    private void playVideo(String ofLeftUrl /*,String ofRightUrl*/)
    {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoViewerLeft);
        videoViewerLeft.setMediaController(mediaController);
        videoViewerLeft.setVideoURI(Uri.parse(ofLeftUrl));
        //videoViewerRight.setVideoURI(Uri.parse(ofRightUrl));
        videoViewerLeft.start();
        //videoViewerLeft.start();
    }
}
