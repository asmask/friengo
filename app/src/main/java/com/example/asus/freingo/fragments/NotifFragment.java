package com.example.asus.freingo.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.freingo.Adapter.RecyclerViewAdapter;
import com.example.asus.freingo.Adapter.SwipeToDeleteCallback;
import com.example.asus.freingo.R;
import com.example.asus.freingo.service.MyFirebaseMessaging;

import com.example.asus.freingo.models.Notification;
import com.example.asus.freingo.remote.Common;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


public class NotifFragment extends Fragment {

    private View view;
    private TextView noNotif;
    RecyclerView recyclerView;
    RecyclerViewAdapter mAdapter;

    ArrayList<Notification> notifications = MyFirebaseMessaging.notifications;
    CoordinatorLayout coordinatorLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_notif, container, false);


        recyclerView = view.findViewById(R.id.recyclerView);
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        noNotif= view.findViewById(R.id.noNotif) ;
        //FireBase
       Common.curentToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("FirebaseToken",Common.curentToken.toString());
        populateRecyclerView();
        enableSwipeToDeleteAndUndo();

        return view;
    }


    private void populateRecyclerView() {
        if(notifications.isEmpty()){
            view.findViewById(R.id.empty).setVisibility(View.VISIBLE);
            view.findViewById(R.id.listView).setVisibility(View.GONE);
        }
        mAdapter = new RecyclerViewAdapter(notifications);
        recyclerView.setAdapter(mAdapter);


    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Notification item = mAdapter.getData().get(position);

                mAdapter.removeItem(position);


                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mAdapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


}
