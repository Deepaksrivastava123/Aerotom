package com.aerotom.aerotom.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;


import com.aerotom.aerotom.R;
import com.aerotom.aerotom.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.aerotom.aerotom.utils.SessionManager.policy;


public class PrivecyPolicyActivity extends BasicActivity {

    @BindView(R.id.txt_privacy)
    TextView txtPrivacy;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privecypolicy);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtPrivacy.setText(Html.fromHtml(sessionManager.getStringData(policy), Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtPrivacy.setText(Html.fromHtml(sessionManager.getStringData(policy)));
        }
    }
}
