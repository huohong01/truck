package com.hsdi.NetMe.ui.startup.phone_verification;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hsdi.NetMe.R;
import com.hsdi.theme.basic.BaseThemeActivity;

import static com.hsdi.theme.basic.ThemeLayoutInflaterFactory.ThemeTypeValue.background_color;

public class PinEntryFragment extends Fragment {

    private PRCommunications prCom;
    private View main;
    private Button verifyBtn;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        main = inflater.inflate(R.layout.fragment_pin_entry, container, false);
        verifyBtn = (Button) main.findViewById(R.id.pin_verify_btn);
        view = main.findViewById(R.id.pin_entry_view);

        verifyBtn.setBackgroundColor(getResources().getColor(R.color.primary_dark));
       ((BaseThemeActivity) getActivity()).applyTheme(verifyBtn, R.color.primary_dark, background_color);

        view.setBackgroundColor(getResources().getColor(R.color.primary_dark));
        ((BaseThemeActivity) getActivity()).applyTheme(view, R.color.primary_dark, background_color);


        //setting the message in the top text view depending on if the user granted permission
        //  to read SMS messages
        TextView topText = (TextView) main.findViewById(R.id.pin_top_message);
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
            topText.setText(R.string.pin_retrieval_auto_msg);
        }
        else topText.setText(R.string.pin_retrieval_manual_msg);

        return main;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupListeners();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {prCom = (PRCommunications) context;}
        catch (Exception e) {
            throw new ClassCastException("Calling activity must implement " + PRCommunications.class.getSimpleName());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {prCom = (PRCommunications) activity;}
        catch (Exception e) {
            throw new ClassCastException("Calling activity must implement " + PRCommunications.class.getSimpleName());
        }
    }

    private void setupListeners() {
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPin();
            }
        });
    }

    private void checkPin() {
        final TextInputEditText pinET = (TextInputEditText) main.findViewById(R.id.pin_entry_field);
        pinET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    pinET.setHintTextColor(getResources().getColor(R.color.btn_primary));
                }
            }
        });
        String enteredPin = pinET.getText().toString();

        if(enteredPin.isEmpty()) pinET.setError(getString(R.string.error_invalid_pin));
        Log.i("yuyong_phone", "checkPin: " + enteredPin);
        prCom.setPin(null, enteredPin);
    }


  /*  @Override
    public boolean onBackPressed() {
        Toast.makeText(getActivity(),"你点击了返回键",Toast.LENGTH_SHORT).show();
        return super.onBackPressed();
    }*/
}
