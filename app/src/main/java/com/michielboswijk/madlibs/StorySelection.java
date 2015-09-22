/* Reference package. */
package com.michielboswijk.madlibs;

/* Necessary imports. */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/* Class implements story selection screen. */
public class StorySelection extends Activity {

    /* Method called when story selection screen is started. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* Call constructor of superclass and link layout file. */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_selection);

        /* Initialize different story options. */
        String[] stories = {"Short story", "Tarzan story", "University story", "Clothes story", "Dance story"};

        /* Initialize adapter and link to ListView. */
        final ListAdapter theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stories);
        final ListView storyListView = (ListView) findViewById(R.id.story_list_view);
        storyListView.setAdapter(theAdapter);

        /* Create override method for processing ListView item clicks. */
        storyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /* Get and show selected story. */
                String pickedStory = "You selected " + String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(StorySelection.this, pickedStory, Toast.LENGTH_SHORT).show();

                /* Create intent to direct to next screen. */
                Intent thirdScreen = new Intent(StorySelection.this, InputScreen.class);
                thirdScreen.putExtra("position", position);
                startActivity(thirdScreen);
            }
        });
    }
}