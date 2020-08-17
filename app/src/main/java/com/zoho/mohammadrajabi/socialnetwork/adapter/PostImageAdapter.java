package com.zoho.mohammadrajabi.socialnetwork.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.data.model.Post;
import com.zoho.mohammadrajabi.socialnetwork.databinding.ItemPostImageBinding;

import java.util.ArrayList;
import java.util.List;

public class PostImageAdapter extends RecyclerView.Adapter {

    private List<Post> posts = new ArrayList<>();
    private PostImageAdapter.onItemClick onItemClick;

    public PostImageAdapter(PostImageAdapter.onItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new PostImageViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_post_image, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((PostImageViewHolder) holder).bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public class PostImageViewHolder extends RecyclerView.ViewHolder {

        private ItemPostImageBinding itemView;

        public PostImageViewHolder(@NonNull ItemPostImageBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }

        public void bind(Post post) {
            itemView.setPost(post);
            itemView.layoutItemPostImage.setOnClickListener(v -> {
                onItemClick.onPostClick(getAdapterPosition());
            });

        }

    }


    public interface onItemClick {
        void onPostClick(int position);
    }
}
