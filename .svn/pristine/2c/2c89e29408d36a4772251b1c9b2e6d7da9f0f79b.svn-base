package com.hsdi.NetMe.ui.startup.change_password;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hsdi.NetMe.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by huohong.yi on 2017/3/17.
 */

public class ChangePwdFailedFragment extends Fragment {

    @Bind(R.id.change_failed_retry)
    TextView changeFailedRetry;
    @Bind(R.id.change_failed_sign_in)
    TextView changeFailedSignIn;

    private IChangePassword changePwdListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_pwd_fail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        changeFailedRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePwdListener.onChangePwd();
            }
        });

        changeFailedSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePwdListener.onReturnToLogin();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IChangePassword){
            changePwdListener = (IChangePassword) context;
        }else {
            throw new RuntimeException(context.toString() + "must implement IChangePwd");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        changePwdListener = null;
    }
}
