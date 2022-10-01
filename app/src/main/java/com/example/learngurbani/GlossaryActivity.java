package com.example.learngurbani;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//glossary page for searching and exploring words and finding out their meanings

public class GlossaryActivity extends AppCompatActivity {

    private ArrayList<Words> wordList;
    private GlossaryDbHelper dbHelper;

    //listview of the data to be displayed on this activity
    private ListView glossaryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glossary);

        getSupportActionBar().setTitle("Glossary");

        setupUI();
        fetchDB();

        //setting up the adapter for the listview, inserting all of the words in the database plus the english translations
        SimpleAdapter adapter = new SimpleAdapter(GlossaryActivity.this, extractWordsAndEng(wordList), R.layout.list_item,
                new String[]{"word", "english"},
                new int[]{R.id.text1, R.id.text2});

        glossaryList.setAdapter(adapter);

        //if a word is clicked on, start a new activity displaying extra details about the word in the form of a flashcard
        glossaryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the data of the word that has been clicked on
                View v = glossaryList.getChildAt(i);
                TextView tx = (TextView) view.findViewById(R.id.text1);
                String currentWord = tx.getText().toString();

                //pushing all the data for the current word to the next activity
                Intent wordDetails = new Intent(GlossaryActivity.this, WordDetails.class);
                //"allGlossary" string tells the enxt activity to return to Glossary rather than Personal Glossary afterwards
                wordDetails.putExtra("Activity", "allGlossary");
                wordDetails.putExtra("Word", currentWord);
                wordDetails.putExtra("Details", extractWordAndDetails(wordList).get(currentWord));
                startActivity(wordDetails);
                finish();
            }
        });
    }


    //initialising the listview
    private void setupUI() {
        glossaryList = findViewById(R.id.glossaryListView);
    }

    //fetching the words from the SQLite database of words
    private void fetchDB() {
        dbHelper = new GlossaryDbHelper(this);
        wordList = dbHelper.getAllWords();
    }

    //setting the back button to return to correct activity
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(GlossaryActivity.this, GlossaryCategories.class);
        startActivity(intent);
        finish();
    }

    //taking an arraylist of Word objects and formatting them as a list to fit in the listview with the word + english definition
    private List<Map<String, String>> extractWordsAndEng(ArrayList<Words> arrayList) {
        List<Map<String, String>> wordAndEng = new ArrayList<Map<String, String>>();

        //sorting the glossary alphabetically
        Collections.sort(arrayList, Comparator.comparing(Words::getWord));

        //for every word, extract the word plus its english definition
        for (int i = 0; i < arrayList.size(); i++) {
            Map<String, String> item = new HashMap<String, String>(2);
            item.put("word", arrayList.get(i).getWord());
            item.put("english", arrayList.get(i).getEng_translation());
            wordAndEng.add(item);
        }
        return wordAndEng;
    }

    //taking an arraylist of Word objects and formatting them as a HashMap with the word as the key and the details as its value,
    //so it can be passed as an intent to the word details activity
    private HashMap<String, ArrayList<String>> extractWordAndDetails(ArrayList<Words> arrayList) {
        HashMap<String, ArrayList<String>> wordAndDetails = new HashMap<String, ArrayList<String>>();
        for (int i = 0; i < arrayList.size(); i++) {
            ArrayList<String> details = new ArrayList<>();
            details.add(arrayList.get(i).getEng_translation());
            details.add(arrayList.get(i).getTransliteration());
            details.add(arrayList.get(i).getOrigin_and_word_type());
            details.add(arrayList.get(i).getPunj_translation());
            details.add(arrayList.get(i).getPangti());
            details.add(arrayList.get(i).getImage());
            details.add(arrayList.get(i).getAudio());
            wordAndDetails.put(arrayList.get(i).getWord(), details);
        }
        return wordAndDetails;
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
            public boolean onQueryTextChange(String newText) {
                //intialising a listview to display the queried data
                List<Map<String, String>> wordAndEng = new ArrayList<Map<String, String>>();

                //alphabetically sorted
                Collections.sort(wordList, Comparator.comparing(Words::getWord));
                for (int i = 0; i < wordList.size(); i++) {
                    //need to show word plus english translation
                    Map<String, String> item = new HashMap<String, String>(2);

                    //if the word/transliteration/english translation matches the query then add it to the listview
                    if (wordList.get(i).getWord().toLowerCase().startsWith(newText.toLowerCase())
                            || wordList.get(i).getEng_translation().toLowerCase().startsWith(newText.toLowerCase())
                            || wordList.get(i).getTransliteration().toLowerCase().startsWith(newText.toLowerCase())) {
                        item.put("word", wordList.get(i).getWord());
                        item.put("english", wordList.get(i).getEng_translation());
                        wordAndEng.add(item);
                    }
                }

                //adapter to set the listview with all of the above data
                SimpleAdapter adapter = new SimpleAdapter(GlossaryActivity.this, wordAndEng, R.layout.list_item,
                        new String[]{"word", "english"},
                        new int[]{R.id.text1, R.id.text2});

                glossaryList.setAdapter(adapter);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}