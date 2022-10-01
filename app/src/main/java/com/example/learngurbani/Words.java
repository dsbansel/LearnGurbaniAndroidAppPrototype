package com.example.learngurbani;

//word object needed for SQLite database

public class Words {

    //each column
    private String Word;
    private String eng_translation;
    private String punj_translation;
    private String transliteration;
    private String origin_and_word_type;
    private String pangti;
    private String image;
    private String audio;

    //constructors
    public Words(){}

    public Words(String word, String eng_translation, String punj_translation, String transliteration, String origin_and_word_type, String pangti, String image, String audio) {
        Word = word;
        this.eng_translation = eng_translation;
        this.punj_translation = punj_translation;
        this.transliteration = transliteration;
        this.origin_and_word_type = origin_and_word_type;
        this.pangti = pangti;
        this.image = image;
        this.audio = audio;
    }

    //getters and setters
    public String getWord() {
        return Word;
    }

    public void setWord(String word) {
        Word = word;
    }

    public String getEng_translation() {
        return eng_translation;
    }

    public void setEng_translation(String eng_translation) {
        this.eng_translation = eng_translation;
    }

    public String getPunj_translation() {
        return punj_translation;
    }

    public void setPunj_translation(String punj_translation) {
        this.punj_translation = punj_translation;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public void setTransliteration(String transliteration) {
        this.transliteration = transliteration;
    }

    public String getOrigin_and_word_type() {
        return origin_and_word_type;
    }

    public void setOrigin_and_word_type(String origin_and_word_type) {
        this.origin_and_word_type = origin_and_word_type;
    }

    public String getPangti() {
        return pangti;
    }

    public void setPangti(String pangti) {
        this.pangti = pangti;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}
