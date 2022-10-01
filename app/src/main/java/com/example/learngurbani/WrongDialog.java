package com.example.learngurbani;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//dialog that pops up when user gets an answer incorrect in the quiz

public class WrongDialog {

    private Context mContext;
    private Dialog wrongDialog;

    private final Handler handler = new Handler();

    //constructor to create dialog
    public WrongDialog(Context mContext) {
        this.mContext = mContext;
    }

    //method to show dialog
    public void wrongDialog(String correctAnswer){
        wrongDialog = new Dialog(mContext);
        wrongDialog.setContentView(R.layout.wrong_answer_dialog);

        //showing the user what the correct was
        TextView txtCorrectAnswer = (TextView) wrongDialog.findViewById(R.id.txt_correct_answer);
        txtCorrectAnswer.setText(correctAnswer);

        //display the dialog
        wrongDialog.show();
        wrongDialog.setCancelable(false);
        wrongDialog.setCanceledOnTouchOutside(false);

        //the dialog is shown for 1600ms before moving onto the next question
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                wrongDialog.dismiss();
            }
        },1600);
    }


}
