package com.example.pickfolder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    Button btn, btn1;
    OpenFileDialog fileDialog;

    private static final int SDCARD_PERMISSION = 1,
            FOLDER_PICKER_CODE = 2,
            FILE_PICKER_CODE = 3;

    TextView tvFolder, tvFile;

    private static final int READ_REQUEST_CODE = 42;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //btn = (Button)findViewById(R.id.btn);
        tvFolder = (TextView) findViewById(R.id.txt1);
        //tvFile = (TextView)findViewById(R.id.txt2);

        checkStoragePermission();
        initUI();


        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

    }

    void checkStoragePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //Write permission is required so that folder picker can create new folder.
            //If you just want to pick files, Read permission is enough.

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        SDCARD_PERMISSION);
            }
        }

    }

    void initUI() {

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFolder();
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFiles();
            }
        });

        tvFolder = (TextView) findViewById(R.id.txt1);

    }

    void pickFolder() {
        Intent intent = new Intent(this, FolderPicker.class);
        startActivityForResult(intent, FOLDER_PICKER_CODE);
    }

    void pickFiles() {
        Intent intent = new Intent(this, FolderPicker.class);

        //Optional
        intent.putExtra("title", "Select file to upload");
        intent.putExtra("location", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        intent.putExtra("pickFiles", true);
        //Optional

        startActivityForResult(intent, FILE_PICKER_CODE);
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FOLDER_PICKER_CODE) {

            if (resultCode == Activity.RESULT_OK && intent.hasExtra("data")) {
                String folderLocation = "<b>Selected Folder: </b>" + intent.getExtras().getString("data");
                tvFolder.setText(Html.fromHtml(folderLocation));
            } else if (resultCode == Activity.RESULT_CANCELED) {
                tvFolder.setText("Закончен");
            }

        } else if (requestCode == FILE_PICKER_CODE) {

            if (resultCode == Activity.RESULT_OK && intent.hasExtra("data")) {
                String fileLocation = "<b>Selected File: </b>" + intent.getExtras().getString("data");
                tvFile.setText(Html.fromHtml(fileLocation));
            } else if (resultCode == Activity.RESULT_CANCELED) {
                tvFile.setText("Ok");
            }

        }


    }
}
