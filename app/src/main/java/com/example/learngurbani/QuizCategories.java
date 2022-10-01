package com.example.learngurbani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

//user can choose which quiz category they want to play


public class QuizCategories extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_categories);
        getSupportActionBar().setTitle("Quiz Categories");

        //setting up firebase to send the average score to the next activity

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();
    }

    //for each category, send the category name as a string and the quiz activity uses this string to pull from the SQLite database as appropriate
    public void miscQuiz(View view) {
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    //need to send average score so the quiz questions/difficulties can be generated based on current user ability
                    //probably not the most efficient way of implementing this
                    long avgScore = (long) documentSnapshot.get("avgScore");
                    Intent intent = new Intent(QuizCategories.this, QuizActivity.class);
                    intent.putExtra("Category", "Misc");
                    intent.putExtra("avgScore", avgScore);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /*
    I've set all the categories just to go to the misc one for the time being
    but once you add more questions to each category then set them to go to that particular category
     */

    public void jsQuiz(View view) {
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    long avgScore = (long) documentSnapshot.get("avgScore");
                    Intent intent = new Intent(QuizCategories.this, QuizActivity.class);
//                    intent.putExtra("Category", "JapjiSahib");
                    intent.putExtra("Category", "Misc");
                    intent.putExtra("avgScore", avgScore);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void csQuiz(View view) {
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    long avgScore = (long) documentSnapshot.get("avgScore");
                    Intent intent = new Intent(QuizCategories.this, QuizActivity.class);
//                    intent.putExtra("Category", "ChaupaiSahib");
                    intent.putExtra("Category", "Misc");
                    intent.putExtra("avgScore", avgScore);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void animalsQuiz(View view) {
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    long avgScore = (long) documentSnapshot.get("avgScore");
                    Intent intent = new Intent(QuizCategories.this, QuizActivity.class);
//                    intent.putExtra("Category", "Animals");
                    intent.putExtra("Category", "Misc");
                    intent.putExtra("avgScore", avgScore);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void natureQuiz(View view) {
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    long avgScore = (long) documentSnapshot.get("avgScore");
                    Intent intent = new Intent(QuizCategories.this, QuizActivity.class);
//                    intent.putExtra("Category", "Nature");
                    intent.putExtra("Category", "Misc");
                    intent.putExtra("avgScore", avgScore);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    //setting the back button
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(QuizCategories.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}