/* Reference package. */
package com.michielboswijk.madlibs;

/* Necessary imports. */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/* Class for displaying the story to the user. */
public class StoryScreen extends Activity {

    /* Declare variables used in class. */
    private Story parser;
    private TextView storyView;
    private String currentStory;

    /* Method called when page is initialized. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* Call constructor of superclass and link layout file. */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_screen);

        /* Initialize widget for story display and receive parser from previous activity. */
        storyView = (TextView) findViewById(R.id.story_view);
        Intent calledActivity = getIntent();
        parser = (Story) calledActivity.getSerializableExtra("parser");
        currentStory = parser.toString();
        storyView.setText(currentStory);

    }

    /* Method called before pausing the activity.
     * Enables correct handling of screen rotation.
     */

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        /* Pass current story. */
        outState.putString("story", currentStory);

    }

    /* Method called to retrieve data after pausing of activity.
     * Enables correct handling of screen rotation.
     */
    @Override
    public void onRestoreInstanceState(Bundle inState) {

        super.onRestoreInstanceState(inState);

        /* Retrieve and set current story. */
        currentStory = inState.getString("story");
        storyView.setText(currentStory);
    }

    /* Method for saving your current story. */
    public void saveStory(View view) {

        Toast.makeText(this, R.string.save_message, Toast.LENGTH_LONG).show();
        SharedPreferences prefs = this.getSharedPreferences("story", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("newStory", parser.toString()); // Save text of story.
        editor.commit();
    }

    /* Method for loading your previously saved story. */
    public void loadStory(View view) {

        Toast.makeText(this, R.string.load_message, Toast.LENGTH_LONG).show();
        SharedPreferences prefs = this.getSharedPreferences("story", MODE_PRIVATE);
        currentStory = prefs.getString("newStory", "222"); // Get text of saved story.

        /* Check if previous story is available. */
        if(currentStory.equals("222")) {
            currentStory = getString(R.string.no_story_message);
        }

        /* Display previously saved story. */
        storyView.setText(currentStory);
    }

    /* Method for starting a new game. */
    public void newGame(View view) {

        Toast.makeText(this, R.string.new_game_message, Toast.LENGTH_LONG).show();
        Intent newGame = new Intent(this, StorySelection.class);
        startActivity(newGame);
    }
}