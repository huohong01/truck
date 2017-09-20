package com.hsdi.NetMe.ui.contact_selection;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hsdi.NetMe.R;
import com.hsdi.NetMe.models.Contact;
import com.hsdi.NetMe.util.PhoneUtils;
import com.hsdi.NetMe.util.SelectPhoneCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class SelectContactAdapter extends RecyclerView.Adapter<SelectContactHolder> implements View.OnClickListener {

    private final LayoutInflater inflater;
    private final List<Contact> contactList;
    private final SortedList<Contact> filteredContactList;
    private final Map<Long, String> selected;
    private final SelectContactFilter filter;
    private String filterText;


    SelectContactAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.contactList = new ArrayList<>();
        this.filteredContactList = new SortedList<>(Contact.class, new SortedList.Callback<Contact>() {
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
        this.selected = new HashMap<>();
        this.filter = new SelectContactFilter(this);
    }

    @Override
    public SelectContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_startchat, parent, false);
        return new SelectContactHolder(view, this);
    }

    @Override
    public void onBindViewHolder(final SelectContactHolder holder, int position) {
        final Contact contact = filteredContactList.get(position);
        holder.bind(contact);
        holder.setChecked(selected.containsKey(contact.getId()));
    }

    @Override
    public int getItemCount() {
        return filteredContactList.size();
    }

    @Override
    public void onClick(View v) {
        final SelectContactHolder holder = (SelectContactHolder) v.getTag();
        final Contact contact = holder.getContact();
        boolean isSelected = selected.containsKey(contact.getId());

        if(isSelected) {
            holder.setChecked(false);
            selected.remove(contact.getId());
        }
        else {
            PhoneUtils.selectPhoneNumber(
                    v.getContext(),
                    contact,
                    new SelectPhoneCallback() {
                        @Override
                        public void selectedNumberIs(Contact contact, String phoneNumber) {
                            //if a valid phone was selected, add that phone to the mapping for this contact
                            if(phoneNumber != null) {
                                holder.setChecked(true);
                                selected.put(
                                        contact.getId(),
                                        phoneNumber
                                );
                            }
                        }
                    }
            );
        }
    }

    public int getTotalCount() {
        return contactList.size();
    }





    List<Contact> getAllContacts() {
        return contactList;
    }

    public void setContacts(List<Contact> contactList) {
        this.contactList.clear();
        this.contactList.addAll(contactList);
        filter(filterText);
    }

    void setFilteredContacts(List<Contact> filteredContacts) {
        this.filteredContactList.clear();
        this.filteredContactList.addAll(filteredContacts);
        notifyDataSetChanged();
    }

    List<String> getSelectedNumbers() {
        List<String> selectedNumbers = new ArrayList<>();

        //populating the list with the selected numbers
        for(Long id : selected.keySet()) selectedNumbers.add(selected.get(id));

        return selectedNumbers;
    }

    public List<Contact> getSelectedContacts() {
        List<Contact> selectedContacts = new ArrayList<>();

        //going through every contact in the original list and finding which one have been selected
        for(Contact contact : contactList) {
            if(selected.containsKey(contact.getId())) selectedContacts.add(contact);
        }

        return selectedContacts;
    }

    public void filter(String filterText) {
        this.filterText = filterText;

        if(filter != null) filter.filter(filterText);
    }
}
