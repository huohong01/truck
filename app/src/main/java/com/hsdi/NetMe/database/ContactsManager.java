package com.hsdi.NetMe.database;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.flurry.android.FlurryAgent;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.models.Contact;
import com.hsdi.NetMe.models.Email;
import com.hsdi.NetMe.models.Phone;
import com.hsdi.NetMe.util.PreferenceManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ContactsManager  {

    /**
     * Get contactId from phoneNumber
     * @param phoneNumber phone number of the contact to find
     * @return -1 if fail
     */
    public static long getContactId(Context context, String phoneNumber) {
        // setup the id with a default invalid '-1' value
        long id = -1;

        // don't bother continuing if the provided phone number is invalid and just return the invalid id
        if (phoneNumber == null || phoneNumber.isEmpty()) return id;

        Cursor cursor = null;
        try {
            // get the uri to search through
            Uri uri = Uri.withAppendedPath(
                    ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                    Uri.encode(phoneNumber)
            );
            // get the contact's id
            String[] projection = { ContactsContract.PhoneLookup._ID };

            // perform the search
            ContentResolver contentResolver = context.getContentResolver();
            cursor = contentResolver.query(
                    uri,
                    projection,
                    null, null, null
            );

            // if something was found, set the id to the found value
            if (cursor != null && cursor.moveToNext()) id = cursor.getLong(0);
            // if nothing was found try adjusting the phone number
            else {
                // creating a single instance of other stuff to reduce memory usage
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                Phonenumber.PhoneNumber phoneNum = new Phonenumber.PhoneNumber();
                String defaultRegion = PreferenceManager.getInstance(context).getCountryCode();

                // try to parse the number and make sure if is a valid number
                phoneUtil.parse(phoneNumber, defaultRegion, phoneNum);

                // make the number into a string
                String numString = phoneNum.getCountryCode() + "" + phoneNum.getNationalNumber();

                id = getContactId(context, numString);
            }
        }
        // print any error the log
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("ContactsManager", "Failed to load contact id", e);
        }
        finally {
            // making sure to close the cursor to avoid memory leaks
            if (cursor != null) cursor.close();
        }

        // return the id
        return id;
    }

    /**
     * Get Contact from phone number
     * @param phoneNumber the phone number of the contact to be found
     * @return null if fail
     */
    public static Contact getContact(Context context, String phoneNumber) {
        // return the contact after finding their id from the phone number
        return getContact(context, getContactId(context, phoneNumber));
    }

    /**
     * Get Contact from contactId
     * @param id the device contact id for the contact to be found
     * @return null if fail
     */
    public static Contact getContact(Context context, long id) {
        // don't bother continuing if the id is invalid
        if (id < 0) return null;

        Contact contact = null;
        Cursor c = null;
        try {
            // get the contact's id, name, and avatar photo
            String[] projection = {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.PHOTO_URI,
                    ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
            };

            //perform the search
            ContentResolver contentResolver = context.getContentResolver();
            c = contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    projection,
                    ContactsContract.Contacts._ID + "= ?",
                    new String[] {Long.toString(id)},
                    null
            );

            // setting some of the info for the contact to return
            if(c != null && c.moveToFirst()) {
                // get all the user's information
                String lookUpKey = c.getString(c.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                String displayName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String photoUriText = c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                String thumbnailUriText = c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
                Uri photoUri = (photoUriText == null) ? null : Uri.parse(photoUriText);
                Uri thumbnailUri = (thumbnailUriText == null) ? null : Uri.parse(thumbnailUriText);

                // get the contact's emails, phones, favorite status, and registered status
                List<Email> emails = getContactEmails(context, Long.toString(id));
                List<Phone> phones = getContactPhones(context, Long.toString(id));
                boolean favorite = RegisteredContactManager.isFavorite(context, id);
                boolean registered = RegisteredContactManager.isRegistered(context, id);

                // creating the contact to return
                contact = new Contact(
                        id,
                        lookUpKey,
                        displayName,
                        phones,
                        emails,
                        photoUri,
                        thumbnailUri,
                        favorite,
                        registered
                );
            }
        }
        // printing any errors to the log
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("ContactsManager", "Failed to load contact", e);
        }
        finally {
            // making sure to close the cursor to avoid memory leaks
            if (c != null) c.close();
        }

        // returning the contact
        return contact;
    }

    /**
     * Get thumbnail Uri from a contactId
     * @return null if not found or phoneNumber is null
     */
    public static Uri getThumbnailUri(Context context, String phoneNumber) {
        // get the contact's id from the phone number
        long contactId = getContactId(context, phoneNumber);

        // return the thumbnail uri
        return getThumbnailUri(context, contactId);
    }

    /**
     * Get thumbnail Uri from a contactId
     * @return null if not found or contactId < 0
     */
    public static Uri getThumbnailUri(Context context, long contactId) {
        // don't bother doing anything is the id is invalid
        if (contactId < 0) return null;

        Uri thumbnailUri = null;
        Cursor c = null;
        try {
            // get the thumbnail uri
            String[] projection = { ContactsContract.Contacts.PHOTO_THUMBNAIL_URI };

            // filter by this contact
            String selection = ContactsContract.Contacts._ID + "=?";
            String[] selectionArgs = { contactId + "" };

            // perform the search
            ContentResolver resolver = context.getContentResolver();
            c = resolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    null
            );

            // if there is something, get the uri
            if (c != null && c.moveToNext()) {
                String uriText = c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
                thumbnailUri = (uriText == null)? null : Uri.parse(uriText);
            }


        }
        // print any errors to the logs
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("ContactsManager", "Failed to load thumbnail uri", e);
        }
        finally {
            // making sure to close the cursor to avoid memory leaks
            if (c != null) c.close();
        }

        return thumbnailUri;
    }

    /**
     * Get display name from a phone number
     * @return null if fail.
     */
    public static String getDisplayName(Context context, String phoneNumber) {
        // find the contact's id from their phone number
        long contactId = getContactId(context, phoneNumber);

        // return the name
        return getDisplayName(context, contactId);
    }

    /**
     * Get display name from contactId
     * @return null if fail.
     */
    public static String getDisplayName(Context context, long contactId) {
        // don't even bother doing anything is the id is invalid
        if (contactId < 0) return null;

        String displayName = null;
        Cursor c = null;
        try {
            // get the contact name
            String[] projection = { ContactsContract.Contacts.DISPLAY_NAME };

            // filter for this contact
            String selection = ContactsContract.Contacts._ID + "=?";
            String[] selectionArgs = { contactId + "" };

            // perform the search
            ContentResolver resolver = context.getContentResolver();
            c = resolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    null
            );

            // get the contact's name
            if (c != null && c.moveToNext()) {
                displayName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            }


        }
        // print any errors to the logs
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("ContactsManager", "Failed to load contact name", e);
        }
        finally {
            // making sure to close the cursor to avoid memory leaks
            if (c != null) c.close();
        }

        return displayName;
    }

    /**
     * Get List of all visible Contact
     * @return empty list if fail
     */
    public static List<Contact> getAllContacts(Context context) {
        // setting up the result list, if nothing is found, empty list will be returned
        List<Contact> contacts = new ArrayList<>();

        // started the search
        Cursor cursor = null;
        try {
            // get the contact's id, name and photo
            String[] projection = {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts.PHOTO_URI,
                    ContactsContract.Contacts.PHOTO_THUMBNAIL_URI
            };
            // sort it by name, this is done to maybe do paging later if contact loading needs to be speedup
            String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;

            // perform the search
            ContentResolver contentResolver = context.getContentResolver();
            cursor = contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    projection,
                    null,
                    null,
                    sortOrder
            );

            // Get emails and phone number maps as well as the favorites and registered lists
            // Doing this out here once, reduces the total time instead of doing individual checks later
            Map<Long, List<Email>> mapEmails = getEmailMap(context);
            Map<Long, List<Phone>> mapNumbers = getNumberMap(context);
            List<Long> favoriteList = RegisteredContactManager.getFavoriteContactIds(context);
            List<Long> registeredList = RegisteredContactManager.getRegisteredContactIds(context);

            if (cursor != null && cursor.getCount() > 0) {
                // for every entry in the contact table
                while (cursor.moveToNext()) {
                    // getting the user info
                    Long id = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String lookUpKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String photoUriText = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                    String thumbnailUriText = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
                    Uri photoUri = (photoUriText == null) ? null : Uri.parse(photoUriText);
                    Uri thumbnailUri = (thumbnailUriText == null) ? null : Uri.parse(thumbnailUriText);

                    // getting the email, phones, favorite status, and registered status
                    List<Email> contactEmails = mapEmails.get(id);
                    List<Phone> contactPhones = mapNumbers.get(id);
                    boolean favorite = favoriteList.contains(id);
                    boolean registered = registeredList.contains(id);

                    // only create this contact if it has a phone number
                    if(contactPhones != null && !contactPhones.isEmpty()){
                        // create the contact
                        Contact contact = new Contact(
                                id,
                                lookUpKey,
                                displayName,
                                contactPhones,
                                (contactEmails == null)? new ArrayList<Email>() : contactEmails,
                                photoUri,
                                thumbnailUri,
                                favorite,
                                registered
                        );

                        // add the contact to the list
                        contacts.add(contact);
                    }
                }
            }
        }
        catch (Exception e) {
            // print error to the log
            e.printStackTrace();
            FlurryAgent.onError("ContactsManager", "Failed to load all contacts", e);
        }
        finally {
            // always remember to close the cursor to avoid memory leaks
            if (cursor != null) cursor.close();
        }

        // return the contact list
        return contacts;
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Open InputStream from thumbnail-size avatar uri
     * @param context context
     * @param thumbnailUri the uri of the thumbnail
     * @return null if there is no thumbnail avatar
     */
    public static InputStream openThumbnailStream(Context context, Uri thumbnailUri) {
        if (thumbnailUri == null) return null;

        // performing the search for the the user avatar
        Cursor cursor = context.getContentResolver().query(
                thumbnailUri,
                new String[] {ContactsContract.Contacts.Photo.PHOTO},
                null,
                null,
                null
        );

        try {
            // only continue if there is some result
            if (cursor != null && cursor.moveToFirst()) {
                // get the avatar data
                byte[] data = cursor.getBlob(0);

                // return the binary data stream
                if (data != null) return new ByteArrayInputStream(data);
            }
        }
        finally {
            // making sure to close the cursor to avoid memory leaks
            if(cursor != null) cursor.close();
        }

        // if something failed or nothing was found
        return null;
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Get emails from contactId
     * @param context context
     * @param contactId the contact id for which to find a list of emails for
     * @return Empty list if fail or non-exist
     */
    private static List<Email> getContactEmails(Context context, String contactId) {
        // creating return list. will return an empty list if nothing is found
        List<Email> list = new ArrayList<>();


        Cursor c = null;
        try {
            // get the email address and address type
            String[] projection = {
                    ContactsContract.CommonDataKinds.Email.ADDRESS,
                    ContactsContract.CommonDataKinds.Email.TYPE
            };

            // filter for this contact
            String selection = ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?";
            String[] selectionArgs = new String[] {contactId};

            // perform search
            ContentResolver contentResolver = context.getContentResolver();
            c = contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    null
            );

            while (c != null && c.moveToNext()) {
                // adding to the list for this contact
                list.add(getEmailFromCursor(c));
            }
        }
        // print any errors to the logs
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("ContactsManager", "Failed to load emails", e);
        }
        finally {
            // making sure to close the cursor to avoid memory leaks
            if (c != null) c.close();
        }

        // returning the list
        return list;
    }

    /**
     * Get phone numbers from contactId
     * @param contactId the contact it for which to find a list of numbers for
     * @return Empty list if fail or non-exist
     */
    private static List<Phone> getContactPhones(Context context, String contactId) {
        // getting the user's phone number, will return an empty list if nothing is found
        String username = NetMeApp.getCurrentUser();

        // creating return list. will return an empty list if nothing is found
        List<Phone> list = new ArrayList<>();
        Set<String> discoveredNumbers = new HashSet<>();

        // creating a single instance of other stuff to reduce memory usage
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber = new Phonenumber.PhoneNumber();
        String defaultRegion = PreferenceManager.getInstance(context).getCountryCode();

        Cursor c = null;
        try {
            // getting phone number and phone type
            String[] projection = {
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.TYPE
            };
            // filter to this contact
            String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";
            String[] selectionArgs = new String[] {contactId};

            // perform search
            ContentResolver contentResolver = context.getContentResolver();
            c = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    null
            );

            // while there are more phone number, add them to the list
            while (c != null && c.moveToNext()) {
                Phone newPhone = getPhoneFromCursor(c, phoneUtil, defaultRegion, phoneNumber);

                // don't add any phone numbers which have already been found or is the current user's number
                if(newPhone != null && !discoveredNumbers.contains(newPhone.getNumber()) && !newPhone.getPlainNumber().equals(username)) {
                    // adding to the list for this contact
                    list.add(newPhone);
                    // adding to the list of discovered numbers
                    discoveredNumbers.add(newPhone.getNumber());
                }
            }
        }
        // print any error to the logs
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("ContactsManager", "Failed to load contact phones", e);
        }
        finally {
            // always make sure to close the cursor to avoid memory leaks
            if (c != null) c.close();
        }

        // return the result list
        return list;
    }

    /**
     * Gets a mapping of all the contact ids to emails
     * @return a map which maps CONTACT_ID to a List of {@link Email}
     */
    private static Map<Long, List<Email>> getEmailMap(Context context) {
        // creating the result map of emails, will return an empty map if nothing is found
        Map<Long, List<Email>> map = new HashMap<>();

        Cursor c = null;
        try {
            // preparing the stuff for the query
            ContentResolver contentResolver = context.getContentResolver();
            String[] projection = {
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Email.ADDRESS,
                    ContactsContract.CommonDataKinds.Email.TYPE
            };

            // doing query
            c = contentResolver.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null
            );

            // for every query result
            while (c != null && c.moveToNext()) {
                // get the contact id
                Long id = c.getLong(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTACT_ID));

                // if the contact doesn't have a list in the mapping, make one
                if (map.get(id) == null) map.put(id, new ArrayList<Email>());

                // get contact's list from the mapping
                List<Email> list = map.get(id);

                // adding to the list for this contact
                list.add(getEmailFromCursor(c));
            }
        }
        // print any problems encountered to the logs
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("ContactsManager", "Failed to load email map", e);
        }
        // always make sure to close the cursor so it won't leak memory
        finally {
            if (c != null) c.close();
        }

        // return the result map
        return map;
    }

    /**
     * Gets the mapping of all the contacts ids to phone numbers
     * @return a map which maps CONTACT_ID to List of {@link Phone}
     */
    private static Map<Long, List<Phone>> getNumberMap(Context context) {
        // getting the user's phone number, will return an empty list if nothing is found
        String username = NetMeApp.getCurrentUser();

        // creating a single instance of other stuff to reduce memory usage
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber = new Phonenumber.PhoneNumber();
        String defaultRegion = PreferenceManager.getInstance(context).getCountryCode();


        // creating the result map and a set for discovered numbers
        Map<Long, List<Phone>> map = new HashMap<>();
        Set<String> discoveredNumbers = new HashSet<>();

        Cursor c = null;
        try {
            // preparing the stuff for the query
            ContentResolver contentResolver = context.getContentResolver();
            String[] projection = {
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.TYPE
            };

            // doing query
            c = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection,
                    null,
                    null,
                    null
            );

            // for every query result
            while (c != null && c.moveToNext()) {
                //get the contact id
                Long id = c.getLong(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

                // if the contact doesn't have a list in the mapping, make one
                if (map.get(id) == null) map.put(id, new ArrayList<Phone>());

                // get contact's list from the mapping
                List<Phone> list = map.get(id);

                // get the phone from the position where cursor is pointing to
                Phone newPhone = getPhoneFromCursor(c, phoneUtil, defaultRegion, phoneNumber);

                // don't add any phone numbers which have already been found or is the current user's number
                if(newPhone != null && !discoveredNumbers.contains(newPhone.getNumber()) && !newPhone.getPlainNumber().equals(username)) {
                    // adding to the list for this contact
                    list.add(newPhone);
                    // adding to the list of discovered numbers
                    discoveredNumbers.add(newPhone.getNumber());
                }
            }
        }
        // print any problems encountered to the logs
        catch (Exception e) {
            e.printStackTrace();
            FlurryAgent.onError("ContactsManager", "Failed to load phone map", e);
        }
        // always make sure to close the cursor so it won't leak memory
        finally {
            if (c != null) c.close();
        }

        // return the result map
        return map;
    }


//--------------------------------------------------------------------------------------------------


    /**
     * Gets the Phone number objects from the passed cursor
     * @param cursor           the cursor to look in
     * @param phoneUtil        an instance of the phone number util library
     * @param defaultRegion    the region to use as the default
     * @param phoneNumber      an instance of a phone number object to use when parsing
     * @return the phone object containing the phone from the cursor
     * */
    @Nullable
    private static Phone getPhoneFromCursor(Cursor cursor, PhoneNumberUtil phoneUtil, String defaultRegion, Phonenumber.PhoneNumber phoneNumber) {
        // get the phone number at this cursor position
        String number  = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        // get the number type. can be mobile, home, work, etc.
        int type = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
        // convert the type number to a human readable label
        String typeLabel = Resources.getSystem().getString(
                ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(type));

        try {
            // try to parse the number and make sure if is a valid number
            phoneUtil.parse(number, defaultRegion, phoneNumber);

            // make the number into a string
            String numString = phoneNumber.getCountryCode() + "" + phoneNumber.getNationalNumber();

            // return the found number
            return new Phone(numString, type, typeLabel);
        }
        catch (Exception ignored) { /*ignored*/ }

        return null;
    }

    /**
     * Gets the email object from the passed cursor
     * @param cursor    the cursor to look in
     * @return the email object containing the email from the cursor
     * */
    @NonNull
    private static Email getEmailFromCursor(Cursor cursor) {
        // get the phone number at this cursor position
        String address = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
        // get the number type. can be mobile, home, work, etc.
        int type = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
        // convert the type number to a human readable label
        String typeLabel = Resources.getSystem().getString(
                ContactsContract.CommonDataKinds.Email.getTypeLabelResource(type)
        );

        // return the found address
        return new Email(address, type, typeLabel);
    }
}
