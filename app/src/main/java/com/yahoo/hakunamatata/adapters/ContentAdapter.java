package com.yahoo.hakunamatata.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.activities.BaseActivity;
import com.yahoo.hakunamatata.fragments.ReplyFragment;
import com.yahoo.hakunamatata.fragments.VideoFragment;
import com.yahoo.hakunamatata.lib.RoundedTransformation;
import com.yahoo.hakunamatata.models.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonaswu on 2015/8/30.
 */
public class ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int POST = 0, PHOTO = 1, VIDEO = 2, LINK = 3;
    private List<Post> postList = new ArrayList<>();
    private Context context;


    public ContentAdapter(Context context) {
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        View itemView;
        switch (viewType) {
            case POST:
                itemView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.joke_item, parent, false);
                viewHolder = new JokeHolder(itemView);
                break;
            case PHOTO:
                itemView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.photo_item, parent, false);
                viewHolder = new JokeHolder(itemView);
                break;
            case VIDEO:
                itemView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.video_item, parent, false);
                viewHolder = new JokeHolder(itemView);
                Log.e("orz", "what happenend?");
                break;
            case LINK:
                itemView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.photo_item, parent, false);
                viewHolder = new JokeHolder(itemView);
                Log.e("orz", "what happenend?");
                break;
            default:
                itemView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.joke_item, parent, false);
                viewHolder = new JokeHolder(itemView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        try {
            final Post post = postList.get(position);
            switch (viewHolder.getItemViewType()) {
                case POST:
                    JokeHolder vh1 = (JokeHolder) viewHolder;
                    vh1.userName.setText(post.from.name);
                    vh1.message.setText(post.message);
                    Picasso.with(context)
                            .load(post.from.picture.url)
                            .transform(new RoundedTransformation(15, 1))
                            .error(R.drawable.images)
                            .placeholder(R.drawable.placeholder)
                            .centerInside()
                            .noFade()
                            .fit()
                            .into(vh1.profileImage);
                    vh1.like.setText(String.valueOf(post.likes.total_count));
                    break;
                case PHOTO:
                    JokeHolder vh2 = (JokeHolder) viewHolder;
                    vh2.userName.setText(post.from.name);
                    Picasso.with(context)
                            .load(post.from.picture.url)
                            .transform(new RoundedTransformation(15, 1))
                            .error(R.drawable.images)
                            .placeholder(R.drawable.placeholder)
                            .centerInside()
                            .noFade()
                            .fit()
                            .into(vh2.profileImage);
                    vh2.like.setText(String.valueOf(post.likes.total_count));
                    Picasso.with(context)
                            .load(post.full_picture)
                            .error(R.drawable.images)
                            .placeholder(R.drawable.placeholder)
                            .into(vh2.image);
                    break;
                case LINK:
                    JokeHolder vh3 = (JokeHolder) viewHolder;
                    vh3.userName.setText(post.from.name);
                    Picasso.with(context)
                            .load(post.from.picture.url)
                            .transform(new RoundedTransformation(15, 1))
                            .error(R.drawable.images)
                            .placeholder(R.drawable.placeholder)
                            .centerInside()
                            .noFade()
                            .fit()
                            .into(vh3.profileImage);
                    vh3.like.setText(String.valueOf(post.likes.total_count));
                    Picasso.with(context)
                            .load(post.full_picture)
                            .error(R.drawable.images)
                            .placeholder(R.drawable.placeholder)
                            .into(vh3.image);
                    break;
                case VIDEO:
                    JokeHolder vh4 = (JokeHolder) viewHolder;
                    vh4.userName.setText(post.from.name);
                    Picasso.with(context)
                            .load(post.from.picture.url)
                            .transform(new RoundedTransformation(15, 1))
                            .error(R.drawable.images)
                            .placeholder(R.drawable.placeholder)
                            .centerInside()
                            .noFade()
                            .fit()
                            .into(vh4.profileImage);
                    vh4.like.setText(String.valueOf(post.likes.total_count));
                    Picasso.with(context)
                            .load(post.full_picture)
                            .transform(new RoundedTransformation(15, 1))
                            .error(R.drawable.images)
                            .placeholder(R.drawable.placeholder)
                            .into(vh4.image);
                    vh4.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showVideoDialog(post.link);
                        }
                    });
                    break;
                default:
                    break;
            }

            // bind replay button
            ImageView replyBtn = ((JokeHolder) viewHolder).replyBtn;
            if (replyBtn != null) {
                replyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showReplyDialog(post);
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        int returnType;
        Post post = postList.get(position);
        if (post.type.equals("photo")) {
            returnType = PHOTO;
        } else if (post.type.equals("status")) {
            returnType = POST;
        } else if (post.type.equals("video")) {
            returnType = VIDEO;
        } else if (post.type.equals("link")) {
            returnType = LINK;
        } else {
            returnType = -1;
        }
        return returnType;
    }

    private void showReplyDialog(Post post) {
        ReplyFragment replyFragment = ReplyFragment.newInstance(post);
        replyFragment.show(((BaseActivity)context).getSupportFragmentManager(), "reply dialog");
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void addWithPostList(List<Post> postList) {
        for (Post post : postList) {
            this.postList.add(post);
        }
    }

    public void clearAll() {
        postList.clear();
        this.notifyDataSetChanged();
    }

    public static class JokeHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView like;
        TextView userName;
        TextView message;
        ImageView profileImage;
        ImageView replyBtn;

        public JokeHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.name);
            message = (TextView) view.findViewById(R.id.message);
            profileImage = (ImageView) view.findViewById(R.id.profile_image);
            like = (TextView) view.findViewById(R.id.like);
            image = (ImageView) view.findViewById(R.id.main_image);
            replyBtn = (ImageView) view.findViewById(R.id.reply);
        }
    }

    private void showVideoDialog(String url) {
        FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        VideoFragment videoFragment = VideoFragment.newInstance(url, true);
        videoFragment.show(fm, "fragment_edit_name");
    }
}
