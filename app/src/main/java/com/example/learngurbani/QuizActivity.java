package com.example.learngurbani;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//Quiz activity for playing a 10-question quiz

/*
Code adapted from a quiz app tutorial by Dino Code Academy on YouTube
link to YouTube tutorial playlist: https://www.youtube.com/playlist?list=PLBcjj47fhbjXhYscBCEBY8-NLhUey4Rsm
link to github for the tutorial: https://github.com/VijayKumar4003/MainGoQuizAgain
 */

public class QuizActivity extends AppCompatActivity {

    //all of the radiobuttons
    private RadioGroup rbGroup;
    private RadioButton rbAnswer1;
    private RadioButton rbAnswer2;
    private RadioButton rbAnswer3;
    private RadioButton rbAnswer4;
    private Button submitButton;

    //all of the textviews
    private TextView txtQuestions;
    private TextView txtScore;
    private TextView txtQuestionNum;
    private TextView txtTimer;
    private TextView txtPoints;
    private TextView qDifficulty;

    //list of questions
    private ArrayList<Questions> questionList = new ArrayList<Questions>();

    //current question number
    private int questionCounter;
    //total num of questions
    private int questionTotalCount;
    //current question
    private Questions currentQuestion;
    //whether question has been answered
    private boolean answered;

    //all of the dialogs
    private ClickStartWhenReadyDialog clickStartWhenReadyDialog;
    private CorrectDialog correctDialog;
    private WrongDialog wrongDialog;

    //time to answer a question
    private static final long COUNTDOWN_IN_MILLIS = 21000;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private long backPressedTime;

    private String categoryValue;

    private int numOfCorrectAnswers = 0;
    private int numOfWrongAnswers = 0;

    int totalScore = 0;
    int timerBonus;
    int points = 0;
    long averageScore;

    //firebase database
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    private final Handler handler = new Handler();

    //keeping track of all of the questions answered so they can be added to firebase later on so the user can see how many different questions they have encountered
    ArrayList<String> allQsAnswered = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_screen);

        getSupportActionBar().setTitle("Quiz");

        //setting up firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        //initialising dialogs
        clickStartWhenReadyDialog = new ClickStartWhenReadyDialog(this);
        correctDialog = new CorrectDialog(this);
        wrongDialog = new WrongDialog(this);

        clickStartWhenReadyDialog.clickStartWhenReadyDialog(this);

        //getting the category and average score so that the quiz category can be set and the questions can be generated based on skill level and difficulties
        Intent intentCategory = getIntent();
        categoryValue = intentCategory.getStringExtra("Category");
        averageScore = intentCategory.getLongExtra("avgScore", 0);

        setupUI();
        fetchDB();

    }

    //set up all of the views
    private void setupUI() {
        txtQuestions = findViewById(R.id.questionContainer);
        txtScore = findViewById(R.id.score);
        txtQuestionNum = findViewById(R.id.questionNum);
        txtTimer = findViewById(R.id.timer);
        txtPoints = findViewById(R.id.points);
        qDifficulty = findViewById(R.id.qDifficulty);

        submitButton = findViewById(R.id.submitButton);
        rbGroup = findViewById(R.id.radioGroup);
        rbAnswer1 = findViewById(R.id.radioButton1);
        rbAnswer2 = findViewById(R.id.radioButton2);
        rbAnswer3 = findViewById(R.id.radioButton3);
        rbAnswer4 = findViewById(R.id.radioButton4);

    }

    //fetch the data from the SQLite database of questions
    private void fetchDB() {

        QuizDbHelper dbHelper = new QuizDbHelper(this);

        //arraylist for each difficulty
        ArrayList<Questions> questionListEasy = dbHelper.getAllQuestionsWithCategoryAndDifficulty(categoryValue, "Easy");
        ArrayList<Questions> questionListMed = dbHelper.getAllQuestionsWithCategoryAndDifficulty(categoryValue, "Medium");
        ArrayList<Questions> questionListHard = dbHelper.getAllQuestionsWithCategoryAndDifficulty(categoryValue, "Hard");

        Collections.shuffle(questionListEasy);
        Collections.shuffle(questionListMed);
        Collections.shuffle(questionListHard);

        //calculate how many questions to take from each arraylist above based on the user's average score AKA skill level
        //this algorithm is simple so can be improved in the future
        int numOfEasy, numOfMed, numOfHard;

        //if score is between a certain threshold, generate a certain number of easy/med/hard questions
        if (averageScore < 600) {
            numOfEasy = 8;
            numOfMed = 1;
            numOfHard = 1;
        } else if (averageScore < 1000) {
            numOfEasy = 6;
            numOfMed = 3;
            numOfHard = 1;
        } else if (averageScore < 1600) {
            numOfEasy = 4;
            numOfMed = 3;
            numOfHard = 3;
        } else if (averageScore < 2400) {
            numOfEasy = 2;
            numOfMed = 4;
            numOfHard = 4;
        } else {
            numOfEasy = 2;
            numOfMed = 2;
            numOfHard = 6;
        }

        //add the calculated number of questions to the main question list
        for (int i = 0; i < numOfEasy; i++) {
            questionList.add(questionListEasy.get(i));
        }

        for (int i = 0; i < numOfMed; i++) {
            questionList.add(questionListMed.get(i));
        }

        for (int i = 0; i < numOfHard; i++) {
            questionList.add(questionListHard.get(i));
        }

        //startQuiz();

    }

    //starting the quiz
    public void startQuiz() {

        //numnber of quiz questions
        questionTotalCount = 10;

        //shuffle the list of questions
        Collections.shuffle(questionList);

        //show the question and all of the potential answers
        showQuestions();

        //drawing each button background as appropriate when selected
        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1:
                        rbAnswer1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_option_selected));
                        rbAnswer2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.answer_buttons_background));
                        rbAnswer3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.answer_buttons_background));
                        rbAnswer4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.answer_buttons_background));

                        break;
                    case R.id.radioButton2:
                        rbAnswer1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.answer_buttons_background));
                        rbAnswer2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_option_selected));
                        rbAnswer3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.answer_buttons_background));
                        rbAnswer4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.answer_buttons_background));

                        break;

                    case R.id.radioButton3:
                        rbAnswer1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.answer_buttons_background));
                        rbAnswer2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.answer_buttons_background));
                        rbAnswer3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_option_selected));
                        rbAnswer4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.answer_buttons_background));

                        break;

                    case R.id.radioButton4:
                        rbAnswer1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.answer_buttons_background));
                        rbAnswer2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.answer_buttons_background));
                        rbAnswer3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.answer_buttons_background));
                        rbAnswer4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.when_option_selected));

                        break;

                }
            }
        });

        //when user submits their answer
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!answered) {
                    //if user has selected an answer then check solution
                    if (rbAnswer1.isChecked() || rbAnswer2.isChecked() || rbAnswer3.isChecked() || rbAnswer4.isChecked()) {
                        quizOperation();
                    } else {
                        //if user has not selected an answer yet
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    //after answer submitted this is run
    private void quizOperation() {

        //set answered to true
        answered = true;

        //cancel timer
        countDownTimer.cancel();

        //check the solution based on the answer selected
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNum = rbGroup.indexOfChild(rbSelected) + 1;

        checkSolution(answerNum, rbSelected);

    }

    //checking the solution
    private void checkSolution(int answerNum, RadioButton rbSelected) {

        //short delay before showing the correct answer
        int delayForShowingCorrect = 1500;

        //setting a points multiplier based on difficulty of the question
        int multiplier = 10;
        if (currentQuestion.getDifficulty().equals("Easy")) {
            multiplier = 10;
        } else if (currentQuestion.getDifficulty().equals("Medium")) {
            multiplier = 15;
        }
        if (currentQuestion.getDifficulty().equals("Hard")) {
            multiplier = 20;
        }

        //checking if the selected answer matches the answer for the question
        switch (currentQuestion.getAnswerNum()) {
            case 1:
                if (currentQuestion.getAnswerNum() == answerNum) {
                    //change color of button to green if correct
                    rbAnswer1.setBackground(ContextCompat.getDrawable(this, R.drawable.when_answer_correct));

                    //increment num of correct answers
                    numOfCorrectAnswers++;

                    //calculating points obtained based on difficulty and how fast they answered
                    points = multiplier * timerBonus;
                    //add to total score
                    totalScore += points;

                    //show correct dialog
                    correctDialog.correctDialog(points);

                } else {
                    //if incorrect, increment num of wrong answers
                    numOfWrongAnswers++;
                    //change the selected button to red
                    changeToIncorrectColour(rbSelected);

                    //show the wrong answer dialog plus the correct answer
                    String correctAnswer = (String) rbAnswer1.getText();
                    wrongDialog.wrongDialog(correctAnswer);

                }
                //implementing the delay before showing the next question
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showQuestions();
                    }
                }, delayForShowingCorrect);
                break;
                //repeat for every answer button, this could be coded more efficiently if the whole system was reconstructed
            case 2:
                if (currentQuestion.getAnswerNum() == answerNum) {
                    rbAnswer2.setBackground(ContextCompat.getDrawable(this, R.drawable.when_answer_correct));
                    numOfCorrectAnswers++;

                    points = multiplier * timerBonus;
                    totalScore += points;

                    correctDialog.correctDialog(points);

                } else {
                    numOfWrongAnswers++;
                    changeToIncorrectColour(rbSelected);

                    String correctAnswer = (String) rbAnswer2.getText();
                    wrongDialog.wrongDialog(correctAnswer);

                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showQuestions();
                    }
                }, delayForShowingCorrect);
                break;

            case 3:
                if (currentQuestion.getAnswerNum() == answerNum) {
                    rbAnswer3.setBackground(ContextCompat.getDrawable(this, R.drawable.when_answer_correct));
                    numOfCorrectAnswers++;

                    points = multiplier * timerBonus;
                    totalScore += points;

                    correctDialog.correctDialog(points);

                } else {
                    numOfWrongAnswers++;
                    changeToIncorrectColour(rbSelected);

                    String correctAnswer = (String) rbAnswer3.getText();
                    wrongDialog.wrongDialog(correctAnswer);

                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showQuestions();
                    }
                }, delayForShowingCorrect);
                break;

            case 4:
                if (currentQuestion.getAnswerNum() == answerNum) {
                    rbAnswer4.setBackground(ContextCompat.getDrawable(this, R.drawable.when_answer_correct));
                    numOfCorrectAnswers++;

                    points = multiplier * timerBonus;
                    totalScore += points;

                    correctDialog.correctDialog(points);

                } else {
                    numOfWrongAnswers++;
                    changeToIncorrectColour(rbSelected);

                    String correctAnswer = (String) rbAnswer4.getText();
                    wrongDialog.wrongDialog(correctAnswer);

                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showQuestions();
                    }
                }, delayForShowingCorrect);
                break;

        }


    }

    //change button to red if incorrect
    private void changeToIncorrectColour(RadioButton rbSelected) {
        rbSelected.setBackground(ContextCompat.getDrawable(this, R.drawable.when_answer_wrong));

    }

    //showing the question along with all of the answers
    private void showQuestions() {

        //resetting the interface

        rbGroup.clearCheck();

        rbAnswer1.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.answer_buttons_background));
        rbAnswer2.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.answer_buttons_background));
        rbAnswer3.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.answer_buttons_background));
        rbAnswer4.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.answer_buttons_background));

        //if the final question has not been reached
        if (questionCounter < questionTotalCount) {

            //get the current question and set the textviews with the associated data
            currentQuestion = questionList.get(questionCounter);
            txtQuestions.setText(currentQuestion.getQuestion());
            rbAnswer1.setText(currentQuestion.getOption1());
            rbAnswer2.setText(currentQuestion.getOption2());
            rbAnswer3.setText(currentQuestion.getOption3());
            rbAnswer4.setText(currentQuestion.getOption4());
            qDifficulty.setText("Difficulty: " + currentQuestion.getDifficulty());

            //add to all questions answered to be checked later
            allQsAnswered.add(currentQuestion.getQuestion());

            //move to next question
            questionCounter++;
            answered = false;

            //if final question reached
            if (questionCounter == questionTotalCount) {
                //change submit button to say Finish
                submitButton.setText("Confirm and Finish");
            } else {
                //if not final question then it should not say finish
                submitButton.setText("Confirm");
            }

            //show current question number
            txtQuestionNum.setText("Question: " + questionCounter + "/" + questionTotalCount);

            //show current result out of 10
            txtScore.setText("Score: " + numOfCorrectAnswers + "/" + 10);

            //show current points total
            txtPoints.setText("Points: " + totalScore);

            //start the timer
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startTimer();

        } else {
            //if final question then run the final result method after a short delay
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finalResult();
                }

            }, 500);
        }

    }

    //update the countdown every second
    private void updateCountDownText() {

        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        //formatting and displaying the time
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        txtTimer.setText(timeFormatted);

        //change the timer to red color text if only 5 seconds remaining
        if (timeLeftInMillis < 5000) {
            txtTimer.setTextColor(Color.RED);
        } else {
            txtTimer.setTextColor(Color.BLACK);
        }

        //if timer reaches 0, user can still answer but gets 0 points for a correct answer
        if (timeLeftInMillis == 0) {
            Toast.makeText(QuizActivity.this, "TIME UP! Even if your answer is correct you will get 0 points", Toast.LENGTH_SHORT).show();
            timerBonus = 0;
        }
    }

    //starting the timer
    private void startTimer() {
        //create a new countdown timer
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                //calculating a timer bonus based on the time remaining
                //if time remaining is 17 seconds, then bonus 170 points, for 12 seconds, it is 120 points etc
                timerBonus = (int) timeLeftInMillis / 1000;

                //update countdown
                updateCountDownText();
            }

            //when timer finishes
            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
            }
        }.start();
    }

    //when app is paused, cancel countdown
    @Override
    protected void onPause() {
        super.onPause();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

    }

    //when activity is destroyed, cancel countdown
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    //update data in firebase and display final result activity
    private void finalResult() {

        Map<String, Object> user = new HashMap<>();

        //listener for firebase
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    //update all of the user's data based on the current game
                    String username = documentSnapshot.getString("username");
                    String email = documentSnapshot.getString("email");

                    ArrayList<Long> listOfScores = (ArrayList<Long>) documentSnapshot.get("scores");
                    listOfScores.add((long) totalScore);

                    ArrayList<Long> listOfResults = (ArrayList<Long>) documentSnapshot.get("results");
                    listOfResults.add((long) numOfCorrectAnswers);

                    //calculate new average score and result
                    long sum = 0;
                    for (int i = 0; i < listOfScores.size(); i++) {
                        sum += listOfScores.get(i);
                    }
                    long avgScore;
                    if (listOfScores.size() <= 1) {
                        avgScore = sum;
                    } else {
                        avgScore = (sum / (listOfScores.size()));
                    }

                    sum = 0;
                    for (int i = 0; i < listOfResults.size(); i++) {
                        sum += listOfResults.get(i);
                    }
                    long avgResult;
                    if (listOfResults.size() <= 1) {
                        avgResult = sum;
                    } else {
                        avgResult = (sum / (listOfResults.size()));
                    }

                    long gamesPlayed = listOfScores.size();

                    long highScore = Collections.max(listOfScores);

                    long totalScore = ((long) documentSnapshot.get("totalScore")) + QuizActivity.this.totalScore;

                    long totalCorrectAnswers = ((long) documentSnapshot.get("totalCorrectAnswers")) + numOfCorrectAnswers;

                    long totalWrongAnswers = ((long) documentSnapshot.get("totalWrongAnswers")) + numOfWrongAnswers;

                    long avgWrongAnswers = questionTotalCount - avgResult;

                    HashMap<String, ArrayList<String>> wordsLearnt = (HashMap<String, ArrayList<String>>) documentSnapshot.get("wordsLearnt");

                    long numOfWordsLearnt = (long) documentSnapshot.get("numOfWordsLearnt");

                    //if they encounter any new questions, these are pushed to firebase, so the stats can keep track of how many different questions they have ever encountered
                    ArrayList<String> qsAnswered = (ArrayList<String>) documentSnapshot.get("qsAnswered");
                    for (String i : allQsAnswered) {
                        if (!qsAnswered.contains(i)) {
                            qsAnswered.add(i);
                        }
                    }

                    //pushing all the new data along with any unchanged data back into firebase
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
                    user.put("qsAnswered", qsAnswered);

                    documentReference.set(user);

                }
            }
        });


        //sending data about this game to be displayed in the final result activity
        Intent resultData = new Intent(QuizActivity.this, FinalResultActivity.class);

        resultData.putExtra("TotalScore", totalScore);
        resultData.putExtra("CorrectAnswers", numOfCorrectAnswers);
        resultData.putExtra("WrongAnswers", numOfWrongAnswers);
        resultData.putExtra("totalQs", questionTotalCount);

        startActivity(resultData);

        finish();

    }

    //setting up the back button
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            Intent intent = new Intent(QuizActivity.this, QuizCategories.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(QuizActivity.this, "Press back once more to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    //exit to main menu, to be used by the clickStartWhenReady dialog
    public void exitFromDialog() {
        Intent intent = new Intent(QuizActivity.this, QuizCategories.class);
        startActivity(intent);
        finish();
    }

}