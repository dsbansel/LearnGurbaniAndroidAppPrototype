package com.example.learngurbani;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

//home screen

public class MainActivity extends AppCompatActivity {

    private long backPressedTime;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userID;
    TextView loggedInAs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting up firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        //textview to show current user that is logged in
        loggedInAs = findViewById(R.id.loggedInAs);

        //listening from firebase
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    //set current username in the textview
                    String username = documentSnapshot.getString("username");
                    loggedInAs.setText("Logged in as: " + username);
                    if(username == null){
                        //if no user is logged in, take them to the log in screen
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                }
            }
        });
    }

    //setting the back button
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            //home screen has a dialog for exiting
            new AlertDialog.Builder(this)
                    .setTitle("Do you want to exit?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            setResult(RESULT_OK, new Intent().putExtra("Exit", true));
                            finish();
                        }
                    }).create().show();
        } else {
            Toast.makeText(MainActivity.this, "Press back once more to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    //depending on which button is pressed, start the associated activity

    public void startQuizMode(View view) {
        Intent quizIntent = new Intent(getApplicationContext(), QuizCategories.class);
        startActivity(quizIntent);
        finish();
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void openFlashcardActivity(View view) {
        startActivity(new Intent(getApplicationContext(), FlashcardActivity.class));
        finish();
    }

    public void openGlossaryActivity(View view) {
        startActivity(new Intent(getApplicationContext(), GlossaryCategories.class));
        finish();
    }

    public void startStats(View view) {
        startActivity(new Intent(getApplicationContext(), StatsActivity.class));
        finish();
    }

    public void openAchievementsActivity(View view) {
        startActivity(new Intent(getApplicationContext(), AchievementsActivity.class));
        finish();
    }

    public void openLeaderboards(View view) {
        startActivity(new Intent(getApplicationContext(), LeaderboardFilters.class));
        finish();
    }
}