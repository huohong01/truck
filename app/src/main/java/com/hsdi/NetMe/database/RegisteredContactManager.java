package com.hsdi.NetMe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.flurry.android.FlurryAgent;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.models.Contact;
import com.hsdi.NetMe.models.Participant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class RegisteredContactManager extends DatabaseContract.RegisteredContacts{

    private static final String TRUE = "1";
    private static final String FALSE = "0";


    /**
     * Adds a new registered contact with a specific register phone number
     * @param context             context
     * @param contact             the registered contact
     * @param registeredNumber    the specific registered number for this contact
     * */
    public static void addRegisteredContact(Context context, Contact contact, String registeredNumber) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = null;

        try {
            String selection = COLUMN_NAME_CONTACT_ID + " = ? AND "
                    + COLUMN_NAME_REGISTERED_NUMBER + " = ? AND "
                    + COLUMN_NAME_LOGGED_IN_USER_NUMBER + " = ?";
            String selectionArgs[] = {
                    String.valueOf(contact.getId()),
                    PhoneNumberUtil.normalizeDigitsOnly(registeredNumber),
                    NetMeApp.getCurrentUser()
            };

            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            // if the contact with this registered number already exists, just perform an update and exit
            if(cursor.moveToFirst()) {
                updateContact(context, contact, registeredNumber);
                return;
            }

            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_CONTACT_ID, contact.getId());
            values.put(COLUMN_NAME_REGISTERED_NUMBER, PhoneNumberUtil.normalizeDigitsOnly(registeredNumber));
            values.put(COLUMN_NAME_FAVORITE, contact.isFavorite() ? TRUE : FALSE);
            values.put(COLUMN_NAME_REGISTRATION_VALID, contact.isRegistered() ? TRUE : FALSE);
            values.put(COLUMN_NAME_LOGGED_IN_USER_NUMBER, NetMeApp.getCurrentUser());

            db.insert(TABLE_NAME, null, values);
        }
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("RegContactMan", "Failed to add registered contact", e);
        }
        finally {
            if(cursor != null) cursor.close();
        }
    }

    /**
     * Adds a new registered contact with a specific register phone number
     * @param context             context
     * @param contact             the registered contact
     * @param registeredUser      {@link Participant} with the user's info
     * */
    public static void addRegisteredContact(Context context, Contact contact, Participant registeredUser) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = null;

        try {
            String selection = COLUMN_NAME_CONTACT_ID + " = ? AND "
                    + COLUMN_NAME_REGISTERED_NUMBER + " = ? AND "
                    + COLUMN_NAME_LOGGED_IN_USER_NUMBER + " = ?";
            String selectionArgs[] = {
                    String.valueOf(contact.getId()),
                    PhoneNumberUtil.normalizeDigitsOnly(registeredUser.getUsername()),
                    NetMeApp.getCurrentUser()
            };

            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            // if the contact with this registered number already exists, just perform an update and exit
            if(cursor.moveToFirst()) {
                updateContact(context, contact, registeredUser);
                return;
            }

            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_CONTACT_ID, contact.getId());
            values.put(COLUMN_NAME_REGISTERED_NUMBER, PhoneNumberUtil.normalizeDigitsOnly(registeredUser.getUsername()));
            values.put(COLUMN_NAME_REGISTERED_USER_ID, registeredUser.getUserId());
            values.put(COLUMN_NAME_REGISTERED_FIRST_NAME, registeredUser.getFirstName());
            values.put(COLUMN_NAME_REGISTERED_LAST_NAME, registeredUser.getLastName());
            values.put(COLUMN_NAME_REGISTERED_AVATAR_URL, registeredUser.getAvatarUrl());
            values.put(COLUMN_NAME_REGISTERED_EMAIL, registeredUser.getEmail());
            values.put(COLUMN_NAME_FAVORITE, contact.isFavorite() ? TRUE : FALSE);
            values.put(COLUMN_NAME_REGISTRATION_VALID, contact.isRegistered() ? TRUE : FALSE);
            values.put(COLUMN_NAME_LOGGED_IN_USER_NUMBER, NetMeApp.getCurrentUser());

            db.insert(TABLE_NAME, null, values);
        }
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("RegContactMan", "Failed to add registered contact", e);
        }
        finally {
            if(cursor != null) cursor.close();
        }
    }

    /**
     * Updates a contact's favorite status and registered status on the table to match
     * what the passed in {@link Contact} is set to.
     * @param context             context
     * @param contact             contact object to update with
     * @param registeredNumber    specific registered number from the contact. Passing a null or empty string will cause all instances of the matching contact to be updated.
     * */
    public static void updateContact(Context context, Contact contact, String registeredNumber) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();

        try {
            // adding the values
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_FAVORITE, contact.isFavorite() ? TRUE : FALSE);
            values.put(COLUMN_NAME_REGISTRATION_VALID, contact.isRegistered() ? TRUE : FALSE);
            values.put(COLUMN_NAME_LOGGED_IN_USER_NUMBER, NetMeApp.getCurrentUser());

            // updating the table
            if(registeredNumber != null && !registeredNumber.isEmpty()) {
                String where = COLUMN_NAME_CONTACT_ID + " = ? AND "
                        + COLUMN_NAME_REGISTERED_NUMBER + " = ? AND "
                        + COLUMN_NAME_LOGGED_IN_USER_NUMBER + " = ?";
                String whereArgs[] = {
                        String.valueOf(contact.getId()),
                        PhoneNumberUtil.normalizeDigitsOnly(registeredNumber),
                        NetMeApp.getCurrentUser()
                };
                db.update(TABLE_NAME, values, where, whereArgs);
            }
            else {
                String where = COLUMN_NAME_CONTACT_ID + " = ? AND "
                        + COLUMN_NAME_LOGGED_IN_USER_NUMBER + " = ?";
                String whereArgs[] = {
                        String.valueOf(contact.getId()),
                        NetMeApp.getCurrentUser()
                };
                db.update(TABLE_NAME, values, where, whereArgs);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("RegContactMan", "Failed to update registered contact", e);
        }
    }

    /**
     * Updates a contact's favorite status and registered status on the table to match
     * what the passed in {@link Contact} is set to.
     * @param context             context
     * @param contact             contact object to update with
     * @param registeredUser      {@link Participant} with the user's info
     * */
    public static void updateContact(Context context, Contact contact, Participant registeredUser) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();

        try {
            // adding the values
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_FAVORITE, contact.isFavorite() ? TRUE : FALSE);
            values.put(COLUMN_NAME_REGISTRATION_VALID, contact.isRegistered() ? TRUE : FALSE);
            values.put(COLUMN_NAME_LOGGED_IN_USER_NUMBER, NetMeApp.getCurrentUser());
            values.put(COLUMN_NAME_REGISTERED_USER_ID, registeredUser.getUserId());
            values.put(COLUMN_NAME_REGISTERED_FIRST_NAME, registeredUser.getFirstName());
            values.put(COLUMN_NAME_REGISTERED_LAST_NAME, registeredUser.getLastName());
            values.put(COLUMN_NAME_REGISTERED_AVATAR_URL, registeredUser.getAvatarUrl());
            values.put(COLUMN_NAME_REGISTERED_EMAIL, registeredUser.getEmail());

            // updating the table
            if(registeredUser.getUsername() != null && !registeredUser.getUsername().isEmpty()) {
                String where = COLUMN_NAME_CONTACT_ID + " = ? AND "
                        + COLUMN_NAME_REGISTERED_NUMBER + " = ? AND "
                        + COLUMN_NAME_LOGGED_IN_USER_NUMBER + " = ?";
                String whereArgs[] = {
                        String.valueOf(contact.getId()),
                        PhoneNumberUtil.normalizeDigitsOnly(registeredUser.getUsername()),
                        NetMeApp.getCurrentUser()
                };
                db.update(TABLE_NAME, values, where, whereArgs);
            }
            else {
                String where = COLUMN_NAME_CONTACT_ID + " = ? AND "
                        + COLUMN_NAME_LOGGED_IN_USER_NUMBER + " = ?";
                String whereArgs[] = {
                        String.valueOf(contact.getId()),
                        NetMeApp.getCurrentUser()
                };
                db.update(TABLE_NAME, values, where, whereArgs);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("RegContactMan", "Failed to update registered contact", e);
        }
    }





    /**
     * Returns if a contact is marked as registered or not.
     * @param context    context
     * @param contact    the contact to check
     * @return true if the contact is marked as a valid registered user, false otherwise
     * */
    public static boolean isRegistered(Context context, Contact contact) {
        return isRegistered(context, contact.getId());
    }

    /**
     * Returns if a contact is marked as registered or not.
     * @param context    context
     * @param contactId  the contact's id to check
     * @return true if the contact is marked as a valid registered user, false otherwise
     * */
    public static boolean isRegistered(Context context, long contactId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = null;

        try {
            String selection = COLUMN_NAME_CONTACT_ID + " = ? AND " + COLUMN_NAME_REGISTRATION_VALID + " = ?";
            String selectionArgs[] = {String.valueOf(contactId), TRUE};

            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            return cursor.moveToFirst();
        }
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("RegContactMan", "Failed to check if registered contact", e);
        }
        finally {
            if(cursor != null) cursor.close();
        }

        return false;
    }

    /**
     * Returns if a phone number is marked as registered or not.
     * @param context        context
     * @param phoneNumber    the phone number to check
     * @return true if the contact is marked as a valid registered user, false otherwise
     * */
    public static boolean isRegistered(Context context, long contactId, String phoneNumber) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = null;

        try {
            String number = PhoneNumberUtil.normalizeDigitsOnly(phoneNumber);
            String selection = COLUMN_NAME_REGISTERED_NUMBER + " = ? AND " + COLUMN_NAME_REGISTRATION_VALID + " = ?";
            String selectionArgs[] = {String.valueOf(number), TRUE};

            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            return cursor.moveToFirst();
        }
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("RegContactMan", "Failed to check if registered contact", e);
        }
        finally {
            if(cursor != null) cursor.close();
        }

        return false;
    }

    /**
     * Returns if a valid registered contact is marked as being a favorite
     * @param context    context
     * @param contact    the contact to check
     * @return true is the contact is marked as a favorite, false otherwise
     * */
    public static boolean isFavorite(Context context, Contact contact) {
        return isFavorite(context, contact.getId());
    }

    /**
     * Returns if a valid registered contact is marked as being a favorite
     * @param context    context
     * @param contactId  the contact to check
     * @return true is the contact is marked as a favorite, false otherwise
     * */
    public static boolean isFavorite(Context context, long contactId) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = null;

        try {
            String selection = COLUMN_NAME_CONTACT_ID + " = ? AND "
                    + COLUMN_NAME_FAVORITE + " = ? AND "
                    + COLUMN_NAME_REGISTRATION_VALID  + " = ? AND "
                    + COLUMN_NAME_LOGGED_IN_USER_NUMBER + " = ?";
            String selectionArgs[] = {String.valueOf(contactId), TRUE, TRUE, NetMeApp.getCurrentUser()};

            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            return cursor.moveToFirst();
        }
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("RegContactMan", "Failed to check is favorite contact", e);
        }
        finally {
            if(cursor != null) cursor.close();
        }

        return false;
    }

    /**
     * Returns if a valid registered contact is marked as being a favorite
     * @param context        context
     * @param phoneNumber    the phone number to check if registered
     * @return true is the contact is marked as a favorite, false otherwise
     * */
    public static boolean isFavorite(Context context, long contactId, String phoneNumber) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = null;

        try {
            String selection = COLUMN_NAME_REGISTERED_NUMBER + " = ? AND "
                    + COLUMN_NAME_FAVORITE + " = ? AND "
                    + COLUMN_NAME_REGISTRATION_VALID + " = ? AND "
                    + COLUMN_NAME_LOGGED_IN_USER_NUMBER + " = ?";
            String selectionArgs[] = {PhoneNumberUtil.normalizeDigitsOnly(phoneNumber), TRUE, TRUE, NetMeApp.getCurrentUser()};

            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            return cursor.moveToFirst();
        }
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("RegContactMan", "Failed to check if favorite contact", e);
        }
        finally {
            if(cursor != null) cursor.close();
        }

        return false;
    }





    /**
     * Returns a list of contacts that has been update to match the database's registration and favorite statuses
     * @param context        context
     * @param allContacts    a list of contacts to update
     * @return the updated contact list
     * */
    public static List<Contact> updateContactList(Context context, List<Contact> allContacts) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = null;

        try {
            //Get all the rows of valid registrations
            String selection = COLUMN_NAME_REGISTRATION_VALID + " = ?";
            String selectionArgs[] = {TRUE};
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            //for every row
            while (cursor.moveToNext()) {
                //get the contact id and if they are a favorite
                long contactId = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_CONTACT_ID));
                boolean isFavorite = cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_CONTACT_ID)) == 1;
                String avatarUrl = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REGISTERED_AVATAR_URL));
                String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REGISTERED_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REGISTERED_LAST_NAME));

                //only contact ids are needed to search for a contact
                Contact favContact = new Contact(contactId, null, null, null, null, null, null, true, true);

                //getting the location of contact from the row
                int location = allContacts.indexOf(favContact);
                //if the location is valid, update values
                if(location > -1) {
                    //as per the search criteria, this user can be assumed to be a valid registered user
                    allContacts.get(location).setRegistered(true);
                    allContacts.get(location).setAvatarUrl(avatarUrl);
                    allContacts.get(location).setRegisteredNames(firstName, lastName);

                    //since contacts can have multiple entries and only one be registered,
                    //  this makes sure that they are marked as a favorite if any of their entries
                    //  is a favorite
                    if(isFavorite) allContacts.get(location).setFavorite(true);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("RegContactMan", "Failed to update registered contact list", e);
        }
        finally {
            if(cursor != null) cursor.close();
        }

        return allContacts;
    }

    /** Returns a list of all the registered contact's ids */
    public static List<Long> getRegisteredContactIds(Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = null;
        Set<Long> matchingIds = new HashSet<>();

        try {
            //setting up for the query
            String selection = COLUMN_NAME_REGISTRATION_VALID + " = ?";
            String selectionArgs[] = {TRUE};

            //Get all the matching rows
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            //for every row
            while (cursor.moveToNext()) {
                //get the contact id
                long contactId = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_CONTACT_ID));
                matchingIds.add(contactId);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("RegContactMan", "Failed to get registered contact ids", e);
        }
        finally {
            if(cursor != null) cursor.close();
        }

        return new ArrayList<>(matchingIds);
    }

    /** Returns a list of contact ids of all the contact which have been added to favorites for this user */
    public static List<Long> getFavoriteContactIds(Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = null;
        Set<Long> matchingIds = new HashSet<>();

        try {
            //setting up for the query
            String selection = COLUMN_NAME_FAVORITE + " = ? AND "
                    + COLUMN_NAME_REGISTRATION_VALID  + " = ? AND "
                    + COLUMN_NAME_LOGGED_IN_USER_NUMBER  + " = ?";
            String selectionArgs[] = {TRUE, TRUE, NetMeApp.getCurrentUser()};

            //Get all the matching rows
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            //for every row
            while (cursor.moveToNext()) {
                //get the contact id
                long contactId = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_CONTACT_ID));
                matchingIds.add(contactId);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("RegContactMan", "Failed to get registered contact ids", e);
        }
        finally {
            if(cursor != null) cursor.close();
        }

        return new ArrayList<>(matchingIds);
    }

    /**
     * Returns the number which was set as a favorite for this contact.
     * @param context    context
     * @param id         the contact's id number
     * @return the favorite number is found, null otherwise
     * */
    public static String getFavoriteNumberForContact(Context context, long id) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = null;

        try {
            // check for this contact with a favorite registered number that belongs to the current user
            String selection = COLUMN_NAME_CONTACT_ID + " = ? AND "
                    + COLUMN_NAME_FAVORITE + " = ? AND "
                    + COLUMN_NAME_REGISTRATION_VALID + " = ? AND "
                    + COLUMN_NAME_LOGGED_IN_USER_NUMBER + " = ?";
            String[] selectionArgs = {String.valueOf(id), TRUE, TRUE, NetMeApp.getCurrentUser()};
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            // check to see if there is an entry
            if(cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REGISTERED_NUMBER));
            }
        }
        catch (Exception ignored) { /*ignored*/ }
        finally {
            //making sure to close the cursor
            if(cursor != null) cursor.close();
        }

        // if here, nothing was found, so return null
        return null;
    }

    /**
     * Returns a list of contact which have valid registrations
     * @param context        context
     * @param allContacts    a list of contacts to check
     * @return a contact list of valid registered users
     * */
    public static List<Contact> getRegisteredContacts(Context context, List<Contact> allContacts) {
        String selection = COLUMN_NAME_REGISTRATION_VALID + " = ?";
        String selectionArgs[] = {TRUE};
        return getMatchingContactList(context, allContacts, selection, selectionArgs);
    }

    /**
     * Returns a list of contacts which have valid registrations and have been marked as favorites
     * @param context        context
     * @param allContacts    a list of contacts to check
     * @return a list of valid registered contacts which have been marked as favorites
     * */
    public static List<Contact> getFavoriteContacts(Context context, List<Contact> allContacts) {
        String selection = COLUMN_NAME_FAVORITE + " = ? AND "
                + COLUMN_NAME_REGISTRATION_VALID  + " = ? AND "
                + COLUMN_NAME_LOGGED_IN_USER_NUMBER  + " = ?";
        String selectionArgs[] = {TRUE, TRUE, NetMeApp.getCurrentUser()};
        return getMatchingContactList(context, allContacts, selection, selectionArgs);
    }

    /**
     * Returns a list of contacts which have valid registrations and not been marked as favorites
     * @param context        context
     * @param allContacts    a list of contacts to check
     * @return a list of valid registered contacts which have not been marked as favorites
     * */
    public static List<Contact> getNonFavoriteContacts(Context context, List<Contact> allContacts) {
        String selection = COLUMN_NAME_FAVORITE + " = ? AND "
                + COLUMN_NAME_REGISTRATION_VALID  + " = ? AND "
                + COLUMN_NAME_LOGGED_IN_USER_NUMBER + " = ?";
        String selectionArgs[] = {FALSE, TRUE, NetMeApp.getCurrentUser()};
        return getMatchingContactList(context, allContacts, selection, selectionArgs);
    }

    /**
     * Returns a contact list that matches the set of parameters passed in
     * @param context          context
     * @param allContacts      a list of all the contacts to compare against
     * @param selection        a query selection to look search for
     * @param selectionArgs    an array of parameters to use to the search selection
     * @return a list of matching contacts
     * */
    private static List<Contact> getMatchingContactList(Context context, List<Contact> allContacts, String selection, String[] selectionArgs) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = null;
        Set<Contact> matchingContacts = new HashSet<>();

        try {
            //Get all the matching rows
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            //for every row
            while (cursor.moveToNext()) {
                //get the contact id
                long contactId = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_CONTACT_ID));
                //only contact ids are needed to search for a contact
                Contact favContact = new Contact(contactId, null, null, null, null, null, null, true, true);

                //getting the location of contact from the row
                int location = allContacts.indexOf(favContact);
                if(location > -1) matchingContacts.add(allContacts.get(location));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("RegContactMan", "Failed to get matching contacts", e);
        }
        finally {
            if(cursor != null) cursor.close();
        }

        return new ArrayList<>(matchingContacts);
    }

    /**
     * Returns the valid registered numbers for the contact
     * @param context    context
     * @param contact    the contact
     * @return a list of the registered number strings
     * */
    public static List<String> getRegisteredNumbersForContact (Context context, Contact contact) {
        return getRegisteredNumbersForContact(context, contact.getId());
    }

    /**
     * Returns the valid registered numbers for the contact
     * @param context    context
     * @param id         the id for the contact
     * @return a list of the registered number strings
     * */
    public static List<String> getRegisteredNumbersForContact (Context context, long id) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor = null;
        List<String> numberList = new ArrayList<>();

        try {
            String selection = COLUMN_NAME_CONTACT_ID + " = ? AND " + COLUMN_NAME_REGISTRATION_VALID + " = ?";
            String[] selectionArgs = {String.valueOf(id), TRUE};
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

            while(cursor.moveToNext()) {
                numberList.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME_REGISTERED_NUMBER)));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("RegContactMan", "Failed to get registered contact numbers", e);
        }
        finally {
            if(cursor != null) cursor.close();
        }

        return numberList;
    }
}
