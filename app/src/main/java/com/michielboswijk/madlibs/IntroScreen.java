/* Reference package. */
package com.michielboswijk.madlibs;

/* Necessary imports. */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/* Class implements the introduction screen. */
public class IntroScreen extends Activity {

    /* Method called when class is created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* Call constructor of superclass and link layout file. */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);
    }

    /* Method for showing the user how to play the game.
     * Method is called when information button is pressed.
     */
    public void showInformation(View view) {

        /* Create dialog with icon and message. */
        AlertDialog.Builder infoAlert = new AlertDialog.Builder(this);
        String message = getString(R.string.htp);
        infoAlert.setIcon(R.drawable.info_small);
        infoAlert.setMessage(message);

        /* Create button in dialog. */
        infoAlert.setNeutralButton(R.string.got_it, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        /* Set title and show dialog. */
        infoAlert.setTitle(R.string.htp_title).create();
        infoAlert.show();
    }

    /* Method starting a new game; called when play icon is pressed.  */
    public void playGame(View view) {

        Intent secondScreen = new Intent(this, InputScreen.class);
        startActivity(secondScreen);
    }
}