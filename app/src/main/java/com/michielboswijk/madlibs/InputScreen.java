/* Reference package. */
package com.michielboswijk.madlibs;

/* Necessary imports. */
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;
import java.io.InputStream;
import java.util.ArrayList;
import android.os.Bundle;
import android.view.View;

/* Class implements the input screen. */
public class InputScreen extends Activity {

    /* Declare private class variables. */
    private Story parser;
    private InputStream stream;
    private TextView categoryView;
    private TextView wordLeftView;
    private EditText textInput;
    private TextToSpeech tts;
    private boolean ttsReady;


    //**********************************************************************************************
    // Override methods.
    //**********************************************************************************************


    /* Method called when input screen is started. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* Call constructor of superclass and link layout file. */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_screen);

        /* Initialize widgets and variables. */
        categoryView = (TextView) findViewById(R.id.category);
        wordLeftView = (TextView) findViewById(R.id.words_left);
        textInput = (EditText) findViewById(R.id.word_input);
        ttsReady = false;

        /* Initialize text to speech object. */
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                ttsReady = true;
            }
        });

        /* Receive data from previous activity to select story. */
        Bundle extras = getIntent().getExtras();
        int position = extras.getInt("position");
        selectStory(position);
    }

    /* Method called before pausing the activity.
     * Enables correct handling of screen rotation.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {

        /* Call constructor of superclass. */
        super.onSaveInstanceState(outState);

        /* Send parser object to be restored. */
        outState.putSerializable("parser", parser);
    }

    /* Method called to retrieve data after pausing of activity.
     * Enables correct handling of screen rotation.
     */
    @Override
    public void onRestoreInstanceState(Bundle inState) {

        /* Call constructor of superclass. */
        super.onRestoreInstanceState(inState);

        /* Retrieve parser object. */
        parser = (Story) inState.getSerializable("parser");

        /* Update print information. */
        updateInfo();
    }

    /* Method for receiving and processing spoken data. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        /* Receive and process spoken text. */
        ArrayList<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        String spokenText = list.get(0);
        processInput(spokenText);
        Toast.makeText(this, "You said: " + spokenText, Toast.LENGTH_LONG).show();
    }

    //**********************************************************************************************
    // Helper methods.
    //**********************************************************************************************


    /* Method called when a word is entered for placeholder. */
    public void enterWord(View view) {

        /* Get input and clear input field. */
        String input = String.valueOf(textInput.getText());
        textInput.setText("");

        /* Check for correct input. */
        if (!input.matches("[a-zA-Z\\s]+")) {
            Toast.makeText(this, R.string.input_message, Toast.LENGTH_LONG).show();
        } else {
            /* Pass input to method for processing input. */
            processInput(input);
        }
    }

    /* Method for handling the spoken/written input of the user. */
    public void processInput(String input) {

        /* Send input to parser. */
        parser.fillInPlaceholder(input);

        /* Proceed to next screen when all placeholders are filled. */
        if (parser.getPlaceholderRemainingCount() == 0) {

            final int result = 1;
            Intent fourthScreen = new Intent(this, StoryScreen.class);
            fourthScreen.putExtra("parser", parser); // Send parser as well (contains story)
            startActivityForResult(fourthScreen, result);
            finish();
        /* If tts is ready, prompt the required word type through spoken text. */
        } else if (ttsReady) {
            tts.speak("Please enter a " + parser.getNextPlaceholder().toLowerCase(), TextToSpeech.QUEUE_FLUSH, null);
        }

        /* Update category and words left- values. */
        updateInfo();
    }

    /* Method for selecting correct story template. */
    public void selectStory(int pos) {

        /* Select chosen template. */
        switch (pos) {
            case 0:
                stream = this.getResources().openRawResource(R.raw.madlib0_simple); break;
            case 1:
                stream = this.getResources().openRawResource(R.raw.madlib1_tarzan); break;
            case 2:
                stream = this.getResources().openRawResource(R.raw.madlib2_university); break;
            case 3:
                stream = this.getResources().openRawResource(R.raw.madlib3_clothes); break;
            case 4:
                stream = this.getResources().openRawResource(R.raw.madlib4_dance); break;
        }
        /* Create new story with selected stream and show placeholder information. */
        try {
            parser = new Story(stream);
            updateInfo();
        } catch (Exception e) {
            Toast.makeText(this, "Please try a different story", Toast.LENGTH_LONG).show();
        }
    }

    /* Method for showing placeholder information. */
    public void updateInfo() {
        categoryView.setText(R.string.category);
        wordLeftView.setText(R.string.words_left);
        categoryView.append(" " + parser.getNextPlaceholder().toLowerCase());
        wordLeftView.append(" " + parser.getPlaceholderRemainingCount());
    }

    /* Method for re-selecting story, takes user back to selection screen. */
    public void changeStory(View view) {
        Intent fourthScreen = new Intent(this, StorySelection.class);
        startActivity(fourthScreen);
    }

    /* Method for starting text to speech functionality. */
    public void speechToText(View view) {

        final int result = 1;

        /* Initialize intent for speech to text handling. */
        Intent convertSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        convertSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

        /* Attempt to start the speech intent (works if stt is supported). */
        try {
            startActivityForResult(convertSpeech, result);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Speech to text not supported", Toast.LENGTH_LONG).show();
        }
    }
}