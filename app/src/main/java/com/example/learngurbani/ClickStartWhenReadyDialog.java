package com.example.learngurbani;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//dialog that pops up before beginning a quiz

public class ClickStartWhenReadyDialog {

    private Context mContext;
    private Dialog clickStartWhenReadyDialog;

    //constructor to create dialog
    public ClickStartWhenReadyDialog(Context mContext) {
        this.mContext = mContext;
    }

    //method to show dialog
    public void clickStartWhenReadyDialog(QuizActivity quizActivity) {

        clickStartWhenReadyDialog = new Dialog(mContext);
        clickStartWhenReadyDialog.setContentView(R.layout.click_start_when_ready_dialog);


        //buttons to start the quiz or return to main menu
        final Button buttonStart = (Button) clickStartWhenReadyDialog.findViewById(R.id.button_start);
        final Button buttonGoMainMenu = (Button) clickStartWhenReadyDialog.findViewById(R.id.button_goMainMenu);


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clickStartWhenReadyDialog.dismiss();
                quizActivity.startQuiz();

            }
        });

        buttonGoMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quizActivity.exitFromDialog();


            }
        });

        //showing the dialog
        clickStartWhenReadyDialog.show();
        clickStartWhenReadyDialog.setCancelable(false);
        clickStartWhenReadyDialog.setCanceledOnTouchOutside(false);

    }

}
