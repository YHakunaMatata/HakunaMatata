package com.yahoo.hakunamatata.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.activities.BaseActivity;
import com.yahoo.hakunamatata.dao.PostDao;
import com.yahoo.hakunamatata.fragments.CommentFragment;
import com.yahoo.hakunamatata.fragments.ReplyFragment;
import com.yahoo.hakunamatata.interfaces.Progressable;
import com.yahoo.hakunamatata.lib.RoundedTransformation;
import com.yahoo.hakunamatata.lib.util;
import com.yahoo.hakunamatata.models.Post;

import java.util.List;

/**
 * Created by jonaswu on 2015/8/30.
 */
public class JokeContentAdapter extends BaseAdapter<Post> {

    private final int POST = 0, PHOTO = 1, VIDEO = 2, LINK = 3;
    private LinearLayout item_action_panel;

    public JokeContentAdapter(Context context) {
        super(context);
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
                break;
            case LINK:
                itemView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.photo_item, parent, false);
                viewHolder = new JokeHolder(itemView);
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

    private View animateItem(View itemView) {
        itemView.setAlpha(0);
        itemView.setScaleX(0.8f);
        itemView.setScaleY(0.8f);
        itemView.animate().setDuration(300).alpha(1);
        itemView.animate().setDuration(200).scaleY(1.0f);
        itemView.animate().setDuration(200).scaleX(1.0f);
        return itemView;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        final Post post = postList.get(position);
        final JokeHolder vh = (JokeHolder) viewHolder;

        vh.time.setText(util.getBestTimeDiff(post.created_time));
        List<com.yahoo.hakunamatata.dao.Post> list = postDao.queryBuilder().where(
                PostDao.Properties.Id.eq(post.id)
        ).build().list();

        ((JokeHolder) viewHolder).item_action_panel.setVisibility(View.GONE);

        if (list.size() > 0) {
            vh.archive.setImageResource(R.drawable.heart_bright);
            vh.archive.setOnClickListener(null);

        } else {
            vh.archive.setImageResource(R.drawable.heart13);
            vh.archive.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    com.yahoo.hakunamatata.dao.Post postToDB = new com.yahoo.hakunamatata.dao.Post();
                    postToDB.setId(post.id);
                    postToDB.setMessage(post.message);
                    postToDB.setCreated_time(post.created_time);
                    postToDB.setType(post.type);
                    postToDB.setPicture(post.picture);
                    postToDB.setLink(post.link);
                    postToDB.setFull_picture(post.full_picture);

                    com.yahoo.hakunamatata.dao.Picture pictureToDB = new com.yahoo.hakunamatata.dao.Picture();
                    pictureToDB.setUrl(post.from.picture.url);
                    pictureDao.insert(pictureToDB);

                    com.yahoo.hakunamatata.dao.User user = new com.yahoo.hakunamatata.dao.User();
                    user.setName(post.from.name);
                    user.setPicture(pictureToDB);
                    user.setId(post.from.id);
                    userDao.insert(user);

                    com.yahoo.hakunamatata.dao.Like like = new com.yahoo.hakunamatata.dao.Like();
                    like.setTotal_count(post.likes.total_count);
                    likeDao.insert(like);

                    postToDB.setUser(user);
                    postToDB.setLike(like);
                    postDao.insert(postToDB);


                    // good to update immediately
                    vh.archive.setImageResource(R.drawable.heart_bright);
                    vh.archive.setOnClickListener(null);

                    util.showToast(context, context.getResources().getString(R.string.add_to_favoraite_success));

                }
            });
        }
        try {
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
                    vh2.message.setText(post.message);
                    Picasso.with(context)
                            .load(post.full_picture)
                            .error(R.drawable.images)
                            .placeholder(R.drawable.placeholder)
                            .into(vh2.image);
                    break;
                case LINK:
                    JokeHolder vh3 = (JokeHolder) viewHolder;
                    vh3.userName.setText(post.from.name);
                    vh3.message.setText(post.message);
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
                            .error(R.drawable.images)
                            .placeholder(R.drawable.placeholder)
                            .into(vh4.image);
                    vh4.image.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showVideoDialog(post.link);
                        }
                    });
                    break;
                default:
                    break;
            }

            // set animation
            ((JokeHolder) viewHolder).view = animateItem(((JokeHolder) viewHolder).view);

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

            ((JokeHolder) viewHolder).view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    // collapse previous
                    if (item_action_panel != null)
                        item_action_panel.setVisibility(View.GONE);

                    item_action_panel = ((JokeHolder) viewHolder).item_action_panel;
                    item_action_panel.setAlpha(0);
                    item_action_panel.setVisibility(View.VISIBLE);
                    item_action_panel.animate().setDuration(500).alpha(1);
                    item_action_panel.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showRepliesDialog(post);
                        }

                    });
                }
            });

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
        replyFragment.show(((BaseActivity) context).getSupportFragmentManager(), "reply dialog");
    }

    private void showRepliesDialog(Post post) {
        CommentFragment commentFragment = CommentFragment.newInstance((Progressable) context, post.id);
        commentFragment.show(((BaseActivity) context).getSupportFragmentManager(), "comment dialog");
    }

}
