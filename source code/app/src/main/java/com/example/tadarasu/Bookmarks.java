package com.example.tadarasu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Bookmarks extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private DatabaseReference database, ref, userReference;
    private BottomNavigationView bottomNavigationView;
    private myadapter bookmarkAdapter;
    private ArrayList<Content> list;
    private FirebaseAuth mAuth;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        bottomNavigationView = findViewById(R.id.naviGBar);
        bottomNavigationView.setSelectedItemId(R.id.bookmark_BTN);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Users");

        recyclerView = findViewById(R.id.bookMarkList);
        TextView name = findViewById(R.id.nameAcc);
        TextView email = findViewById(R.id.emailAcc);
        database = FirebaseDatabase.getInstance().getReference("Posts");


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userReference = ref.child(uid);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText("الاسم: "+ dataSnapshot.child("name").getValue(String.class));
                email.setText("الايميل: "+ dataSnapshot.child("email").getValue(String.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        bookmarkAdapter = new myadapter(this,list);
        recyclerView.setAdapter(bookmarkAdapter);

        ref.child(mAuth.getUid()).child("Bookmarks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearData();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String PostID = "" + dataSnapshot.getRef().getKey();
                    listA(PostID);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        }


    public void clearData() {
        recyclerView.removeAllViewsInLayout();
        list.clear();
        bookmarkAdapter.notifyDataSetChanged();
    }


    private void listA(final String postId) {

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Content content = dataSnapshot.getValue(Content.class);
                    if (content.getPostID().equals(postId)) {
                        Collections.reverse(list);
                        list.add(content);
                        Collections.reverse(list);
                    }
                }
                bookmarkAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                startActivity(new Intent(this,AddContent.class));
                break;
            case R.id.bookmark_BTN:
                break;
        }
        return false;
    }


    public void popupChangeName(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_change_name, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button button = popupView.findViewById(R.id.BTNChangeName);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout NameN = popupView.findViewById(R.id.newNameInput);
                String newName = NameN.getEditText().getText().toString().trim();
                if (newName.isEmpty()) {
                    NameN.setError("الاسم الجديد مطلوب");
                    return;
                }
                else {
                    ref.child(uid).child("name").setValue(newName);
                    Toast.makeText(Bookmarks.this,"تم تحديث الاسم بنجاح",Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(getIntent());
                }
            }
        });
        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void popupDeleteAcc(View view) {

        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_delete_acc, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button Yes = popupView.findViewById(R.id.DeleteYes);
        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout Password = popupView.findViewById(R.id.PasswordCheck);
                String password = Password.getEditText().getText().toString().trim();
                if (password.isEmpty()) {
                    Password.setError("الاسم الجديد مطلوب");
                    return;
                }
                else
                    delete_acc(password);
            }
        });

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }


    private void delete_acc(String Pass) {
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    AuthCredential credential = EmailAuthProvider
            .getCredential(mAuth.getCurrentUser().getEmail(), Pass);

    user.reauthenticate(credential)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Bookmarks.this, "تم حذف الحساب", Toast.LENGTH_LONG).show();
                                            userReference.removeValue();
                                            startActivity(new Intent(Bookmarks.this, MainActivity.class));
                                        } else
                                            Toast.makeText(Bookmarks.this, "فشل حذف الحساب", Toast.LENGTH_LONG).show();
                                    }
                                });

                    } else
                        Toast.makeText(Bookmarks.this, "كلمة المرور المدخلة خاطئة", Toast.LENGTH_LONG).show();

                }
            });
    }
}