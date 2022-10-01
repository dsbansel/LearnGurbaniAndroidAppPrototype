package com.example.learngurbani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//user can choose glossary of all terms OR glossary of terms they have learnt with flashcards

public class GlossaryCategories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glossary_categories);

        getSupportActionBar().setTitle("Glossary");
    }

    //go to all words glossary
    public void goAllGlossary(View view) {
        Intent intent = new Intent(GlossaryCategories.this, GlossaryActivity.class);
        startActivity(intent);
        finish();
    }

    //go to personal glossary
    public void goPersonalGlossary(View view) {
        Intent intent = new Intent(GlossaryCategories.this, PersonalGlossary.class);
        startActivity(intent);
        finish();
    }

    //seting the back button
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GlossaryCategories.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}