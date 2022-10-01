package com.example.learngurbani;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//signup screen, user can sign up with unique username, email, password

public class SignUpActivity extends AppCompatActivity {

    EditText usernameET, passwordET, reenterPasswordET, emailET;
    TextView loginTextView;
    Button signUpButton;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    private long backPressedTime;
    FirebaseFirestore fStore;
    String userID;
//    ArrayList<UsersModel> userArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle("Sign Up");


        //setting the views
        usernameET = findViewById(R.id.username);
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        reenterPasswordET = findViewById(R.id.reenterPassword);

        loginTextView = findViewById(R.id.loginText);

        signUpButton = findViewById(R.id.btnSignUp);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        progressBar = findViewById(R.id.progressBar);

        //list of users to check if username entered is a duplicate
//        userArrayList = new ArrayList<UsersModel>();


        //if the current user is logged in from the previous session, go straight to the home screen
        if (fAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        //setting up the sign up button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = usernameET.getText().toString();
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String reenterPassword = reenterPasswordET.getText().toString();

                //listening for all users
                ArrayList<String> usernames = new ArrayList<>();
                fStore.collection("users")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    return;
                                }

                                //for every user in firebase, add their username to an arraylist so you can check for duplicates
                                for (DocumentChange dc : value.getDocumentChanges()) {
                                    if (dc.getType() == DocumentChange.Type.ADDED) {
//                                        userArrayList.add(dc.getDocument().toObject(UsersModel.class));
                                        usernames.add(dc.getDocument().toObject(UsersModel.class).username);
                                    }

                                }

                                //validation of all of the fields

                                //this could be neater instead of nested ifs

                                //if username is not a duplicate
                                if (!usernames.contains(username)) {
                                    //if fields are not empty
                                    if (!username.equals("") || !email.equals("") || !password.equals("") || !reenterPassword.equals("")) {
                                        //if the password and re-enter password match
                                        if (password.equals(reenterPassword)) {
                                            //if username length is appropriate
                                            if (username.length() > 3 && username.length() < 17) {
                                                //if email is not empty
                                                if (!email.isEmpty()) {
                                                    //if password length is appropriate
                                                    if (password.length() > 5 && password.length() < 17) {

                                                        //progress bar for better user interface
                                                        progressBar.setVisibility(View.VISIBLE);

                                                        //inbuilt firebase method to create a new user with an email and password
                                                        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                                if (task.isSuccessful()) {
                                                                    //show user successful process
                                                                    Toast.makeText(SignUpActivity.this, "Registered successfully, Logged in", Toast.LENGTH_SHORT).show();

                                                                    userID = fAuth.getCurrentUser().getUid();
                                                                    DocumentReference documentReference = fStore.collection("users").document(userID);

                                                                    //initialise all of the fields for the new user to be stored in firebase
                                                                    ArrayList<Long> listOfScores = new ArrayList<Long>();
                                                                    ArrayList<Long> listOfResults = new ArrayList<Long>();
                                                                    long avgScore = 0;
                                                                    long avgResult = 0;
                                                                    long gamesPlayed = 0;
                                                                    long highScore = 0;
                                                                    long totalScore = 0;
                                                                    long totalCorrectAnswers = 0;
                                                                    long totalWrongAnswers = 0;
                                                                    long avgWrongAnswers = 0;
                                                                    HashMap<String, String> wordsLearnt = new HashMap<String, String>();
                                                                    long numOfWordsLearnt = 0;
                                                                    ArrayList<String> qsAnswered = new ArrayList<String>();

                                                                    //upload the new user
                                                                    Map<String, Object> user = new HashMap<>();
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


                                                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                        }
                                                                    });
                                                                    //move to home screen if successful
                                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();


                                                                } else {
                                                                    //helpful toasts to let the user know why fields may not be valid
                                                                    Toast.makeText(SignUpActivity.this, "Please use a valid email address that is not already in use", Toast.LENGTH_SHORT).show();
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        Toast.makeText(SignUpActivity.this, "Password must be 6 - 16 characters long", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(SignUpActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(SignUpActivity.this, "Username must be 4 - 16 characters long", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Passwords not matching", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });

        //if user already has an account, they can go to the login screen
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    //setting the back button
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finish();
        } else {
            Toast.makeText(SignUpActivity.this, "Press back once more to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

}