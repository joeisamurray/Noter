/*
purpose:
learning exercise.
a minimalist text editor.
The code is intended to be reuse in my next bigger project.

current:
connect save button to function
share file

current issues:
will share a clipboard text
will not share a file from the file browser

next:
Java File IO
load textfile from user directory - path from app? in emulator?
 */

package com.psymblyb.noter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import java.io.File;

// todo: add to open with

public class MainActivity extends AppCompatActivity {
    private String SharedText;
    private EditText textbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("version", "1.1");
        textbox = findViewById(R.id.gettext); // assign reference of textbox

        // initialise intent
        Intent intent = getIntent();
        //Intent intent = new Intent(Intent.ACTION_SEND); ///wrong !!!
        intent.setType("text/plain");
        String action = intent.getAction();
        String type = intent.getType();

        // if the action is a send and the type is not null
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            //  get text from share highlighted text
            try {
                SharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                //SharedText = intent.getStringExtra(Intent.EXTRA_STREAM);

                if (SharedText != null) textbox.setText(SharedText.toString()); // set to textbox
                if (SharedText == null) Log.d("m extra", "null");
                if(intent.getData()==null) Log.d("m data", "null");
            } catch (Exception e) {
                Log.d("m catch s", e.getLocalizedMessage()); } }
    }

    private void save(View view) {
        Log.i("button", "clicked");
        Log.d("m Button", "cicked");
    }
}