package com.zoho.mohammadrajabi.socialnetwork.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.data.model.Post;
import com.zoho.mohammadrajabi.socialnetwork.databinding.ItemPostBinding;
import com.zoho.mohammadrajabi.socialnetwork.databinding.ItemProgressBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter {

    private List<Post> posts = new ArrayList<>();
    private PostAdapter.onPostClick onPostClick;
    private final int POST_TYPE = 1;
    private final int PROGRESS_TYPE = 2;

    public PostAdapter(PostAdapter.onPostClick onPostClick) {
        this.onPostClick = onPostClick;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((PostViewHolder) holder).bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        return position == posts.size() ? this.PROGRESS_TYPE : this.POST_TYPE;
//    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        private ItemPostBinding itemView;

        public PostViewHolder(@NonNull ItemPostBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }

        public void bind(Post post) {

            itemView.setPost(post);

            if (post.isLiked()) {
                itemView.imageViewPostItemLike.setColorFilter(itemView.getRoot().getContext().getResources().getColor(R.color.red));
            } else {
                itemView.imageViewPostItemLike.setColorFilter(itemView.getRoot().getContext().getResources().getColor(R.color.gray700));
            }

            if (post.getLikeCount() != 0) {
//                itemView.imageViewPostItemPostLikeCount.setVisibility(View.VISIBLE);
                itemView.textViewPostImagePostLikeCount.setText(String.valueOf(post.getLikeCount()));
            }

            itemView.imageViewPostItemLike.setOnClickListener(v -> {
                onPostClick.onLikeClick(getAdapterPosition(), post.getPostId());
            });

            itemView.imageViewPostItemComment.setOnClickListener(v -> {
                onPostClick.onCommentClick(post.getPostId());
            });
        }

    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(@NonNull ItemProgressBinding itemView) {
            super(itemView.getRoot());
        }
    }

    public interface onPostClick {
        void onLikeClick(int position, String postId);

        void onCommentClick(String postId);
    }

    public void likedPost(int position, int likeCount) {

        if (posts.get(position).isLiked()) {
            posts.get(position).setLiked(false);
        } else {
            posts.get(position).setLiked(true);
        }
        posts.get(position).setLikeCount(likeCount);
        posts.set(position, posts.get(position));
        notifyItemChanged(position);
    }
}
