package com.hsdi.NetMe.ui.main.favorites;

import android.widget.Filter;

import com.hsdi.NetMe.models.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FavoritesFilter extends Filter {

    private final FavoritesAdapter adapter;

    public FavoritesFilter(FavoritesAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();

        //if the filter is empty, just show the entire list
        if (constraint == null || constraint.length() == 0) {
            filterResults.values = new ArrayList<>(adapter.getAllFavorites());
            filterResults.count = adapter.getTotalCount();
        }
        //if there is a value to filter by, go through every item to look for matches
        else {
            List<Contact> list = new ArrayList<>();
            String constraintText = constraint.toString().toLowerCase();

            for (Contact contact : adapter.getAllFavorites()) {
                //get the name and make sure it is lower case, the initial empty string is to make sure name isn't a null string
                String name = "" + contact.getName().toLowerCase(Locale.getDefault());

                //get an array of phones if it is not null or empty
                String phoneArray = "";
                if(contact.getPhones() != null && !contact.getPhones().isEmpty()) {
                    phoneArray = contact.getPhones().toString();
                }

                //if the name contains a match add it to the list
                if (name.contains(constraintText)) list.add(contact);
                else if(phoneArray.contains(constraintText)) list.add(contact);
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
        adapter.setFilteredFavorites((List<Contact>) results.values);
    }
}
