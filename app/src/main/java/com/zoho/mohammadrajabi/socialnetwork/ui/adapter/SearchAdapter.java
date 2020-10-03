package com.zoho.mohammadrajabi.socialnetwork.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.data.model.User;
import com.zoho.mohammadrajabi.socialnetwork.databinding.ItemSearchBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<User> users;

    public SearchAdapter(List<User> users) {
        this.users = users;
    }

    public void setUsers(List<User> users) {
        clearUsers();
        this.users = users;
        notifyDataSetChanged();
    }

    public void clearUsers() {
        users.clear();
    }


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(ItemSearchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class SearchViewHolder extends RecyclerView.ViewHolder {

        private ItemSearchBinding itemView;

        public SearchViewHolder(@NonNull ItemSearchBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }

        public void bind(User user) {
            itemView.tvItemSearchUsername.setText(user.getUsername());
            Glide.with(itemView.getRoot().getContext())
                    .load(user.getProfileImage())
                    .placeholder(itemView.getRoot().getContext().getResources().getDrawable(R.drawable.shape_placeholder))
                    .error(itemView.getRoot().getContext().getResources().getDrawable(R.drawable.ic_user))
                    .fallback(itemView.getRoot().getContext().getResources().getDrawable(R.drawable.ic_user))
                    .into(itemView.ivItemSearchProfileImage);
        }
    }
}
