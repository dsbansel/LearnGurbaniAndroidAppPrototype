package com.example.learngurbani;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import static java.lang.Thread.sleep;

//splash screen that shows upon opening the app

public class SplashScreen extends AppCompatActivity {

    TextView txtLogoCaption;
    ImageView imgViewLogo;

    private final static int EXIT_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //setting the views
        txtLogoCaption = findViewById(R.id.logoCaptionSplash);
        imgViewLogo = findViewById(R.id.imgLogoSplash);

        //setting the animation
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.transition);
        imgViewLogo.setAnimation(animation);
        txtLogoCaption.setAnimation(animation);

        //new thread for the loading screen
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //time loading screen lasts
                    sleep(2000);
                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    GoToMainMenu();
                }
            }
        });
        thread.start();

    }

    private void GoToMainMenu() {
        //go to sign up screen
        //sign up screen realises if user is already logged in and then goes straight to main menu
        startActivityForResult(new Intent(SplashScreen.this,SignUpActivity.class),EXIT_CODE);
    }

    //allowing the app to move to the next activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EXIT_CODE){
            if(resultCode == RESULT_OK){
                if(data.getBooleanExtra("EXIT",true)){
                    finish();
                }
            }
        }
    }

    //if this screen stops, kill the activity
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}