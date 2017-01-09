package saleelkhan.com.gpstracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Signin extends AppCompatActivity {
    EditText username,password,repassword;
    Button admin,user;
    Database database;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        repassword = (EditText) findViewById(R.id.editText3);
        username.requestFocus();
        user = (Button) findViewById(R.id.button);
        admin = (Button) findViewById(R.id.button2);
        database = new Database(this);
        database.run();
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String password1 = password.getText().toString();
                String password2 = repassword.getText().toString();
                if(!(name.equals(""))&& !(password1.equals(""))&&!(password2.equals("")))
                {
                    if(password1.equals(password2))
                    {

                        database.insertAdminDetails(name,password1);
                        Toast.makeText(Signin.this,"Sucessfully register",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Signin.this,MainActivity.class));
                        finish();
                    }else
                    {
                        Toast.makeText(Signin.this,"Both Passwords Not matched",Toast.LENGTH_SHORT).show();
                        password.setText("");
                        repassword.setText("");
                        password.requestFocus();
                    }
                }else

                {
                    Toast.makeText(Signin.this,"Fill all the columns",Toast.LENGTH_SHORT).show();
                }

            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String password1 = password.getText().toString();
                String password2 = repassword.getText().toString();
                if(!(name.equals(""))&& !(password1.equals(""))&&!(password2.equals("")))
                {
                    if(password1.equals(password2))
                    {
                        String uId = name.substring(0,name.length()/2)+"@"+password1.substring(0,password1.length()/2);
                        database.insertUserDetails(name,password1,uId
                        );
                        Toast.makeText(Signin.this,"Sucessfully register",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Signin.this,MainActivity.class));
                        finish();
                    }else
                    {
                        Toast.makeText(Signin.this,"Both Passwords Not matched",Toast.LENGTH_SHORT).show();
                        password.setText("");
                        repassword.setText("");
                        password.requestFocus();
                    }
                }else

                {
                    Toast.makeText(Signin.this,"Fill all the columns",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

}
