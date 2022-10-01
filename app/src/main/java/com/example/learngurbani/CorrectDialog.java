package com.example.learngurbani;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//dialog that pops up when user gets an answer correct in the quiz

public class CorrectDialog {

    private Context mContext;
    private Dialog correctDialog;

    private final Handler handler = new Handler();

    //constructor to create dialog
    public CorrectDialog(Context mContext) {
        this.mContext = mContext;
    }

    //method to show dialog
    public void correctDialog(int points) {
        correctDialog = new Dialog(mContext);
        correctDialog.setContentView(R.layout.correct_answer_dialog);

        //showing the points achieved in the textview
        TextView txtScore = (TextView) correctDialog.findViewById(R.id.txt_score);
        txtScore.setText(String.valueOf(points) + " points!");

        //display the dialog
        correctDialog.show();
        correctDialog.setCancelable(false);
        correctDialog.setCanceledOnTouchOutside(false);

        //the dialog is shown for 1600ms before moving onto the next question
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                correctDialog.dismiss();
            }
        }, 1600);

    }


}
