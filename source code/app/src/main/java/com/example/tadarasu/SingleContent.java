package com.example.tadarasu;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.HashMap;


public class SingleContent extends AppCompatActivity {

    LinearLayout top_Bar;
    DatabaseReference ref;
    FirebaseAuth mAuth;
    boolean IsInBookmark;
    String Post_ID, Post_type, Post_Text, Post_Sound, Post_Video, Post_Name;
    ImageButton bookmarkBTN, backBTN;

    private ImageView imgPlay, imgPause;
    private SeekBar seekBar;
    private TextView txtProcess;
    private String path =  "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_content);

        imgPlay = findViewById(R.id.imgPlay);
        imgPause = findViewById(R.id.imgPause);
        seekBar = findViewById(R.id.seekBar);
        txtProcess = findViewById(R.id.txtTime);



        top_Bar= findViewById(R.id.topBar);

        TextView ViewText = findViewById(R.id.ViewText);
        TextView Ptitle = findViewById(R.id.PostTitle);
        TextView PCategory = findViewById(R.id.RCat);
        TextView PType = findViewById(R.id.RType);
        TextView PName = findViewById(R.id.NScholar);
        TextView PName2 = findViewById(R.id.Rscol);
        bookmarkBTN = findViewById(R.id.bookmarkBTN);
        backBTN = findViewById(R.id.backBTN);
        backBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Users");

        Post_ID = getIntent().getExtras().getString("ContentID");
        Post_Text = getIntent().getExtras().getString("ContentText");
        Post_Sound = getIntent().getExtras().getString("ContentSound");
        Post_Video = getIntent().getExtras().getString("ContentVideo");
        Post_Name = getIntent().getExtras().getString("ContentName");
        Ptitle.setText(getIntent().getExtras().getString("ContentTitle"));
        PCategory.setText(getIntent().getExtras().getString("ContentCategory"));
        PName.setText(Post_Name);
        PName2.setText(Post_Name);
        ViewText.setText(Post_Text);
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        LinearLayout paddedLinearLayout = findViewById(R.id.paddedLinearLayout);

        if (Post_Video!= null && !Post_Video.isEmpty()) {
            Post_type = "مرئيات";
            paddedLinearLayout.setVisibility(View.GONE);
            getLifecycle().addObserver(youTubePlayerView);
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    String videoId = Post_Video;
                    youTubePlayer.cueVideo(videoId, 0);
                }
            });
        }
        else if (Post_Sound!= null && !Post_Sound.isEmpty()) {
            Post_type = "صوتيات";
            youTubePlayerView.setVisibility(View.GONE);
            VoicePlayer.getInstance(SingleContent.this).init(Post_Sound, imgPlay, imgPause, seekBar, txtProcess);

        }
        else {
            Post_type = "مقالات";
            youTubePlayerView.setVisibility(View.GONE);
            paddedLinearLayout.setVisibility(View.GONE);
        }

        PType.setText(Post_type);

        bookMarkBCheck();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Post_type == "صوتيات")
            VoicePlayer.getInstance(SingleContent.this).onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Post_type == "صوتيات")
            VoicePlayer.getInstance(SingleContent.this).onStop();
    }

    public void bookMarkB(View view) {

        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null) {
            if (user.isEmailVerified()) {
                bookMarkBCheck();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("PostID", "" + Post_ID);

            if (IsInBookmark == true)
                ref.child(mAuth.getUid()).child("Bookmarks").child(Post_ID).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(SingleContent.this, "تمت الازالة الى قائمة الحفظ", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SingleContent.this, "لم تتم الاضافة الى قائمة الحفظ", Toast.LENGTH_LONG).show();

                            }
                        });
            else {
                ref.child(mAuth.getUid()).child("Bookmarks").child(Post_ID).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(SingleContent.this, "تمت الاضافة الى قائمة الحفظ", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SingleContent.this, "لم تتم الاضافة الى قائمة الحفظ", Toast.LENGTH_LONG).show();

                            }
                        });
            }
        }
            else {
                user.sendEmailVerification();
                Toast.makeText(SingleContent.this, "الرجاء تفعيل الحساب من البريد الإلكتروني", Toast.LENGTH_LONG).show();
            }
        }
        else
            Toast.makeText(SingleContent.this, "يرجى تسجيل الدخول من أجل الاضافة",Toast.LENGTH_LONG).show();

    }

    public void bookMarkBCheck() {
        ref.child(mAuth.getUid()).child("Bookmarks").child(Post_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    IsInBookmark = true;
                    bookmarkBTN.setImageResource(R.drawable.ic_baseline_bookmark_24);
                }
                else {
                    IsInBookmark = false;
                    bookmarkBTN.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}