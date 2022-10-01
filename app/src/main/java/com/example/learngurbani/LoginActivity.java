package com.example.learngurbani;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//login screen, user can log in with their email and password

public class LoginActivity extends AppCompatActivity {

    EditText passwordEditText, emailEditText;
    TextView signupTextView, forgotPasswordTextView;
    Button loginButton;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Log in");

        //setting up all of the views
        passwordEditText = findViewById(R.id.password2);
        emailEditText = findViewById(R.id.email2);

        signupTextView = findViewById(R.id.signupText);
        forgotPasswordTextView = findViewById(R.id.forgotPassword);

        loginButton = findViewById(R.id.btnLogIn);

        //setting up firebase
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar2);


        //allows user to go to sign up screen if they do not have an account to log into
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //allows user to enter their email and reset their password if they have forgotten it
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setting up the dialog to enter their email
                EditText resetMail = new EditText(view.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset password?");
                passwordResetDialog.setMessage("Enter your email to receive password reset link");
                passwordResetDialog.setView(resetMail);

                //the reset password process
                passwordResetDialog.setPositiveButton("Send link", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extracting the email they entered
                        String mail = resetMail.getText().toString();

                        //firebase has an inbuilt password reset process which automatically sends an email to the email address provided
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LoginActivity.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error! Reset link has not been sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                //cancel button
                passwordResetDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                //show reset dialog
                passwordResetDialog.create().show();
            }
        });

        //allowing a user to log in with their details
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //extracting the email and password from the edittexts
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                //if fields are not empty
                if (!email.equals("") || !password.equals("")) {
                    //show progress bar
                    progressBar.setVisibility(View.VISIBLE);

                    //using firebase's in built login with email and password function
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //if complete go to home screen
                                Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                //tell the user that there is some error
                                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                        }
                    });
                    //ask user to ensure fields aren't empty
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //setting the back button
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finish();
        } else {
            Toast.makeText(LoginActivity.this, "Press back once more to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}