package com.example.tadarasu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    DatabaseReference database;
    BottomNavigationView bottomNavigationView;
    myadapter MyAdapter;
    FirebaseAuth mAuth;
    ArrayList<Content> list;
    FirebaseUser user;
    private String Category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.contentList);
        database = FirebaseDatabase.getInstance().getReference("Posts");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        MyAdapter = new myadapter(this,list);
        recyclerView.setAdapter(MyAdapter);

        bottomNavigationView = findViewById(R.id.naviHBar);
        bottomNavigationView.setSelectedItemId(R.id.home_BTN);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        listA();

    }

    protected void onRestart() {
        super.onRestart();
        clearData();
        listA();
    }


    public void clearData() {
        recyclerView.removeAllViewsInLayout();
        list.clear();
        MyAdapter.notifyDataSetChanged();
    }

    public void selectCategory(View view) {
        switch(view.getId()){
            case R.id.radioButtonAll:
                Category = null;
                clearData();
                listA();

                break;
            case R.id.radioButtonF:
                Category = "فقه";
                clearData();
                listA();
                break;
            case R.id.radioButton3:
                Category = "عقيدة";
                clearData();
                listA();
                break;
            case R.id.radioButtonT:
                Category = "تفسير";
                clearData();
                listA();
                break;
        }

    }

    private void listA() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Content content = dataSnapshot.getValue(Content.class);
                    if (Category==null) {
                        Collections.reverse(list);
                        list.add(content);
                        Collections.reverse(list);
                    }

                    else if (Category.compareTo(content.getCategory())==0) {
                            Collections.reverse(list);
                            list.add(content);
                            Collections.reverse(list);
                    }

                }
                MyAdapter.notifyDataSetChanged();


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
                break;
            case R.id.search_BTN:
                startActivity(new Intent(this,Search.class));
                break;
            case R.id.addC_BTN:
                startActivity(new Intent(this,AddContent.class));
                break;
            case R.id.bookmark_BTN:
                startActivity(new Intent(this,Bookmarks.class));
                break;
        }
        return false;
    }



}