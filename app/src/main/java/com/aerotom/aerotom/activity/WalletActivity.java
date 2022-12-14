package com.aerotom.aerotom.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aerotom.aerotom.R;
import com.aerotom.aerotom.adepter.TrazectionAdapter;
import com.aerotom.aerotom.model.User;
import com.aerotom.aerotom.model.Wallet;
import com.aerotom.aerotom.retrofit.APIClient;
import com.aerotom.aerotom.retrofit.GetResult;
import com.aerotom.aerotom.utils.CustPrograssbar;
import com.aerotom.aerotom.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class WalletActivity extends BasicActivity implements GetResult.MyListener {
    @BindView(R.id.txt_wallet)
    TextView txtWallet;
    @BindView(R.id.txt_income)
    TextView txtIncome;
    @BindView(R.id.txt_expence)
    TextView txtExpence;
    @BindView(R.id.recycle_trazection)
    RecyclerView recycleTrazection;

    SessionManager sessionManager;
    User user;
    CustPrograssbar custPrograssbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        getSupportActionBar().setTitle("My Wallet");
        ButterKnife.bind(this);

        recycleTrazection.setLayoutManager(new LinearLayoutManager(this));
        recycleTrazection.setItemAnimator(new DefaultItemAnimator());


        sessionManager = new SessionManager(WalletActivity.this);
        user = sessionManager.getUserDetails("");
        custPrograssbar = new CustPrograssbar();
        getWallet();

    }

    private void getWallet() {
        custPrograssbar.prograssCreate(WalletActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getWallet(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Wallet wallet = gson.fromJson(result.toString(), Wallet.class);
                if (wallet.getResult().equalsIgnoreCase("true")) {
                    txtWallet.setText(sessionManager.getStringData(SessionManager.currency) + wallet.getWallets());
                    txtExpence.setText(sessionManager.getStringData(SessionManager.currency) + wallet.getDebittotal());
                    txtIncome.setText(sessionManager.getStringData(SessionManager.currency) + wallet.getCredittotal());
                    sessionManager.setStringData(SessionManager.wallet,wallet.getWallets());
                    TrazectionAdapter adapter = new TrazectionAdapter(this, wallet.getWalletitem());
                    recycleTrazection.setAdapter(adapter);
                }
            }

        } catch (Exception e) {
            Log.e("Error", "" + e.toString());


        }

    }
}