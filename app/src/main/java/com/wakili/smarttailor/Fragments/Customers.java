package com.wakili.smarttailor.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wakili.smarttailor.R;
import com.wakili.smarttailor.ui.send.SendViewModel;

public class Customers extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customers, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fabcustomers);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCustomerFragment addCustomerFragment  = new addCustomerFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        //setCustomAnimations(R.anim.enter, R.anim.exit,R.anim.pop_enter,R.anim.pop_exit)
                        .replace(R.id.nav_host_fragment,addCustomerFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });




        return view;
    }
}