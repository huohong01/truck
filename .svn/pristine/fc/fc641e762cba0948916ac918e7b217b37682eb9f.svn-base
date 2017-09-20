package com.hsdi.NetMe.ui.contact_selection;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hsdi.NetMe.BaseActivity;
import com.hsdi.NetMe.NetMeApp;
import com.hsdi.NetMe.R;
import com.hsdi.NetMe.database.RegisteredContactManager;
import com.hsdi.NetMe.models.Contact;
import com.hsdi.NetMe.models.Phone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.hsdi.theme.basic.ThemeLayoutInflaterFactory.ThemeTypeValue.menuitem_icon;
import static com.hsdi.theme.basic.ThemeLayoutInflaterFactory.ThemeTypeValue.toolbar_nav_icon;

public class SelectContactsActivity extends BaseActivity {

    // extras
    public static final String EXTRA_CONTACT_TYPE = "contacts";
    public static final String EXTRA_CONTACT_IDS = "contact_ids";
    public static final String EXTRA_CONTACT_NUMBERS = "contact_numbers";

    // result
    public static final String RESULT_CONTACT_NUMBERS = "contact_numbers";

    // contact types
    public static final int ALL = 0;
    public static final int REGISTERED = 1;
    public static final int NONREGISTERED = 2;
    public static final int FAVORITES = 3;
    public static final int NONFAVORITES = 4;

    // Widgets
    @Bind(R.id.select_contact_toolbar)   Toolbar toolbar;
    @Bind(R.id.select_contact_list)      RecyclerView listView;
    @Bind(R.id.select_contact_progress)  View progressbar;

    // Others
    private SelectContactAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);
        ButterKnife.bind(this);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.back);
        applyTheme(toolbar,R.drawable.back,toolbar_nav_icon);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setResult(RESULT_CANCELED);

        if(getIntent().getExtras().containsKey(EXTRA_CONTACT_TYPE)) loadList();
        else if(getIntent().getExtras().containsKey(EXTRA_CONTACT_IDS)) loadContactsByIdList();
        else if(getIntent().getExtras().containsKey(EXTRA_CONTACT_NUMBERS)) loadContactsByNumbersList();
        else finish();
    }

    @Override
    protected void onDestroy() {
        listView = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_contacts, menu);
        setupSearchView(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        applyTheme(menu.findItem(R.id.menu_search),R.drawable.search,menuitem_icon);
        applyTheme(menu.findItem(R.id.menu_done),R.drawable.check,menuitem_icon);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // if the start menu option was clicked, check if everything is valid and try to start a chat
        if(item.getItemId() == R.id.menu_done) {
            // if the list hasn't finished loading or no contact has been selected alert the user
            if (adapter == null || adapter.getSelectedNumbers() == null || adapter.getSelectedNumbers().isEmpty()) {
                Toast.makeText(this, R.string.error_no_selected_contacts, Toast.LENGTH_SHORT).show();
            }
            // start a chat with the selected contacts
            else {
                // make the selected numbers into an array
                List<String> adapterList = adapter.getSelectedNumbers();
                String[] numbersArray = adapterList.toArray(new String[adapterList.size()]);

                // create the intent to be returned
                Intent resultIntent = new Intent();
                resultIntent.putExtra(RESULT_CONTACT_NUMBERS, numbersArray);

                // update the result
                setResult(RESULT_OK, resultIntent);
                finish();
            }

            // action completed successfully
            return true;
        }
        // return super for other items
        else return super.onOptionsItemSelected(item);
    }

    private void setupSearchView(Menu menu) {
        // find the search item
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);

        // get the search item view
        SearchView searchView  = null;
        if (searchItem != null) searchView = (SearchView) searchItem.getActionView();

        // we were able to get the search view apply a text listener to filter as the user types
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //telling the adapter to search for the entered text
                    if (adapter != null)
                        adapter.filter(newText);

                    //action completed successfully
                    return true;
                }
            });
        }
    }

    /** Loads the list of Contacts depending on which type of list is requested */
    private void loadList() {
        // showing the progress bar while loading
        progressbar.setVisibility(View.VISIBLE);

        // create the adapter and apply it to the list
        adapter = new SelectContactAdapter(SelectContactsActivity.this);
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(this));

        switch (getIntent().getIntExtra(EXTRA_CONTACT_TYPE, ALL)) {
            case ALL:
                loadAllContacts();
                break;

            case REGISTERED:
                loadRegisteredList();
                break;

            case NONREGISTERED:
                loadNonRegisteredList();
                break;

            case FAVORITES:
                loadFavoritesList();
                break;

            case NONFAVORITES:
                loadNonFavoritesList();
                break;

            default:
                finish();
        }
    }

    /** Loads all the contacts in a background thread, then adds them to the list */
    private void loadAllContacts() {
        // do the rest in a separate thread
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                // get the registered contacts
                final List<Contact> allContacts = NetMeApp.getContactList();

                // make sure to sort the list
                Collections.sort(allContacts);

                // load the contacts into the list view
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // set contacts
                        if(adapter != null) adapter.setContacts(allContacts);

                        if (allContacts.size() <= 0) {
                            Toast.makeText(
                                    SelectContactsActivity.this,
                                    R.string.no_contacts,
                                    Toast.LENGTH_SHORT
                            ).show();
                            finish();
                        }

                        // progress bar should no longer be visible
                        progressbar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    /**
     * Loads all the contacts marked as registered on the registered contact database on a
     * background thread, then adds them to the list.
     * */
    private void loadRegisteredList() {
        // do the rest in a separate thread
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                // get the registered contacts
                final List<Contact> registeredContacts = RegisteredContactManager.getRegisteredContacts(
                        SelectContactsActivity.this,
                        NetMeApp.getContactList()
                );

                // make sure to sort the list
                Collections.sort(registeredContacts);

                // load the contacts into the list view
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // set contacts
                        if(adapter != null) adapter.setContacts(registeredContacts);

                        if (registeredContacts.size() <= 0) {
                            Toast.makeText(
                                    SelectContactsActivity.this,
                                    R.string.no_contacts,
                                    Toast.LENGTH_SHORT
                            ).show();
                            finish();
                        }

                        // progress bar should no longer be visible
                        progressbar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    /**
     * Loads all the contacts not marked as registered on the registered contact database on a
     * background thread, then adds them to the list.
     * */
    private void loadNonRegisteredList() {
        // do the rest in a separate thread
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                // get the registered contacts
                List<Contact> allContacts = NetMeApp.getContactList();
                List<Contact> registeredContacts = RegisteredContactManager.getRegisteredContacts(
                        SelectContactsActivity.this,
                        allContacts
                );

                // adjust to have non registered contacts
                final List<Contact> nonRegisteredContacts = allContacts;
                nonRegisteredContacts.removeAll(registeredContacts);

                // make sure to sort the list
                Collections.sort(nonRegisteredContacts);

                // load the contacts into the list view
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // set contacts
                        if(adapter != null) adapter.setContacts(nonRegisteredContacts);

                        if (nonRegisteredContacts.size() <= 0) {
                            Toast.makeText(
                                    SelectContactsActivity.this,
                                    R.string.no_contacts,
                                    Toast.LENGTH_SHORT
                            ).show();
                            finish();
                        }

                        // progress bar should no longer be visible
                        progressbar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    /**
     * Loads all the contacts marked as a favorite on the registered contact database on a
     * background thread, then adds them to the list.
     * */
    private void loadFavoritesList() {
        // do the rest in a separate thread
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                // get the registered contacts
                final List<Contact> favoriteContacts = RegisteredContactManager.getFavoriteContacts(
                        SelectContactsActivity.this,
                        NetMeApp.getContactList()
                );

                // make sure to sort the list
                Collections.sort(favoriteContacts);

                // load the contacts into the list view
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // set contacts
                        if(adapter != null) adapter.setContacts(favoriteContacts);

                        if (favoriteContacts.size() <= 0) {
                            Toast.makeText(
                                    SelectContactsActivity.this,
                                    R.string.no_contacts,
                                    Toast.LENGTH_SHORT
                            ).show();
                            finish();
                        }

                        // progress bar should no longer be visible
                        progressbar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    /**
     * Loads all the contacts not marked as a favorite on the registered contact database on a
     * background thread, then adds them to the list.
     * */
    private void loadNonFavoritesList() {
        // do the rest in a separate thread
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                // adjust to have non registered contacts
                final List<Contact> nonFavoritesContact = RegisteredContactManager.getNonFavoriteContacts(
                        SelectContactsActivity.this,
                        NetMeApp.getContactList()
                );

                // make sure to sort the list
                Collections.sort(nonFavoritesContact);

                // load the contacts into the list view
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // set contacts
                        if(adapter != null) adapter.setContacts(nonFavoritesContact);

                        if (nonFavoritesContact.size() <= 0) {
                            Toast.makeText(
                                    SelectContactsActivity.this,
                                    R.string.no_contacts,
                                    Toast.LENGTH_SHORT
                            ).show();
                            finish();
                        }

                        // progress bar should no longer be visible
                        progressbar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    /**
     * Loads all the contacts with ids matching the passed id array list
     * background thread, then adds them to the list.
     * */
    private void loadContactsByIdList() {
        final long[] requestedIds = getIntent().getLongArrayExtra(EXTRA_CONTACT_IDS);

        // do the rest in a separate thread
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                final List<Contact> requestedContacts = new ArrayList<>();

                for (Contact contact : NetMeApp.getContactList()) {
                    for (long id : requestedIds) {
                        if (id == contact.getId()) {
                            requestedContacts.add(contact);
                            break;
                        }
                    }
                }

                if(requestedContacts.size() == 0) {
                    SelectContactsActivity.this.finish();
                    return;
                }

                // make sure to sort the list
                Collections.sort(requestedContacts);

                // load the contacts into the list view
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // set contacts
                        if(adapter != null) adapter.setContacts(requestedContacts);

                        if (requestedContacts.size() <= 0) {
                            Toast.makeText(
                                    SelectContactsActivity.this,
                                    R.string.no_contacts,
                                    Toast.LENGTH_SHORT
                            ).show();
                            finish();
                        }

                        // progress bar should no longer be visible
                        progressbar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    /**
     * Loads all the contacts with usernames/numbers matching the passed numbers array list
     * background thread, then adds them to the list.
     * */
    private void loadContactsByNumbersList() {
        final String[] requestedNumbers = getIntent().getStringArrayExtra(EXTRA_CONTACT_NUMBERS);

        // do the rest in a separate thread
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                final List<Contact> requestedContacts = new ArrayList<>();

                for (Contact contact : NetMeApp.getContactList()) {
                    for (Phone phone : contact.getPhones()) {
                        for (String number : requestedNumbers) {
                            if (phone.getPlainNumber().equals(number)) {
                                requestedContacts.add(contact);
                                break;
                            }
                        }
                    }
                }

                if(requestedContacts.size() == 0) {
                    SelectContactsActivity.this.finish();
                    return;
                }

                // make sure to sort the list
                Collections.sort(requestedContacts);

                // load the contacts into the list view
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // set contacts
                        if(adapter != null) adapter.setContacts(requestedContacts);

                        if (requestedContacts.size() <= 0) {
                            Toast.makeText(
                                    SelectContactsActivity.this,
                                    R.string.no_contacts,
                                    Toast.LENGTH_SHORT
                            ).show();
                            finish();
                        }

                        // progress bar should no longer be visible
                        progressbar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}
