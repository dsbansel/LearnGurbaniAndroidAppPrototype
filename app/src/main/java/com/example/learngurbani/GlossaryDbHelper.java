package com.example.learngurbani;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.learngurbani.GlossaryContract.*;

import java.util.ArrayList;

//setting up the SQLite database for the glossary of words

public class GlossaryDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "learngurbaniGlossary.db";

    //update version if structure has changed
    private static final int DATABASE_VERSION = 3;

    private SQLiteDatabase db;

    //constructor
    public GlossaryDbHelper(Context context) {super(context, DATABASE_NAME,null,DATABASE_VERSION);}

    //create the table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;

        final String SQL_CREATE_GLOSSARY_TABLE = "CREATE TABLE " +
                GlossaryTable.TABLE_NAME + " ( " +
                GlossaryTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GlossaryTable.COLUMN_WORD + " TEXT, " +
                GlossaryTable.COLUMN_ENG_TRANSLATION + " TEXT, " +
                GlossaryTable.COLUMN_PUNJ_TRANSLATION + " TEXT, " +
                GlossaryTable.COLUMN_TRANSLITERATION + " TEXT, " +
                GlossaryTable.COLUMN_ORIGIN_AND_WORD_TYPE + " TEXT, " +
                GlossaryTable.COLUMN_PANGTI + " TEXT, " +
                GlossaryTable.COLUMN_IMAGE + " TEXT, " +
                GlossaryTable.COLUMN_AUDIO + " TEXT" +
                ")";

        db.execSQL(SQL_CREATE_GLOSSARY_TABLE);
        fillGlossaryTable();

    }

    //updating the database if changes are made and versions do not match
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + GlossaryTable.TABLE_NAME);
        onCreate(db);
    }

    //adding a word to the table
    private void addWords(Words words){
        ContentValues cv = new ContentValues();

        //word details
        cv.put(GlossaryTable.COLUMN_WORD, words.getWord());
        cv.put(GlossaryTable.COLUMN_ENG_TRANSLATION, words.getEng_translation());
        cv.put(GlossaryTable.COLUMN_PUNJ_TRANSLATION, words.getPunj_translation());
        cv.put(GlossaryTable.COLUMN_TRANSLITERATION, words.getTransliteration());
        cv.put(GlossaryTable.COLUMN_ORIGIN_AND_WORD_TYPE, words.getOrigin_and_word_type());
        cv.put(GlossaryTable.COLUMN_PANGTI, words.getPangti());
        cv.put(GlossaryTable.COLUMN_IMAGE, words.getImage());
        cv.put(GlossaryTable.COLUMN_AUDIO, words.getAudio());

        //insert as a new entry
        db.insert(GlossaryTable.TABLE_NAME, null,cv);

    }

    //filling the table with data
    private void fillGlossaryTable() {

        //new word entry
        Words khem = new Words("ਖੇਮ","Happiness","ਸੁਖ","khem","Sanskrit, Noun","ਹਰਿ ਹਰਿ ਧਿਆਇ ਕੁਸਲ ਸਭਿ ਖੇਮਾ ॥੨॥\nMeditating on the Lord, Har, Har, I have found total joy and bliss. ||2||","khem.jpeg","khem.mp3");
        //add this word to table
        addWords(khem);

        Words baalik = new Words("ਬਾਲਿਕ","Child","ਬੱਚਾ","baalik","Sanskrit, Noun","ਮੇਰੇ ਸਤਿਗੁਰ ਅਪਨੇ ਬਾਲਿਕ ਰਾਖਹੁ ਲੀਲਾ ਧਾਰਿ ॥\n" +
                "O my True Guru, protect me, Your child, through the power of Your play.","baalik.jpeg","baalik.mp3");
        addWords(baalik);

        Words thaakur = new Words("ਠਾਕੁਰ","Master","ਸਾਹਿਬ",
                "thaakur","Punjabi, Noun",
                "ਹਰਿ ਆਪੇ ਠਾਕੁਰੁ ਹਰਿ ਆਪੇ ਸੇਵਕੁ ਜੀ ਕਿਆ ਨਾਨਕ ਜੰਤ ਵਿਚਾਰਾ ॥੧॥\n" +
                "The Lord Himself is the Master, the Lord Himself is the Servant. O Nanak, the poor beings are wretched and miserable!",
                "thaakur.jpeg","thaakur.mp3");
        addWords(thaakur);

        Words leelaa = new Words("ਲੀਲਾ","Play","ਖੇਡ","leelaa","Sanskrit, Noun","ਲਖੀ ਨ ਜਾਈ ਨਾਨਕ ਲੀਲਾ ॥੧॥\n" +
                "O Nanak, His wondrous play cannot be understood.","leelaa.jpeg","leelaa.mp3");
        addWords(leelaa);

        Words nindaa = new Words("ਨਿੰਦਾ","Slander","ਔਗੁਣ ਕਢਣ ਦੀ ਕਿਰਿਆ","nindaa","Sanskrit, Noun","ਮੇਰੇ ਮਨ ਤਜਿ ਨਿੰਦਾ ਹਉਮੈ ਅਹੰਕਾਰੁ ॥\n" +
                "O my mind, give up slander, egotism and arrogance.\n","nindaa.jpeg","nindaa.mp3");
        addWords(nindaa);

        Words raakhu = new Words("ਰਾਖਹੁ","Protect","ਬਚਾ ਲਵੋ","raakhu","Punjabi, Verb","ਹਰਿ ਜੀਉ ਰਾਖਹੁ ਅਪੁਨੀ ਸਰਣਾਈ ਜਿਉ ਰਾਖਹਿ ਤਿਉ ਰਹਣਾ ॥\n" +
                "O Dear Lord, please protect and preserve me in Your Sanctuary. As You keep me, so do I remain.","raakhu.jpeg","raakhu.mp3");
        addWords(raakhu);

        Words maslat = new Words("ਮਸਲਤਿ","Advice","ਸਲਾਹ","masalat(i)","Arabic, Noun","ਅਹੰਮੇਵ ਸਿਉ ਮਸਲਤਿ ਛੋਡੀ ॥\n" +
                "I have forsaken the advice of my ego.","maslat.jpeg","maslat.mp3");
        addWords(maslat);

        Words birhaa = new Words("ਬਿਰਹਾ","Separation, Love","ਵਿਛੋੜਾ, ਪਿਆਰ","birhaa","Sanskrit, Noun","ਮੈ ਹਰਿ ਬਿਰਹੀ ਹਰਿ ਨਾਮੁ ਹੈ ਕੋਈ ਆਣਿ ਮਿਲਾਵੈ ਮਾਇ ॥\n" +
                "I feel the pains of love and yearning for the Lord, and the Name of the Lord. If only someone would come and unite me with Him, O my mother." ,"birhaa.jpeg","birhaa.mp3");
        addWords(birhaa);

        Words w1 = new Words("ਵਿਸਰਿ","Forget","ਭੁਲਾ","visar(i)","Punjabi, Verb","ਸੋ ਕਿਉ ਬਿਸਰੈ ਜਿਨਿ ਸਭੁ ਕਿਛੁ ਦੀਆ ॥\n" +
                "Why forget Him, who has given us everything?", "visar.jpeg","visar.mp3");
        addWords(w1);


        Words w2 = new Words("ਕੰਚਨ","Gold, Golden","ਸੋਨਾ, ਸੁਨਹਿਰੀ", "kanchan", "Sanskrit, Noun/Adjective", "ਸਤਿਗੁਰ ਕੀ ਜੇ ਸਰਣੀ ਆਵੈ ਫਿਰਿ ਮਨੂਰਹੁ ਕੰਚਨੁ ਹੋਹਾ ॥\n" +
                "One who comes to the Sanctuary of the True Guru, shall be transformed from iron into gold.", "kanchan.jpeg", "kanchan.mp3");
        addWords(w2);

    }

    //getting all words from the database
    public ArrayList<Words> getAllWords(){

        //list of words to return
        ArrayList<Words> wordsList = new ArrayList<>();

        db = getReadableDatabase();

        String Projection[] = {
                GlossaryTable._ID,
                GlossaryTable.COLUMN_WORD,
                GlossaryTable.COLUMN_ENG_TRANSLATION,
                GlossaryTable.COLUMN_PUNJ_TRANSLATION,
                GlossaryTable.COLUMN_TRANSLITERATION,
                GlossaryTable.COLUMN_ORIGIN_AND_WORD_TYPE,
                GlossaryTable.COLUMN_PANGTI,
                GlossaryTable.COLUMN_IMAGE,
                GlossaryTable.COLUMN_AUDIO
        };

        //no querying applied
        Cursor c = db.query(GlossaryTable.TABLE_NAME,
                Projection,
                null,
                null,
                null,
                null,
                null
        );

        //create a new word object, initialise all of its data, add it to the wordsList (to be returned) before moving on to the next word
        if (c.moveToFirst()){
            do {
                Words words = new Words();
                words.setWord(c.getString(c.getColumnIndex(GlossaryTable.COLUMN_WORD)));
                words.setEng_translation(c.getString(c.getColumnIndex(GlossaryTable.COLUMN_ENG_TRANSLATION)));
                words.setPunj_translation(c.getString(c.getColumnIndex(GlossaryTable.COLUMN_PUNJ_TRANSLATION)));
                words.setTransliteration(c.getString(c.getColumnIndex(GlossaryTable.COLUMN_TRANSLITERATION)));
                words.setOrigin_and_word_type(c.getString(c.getColumnIndex(GlossaryTable.COLUMN_ORIGIN_AND_WORD_TYPE)));
                words.setPangti(c.getString(c.getColumnIndex(GlossaryTable.COLUMN_PANGTI)));
                words.setImage(c.getString(c.getColumnIndex(GlossaryTable.COLUMN_IMAGE)));
                words.setAudio(c.getString(c.getColumnIndex(GlossaryTable.COLUMN_AUDIO)));

                wordsList.add(words);

            } while (c.moveToNext());
        }

        c.close();

        return wordsList;

    }

}
