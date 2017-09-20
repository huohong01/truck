package com.hsdi.NetMe.ui.main.recent_logs;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.models.Meeting;

import java.util.ArrayList;
import java.util.List;

public class RecentAdapter extends RecyclerView.Adapter<RecentHolder> {
    private static final String TAG = "RecentAdapter";

    private final RecentFragment recentFragment;
    private final LayoutInflater inflater;
    private final List<Meeting> meetingList;
    private final SortedList<Meeting> filteredMeetingList;
    private final RecentFilter filter;
    private CharSequence filterText;
    private final ViewBinderHelper viewBinderHelper;

    public RecentAdapter(RecentFragment recentFragment) {
        this.recentFragment = recentFragment;
        this.inflater = LayoutInflater.from(recentFragment.getActivity());
        this.meetingList = new ArrayList<>();
        this.filteredMeetingList = new SortedList<>(Meeting.class, new SortedList.Callback<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {
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
                notifyItemChanged(position);
            }

            @Override
            public boolean areContentsTheSame(Meeting oldItem, Meeting newItem) {
                return oldItem.compareTo(newItem) != 0;
            }

            @Override
            public boolean areItemsTheSame(Meeting item1, Meeting item2) {
                return item1.getMeetingId() == item2.getMeetingId();
            }
        });
        this.filter = new RecentFilter(this);
        this.viewBinderHelper = new ViewBinderHelper();
    }

    @Override
    public RecentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SwipeRevealLayout itemView = (SwipeRevealLayout) inflater.inflate(R.layout.item_log_meeting, parent, false);
        return new RecentHolder(itemView, recentFragment);
    }

    @Override
    public void onBindViewHolder(RecentHolder holder, int position) {
        Meeting meeting = filteredMeetingList.get(position);

        // Save/restore the open/close state with the meeting's id
        viewBinderHelper.bind(holder.swipeLayout, String.valueOf(meeting.getMeetingId()));

        // bind the new info to the views
        holder.bind(meeting);
    }

    @Override
    public int getItemCount() {
        return filteredMeetingList.size();
    }

    public int getTotalCount() {
        return meetingList.size();
    }


    public void addAll(List<Meeting> meetings) {
        //only add new meetings to the list
        for(Meeting newMeeting : meetings) {
            if(meetingList.contains(newMeeting)) {
                int position = meetingList.indexOf(newMeeting);
                meetingList.set(position, newMeeting);
            }
            else meetingList.add(newMeeting);
        }

        //apply any filters to the new meetings
        filter.filter(filterText);

        Log.d(TAG, "Pre-filtering counts:" +
                "\ntotal contacts = " + meetingList.size() +
                "\nfiltered contacts = " + filteredMeetingList.size());
    }

    public List<Meeting> getAllRecent() {
        return meetingList;
    }

    public void setFilteredRecent(List<Meeting> filteredMeetings) {
        this.filteredMeetingList.clear();
        if(filteredMeetings != null) this.filteredMeetingList.addAll(filteredMeetings);

        Log.d(TAG, "Post-filtering counts:" +
                "\ntotal contacts = " + meetingList.size() +
                "\nfiltered contacts = " + filteredMeetingList.size());
    }

    public void filter(CharSequence filterText) {
        this.filterText = filterText;
        if(filter != null) filter.filter(filterText);
    }

    public void filter(@RecentFragment.FilterType int type) {
        filter.setFilterType(type);
        filter(filterText);
    }

    public void remove(Meeting meeting) {
        // get position from the visible list
        int position = filteredMeetingList.indexOf(meeting);

        //remove the item from both lists
        meetingList.remove(meeting);
        filteredMeetingList.remove(meeting);

        //notify the item removed
        notifyItemRemoved(position);
    }
}
