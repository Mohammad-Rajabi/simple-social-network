package com.zoho.mohammadrajabi.socialnetwork.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.data.model.User;
import com.zoho.mohammadrajabi.socialnetwork.databinding.ItemSearchBinding;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<User> users;

    public SearchAdapter(List<User> users) {
        this.users = users;
    }

    public void setUsers(List<User> users) {
        this.users.clear();
        this.users = users;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_search, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.itemView.setUser(users.get(position));
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
    }
}
