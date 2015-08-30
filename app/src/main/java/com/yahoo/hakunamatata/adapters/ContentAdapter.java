package com.yahoo.hakunamatata.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.element.Post;
import com.yahoo.hakunamatata.lib.RoundedTransformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonaswu on 2015/8/30.
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.JokeHolder> {


    private List<Post> postList = new ArrayList<>();
    private Context context;


    public ContentAdapter(Context context) {
        this.context = context;
    }


    @Override
    public JokeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.joke_item, parent, false);
        return new JokeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JokeHolder holder, int position) {
        Post post = postList.get(position);
        holder.userName.setText(post.from.name);
        holder.message.setText(post.message);
        Picasso.with(context)
                .load(post.from.picture.url)
                .transform(new RoundedTransformation(15, 1))
                .error(R.drawable.images)
                .placeholder(R.drawable.placeholder)
                .centerInside()
                .noFade()
                .fit()
                .into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public static class JokeHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView message;
        ImageView profileImage;

        public JokeHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.name);
            message = (TextView) view.findViewById(R.id.message);
            profileImage = (ImageView) view.findViewById(R.id.profile_image);
        }
    }
}
