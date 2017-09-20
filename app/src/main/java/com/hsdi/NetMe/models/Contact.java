package com.hsdi.NetMe.models;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.flurry.android.FlurryAgent;
import com.hsdi.NetMe.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Contact implements Comparable<Contact>, Parcelable {

    private long id             = -1;
    private String lookupKey;

    private String name;
    private String regFirstName;
    private String regLastName;

    private Uri avatarUri;
    private Uri thumbnailUri;
    private String avatarUrl;

    private boolean favorite    = false;
    private boolean registered  = false;

    private List<Phone> phones  = new ArrayList<>();
    private List<Email> emails  = new ArrayList<>();


//--------------------------------------------------------------------------------------------------


    /**
     * This should only be used when search a contact list with an id
     * */
    public Contact(long id) {
        this.id = id;
        this.lookupKey = null;
        this.name = null;
        this.phones = null;
        this.emails = null;
        this.avatarUri = null;
        this.thumbnailUri = null;
        this.avatarUrl = null;
        this.favorite = false;
        this.registered = false;
    }

    public Contact(long id, String lookupKey, String name, List<Phone> phones, List<Email> emails, Uri avatarUri, Uri thumbnailUri, boolean favorite, boolean registered) {
        this.id = id;
        this.lookupKey = lookupKey;
        this.name = name;
        this.phones = phones;
        this.emails = emails;
        this.avatarUri = avatarUri;
        this.thumbnailUri = thumbnailUri;
        this.avatarUrl = null;
        this.favorite = favorite;
        this.registered = registered;
    }

    public Contact(Contact contact) {
        if (contact != null) {
            this.id = contact.id;
            this.lookupKey = contact.lookupKey;
            this.name = contact.name;
            this.phones = contact.phones;
            this.emails = contact.emails;
            this.avatarUri = contact.avatarUri;
            this.thumbnailUri = contact.thumbnailUri;
            this.avatarUrl = null;
            this.favorite = contact.favorite;
            this.registered = contact.registered;
        }
    }


//--------------------------------------------------------------------------------------------------


    /** Returns the id from the device's database */
    public long getId() {
        return id;
    }

    /** Updates the id */
    public void setId(long id) {
        this.id = id;
    }

    /** Returns the lookup key */
    public String getLookupKey() {
        return lookupKey;
    }

    /** Returns the whole name obtained from the device */
    public String getName() {
        return name;
    }

    /** Returns the first name from the device */
    public String getFirstName() {
        try {
            if (name == null || name.isEmpty()) return "";
            String[] split = name.split(" ");
            return split[0];
        }
        catch (Exception e) {
            FlurryAgent.onError("Contact object", "Failed to get first name", e);
            return name;
        }
    }

    /**
     * Returns the phone numbers
     * @return a list of phone objects
     * */
    public List<Phone> getPhones() {
        return phones;
    }

    /**
     * Sets the list of phone numbers
     * @param phones    the new list of phone numbers
     * */
    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    /**
     * Returns emails
     * @return a list of email objects
     * */
    public List<Email> getEmails() {
        return emails;
    }

    /** Returns the Uri for the full size avatar from the device */
    public Uri getAvatarUri() {
        return avatarUri;
    }

    /** Returns the Uri for the avatar thumbnail size avatar from the device */
    public Uri getThumbnailUri() {
        return thumbnailUri;
    }

    /** Returns the string url for the avatar from the NetMe Backend */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /** Updates the backend string avatar url locally */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * Checks that the backend string avatar url is valid.
     * To be valid, it can not be empty or be the default backend avatar url.
     * @param context    context
     * @return {@code TRUE} if the avatar is valid, {@code FALSE} otherwise
     * */
    public boolean hasValidAvatarUrl(Context context) {
        return avatarUrl != null && !avatarUrl.isEmpty() && !avatarUrl.contains(context.getString(R.string.default_avatar));
    }

    /** Returns {@code TRUE} if marked as a favorite, {@code FALSE} otherwise */
    public boolean isFavorite() {
        return favorite;
    }

    /** Updates the favorite status for this contact */
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    /** Returns {@code TRUE} if marked as registered, {@code FALSE} otherwise */
    public boolean isRegistered() {
        return registered;
    }

    /** Updates the registered status for this contact */
    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    /** Returns the registered first name; the first name from the backend */
    public String getRegisteredFirstName() {
        return regFirstName;
    }

    /** Returns the registered last name; the last name from the backend */
    public String getRegisteredLastName() {
        return regLastName;
    }

    /** Returns the registered first and last name combinations from the backend */
    public String getRegisteredName() {
        return regFirstName + regLastName;
    }

    /**
     * Updates both first and last names from the backend locally.
     * @param firstName    the first name from the backend
     * @param lastName     the last name from the backend
     * */
    public void setRegisteredNames(String firstName, String lastName) {
        regFirstName = firstName;
        regLastName = lastName;
    }

    /**
     * Returns the contact's initials using the first and last words of the name
     * @return the initials for the contact
     * */
    public String getInitials() {
        // split first, middle, last, etc. names
        String[] nameParts = name.split(" ");

        // try to get the initials from the registered names if possible
        if(!TextUtils.isEmpty(regFirstName) && !TextUtils.isEmpty(regLastName)) {
            return regFirstName.substring(0,1).toUpperCase(Locale.getDefault())
                    + regLastName.substring(0,1).toUpperCase(Locale.getDefault());
        }
        else if(nameParts.length > 1){
            return nameParts[0].substring(0,1).toUpperCase(Locale.getDefault())
                    + nameParts[nameParts.length - 1].substring(0,1).toUpperCase(Locale.getDefault());
        }
        // only one name, so just get that initial
        else return name.substring(0,1).toUpperCase(Locale.getDefault());
    }


//--------------------------------------------------------------------------------------------------


    @Override
    public int compareTo(@NonNull Contact another) {
        if(name == null || another.getName() == null) return 0;
        return name.toLowerCase().compareTo(another.name.toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof Contact && id == ((Contact) o).id;
    }

    /**
     * Checks if this and the other contact have the same content
     * @param contact    the contact to compare to this
     * @return true if the content is the same, false otherwise
     * */
    public boolean contentSameAs(Contact contact) {
        return this == contact ||
                !(contact == null ||
                        getClass() != contact.getClass()) &&
                        id == contact.id &&
                        favorite == contact.favorite &&
                        registered == contact.registered &&
                        (name != null ? name.equals(contact.name) : contact.name == null &&
                                (regFirstName != null ? regFirstName.equals(contact.regFirstName) : contact.regFirstName == null &&
                                        (regLastName != null ? regLastName.equals(contact.regLastName) : contact.regLastName == null &&
                                                (avatarUri != null ? avatarUri.equals(contact.avatarUri) : contact.avatarUri == null &&
                                                        (thumbnailUri != null ? thumbnailUri.equals(contact.thumbnailUri) : contact.thumbnailUri == null &&
                                                                (avatarUrl != null ? avatarUrl.equals(contact.avatarUrl) : contact.avatarUrl == null &&
                                                                        phones.equals(contact.phones) &&
                                                                        (emails != null ? emails.equals(contact.emails) : contact.emails == null)))))));

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            Bundle bundle = source.readBundle(getClass().getClassLoader());

            bundle.setClassLoader(Contact.class.getClassLoader());
            long id = bundle.getLong("id");
            String lookupKey = bundle.getString("lookupKey");
            String name = bundle.getString("name");
            Uri avatarUri = bundle.getParcelable("avatarUri");
            Uri thumbnailUri = bundle.getParcelable("thumbnailUri");
            boolean favorite = bundle.getBoolean("favorite");
            boolean registered = bundle.getBoolean("registered");

            bundle.setClassLoader(Email.class.getClassLoader());
            List<Email> emails = bundle.getParcelableArrayList("emails");

            bundle.setClassLoader(Phone.class.getClassLoader());
            List<Phone> phones = bundle.getParcelableArrayList("phones");

            return new Contact(
                    id,
                    lookupKey,
                    name,
                    phones,
                    emails,
                    avatarUri,
                    thumbnailUri,
                    favorite,
                    registered
            );
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();

        bundle.putLong("id", id);
        bundle.putString("lookupKey", lookupKey);
        bundle.putString("name", name);
        bundle.putParcelable("avatarUri", avatarUri);
        bundle.putParcelable("thumbnailUri", thumbnailUri);
        bundle.putString("avatarUrl", avatarUrl);

        bundle.putParcelableArrayList("emails", validateList(emails));
        bundle.putParcelableArrayList("phones", validateList(phones));

        bundle.putBoolean("favorite", favorite);
        bundle.putBoolean("registered", registered);

        dest.writeBundle(bundle);
    }

    @Override
    public String toString() {
        return "Contact{" + "id=" + id +
                ", lookupKey='" + lookupKey + '\'' +
                ", name='" + name + '\'' +
                ", avatarUri=" + avatarUri +
                ", thumbnailUri=" + thumbnailUri +
                ", favorite=" + favorite +
                ", registered=" + registered +
                ", phones=" + phones +
                ", emails=" + emails +
                '}';
    }

    @SuppressWarnings("unchecked")
    private ArrayList<? extends Parcelable> validateList(List<? extends Parcelable> list) {
        if (list == null) list = new ArrayList<>();
        if ( !(list instanceof ArrayList)) list = new ArrayList<>(list);
        return (ArrayList) list;
    }
}