package com.wafflecopter.multicontactpickerexample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CONTACT_PICKER_REQUEST = 991;
    private static final int READ_CONTACTS_REQUEST = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOpenPicker = (Button) findViewById(R.id.btnOpenPicker);

        btnOpenPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    new MultiContactPicker.Builder(MainActivity.this) //Activity/fragment context
                            .theme(R.style.MyCustomPickerTheme) //Optional - default: MultiContactPicker.Azure
                            .hideScrollbar(false) //Optional - default: false
                            .showTrack(true) //Optional - default: true
                            .searchIconColor(Color.WHITE) //Option - default: White
                            .handleColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                            .bubbleColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                            .bubbleTextColor(Color.WHITE) //Optional - default: White
                            .showPickerForResult(CONTACT_PICKER_REQUEST);
                }else{

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS},
                            READ_CONTACTS_REQUEST);

                    //Toast.makeText(MainActivity.this, "Remember to go into settings and enable the contacts permission.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == CONTACT_PICKER_REQUEST){
            if(resultCode == RESULT_OK) {
                List<ContactResult> results = MultiContactPicker.obtainResult(data);
                Log.d("MyTag", results.get(0).getDisplayName());
            } else if(resultCode == RESULT_CANCELED){
                System.out.println("User closed the picker without selecting items.");
            }
        }

        if(resultCode == RESULT_OK && requestCode == READ_CONTACTS_REQUEST) {
            Toast.makeText(MainActivity.this, "granted", Toast.LENGTH_LONG).show();
        }
    }
}
