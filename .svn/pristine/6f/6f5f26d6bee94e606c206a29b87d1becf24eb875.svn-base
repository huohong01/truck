package com.hsdi.NetMe.ui.ContactListUtils;

import android.widget.Filter;

import com.hsdi.NetMe.models.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactFilter extends Filter {

    private final ContactAdapter adapter;
    private boolean registeredOnly = false;

    public ContactFilter(ContactAdapter adapter) {
        this.adapter = adapter;
    }

    public void setFilterRegisteredOnly(boolean registeredOnly) {
        this.registeredOnly = registeredOnly;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();

        //if the filter is empty, just show the entire list
        if ((constraint == null || constraint.length() == 0) && !registeredOnly) {
            filterResults.values = new ArrayList<>(adapter.getAllContacts());
            filterResults.count = adapter.getTotalCount();
        }
        //if there is a value to filter by, go through every item to look for matches
        else {
            List<Contact> list = new ArrayList<>();
            String constraintText = "";
            if(constraint != null) constraintText = constraint.toString().toLowerCase();

            for (Contact contact : adapter.getAllContacts()) {
                if(registeredOnly && !contact.isRegistered()) continue;

                //get the name and make sure it is lower case, the initial empty string is to make sure name isn't a null string
                String name = "" + contact.getName().toLowerCase(Locale.getDefault());

                //get an array of phones if it is not null or empty
                String phoneArray = "";
                if(contact.getPhones() != null && !contact.getPhones().isEmpty()) {
                    phoneArray = contact.getPhones().toString();
                }

                //if the name contains a match add it to the list
                if (name.toLowerCase().contains(constraintText) || phoneArray.toLowerCase().contains(constraintText)) {
                    list.add(contact);
                }
            }

            //setting the results
            filterResults.values = list;
            filterResults.count = list.size();
        }

        return filterResults;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.setFilteredContacts((List<Contact>) results.values);
    }
}
