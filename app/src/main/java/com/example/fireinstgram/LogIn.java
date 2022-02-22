package com.example.fireinstgram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {
EditText email , passWord;
FirebaseAuth auth;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    email = findViewById(R.id.EmailLogIn);
    passWord = findViewById(R.id.PasswordLogIn);
auth = FirebaseAuth.getInstance();

    }

    public void SignIn(View view) {
String Email = email.getText().toString();
        String PassWord = passWord.getText().toString();
if (TextUtils.isEmpty(Email) || TextUtils.isEmpty(PassWord)){
    Toast.makeText(this, "fell the boxes", Toast.LENGTH_SHORT).show();
}else {
    Login(Email , PassWord);
}
    }

    private void Login(String email, String passWord) {
auth.signInWithEmailAndPassword(email,passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
   if (task.isSuccessful()){
       Toast.makeText(LogIn.this, "Uploading profile data", Toast.LENGTH_SHORT).show();
       Intent intent = new Intent(LogIn.this,MainPage.class);
       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
       startActivity(intent);
       finish();
   }
    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(LogIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
});
}

    public void SignUP(View view) {
startActivity(new Intent(this,Registration.class));

    }
}
