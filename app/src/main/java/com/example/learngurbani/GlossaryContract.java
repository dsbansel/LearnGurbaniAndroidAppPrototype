package com.example.learngurbani;

import android.provider.BaseColumns;

//contract class for SQLite database

public final class GlossaryContract {

    public GlossaryContract(){}

    //columns of the glossary table
    public static class GlossaryTable implements BaseColumns {

        public static final String TABLE_NAME = "glossary_of_words";
        public static final String COLUMN_WORD = "word";
        public static final String COLUMN_ENG_TRANSLATION = "eng_translation";
        public static final String COLUMN_PUNJ_TRANSLATION = "punj_translation";
        public static final String COLUMN_TRANSLITERATION = "transliteration";
        public static final String COLUMN_ORIGIN_AND_WORD_TYPE = "origin_and_word_type";
        public static final String COLUMN_PANGTI = "pangti";

        //image and audio strings give the file name, the actual image and audio are stored in assets
        //would be better if this was not stored on the device and was pulled from an online source
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_AUDIO = "audio";


    }

}
