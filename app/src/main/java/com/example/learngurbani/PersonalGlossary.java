package com.example.learngurbani;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

//glossary of terms learnt in flashcards by the current user

public class PersonalGlossary extends AppCompatActivity {

    //list view to display words
    private ListView pGlossaryList;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    HashMap<String, ArrayList<String>> wordsLearnt = new HashMap<String, ArrayList<String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_glossary);

        getSupportActionBar().setTitle("Personal Glossary");

        pGlossaryList = findViewById(R.id.personalGlossaryListView);

        //setting up firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        //pulling (from firebase) the list of words that the current user has learnt
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    //storing the words they have learnt as a hashmap
                    wordsLearnt = (HashMap<String, ArrayList<String>>) documentSnapshot.get("wordsLearnt");

                    //adapter for the listview, inserting the list of words
                    SimpleAdapter adapter = new SimpleAdapter(PersonalGlossary.this, extractWordsAndEng(wordsLearnt), R.layout.list_item,
                            new String[]{"word", "english"},
                            new int[]{R.id.text1, R.id.text2});

                    pGlossaryList.setAdapter(adapter);

                    //if a word is clicked on, start a new activity displaying extra details about the word in the form of a flashcard
                    pGlossaryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            //getting the data of the word that has been clicked on
                            View v = pGlossaryList.getChildAt(i);
                            TextView tx = (TextView) view.findViewById(R.id.text1);
                            String currentWord = tx.getText().toString();

                            //pushing all the data for the current word to the next activity
                            Intent fcSummary = new Intent(PersonalGlossary.this, WordDetails.class);

                            //"personalGlossary" string tells the enxt activity to return to Glossary rather than Personal Glossary afterwards
                            fcSummary.putExtra("Activity", "personalGlossary");
                            fcSummary.putExtra("Word", currentWord);
                            fcSummary.putExtra("Details", wordsLearnt.get(currentWord));
                            startActivity(fcSummary);
                            finish();
                        }
                    });
                }
            }
        });


    }

    //taking hashmap of words with their details and formatting them as a list to fit in the listview with the word + english definition
    private List<Map<String, String>> extractWordsAndEng(HashMap<String, ArrayList<String>> learntWords) {
        List<Map<String, String>> wordAndEng = new ArrayList<Map<String, String>>();

        //sorting the words alphabetically
        Map<String, ArrayList<String>> sortedLearntWords = new TreeMap<String, ArrayList<String>>(learntWords);

        //for every item in the hashmap, extract the word plus the english translation
        for (String i : sortedLearntWords.keySet()) {
            Map<String, String> item = new HashMap<String, String>(2);
            item.put("word", i);
            item.put("english", sortedLearntWords.get(i).get(0));
            wordAndEng.add(item);
        }
        return wordAndEng;
    }

    //being able to search for words in the glossary
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        //setting the search bar
        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        //querying the listview
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            //whenever the query changes eg type/remove a letter
            @Override
            public boolean onQueryTextChange(String queriedText) {
                //intialising a listview to display the queried data
                List<Map<String, String>> wordAndEng = new ArrayList<Map<String, String>>();

                //alphabetically sorted
                Map<String, ArrayList<String>> sortedLearntWords = new TreeMap<String, ArrayList<String>>(wordsLearnt);
                for (String i : sortedLearntWords.keySet()) {
                    //need to show word plus english translation
                    Map<String, String> item = new HashMap<String, String>(2);

                    //if the word/transliteration/english translation matches the query then add it to the listview
                    if (i.toLowerCase().startsWith(queriedText.toLowerCase())
                            || sortedLearntWords.get(i).get(0).toLowerCase().startsWith(queriedText.toLowerCase())
                            || sortedLearntWords.get(i).get(1).toLowerCase().startsWith(queriedText.toLowerCase())) {
                        item.put("word", i);
                        item.put("english", sortedLearntWords.get(i).get(0));
                        wordAndEng.add(item);
                    }
                }

                //adapter to set the listview with all of the above data
                SimpleAdapter adapter = new SimpleAdapter(PersonalGlossary.this, wordAndEng, R.layout.list_item,
                        new String[]{"word", "english"},
                        new int[]{R.id.text1, R.id.text2});

                pGlossaryList.setAdapter(adapter);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //setting the back button
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PersonalGlossary.this, GlossaryCategories.class);
        startActivity(intent);
        finish();
    }
}