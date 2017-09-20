package com.hsdi.NetMe.ui.main.favorites;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hsdi.NetMe.R;
import com.hsdi.NetMe.models.Contact;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesHolder> {
    public static final String TAG = FavoritesAdapter.class.getSimpleName();

    private final Context context;
    private final LayoutInflater inflater;
    private final List<Contact> favoritesList;
    private final SortedList<Contact> filteredFavorites;
    private final OnFavoritesSelectedListener onFavoritesSelectedListener;
    private final FavoritesFilter filter;
    private CharSequence filterText;

    public FavoritesAdapter(Context context, OnFavoritesSelectedListener onItemSelected) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.favoritesList = new ArrayList<>();
        this.filteredFavorites = new SortedList<>(Contact.class, new SortedList.Callback<Contact>() {
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
        this.onFavoritesSelectedListener = onItemSelected;
        this.filter = new FavoritesFilter(this);
    }

    @Override
    public FavoritesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = inflater.inflate(R.layout.item_favorite, parent, false);
        return new FavoritesHolder(context, viewItem, onFavoritesSelectedListener);
    }

    @Override
    public void onBindViewHolder(FavoritesHolder holder, int position) {
        holder.bind(filteredFavorites.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredFavorites.size();
    }

    public int getTotalCount() {
        return favoritesList.size();
    }





    public List<Contact> getAllFavorites() {
        return favoritesList;
    }

    public void addFavorite(Contact contact) {
        this.favoritesList.add(contact);
        this.filter.filter(filterText);
    }

    public void setFavoritesList(List<Contact> favoritesList) {
        this.favoritesList.clear();
        this.favoritesList.addAll(favoritesList);
        this.filter.filter(filterText);
    }

    public void setFilteredFavorites(List<Contact> filteredFavorites) {
        this.filteredFavorites.clear();
        this.filteredFavorites.addAll(filteredFavorites);
        notifyDataSetChanged();
    }

    public void updateContact(Contact contact) {
        //update the main list
        int fullListPosition = favoritesList.indexOf(contact);
        favoritesList.set(fullListPosition, contact);

        //update the filtered list
        int filterListPosition = filteredFavorites.indexOf(contact);
        filteredFavorites.updateItemAt(filterListPosition, contact);

        //notify of the change
        this.notifyItemChanged(filterListPosition);
    }

    public void filter(CharSequence filterText) {
        this.filterText = filterText;

        this.filter.filter(filterText);
    }

    public void remove(Contact contact) {
        //get the position from the visible list
        int itemPosition = filteredFavorites.indexOf(contact);

        //remove the items from both lists
        filteredFavorites.remove(contact);
        favoritesList.remove(contact);

        //notify the item removed
        this.notifyItemRemoved(itemPosition);
    }
}
