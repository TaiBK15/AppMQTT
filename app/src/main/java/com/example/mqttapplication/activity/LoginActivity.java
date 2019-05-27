package com.example.mqttapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mqttapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import static java.lang.Thread.sleep;

public class LoginActivity extends AppCompatActivity {
    final String TAG = "Login Activity";
    private FirebaseAuth mAuth;
    private EditText edt_email, edt_password;
    private CheckBox showpw, remember;
    private Button btn_login;
    private ProgressBar prBar;

    private String email, password;
    private boolean isSave, isLogin, isShowpw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences login_state = getSharedPreferences("Login_State", MODE_PRIVATE);
        isLogin = login_state.getBoolean("loginState", false);
        if(isLogin){
           goToMainActivity();
        }
        else {
            setContentView(R.layout.activity_login);

            mAuth = FirebaseAuth.getInstance();

            edt_email = findViewById(R.id.edt_email);
            edt_password = findViewById(R.id.edt_password_login);
            btn_login = findViewById(R.id.btn_login);
            showpw = findViewById(R.id.check_show_password);
            remember = findViewById(R.id.check_remember);
            prBar = findViewById(R.id.progressBarLogin);

            SharedPreferences account = getSharedPreferences("User_Account", MODE_PRIVATE);
            email = account.getString("email", "");
            password = account.getString("password", "");
            isSave = account.getBoolean("remember", false);
            isShowpw = account.getBoolean("showpw", false);
            edt_email.setText(email);
            edt_password.setText(password);
            remember.setChecked(isSave);
            showpw.setChecked(isShowpw);

            //Show and hide password
            if (showpw.isChecked())
                edt_password.setTransformationMethod(null);
            else
                edt_password.setTransformationMethod(new PasswordTransformationMethod());

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
    }

    private void goToMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void loginUserAccount() {
        prBar.setVisibility(View.VISIBLE);

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

        mAuth.signInWithEmailAndPassword(email + "@gmail.com", password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor account = getSharedPreferences("User_Account", MODE_PRIVATE).edit();
                            account.putBoolean("remember", isSave);
                            account.putBoolean("showpw", showpw.isChecked());
                            if(isSave){
                                account.putString("email", email);
                                account.putString("password", password);
                            }else{
                                account.remove("email");
                                account.remove("password");
                            }
                            account.commit();

                            prBar.setVisibility(View.GONE);

                            goToMainActivity();
                        }
                        else {
                            prBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        closeKeyboard();
    }

    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



}
