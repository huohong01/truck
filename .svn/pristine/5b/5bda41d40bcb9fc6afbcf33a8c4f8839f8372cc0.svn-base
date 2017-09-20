package com.hsdi.NetMe.ui.startup.recovery;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hsdi.NetMe.R;
import com.hsdi.theme.basic.BaseThemeActivity;
import com.hsdi.theme.basic.ThemeLayoutInflaterFactory;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.hsdi.NetMe.R.drawable.check_icon;

public class RecoverySuccessFragment extends Fragment {
    private static final String TAG = "RecoverySuccessFrag";

    @Bind(R.id.recovery_success_check)View checkView;
    @Bind(R.id.recovery_success_bottom_message)View bottomMessageView;

    private RecoveryCommunications recoveryListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recovery_success, container, false);
        ButterKnife.bind(this, view);


       // ((BaseThemeActivity) getActivity()).applyTheme(checkView, check_icon, ThemeLayoutInflaterFactory.ThemeTypeValue.src);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoveryListener.onReturnToLogin();
            }
        };

        checkView.setOnClickListener(onClickListener);
        bottomMessageView.setOnClickListener(onClickListener);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((BaseThemeActivity) getActivity()).applyTheme(checkView, check_icon, ThemeLayoutInflaterFactory.ThemeTypeValue.src);
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
