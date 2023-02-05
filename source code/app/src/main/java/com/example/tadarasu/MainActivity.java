package com.example.tadarasu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView forgotPassword;
    private TextInputLayout EmailInput, PasswordInput;
    private Button Login, register;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = findViewById(R.id.buttonRegister);
        register.setOnClickListener(this);

        Login = findViewById(R.id.buttonLogIn);
        Login.setOnClickListener(this);

        forgotPassword = findViewById(R.id.ResetPassword);
        forgotPassword.setOnClickListener(this);



        EmailInput = findViewById(R.id.EmailInput);
        PasswordInput = findViewById(R.id.PasswordInput);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            if (user.isEmailVerified())
                startActivity(new Intent(MainActivity.this, Home.class));
            else {
                user.sendEmailVerification();
                Toast.makeText(MainActivity.this, "الرجاء تفعيل الحساب من البريد الإلكتروني", Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonRegister:
                startActivity(new Intent(this, Register.class));
                break;
            case R.id.buttonLogIn:
                userLogin();
                break;
            case R.id.ResetPassword:
                startActivity(new Intent(this, ResetPassword.class));
                break;
        }
    }



    private void userLogin() {
        String Email = EmailInput.getEditText().getText().toString().trim();
        String Password = PasswordInput.getEditText().getText().toString().trim();

        if (Email.isEmpty()) {
            EmailInput.setError("البريد الإلكتروني مطلوب");
            EmailInput.requestFocus();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            EmailInput.setError("الرجاء ادخال بريد إلكتروني فعال");
            EmailInput.requestFocus();
            return;
        }
        else
            EmailInput.setError(null);


        if (Password.isEmpty()) {
            PasswordInput.setError("كلمة المرور مطلوبة");
            PasswordInput.requestFocus();
            return;
        }
        else
            PasswordInput.setError(null);

        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified())
                        startActivity(new Intent(MainActivity.this, Home.class));
                    else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "الرجاء تفعيل الحساب من البريد الإلكتروني", Toast.LENGTH_LONG).show();
                    }
                }
                else
                    Toast.makeText(MainActivity.this,"فشل تسجيل الدخول، الرجاء التحقق والمحاولة مرة أخرى",Toast.LENGTH_LONG).show();
            }
        });
    }
}