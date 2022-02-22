package com.example.fireinstgram.fragmen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.fireinstgram.Adapter.TagAdapter;
import com.example.fireinstgram.Adapter.UserAdapter;
import com.example.fireinstgram.Model.User;
import com.example.fireinstgram.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SocialAutoCompleteTextView searchBar;

    private List <User> mUsers ;
    private UserAdapter userAdapter;
    private  RecyclerView recyclerViewTag;

    private List<String> mHashTag ;
    private List<String> mHashTagCount ;
    private TagAdapter tagAdapter ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewTag = view.findViewById(R.id.recyclerViewTags);
        recyclerViewTag.setHasFixedSize(true);
        recyclerViewTag.setLayoutManager(new LinearLayoutManager(getContext()));

        mHashTag = new ArrayList<>();
        mHashTagCount = new ArrayList<>();
        tagAdapter = new TagAdapter(getContext(), mHashTag ,mHashTagCount);
        recyclerViewTag.setAdapter(tagAdapter);

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext() ,mUsers,true );
        recyclerView.setAdapter(userAdapter);

        searchBar = view.findViewById(R.id.searchbar);
        readUsers();
        readTags();
        searchBar.addTextChangedListener(new TextWatcher() {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser(s.toString());

    }

    @Override
    public void afterTextChanged(Editable s) {

        filter(s.toString());

    }
});
        return view;
    }

    private void readTags() {
        FirebaseDatabase.getInstance().getReference().child("HashTags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

           mHashTag.clear();
           mHashTagCount.clear();
           for(DataSnapshot snapshot : dataSnapshot.getChildren()){
               mHashTag.add(snapshot.getKey());
               mHashTagCount.add(snapshot.getChildrenCount() +"");

           }
           tagAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

}

    private void readUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(TextUtils.isEmpty(searchBar.getText().toString())){
                    mUsers.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        User user = snapshot.getValue(User.class);
                        mUsers.add(user);

                    }
                userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
}
private void searchUser(final String s){

   Query query= FirebaseDatabase.getInstance().getReference().child("users")
           .orderByChild("UserName").startAt(s).endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            mUsers.clear();
       for(DataSnapshot snapshot : dataSnapshot.getChildren()){
           User user = snapshot.getValue(User.class);
           mUsers.add(user);

       }
       userAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}

private void filter(String text){
        List<String> mSearchTags  = new ArrayList<>();

    List<String> mSearchTagsCount  = new ArrayList<>();

        for (String s : mHashTag){
            if(s.toLowerCase().contains(text.toLowerCase())){
                mSearchTags.add(s);
                mSearchTagsCount.add(mHashTagCount.get(mHashTag.indexOf(s)));

            }
        }
tagAdapter.filter(mSearchTags , mSearchTagsCount);

}
}
