package com.example.tadarasu;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AddContent extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener {


    private TextInputLayout ContentTitleInput, ContentInput, ContentVideoInput
            , ContentSoundInput, ContentNameInput;
    private Button buttonUpPhoto, buttonContent;
    private TextView textView;
    private RadioGroup radioGroup;
    private ImageView imgUpload;
    private Uri imageUri;

    private static final int PICK_IMAGE_REQUEST = 2;
    private BottomNavigationView bottomNavigationView;

    private DatabaseReference myRef;
    private String Category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);

        bottomNavigationView = findViewById(R.id.naviAdBar);
        bottomNavigationView.setSelectedItemId(R.id.addC_BTN);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        myRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        ContentTitleInput = findViewById(R.id.ContentTitleInput);
        ContentInput = findViewById(R.id.ContentInput);
        ContentVideoInput = findViewById(R.id.ContentVideoInput);
        ContentSoundInput = findViewById(R.id.ContentSoundInput);
        ContentNameInput = findViewById(R.id.ContentNameInput);
        buttonUpPhoto = findViewById(R.id.buttonUpPhoto);
        buttonContent = findViewById(R.id.buttonContent);
        imgUpload = findViewById(R.id.imgUpload);
        textView = findViewById(R.id.textView);
        radioGroup = findViewById(R.id.radioGroupCat);


        // Button select img
        buttonUpPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        buttonContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popContent();
            }
        });



    }

    // method convert img to Uri
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgUpload.setImageURI(imageUri);
        }
    }

    // method checked Youtube URL is valid or not (not checked is playable or not)
    public static String extractYouTubeId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()){
            vId = matcher.group(1);
        }
        return vId;
    }

    public void selectCategory(View V){
        switch(V.getId()){
            case R.id.radioButton:
                Category = "فقه";
                break;
            case R.id.radioButton2:
                Category = "عقيدة";
                break;
            case R.id.radioButton3:
                Category = "تفسير";
                break;
        }
    }

    private void popContent() {

        // convert from "TextInputLayout" to "String"
        String Title = ContentTitleInput.getEditText().getText().toString().trim();
        String Content = ContentInput.getEditText().getText().toString().trim();
        String Video = ContentVideoInput.getEditText().getText().toString().trim();
        String Sound = ContentSoundInput.getEditText().getText().toString().trim();
        String Name = ContentNameInput.getEditText().getText().toString().trim();
        String VideoChecked, SoundChecked;



        // checked input values empty or not
        if (Title.isEmpty()) {
            ContentTitleInput.setError("العنوان مطلوب");
            ContentTitleInput.requestFocus();
            return;
        }
        else {
            ContentTitleInput.setError(null);
            ContentTitleInput.setErrorEnabled(false);
        }

        if (Content.isEmpty()&&Video.isEmpty()&&Sound.isEmpty()) {
            ContentInput.setError("الرجاء تعبئة المحتوى نصا او فيديو او صوت");
            ContentVideoInput.setError("الرجاء تعبئة المحتوى نصا او فيديو او صوت");
            ContentSoundInput.setError("الرجاء تعبئة المحتوى نصا او فيديو او صوت");
            ContentInput.requestFocus();
            return;
        }
        else {
            ContentInput.setError(null);
            ContentVideoInput.setError(null);
            ContentSoundInput.setError(null);
            ContentInput.setErrorEnabled(false);
            ContentVideoInput.setErrorEnabled(false);
            ContentSoundInput.setErrorEnabled(false);
        }

        if (radioGroup.getCheckedRadioButtonId()==-1) {
            textView.setError("الرجاء اختيار قسم");
            textView.requestFocus();
            return;
        }
        else {
            textView.setError(null);
        }

        if (Name.isEmpty()||imageUri==null) {
            ContentNameInput.setError("اسم الشيخ / الصورة مطلوبة");
            ContentNameInput.requestFocus();
            return;
        }
        else {
            ContentNameInput.setError(null);
            ContentNameInput.setErrorEnabled(false);
        }

        // checked inputs valid or not
        VideoChecked = extractYouTubeId(Video);
        if (VideoChecked==null&&!Video.isEmpty()) {
            ContentVideoInput.setError("الرجاء وضع رابط فيديو يوتيوب فعال");
            ContentVideoInput.requestFocus();
            return;
        }
        else {
            ContentVideoInput.setError(null);
            ContentVideoInput.setErrorEnabled(false);
        }
        if(!Sound.contains(".mp3")&&!Sound.isEmpty()) {
            ContentSoundInput.setError("الرجاء وضع رابط صوت mp3 فعال");
            ContentSoundInput.requestFocus();
            return;
        }
        else {
            ContentSoundInput.setError(null);
            ContentSoundInput.setErrorEnabled(false);
        }

        Toast.makeText(AddContent.this,"جاري نشر المحتوى",Toast.LENGTH_LONG).show();



        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("post_images");
        final  StorageReference imageFilePath = storageReference.child(imageUri.getLastPathSegment());
        imageFilePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> imageFilePath = taskSnapshot.getStorage().getDownloadUrl();
                imageFilePath.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference newPost = myRef.push();
                        newPost.child("PostID").setValue(newPost.getKey());
                        newPost.child("Title").setValue(Title);
                        newPost.child("Text").setValue(Content);
                        newPost.child("Video").setValue(VideoChecked);
                        newPost.child("Sound").setValue(Sound);
                        newPost.child("Name").setValue(Name);

                        String url = uri.toString();
                        newPost.child("image").setValue(url);
                        newPost.child("Time").setValue(ServerValue.TIMESTAMP);
                        newPost.child("Category").setValue(Category);
                        newPost.child("UserId").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        Toast.makeText(AddContent.this,"تم نشر المحتوى",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AddContent.this, Home.class));


                    }
                });
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        switch (id) {
            case R.id.home_BTN:
                startActivity(new Intent(this,Home.class));
                break;
            case R.id.search_BTN:
                startActivity(new Intent(this,Search.class));
                break;
            case R.id.addC_BTN:
                break;
            case R.id.bookmark_BTN:
                startActivity(new Intent(this,Bookmarks.class));
                break;
        }
        return false;
    }



}