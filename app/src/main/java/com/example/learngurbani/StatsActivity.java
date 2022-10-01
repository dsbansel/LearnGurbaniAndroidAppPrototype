package com.example.learngurbani;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
//import android.support.v7.app.ActionBarActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

//page for viewing current user's statistics

/*
Chart designs and code taken/adapted from: https://github.com/PhilJay/MPAndroidChart
 */

public class StatsActivity extends AppCompatActivity {

    TextView gamesPlayed, avgScore, avgResult, highScore, numOfWordsLearnt,
            qsAnswered, totalCorrect, totalWrong, totalScore,
            last5Scores, last5Results, welcome, email;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userID;

    ArrayList<Long> fivescores = new ArrayList<Long>();
    ArrayList<Long> fiveresults = new ArrayList<Long>();

    BarChart chart;
    PieChart pieChart;
    LineChart lineChart, lineChart2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        getSupportActionBar().setTitle("Statistics");

        //setting up firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        //setting all of the textviews and charts
        gamesPlayed = findViewById(R.id.stats_gamesPlayed);
        avgScore = findViewById(R.id.stats_avgScore);
        avgResult = findViewById(R.id.stats_avgResult);
        highScore = findViewById(R.id.stats_highScore);
        numOfWordsLearnt = findViewById(R.id.stats_numOfWordsLearnt);
        qsAnswered = findViewById(R.id.stats_qsAnswered);
        totalCorrect = findViewById(R.id.stats_totalCorrectAns);
        totalWrong = findViewById(R.id.stats_totalWrongAns);
        totalScore = findViewById(R.id.stats_totalScore);
        last5Scores = findViewById(R.id.stats_last5Scores);
        last5Results = findViewById(R.id.stats_last5results);
        welcome = findViewById(R.id.stats_welcome);
        email = findViewById(R.id.stats_email);
        pieChart = findViewById(R.id.pieChartResults);
        lineChart = findViewById(R.id.linechart);
        lineChart2 = findViewById(R.id.linechart2);

        //listening from firebase for user data
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if (error == null) {
                    //entering all the data in the associated views
                    welcome.setText("Player Statistics for: " + documentSnapshot.getString("username"));
                    email.setText("Email: " + documentSnapshot.getString("email"));
                    gamesPlayed.setText("Total Games Played: " + documentSnapshot.get("gamesPlayed"));
                    avgScore.setText("Average Score: " + documentSnapshot.get("avgScore"));
                    highScore.setText("High Score: " + documentSnapshot.get("highScore"));
                    numOfWordsLearnt.setText("Total Words Learnt: " + documentSnapshot.get("numOfWordsLearnt"));
                    ArrayList<String> allQsAnswered = (ArrayList<String>) documentSnapshot.get("qsAnswered");
                    qsAnswered.setText("Total Different Q's Answered: " + allQsAnswered.size());
                    totalCorrect.setText("Total Correct Answers: " + documentSnapshot.get("totalCorrectAnswers"));
                    totalWrong.setText("Total Wrong Answers: " + documentSnapshot.get("totalWrongAnswers"));
                    totalScore.setText("Total Lifetime Score: " + documentSnapshot.get("totalScore"));

                    //showing the last 5 scores
                    ArrayList<Long> listOfScores = (ArrayList<Long>) documentSnapshot.get("scores");
                    if (listOfScores.size() < 5) {
                        //if the user is yet to play 5 games, no data available
                        last5Scores.setText("Last 5 Scores: (Please play 5 games)");
                    } else {
                        for (int i = 5; i > 0; i--) {
                            //add the last 5 scores to an arraylist
                            fivescores.add(listOfScores.get(listOfScores.size() - i));
                        }
                        last5Scores.setText("Last 5 Scores:");
                        //show the scores as a barchart
                        chart = findViewById(R.id.chart);
                        BarData data = createChartData(fivescores, "Points earned", Color.parseColor("#4D8FF4"));
                        configureChartAppearance();
                        prepareChartData(data);
                    }

                    //code to show the average score as a piechart
//                    float avgScore = ((Long) documentSnapshot.get("avgScore")).floatValue();
//                    showPieChart(pieChart, avgScore, 2000,"Points","Points Missed", Color.parseColor("#ffbf00"),Color.parseColor("#4D8FF4"));
//                    pieChart.getLegend().setEnabled(false);

                    //showing the last 5 results
                    ArrayList<Long> listOfResults = (ArrayList<Long>) documentSnapshot.get("results");
                    if (listOfResults.size() < 5) {
                        //if the user is yet to play 5 games, no data available
                        last5Results.setText("Last 5 Results: (Please play 5 games)");
                    } else {
                        for (int i = 5; i > 0; i--) {
                            //add the last 5 results to an arraylist
                            fiveresults.add(listOfResults.get(listOfResults.size() - i));
                        }
                        last5Results.setText("Last 5 Results:");

                        //show the results as a barchart
                        chart = findViewById(R.id.chart2);

                        BarData data = createChartData(fiveresults, "Score out of 10", Color.parseColor("#ffbf00"));
                        configureChartAppearance();
                        prepareChartData(data);
                    }

                    //calculating and showing the average result as a 2dp float, integer is not precise enough since it is a small number (0-10)
                    float sum = 0;
                    for (int i = 0; i < listOfResults.size(); i++) {
                        sum += listOfResults.get(i);
                    }
                    float avgResultFloat;
                    if (listOfResults.size() <= 1) {
                        avgResultFloat = sum;
                    } else {
                        avgResultFloat = (float) (sum / (listOfResults.size()));
                    }
                    String avgResultString = String.format("%.2f", avgResultFloat);
                    avgResult.setText("Average Result: " + avgResultString + "/10");

                    //showing the average result in a pie chart
                    showPieChart(pieChart, avgResultFloat, 10,"Correct","Wrong", Color.RED, Color.GREEN);


                    //create line chart of every score
                    showLineChart(listOfScores, lineChart, Color.parseColor("#4D8FF4"));

                    //create line chart of every result
                    showLineChart(listOfResults, lineChart2, Color.parseColor("#ffbf00"));
                }
            }
        });


    }

    //preparing bar chart data
    private void prepareChartData(BarData data) {
        data.setValueTextSize(12f);
        chart.setData(data);
        chart.invalidate();
    }

    //bar chart appearance
    private void configureChartAppearance() {
        chart.getDescription().setEnabled(false);
        chart.setDrawValueAboveBar(false);

        chart.getXAxis().setEnabled(false);

        YAxis axisLeft = chart.getAxisLeft();
        axisLeft.setGranularity(1f);
        axisLeft.setAxisMinimum(0);

        chart.getAxisRight().setEnabled(false);
    }

    //data for the bar charts
    private BarData createChartData(ArrayList<Long> array, String s, int color) {
        ArrayList<BarEntry> values = new ArrayList<>();
        //for each x value, insert a value from the array
        for (int i = 0; i < array.size(); i++) {
            float x = i;
            float y = array.get(i);
            values.add(new BarEntry(x, y));
        }

        //setting up the data then returning it
        BarDataSet set1 = new BarDataSet(values, s);
        set1.setColor(color);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        return data;
    }

    //setting up and creating a pie chart
    private void showPieChart(PieChart pieChart, float avgResult, float total, String label1, String label2, int color1, int color2) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
//        String avgResultString = String.format("%.2f", avgResult);

        //results to be stored in the piechart
        Map<String, Float> results = new HashMap<>();
        results.put(label1, avgResult);
        results.put(label2, total - avgResult);

        //setting appearance of colors
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(color1);
        colors.add(color2);

        //add the desired fields to pie entries
        for (String type : results.keySet()) {
            pieEntries.add(new PieEntry(results.get(type).floatValue(), type));
        }

        //setting up the piechart dataset
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleColor(Color.parseColor("#ffe6c7"));
        pieChart.setTransparentCircleRadius(0f);
        pieChart.invalidate();

    }

    //setting up and creating a line chart
    private void showLineChart(ArrayList<Long> allScores, LineChart lineChart, int color) {
        LineData lineData;
        LineDataSet lineDataSet;
        ArrayList lineEntries = new ArrayList<>();

        //add each item of the array to the line chart entries
        for (int i = 0; i < allScores.size(); i++) {
            lineEntries.add(new Entry(i, allScores.get(i)));
        }

        //setting up the line chart dataset
        lineDataSet = new LineDataSet(lineEntries, "");
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        //setting up appearance
        lineDataSet.setColors(color);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTextSize(12f);

        lineChart.getDescription().setEnabled(false);
        lineChart.getXAxis().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);

        lineChart.getLegend().setEnabled(false);
        lineChart.invalidate();

    }

    //setting the back button
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StatsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}