package com.example.vtradeversion3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;


public class AccountFragment extends Fragment {
    private Button btnSignOut;
    private FirebaseAuth mAuth;


    public static AccountFragment newInstance() {
        return new AccountFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        btnSignOut = view.findViewById(R.id.sign_out_button);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                signOutUser();
                btnSignOut.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    private void signOutUser (){
        Intent mainActivity = new Intent(AccountFragment.this.getActivity(), MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivity);
     //   finish();
    }

}