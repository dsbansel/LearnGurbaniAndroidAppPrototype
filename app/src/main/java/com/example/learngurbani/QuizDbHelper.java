package com.example.learngurbani;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.learngurbani.QuizContract.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

//setting up the SQLite database for the table of quiz questions

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "learngurbaniQuiz.db";

    //update version if structure has changed
    private static final int DATABASE_VERSION = 3;

    private SQLiteDatabase db;

    //constructor
    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //create the table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.db = sqLiteDatabase;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionTable.TABLE_NAME + " ( " +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionTable.COLUMN_OPTION4 + " TEXT, " +
                QuestionTable.COLUMN_ANSWER_NUM + " INTEGER, " +
                QuestionTable.COLUMN_CATEGORY + " TEXT, " +
                QuestionTable.COLUMN_DIFFICULTY + " TEXT" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);

        fillQuestionsTable(); //insert data inside table

    }

    //updating the database if changes are made and versions do not match
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
        onCreate(db);
    }

    //adding a question to the table
    private void addQuestions(Questions questions) {

        ContentValues cv = new ContentValues();

        //question details
        cv.put(QuestionTable.COLUMN_QUESTION, questions.getQuestion());
        cv.put(QuestionTable.COLUMN_OPTION1, questions.getOption1());
        cv.put(QuestionTable.COLUMN_OPTION2, questions.getOption2());
        cv.put(QuestionTable.COLUMN_OPTION3, questions.getOption3());
        cv.put(QuestionTable.COLUMN_OPTION4, questions.getOption4());
        cv.put(QuestionTable.COLUMN_ANSWER_NUM, questions.getAnswerNum());
        cv.put(QuestionTable.COLUMN_CATEGORY, questions.getCategory());
        cv.put(QuestionTable.COLUMN_DIFFICULTY, questions.getDifficulty());

        //insert into table
        db.insert(QuestionTable.TABLE_NAME, null, cv);

    }

    //filling the table with data
    private void fillQuestionsTable() {

        //Misc Qs
        //new question entry
        Questions q1 = new Questions("What does the word ਖੇਮ ('khem') mean?", "Water", "Butter", "Hatred", "Happiness", 4, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        //add this entry into the table
        addQuestions(q1);

        Questions q2 = new Questions("What does the word ਮੂਰਖ ('moorakh') mean?",
                "Fool", "Dog", "Wise", "Arm", 1,
                Questions.CATEGORY_MISC, Questions.DIFFICULTY_EASY);
        addQuestions(q2);

        Questions q3 = new Questions("What does the word ਪੰਡਿਤੁ ('pandit') mean?", "Muslim teacher", "Hindu Scholar", "Doctor", "Trousers", 2, Questions.CATEGORY_MISC, Questions.DIFFICULTY_EASY);
        addQuestions(q3);

        Questions q4 = new Questions("What does the word ਪਾਰਖੂ ('paarkhoo') mean?", "Park", "Drowning", "Horse", "Judge", 4, Questions.CATEGORY_MISC, Questions.DIFFICULTY_HARD);
        addQuestions(q4);

        Questions q5 = new Questions("What does the word ਭਗਤ ('bhagat') mean?", "Devotee", "Bag", "Living", "Reincarnation", 1, Questions.CATEGORY_MISC, Questions.DIFFICULTY_EASY);
        addQuestions(q5);

        Questions q6 = new Questions("What does the word ਪਾਪੀ ('paapee') mean?", "Saint", "Sinner", "Child", "Old person", 2, Questions.CATEGORY_MISC, Questions.DIFFICULTY_EASY);
        addQuestions(q6);

        Questions q7 = new Questions("What does the word ਖਜਾਨਾ ('khajaanaa') mean?", "Treasure", "Wealth", "Hunger", "Baby", 1, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(q7);

        Questions q8 = new Questions("What does the word ਧਿਆਇ ('dhiaai') mean?", "Sleep", "Knowledge", "Focus on", "Try", 3, Questions.CATEGORY_MISC, Questions.DIFFICULTY_EASY);
        addQuestions(q8);

        Questions q9 = new Questions("What does the word ਵਿਕਾਰ ('vikaar') mean?", "Death", "Sin", "Eating", "Get rid of", 2, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(q9);

        Questions q10 = new Questions("What does the word ਵਡਭਾਗੀ ('vadbhaagee') mean?", "Sleepy", "Big head", "Lazy", "Great fortune", 4, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(q10);

        Questions q11 = new Questions("What does the word ਭੀਤਰਿ ('bheetar(i)') mean?", "Winning", "Sitting down", "Inside", "Outside", 3, Questions.CATEGORY_MISC, Questions.DIFFICULTY_HARD);
        addQuestions(q11);

        Questions q12 = new Questions("What does the word ਬਿਖੋਟਿ ('bikhot(i)') mean?", "Faith", "Sell", "Counterfeit", "Wealth", 1, Questions.CATEGORY_MISC, Questions.DIFFICULTY_HARD);
        addQuestions(q12);

        Questions q13 = new Questions("What does the word ਹਿਰਦੈ ('hirdai') mean?", "Everyday", "Heard it", "In the heart", "House", 3, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(q13);

        Questions q14 = new Questions("What does the word ਨਿਹਾਲ ('nihaal') mean?", "Asleep", "Exalted", "Detached", "Desire", 2, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(q14);

        Questions q15 = new Questions("What does the word ਦਇਆ ('daya') mean?", "Compassion", "Give", "Washing", "Impure", 1, Questions.CATEGORY_MISC, Questions.DIFFICULTY_EASY);
        addQuestions(q15);

        Questions q16 = new Questions("What does the word ਤਾਣੁ ('taan') mean?", "Knowledge", "Arrow", "Strength", "Love", 3, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(q16);

        Questions q17 = new Questions("Which word describes animals that are born from their mother’s womb?", "ਉਤਭੁਜ", "ਗਰਭ", "ਜੇਰਜ", "None of the above", 3, Questions.CATEGORY_MISC, Questions.DIFFICULTY_HARD);
        addQuestions(q17);

        Questions q18 = new Questions("A ਬਗੁਲਾ (heron/crane bird) is used to represent what and why?", "A thief because it steals from other birds"
                , "A saintly being because because of its beauty", "A king because of its long life", "A false impure person because it likes dirty water", 4, Questions.CATEGORY_MISC, Questions.DIFFICULTY_HARD);
        addQuestions(q18);


        Questions q19 = new Questions("What does the word ਧਰਤੀ 'dhartee' mean?", "Earth, world", "Tree, forest", "Cloud", "River", 1, Questions.CATEGORY_MISC, Questions.DIFFICULTY_EASY);
        addQuestions(q19);

        Questions q20 = new Questions("What does the word ਭਵਣ 'bhavan' mean?", "World", "Universe", "Air", "Fire", 2, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(q20);

        Questions q21 = new Questions("Which of these is NOT a word for the world and creation?", "ਰਚਨਾ (rachnaa)", "ਜਗਤ (jagat)", "ਜਹਾਜ (jahaaj)", "ਜਹਾਨ (jahaan)", 3, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(q21);

        Questions q22 = new Questions("What does the word ਕਸੁੰਭ 'kasumbh' represent?", "The rays of the Sun as the rays of the Guru’s teachings",
                "The dirt of illusion", "The temporary nature of false desires similar to the temporary colour from safflowers", "The love for saints",
                3, Questions.CATEGORY_MISC, Questions.DIFFICULTY_HARD);
        addQuestions(q22);

        Questions q23 = new Questions("What does the word ਜਗੁ 'jag(u)' mean?", "World", "Jug", "Organism", "Rainforest", 1, Questions.CATEGORY_MISC, Questions.DIFFICULTY_EASY);
        addQuestions(q23);

        Questions q24 = new Questions("If we want to say that God knows what is in our mind/heart, we say they are…",
                "ਸੁਜਾਨੁ (sujaan)", "ਨਾਨਕ (nanak)", "ਅੰਤਰਜਾਮੀ (antarjaamee)", "ਨਿਰਮਲ (nirmal)", 3, Questions.CATEGORY_MISC, Questions.DIFFICULTY_EASY);
        addQuestions(q24);

        Questions q25 = new Questions("How do we say ‘master’?", "ਸਾਹਿਬ (saahib)", "ਸਤਿਗੁਰੂ (satiguroo)", "ਸੇਵਕ (sevak)", "Answer 1 and 3", 1, Questions.CATEGORY_MISC, Questions.DIFFICULTY_EASY);
        addQuestions(q25);

        Questions q26 = new Questions("Which of these is NOT a word for God?", "ਪ੍ਰਭ (prabh)", "ਭਗਵਾਨ (bhagvaan)", "ਗੋਬਿੰਦ (gobind)", "ਨਰਕ (narak)", 4, Questions.CATEGORY_MISC, Questions.DIFFICULTY_EASY);
        addQuestions(q26);

        Questions q27 = new Questions("Which word means ‘protector’?", "ਰਾਖਨਹਾਰੁ (raakhanhaar)", "ਪ੍ਰਤਿਪਾਲਕ (pratipaalak)", "ਬਹਾਦਰ (bahaadar)", "Answers 1 and 2", 4, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(q27);

        Questions q28 = new Questions("What does the word ਦਾਤਾਰ 'daataar' mean?", "Giver", "All-powerful", "Father", "Greatest", 1, Questions.CATEGORY_MISC, Questions.DIFFICULTY_EASY);
        addQuestions(q28);

        Questions q29 = new Questions("What does the word ਪੂਰੈ 'poorai' mean?", "Perfect", "Full", "Complete", "All of the above", 4, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(q29);

        Questions q30 = new Questions("What does the word ਤਪ 'tap' mean?", "Yoga", "Worship", "Vow of silence", "Intense meditation", 4, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(q30);

        Questions q31 = new Questions("What is ਕੀਰਤਨੁ 'keertan'?", "Singing God’s praises", "Reciting daily prayers", "Playing musical instruments", "All of the above", 1, Questions.CATEGORY_MISC, Questions.DIFFICULTY_EASY);
        addQuestions(q31);


        Questions q32 = new Questions("What is the gathering of holy worshippers/saints known as?", "ਸਾਧਸੰਗਤਿ (saadhsangat(i))", "ਦਰਬਾਰ (darbaar)",
                "ਨਗਰ (nagar)", "ਮਸੀਤ (maseet)", 1, Questions.CATEGORY_MISC, Questions.DIFFICULTY_EASY);
        addQuestions(q32);


        Questions q33 = new Questions("How do we say ‘Hell’?", "ਪਤਾਲ (pataal)", "ਨਰਕ (narak)", "ਦੋਜਕ (dojak)", "All of the above", 4, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(q33);

        Questions q34 = new Questions("What does the word ਮਿਲਖ 'milakh' mean?", "Property", "Possessions", "Wealth", "All of the above", 4, Questions.CATEGORY_MISC, Questions.DIFFICULTY_HARD);
        addQuestions(q34);

        Questions q35 = new Questions("What does the word ਮਸੂਰਤਿ 'masoorat(i)' mean?", "Painting", "Advice", "Learning", "Sleeping", 2, Questions.CATEGORY_MISC, Questions.DIFFICULTY_HARD);
        addQuestions(q35);

        Questions q36 = new Questions("What does the word ਬਿਖ 'bikh' mean?", "Poison", "Brick", "Brother", "Large", 1, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(q36);

        Questions q37 = new Questions("What does the word ਬਿਉਹਾਰੁ 'biuhaar' mean?", "Business", "Occupation", "Dealing/Affair", "All of the above", 4, Questions.CATEGORY_MISC, Questions.DIFFICULTY_HARD);
        addQuestions(q37);

        Questions q38 = new Questions("Which of these does NOT mean ਆਚਾਰੁ 'aachaar(u)'?", "Lifestyle", "Actions", "Pickles", "Rituals", 3, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(q38);

        Questions q39 = new Questions("What does the word ਅਗਨ 'agan' mean?", "Angel", "Goddess", "Fire", "Stove", 3, Questions.CATEGORY_MISC, Questions.DIFFICULTY_EASY);
        addQuestions(q39);

        Questions q40 = new Questions("What does the word ਦਾਗ 'daag' mean?", "Stain", "Pudding", "Offering", "None of the above", 1, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(q40);

        //Japji Sahib Qs

        Questions jsQ1 = new Questions("What does the word ਲਖ ('lakh') mean?", "100,000", "Sleep", "Drugs", "Lime", 1, Questions.CATEGORY_MISC, Questions.DIFFICULTY_EASY);
        addQuestions(jsQ1);

        Questions jsQ2 = new Questions("What does the word ਸੋਚਿ ('soch') mean?", "Try", "Dig", "Wash", "Sock", 3, Questions.CATEGORY_MISC, Questions.DIFFICULTY_HARD);
        addQuestions(jsQ2);

        Questions jsQ3 = new Questions("What does the word ਭੁਖ ('bhukh') mean?", "Grow", "Book", "Wine", "Hunger", 4, Questions.CATEGORY_MISC, Questions.DIFFICULTY_EASY);
        addQuestions(jsQ3);

        Questions jsQ4 = new Questions("What does the word ਪੁਰੀਆ ('pureeaa') mean?", "Worlds", "Evening", "Perfect", "Alive", 1, Questions.CATEGORY_MISC, Questions.DIFFICULTY_HARD);
        addQuestions(jsQ4);

        Questions jsQ5 = new Questions("What does the word ਸਿਆਣਪਾ ('siaanpaa') mean?", "Dumb", "Cleverness", "Deaf", "Book", 2, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(jsQ5);

        Questions jsQ6 = new Questions("What does the word ਸਚਿਆਰਾ ('sachiaaraa') mean?", "Hungry", "Sleep", "Truthful", "Mouse", 3, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(jsQ6);

        Questions jsQ7 = new Questions("What does the word ਰਜਾਈ ('rajaaee') mean?", "Will", "Duvet", "Pillow", "Bed", 1, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(jsQ7);

        Questions jsQ8 = new Questions("What does the word ਵਡਿਆਈ ('vadiaaee') mean?", "Sweeter", "Sour", "Tiny", "Greatness", 4, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(jsQ8);

        //Chaupai Sahib Qs
        Questions csQ1 = new Questions("What does the word ਰੱਛਾ ('rachhaa') mean?", "Protection", "Fly", "Sound", "Creation", 1, Questions.CATEGORY_MISC, Questions.DIFFICULTY_HARD);
        addQuestions(csQ1);

        Questions csQ2 = new Questions("What does the word ਦੁਸਟ ('dust') mean?", "Parents", "Brothers", "Friends", "Enemies", 4, Questions.CATEGORY_MISC, Questions.DIFFICULTY_HARD);
        addQuestions(csQ2);

        Questions csQ3 = new Questions("What does the word ਸੇਵਕ ('sevak') mean?", "Sweeper", "Servants", "Cousins", "Asleep", 2, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(csQ3);

        //Animal Qs
        Questions animalQ1 = new Questions("What does the word ਕੁੰਚਰ ('kunchar') mean?", "Dragon", "Pig", "Elephant", "Cow", 3, Questions.CATEGORY_MISC, Questions.DIFFICULTY_HARD);
        addQuestions(animalQ1);

        Questions animalQ2 = new Questions("What does the word ਕੂਕਰ ('kookar') mean?", "Chicken", "Dog", "Cat", "Mouse", 2, Questions.CATEGORY_MISC, Questions.DIFFICULTY_HARD);
        addQuestions(animalQ2);

        Questions animalQ3 = new Questions("What does the word ਧੇਨ ('dhen') mean?", "Dinosaur", "Beetle", "Weasel", "Cow", 4, Questions.CATEGORY_MISC, Questions.DIFFICULTY_HARD);
        addQuestions(animalQ3);

        //Nature Qs
        Questions natureQ1 = new Questions("What does the word ਪਰਬਤੁ ('parbat') mean?", "Mountain", "Waterfall", "Ocean", "Field", 1, Questions.CATEGORY_MISC, Questions.DIFFICULTY_HARD);
        addQuestions(natureQ1);

        Questions natureQ2 = new Questions("What does the word ਸਾਗਰੁ ('saagar') mean?", "Herbs", "Ocean", "Tree", "Flower", 2, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(natureQ2);

        Questions natureQ3 = new Questions("What does the word ਦਰਖਤ ('darakht') mean?", "Grass", "Cloud", "Rain", "Tree", 4, Questions.CATEGORY_MISC, Questions.DIFFICULTY_MED);
        addQuestions(natureQ3);


    }

    //getting all questions from the database
    //unused since we get questions based on category and difficulties now
    public ArrayList<Questions> getAllQuestions() {

        ArrayList<Questions> questionsList = new ArrayList<>();

        db = getReadableDatabase();

        String Projection[] = {
                QuestionTable._ID,
                QuestionTable.COLUMN_QUESTION,
                QuestionTable.COLUMN_OPTION1,
                QuestionTable.COLUMN_OPTION2,
                QuestionTable.COLUMN_OPTION3,
                QuestionTable.COLUMN_OPTION4,
                QuestionTable.COLUMN_ANSWER_NUM
        };


        Cursor c = db.query(QuestionTable.TABLE_NAME,
                Projection,
                null,
                null,
                null,
                null,
                null

        );

        if (c.moveToFirst()) {

            do {

                Questions questions = new Questions();
                questions.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                questions.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                questions.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                questions.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                questions.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                questions.setAnswerNum(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NUM)));

                questionsList.add(questions);


            } while (c.moveToNext());
        }

        c.close();

        return questionsList;

    }

    //getting all questions from the database based on category alone
    //unused since we get questions based on category AND difficulties now
    public ArrayList<Questions> getAllQuestionsWithCategory(String category) {

        ArrayList<Questions> questionsList = new ArrayList<>();

        db = getReadableDatabase();

        String Projection[] = {
                QuestionTable._ID,
                QuestionTable.COLUMN_QUESTION,
                QuestionTable.COLUMN_OPTION1,
                QuestionTable.COLUMN_OPTION2,
                QuestionTable.COLUMN_OPTION3,
                QuestionTable.COLUMN_OPTION4,
                QuestionTable.COLUMN_ANSWER_NUM,
                QuestionTable.COLUMN_CATEGORY,
                QuestionTable.COLUMN_DIFFICULTY
        };

        String selection = QuestionTable.COLUMN_CATEGORY + " = ? ";
        String selectionArgs[] = {category};


        Cursor c = db.query(QuestionTable.TABLE_NAME,
                Projection,
                selection,
                selectionArgs,
                null,
                null,
                null

        );

        if (c.moveToFirst()) {

            do {

                Questions questions = new Questions();
                questions.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                questions.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                questions.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                questions.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                questions.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                questions.setAnswerNum(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NUM)));
                questions.setCategory(c.getString(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY)));
                questions.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DIFFICULTY)));


                questionsList.add(questions);


            } while (c.moveToNext());
        }

        c.close();

        return questionsList;

    }

    //getting questions based on cateogires and difficulties
    public ArrayList<Questions> getAllQuestionsWithCategoryAndDifficulty(String category, String difficulty) {

        //list of questions to return
        ArrayList<Questions> questionsList = new ArrayList<>();

        db = getReadableDatabase();

        String Projection[] = {
                QuestionTable._ID,
                QuestionTable.COLUMN_QUESTION,
                QuestionTable.COLUMN_OPTION1,
                QuestionTable.COLUMN_OPTION2,
                QuestionTable.COLUMN_OPTION3,
                QuestionTable.COLUMN_OPTION4,
                QuestionTable.COLUMN_ANSWER_NUM,
                QuestionTable.COLUMN_CATEGORY,
                QuestionTable.COLUMN_DIFFICULTY
        };

        //querying based on category and difficulty
        String selection = QuestionTable.COLUMN_CATEGORY + " = ? AND " + QuestionTable.COLUMN_DIFFICULTY + " = ? ";
        String selectionArgs[] = {category, difficulty};


        Cursor c = db.query(QuestionTable.TABLE_NAME,
                Projection,
                selection,
                selectionArgs,
                null,
                null,
                null

        );

        //create a new question object, initialise all of its data, add it to the questionList (to be returned) before moving on to the next question
        if (c.moveToFirst()) {
            do {
                Questions questions = new Questions();
                questions.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                questions.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                questions.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                questions.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                questions.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                questions.setAnswerNum(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NUM)));
                questions.setCategory(c.getString(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY)));
                questions.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DIFFICULTY)));

                questionsList.add(questions);

            } while (c.moveToNext());
        }

        c.close();

        return questionsList;

    }

}
