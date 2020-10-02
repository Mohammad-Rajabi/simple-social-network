package com.zoho.mohammadrajabi.socialnetwork.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.zoho.mohammadrajabi.socialnetwork.databinding.DialogEditingContinueBinding;

public class EditingContinueDialog extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        DialogEditingContinueBinding binding = DialogEditingContinueBinding.inflate(LayoutInflater.from(getContext()), null, false);

        binding.tvAcceptEditingContinueDialog.setOnClickListener(v -> {
            dismiss();
        });

        binding.tvDenyEditingContinueDialog.setOnClickListener(v ->
        {
            dismiss();
            getActivity().finish();
        });

        builder.setView(binding.getRoot());
        return builder.create();
    }
}
