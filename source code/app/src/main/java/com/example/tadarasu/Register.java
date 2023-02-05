package com.example.tadarasu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class Register extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private TextInputLayout NameInput, EmailInput, PasswordInput;
    private Button RegisterU;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        NameInput = findViewById(R.id.NameInput);
        EmailInput = findViewById(R.id.EmailInput);
        PasswordInput = findViewById(R.id.PasswordInput);
        RegisterU = findViewById(R.id.buttonSignUp);

        RegisterU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterU();
            }
        });



    }




    private void RegisterU() {
        String Name = NameInput.getEditText().getText().toString().trim();
        String Email = EmailInput.getEditText().getText().toString().trim();
        String Password = PasswordInput.getEditText().getText().toString().trim();

        if (Name.isEmpty()) {
            NameInput.setError("الاسم مطلوب");
            NameInput.requestFocus();
            return;
        }
        else
            NameInput.setError(null);


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
        else if (Password.length() < 6) {
            PasswordInput.setError("الرجاء ادخال كلمة مرور اطول من 6 احرف");
            PasswordInput.requestFocus();
            return;
        }
        else
            PasswordInput.setError(null);

        mAuth.createUserWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(Name,Email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register.this,"تم تسجيل المستخدم",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(Register.this, MainActivity.class));
                                    }

                                    else
                                        Toast.makeText(Register.this,"فشل تسجيل المستخدم!، حاول مجددا",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else
                            Toast.makeText(Register.this,"فشل تسجيل المستخدم",Toast.LENGTH_LONG).show();
                    }
                });
    }
}