package com.example.fireinstgram.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fireinstgram.Model.User;
import com.example.fireinstgram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder>{
    private Context mContext;
    private List<User> users ;
    private boolean isFragment;

    private FirebaseUser firebaseUser;

    public UserAdapter(Context mContext, List<User> users, boolean isFragment) {
        this.mContext = mContext;
        this.users = users;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item , parent , false);
        return new UserAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, int position) {
firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

final User user = users.get(position);
holder.follow.setVisibility(View.VISIBLE);
holder.userName.setText(user.getUserName());

holder.name.setText(user.getName());


        Picasso.get().load(user.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);
    isFollowed(user.getId() , holder.follow);
    if(user.getId().equals(firebaseUser.getUid())){
        holder.follow.setVisibility(View.GONE);
    }
    holder.follow.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(holder.follow.getText().equals("follow")){
                FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid()).child("following").child(user.getId()).setValue(true);
            FirebaseDatabase.getInstance().getReference().child("follow").child(user.getId()).child("followers").child(firebaseUser.getUid()).setValue(true);

            }else {
                FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid()).child("following").child(user.getId()).removeValue();
                FirebaseDatabase.getInstance().getReference().child("follow").child(user.getId()).child("followers").child(firebaseUser.getUid()).removeValue();

            }

        }
    });
    }

    private void isFollowed(final String id, final Button follow) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(id).exists()){
                    follow.setText("following");

                }else{
                    follow.setText("follow");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public  class viewHolder extends RecyclerView.ViewHolder{
        public CircleImageView imageProfile;
        public TextView userName,name;
        public Button follow;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            userName = itemView.findViewById(R.id.UserName);
            name = itemView.findViewById(R.id.fullName);
            follow = itemView.findViewById(R.id.btn_follow);

        }
    }
}
