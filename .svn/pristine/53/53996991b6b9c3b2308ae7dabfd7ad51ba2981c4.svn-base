package com.hsdi.NetMe.ui.startup.change_password;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hsdi.NetMe.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by huohong.yi on 2017/3/17.
 */

public class ChangePwdSuccessFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.change_pwd_success_check)
    ImageView changePwdSuccessCheck;
    @Bind(R.id.change_success_message)
    TextView changeSuccessMessage;

    private IChangePassword changePwdListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_pwd_success, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        changePwdSuccessCheck.setOnClickListener(this);
        changeSuccessMessage.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
       changePwdListener.onReturnToLogin();
    }
}
