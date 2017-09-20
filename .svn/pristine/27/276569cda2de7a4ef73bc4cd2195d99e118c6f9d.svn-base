package com.hsdi.NetMe.ui.ContactListUtils;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hsdi.NetMe.R;
import com.hsdi.NetMe.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactHolder> {
    public static final String TAG = ContactAdapter.class.getSimpleName();

    private final Context context;
    private final LayoutInflater inflater;
    private final List<Contact> contactList;
    private final SortedList<Contact> filteredContacts;
    private final OnContactClickListener listener;
    private final ContactFilter filter;
    private CharSequence filterText;

    public ContactAdapter(Context context, OnContactClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
        this.contactList = new ArrayList<>();
        this.filteredContacts = new SortedList<>(Contact.class, new SortedList.Callback<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.compareTo(o2);
            }

            @Override
            public void onInserted(int position, int count) {
                notifyDataSetChanged();
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyDataSetChanged();
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyDataSetChanged();
            }

            @Override
            public void onChanged(int position, int count) {
                notifyDataSetChanged();
            }

            @Override
            public boolean areContentsTheSame(Contact oldItem, Contact newItem) {
                return oldItem.contentSameAs(newItem);
            }

            @Override
            public boolean areItemsTheSame(Contact item1, Contact item2) {
                return item1.equals(item2);
            }
        });
        this.filter = new ContactFilter(this);
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_contact, parent, false);
        return new ContactHolder(context, view, listener);
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        holder.bind(filteredContacts.get(position), showIndex(position), showDivider(position));
    }

    @Override
    public int getItemCount() {
        if (filteredContacts == null) return 0;
        return filteredContacts.size();
    }

    public int getTotalCount() {
        if(contactList == null) return 0;
        return contactList.size();
    }





    public List<Contact> getAllContacts() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList.clear();
        if(contactList.size() > 0) this.contactList.addAll(contactList);
        filter(filterText);

        Log.d(TAG, "Pre-filtering counts:" +
                "\ntotal contacts = " + contactList.size() +
                "\nfiltered contacts = " + filteredContacts.size());
    }

    public void setFilteredContacts(List<Contact> filteredList) {
        filteredContacts.clear();
        if(filteredList != null) filteredContacts.addAll(filteredList);
        notifyDataSetChanged();

        Log.d(TAG, "Post-filtering counts:" +
                "\ntotal contacts = " + contactList.size() +
                "\nfiltered contacts = " + filteredContacts.size());
    }

    public void updateContact(Contact contact) {
        //update the main list
        int fullListPosition = contactList.indexOf(contact);
        contactList.set(fullListPosition, contact);

        //update the filtered list
        int filterListPosition = filteredContacts.indexOf(contact);
        filteredContacts.updateItemAt(filterListPosition, contact);

        //notify of the change
        this.notifyItemChanged(filterListPosition);
    }

    public void filter(CharSequence filterText) {
        //always make the filter lower so that later the search can more easily be made case insensitive
        this.filterText = filterText;
        if(filter != null) filter.filter(filterText);
    }

    public void filterRegistered(boolean registeredOnly) {
        filter.setFilterRegisteredOnly(registeredOnly);
        filter(filterText);
    }

    /** determines if the group character index should be shown */
    private boolean showIndex(int position) {
        // if this is the first contact or if the previous item had a different first character, show the index
        return position <= 0 || (filteredContacts.get(position).getName().charAt(0) != filteredContacts.get(position - 1).getName().charAt(0));
    }

    /** Determines if the group divider should be shown */
    private boolean showDivider(int position) {
        // if there is a next contact and it has a different first character, show the divider
        return (position < (filteredContacts.size() - 1)) && (filteredContacts.get(position).getName().charAt(0) != filteredContacts.get(position + 1).getName().charAt(0));
    }
}
