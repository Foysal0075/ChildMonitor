package com.finalproject.childmonitor.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.finalproject.childmonitor.AddChildActivity;
import com.finalproject.childmonitor.ObjectClass.Parent;
import com.finalproject.childmonitor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ImageButton addChildButton;
    private TextView addedDeviceView, nameView, emailView;
    private FirebaseUser user;
    private DatabaseReference reference;

    private List<Parent> parentList = new ArrayList<>();
    Parent parent = new Parent();
    int deviceCount = 0;
    ProgressDialog progressDialog;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView = inflater.inflate(R.layout.fragment_home, container, false);
        addChildButton = convertView.findViewById(R.id.add_child_btn);
        addedDeviceView = convertView.findViewById(R.id.added_device_view);
        nameView = convertView.findViewById(R.id.user_name_view);
        emailView = convertView.findViewById(R.id.user_email_view);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Getting Info..");
        progressDialog.show();

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Parents").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                parentList.clear();
                for (DataSnapshot parents : dataSnapshot.getChildren()) {

                    parent = parents.getValue(Parent.class);

                }

                nameView.setText(parent.getName());
                emailView.setText(parent.getEmail());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference.child("Child").orderByChild("childKey").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    deviceCount++;
                }
                addedDeviceView.setText(String.valueOf(deviceCount));
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddChildActivity.class);
                startActivity(intent);
            }
        });


        return convertView;
    }

}
