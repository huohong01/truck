package com.hsdi.NetMe.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.macate.csmp.CSMPCryptException;
import com.macate.csmp.CSMPCryptStringUtil;
import com.macate.csmp.CSMPIndexingKeyGenerator;
import com.macate.csmp.CSMPKeyGenerator;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import static com.hsdi.NetMe.NetMeApp.pwdManager;

public class PreferenceManager {
    private static final String TAG = "preference";

    private static final String NETME_PREF_NAME = "Codetel msg preferences";
    private static final String GCM_PREF_NAME = "Codetel msg gcm preferences";

    private static final String PREF_APK_VERSION = "versionName";
    private static final String PREF_USER_COUNTRY_CALLING_CODE = "userCC";
    private static final String PREF_USER_COUNTRY_CODE = "userCountryCode";
    private static final String PREF_USER_PHONE_NUMBER = "userPN";
    private static final String PREF_USER_PASS_KEY = "password";
    private static final String PREF_USER_REMEMBER_LOGIN_KEY = "logging status";
    private static final String PREF_USER_LOGOUT_KEY = "logout status";
    private static final String PREF_FIRST_NAME_KEY = "firstname";
    private static final String PREF_LAST_NAME_KEY = "lastname";
    private static final String PREF_EMAIL_KEY = "email";
    private static final String PREF_USER_AVATAR_KEY = "avatarUrl";

    private static final String PREF_DO_NOT_DISTURB_KEY = "do not disturb";
    private static final String GCM_REG_ID = "gcm_registration_id";
    private static final String PREF_CONTACT_REG_CHECK = "contact_reg_check_time";

    private static final String PREF_INITIAL_TUTORIAL_SHOWN = "initial_tutorial_shown";
    private static final String PREF_MAIN_TUTORIAL_SHOWN = "main_tutorial_shown";
    private static final String PREF_CHAT_TUTORIAL_SHOWN = "chat_tutorial_shown";

    private static final long timeBetweenRegChecks = 24 * 60 * 60 * 1000; // 24 hours in milliseconds


    private static PreferenceManager instance;
    private SharedPreferences msgPref;
    private SharedPreferences gcmPref;
    private CSMPKeyGenerator keyGen;
    private AESCrypt aesCrypt;


//--------------------------------------------------------------------------------------------------


    private PreferenceManager(Context context) {
        msgPref = context.getSharedPreferences(NETME_PREF_NAME, Context.MODE_PRIVATE);
        gcmPref = context.getSharedPreferences(GCM_PREF_NAME, Context.MODE_PRIVATE);
        keyGen = getKeyGenerator(context);
       // aesCrypt = getAESCrypt(context);
        aesCrypt = getAesCrypt();
    }

    public static PreferenceManager getInstance(Context context) {
        if (instance == null) instance = new PreferenceManager(context);
        return instance;
    }


    //--------------------------------------------------------------------------------------------------
    public void setVersion(Context context) {
        String versionName = VersionUtils.getApkVersion(context);
        SharedPreferences.Editor editor = msgPref.edit();
        editor.putString(PREF_APK_VERSION, versionName);
        editor.apply();
    }

    public String getVersionName() {
        String versionName = msgPref.getString(PREF_APK_VERSION, "");
        return versionName;
    }

    /**
     * sets the user as binging logged in so that on the next start of the app, the login process is skipped
     */
    public void setRememberUser() {
        msgPref.edit().putBoolean(PREF_USER_REMEMBER_LOGIN_KEY, true).apply();
    }

    /**
     * Check if the user have already logged in before
     */
    public boolean userLoginRemembered() {
        return msgPref.getBoolean(PREF_USER_REMEMBER_LOGIN_KEY, false);
    }

    public void removeRemenberMe() {
        SharedPreferences.Editor editor = msgPref.edit();
        editor.remove(PREF_USER_REMEMBER_LOGIN_KEY);
        editor.apply();
    }


    /**
     * Check if the user's country and phone number have been set
     */
    public boolean haveUsername() {
        String encryptedCountryCallingCode = msgPref.getString(PREF_USER_COUNTRY_CALLING_CODE, "");
        String encryptedPhoneNumber = msgPref.getString(PREF_USER_PHONE_NUMBER, "");

        // try decrypting using AES
        try {
            String decryptedCC = aesCrypt.decryptString(encryptedCountryCallingCode);
            String decryptedPN = aesCrypt.decryptString(encryptedPhoneNumber);
            return !decryptedCC.isEmpty() && !decryptedPN.isEmpty();
        } catch (Exception e) {
            Log.d(TAG, "Failed to decrypt username using AES", e);
        }

        // try decrypting using Codetel
        try {
            // decrypt
            String decryptedCC = decryptString(encryptedCountryCallingCode);
            String decryptedPN = decryptString(encryptedPhoneNumber);

            // update using new encryption
            String aesCC = aesCrypt.encryptString(decryptedCC);
            String aesPN = aesCrypt.encryptString(decryptedPN);

            // store updates
            SharedPreferences.Editor editor = msgPref.edit();
            editor.putString(PREF_USER_COUNTRY_CALLING_CODE, aesCC);
            editor.putString(PREF_USER_PHONE_NUMBER, aesPN);
            editor.apply();

            // return result
            return !decryptedCC.isEmpty() && !decryptedPN.isEmpty();
        } catch (CSMPCryptException e) {
            Log.d(TAG, "Failed to decrypt username using Codetel", e);
        } catch (Exception e) {
            Log.d(TAG, "Failed to decrypt username using Codetel or to update using AES", e);
        }
        Log.i(TAG, "haveUsername: " + (!encryptedCountryCallingCode.isEmpty() && !encryptedPhoneNumber.isEmpty()));
        // just return what is there
        return !encryptedCountryCallingCode.isEmpty() && !encryptedPhoneNumber.isEmpty();
    }

    /**
     * Returns the current user's username
     */
    public String getUsername() {
        String encryptedCountryCallingCode = msgPref.getString(PREF_USER_COUNTRY_CALLING_CODE, "");
        String encryptedPhoneNumber = msgPref.getString(PREF_USER_PHONE_NUMBER, "");

        // try decrypting using AES
        try {
            String decryptedCC = aesCrypt.decryptString(encryptedCountryCallingCode);
            String decryptedPN = aesCrypt.decryptString(encryptedPhoneNumber);
            return decryptedCC + decryptedPN;
        } catch (Exception e) {
            Log.d(TAG, "Failed to decrypt username using AES", e);
        }

        // try decrypting using Codetel
        try {
            // decrypt
            String decryptedCC = decryptString(encryptedCountryCallingCode);
            String decryptedPN = decryptString(encryptedPhoneNumber);

            // update using new encryption
            String aesCC = aesCrypt.encryptString(decryptedCC);
            String aesPN = aesCrypt.encryptString(decryptedPN);

            // store updates
            SharedPreferences.Editor editor = msgPref.edit();
            editor.putString(PREF_USER_COUNTRY_CALLING_CODE, aesCC);
            editor.putString(PREF_USER_PHONE_NUMBER, aesPN);
            editor.apply();

            // return result
            return decryptedCC + decryptedPN;
        } catch (CSMPCryptException e) {
            Log.d(TAG, "Failed to decrypt username using Codetel", e);
        } catch (Exception e) {
            Log.d(TAG, "Failed to decrypt username using Codetel or to update using AES", e);
        }

        // just return what is there
        return encryptedCountryCallingCode + encryptedPhoneNumber;
    }

    /**
     * Sets the users country code and phone number
     */
    public void setUsername(String countryCode, String countryCallingCode, String phoneNumber) {
        try {
            SharedPreferences.Editor editor = msgPref.edit();

            editor.putString(PREF_USER_COUNTRY_CODE, aesCrypt.encryptString(countryCode));
            editor.putString(PREF_USER_COUNTRY_CALLING_CODE, aesCrypt.encryptString(countryCallingCode));
            editor.putString(PREF_USER_PHONE_NUMBER, aesCrypt.encryptString(phoneNumber));
            editor.apply();
            Log.i(TAG, "setUsername: " + aesCrypt.hashCode());
        } catch (Exception e) {
            Log.d(TAG, "Failed to store all the username parts", e);
        }
    }

    /**
     * Returns the country code for the
     */
    public String getCountryCode() {
        String encryptedCountryCode = msgPref.getString(PREF_USER_COUNTRY_CODE, "");

        if (encryptedCountryCode.isEmpty()) return Locale.getDefault().getCountry();
        else {
            // try decrypting using AES
            try {
                return aesCrypt.decryptString(encryptedCountryCode);
            } catch (Exception e) {
                Log.d(TAG, "Failed to decrypt country code using AES", e);
            }

            // try decrypting using Codetel
            try {
                // decrypt
                String countryCode = decryptString(encryptedCountryCode);

                // update using new encryption
                String aesCC = aesCrypt.encryptString(countryCode);

                // store updates
                SharedPreferences.Editor editor = msgPref.edit();
                editor.putString(PREF_USER_COUNTRY_CODE, aesCC);
                editor.apply();

                // return result
                return countryCode;
            } catch (CSMPCryptException e) {
                Log.d(TAG, "Failed to decrypt country code using Codetel", e);
            } catch (Exception e) {
                Log.d(TAG, "Failed to decrypt country code using Codetel or update using AES", e);
            }

            // just return what is there
            return encryptedCountryCode;
        }
    }

    /**
     * Returns the user's calling code
     */
    public String getCountryCallingCode() {
        String encryptedCallingCode = msgPref.getString(PREF_USER_COUNTRY_CALLING_CODE, "");

        // try decrypting using AES
        try {
            return aesCrypt.decryptString(encryptedCallingCode);
        } catch (Exception e) {
            Log.d(TAG, "Failed to decrypt calling code using AES", e);
        }

        // try decrypting using Codetel
        try {
            // decrypt
            String callingCode = decryptString(encryptedCallingCode);

            // update using new encryption
            String aesCC = aesCrypt.encryptString(callingCode);

            // store updates
            SharedPreferences.Editor editor = msgPref.edit();
            editor.putString(PREF_USER_COUNTRY_CALLING_CODE, aesCC);
            editor.apply();

            // return result
            return callingCode;
        } catch (CSMPCryptException e) {
            Log.d(TAG, "Failed to decrypt calling code using Codetel", e);
        } catch (Exception e) {
            Log.d(TAG, "Failed to decrypt calling code using Codetel or to update using AES", e);
        }

        // just return what is there
        return encryptedCallingCode;
    }

    /**
     * Returns the user's phone number
     */
    public String getPhoneNumber() {
        String encryptedPhoneNumber = msgPref.getString(PREF_USER_PHONE_NUMBER, "");

        // try decrypting using AES
        try {
            return aesCrypt.decryptString(encryptedPhoneNumber);
        } catch (Exception e) {
            Log.d(TAG, "Failed to decrypt phone number using AES", e);
        }

        // try decrypting using Codetel
        try {
            // decrypt
            String phoneNumber = decryptString(encryptedPhoneNumber);

            // update using new encryption
            String aesPN = aesCrypt.encryptString(phoneNumber);

            // store updates
            SharedPreferences.Editor editor = msgPref.edit();
            editor.putString(PREF_USER_PHONE_NUMBER, aesPN);
            editor.apply();
            // return result
            return phoneNumber;
        } catch (CSMPCryptException e) {
            Log.d(TAG, "Failed to decrypt phone number using Codetel", e);
        } catch (Exception e) {
            Log.d(TAG, "Failed to decrypt phone number using Codetel or to update AES", e);
        }
        Log.i(TAG, "getPhoneNumber: " + aesCrypt.hashCode() + ",encryptedPhoneNumber = " + encryptedPhoneNumber);
        // just return what is there
        return encryptedPhoneNumber;
    }

    /**
     * stores the user's password
     */
    public void setPassword(String password) {
        try {
            msgPref.edit().putString(PREF_USER_PASS_KEY, aesCrypt.encryptString(password)).apply();
        } catch (Exception e) {
            Log.d(TAG, "Failed to store the password", e);
        }
    }

    /**
     * returns the user's password or an empty string if the password was not found
     */
    public String getPassword() {
        String encryptedPassword = msgPref.getString(PREF_USER_PASS_KEY, "");

        // try decrypting using AES
        try {
            return aesCrypt.decryptString(encryptedPassword);
        } catch (Exception e) {
            Log.d(TAG, "Failed to decrypt password using AES", e);
        }

        // try decrypting using Codetel
        try {
            // decrypt
            String password = decryptString(encryptedPassword);
            // update using the new encryption
            setPassword(password);
            // return result
            return password;
        } catch (CSMPCryptException e) {
            Log.d(TAG, "Failed to decrypt password using Codetel", e);
        }
        Log.i(TAG, "getPassword: " + aesCrypt.hashCode() + ",encryptedPassword = " + encryptedPassword);
        // just return what is there
        return encryptedPassword;
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Get current first name string, return empty string if failed
     */
    public String getFirstName() {
        String encryptedName = msgPref.getString(PREF_FIRST_NAME_KEY, "");

        // try decrypting using AES
        try {
            return aesCrypt.decryptString(encryptedName);
        } catch (Exception e) {
            Log.d(TAG, "Failed to decrypt first name using AES", e);
        }

        // try decrypting using Codetel
        try {
            // decrypt
            String firstName = decryptString(encryptedName);
            // update using the new encryption
            setFirstName(firstName);
            // return result
            return firstName;
        } catch (CSMPCryptException e) {
            Log.d(TAG, "Failed to decrypt first name using Codetel", e);
        }

        // just return what is there
        return encryptedName;
    }

    /**
     * Set current first name
     */
    public void setFirstName(String firstName) {
        try {
            SharedPreferences.Editor editor = msgPref.edit();
            editor.putString(PREF_FIRST_NAME_KEY, aesCrypt.encryptString(firstName));
            editor.apply();
        } catch (Exception e) {
            Log.d(TAG, "Failed to store the first name", e);
        }
    }

    /**
     * Get current last name string, return empty string if failed
     */
    public String getLastName() {
        String encryptedName = msgPref.getString(PREF_LAST_NAME_KEY, "");

        // try decrypting using AES
        try {
            return aesCrypt.decryptString(encryptedName);
        } catch (Exception e) {
            Log.d(TAG, "Failed to decrypt last name using AES", e);
        }

        // try decrypting using Codetel
        try {
            // decrypt
            String lastName = decryptString(encryptedName);
            // update using the new encryption
            setLastName(lastName);
            // return result
            return lastName;
        } catch (CSMPCryptException e) {
            Log.d(TAG, "Failed to decrypt last name using Codetel", e);
        }

        // just return what is there
        return encryptedName;
    }

    /**
     * Set current last name
     */
    public void setLastName(String lastName) {
        try {
            SharedPreferences.Editor editor = msgPref.edit();
            editor.putString(PREF_LAST_NAME_KEY, aesCrypt.encryptString(lastName));
            editor.apply();
        } catch (Exception e) {
            Log.d(TAG, "Failed to store the last name", e);
        }
    }

    /**
     * returns the user's email address or an empty string if one was not found
     */
    public String getEmail() {
        String encryptedEmail = msgPref.getString(PREF_EMAIL_KEY, "");

        // try decrypting using AES
        try {
            return aesCrypt.decryptString(encryptedEmail);
        } catch (Exception e) {
            Log.d(TAG, "Failed to decrypt email using AES", e);
        }

        // try decrypting using Codetel
        try {
            // decrypt
            String email = decryptString(encryptedEmail);
            // update using the new encryption
            setEmail(email);
            // return result
            return email;
        } catch (CSMPCryptException e) {
            Log.d(TAG, "Failed to decrypt email using Codetel", e);
        }

        // just return what is there
        return encryptedEmail;
    }

    /**
     * Set current email
     */
    public void setEmail(String email) {
        try {
            SharedPreferences.Editor editor = msgPref.edit();
            editor.putString(PREF_EMAIL_KEY, aesCrypt.encryptString(email));
            editor.apply();
        } catch (Exception e) {
            Log.d(TAG, "Failed to store email", e);
        }
    }

    /**
     * returns the user's user's avatar url string
     */
    public String getAvatarUrl() {
        String encryptedUserId = msgPref.getString(PREF_USER_AVATAR_KEY, "");

        // try decrypting using AES
        try {
            return aesCrypt.decryptString(encryptedUserId);
        } catch (Exception e) {
            Log.d(TAG, "Failed to decrypt avatar url using AES", e);
        }

        // try decrypting using Codetel
        try {
            // decrypt
            String avatarUrl = decryptString(encryptedUserId);
            // update using the new encryption
            setAvatarUrl(avatarUrl);
            // return result
            return avatarUrl;
        } catch (CSMPCryptException e) {
            Log.d(TAG, "Failed to decrypt avatar url using Codetel", e);
        }

        // just return what is there
        return encryptedUserId;
    }

    /**
     * sets the current user's avatar url string
     */
    public void setAvatarUrl(String avatarUrl) {
        try {
            SharedPreferences.Editor editor = msgPref.edit();
            editor.putString(PREF_USER_AVATAR_KEY, aesCrypt.encryptString(avatarUrl));
            editor.apply();
        } catch (Exception e) {
            Log.d(TAG, "Failed to store the avatar url", e);
        }
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Gets the GCM Registration Id or an empty string if one was not found
     */
    public String getGCMRegistrationId() {
        String encryptedRegId = gcmPref.getString(GCM_REG_ID, "");

        // try decrypting using AES
        try {
            return aesCrypt.decryptString(encryptedRegId);
        } catch (Exception e) {
            Log.d(TAG, "Failed to decrypt GCM using AES", e);
        }

        // try decrypting using Codetel
        try {
            // decrypt
            String regId = decryptString(encryptedRegId);
            // update using the new encryption
            storeGCMRegistrationId(regId);
            // return result
            return regId;
        } catch (CSMPCryptException e) {
            Log.d(TAG, "Failed to decrypt GCM using Codetel", e);
        }

        // just return what is there
        return encryptedRegId;
    }

    /**
     * Stores the GCM Registration Id
     */
    public void storeGCMRegistrationId(String regId) {
        try {
            gcmPref.edit().putString(GCM_REG_ID, aesCrypt.encryptString(regId)).apply();
        } catch (Exception e) {
            Log.d(TAG, "Failed to store GCM", e);
        }
    }

    /**
     * Sets the do not disturb mode on or off
     */
    public void setDoNotDisturb(boolean doNotDisturbOn) {
        msgPref.edit().putBoolean(PREF_DO_NOT_DISTURB_KEY, doNotDisturbOn).apply();
    }

    /**
     * Returns the boolean state of do not disturb
     */
    public boolean getDoNotDisturb() {
        return msgPref.getBoolean(PREF_DO_NOT_DISTURB_KEY, false);
    }

    /**
     * Sets that the contact's registrations have been checked at the current time
     */
    public void setRegisteredContactsChecked() {
        msgPref.edit().putLong(PREF_CONTACT_REG_CHECK, System.currentTimeMillis()).apply();
    }

    /**
     * Determines if the contact's registrations need to be check
     */
    public boolean isTimeToCheckContactRegistrations() {
        // get the last check time and the current check time in milliseconds
        long lastCheckTime = msgPref.getLong(PREF_CONTACT_REG_CHECK, 0);
        long currentTime = System.currentTimeMillis();

        // if more time has passed from the last check then what timeBetweenRegChecks is
        //    equal to, perform a check
        return currentTime - lastCheckTime > timeBetweenRegChecks;
    }

    /**
     * Resets the time to check parameter to make sure that the next check will return that it is
     * time to check.
     **/
    public void resetTimeToCheckContactRegistrations() {
        msgPref.edit().putLong(PREF_CONTACT_REG_CHECK, 0).apply();
    }


    //--------------------------------------------------------------------------------------------------
 /* Updates the status of the tutorial shown on startup to true*/
    public void setSkipTutorialShown() {
        msgPref.edit().putBoolean(PREF_MAIN_TUTORIAL_SHOWN, true).apply();
        msgPref.edit().putBoolean(PREF_CHAT_TUTORIAL_SHOWN, true).apply();
    }


    /**
     * Returns true if the tutorial on the startup has been shown, false otherwise
     */
    public boolean initialTutorialShown() {
        return msgPref.getBoolean(PREF_INITIAL_TUTORIAL_SHOWN, false);
    }

    /**
     * Updates the status of the tutorial shown on startup to true
     */
    public void setInitialTutorialShown() {
        msgPref.edit().putBoolean(PREF_INITIAL_TUTORIAL_SHOWN, true).apply();
    }

    /**
     * Returns true if the tutorial on the main screen has been shown, false otherwise
     */
    public boolean mainTutorialShown() {
        return msgPref.getBoolean(PREF_MAIN_TUTORIAL_SHOWN, false);
    }

    /**
     * Updates the status of the tutorial shown on the main screen to true
     */
    public void setMainTutorialShown() {
        msgPref.edit().putBoolean(PREF_MAIN_TUTORIAL_SHOWN, true).apply();
    }

    /**
     * Returns true if the tutorial on the chat screen has been shown, false otherwise
     */
    public boolean chatTutorialShown() {
        return msgPref.getBoolean(PREF_CHAT_TUTORIAL_SHOWN, false);
    }

    /**
     * Updates the status of the tutorial shown on chat screen to true
     */
    public void setChatTutorialShown() {
        msgPref.edit().putBoolean(PREF_CHAT_TUTORIAL_SHOWN, true).apply();
    }

    /**
     * ONLY FOR TESTING
     * Resets the status of all the tutorial so they are all shown again
     */
    public void resetAllTutorialShownStatus() {
        msgPref.edit()
                .putBoolean(PREF_INITIAL_TUTORIAL_SHOWN, false)
                .putBoolean(PREF_MAIN_TUTORIAL_SHOWN, false)
                .putBoolean(PREF_CHAT_TUTORIAL_SHOWN, false)
                .apply();
    }

    public void setLoginStatus(boolean isLogined) {
        msgPref.edit().putBoolean(PREF_USER_LOGOUT_KEY, isLogined).apply();
    }

    public boolean getLoginStatus() {
        return msgPref.getBoolean(PREF_USER_LOGOUT_KEY, false);
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Removes the user login status and password, also resets all other timers
     */
    public boolean logout() {
        SharedPreferences.Editor editor = msgPref.edit();
        //  editor.remove(PREF_USER_REMEMBER_LOGIN_KEY);
        //  editor.remove(PREF_USER_PASS_KEY);
        editor.remove(PREF_CONTACT_REG_CHECK);
        return editor.commit();
    }

    /**
     * Removes all the preferences stored for this application which is needed when switching users
     */
    public void switchUsers() {
        boolean genResult = msgPref.edit().clear().commit();
        boolean gcmResult = gcmPref.edit().clear().commit();
        Log.i("hong", String.format("switchUsers ------->%s-------->%s", genResult, gcmResult));
        System.out.println("Deleting sharePrefs results, gen = " + genResult + "; gcm = " + gcmResult);
    }

    public void cleanSharedPreference() {
        msgPref.edit().clear().commit();
        gcmPref.edit().clear().commit();
    }


//--------------------------------------------------------------------------------------------------

    /**
     * Decrypt the string. If decrypted failed, return the original encrypted string.
     *
     * @return decrypted string or original string if decryption failed
     */
    private String decryptString(String string) throws CSMPCryptException {

        if (string == null || string.isEmpty()) return "";
        else {
            Log.i("CSMPCrypt", "decryptString: string = " + string);
            return CSMPCryptStringUtil.decrypt(keyGen, string);
        }
    }

    /**
     * Creates a key generator which will be used to encrypt everything in the preferences
     */
    private CSMPKeyGenerator getKeyGenerator(Context context) {
        // get the stored salt and indexer values
        SharedPreferences sharedPreferences = context.getSharedPreferences("CodeTel Key", Context.MODE_PRIVATE);
        String salt = sharedPreferences.getString("st", "");
        String indexer = sharedPreferences.getString("idx", "");
        Log.i(TAG, "getKeyGenerator: salt = " + salt + ",indexer = " + indexer);
        // getting the byte representation of the salt and indexer
        byte[] saltBytes;
        byte[] indexerBytes;
        try {
            saltBytes = salt.getBytes("ISO-8859-1");
            indexerBytes = indexer.getBytes("ISO-8859-1");
        }
        // should never be here, but just in case, use the normal get bytes method
        catch (UnsupportedEncodingException un) {
            saltBytes = salt.getBytes();
            indexerBytes = indexer.getBytes();
        }

        // creating the key generator
        return new CSMPIndexingKeyGenerator(indexerBytes, saltBytes);
    }

    /**
     * Creates the AESCrypt object which will be used to encrypt all the preferences
     */
    private AESCrypt getAESCrypt(Context context) {
        // get the stored salt and indexer values
        SharedPreferences sharedPreferences = context.getSharedPreferences("AES key", Context.MODE_PRIVATE);
        String password = sharedPreferences.getString("password", "");
        Log.i(TAG, "getAESCrypt: password = " + password);
        // if there is no stored password, create one and store it for future use
        if (password.isEmpty()) {
            // this part is done for backwards compatibility with devices which aren't currently storing the password
            // making a random string to use as the password
            password = Utils.random();
            sharedPreferences.edit().putString("password", password).apply();
            Log.i(TAG, "getAESCrypt:---> password = " + password);
        }
        // initializing the AESCrypt class
        try {
            return new AESCrypt(password);
        } catch (Exception e) {
            Log.e(TAG, "failed to get the AESCrypt", e);
            return null;
        }
    }
    private AESCrypt getAesCrypt(){
        String password = pwdManager.getPwd("aes_key");
        try {
            return new AESCrypt(password);
        } catch (Exception e) {
            Log.e(TAG, "failed to get the AESCrypt", e);
            e.printStackTrace();
        }
        return null;
    }

    public boolean setFirstLogin() {
        boolean firstLogin = msgPref.edit().putBoolean("first_login", true).commit();
        return firstLogin;
    }

    public boolean getFirstLogin() {
        return msgPref.getBoolean("first_login", false);
    }
}
