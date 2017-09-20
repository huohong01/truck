package com.hsdi.NetMe.ui.main.message_logs;

import android.widget.Filter;

import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.models.Chat;
import com.hsdi.NetMe.models.Contact;
import com.hsdi.NetMe.models.Participant;

import java.util.ArrayList;
import java.util.List;

public class MessageLogFilter extends Filter {

    private final MessageLogAdapter adapter;

    public MessageLogFilter(MessageLogAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();

        //if the filter is empty, just show the entire list
        if (constraint == null || constraint.length() == 0) {
            filterResults.values = new ArrayList<>(adapter.getAllChats());
            filterResults.count = adapter.getTotalCount();
        }
        //if there is a value to filter by, go through every chat and participant to look for matches
        else {
            List<Chat> list = new ArrayList<>();

            //for every chat
            for (Chat chat : adapter.getAllChats()) {
                //for every participant in the chat
                for(Participant participant : chat.getParticipants()) {
                    // Ignore if current user
                    if(participant.isCurrentUser()) continue;

                    // get the name from the participant object
                    String pName = participant.getFirstName() + " " + participant.getLastName();
                    String constraintText = constraint.toString().toLowerCase();

                    // The contacts name contains a matching string. Add it to the list
                    Contact contact = NetMeApp.getContactWith(participant.getUsername());
                    if(contact != null && contact.getName() != null) {
                        if(contact.getName().toLowerCase().contains(constraintText)) {
                            list.add(chat);
                        }
                        break;
                    }
                    // if the participant name contains the searched text
                    else if (pName.toLowerCase().contains(constraintText)) {
                        list.add(chat);
                        break;
                    }
                    // if the participant username contains the searched text
                    else if (participant.getUsername().contains(constraintText)){
                        list.add(chat);
                        break;
                    }
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
        adapter.setFilteredChats((List<Chat>) results.values);
    }
}
