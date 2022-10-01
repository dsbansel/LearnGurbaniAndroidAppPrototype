package com.example.learngurbani;

import java.util.ArrayList;
import java.util.HashMap;

//object for a single user
//used for leaderboards by allowing android and firebase to communicate and for pulling data from firebase

public class UsersModel {

    //all of the user's details
    String username;
    long avgScore;
    String email;
    ArrayList<Long> listOfScores;
    ArrayList<Long> listOfResults;
    long avgResult;
    long gamesPlayed;
    long highScore;
    long totalScore;
    long totalCorrectAnswers;
    long totalWrongAnswers;
    long avgWrongAnswers;

    //cannot use a hashmap with an arrayList field when communicating between android and firebase, needs to be converted
//    HashMap<String, ArrayList<String>> wordsLearnt;

    long numOfWordsLearnt;
    ArrayList<String> qsAnswered;

    //constructor for firebase
    public UsersModel() {
    }

    //constructor for android
    public UsersModel(String username, long avgScore, String email, ArrayList<Long> listOfScores, ArrayList<Long> listOfResults, long avgResult, long gamesPlayed, long highScore, long totalScore, long totalCorrectAnswers, long totalWrongAnswers, long avgWrongAnswers, HashMap<String, ArrayList<String>> wordsLearnt, long numOfWordsLearnt, ArrayList<String> qsAnswered) {
        this.username = username;
        this.avgScore = avgScore;
        this.email = email;
        this.listOfScores = listOfScores;
        this.listOfResults = listOfResults;
        this.avgResult = avgResult;
        this.gamesPlayed = gamesPlayed;
        this.highScore = highScore;
        this.totalScore = totalScore;
        this.totalCorrectAnswers = totalCorrectAnswers;
        this.totalWrongAnswers = totalWrongAnswers;
        this.avgWrongAnswers = avgWrongAnswers;
//        this.wordsLearnt = wordsLearnt;
        this.numOfWordsLearnt = numOfWordsLearnt;
        this.qsAnswered = qsAnswered;
    }

    //getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Long> getListOfScores() {
        return listOfScores;
    }

    public void setListOfScores(ArrayList<Long> listOfScores) {
        this.listOfScores = listOfScores;
    }

    public ArrayList<Long> getListOfResults() {
        return listOfResults;
    }

    public void setListOfResults(ArrayList<Long> listOfResults) {
        this.listOfResults = listOfResults;
    }

    public long getAvgResult() {
        return avgResult;
    }

    public void setAvgResult(long avgResult) {
        this.avgResult = avgResult;
    }

    public long getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(long gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public long getHighScore() {
        return highScore;
    }

    public void setHighScore(long highScore) {
        this.highScore = highScore;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(long totalScore) {
        this.totalScore = totalScore;
    }

    public long getTotalCorrectAnswers() {
        return totalCorrectAnswers;
    }

    public void setTotalCorrectAnswers(long totalCorrectAnswers) {
        this.totalCorrectAnswers = totalCorrectAnswers;
    }

    public long getTotalWrongAnswers() {
        return totalWrongAnswers;
    }

    public void setTotalWrongAnswers(long totalWrongAnswers) {
        this.totalWrongAnswers = totalWrongAnswers;
    }

    public long getAvgWrongAnswers() {
        return avgWrongAnswers;
    }

    public void setAvgWrongAnswers(long avgWrongAnswers) {
        this.avgWrongAnswers = avgWrongAnswers;
    }

//    public HashMap<String, ArrayList<String>> getWordsLearnt() {
//        return wordsLearnt;
//    }
//
//    public void setWordsLearnt(HashMap<String, ArrayList<String>> wordsLearnt) {
//        this.wordsLearnt = wordsLearnt;
//    }

    public long getNumOfWordsLearnt() {
        return numOfWordsLearnt;
    }

    public void setNumOfWordsLearnt(long numOfWordsLearnt) {
        this.numOfWordsLearnt = numOfWordsLearnt;
    }

    public ArrayList<String> getQsAnswered() {
        return qsAnswered;
    }

    public void setQsAnswered(ArrayList<String> qsAnswered) {
        this.qsAnswered = qsAnswered;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(long avgScore) {
        this.avgScore = avgScore;
    }


}
