package com.example.fireinstgram.fragmen;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.fireinstgram.Adapter.PostAdapter;
import com.example.fireinstgram.Model.Post;
import com.example.fireinstgram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
private  RecyclerView recyclerViewPost;
private PostAdapter postAdapter;
private List<Post> postList ;
private List<String> followingList ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_home, container, false);

         recyclerViewPost = view.findViewById(R.id.recycler_view_posts);
         recyclerViewPost.setHasFixedSize(true);


         LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
         linearLayoutManager.setStackFromEnd(true);
         linearLayoutManager.setReverseLayout(true);
         recyclerViewPost.setLayoutManager(linearLayoutManager);


         postList = new ArrayList<>();
         postAdapter = new PostAdapter(getContext() , postList);
         recyclerViewPost.setAdapter(postAdapter);


        followingList = new ArrayList<>();
        
        
        CheckFollowingUsers();

        return view ;
    }

    private void CheckFollowingUsers() {
        FirebaseDatabase.getInstance().getReference().child("follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
followingList.clear();
for (DataSnapshot snapshot : dataSnapshot.getChildren()){
    followingList.add(snapshot.getKey());
}

readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readPosts() {
        FirebaseDatabase.getInstance().getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                        for (String id : followingList){
                            if (post.getPublisher().equals(id)){
                                postList.add(post);
                            }
                        }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
