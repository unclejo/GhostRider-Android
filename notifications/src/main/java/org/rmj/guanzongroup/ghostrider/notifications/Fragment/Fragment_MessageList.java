/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.notifications
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:18 PM
 */

package org.rmj.guanzongroup.ghostrider.notifications.Fragment;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.guanzongroup.ghostrider.notifications.Activity.Activity_Notifications;
import org.rmj.guanzongroup.ghostrider.notifications.Adapter.MessageListAdapter;
import org.rmj.guanzongroup.ghostrider.notifications.Object.EmployeeSearchItem;
import org.rmj.guanzongroup.ghostrider.notifications.Object.MessageItemList;
import org.rmj.guanzongroup.ghostrider.notifications.R;
import org.rmj.guanzongroup.ghostrider.notifications.ViewModel.VMMessageList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Fragment_MessageList extends Fragment {

    private VMMessageList mViewModel;
    private RecyclerView recyclerViewMsg;
    private RecyclerView recyclerViewEmp;
    private MaterialButton btnSearch;
    private TextInputEditText txtEmployee;

    public static Fragment_MessageList newInstance() {
        return new Fragment_MessageList();
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);
        recyclerViewMsg = view.findViewById(R.id.recyclerview_messages);
        recyclerViewEmp = view.findViewById(R.id.recyclerview_employee);
        btnSearch = view.findViewById(R.id.btnSearch);
        txtEmployee = view.findViewById(R.id.txt_employeeSearch);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VMMessageList.class);
        mViewModel.getUserMessagesList().observe(getViewLifecycleOwner(), userNotificationInfos -> {
            if(userNotificationInfos.size() > 0) {
                recyclerViewMsg.setVisibility(View.VISIBLE);
                List<MessageItemList> messageItemLists = new ArrayList<>();
                messageItemLists.clear();
                for (int x = 0; x < userNotificationInfos.size(); x++) {
                    MessageItemList message = new MessageItemList(userNotificationInfos.get(x).CreatrNm,
                            userNotificationInfos.get(x).CreatrID,
                            userNotificationInfos.get(x).Messagex,
                            userNotificationInfos.get(x).Received,
                            userNotificationInfos.get(x).MsgTitle);
                    messageItemLists.add(message);
                }

                LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                manager.setOrientation(RecyclerView.VERTICAL);
                recyclerViewMsg.setLayoutManager(manager);
                recyclerViewMsg.setAdapter(new MessageListAdapter(messageItemLists, (Title, Message, SenderID) -> {
                    Intent loIntent = new Intent(getActivity(), Activity_Notifications.class);
                    loIntent.putExtra("title", Title);
                    loIntent.putExtra("message", Message);
                    loIntent.putExtra("type", "message");
                    loIntent.putExtra("sender", SenderID);
                    startActivity(loIntent);
                }));
            } else {
                recyclerViewMsg.setVisibility(View.GONE);
            }
        });

        btnSearch.setOnClickListener(v -> {
            String lsEmpName = Objects.requireNonNull(txtEmployee.getText()).toString();
            mViewModel.SearchEmployee(lsEmpName, new VMMessageList.OnEmloyeeSearchListener() {
                @Override
                public void OnSearch(List<EmployeeSearchItem> employeeSearchItems) {

                }

                @Override
                public void OnError(String message) {

                }
            });
        });
    }
}