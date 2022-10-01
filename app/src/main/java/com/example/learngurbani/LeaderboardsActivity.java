package com.example.learngurbani;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//leaderboard of all users for a particular statistic

public class LeaderboardsActivity extends AppCompatActivity {

    RecyclerView rvLeaderboard;
    FirebaseFirestore fStore;
    ArrayList<UsersModel> userArrayList;
    RVadapter rvAdapter;
    ProgressDialog pDialog;
    FirebaseAuth fAuth;
    String currentUserEmail;

    ArrayList<String> userEmails = new ArrayList<>();

    String filter;
    TextView whatFilter;
    String filterFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);

        //setting up firebase
        fAuth = FirebaseAuth.getInstance();
        currentUserEmail = fAuth.getCurrentUser().getEmail();

        //progress dialog to improve user experience
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Fetching Data...");
        pDialog.show();


        //setting up the leaderboard filter
        whatFilter = findViewById(R.id.whatFilter);

        Intent intent = getIntent();

        filter = (String) intent.getStringExtra("Filter");

        //using the full name so that the action bar can display the correct filter name
        if (filter.equals("avgScore")) {
            filterFullName = "Average Score";
        } else if (filter.equals("gamesPlayed")) {
            filterFullName = "Games Played";
        } else if (filter.equals("totalScore")) {
            filterFullName = "Total Score";
        } else if (filter.equals("highScore")) {
            filterFullName = "High Score";
        }

        whatFilter.setText(filterFullName);
        getSupportActionBar().setTitle("Leaderboards: " + filterFullName);


        //setting up the recycler view
        rvLeaderboard = findViewById(R.id.rvLeaderboards);
        rvLeaderboard.setHasFixedSize(true);
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(this));

        fStore = FirebaseFirestore.getInstance();
        userArrayList = new ArrayList<UsersModel>();
        rvAdapter = new RVadapter(LeaderboardsActivity.this, userArrayList, filter);

        rvLeaderboard.setAdapter(rvAdapter);

        EventChangeListener();

    }

    //listening for data to be inserted into the leaderboard
    private void EventChangeListener() {
        //listening from firebase via the filter selected in descending order
        fStore.collection("users").orderBy(filter, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            return;
                        }

                        //for every user, add a new user to the user arraylist using the UserModel class
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                userArrayList.add(dc.getDocument().toObject(UsersModel.class));

                                //arraylist of user emails lets me see the position of the current user in the leaderboard
                                userEmails.add(dc.getDocument().toObject(UsersModel.class).email);
                            }

                            //linking to recyclerview adapter
                            rvAdapter.notifyDataSetChanged();
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                        }

                        //finding the position of the current user in the leaderboard
                        int posOfCurrentUser = userEmails.indexOf(currentUserEmail);

                        //scroll down to the position of the current user
                        rvLeaderboard.smoothScrollToPosition(posOfCurrentUser);

                    }
                });
    }


    //setting the back button
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LeaderboardsActivity.this, LeaderboardFilters.class);
        startActivity(intent);
        finish();
    }
}