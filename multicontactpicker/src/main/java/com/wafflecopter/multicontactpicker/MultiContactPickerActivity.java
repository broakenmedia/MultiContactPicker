package com.wafflecopter.multicontactpicker;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.l4digital.fastscroll.FastScrollRecyclerView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.wafflecopter.multicontactpicker.RxContacts.Contact;
import com.wafflecopter.multicontactpicker.RxContacts.RxContacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import com.wafflecopter.multicontactpicker.R;

public class MultiContactPickerActivity extends AppCompatActivity implements MaterialSearchView.OnQueryTextListener {

    public static final String EXTRA_RESULT_SELECTION = "extra_result_selection";
    private FastScrollRecyclerView recyclerView;
    private List<Contact> contactList = new ArrayList<>();
    private MultiContactPickerAdapter adapter;
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private ProgressBar progressBar;
    private MenuItem searchMenuItem, selectMenuItem, doneMenuItem;
    private MultiContactPicker.Builder builder;
    private boolean isAllSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) return;

        builder = (MultiContactPicker.Builder) intent.getSerializableExtra("builder");

        setTheme(builder.theme);

        setContentView(R.layout.activity_multi_contact_picker);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (FastScrollRecyclerView) findViewById(R.id.recyclerView);

        initialiseUI(builder);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new MultiContactPickerAdapter(contactList, new MultiContactPickerAdapter.ContactSelectListener() {
            @Override
            public void onContactSelected(Contact contact, int totalSelectedContacts) {
                doneMenuItem.setVisible(totalSelectedContacts > 0);
                if(totalSelectedContacts > 0) {
                    toolbar.setTitle(String.valueOf(totalSelectedContacts));
                } else {
                    toolbar.setTitle(getString(R.string.picker_title));
                }
            }

            @Override
            public void onAllContactSelected(boolean isSelected, int totalSelectedContacts) {
                doneMenuItem.setVisible(isSelected);
                if(isSelected) {
                    toolbar.setTitle(String.valueOf(totalSelectedContacts));
                } else {
                    toolbar.setTitle(getString(R.string.picker_title));
                }
            }
        });

        loadContacts();

        recyclerView.setAdapter(adapter);
    }

    private void initialiseUI(MultiContactPicker.Builder builder){
        setSupportActionBar(toolbar);
        searchView.setOnQueryTextListener(this);
        if(builder.bubbleColor != 0)
            recyclerView.setBubbleColor(builder.bubbleColor);
        if(builder.handleColor != 0)
            recyclerView.setHandleColor(builder.handleColor);
        if(builder.bubbleTextColor != 0)
            recyclerView.setBubbleTextColor(builder.bubbleTextColor);
        if(builder.trackColor != 0)
            recyclerView.setTrackColor(builder.trackColor);
        recyclerView.setHideScrollbar(builder.hideScrollbar);
        recyclerView.setTrackVisible(builder.showTrack);
    }

    private void loadContacts(){
        progressBar.setVisibility(View.VISIBLE);
        RxContacts.fetch(this)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<Contact>() {
                    @Override
                    public boolean test(Contact contact) throws Exception {
                        return contact.getDisplayName() != null;
                    }
                })
                .subscribe(new Observer<Contact>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(Contact value) {
                        contactList.add(value);
                        Collections.sort(contactList, new Comparator<Contact>() {
                            @Override
                            public int compare(Contact contact, Contact t1) {
                                return contact.getDisplayName().compareToIgnoreCase(t1.getDisplayName());
                            }
                        });
                        if(adapter != null){
                            adapter.notifyDataSetChanged();
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mcp_menu_main, menu);
        searchMenuItem = menu.findItem(R.id.mcp_action_search);
        selectMenuItem = menu.findItem(R.id.mcp_action_select);
        doneMenuItem = menu.findItem(R.id.mcp_action_done);
        setSearchIconColor(searchMenuItem, builder.searchIconColor);
        searchView.setMenuItem(searchMenuItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        else if(id==R.id.mcp_action_select) {
            if(!isAllSelected) {
                adapter.setAllContactSelected(isAllSelected = true);
                selectMenuItem.setIcon(R.drawable.ic_check_box_24dp);
            }
            else {
                adapter.setAllContactSelected(isAllSelected = false);
                selectMenuItem.setIcon(R.drawable.ic_check_box_outline_blank_24dp);
            }
        }
        else if(id==R.id.mcp_action_done) {
            Intent result = new Intent();
            result.putExtra(EXTRA_RESULT_SELECTION, MultiContactPicker.buildResult(adapter.getSelectedContacts()));
            setResult(RESULT_OK, result);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSearchIconColor(MenuItem menuItem, final Integer color) {
        if(color != null) {
            Drawable drawable = menuItem.getIcon();
            if (drawable != null) {
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(drawable.mutate(), color);
                menuItem.setIcon(drawable);
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(adapter != null){
            adapter.filterOnText(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(adapter != null){
            adapter.filterOnText(newText);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
