package com.zoho.mohammadrajabi.socialnetwork.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.zoho.mohammadrajabi.socialnetwork.R;
import com.zoho.mohammadrajabi.socialnetwork.databinding.DialogExitBinding;

public class ExitDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        DialogExitBinding binding = DialogExitBinding.inflate(LayoutInflater.from(getContext()), null, false);

        binding.tvAcceptExitDialog.setOnClickListener(v -> {
            dismiss();
            getActivity().finish();
        });

        binding.tvDenyExitDialog.setOnClickListener(v ->
        {
            dismiss();
        });

        builder.setView(binding.getRoot());
        return builder.create();
    }
}
