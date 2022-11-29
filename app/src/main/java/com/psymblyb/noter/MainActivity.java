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
load text file from user directory - path from app? in emulator?
 */

package com.psymblyb.noter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//import java.io.File;

// todo: add to open with

public class MainActivity extends AppCompatActivity {
    private static String SharedText;
    private static String action;
    private static String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Noter version", "1.2");

        //find xml components
        EditText MEdit = findViewById(R.id.MEdit);

        // initialise intent
        Intent intent = getIntent();
        //Intent intent = new Intent(Intent.ACTION_SEND); ///wrong !!!
        intent.setType("text/plain");
        action = intent.getAction();
        type = intent.getType();

        // if the action is a send and the type is not null
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            //  get text from share highlighted text
            try {
                SharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                //SharedText = intent.getStringExtra(Intent.EXTRA_STREAM);

                if (SharedText != null) MEdit.setText(SharedText); // set to textbox
                if (SharedText == null) Log.d("Noter extra", "null");
                if(intent.getData()==null) Log.d("Noter data", "null");
            } catch (Exception e) {
                Log.d("Noter", e.getLocalizedMessage()); } }
        //print();
        Toast.makeText(this, "Open", Toast.LENGTH_LONG).show();
    }

    private static void print() {
        Log.i("Noter Shared text", SharedText);
        Log.i("Noter action", action);
        Log.i("Noter type", type);
    }

    //needs to be static, belongs to class not an object
    public static boolean validateType() {
        return type==null;
    }

    public void FMBSave(View view) {
        Log.d("Noter", "button clicked");
        Toast.makeText(this, "button clicked", Toast.LENGTH_LONG).show();
    }
}