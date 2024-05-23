package com.example.port_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    EditText Email,Password;
    Button Login,Signup;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Email = findViewById(R.id.etEmail);
        Password = findViewById(R.id.etPassword);
        Login = findViewById(R.id.btnLogin);
        Signup = findViewById(R.id.btnSignup);

        db = openOrCreateDatabase("credentials",MODE_PRIVATE,null);
        String query = "CREATE TABLE IF NOT EXISTS clients(id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, password TEXT)";
        db.execSQL(query);

        Signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email1 =  Email.getText().toString().trim();
                String password1 = Password.getText().toString().trim();
                if(email1.isEmpty() || password1.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "you did not enter your credentials", Toast.LENGTH_LONG).show();
                }
                else {

                    String checkQuery = "SELECT * FROM clients WHERE email = ?";
                    Cursor cursor = db.rawQuery(checkQuery, new String[]{email1});
                    if (cursor.getCount() > 0)
                    {
                        Toast.makeText(MainActivity.this, "User name already in use", Toast.LENGTH_LONG).show();
                    } else
                    {
                        String insertQuery = "INSERT INTO clients(email, password) VALUES (?, ?)";
                        db.execSQL(insertQuery, new String[]{email1, password1});
                        Toast.makeText(MainActivity.this, "Sign up successful", Toast.LENGTH_LONG).show();
                    }
                    cursor.close();
                }

            }
        });
        Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email2 = Email.getText().toString().trim();
                String password2 = Password.getText().toString().trim();

                String query = "SELECT * FROM clients WHERE email = '" + email2 + "' AND password = '" + password2 + "'";
                Cursor c = db.rawQuery(query, null);

                if (c.moveToFirst())
                {
                    Email.setText("");
                    Password.setText("");
                    Intent i = new Intent(MainActivity.this, scanner.class);
                    i.putExtra("EMAIL",email2);
                    startActivity(i);

                }
                else
                {
                    Toast.makeText(MainActivity.this, "Invalid user name or password", Toast.LENGTH_LONG).show();
                }


                c.close();
            }
        });

    }
}