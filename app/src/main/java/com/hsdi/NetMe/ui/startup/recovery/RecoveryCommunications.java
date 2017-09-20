package com.hsdi.NetMe.ui.startup.recovery;

public interface RecoveryCommunications {
    void onRecoverySuccess();
    void onRecoveryFailed();
    void onTryAgain();
    void onReturnToLogin();
    void onSignUp();
}
