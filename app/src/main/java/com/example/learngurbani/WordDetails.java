package com.example.learngurbani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

//after a word is clicked on in the glossary, this screen shows additional info about the word

public class WordDetails extends AppCompatActivity {
    TextView word, translit, eng, punj, pangti, origin;
    ImageView img;
    Button playAudio;
    private String imgFile, audioFile;

    String previousActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_details);

        //setting the views
        word = findViewById(R.id.wdWord);
        eng = findViewById(R.id.wdEngTransl);
        translit = findViewById(R.id.wdTranslit);
        origin = findViewById(R.id.wdOrigin);
        punj = findViewById(R.id.wdPunj);
        pangti = findViewById(R.id.wdPangti);

        img = findViewById(R.id.wdImage);
        playAudio = findViewById(R.id.wdPlayAudio);

        //getting all the data about the word from the previous activity
        Intent intent = getIntent();

        String sentWord = (String) intent.getStringExtra("Word");
        previousActivity = (String) intent.getStringExtra("Activity");
        ArrayList<String> details = (ArrayList<String>) intent.getSerializableExtra("Details");

        //showing the word and its english definition in the appbar
        getSupportActionBar().setTitle(sentWord + " - " + details.get(0));

        //setting the views as the appropriate data
        word.setText(sentWord);
        eng.setText(details.get(0));
        translit.setText(details.get(1));
        origin.setText(details.get(2));
        punj.setText(details.get(3));
        pangti.setText(details.get(4));

        //get the filename for the image and audio
        imgFile = details.get(5);
        audioFile = details.get(6);

        //setting the imageview as the imagefile by setting it as bitmap
        img.setImageBitmap(getBitmapFromAssets(imgFile));

        //button for playing the audio from the associated audio asset
        playAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer m = new MediaPlayer();
                try {
                    if (m.isPlaying()) {
                        m.stop();
                        m.release();
                        m = new MediaPlayer();
                    }

                    AssetFileDescriptor descriptor = getAssets().openFd(audioFile);
                    m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                    descriptor.close();

                    m.prepare();
                    m.setVolume(1f, 1f);
                    m.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    //read an imagefile and display it as an imageview
    private Bitmap getBitmapFromAssets(String fileName) {
        AssetManager am = getAssets();
        InputStream is = null;
        try {
            is = am.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }


    //setting the back button, if they were previously on personal glossary go back there, else return to the 'allGlossary'
    @Override
    public void onBackPressed() {
        Intent intent;
        if(previousActivity.equals("personalGlossary")){
            intent = new Intent(WordDetails.this, PersonalGlossary.class);
        } else {
            intent = new Intent(WordDetails.this, GlossaryActivity.class);
        }
        startActivity(intent);
        finish();
    }
}