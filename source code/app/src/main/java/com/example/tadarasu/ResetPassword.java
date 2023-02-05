package com.example.tadarasu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private TextInputLayout EmailInput;
    private Button ResetU;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        EmailInput = findViewById(R.id.EmailInput);
        ResetU = findViewById(R.id.buttonReset);

        auth = FirebaseAuth.getInstance();

        ResetU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetU();
            }
        });

    }

    private void resetU() {
        String Email = EmailInput.getEditText().getText().toString().trim();
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

        auth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(ResetPassword.this, "تم ارسال رابط الاستعادة إلى البريد الإلكتروني", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(ResetPassword.this, "االبريد الإلكتروني المدخل غير موجود", Toast.LENGTH_LONG).show();


            }
        });
    }
}