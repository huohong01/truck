package com.hsdi.NetMe.ui.startup.recovery;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.hsdi.NetMe.R;
import com.hsdi.theme.basic.BaseThemeActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.hsdi.NetMe.R.id.recovery_failed_signup_btn;
import static com.hsdi.theme.basic.ThemeLayoutInflaterFactory.ThemeTypeValue.background_color;

public class RecoveryFailedFragment extends Fragment{
    private static final String TAG = "RecoveryFailedFrag";

    @Bind(R.id.recovery_failed_retry_btn)Button retryBtn;
    @Bind(recovery_failed_signup_btn)Button signUpBtn;

    private RecoveryCommunications recoveryListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recovery_failed, container, false);
        ButterKnife.bind(this, view);

        signUpBtn.setBackgroundColor(getResources().getColor(R.color.primary_dark));
        ((BaseThemeActivity) getActivity()).applyTheme(signUpBtn, R.color.primary_dark, background_color);

        View softView = getActivity().getWindow().peekDecorView();
        if (softView != null){
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(softView.getWindowToken(),InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoveryListener.onTryAgain();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoveryListener.onSignUp();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecoveryCommunications) {
            recoveryListener = (RecoveryCommunications) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement RecoveryCommunications");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        recoveryListener = null;
    }

}
