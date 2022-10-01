package com.example.learngurbani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

//user can choose which statistic to look at the leaderboard for

public class LeaderboardFilters extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_filters);

        getSupportActionBar().setTitle("Leaderboard Filters");
    }

    //if user wants to see high score, then pass high score as a string and the leaderboard class will use this to pull this data from firebase
    public void goHighScore(View view) {
        Intent fcSummary = new Intent(LeaderboardFilters.this, LeaderboardsActivity.class);
        fcSummary.putExtra("Filter", "highScore");
        startActivity(fcSummary);
    }

    public void goAvgScore(View view) {
        Intent fcSummary = new Intent(LeaderboardFilters.this, LeaderboardsActivity.class);
        fcSummary.putExtra("Filter", "avgScore");
        startActivity(fcSummary);
    }

    public void goGamesPlayed(View view) {
        Intent fcSummary = new Intent(LeaderboardFilters.this, LeaderboardsActivity.class);
        fcSummary.putExtra("Filter", "gamesPlayed");
        startActivity(fcSummary);
    }

    public void goTotalScore(View view) {
        Intent fcSummary = new Intent(LeaderboardFilters.this, LeaderboardsActivity.class);
        fcSummary.putExtra("Filter", "totalScore");
        startActivity(fcSummary);
    }

    //setting the back button
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LeaderboardFilters.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}