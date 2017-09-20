package com.hsdi.NetMe.ui.main.recent_logs;

import android.widget.Filter;

import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.models.Contact;
import com.hsdi.NetMe.models.Meeting;
import com.hsdi.NetMe.models.Participant;

import java.util.ArrayList;
import java.util.List;

public class RecentFilter extends Filter {

    private final RecentAdapter adapter;
    @RecentFragment.FilterType
    private int filterType = RecentFragment.FILTER_ALL;

    public RecentFilter(RecentAdapter adapter) {
        this.adapter = adapter;
    }

    public void setFilterType(@RecentFragment.FilterType int type) {
        filterType = type;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        boolean searchParticipants = constraint != null && !constraint.toString().trim().isEmpty();
        boolean searchByType = filterType != RecentFragment.FILTER_ALL;

        // Return the entire list when filters are empty and set to show all
        if (!searchParticipants && !searchByType) {
            filterResults.values = new ArrayList<>(adapter.getAllRecent());
            filterResults.count = adapter.getTotalCount();
        }
        // There is a value to filter by. Go through every chat and participant to look for matches
        else {
            List<Meeting> list = new ArrayList<>();

            // For every chat
            for (Meeting meeting : adapter.getAllRecent()) {
                // Ignore this meeting if it is not the correct type
                if(filterType == RecentFragment.FILTER_ALL) {/*ignore*/}
                else if(filterType == RecentFragment.FILTER_MISSED && !meeting.isMissedStatus()) {
                    continue;
                }
                else if(filterType == RecentFragment.FILTER_OUTGOING && !meeting.isSentStatus()) {
                    continue;
                }
                else if(filterType == RecentFragment.FILTER_INCOMING && !meeting.isReceivedStatus()) {
                    continue;
                }

                // Searching the participants name
                if(searchParticipants) {
                    // For every participant in the chat
                    for (Participant participant : meeting.getParticipants()) {
                        // Ignore if current user
                        if(participant.isCurrentUser()) continue;

                        String pName = participant.getFirstName() + " " + participant.getLastName();
                        String constraintText = constraint.toString().toLowerCase();

                        // The contacts name contains a matching string. Add it to the list
                        Contact contact = NetMeApp.getContactWith(participant.getUsername());
                        if(contact != null && contact.getName() != null) {
                            if(contact.getName().toLowerCase().contains(constraintText)) {
                                list.add(meeting);
                            }
                            break;
                        }
                        else if (pName.toLowerCase().contains(constraintText)) {
                            list.add(meeting);
                            break;
                        }
                    }
                }
                // Not filtering by participant
                else list.add(meeting);
            }

            // Setting the results
            filterResults.values = list;
            filterResults.count = list.size();
        }

        return filterResults;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        adapter.setFilteredRecent((List<Meeting>) filterResults.values);
    }
}
