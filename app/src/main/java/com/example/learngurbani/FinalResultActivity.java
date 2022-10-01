package com.example.learngurbani;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Screen shown after a quiz is completed
public class FinalResultActivity extends AppCompatActivity {

    TextView txtScore;
    TextView txtAvgScore;
    TextView txtPoints;
    TextView txtHighScore;
    TextView txtAvgResult;

    Button btRestartQuiz;
    Button btMainMenu;

    private long backPressedTime;

    private Handler handler = new Handler();

    ArrayList<Long> listOfScores = new ArrayList<Long>();

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result);

        getSupportActionBar().setTitle("Final Result");

        //setting all of the textviews and buttons
        btRestartQuiz = findViewById(R.id.result_btn_playAgain);
        btMainMenu = findViewById(R.id.result_btn_mainMenu);
        txtPoints = findViewById(R.id.result_txt_points);
        txtScore = findViewById(R.id.result_txt_score);
        txtAvgScore = findViewById(R.id.result_txt_averageScore);
        txtHighScore = findViewById(R.id.result_txt_highScore);
        txtAvgResult = findViewById(R.id.result_txt_averageResult);

        //button to play again
        btRestartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinalResultActivity.this, QuizCategories.class);
                startActivity(intent);
            }
        });

        //button to return to main menu
        btMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinalResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //receiving data from the quiz activity about the user's results
        Intent intent = getIntent();

        int score = intent.getIntExtra("TotalScore", 0);
        int correctAnswers = intent.getIntExtra("CorrectAnswers", 0);
        int wrongAnswers = intent.getIntExtra("WrongAnswers", 0);
        int totalQs = intent.getIntExtra("totalQs", 0);

        //displaying the user's results in textviews
        txtPoints.setText("  Final Score: " + String.valueOf(score));
        txtScore.setText("Result: " + correctAnswers + "/" + totalQs);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();
        Map<String, Object> user = new HashMap<>();

        //pulling additional data from the Firebase database so the user can see their high/avg score
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    long avgScore = (long) documentSnapshot.get("avgScore");
                    txtAvgScore.setText("Average Score: " + avgScore);
                    long highScore = (long) documentSnapshot.get("highScore");
                    txtHighScore.setText("High Score: " + highScore);
                    ArrayList<Long> listOfResults = (ArrayList<Long>) documentSnapshot.get("results");

                    //displaying the average result as a 2dp float rather than an integer, integer not precise enough when result is 0-10
                    float sum = 0;
                    for (int i = 0; i < listOfResults.size(); i++) {
                        sum += listOfResults.get(i);
                    }
                    float avgResultFloat;
                    if (listOfResults.size() <= 1) {
                        avgResultFloat = sum;
                    } else {
                        avgResultFloat = (float) (sum / (listOfResults.size()));
                    }
                    String avgResultString = String.format("%.2f", avgResultFloat);
                    txtAvgResult.setText("Average Result: " + avgResultString + "/10");
                }
            }
        });


    }

    //ensuring going back kills the current activity and starts a new activity
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {

            Intent intent = new Intent(FinalResultActivity.this, MainActivity.class);
            startActivity(intent);

            finish();

        } else {
            Toast.makeText(FinalResultActivity.this, "Press Back Once More to Exit to Main Menu", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

}