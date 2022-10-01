package com.example.learngurbani;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

//dialog shown before starting flashcard learning, gives extra info and option to START or EXIT

public class FlashcardDialog extends AppCompatActivity {

    private Context mContext;
    private Dialog fcDialog;

    //constructor to make dialog
    public FlashcardDialog(Context mContext) {
        this.mContext = mContext;
    }

    //method to set up dialog
    public void fcDialog(FlashcardActivity fcActivity) {

        fcDialog = new Dialog(mContext);
        fcDialog.setContentView(R.layout.activity_flashcard_dialog);

        //buttons to begin flashcards or to exit to the main menu
        final Button buttonStart = (Button) fcDialog.findViewById(R.id.fc_button_start);
        final Button buttonGoMainMenu = (Button) fcDialog.findViewById(R.id.fc_button_goMainMenu);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fcDialog.dismiss();
                fcActivity.startFlashcards();

            }
        });

        buttonGoMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fcActivity.exitFromDialog();

            }
        });

        //showing and setting the dialog
        fcDialog.show();
        fcDialog.setCancelable(false);
        fcDialog.setCanceledOnTouchOutside(false);

    }
}