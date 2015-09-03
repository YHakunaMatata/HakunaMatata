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
import com.yahoo.hakunamatata.lib.RoundedTransformation;
import com.yahoo.hakunamatata.models.Comment;

/**
 * Created by jonaswu on 2015/9/3.
 */
public class CommentAdapter extends BaseAdapter<Comment> {

    public CommentAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        View itemView;
        itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.reply_item, parent, false);
        viewHolder = new ReplyHolder(itemView);
        itemView.setAlpha(0);
        itemView.animate().setDuration(1000).alpha(1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final Comment comment = postList.get(position);
        ReplyHolder vh = (ReplyHolder) viewHolder;
        vh.userName.setText(comment.from.name);
        vh.message.setText(comment.message);
        Picasso.with(context)
                .load(comment.from.picture.url)
                .transform(new RoundedTransformation(15, 1))
                .error(R.drawable.images)
                .placeholder(R.drawable.placeholder)
                .centerInside()
                .noFade()
                .fit()
                .into(vh.profileImage);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    public static class ReplyHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView userName;
        TextView message;
        ImageView profileImage;
        View view;

        public ReplyHolder(View view) {
            super(view);
            this.view = view;
            userName = (TextView) view.findViewById(R.id.name);
            message = (TextView) view.findViewById(R.id.message);
            profileImage = (ImageView) view.findViewById(R.id.profile_image);
            image = (ImageView) view.findViewById(R.id.main_image);
        }
    }
}
