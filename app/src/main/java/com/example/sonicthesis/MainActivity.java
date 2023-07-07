package com.example.sonicthesis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button listennow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listennow=findViewById(R.id.listennow);
    }
    public void listennow(View v)
    {

        Toast.makeText(this, "WELCOME", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this,songInterface.class);

        startActivity(i);
    }

}
