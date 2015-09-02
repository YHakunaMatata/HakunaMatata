package com.yahoo.hakunamatata.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.yahoo.hakunamatata.R;
import com.yahoo.hakunamatata.lib.RoundedTransformation;

/**
 * Created by jonaswu on 2015/9/2.
 */
public class FavoriteContentAdapter extends BaseAdapter<com.yahoo.hakunamatata.dao.Post> {
    private final int POST = 0, PHOTO = 1, VIDEO = 2, LINK = 3;

    public FavoriteContentAdapter(Context context) {
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
                viewHolder = new BaseAdapter.JokeHolder(itemView);
                break;
            case PHOTO:
                itemView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.photo_item, parent, false);
                viewHolder = new BaseAdapter.JokeHolder(itemView);
                break;
            case VIDEO:
                itemView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.video_item, parent, false);
                viewHolder = new BaseAdapter.JokeHolder(itemView);
                Log.e("orz", "what happenend?");
                break;
            case LINK:
                itemView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.photo_item, parent, false);
                viewHolder = new BaseAdapter.JokeHolder(itemView);
                Log.e("orz", "what happenend?");
                break;
            default:
                itemView = LayoutInflater.
                        from(parent.getContext()).
                        inflate(R.layout.joke_item, parent, false);
                viewHolder = new BaseAdapter.JokeHolder(itemView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        try {
            final com.yahoo.hakunamatata.dao.Post post = postList.get(position);
            switch (viewHolder.getItemViewType()) {
                case POST:
                    BaseAdapter.JokeHolder vh1 = (BaseAdapter.JokeHolder) viewHolder;
                    vh1.userName.setText(post.getUser().getName());
                    vh1.message.setText(post.getMessage());
                    Picasso.with(context)
                            .load(post.getUser().getPicture().getUrl())
                            .transform(new RoundedTransformation(15, 1))
                            .error(R.drawable.images)
                            .placeholder(R.drawable.placeholder)
                            .centerInside()
                            .noFade()
                            .fit()
                            .into(vh1.profileImage);
                    vh1.like.setText(String.valueOf(post.getLike().getTotal_count()));
                    break;
                case PHOTO:
                    BaseAdapter.JokeHolder vh2 = (BaseAdapter.JokeHolder) viewHolder;
                    vh2.userName.setText(post.getUser().getName());
                    Picasso.with(context)
                            .load(post.getUser().getPicture().getUrl())
                            .transform(new RoundedTransformation(15, 1))
                            .error(R.drawable.images)
                            .placeholder(R.drawable.placeholder)
                            .centerInside()
                            .noFade()
                            .fit()
                            .into(vh2.profileImage);
                    vh2.like.setText(String.valueOf(post.getLike().getTotal_count()));
                    vh2.message.setText(post.getMessage());
                    Picasso.with(context)
                            .load(post.getPicture())
                            .transform(new RoundedTransformation(15, 1))
                            .error(R.drawable.images)
                            .placeholder(R.drawable.placeholder)
                            .centerInside()
                            .noFade()
                            .fit()
                            .into(vh2.image);
                    break;
                case LINK:
                    BaseAdapter.JokeHolder vh3 = (BaseAdapter.JokeHolder) viewHolder;
                    vh3.userName.setText(post.getUser().getName());
                    vh3.message.setText(post.getMessage());
                    Picasso.with(context)
                            .load(post.getUser().getPicture().getUrl())
                            .transform(new RoundedTransformation(15, 1))
                            .error(R.drawable.images)
                            .placeholder(R.drawable.placeholder)
                            .centerInside()
                            .noFade()
                            .fit()
                            .into(vh3.profileImage);
                    vh3.like.setText(String.valueOf(post.getLike().getTotal_count()));
                    Picasso.with(context)
                            .load(post.getPicture())
                            .transform(new RoundedTransformation(15, 1))
                            .error(R.drawable.images)
                            .placeholder(R.drawable.placeholder)
                            .centerInside()
                            .noFade()
                            .fit()
                            .into(vh3.image);
                    break;
                case VIDEO:
                    BaseAdapter.JokeHolder vh4 = (BaseAdapter.JokeHolder) viewHolder;
                    vh4.userName.setText(post.getUser().getName());
                    Picasso.with(context)
                            .load(post.getUser().getPicture().getUrl())
                            .transform(new RoundedTransformation(15, 1))
                            .error(R.drawable.images)
                            .placeholder(R.drawable.placeholder)
                            .centerInside()
                            .noFade()
                            .fit()
                            .into(vh4.profileImage);
                    vh4.like.setText(String.valueOf(post.getLike().getTotal_count()));
                    Picasso.with(context)
                            .load(post.getPicture())
                            .transform(new RoundedTransformation(15, 1))
                            .error(R.drawable.images)
                            .placeholder(R.drawable.placeholder)
                            .centerInside()
                            .noFade()
                            .fit()
                            .into(vh4.image);
                    vh4.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showVideoDialog(post.getLink());
                        }
                    });
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        BaseAdapter.JokeHolder vh = (BaseAdapter.JokeHolder) viewHolder;
        vh.item_action_panel.setVisibility(View.GONE);
    }

    @Override
    public int getItemViewType(int position) {
        int returnType;
        com.yahoo.hakunamatata.dao.Post post = postList.get(position);
        String type = post.getType();
        if (type.equals("photo")) {
            returnType = PHOTO;
        } else if (type.equals("status")) {
            returnType = POST;
        } else if (type.equals("video")) {
            returnType = VIDEO;
        } else if (type.equals("link")) {
            returnType = LINK;
        } else {
            returnType = -1;
        }
        return returnType;
    }
}
