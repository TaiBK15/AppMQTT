package com.example.mqttapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mqttapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static java.lang.Thread.sleep;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText edt_email, edt_password;
    private CheckBox showpw, remember;
    private Button btn_login;

    private String email, password;
    private boolean isSave, isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences login_state = getSharedPreferences("Login_State", MODE_PRIVATE);
        isLogin = login_state.getBoolean("loginState", false);
        if(isLogin){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        else
        setContentView(R.layout.activity_login);

        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password_login);
        btn_login = findViewById(R.id.btn_login);
        showpw = findViewById(R.id.check_show_password);
        remember = findViewById(R.id.check_remember);

        mAuth = FirebaseAuth.getInstance();

        SharedPreferences account = getSharedPreferences("User_Account", MODE_PRIVATE);
        email = account.getString("email","");
        password = account.getString("password", "");
        isSave = account.getBoolean("remember", false);
        edt_email.setText(email);
        edt_password.setText(password);
        remember.setChecked(isSave);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
            }
        });

        //Set click on checkbox show password
        showpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Show and hide password
                if (showpw.isChecked())
                    edt_password.setTransformationMethod(null);
                else
                    edt_password.setTransformationMethod(new PasswordTransformationMethod());

                edt_password.setSelection(edt_password.getText().length());
            }
        });

        //Set click on checkbox remember me
        remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save email & password
                if (remember.isChecked())
                    isSave = true;
                else
                    isSave = false;
            }
        });
    }

    private void loginUserAccount() {
        email = edt_email.getText().toString();
        password = edt_password.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor account = getSharedPreferences("User_Account", MODE_PRIVATE).edit();
                            account.putBoolean("remember", isSave);
                            if(isSave){
                                account.putString("email", email);
                                account.putString("password", password);
                            }else{
                                account.remove("email");
                                account.remove("password");
                            }
                            account.commit();

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}
