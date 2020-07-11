package com.finalproject.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.finalproject.Const;
import com.finalproject.LoginActivity;
import com.finalproject.R;

public class LogoutFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_logout, container, false);
        SharedPreferences preferences = requireActivity().getSharedPreferences(Const.SHAREDPREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
        FirebaseAuth.getInstance().signOut();
        getActivity().finish();
        return root;
    }
}
