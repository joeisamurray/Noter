/*
purpose:
learning exercise.
a minimalist text editor.
The code is intended to be reuse in my next bigger project.

current:
share load or append choice Dialog
save file,  shared ->  save to Dialog
permission grant Dialog
 */

package com.psymblyb.noter;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class MainActivity extends AppCompatActivity {
    private static String SharedText = "";
    private static String action;
    private static String type;
    private static Uri uri;
    private static String p;
    private static String permission = WRITE_EXTERNAL_STORAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Noter version", "Permission");
        Toast.makeText(this, "V: Permission", Toast.LENGTH_LONG).show();

        //find xml components
        EditText MEdit = findViewById(R.id.MEdit);

//        // register permission dialog
        ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult
                (
                        new ActivityResultContracts.RequestPermission(), isGranted -> {
                            if (isGranted) {
                                //Log.d("Noter request", " is");
                            } else {
                                //Log.d("Noter request", " isnt");
                            }
                        }
                );
//        // 4 permission already granted to your app?
//        //if (ContextCompat.checkSelfPermission(this, ACCESS_MEDIA_LOCATION) == PERMISSION_DENIED)
            requestPermissionLauncher.launch(permission);



        // initialise intent
        Intent intent = getIntent();
        action = intent.getAction();
        type = intent.getType();

        // if opened directly
        if (Intent.ACTION_VIEW.equals(action)) {
            try {
                uri = intent.getData();
                BufferedReader br = new BufferedReader(new
                        InputStreamReader(getContentResolver().openInputStream(uri), StandardCharsets.UTF_8));
                int I;
                while ((I = br.read()) != -1) {
                    SharedText = SharedText + (char) I;
                }
            } catch (Exception e) {
                Log.d("Noter open view", e.getLocalizedMessage());
            }
        }

        // if opened with shared file
        //if (intent.hasCategory("android.intent.category.DEFAULT")) {
        if (Intent.ACTION_SEND.equals(action)) {
            //Toast.makeText(this, "Opened shared file", Toast.LENGTH_LONG).show();

            // if shared from text from share highlighted text ++
//            if (intent.getStringExtra(Intent.EXTRA_TEXT) != null) {
//                SharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
//                Log.d("Noter extra", SharedText);
//            }

            //AlertDialog(this);

            // actually this handles both
            Log.d("Noter Shared Intent", intent.toString());
            if (intent.getClipData() != null) {
                SharedText = SharedText + intent.getClipData().getItemAt(0).coerceToText(this).toString();
                Log.d("Noter clip", SharedText);
            }

        }

        //Log.d("Noter Final null", String.valueOf(SharedText == null));
        if (SharedText != null) {
            MEdit.setText(SharedText); // set to textbox
            SharedText = "";
        }
    }

    public void FMBSave(View view) {

        if (uri != null) {
            if ( Build.FINGERPRINT.contains("generic") ) p = uri.getLastPathSegment().substring(4); // remove raw: on my files emulator
            else {
                p = uri.getPath().substring(5); // remove root prefix filemanager+
                //my files on samsung external/file/69367 wtf
            }

            Log.d("Noter path substring", p);
        } else {
            // add overwrite dialog
            Log.d("Noter error", "reach");
        }

        try {
            EditText MEdit = findViewById(R.id.MEdit);
            byte[] byteArrray = MEdit.getText().toString().getBytes();

            //requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, 1); // start permission request Activity, not working did it manually
            FileOutputStream fout = new FileOutputStream(p);
            fout.write(byteArrray);
            fout.close();
        } catch (Exception e) {
            Log.d("Noter error", e.getLocalizedMessage());
        } finally {
            Toast.makeText(this, "Saved File", Toast.LENGTH_LONG).show();
        }
    }

    public void FMBUndo(View view) {
        EditText m = findViewById(R.id.MEdit);
        m.onTextContextMenuItem(android.R.id.undo);

    }

    public void FMBRedo(View view) {
        EditText m = findViewById(R.id.MEdit);
        m.onTextContextMenuItem(android.R.id.redo);
    }

    public void FMBPaste(View view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String pasteData = "";
        if (clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            if (item != null) pasteData = item.getText().toString();
        }
        EditText e = findViewById(R.id.MEdit);
        e.setText(e.getText() + pasteData);
    }

}