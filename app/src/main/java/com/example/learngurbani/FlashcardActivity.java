package com.example.learngurbani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

//Setting up the flashcard mode

public class FlashcardActivity extends AppCompatActivity {

    private TextView word, translit, origin, engTransl, punjTransl, pangti, txtWordNum;

    private ImageView image;
    private String imgFile, audioFile;

    private int wordCounter = 0;
    private int wordTotalCount;
    private Words currentWord;

    private long backPressedTime;

    private ArrayList<Words> wordList;

    ArrayList<String> words = new ArrayList<String>();

    private FlashcardDialog fcDialog;

    private Button rvlTranslit, rvlOrigin, rvlPunj, rvlPangti, rvlImage, rvlEngMeaning, nextWord, playAudio;

    private HashMap<String, ArrayList<String>> learntWords = new HashMap<String, ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_actvity);

        getSupportActionBar().setTitle("Flashcards");

        setupUI();
        fetchDB();

        //dialog that appears before beginning, gives extra info and option to START or EXIT
        fcDialog = new FlashcardDialog(this);
        fcDialog.fcDialog(this);


        nextWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translit.setVisibility(View.INVISIBLE);
                origin.setVisibility(View.INVISIBLE);
                punjTransl.setVisibility(View.INVISIBLE);
                engTransl.setVisibility(View.INVISIBLE);
                pangti.setVisibility(View.INVISIBLE);
                image.setVisibility(View.INVISIBLE);

                rvlTranslit.setText("Show\ntranslit.");
                rvlOrigin.setText("Show\norigin");
                rvlPunj.setText("Show punjabi");
                rvlPangti.setText("Show\npangti");
                rvlImage.setText("Show\nimage");
                rvlEngMeaning.setText("Show meaning!");

                showWords();

            }
        });

    }

    //set up all of the views and buttons
    private void setupUI() {
        word = findViewById(R.id.fcWord);
        translit = findViewById(R.id.fcTranslit);
        origin = findViewById(R.id.fcOrigin);
        punjTransl = findViewById(R.id.fcPunj);
        engTransl = findViewById(R.id.fcEngTransl);
        pangti = findViewById(R.id.fcPangti);
        image = findViewById(R.id.fcImage);
        playAudio = findViewById(R.id.playAudio);

        txtWordNum = findViewById(R.id.fcWordNum);

        rvlTranslit = findViewById(R.id.rvlTranslit);
        rvlOrigin = findViewById(R.id.rvlOrigin);
        rvlPunj = findViewById(R.id.rvlPunjMeaning);
        rvlPangti = findViewById(R.id.rvlPangti);
        rvlImage = findViewById(R.id.rvlImage);
        rvlEngMeaning = findViewById(R.id.rvlEngMeaning);

        nextWord = findViewById(R.id.fcNextWord);

    }


    //fetching words from the SQLite database
    private void fetchDB() {
        GlossaryDbHelper dbHelper = new GlossaryDbHelper(this);
        wordList = dbHelper.getAllWords();
    }

    //set the number of flashcards, shuffle the word list and showing the words in the activity
    public void startFlashcards() {
        wordTotalCount = 5;
        Collections.shuffle(wordList);
        showWords();
    }


    //setting up the UI to be able to display a word with all of its relevant details
    //setting up the buttons to make different views visible/invisible
    private void showWords() {

        nextWord.setVisibility(View.INVISIBLE);

        rvlTranslit.setOnClickListener(new View.OnClickListener() {
            Boolean hidden = true;

            @Override
            public void onClick(View view) {
                hidden = !hidden;
                hideAndShowButton(view, hidden, translit, null, rvlTranslit, "translit.");
            }
        });

        rvlOrigin.setOnClickListener(new View.OnClickListener() {
            Boolean hidden = true;

            @Override
            public void onClick(View view) {
                hidden = !hidden;
                hideAndShowButton(view, hidden, origin, null, rvlOrigin, "origin");
            }
        });

        rvlPangti.setOnClickListener(new View.OnClickListener() {
            Boolean hidden = true;

            @Override
            public void onClick(View view) {
                hidden = !hidden;
                hideAndShowButton(view, hidden, pangti, null, rvlPangti, "pangti");
            }
        });

        rvlPunj.setOnClickListener(new View.OnClickListener() {
            Boolean hidden = true;

            @Override
            public void onClick(View view) {
                hidden = !hidden;
                hideAndShowButton(view, hidden, punjTransl, null, rvlPunj, "punjabi");
            }
        });

        rvlImage.setOnClickListener(new View.OnClickListener() {
            Boolean hidden = true;

            @Override
            public void onClick(View view) {
                hidden = !hidden;
                hideAndShowButton(view, hidden, null, image, rvlImage, "image");
            }
        });

        rvlEngMeaning.setOnClickListener(new View.OnClickListener() {
            Boolean hidden = true;

            @Override
            public void onClick(View view) {
                hidden = !hidden;
                hideAndShowButton(view, hidden, engTransl, null, rvlEngMeaning, "meaning");
                nextWord.setVisibility(View.VISIBLE);
            }

        });


        //when the final word is not reached yet, show all of the relevant data in each view
        if (wordCounter < wordTotalCount) {
            currentWord = wordList.get(wordCounter);
            word.setText(currentWord.getWord());
            translit.setText(currentWord.getTransliteration());
            origin.setText(currentWord.getOrigin_and_word_type());
            punjTransl.setText(currentWord.getPunj_translation());
            engTransl.setText(currentWord.getEng_translation());
            pangti.setText(currentWord.getPangti());

            //get the filename for the image and audio
            imgFile = currentWord.getImage();
            audioFile = currentWord.getAudio();

            //setting the imageview as the imagefile by setting it as bitmap
            image.setImageBitmap(getBitmapFromAssets(imgFile));

            //button for playing the audio from the associated audio asset
            playAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaPlayer m = new MediaPlayer();
                    try {
                        if (m.isPlaying()) {
                            m.stop();
                            m.release();
                            m = new MediaPlayer();
                        }

                        AssetFileDescriptor descriptor = getAssets().openFd(audioFile);
                        m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                        descriptor.close();

                        m.prepare();
                        m.setVolume(1f, 1f);
                        m.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            //arraylist of all of the words
            words.add(currentWord.getWord());

            //arraylist of all of the details for the current word
            ArrayList<String> oneWordsData = new ArrayList<String>();

            oneWordsData.add(currentWord.getEng_translation());
            oneWordsData.add(currentWord.getTransliteration());
            oneWordsData.add(currentWord.getOrigin_and_word_type());
            oneWordsData.add(currentWord.getPunj_translation());
            oneWordsData.add(currentWord.getPangti());
            oneWordsData.add(currentWord.getImage());
            oneWordsData.add(currentWord.getAudio());


            //adding the word plus all of its details into a hashmap so it can be passed as an intent
            learntWords.put(currentWord.getWord(), oneWordsData);

            //next word
            wordCounter++;

            //once the end is reached
            if (wordCounter == wordTotalCount) {
                //the nextWord button should change from "Next Word" to "Finish"
                nextWord.setText("Finish");

                //once the end is reached, the button should pass the hashmap of the words they have learnt to the next activity
                nextWord.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent fcSummary = new Intent(FlashcardActivity.this, FlashcardSummaryActivity.class);
                        //the hashmap of all words and details as well as the arraylist of only words is needed in the flashcard summary activity
                        fcSummary.putExtra("LearntWords", learntWords);
                        fcSummary.putExtra("words", words);
                        startActivity(fcSummary);
                        finish();
                    }
                });
            } else {
                //if end is not reached, user should be able to move to next word
                nextWord.setText("Next word");
            }

            //showing the user how many words they have done so far and the total words
            txtWordNum.setText("Word: " + wordCounter + "/" + wordTotalCount);
        }

    }

    //setting visibility of text/imageviews and setting text of buttons once clicked
    public void hideAndShowButton(View view, Boolean hidden, TextView txt, ImageView img, Button button, String btnName) {
        if (hidden) {
            if (txt != null) {
                txt.setVisibility(view.INVISIBLE);
            }
            if (img != null) {
                img.setVisibility(View.INVISIBLE);
            }
            if (btnName.equals("meaning")) {
                button.setText("Show " + btnName + "!");
            } else if (btnName.equals("punjabi")) {
                button.setText("Show " + btnName);
            } else {
                button.setText("Show\n" + btnName);
            }
        } else {
            if (txt != null) {
                txt.setVisibility(view.VISIBLE);
            }
            if (img != null) {
                img.setVisibility(View.VISIBLE);
            }
            if (btnName.equals("meaning")) {
                button.setText("Hide " + btnName);
            } else if (btnName.equals("punjabi")) {
                button.setText("Hide " + btnName);
            } else {
                button.setText("Hide\n" + btnName);
            }
        }

    }

    //read an imagefile and display it as an imageview
    private Bitmap getBitmapFromAssets(String fileName) {
        AssetManager am = getAssets();
        InputStream is = null;
        try {
            is = am.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    //ensuring going back words
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            Intent intent = new Intent(FlashcardActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(FlashcardActivity.this, "Press back once more to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    //exiting to the main menu from the dialog, not used in this class, used in the flashcard dialog class
    public void exitFromDialog() {
        Intent intent = new Intent(FlashcardActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}