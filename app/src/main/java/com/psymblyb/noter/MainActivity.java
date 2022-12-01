/*
purpose:
learning exercise.
a minimalist text editor.
The code is intended to be reuse in my next bigger project.

current:
save file,  shared ->  save to, open directly use uri to save the file.
read, char instead to maintain carriage return
add widget code
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

public class MainActivity extends AppCompatActivity {
    private static String SharedText = "";
    private static String action;
    private static String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Noter version", "Load and Save");
        Toast.makeText(this, "V: Load and Save", Toast.LENGTH_LONG).show();

        //find xml components
        EditText MEdit = findViewById(R.id.MEdit);

        // initialise intent
        Intent intent = getIntent();
        action = intent.getAction();
        type = intent.getType();

        // if opened directly
        if(Intent.ACTION_VIEW.equals(action)) {
            try {
                Uri uri = intent.getData();
                BufferedReader br = new BufferedReader(new
                InputStreamReader(getContentResolver().openInputStream(uri), "UTF-8"));
                String Line;
                while( (Line = br.readLine()) != null) SharedText = SharedText + Line;
            } catch (Exception e) {
                Log.d("Noter open view", e.getLocalizedMessage()); }
        }

        // if opened with shared file
        if( intent.hasCategory("android.intent.category.DEFAULT") ){
            Log.d("Noter", "If Shared file");
            Toast.makeText(this, "Opened shared file", Toast.LENGTH_LONG).show();

            Log.d("Noter Shared Intent", intent.toString());
            if(intent.getClipData() != null)
            {
                SharedText = intent.getClipData().getItemAt(0).coerceToText(this).toString();
                Log.d("Noter SharedText", "Shared Text");
            }
            // if shared from text from share highlighted text ++
            if (intent.getStringExtra(Intent.EXTRA_TEXT) != null) {
                SharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                Log.d("Noter", "Highlighted text");
            }
        }

        Log.d("Noter Final null", String.valueOf(SharedText==null));
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