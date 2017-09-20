package com.hsdi.NetMe.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.hsdi.NetMe.BaseActivity;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.util.DeviceUtils;

import static com.hsdi.theme.basic.ThemeLayoutInflaterFactory.ThemeTypeValue.toolbar_nav_icon;

/**
 * The Activity which show the User Agreement for this application
 * */
public class EulaActivity extends BaseActivity {
    private View progressbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eula);

        progressbar = findViewById(R.id.eula_progressbar);

        //setup the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.eula_toolbar);
        if(toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.back);
            applyTheme(toolbar,R.drawable.back,toolbar_nav_icon);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        //show the eula
        showEula();
    }

    /**
     * loads the eula url and displays it to the user
     * */
    private void showEula() {
        final WebView wv = (WebView) findViewById(R.id.eula_view);

        if (!DeviceUtils.hasInternetConnection(this) || wv == null) {
            Toast.makeText(this, R.string.error_connection, Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            wv.setWebViewClient(
                    new WebViewClient() {
                        public void onPageFinished(WebView view, String url) {
                            progressbar.setVisibility(View.GONE);
                        }
                    });
            wv.loadUrl(getString(R.string.eula_url));
        }
    }
}