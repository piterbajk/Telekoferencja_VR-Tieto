package com.bibip.vrplayer_01;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;
import android.widget.EditText;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity implements OnClickListener {

    EditText leftUrl;
    //EditText rightUrl;
    Button playButton;

    Intent videoViewerIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        playButton = (Button) findViewById(R.id.playButton);
        leftUrl = (EditText) this.findViewById(R.id.leftUrl);
        //rightUrl = (EditText) this.findViewById(R.id.rightUrl);


        videoViewerIntent = new Intent(this, video_viewer.class);
        videoViewerIntent.putExtra("leftUrl",leftUrl.getText().toString());

        playButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.playButton:
                startActivity(videoViewerIntent);
                break;
        }
    }
}
