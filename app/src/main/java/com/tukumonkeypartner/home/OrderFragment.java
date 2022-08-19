package com.tukumonkeypartner.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import com.tukumonkeypartner.R;
import com.tukumonkeypartner.accept_order.AcceptContract;
import com.tukumonkeypartner.accept_order.AcceptIntract;
import com.tukumonkeypartner.accept_order.AcceptPresenter;
import com.tukumonkeypartner.home.dashdata_mvp.DashboardContract;
import com.tukumonkeypartner.home.dashdata_mvp.DashboardIntract;
import com.tukumonkeypartner.home.dashdata_mvp.DashboardPresenter;
import com.tukumonkeypartner.home.model.DashboardResponse;
import com.tukumonkeypartner.mark_as_reject.RejectContract;
import com.tukumonkeypartner.mark_as_reject.RejectIntract;
import com.tukumonkeypartner.mark_as_reject.RejectPresenter;
import com.tukumonkeypartner.notification.NotificationActivity;
import com.tukumonkeypartner.profile.ProfileActivity;
import com.tukumonkeypartner.search.SearchContract;
import com.tukumonkeypartner.search.SearchIntract;
import com.tukumonkeypartner.search.SearchPresenter;
import com.tukumonkeypartner.search.model.SearchResultResponse;
import com.tukumonkeypartner.utils.BaseFragment;
import com.tukumonkeypartner.utils.GeneralResponse;
import com.tukumonkeypartner.utils.MnxConstant;
import com.tukumonkeypartner.utils.MnxPreferenceManager;


public class OrderFragment  extends BaseFragment implements DashboardContract, AcceptContract,
        RejectContract, SearchContract {

    RecyclerView recyclerView;
    String TAG=OrderFragment.class.getSimpleName();
    DashboardPresenter dashboardPresenter;
    OrdersAdapter ordersAdapter;
    ConstraintLayout no_data_constraint;
    AcceptPresenter acceptPresenter;
    RejectPresenter rejectPresenter;
    SearchPresenter searchPresenter;
    EditText search_et;
    String search_keyword;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_new_orders, container, false);

        dash_apicall();
        recyclerView=view.findViewById(R.id.recyclerview);
        search_et=view.findViewById(R.id.serach_et);
        no_data_constraint=view.findViewById(R.id.no_data_constraint);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        search_et.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    search_keyword=search_et.getText().toString().trim();
                    do_search_apicall();
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    private void do_search_apicall() {
        if(MnxPreferenceManager.getString(MnxConstant.TOKEN,null)!=null) {
            showLoading();
            searchPresenter = new SearchPresenter(OrderFragment.this, new SearchIntract());
            searchPresenter.validateDetails(search_keyword);
        }else {
            Toast.makeText(getContext(), "Token Issue", Toast.LENGTH_SHORT).show();
        }
    }

    public void dash_apicall() {
        if(MnxPreferenceManager.getString(MnxConstant.TOKEN,null)!=null){
            showLoading();
            dashboardPresenter=new DashboardPresenter(OrderFragment.this,new DashboardIntract());
            dashboardPresenter.validateDetails();
        }else{
            Toast.makeText(getContext(), "Token Issue", Toast.LENGTH_SHORT).show();
        }
    }

    private void callnextscreen(int nVal){
        try {
            Intent myintent;
            if (nVal==1)
             myintent=new Intent(getActivity(), NotificationActivity.class);
            else {
                Log.i("TESTCALL","TESTCALL");
                myintent = new Intent(getActivity(), ProfileActivity.class);
            }
            startActivity(myintent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dash_success(DashboardResponse dashboardResponse) {
        try {
            hideLoading();
            if (dashboardResponse != null) {
                if (dashboardResponse.getStatus()) {
                    MnxPreferenceManager.setString(MnxConstant.NOTIFICATION_COUNT, dashboardResponse.getCount());
                    MnxPreferenceManager.setString(MnxConstant.TERMS_CONDITION, dashboardResponse.getTerms_and_conditions());
                    MnxPreferenceManager.setString(MnxConstant.PRIVACY_POLICY, dashboardResponse.getPrivacy_policy());
                    //((HomeScreenActivity) Objects.requireNonNull(getActivity())).update_notification_count();
                    if (getActivity() instanceof HomeScreenActivity) {
                        ((HomeScreenActivity) getActivity()).update_notification_count();
                    }
                    if (dashboardResponse.getOrders() != null) {
                        if (dashboardResponse.getOrders().size() > 0) {
                            no_data_constraint.setVisibility(View.INVISIBLE);
                            Log.d("Sizeeee", "dash_success: size " + dashboardResponse.getOrders().size());
                            ordersAdapter = new OrdersAdapter(getActivity(), dashboardResponse.getOrders(), OrderFragment.this);
                            recyclerView.setAdapter(ordersAdapter);
                        } else {
                            no_data_constraint.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), dashboardResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            Log.d(TAG, "dash_success: exception "+e.getMessage());
        }

    }

    @Override
    public void dash_failure(String msg) {
        try {
            hideLoading();
            no_data_constraint.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Log.d(TAG, "dash_failure: exception "+e.getMessage());
        }
    }



    @Override
    public void accept_success(GeneralResponse generalResponse) {
        hideLoading();
        if(generalResponse!=null){
            if(generalResponse.getStatus()){
                Toast.makeText(getContext(), generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                //dash_apicall();
                ((HomeScreenActivity) Objects.requireNonNull(getActivity())).do_move_to_ongoing_frag();
            }else{
                Toast.makeText(getContext(), generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void accept_failure(String msg) {
        hideLoading();
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void order_accept_apicall(String order_id) {
        if (MnxPreferenceManager.getString(MnxConstant.TOKEN, null) != null) {
            showLoading();
            acceptPresenter = new AcceptPresenter(OrderFragment.this, new AcceptIntract());
            acceptPresenter.validateDetails(order_id);
        }else{
            Toast.makeText(getContext(), "User Not Logged-In", Toast.LENGTH_SHORT).show();
        }
    }

    public void order_reject_apicall(String order_id) {
        if (MnxPreferenceManager.getString(MnxConstant.TOKEN, null) != null) {
            showLoading();
            rejectPresenter = new RejectPresenter(OrderFragment.this, new RejectIntract());
            rejectPresenter.validateDetails(order_id);
        }else{
            Toast.makeText(getContext(), "User Not Logged-In", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void reject_success(GeneralResponse generalResponse) {
        hideLoading();
        if(generalResponse!=null){
            if(generalResponse.getStatus()){
                Toast.makeText(getContext(), generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
                dash_apicall();
            }else{
                Toast.makeText(getContext(), generalResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void reject_failure(String msg) {
        hideLoading();
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void search_success(SearchResultResponse searchResultResponse) {
        hideLoading();
        if(searchResultResponse!=null){
            if(searchResultResponse.getStatus()){
                if(searchResultResponse.getOrders()!=null) {
                 if(searchResultResponse.getOrders().size()>0) {
                     recyclerView.removeAllViewsInLayout();
                     Toast.makeText(getContext(), searchResultResponse.getMessage(), Toast.LENGTH_SHORT).show();
                     ordersAdapter= new OrdersAdapter(getActivity(),searchResultResponse.getOrders(),OrderFragment.this);
                     recyclerView.setAdapter(ordersAdapter);
                 }else{
                     Toast.makeText(getContext(), "Order not found", Toast.LENGTH_SHORT).show();
                 }
                }else{
                    Toast.makeText(getContext(), "Order not found", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getContext(), searchResultResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void search_failure(String msg) {
        hideLoading();
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dashboard_logout() {
            hideLoading();
            do_logout_and_login_redirect();
    }
}
