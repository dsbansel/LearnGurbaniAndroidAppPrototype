package com.example.learngurbani;

//Setting up a question object for the SQLite database

public class Questions {

    //constants for certain columns
    public static final String CATEGORY_MISC = "Misc";
    public static final String CATEGORY_JAPJISAHIB = "JapjiSahib";
    public static final String CATEGORY_CHAUPAISAHIB = "ChaupaiSahib";
    public static final String CATEGORY_ANIMALS = "Animals";
    public static final String CATEGORY_NATURE = "Nature";

    public static final String DIFFICULTY_EASY = "Easy";
    public static final String DIFFICULTY_MED = "Medium";
    public static final String DIFFICULTY_HARD = "Hard";


    //all of the columns
    private String Question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int answerNum;
    private String category;
    private String difficulty;

    //constructors
    public Questions() {
    }

    public Questions(String question, String option1, String option2, String option3, String option4, int answerNum, String category, String difficulty) {
        Question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answerNum = answerNum;
        this.category = category;
        this.difficulty = difficulty;
    }

    //getters and setters
    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public int getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(int answerNum) {
        this.answerNum = answerNum;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() { return difficulty; }

    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
}
