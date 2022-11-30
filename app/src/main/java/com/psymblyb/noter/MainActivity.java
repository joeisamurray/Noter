/*
purpose:
learning exercise.
a minimalist text editor.
The code is intended to be reuse in my next bigger project.

current:
save file
open with, read uri buffered
 */

package com.psymblyb.noter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
        Log.i("Noter version", "open with 1.0");

        //find xml components
        EditText MEdit = findViewById(R.id.MEdit);

        // initialise intent
        Intent intent = getIntent();
        //Intent intent = new Intent(Intent.ACTION_SEND); // creates open dialog
        intent.setType("text/plain");
        action = intent.getAction();
        type = intent.getType();

        if(Intent.ACTION_VIEW.equals(action)) {
            try {
                Uri uri= getIntent().getData();
                BufferedReader br = new BufferedReader(new
                InputStreamReader(getContentResolver().openInputStream(uri), "UTF-8"));
                SharedText = br.readLine();
            } catch (Exception e) {
                Log.d("Noter open view", e.getLocalizedMessage()); }
        }

        if( intent.hasCategory("android.intent.category.DEFAULT") ){
            intent = getIntent();
            Toast.makeText(this, "Opened shared file", Toast.LENGTH_LONG).show();

            if(intent!=null) {
                Log.d("Noter Shared Intent", intent.toString());
                if(intent.getClipData() != null)
                {
                    SharedText = intent.getClipData().getItemAt(0).coerceToText(this).toString();
                    Log.d("Noter SharedText", SharedText);
                }
            }
        }

        // if shared from text from share highlighted text
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            try {
                SharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            } catch (Exception e) {
                Log.d("Noter", e.getLocalizedMessage()); } }

        Toast.makeText(this, "Open", Toast.LENGTH_LONG).show();
        if (SharedText != null) MEdit.setText(SharedText); // set to textbox
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