package com.hsdi.NetMe.util;

import android.content.Context;
import android.widget.EditText;

import com.hsdi.NetMe.R;

public class AccountInfoUtils {

    public static String checkOldPassword(Context context, EditText passET) {
        String password = passET.getText().toString();

        // password and confirmation can not be empty
        if (password.isEmpty()) {
            passET.setError(context.getString(R.string.required));
            return null;
        }
        // the password has to be at least 4 characters long
        else if (password.length() < 4) {
            passET.setError(context.getString(R.string.password_short));
            return null;
        }

        // passed all the checks so returning the checked password
        return password;
    }


    /**
     * Checks two {@link EditText} fields for two matching valid passwords.
     * A valid password is a non-empty character string with at least 8 characters.
     * Both EditText entries must also match.
     * @param context       context
     * @param passET        password EditText
     * @param passConfET    password confirmation EditText
     * @return the entered password if valid and matching, null otherwise
     * */
    public static String checkPassword(Context context, EditText passET, EditText passConfET) {
        String password = passET.getText().toString();
        String passwordConf = passConfET.getText().toString();

        // password and confirmation can not be empty
        if (password.isEmpty() ) {
            passET.setError(context.getString(R.string.required));
            return null;
        }else if (passwordConf.isEmpty()){
            passConfET.setError(context.getString(R.string.required));
            return null;
        }
        // the password and confirmation have to be the same
        else if(!password.equals(passwordConf)){
            passConfET.setError(context.getString(R.string.password_mismatch));
            return null;
        }
        // the password has to be at least 4 characters long
        else if (password.length() < 4) {
            passET.setError(context.getString(R.string.password_short));
            return null;
        }

        // passed all the checks so returning the checked password
        return password;
    }

    /**
     * Checks an {@link EditText} field for a valid email.
     * A valid email address is considered any character string containing
     * an '@', a period '.', and is at least 7 characters long.
     * @param context    context
     * @param emailET    email EditText
     * @return the entered email if valid, null otherwise
     * */
    public static String checkEmail(Context context, EditText emailET) {
        String email = emailET.getText().toString();

        if(email.isEmpty()) {
            emailET.setError(context.getString(R.string.required));
            return null;
        }
        else if (!email.contains("@") || !email.contains(".") || email.length() < 7) {
            emailET.setError(context.getString(R.string.invalid));
            return null;
        }

        // passed all the checks so returning the checked email address
        return email;
    }

    /**
     * Checks an {@link EditText} field for a valid first name.
     * A valid first name is considered any character string containing
     * only letters, spaces, and hyphens.
     * @param context    context
     * @param fNameET    first name EditText
     * @return the entered name if valid, null otherwise
     * */
    public static String checkFirstName(Context context, EditText fNameET) {
        String fName = fNameET.getText().toString();

        if(fName.isEmpty()){
            fNameET.setError(context.getString(R.string.required));
            return null;
        }

        for(Character c : fName.toCharArray()){
            if(!Character.isLetter(c) && c != ' ' && c != '-'){
                fNameET.setError(context.getString(R.string.invalid));
                return null;
            }
        }

        // passed all the checks so returning the checked first name
        return fName;
    }

    public static String checkFirstName(Context context, EditText fNameET, String originalFirstName) {
        String checkedName = checkFirstName(context, fNameET);

        // if the first check found an error, just propagate the null result forward
        if(checkedName == null) return null;
            // the new first name can not be empty if the original first name is not empty
        else if(!originalFirstName.isEmpty() && checkedName.isEmpty()) {
            fNameET.setError(context.getString(R.string.error_invalid_entry));
            return null;
        }

        // passed all the checks so returning the checked first name
        return checkedName;
    }

    /**
     * Checks an {@link EditText} field for a valid last name.
     * A valid last name is considered any character string containing
     * only letters, spaces, and hyphens.
     * @param context    context
     * @param lNameET    last name EditText
     * @return the entered name if valid, null otherwise
     * */
    public static String checkLastName(Context context, EditText lNameET) {
        String lName = lNameET.getText().toString();

        for(Character c : lName.toCharArray()){
            if(!Character.isLetter(c) && c != ' ' && c != '-'){
                lNameET.setError(context.getString(R.string.invalid));
                return null;
            }
        }

        // passed all the checks so returning the checked last name
        return lName;
    }
}
