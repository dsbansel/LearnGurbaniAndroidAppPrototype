package com.example.learngurbani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

//Java class for initialising all of the achievements and calculating whether the user has achieved them

public class AchievementsActivity extends AppCompatActivity {

    //initialise all of the textviews
    TextView play5games, play10games, play25games, play50games, play100games,
            scoreAbove1000, scoreAbove1500, scoreAbove2000, scoreAbove2400,
            total100k, tenOutOf10, avgAbove1600, avgAbove8,
            learn10words, learn50words, learn100words,
            qs25, qs50, qs100;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        getSupportActionBar().setTitle("Achievements");

        //assigning all of the textviews
        play5games = findViewById(R.id.play5games);
        play10games = findViewById(R.id.play10games);
        play25games = findViewById(R.id.play25games);
        play50games = findViewById(R.id.play50games);
        play100games = findViewById(R.id.play100games);
        scoreAbove1000 = findViewById(R.id.highscoreAbove1000);
        scoreAbove1500 = findViewById(R.id.highscoreAbove1500);
        scoreAbove2000 = findViewById(R.id.highscore2000);
        scoreAbove2400 = findViewById(R.id.highscore2400);
        total100k = findViewById(R.id.totalScore100000);
        tenOutOf10 = findViewById(R.id.get10outof10);
        learn10words = findViewById(R.id.learn10Words);
        learn50words = findViewById(R.id.learn50Words);
        learn100words = findViewById(R.id.learn100Words);
        avgAbove1600 = findViewById(R.id.avgAbove1600);
        avgAbove8 = findViewById(R.id.avgAbove8);
        qs25 = findViewById(R.id.qs25);
        qs50 = findViewById(R.id.qs50);
        qs100 = findViewById(R.id.qs100);

        //initialising the FireBase database
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        //start a listener to read from the FireBase Database
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    //pulling all of the data from the database
                    //some variables are unused but are kept here in case new achievements are to be added
                    String username = documentSnapshot.getString("username");
                    String email = documentSnapshot.getString("email");
                    ArrayList<Long> listOfScores = (ArrayList<Long>) documentSnapshot.get("scores");
                    ArrayList<Long> listOfResults = (ArrayList<Long>) documentSnapshot.get("results");
                    long avgScore = ((long) documentSnapshot.get("avgScore"));
                    long avgResult = ((long) documentSnapshot.get("avgResult"));
                    long gamesPlayed = ((long) documentSnapshot.get("gamesPlayed"));
                    long highScore = ((long) documentSnapshot.get("highScore"));
                    long totalScore = ((long) documentSnapshot.get("totalScore"));
                    long totalCorrectAnswers = ((long) documentSnapshot.get("totalCorrectAnswers"));
                    long totalWrongAnswers = ((long) documentSnapshot.get("totalWrongAnswers"));
                    long avgWrongAnswers = ((long) documentSnapshot.get("avgWrongAnswers"));
                    HashMap<String, ArrayList<String>> wordsLearnt = (HashMap<String, ArrayList<String>>) documentSnapshot.get("wordsLearnt");
                    long numOfWordsLearnt = ((long) documentSnapshot.get("numOfWordsLearnt"));
                    ArrayList<String> qsAnswered = (ArrayList<String>) documentSnapshot.get("qsAnswered");

                    //set the colour of the achievement to green if threshold has been exceeded
                    //set colour to red if they are yet to achieve it
                    if (gamesPlayed >= 5) {
                        play5games.setTextColor(Color.GREEN);
                    } else {
                        play5games.setTextColor(Color.RED);
                    }

                    if (gamesPlayed >= 10) {
                        play10games.setTextColor(Color.GREEN);
                    } else {
                        play10games.setTextColor(Color.RED);
                    }

                    if (gamesPlayed >= 25) {
                        play25games.setTextColor(Color.GREEN);
                    } else {
                        play25games.setTextColor(Color.RED);
                    }

                    if (gamesPlayed >= 50) {
                        play50games.setTextColor(Color.GREEN);
                    } else {
                        play50games.setTextColor(Color.RED);
                    }
                    if (gamesPlayed >= 100) {
                        play100games.setTextColor(Color.GREEN);
                    } else {
                        play100games.setTextColor(Color.RED);
                    }

                    if (highScore > 1000) {
                        scoreAbove1000.setTextColor(Color.GREEN);
                    } else {
                        scoreAbove1000.setTextColor(Color.RED);
                    }
                    if (highScore > 1500) {
                        scoreAbove1500.setTextColor(Color.GREEN);
                    } else {
                        scoreAbove1500.setTextColor(Color.RED);
                    }
                    if (highScore > 2000) {
                        scoreAbove2000.setTextColor(Color.GREEN);
                    } else {
                        scoreAbove2000.setTextColor(Color.RED);
                    }
                    if (highScore > 2400) {
                        scoreAbove2400.setTextColor(Color.GREEN);
                    } else {
                        scoreAbove2400.setTextColor(Color.RED);
                    }

                    if (totalScore > 100000) {
                        total100k.setTextColor(Color.GREEN);
                    } else {
                        total100k.setTextColor(Color.RED);
                    }

                    if (listOfResults.contains((long) 10)) {
                        tenOutOf10.setTextColor(Color.GREEN);
                    } else {
                        tenOutOf10.setTextColor(Color.RED);
                    }

                    if (numOfWordsLearnt > 10) {
                        learn10words.setTextColor(Color.GREEN);
                    } else {
                        learn10words.setTextColor(Color.RED);
                    }
                    if (numOfWordsLearnt > 50) {
                        learn50words.setTextColor(Color.GREEN);
                    } else {
                        learn50words.setTextColor(Color.RED);
                    }
                    if (numOfWordsLearnt > 100) {
                        learn100words.setTextColor(Color.GREEN);
                    } else {
                        learn100words.setTextColor(Color.RED);
                    }

                    if (avgScore > 1600) {
                        avgAbove1600.setTextColor(Color.GREEN);
                    } else {
                        avgAbove1600.setTextColor(Color.RED);
                    }

                    if (avgResult > 8) {
                        avgAbove8.setTextColor(Color.GREEN);
                    } else {
                        avgAbove8.setTextColor(Color.RED);
                    }

                    if (qsAnswered.size() > 25) {
                        qs25.setTextColor(Color.GREEN);
                    } else {
                        qs25.setTextColor(Color.RED);
                    }
                    if (qsAnswered.size() > 50) {
                        qs50.setTextColor(Color.GREEN);
                    } else {
                        qs50.setTextColor(Color.RED);
                    }
                    if (qsAnswered.size() > 100) {
                        qs100.setTextColor(Color.GREEN);
                    } else {
                        qs100.setTextColor(Color.RED);
                    }
                }
            }
        });

    }

    //ensuring going back kills the current activity and starts a new activity
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AchievementsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}