/*
 * Created by Android Team MIS-SEG Year 2021
 * Copyright (c) 2021. Guanzon Central Office
 * Guanzon Bldg., Perez Blvd., Dagupan City, Pangasinan 2400
 * Project name : GhostRider_Android
 * Module : GhostRider_Android.dailyCollectionPlan
 * Electronic Personnel Access Control Security System
 * project file created : 4/24/21 3:19 PM
 * project file last modified : 4/24/21 3:18 PM
 */

package org.rmj.guanzongroup.ghostrider.dailycollectionplan.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.rmj.g3appdriver.GRider.Constants.AppConstants;
import org.rmj.g3appdriver.GRider.Database.Entities.EAddressUpdate;
import org.rmj.g3appdriver.GRider.Database.Entities.EDCPCollectionDetail;
import org.rmj.g3appdriver.GRider.Database.Entities.EImageInfo;
import org.rmj.g3appdriver.GRider.Etc.GToast;
import org.rmj.g3appdriver.GRider.Etc.GeoLocator;
import org.rmj.g3appdriver.GRider.Etc.MessageBox;
import org.rmj.g3appdriver.etc.WebFileServer;
import org.rmj.guanzongroup.ghostrider.dailycollectionplan.Activities.Activity_Transaction;
import org.rmj.guanzongroup.ghostrider.dailycollectionplan.Adapter.AddressInfoAdapter;
import org.rmj.guanzongroup.ghostrider.dailycollectionplan.Adapter.MobileInfoAdapter;
import org.rmj.guanzongroup.ghostrider.dailycollectionplan.Etc.DCP_Constants;
import org.rmj.guanzongroup.ghostrider.dailycollectionplan.Model.AddressUpdate;
import org.rmj.guanzongroup.ghostrider.dailycollectionplan.Model.MobileUpdate;
import org.rmj.guanzongroup.ghostrider.dailycollectionplan.R;
import org.rmj.guanzongroup.ghostrider.dailycollectionplan.ViewModel.VMCustomerNotAround;
import org.rmj.guanzongroup.ghostrider.dailycollectionplan.ViewModel.ViewModelCallback;
import org.rmj.guanzongroup.ghostrider.imgcapture.ImageFileCreator;

import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class Fragment_CustomerNotAround extends Fragment implements ViewModelCallback {
    private VMCustomerNotAround mViewModel;
    private AddressUpdate addressInfoModel;
    private MobileUpdate mobileInfoModel;
    private GeoLocator poLocator;
    private MobileInfoAdapter mobileAdapter;
    private AddressInfoAdapter addressAdapter;
    private EImageInfo poImageInfo;

    private ImageFileCreator poImage;

    private MessageBox poMessage;
    private CheckBox cbPrimeContact, cbPrimary;
    private TextView lblBranch, lblAddress, lblAccNo, lblClientNm, lblClientAddress;
    private RadioGroup rg_CNA_Input, rg_addressType;
    private RadioButton rb_permanent, rb_present;
    private TextInputEditText txtContact, txtHouseNox, txtAddress, txtRemarks;
    private AutoCompleteTextView txtTown, txtBrgy, spnRequestCode;
    private LinearLayout lnContactNox,
            lnAddress;
    private MaterialButton btnAdd, btnSubmit;
    private RecyclerView rvCNAOutputs;
    private String Remarksx;

    private String sRqstCode, sPrimaryx , psPhotox;
    private static final int MOBILE_DIALER = 104;

    private boolean isAddressAdded, isMobileAdded, isMobileToggled = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_not_around_fragment, container, false);
        addressInfoModel = new AddressUpdate();
        poMessage = new MessageBox(getActivity());
        poImageInfo = new EImageInfo();
        poLocator = new GeoLocator(getContext(), getActivity());

        initWidgets(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String TransNox = Activity_Transaction.getInstance().getTransNox();
        int EntryNox = Activity_Transaction.getInstance().getEntryNox();
        try {
            mViewModel = ViewModelProviders.of(this).get(VMCustomerNotAround.class);

            mViewModel.setParameter(TransNox, EntryNox);

            mViewModel.getCollectionDetail().observe(getViewLifecycleOwner(), collectionDetail -> {
                try {
                    lblAccNo.setText(collectionDetail.getAcctNmbr());
                    lblClientNm.setText(collectionDetail.getFullName());
                    lblClientAddress.setText(collectionDetail.getAddressx());

                    mViewModel.setClientID(collectionDetail.getClientID());
                    mViewModel.setCurrentCollectionDetail(collectionDetail);

                    mViewModel.setAccountNo(collectionDetail.getAcctNmbr());
                    poImage = new ImageFileCreator(getActivity(), AppConstants.SUB_FOLDER_DCP, TransNox);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            mViewModel.getUserBranchEmployee().observe(getViewLifecycleOwner(), eBranchInfo -> {
                try {
                    lblBranch.setText(eBranchInfo.getBranchNm());
                    lblAddress.setText(eBranchInfo.getAddressx());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            mViewModel.getMobileRequestListForClient().observe(getViewLifecycleOwner(), mobileNox -> {
                try {
                    mobileAdapter = new MobileInfoAdapter(new MobileInfoAdapter.OnItemInfoClickListener() {
                        @Override
                        public void OnDelete(int position) {
                            //mViewModel.deleteMobile(mobileNox.get(position).getTransNox());
                            mViewModel.deleteMobile(position);
                            GToast.CreateMessage(getActivity(), "Mobile number deleted.", GToast.INFORMATION).show();
                            mobileAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void OnMobileNoClick(String MobileNo) {
                            Intent mobileIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", MobileNo, null));
                            startActivityForResult(mobileIntent, MOBILE_DIALER);
                        }
                    });
                    rvCNAOutputs.setAdapter(mobileAdapter);
                    mobileAdapter.setMobileNox(mobileNox);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Province
            mViewModel.getTownProvinceInfo().observe(getViewLifecycleOwner(), townProvinceInfos -> {
                String[] townProvince = new String[townProvinceInfos.size()];
                for (int x = 0; x < townProvinceInfos.size(); x++) {
                    townProvince[x] = townProvinceInfos.get(x).sTownName + ", " + townProvinceInfos.get(x).sProvName;
                }

                ArrayAdapter<String> loAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, townProvince);
                txtTown.setAdapter(loAdapter);
            });

            txtTown.setOnItemClickListener((adapterView, view, i, l) -> {
                String lsTown = txtTown.getText().toString();
                String[] town = lsTown.split(", ");
                mViewModel.getTownProvinceInfo().observe(getViewLifecycleOwner(), townProvinceInfos -> {
                    for (int x = 0; x < townProvinceInfos.size(); x++) {
                        if (lsTown.equalsIgnoreCase(townProvinceInfos.get(x).sTownName + ", " + townProvinceInfos.get(x).sProvName)) {
                            addressInfoModel.setsProvName(townProvinceInfos.get(x).sProvName);
                            addressInfoModel.setsTownName(townProvinceInfos.get(x).sTownName);
                            addressInfoModel.setTownID(townProvinceInfos.get(x).sTownIDxx);
                            mViewModel.setTownID(townProvinceInfos.get(x).sTownIDxx);
                            break;
                        }
                    }

                    mViewModel.getBarangayNameList().observe(getViewLifecycleOwner(), strings -> {
                        ArrayAdapter<String> loAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, strings);
                        txtBrgy.setAdapter(loAdapter);
                    });
                });
            });

            txtBrgy.setOnItemClickListener((adapterView, view, i, l) -> mViewModel.getBarangayInfoList().observe(getViewLifecycleOwner(), eBarangayInfos -> {
                for (int x = 0; x < eBarangayInfos.size(); x++) {
                    if (txtBrgy.getText().toString().equalsIgnoreCase(eBarangayInfos.get(x).getBrgyName())) {
                        addressInfoModel.setBarangayID(eBarangayInfos.get(x).getBrgyIDxx());
                        addressInfoModel.setsBrgyName(eBarangayInfos.get(x).getBrgyName());
                        mViewModel.setBrgyID(eBarangayInfos.get(x).getBrgyIDxx());
                        break;
                    }
                }
            }));


            mViewModel.getRequestCodeOptions().observe(getViewLifecycleOwner(), stringArrayAdapter -> {
                spnRequestCode.setAdapter(stringArrayAdapter);
                spnRequestCode.setDropDownBackgroundResource(R.drawable.bg_gradient_light);
            });

            btnSubmit.setOnClickListener(v -> {
                poMessage.initDialog();
                poMessage.setTitle("Customer Not Around");
                poMessage.setMessage("Please take a selfie in customer's place in order to confirm transaction.");
                poMessage.setPositiveButton("Okay", (view, dialog) -> {
                    dialog.dismiss();
                    poImage.CreateFile((openCamera, camUsage, photPath, FileName, latitude, longitude) -> {
                        psPhotox = photPath;
                        poImageInfo.setSourceNo(TransNox);
                        poImageInfo.setSourceCD("DCPa");
                        poImageInfo.setImageNme(FileName);
                        poImageInfo.setFileLoct(photPath);
                        poImageInfo.setFileCode("0020");
                        poImageInfo.setLatitude(String.valueOf(latitude));
                        poImageInfo.setLongitud(String.valueOf(longitude));
                        mViewModel.setImagePath(photPath);
                        mViewModel.setImgFileNme(FileName);
                        startActivityForResult(openCamera, ImageFileCreator.GCAMERA);
                    });
                });
                poMessage.setNegativeButton("Cancel", (view, dialog) -> {
                    dialog.dismiss();
                });
                poMessage.show();
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageFileCreator.GCAMERA) {
            if (resultCode == RESULT_OK) {
                if (isMobileToggled) {
                    mViewModel.saveMobileToLocal(Fragment_CustomerNotAround.this);
                } else {
                    mViewModel.saveAddressToLocal(Fragment_CustomerNotAround.this);
                }

                try{
                    poImageInfo.setMD5Hashx(WebFileServer.createMD5Hash(psPhotox));
                    mViewModel.saveImageInfo(poImageInfo);
                    mViewModel.updateCollectionDetail(DCP_Constants.getRemarksCode(Remarksx));
                    Log.e("Fragment_CNA:", "Image Info Save");
                    OnSuccessResult(new String[]{"Customer Not Around Info has been saved."});
                }catch (NullPointerException e){
                    e.printStackTrace();
                }catch (RuntimeException e){
                    e.printStackTrace();
                }
            } else {
                requireActivity().finish();
            }
        }
    }

    private void initWidgets(View v){
        Remarksx = Activity_Transaction.getInstance().getRemarksCode();
        lblBranch = v.findViewById(R.id.lbl_headerBranch);
        lblAddress = v.findViewById(R.id.lbl_headerAddress);
        lblAccNo = v.findViewById(R.id.lbl_dcpAccNo);
        lblClientAddress = v.findViewById(R.id.lbl_dcpClientAddress);
        lblClientNm = v.findViewById(R.id.lbl_dcpClientNm);

        cbPrimeContact = v.findViewById(R.id.cb_primaryContact);
        cbPrimary = v.findViewById(R.id.cb_primary);
        spnRequestCode = v.findViewById(R.id.spn_requestCode);
        rg_CNA_Input = v.findViewById(R.id.rg_CnaInput);
        rg_addressType = v.findViewById(R.id.rg_address_type);
        rb_permanent = v.findViewById(R.id.rb_permanent);
        rb_present = v.findViewById(R.id.rb_present);
        txtContact = v.findViewById(R.id.txt_dcpCNA_contactNox);
        txtHouseNox = v.findViewById(R.id.txt_houseNox);
        txtAddress = v.findViewById(R.id.txt_address);
        txtTown = v.findViewById(R.id.txt_dcpCNA_town);
        txtBrgy = v.findViewById(R.id.txt_dcpCNA_brgy);
        txtRemarks = v.findViewById(R.id.txt_dcpCNA_remarks);
        btnAdd = v.findViewById(R.id.btn_add);
        btnSubmit = v.findViewById(R.id.btn_submit);

        lnContactNox = v.findViewById(R.id.CNA_Contact);
        lnAddress = v.findViewById(R.id.CNA_Address);

        rvCNAOutputs = v.findViewById(R.id.rv_CNA_outputs);
        rvCNAOutputs.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCNAOutputs.setHasFixedSize(true);

        btnAdd.setOnClickListener(view -> addMobile());
        rg_CNA_Input.setOnCheckedChangeListener(new Fragment_CustomerNotAround.OnRadioButtonSelectListener());
        rg_addressType.setOnCheckedChangeListener(new Fragment_CustomerNotAround.OnRadioButtonSelectListener());
        spnRequestCode.setOnItemClickListener((adapterView, view, i, l) -> mViewModel.setRequestCode(String.valueOf(i)));
        cbPrimary.setOnCheckedChangeListener(new OnCheckboxSetListener());
        cbPrimeContact.setOnCheckedChangeListener(new OnCheckboxSetListener());
    }

    private void addAddress() {
        addressInfoModel.setHouseNumber(Objects.requireNonNull(Objects.requireNonNull(txtHouseNox.getText()).toString()));
        addressInfoModel.setAddress(Objects.requireNonNull(Objects.requireNonNull(txtAddress.getText()).toString()));
        addressInfoModel.setLatitude(String.valueOf(poLocator.getLattitude()));
        addressInfoModel.setLongitude(String.valueOf(poLocator.getLongitude()));
        addressInfoModel.setsRemarksx(Objects.requireNonNull(Objects.requireNonNull(txtRemarks.getText()).toString()));

        isAddressAdded = mViewModel.addAddressToList(addressInfoModel, Fragment_CustomerNotAround.this);
        checkIfAddressAdded(isAddressAdded);
    }

    private void addMobile() {
        mViewModel.getRequestCode().observe(getViewLifecycleOwner(), string -> sRqstCode = string);
        mViewModel.getPrimeContact().observe(getViewLifecycleOwner(), string -> sPrimaryx = string);

        String sMobileNo = Objects.requireNonNull(txtContact.getText()).toString();
        String sRemarks = Objects.requireNonNull(txtRemarks.getText()).toString();

        mobileInfoModel = new MobileUpdate(sRqstCode, sMobileNo, sPrimaryx,sRemarks);
        isMobileAdded = mViewModel.AddMobileToList(mobileInfoModel, Fragment_CustomerNotAround.this);
        checkIfMobileAdded(isMobileAdded);
    }

    @Override
    public void OnStartSaving() {

    }

    @Override
    public void OnSuccessResult(String[] args) {
        poMessage.initDialog();
        poMessage.setTitle("Transaction Success");
        poMessage.setMessage(args[0]);
        poMessage.setPositiveButton("Okay", (view, dialog) -> {
            dialog.dismiss();
            getActivity().finish();
        });
        poMessage.show();
    }

    @Override
    public void OnFailedResult(String message) {
        poMessage.initDialog();
        poMessage.setTitle("Transaction Failed");
        poMessage.setMessage(message);
        poMessage.setPositiveButton("Okay", (view, dialog) -> dialog.dismiss());
        poMessage.show();
    }

    private void checkIfAddressAdded(boolean isAddressAdded) {
        if(isAddressAdded) {
            spnRequestCode.setSelection(0);
            rb_permanent.setChecked(false);
            rb_present.setChecked(false);
            txtHouseNox.setText("");
            txtAddress.setText("");
            txtTown.setText("");
            txtBrgy.setText("");
            cbPrimary.setChecked(false);
            txtRemarks.setText("");
            addressAdapter.notifyDataSetChanged();
        }
    }

    private void checkIfMobileAdded(boolean isMobileAdded) {
        if(isMobileAdded) {
            spnRequestCode.setSelection(0);
            cbPrimeContact.setChecked(false);
            txtContact.setText("");
            txtRemarks.setText("");
            mobileAdapter.notifyDataSetChanged();
        }
    }

    private class OnRadioButtonSelectListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(group.getId() == R.id.rg_CnaInput) {
                if(checkedId == R.id.rb_contactNox) {
                    isMobileToggled = true;
                    lnContactNox.setVisibility(View.VISIBLE);
                    lnAddress.setVisibility(View.GONE);
                    spnRequestCode.setSelection(0);
                    rb_permanent.setChecked(false);
                    rb_present.setChecked(false);
                    txtHouseNox.setText("");
                    txtAddress.setText("");
                    txtTown.setText("");
                    txtBrgy.setText("");
                    cbPrimary.setChecked(false);
                    txtRemarks.setText("");
                    btnAdd.setOnClickListener(view -> addMobile());

                    mViewModel.getMobileRequestListForClient().observe(getViewLifecycleOwner(), mobileNox -> {
                        try {
                            mobileAdapter = new MobileInfoAdapter(new MobileInfoAdapter.OnItemInfoClickListener() {
                                @Override
                                public void OnDelete(int position) {
                                    //mViewModel.deleteMobile(mobileNox.get(position).getTransNox());
                                    mViewModel.deleteMobile(position);
                                    GToast.CreateMessage(getActivity(), "Mobile number deleted.", GToast.INFORMATION).show();
                                    mobileAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void OnMobileNoClick(String MobileNo) {
                                    Intent mobileIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", MobileNo, null));
                                    startActivityForResult(mobileIntent, MOBILE_DIALER);
                                }
                            });
                            rvCNAOutputs.setAdapter(mobileAdapter);
                            mobileAdapter.setMobileNox(mobileNox);
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
                else if(checkedId == R.id.rb_address){
                    isMobileToggled = false;
                    lnContactNox.setVisibility(View.GONE);
                    lnAddress.setVisibility(View.VISIBLE);
                    spnRequestCode.setSelection(0);
                    cbPrimeContact.setChecked(false);
                    txtContact.setText("");
                    txtRemarks.setText("");
                    btnAdd.setOnClickListener(view -> addAddress());

                    mViewModel.getAddressRequesListForClient().observe(getViewLifecycleOwner(), eAddressUpdates -> {
                        try {
                            addressAdapter = new AddressInfoAdapter(position -> {
                                //mViewModel.deleteAddress(addressNme.get(position).sTransNox);
                                mViewModel.deleteAddress(position);
                                GToast.CreateMessage(getActivity(), "Address deleted.", GToast.INFORMATION).show();
                                addressAdapter.notifyDataSetChanged();
                            });
                            rvCNAOutputs.setAdapter(addressAdapter);
                            addressAdapter.setAddress(eAddressUpdates);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    });
//                    mViewModel.getAddressNames().observe(getViewLifecycleOwner(), addressNme -> {
//                        try {
//                            addressAdapter = new AddressInfoAdapter(position -> {
//                                mViewModel.deleteAddress(addressNme.get(position).sTransNox);
//                                GToast.CreateMessage(getActivity(), "Address deleted.", GToast.INFORMATION).show();
//                            });
//                            rvCNAOutputs.setAdapter(addressAdapter);
//                            addressAdapter.setAddress(addressNme);
//                        } catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    });
                }
            }
            else if(group.getId() == R.id.rg_address_type) {
                if(checkedId == R.id.rb_permanent) {
                    mViewModel.setAddressType("0");
                }
                else if(checkedId == R.id.rb_present) {
                    mViewModel.setAddressType("1");
                }
            }
        }
    }

    private class OnCheckboxSetListener implements CheckBox.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(buttonView.isChecked()){
                mViewModel.setPrimeAddress("1");
                mViewModel.setPrimeContact("1");
            }
            else{
                mViewModel.setPrimeAddress("0");
                mViewModel.setPrimeContact("0");
            }
        }
    }
}