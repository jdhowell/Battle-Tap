package com.jhowell.battletap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseHandler db;
    private Button login, register;
    private String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization of the database
        db = new DatabaseHandler(this);

        // Initialization of buttons
        login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(this);
        register = (Button) findViewById(R.id.register_button);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.login_button:
                // Check database to see if username and password are correct
                EditText loginName = (EditText) findViewById(R.id.login_username);
                username = loginName.getText().toString();

                // If username exists, check password
                if (db.contains(username)) {
                    EditText loginPass = (EditText) findViewById(R.id.login_password);
                    password = loginPass.getText().toString();

                    // If password is correct, login and start game
                    if (password.equals(db.getPlayer(username).getPassword())) {
                        Intent intent = new Intent(this, HomeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("player", db.getPlayer(username));
                        intent.putExtras(bundle);
                        startActivity(intent);

                    } else {
                        Toast.makeText(this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this,"Username does not exist", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.register_button:
                EditText registerName = (EditText) findViewById(R.id.register_username);
                username = registerName.getText().toString();

                // Check if username is available
                if (db.isAvailable(username)) {
                    EditText registerPass = (EditText) findViewById(R.id.register_password);
                    password = registerPass.getText().toString();
                    EditText confirmPass = (EditText) findViewById(R.id.register_confirm_password);
                    String confirmPassword = confirmPass.getText().toString();

                    // Check if passwords match
                    if (password.equals(confirmPassword)) {
                        Player player = new Player(username, password);
                        db.addPlayer(player);
                        Intent intent = new Intent(this, HomeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("player", player);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    } else {
                        Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Username is taken", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}