package co.edu.unal.tiendabd.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import co.edu.unal.tiendabd.MainActivity;
import co.edu.unal.tiendabd.R;

public class Login extends AppCompatActivity implements View.OnClickListener {
    // Instance Firebase
    private FirebaseAuth mAuth;
    // Edit text Login
    private EditText etxtEmail, etxtPassword;
    // Button Login and Register
    private Button btnRegister, btnLogin;
    // String login
    public String email = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initializing FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        // Edit text Login
        etxtEmail = (EditText) findViewById(R.id.etxtEmail);
        etxtPassword = (EditText) findViewById(R.id.etxtPassword);
        // Button Register
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        // Button Login
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);


    }

    // Register the user with email and password
    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "User has been registered with email: "+etxtEmail.getText(), Toast.LENGTH_SHORT).show();

                        } else {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                Toast.makeText(Login.this, "Existing user", Toast.LENGTH_SHORT).show();

                            }else{
                            Toast.makeText(Login.this, "User could not be registered", Toast.LENGTH_SHORT).show();
                        }}

                        // ...
                    }
                });
    }

    //login with email and password
    private void loginUser() {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Welcome user with email: "+email, Toast.LENGTH_SHORT).show();

                            etxtEmail.setText("");
                            etxtPassword.setText("");
                            SharedPreferences prefs = getSharedPreferences("shared_login_data",   Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            Intent i= new Intent(Login.this,MainActivity.class);
                            startActivity(i);
                            editor.putString("email", email);
                            editor.commit();
                        } else {
                                Toast.makeText(Login.this, "\n" +
                                        "failed to login", Toast.LENGTH_SHORT).show();
                            }

                        // ...
                    }
                });
    }
    //Capture the id of which the event was generated
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                nullStringRegister();

                break;

            case R.id.btnLogin:
                nullStringLogin();

                break;
        }
    }

    // convert email to String
    private String convertEmailToString(String Email) {
        String value = Email.substring(0, Email.indexOf('@'));
        value = value.replace(".", "");
        return value;
    }

    // Check that the fields are not empty
    private void nullStringRegister() {
        email = etxtEmail.getText().toString().trim();
        password = etxtPassword.getText().toString().trim();
        if (!email.isEmpty() && !password.isEmpty()) {
            registerUser();


        } else {
            Toast.makeText(Login.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    // Check that the fields are not empty
    private void nullStringLogin() {
        email = etxtEmail.getText().toString().trim();
        password = etxtPassword.getText().toString().trim();
        if (!email.isEmpty() && !password.isEmpty()) {
            loginUser();


        } else {
            Toast.makeText(Login.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }
}