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
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static String SharedText = "";
    private static String action;
    private static String type;
    private static Uri uri = null;
    private static String p;
    private static String permission = WRITE_EXTERNAL_STORAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Noter version", "V: 1.3 Save Content");
        Toast.makeText(this, "V: 1.3 Save Content", Toast.LENGTH_LONG).show();
        pathdetails();

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
//        // 4 permission already granted to your app? dont seem to need it it doesnt keep asking
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

            // actually this handles both
            Log.d("Noter Shared Intent", intent.toString());
            if (intent.getClipData() != null) {
                SharedText = SharedText + intent.getClipData().getItemAt(0).coerceToText(this).toString();
                Log.d("Noter clip", SharedText);
            }
        }

        if (SharedText != null) {
            MEdit.setText(SharedText); // set to textbox
            SharedText = "";
        }
    }

    // new file open a choose file dialog
    public void chooser() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/plain");
        //startActivity(intent); //choose a new file
        startActivityForResult(intent, 1);

        // deprecated, i think im supposed to use a contract
//        ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK) {
//                            Intent intent = result.getData();
//                            // Handle the Intent
//                        }
//                    }
//                });
    }

    // handle result. start activity didnt stop execution and resulted in null pointer when i tried to get intent
    @Override
    protected void onActivityResult (int requestCode,
                                     int resultCode,
                                     Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri = data.getData();
        //extractPath(); no longer need this :)
        if (uri != null) Log.d("Noter chooser uri: ", uri.toString());
        save();
    }

    // save to file
    // a content provider sits between the file and the app
    public void save() {
        try {
            EditText MEdit = findViewById(R.id.MEdit);
            byte[] byteArrray = MEdit.getText().toString().getBytes();

            //FileOutputStream fout = new FileOutputStream(p);
            OutputStream fout = getContentResolver().openOutputStream(uri);
            fout.write(byteArrray);
            fout.close();
        } catch (Exception e) {
            Log.d("Noter error", e.getLocalizedMessage());
        } finally {
            Toast.makeText(this, "saved " + uri.toString(), Toast.LENGTH_LONG).show();
        }
    }

    // save buttoned clicked
    public void FMBSave(View view) {
        // if new document ask for file to save to and on result do the save
        if (uri == null) chooser(); // save to
        // if opened directly just save
        if (uri != null) save(); // if all good save it
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

    // print out some details about paths.
    public void pathdetails()
    {
        Log.d("Noter root dir", Environment.getRootDirectory().toString());
        Log.d("Noter root dir", Environment.getExternalStorageDirectory().toString());
        Log.d("Noter root dir", getExternalFilesDir(null).toString());
        if(uri != null) {
            uri.getPath();
            // uri spec https://www.ietf.org/rfc/rfc2396.txt
            // scheme: //authority /path ?query #fragment
            List<String> segments;
            segments = uri.getPathSegments();
            Log.d("Noter segs", segments.toString());
            String two = segments.get(1);
            Log.d("Noter segs", two); //gives path instead of path segments ! :(
            extractPath();
        }
    }

    // changed to content resolver.
    public void extractPath() {
        if ( Build.FINGERPRINT.contains("generic") ) p = uri.getLastPathSegment().substring(4); // remove raw: on my files emulator
        else {
            p = uri.getPath().substring(5); // remove root prefix filemanager+
            //my files on samsung external/file/69367 wtf
        }
        Log.d("Noter Extracted Path", p);
    }

    // test unit MainActivityTest.java
    //needs to be static, belongs to class not an object
    public static boolean validateType() {
        return type==null;
    }
}