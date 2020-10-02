package com.zoho.mohammadrajabi.socialnetwork.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.data.model.Comment;
import com.zoho.mohammadrajabi.socialnetwork.databinding.ItemCommentBinding;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private List<Comment> comments;

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentHolder(ItemCommentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.bind(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {

        ItemCommentBinding itemView;

        public CommentHolder(@NonNull ItemCommentBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }

        public void bind(Comment comment) {
            itemView.textViewCommentItemUsername.setText(comment.getCommentOwnerUsername());
            itemView.textViewCommentItemContent.setText(comment.getCommentContent());
            Glide.with(itemView.getRoot().getContext())
                    .load(comment.getCommentOwnerProfileImage())
                    .placeholder(itemView.getRoot().getContext().getResources().getDrawable(R.drawable.shape_placeholder))
                    .error(itemView.getRoot().getContext().getResources().getDrawable(R.drawable.ic_user))
                    .fallback(itemView.getRoot().getContext().getResources().getDrawable(R.drawable.ic_user))
                    .into(itemView.imageViewCommentItemProfileImage);

        }

    }
}
