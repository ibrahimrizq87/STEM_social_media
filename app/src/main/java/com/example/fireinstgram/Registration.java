package com.example.fireinstgram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Registration extends AppCompatActivity {

    private Uri imageUri;
    private String imageUrl;
private ImageView imageAdded ;
    private EditText username;
    private EditText name;
    private EditText email;
    private EditText password;
    StorageReference storageReference ;
    FirebaseStorage storage;
    private Button register;
    private TextView loginUser;

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
storage = FirebaseStorage.getInstance();
storageReference = storage.getReference().child("profile");

        imageAdded = findViewById(R.id.image_profile1111);

        username = findViewById(R.id.UsernameRegistration);
        name = findViewById(R.id.nameRegistration);
        email = findViewById(R.id.EmailRegistration);
        password = findViewById(R.id.PasswordRegistration);
        register = findViewById(R.id.registrationButton);
        loginUser = findViewById(R.id.textRegistration);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);

        imageAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity().start(Registration.this);

            }
        });
        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this , LogIn.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUsername = username.getText().toString();
                String txtName = name.getText().toString();
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();
                if(imageUri != null){
                    final StorageReference imageName = storageReference.child("image"+ imageUri.getLastPathSegment());
                    imageName.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = String.valueOf(uri);
                                 FirebaseDatabase.getInstance().getReference().child("Users").child("imageUrl").setValue(imageUrl);
                                    Toast.makeText(Registration.this, imageUrl, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });


                }
                if (TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtName)
                        || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)){
                    Toast.makeText(Registration.this, "Empty credentials!", Toast.LENGTH_SHORT).show();
                } else if (txtPassword.length() < 6){
                    Toast.makeText(Registration.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txtUsername , txtName , txtEmail , txtPassword);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            imageAdded.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Registration.this , MainActivity.class));
            finish();
        }
    }



    private void registerUser(final String username, final String name, final String email, String password ) {
        pd.setMessage("Please Wail!");
        pd.show();

        mAuth.createUserWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                HashMap<String , Object> map = new HashMap<>();
                map.put("name" , name);
                map.put("email", email);
                map.put("username" , username);

                map.put("id" , mAuth.getCurrentUser().getUid());
                map.put("bio" , "");



                mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            pd.dismiss();
                            Toast.makeText(Registration.this, "Update the profile " +
                                    "for better expereince", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Registration.this , MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(Registration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
