package org.rmj.guanzongroup.ghostrider.dailycollectionplan.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.rmj.guanzongroup.ghostrider.dailycollectionplan.Model.CollectionPlan;
import org.rmj.guanzongroup.ghostrider.dailycollectionplan.R;

import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {

    private final List<CollectionPlan> collectionPlans;
    private final OnItemClickListener mListener;

    public CollectionAdapter(List<CollectionPlan> collectionPlans, OnItemClickListener listener){
        this.collectionPlans = collectionPlans;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_list_item, parent, false);
        return new CollectionViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        CollectionPlan collection = collectionPlans.get(position);
        holder.lblAcctNo.setText(collection.getAcctNoxxx());
        holder.lblDCPNox.setText(collection.getDCPNumber());
        holder.lblClient.setText(collection.getClientNme());
        holder.lblAdd1xx.setText(collection.getAddressxx());
        //holder.lblAdd2xx.setText(collection.getAddress2x());
        holder.lblMobile.setText(collection.getContactxx());
        holder.lblBalanc.setText(collection.getBalancexx());
        holder.lblAmount.setText(collection.getAmntDuexx());
        holder.lblStatus.setText(collection.getStatusxxx());
    }

    @Override
    public int getItemCount() {
        return collectionPlans.size();
    }

    public static class CollectionViewHolder extends RecyclerView.ViewHolder{

        public TextView lblAcctNo;
        public TextView lblDCPNox;
        public TextView lblClient;
        public TextView lblAdd1xx;
        public TextView lblAdd2xx;
        public TextView lblMobile;
        public TextView lblBalanc;
        public TextView lblAmount;
        public TextView lblStatus;

        public CollectionViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            lblAcctNo = itemView.findViewById(R.id.lbl_AccountNo);
            lblDCPNox = itemView.findViewById(R.id.lbl_dcpNox);
            lblClient = itemView.findViewById(R.id.lbl_clientNm);
            lblAdd1xx = itemView.findViewById(R.id.lbl_dcpAddress1);
            //lblAdd2xx = itemView.findViewById(R.id.lbl_dcpAddress2);
            lblMobile = itemView.findViewById(R.id.lbl_dcpContact);
            lblBalanc = itemView.findViewById(R.id.lbl_dcpBalance);
            lblAmount = itemView.findViewById(R.id.lbl_dcpAmount);
            lblStatus = itemView.findViewById(R.id.lbl_dcpStatus);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    listener.OnClick();
                }
            });

            lblAdd1xx.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    listener.OnAddressClickListener();
                }
            });

            lblAdd2xx.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    listener.OnAddressClickListener();
                }
            });

            lblMobile.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    listener.OnMobileNoClickListener();
                }
            });
        }
    }

    public interface OnItemClickListener{
        void OnClick();
        void OnMobileNoClickListener();
        void OnAddressClickListener();
        void OnActionButtonClick();
    }
}
