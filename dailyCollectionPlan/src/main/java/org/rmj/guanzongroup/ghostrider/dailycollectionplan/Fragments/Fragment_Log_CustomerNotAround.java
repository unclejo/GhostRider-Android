package org.rmj.guanzongroup.ghostrider.dailycollectionplan.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.rmj.g3appdriver.GRider.Etc.GToast;
import org.rmj.guanzongroup.ghostrider.dailycollectionplan.Activities.Activity_LogTransaction;
import org.rmj.guanzongroup.ghostrider.dailycollectionplan.Adapter.AddressInfoAdapter_Log;
import org.rmj.guanzongroup.ghostrider.dailycollectionplan.Adapter.MobileInfoAdapter_Log;
import org.rmj.guanzongroup.ghostrider.dailycollectionplan.R;
import org.rmj.guanzongroup.ghostrider.dailycollectionplan.ViewModel.VMLogCustomerNotAround;

public class Fragment_Log_CustomerNotAround extends Fragment {
    private static final String TAG = Fragment_Log_CustomerNotAround.class.getSimpleName();
    private static final int MOBILE_DIALER = 104;
    private VMLogCustomerNotAround mViewModel;
    private MobileInfoAdapter_Log mobileAdapter;
    private AddressInfoAdapter_Log addressAdapter;
    private TextView txtAcctNo, txtClientName, txtClientAddress;
    private RecyclerView rvMobileNox, rvAddress;
    private ImageView ivTransImage;
    private LinearLayout lnMobilenox, lnAddressx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_not_around_log, container, false);
        mViewModel = ViewModelProviders.of(this).get(VMLogCustomerNotAround.class);
        initWidgets(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        txtAcctNo.setText(Activity_LogTransaction.acctNox);
        txtClientName.setText(Activity_LogTransaction.fullNme);
        txtClientAddress.setText(Activity_LogTransaction.clientAddress);
        mViewModel.setClientID(Activity_LogTransaction.clientID);
        //Image Location
        mViewModel.getImageLocation(Activity_LogTransaction.acctNox, Activity_LogTransaction.imgNme)
                .observe(getViewLifecycleOwner(), eImageInfo -> {
            // TODO: Display Image
                    setPic(eImageInfo.getFileLoct());
        });

        mViewModel.getCNA_MobileDataList().observe(getViewLifecycleOwner(), cna_mobileInfos -> {
            if (!cna_mobileInfos.isEmpty()) {
                try {
                    lnMobilenox.setVisibility(View.VISIBLE);
                    mobileAdapter = new MobileInfoAdapter_Log(new MobileInfoAdapter_Log.OnItemInfoClickListener() {
                        @Override
                        public void OnDelete(int position) {
                            GToast.CreateMessage(getActivity(), "Mobile number deleted.", GToast.INFORMATION).show();
                        }

                        @Override
                        public void OnMobileNoClick(String MobileNo) {
                            Intent mobileIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", MobileNo, null));
                            startActivityForResult(mobileIntent, MOBILE_DIALER);
                        }
                    });
                    rvMobileNox.setAdapter(mobileAdapter);
                    mobileAdapter.setMobileNox(cna_mobileInfos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mViewModel.getCNA_AddressDataList().observe(getViewLifecycleOwner(), cna_addressInfos -> {
            if (!cna_addressInfos.isEmpty()) {
                try {
                    lnAddressx.setVisibility(View.VISIBLE);
                    addressAdapter = new AddressInfoAdapter_Log();
                    rvAddress.setAdapter(addressAdapter);
                    addressAdapter.setAddress(cna_addressInfos);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initWidgets(View v) {
        ivTransImage = v.findViewById(R.id.iv_transaction_img);
        txtAcctNo = v.findViewById(R.id.txt_acctNo);
        txtClientName = v.findViewById(R.id.txt_clientName);
        txtClientAddress = v.findViewById(R.id.txt_client_address);
        lnMobilenox = v.findViewById(R.id.ln_mobileNox);
        lnAddressx = v.findViewById(R.id.ln_addressx);
        rvMobileNox = v.findViewById(R.id.rv_mobileNox);
        rvMobileNox.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMobileNox.setHasFixedSize(true);
        rvAddress = v.findViewById(R.id.rv_addressx);
        rvAddress.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvAddress.setHasFixedSize(true);
    }

    private void setPic(String photoPath) {
        // Get the dimensions of the View
        int targetW = ivTransImage.getWidth();
        int targetH = ivTransImage.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(photoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);

        Bitmap bOutput;
        float degrees = 90;//rotation degree
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees);
        bOutput = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        ivTransImage.setImageBitmap(bOutput);
    }

}