package com.example.learngurbani;

import android.provider.BaseColumns;

//contract class for SQLite database

public final class QuizContract {

    public QuizContract(){}

    //columns of the quiz questions table
    public static class QuestionTable implements BaseColumns{

        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_QUESTION = "questions";
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
        public static final String COLUMN_OPTION4 = "option4";
        public static final String COLUMN_ANSWER_NUM = "answerNum";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_DIFFICULTY = "difficulty";

    }

    //can create more tables if necessary

//    public static class QuestionTable2 implements BaseColumns{}

}
