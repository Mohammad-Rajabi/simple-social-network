package com.zoho.mohammadrajabi.socialnetwork.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

        return new PostImageViewHolder(ItemPostImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

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

            Glide.with(itemView.getRoot().getContext())
                    .load(post.getPostImage())
                    .placeholder(itemView.getRoot().getContext().getResources().getDrawable(R.drawable.ic_baseline_image_24))
                    .error(itemView.getRoot().getContext().getResources().getDrawable(R.drawable.spring_in_shiraz))
                    .fallback(itemView.getRoot().getContext().getResources().getDrawable(R.drawable.spring_in_shiraz))
                    .into(itemView.ivItemPostImage);

            itemView.layoutItemPostImage.setOnClickListener(v -> {
                onItemClick.onPostClick(getAdapterPosition());
            });

        }

    }


    public interface onItemClick {
        void onPostClick(int position);
    }
}
