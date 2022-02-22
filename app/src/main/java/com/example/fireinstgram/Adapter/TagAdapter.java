package com.example.fireinstgram.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fireinstgram.R;

import java.util.List;

public class TagAdapter extends  RecyclerView.Adapter<TagAdapter.viewHolder>{
    private Context mContext;
    private List<String> mTag;
    private List<String> mTagCount;
    public TagAdapter(Context mContext, List<String> mTag, List<String> mTagCount) {
        this.mContext = mContext;
        this.mTag = mTag;
        this.mTagCount = mTagCount;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view =LayoutInflater.from(mContext).inflate(R.layout.tag_item , parent , false);
        return new TagAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.tag.setText("# " + mTag.get(position));
        holder.noOfPosts.setText(mTagCount.get(position) + " posts");
    }

    @Override
    public int getItemCount() {
        return mTag.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView tag , noOfPosts;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
        tag = itemView.findViewById(R.id.hashTag);
        noOfPosts = itemView.findViewById(R.id.no_of_posts);

        }
    }
public void filter (List<String> filterTag , List<String> filterTagsCount ){
        this.mTag=filterTag;
        this.mTagCount = filterTagsCount ;

        notifyDataSetChanged();

}
}
