package com.wakili.smarttailor.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wakili.smarttailor.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class GalleryFragment extends Fragment {

    private ImageView styleImg;
    private CircleImageView addImg;
    private MaterialEditText edtTitle, edtdetail;
    AlertDialog ourdialog;
    private EditText seachEdt;

    private DatabaseReference databaseRef, mDatabaseRef;
    private ArrayAdapter<CharSequence> spinner_adapter;



    private RecyclerView recycler_siwes;
    private FirebaseDatabase mFirebaseInstance;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fabstyles);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddStyleFragment addStyleFragment  = new AddStyleFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        //setCustomAnimations(R.anim.enter, R.anim.exit,R.anim.pop_enter,R.anim.pop_exit)
                        .replace(R.id.nav_host_fragment,addStyleFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });




        return view;

    }


    private void search(String s) {
        Query query = databaseRef.orderByChild("name")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    meetUpDataItems.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        final MeetUpDataItem meetUpDataItem = dataSnapshot1.getValue(MeetUpDataItem.class);
                        meetUpDataItems.add(meetUpDataItem);
                    }
                    SearchAdapter searchAdapter = new SearchAdapter(getApplicationContext(),meetUpDataItems);
                    recycler_visitor.setAdapter(searchAdapter);
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void initView() {
        mFirebaseInstance = FirebaseDatabase.getInstance();
        databaseRef = mFirebaseInstance.getReference("Visitors");
        recycler_visitor = findViewById(R.id.recycler_visitor);
        recycler_visitor.setLayoutManager(new LinearLayoutManager(this));
        recycler_visitor.setHasFixedSize(true);
        FirebaseRecyclerOptions<VisitorDataItem> options = new FirebaseRecyclerOptions.Builder<VisitorDataItem>().setQuery(databaseRef, VisitorDataItem.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<VisitorDataItem, VisitorViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull VisitorViewHolder viewHolder, int position, @NonNull VisitorDataItem model) {
                viewHolder.txt_name.setText(model.getName());
                viewHolder.txt_date.setText(model.getDate());
                viewHolder.txt_email.setText(model.getEmail());
                viewHolder.txt_phone.setText(model.getPhone());
                viewHolder.setItemClickListener((view, position1, isLongClick) -> Toast.makeText(VisitorActivity.this, model.getName()+ "is Selected", Toast.LENGTH_SHORT).show());
            }

            @NonNull
            @Override
            public VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.visitor_card, viewGroup, false);
                return new VisitorViewHolder(view);
            }
        };
        adapter.startListening();
        recycler_visitor.setAdapter(adapter);
    }

}