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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Search extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    DatabaseReference database;
    myadapter MyAdapter;
    BottomNavigationView bottomNavigationView;
    ArrayList<Content> list;
    private TextInputLayout ContentSearchInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bottomNavigationView = findViewById(R.id.naviSBar);
        bottomNavigationView.setSelectedItemId(R.id.search_BTN);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        ContentSearchInput = findViewById(R.id.ContenSearchInput);
        recyclerView = findViewById(R.id.contentList);
        database = FirebaseDatabase.getInstance().getReference("Posts");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        MyAdapter = new myadapter(this,list);
        recyclerView.setAdapter(MyAdapter);
    }

    public void SearchC(View view) {
        String search = ContentSearchInput.getEditText().getText().toString();
        clearSearch();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Content content = dataSnapshot.getValue(Content.class);
                    if (search==null) {
                        Collections.reverse(list);
                        list.add(content);
                        Collections.reverse(list);
                    }
                    else if (content.getTitle().toLowerCase().contains(search.toLowerCase())) {
                        Collections.reverse(list);
                        list.add(content);
                        Collections.reverse(list);
                    }
                    else if (content.getName().toLowerCase().contains(search.toLowerCase())) {
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

    private void clearSearch() {
        recyclerView.removeAllViewsInLayout();
        list.clear();
        MyAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        switch (id) {
            case R.id.home_BTN:
                startActivity(new Intent(this,Home.class));
                break;
            case R.id.search_BTN:
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