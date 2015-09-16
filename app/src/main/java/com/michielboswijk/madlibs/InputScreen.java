/* Reference package. */
package com.michielboswijk.madlibs;

/* Necessary imports. */
import android.content.Intent;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.app.Activity;
import android.widget.Toast;
import java.io.InputStream;
import android.os.Bundle;
import android.view.View;

/* Class implements the input screen. */
public class InputScreen extends Activity {

    /* Declare private class variables. */
    private Story parser;
    private RadioGroup stories;
    private InputStream stream;
    private TextView categoryView;
    private TextView wordLeftView;
    private EditText textInput;

    /* Method called when input screen is started. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* Call constructor of superclass and link layout file. */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_screen);

        /* Initialize widgets. */
        categoryView = (TextView) findViewById(R.id.category);
        wordLeftView = (TextView) findViewById(R.id.words_left);
        textInput = (EditText) findViewById(R.id.word_input);
        stories = (RadioGroup) findViewById(R.id.group);

        /* Initialize InputStream and make first story default. */
        stream = this.getResources().openRawResource(R.raw.madlib0_simple);
        RadioButton firstButton = (RadioButton) findViewById(R.id.first_radio_button);
        stories.check(firstButton.getId());

        /* Send inputStream to parser. */
        parseStream(stream);
    }

    /* Method called before pausing the activity.
     * Enables correct handling of screen rotation.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {

        /* Call constructor of superclass. */
        super.onSaveInstanceState(outState);

        /* Send checked button id, and parser object to be restored. */
        int sentId = stories.getCheckedRadioButtonId();
        outState.putInt("buttonId", sentId);
        outState.putSerializable("parser", parser);
    }

    /* Method called to retrieve data after pausing of activity.
     * Enables correct handling of screen rotation.
     */
    @Override
    public void onRestoreInstanceState(Bundle inState) {

        /* Call constructor of superclass. */
        super.onRestoreInstanceState(inState);

        /* Retrieve checked button id, and parser object. */
        parser = (Story) inState.getSerializable("parser");
        int receivedId = inState.getInt("buttonId");

        /* Recheck radio button, and display same info as before activity pause. */
        resetViews();
        stories.check(receivedId);
        categoryView.append(" " + parser.getNextPlaceholder());
        wordLeftView.append(" " + parser.getPlaceholderRemainingCount());
    }

    /* Method called when a word is entered for placeholder. */
    public void enterWord(View view) {

        int remaining;

        /* Get input and clear input field. */
        String input = String.valueOf(textInput.getText());
        textInput.setText("");

        /* Check for correct input. */
        if (!input.matches("[a-zA-Z\\s]+")) {
            Toast.makeText(this, R.string.input_message, Toast.LENGTH_LONG).show();
        } else {
            /* Pass input to parser. */
            parser.fillInPlaceholder(input);
        }

        /* Get number of remaining placeholders. */
        remaining = parser.getPlaceholderRemainingCount();

        /* Proceed to next screen when all placeholders are filled. */
        if (remaining == 0) {

            final int result = 1;
            Intent thirdScreen = new Intent(this, StoryScreen.class);
            thirdScreen.putExtra("parser", parser); // Send parser as well (contains story)
            startActivityForResult(thirdScreen, result);
        }

        /* Update category and words left- values. */
        resetViews();
        categoryView.append(" " + parser.getNextPlaceholder());
        wordLeftView.append(" " + remaining);
    }

    /* Method called when refresh button is pressed. */
    public void selectStory(View view) {

        resetViews();

        /* Show which button is selected. */
        RadioButton selected = (RadioButton) findViewById(stories.getCheckedRadioButtonId());
        CharSequence buttonText = selected.getText();
        Toast.makeText(InputScreen.this, "You selected a " + buttonText + " story", Toast.LENGTH_SHORT).show();

        /* Load selected text template and pass it to method parseStream. */
        if (buttonText.toString().equals("Tarzan")) {
            stream = this.getResources().openRawResource(R.raw.madlib1_tarzan);
        } else if (buttonText.toString().equals("University")) {
            stream = this.getResources().openRawResource(R.raw.madlib2_university);
        } else if (buttonText.toString().equals("Clothes")) {
            stream = this.getResources().openRawResource(R.raw.madlib3_clothes);
        } else if (buttonText.toString().equals("Dance")) {
            stream = this.getResources().openRawResource(R.raw.madlib4_dance);
        } else {
            stream = this.getResources().openRawResource(R.raw.madlib0_simple);
        }
        parseStream(stream);
    }

    /* Method for passing stream to story. */
    public void parseStream(InputStream stream) {

        /* Create new story with selected stream and show placeholder information. */
        try {
            parser = new Story(stream);
            categoryView.append(" " + parser.getNextPlaceholder());
            wordLeftView.append(" " + parser.getPlaceholderCount());
        } catch (Exception e) {
            Toast.makeText(this, "Please try a different story", Toast.LENGTH_LONG).show();
        }
    }

    /* Method for resetting the placeholder information. */
    public void resetViews() {
        categoryView.setText(R.string.category);
        wordLeftView.setText(R.string.words_left);
    }
}
