package com.example.learngurbani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//gives the user a summary of all of the words learning during their flashcards session

public class FlashcardSummaryActivity extends AppCompatActivity {

    private Button mainMenu;
    private TextView word1, meaning1, translit1, word2, meaning2, translit2, word3, meaning3, translit3, word4, meaning4, translit4, word5, meaning5, translit5;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_summary);

        getSupportActionBar().setTitle("Flashcard Summary");

        //setting up the firebase database
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        //setting all of the textviews, this could be improved with a listview/recyclerview maybe
        mainMenu = findViewById(R.id.fc_summary_btn_mainMenu);
        word1 = findViewById(R.id.fcword1);
        meaning1 = findViewById(R.id.fcword1meaning);
        translit1 = findViewById(R.id.fcword1translit);
        word2 = findViewById(R.id.fcword2);
        meaning2 = findViewById(R.id.fcword2meaning);
        translit2 = findViewById(R.id.fcword2translit);
        word3 = findViewById(R.id.fcword3);
        meaning3 = findViewById(R.id.fcword3meaning);
        translit3 = findViewById(R.id.fcword3translit);
        word4 = findViewById(R.id.fcword4);
        meaning4 = findViewById(R.id.fcword4meaning);
        translit4 = findViewById(R.id.fcword4translit);
        word5 = findViewById(R.id.fcword5);
        meaning5 = findViewById(R.id.fcword5meaning);
        translit5 = findViewById(R.id.fcword5translit);

        //get the data sent from the flashcard activity
        Intent intent = getIntent();

        //list of words they learnt
        ArrayList<String> words = (ArrayList<String>) intent.getSerializableExtra("words");
        //hashmap of words plus all of the word's details
        HashMap<String, ArrayList<String>> wordsPlusDetails = (HashMap<String, ArrayList<String>>) intent.getSerializableExtra("LearntWords");

        //setting each textview with the relevant data from the arraylist and hashmap
        word1.setText(words.get(0));
        meaning1.setText(wordsPlusDetails.get(words.get(0)).get(0));
        translit1.setText(wordsPlusDetails.get(words.get(0)).get(1) + " ");
        word2.setText(words.get(1));
        meaning2.setText(wordsPlusDetails.get(words.get(1)).get(0));
        translit2.setText(wordsPlusDetails.get(words.get(1)).get(1) + " ");
        word3.setText(words.get(2));
        meaning3.setText(wordsPlusDetails.get(words.get(2)).get(0));
        translit3.setText(wordsPlusDetails.get(words.get(2)).get(1) + " ");
        word4.setText(words.get(3));
        meaning4.setText(wordsPlusDetails.get(words.get(3)).get(0));
        translit4.setText(wordsPlusDetails.get(words.get(3)).get(1) + " ");
        word5.setText(words.get(4));
        meaning5.setText(wordsPlusDetails.get(words.get(4)).get(0));
        translit5.setText(wordsPlusDetails.get(words.get(4)).get(1) + " ");

        //opening the firebase database so that the arraylist of words they have learnt can be updated
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    //inefficient way of pushing to the database by getting all of the data and re-uploading it
                    Map<String, Object> user = new HashMap<>();
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

                    //if any of the words they have learnt in the current flashcards has not been encountered yet, it is pushed to the arraylist in firebase
                    for (String i : wordsPlusDetails.keySet()) {
                        if (!wordsLearnt.containsKey(wordsPlusDetails.get(i))) {
                            wordsLearnt.put(i, wordsPlusDetails.get(i));
                        }
                    }

                    //storing the number of words they have learnt
                    long numOfWordsLearnt = wordsLearnt.size();

                    ArrayList<String> qsAnswered = (ArrayList<String>) documentSnapshot.get("qsAnswered");

                    //re-uploading everything back into firebase, probably not the best way of pushing data
                    user.put("username", username);
                    user.put("email", email);
                    user.put("scores", listOfScores);
                    user.put("results", listOfResults);
                    user.put("avgScore", avgScore);
                    user.put("avgResult", avgResult);
                    user.put("gamesPlayed", gamesPlayed);
                    user.put("highScore", highScore);
                    user.put("totalScore", totalScore);
                    user.put("totalCorrectAnswers", totalCorrectAnswers);
                    user.put("totalWrongAnswers", totalWrongAnswers);
                    user.put("avgWrongAnswers", avgWrongAnswers);
                    user.put("wordsLearnt", wordsLearnt);
                    user.put("numOfWordsLearnt", numOfWordsLearnt);
                    user.put("qsAnswered",qsAnswered);

                    documentReference.set(user);


                }
            }
        });


        //button to return to main menu
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FlashcardSummaryActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //setting the back button to return to the correct activity
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FlashcardSummaryActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}