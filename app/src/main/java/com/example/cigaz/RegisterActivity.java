package com.example.cigaz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    EditText editTextFirstName;
    EditText editTextLastName;
    EditText editTextMeterNumber;
    EditText editTextEmailAddress;
    EditText editTextPassword;
    EditText editTextPasswordAgain;
    Button registerBtn;
    Vibrator vibrator;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = findViewById(R.id.registerButton);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 99) {
            finish();
        }

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextMeterNumber = findViewById(R.id.editTextMeterNumber);
        editTextEmailAddress = findViewById(R.id.editTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordAgain = findViewById(R.id.editTextPasswordAgain);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String emailAddress = preferences.getString("emailAddress", "");
        String password = preferences.getString("password", "");

        editTextEmailAddress.setText(emailAddress);
        editTextPassword.setText(password);
        editTextPasswordAgain.setText(password);

        mAuth = FirebaseAuth.getInstance();

        Log.i(LOG_TAG, "onCreate");
    }

    //E-mail formátum ellenörzés
    private boolean isValidEmailId(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public void register(View view) {
        String pattern = "^[a-z0-9_-]{5,15}$";

        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String meterNumber = editTextMeterNumber.getText().toString();
        String emailAddress = editTextEmailAddress.getText().toString();
        String password = editTextPassword.getText().toString();
        String passwordAgain = editTextPasswordAgain.getText().toString();

        if (firstName.length() == 0) {
            Log.e(LOG_TAG, "Hiányzik a vezetéknév!");
            return;
        }

        if (lastName.length() == 0) {
            Log.e(LOG_TAG, "Hiányzik az utónév!");
            return;
        }

        if (meterNumber.length() != 10) {
            Log.e(LOG_TAG, "A gyári számnak 10 karakter hosszúnak kell lennie!");
            return;
        }

        if(!isValidEmailId(editTextEmailAddress.getText().toString().trim())){
            Log.e(LOG_TAG, "Hibás e-mail formátum!");
            return;
        }

        if (!password.equals(passwordAgain)) {
            Log.e(LOG_TAG, "A két jelszó nem egyezik!");
            return;
        }

        if (password.length() < 6) {
            Log.e(LOG_TAG, "A jelszó túl rövid (min. 6 karakter)!");
            return;
        }

        if (password.length() > 15) {
            Log.e(LOG_TAG, "A jelszó túl hosszú (max. 15 karakter!)");
            return;
        }

        Log.i(LOG_TAG, "Regisztrált: " + firstName + " " + lastName);

        //Regisztráció
        mAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "Fiók sikeresen létrehozva!");
                    vibrator.vibrate(1000);
                    goHome();
                } else {
                    Log.d(LOG_TAG, "A fiók létrehozása sikertelen!");
                    Toast.makeText(RegisterActivity.this, "A fiók létrehozása sikertelen: " + task.getException().getMessage() , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void goHome(/**/) {
        Intent intent = new Intent(this, HomeActivity.class);
        //intent.putExtra("SECRET_KEY", SECRET_KEY);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }
}