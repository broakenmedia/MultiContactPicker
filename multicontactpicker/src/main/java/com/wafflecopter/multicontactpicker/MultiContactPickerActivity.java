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

import com.l4digital.fastscroll.FastScrollRecyclerView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.wafflecopter.multicontactpicker.RxContacts.ContactResult;
import com.wafflecopter.multicontactpicker.RxContacts.RxContacts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MultiContactPickerActivity extends AppCompatActivity implements MaterialSearchView.OnQueryTextListener {

    public static final String EXTRA_RESULT_SELECTION = "extra_result_selection";
    private FastScrollRecyclerView recyclerView;
    private List<ContactResult> contactList = new ArrayList<>();
    private TextView tvSelectBtn;
    private MultiContactPickerAdapter adapter;
    private Toolbar toolbar;
    private MaterialSearchView searchView;
    private ProgressBar progressBar;
    private MenuItem searchMenuItem;
    private MultiContactPicker.Builder builder;

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
        tvSelectBtn = (TextView) findViewById(R.id.tvSelect);
        recyclerView = (FastScrollRecyclerView) findViewById(R.id.recyclerView);

        initialiseUI(builder);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new MultiContactPickerAdapter(contactList, new MultiContactPickerAdapter.ContactSelectListener() {
            @Override
            public void onContactSelected(ContactResult contact, int totalSelectedContacts) {
                tvSelectBtn.setEnabled(totalSelectedContacts > 0);
                if(totalSelectedContacts > 0) {
                    tvSelectBtn.setText(getString(R.string.tv_select_btn_text_enabled, String.valueOf(totalSelectedContacts)));
                } else {
                    tvSelectBtn.setText(getString(R.string.tv_select_btn_text_disabled));
                }
            }
        });

        loadContacts();

        recyclerView.setAdapter(adapter);

        tvSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent result = new Intent();
                result.putExtra(EXTRA_RESULT_SELECTION, adapter.getSelectedContacts());
                setResult(RESULT_OK, result);
                finish();
            }
        });

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadContacts(){
        progressBar.setVisibility(View.VISIBLE);
        RxContacts.fetch(this)
                .filter(new Predicate<ContactResult>() {
                    @Override
                    public boolean test(ContactResult contact) throws Exception {
                        return contact.getDisplayName() != null;
                    }
                })
                .toSortedList(new Comparator<ContactResult>() {
                    @Override
                    public int compare(ContactResult contact, ContactResult t1) {
                        return contact.getDisplayName().compareToIgnoreCase(t1.getDisplayName());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new SingleObserver<List<ContactResult>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}
                    @Override
                    public void onSuccess(List<ContactResult> contacts) {
                        contactList.clear();
                        contactList.addAll(contacts);
                        if(adapter != null && contacts.size() > 0){
                            adapter.notifyDataSetChanged();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchMenuItem = menu.findItem(R.id.action_search);
        setSearchIconColor(searchMenuItem, builder.searchIconColor);
        searchView.setMenuItem(searchMenuItem);
        return true;
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
            adapter.getFilter().filter(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(adapter != null){
            adapter.getFilter().filter(newText);
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
