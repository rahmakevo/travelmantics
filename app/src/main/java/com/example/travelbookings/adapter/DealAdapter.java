package com.example.travelbookings.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbookings.MainActivity;
import com.example.travelbookings.R;
import com.example.travelbookings.common.FirebaseUtil;
import com.example.travelbookings.model.TravelDeal;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealsViewHolder> {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    ArrayList<TravelDeal> deals;
    private ChildEventListener childEventListener;

    public DealAdapter() {
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        deals = FirebaseUtil.mDeals;

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal deal = dataSnapshot.getValue(TravelDeal.class);
                deal.setId(dataSnapshot.getKey());
                deals.add(deal);
                notifyItemInserted(deals.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(childEventListener);
    }

    @NonNull
    @Override
    public DealsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.custom_item, parent, false);
        return new DealsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DealsViewHolder holder, int position) {
        TravelDeal deal = deals.get(position);
        holder.bind(deal);
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public class DealsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, price, desc;
        public DealsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            desc = itemView.findViewById(R.id.textViewDescription);
            price = itemView.findViewById(R.id.textViewPrice);

            itemView.setOnClickListener(this);
        }

        public void bind(TravelDeal deal)   {
            title.setText(deal.getTitle());
            price.setText(deal.getPrice());
            desc.setText(deal.getDescription());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            TravelDeal deal = deals.get(position);
            Intent intent = new Intent(itemView.getContext(), MainActivity.class);
            intent.putExtra("deal", deal);
            itemView.getContext().startActivity(intent);
        }
    }

}
