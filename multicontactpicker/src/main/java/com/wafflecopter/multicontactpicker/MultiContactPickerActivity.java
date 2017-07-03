package com.wafflecopter.multicontactpicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.l4digital.fastscroll.FastScrollRecyclerView;
import com.wafflecopter.multicontactpicker.RxContacts.Contact;
import com.wafflecopter.multicontactpicker.RxContacts.RxContacts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MultiContactPickerActivity extends AppCompatActivity {

    public static final String EXTRA_RESULT_SELECTION = "extra_result_selection";

    private FastScrollRecyclerView recyclerView;
    private List<Contact> contactList = new ArrayList<>();
    private MultiContactPickerAdapter adapter;
    private TextView tvSelectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null) return;

        MultiContactPicker.Builder builder = (MultiContactPicker.Builder) intent.getSerializableExtra("builder");

        setTheme(builder.theme != null ? builder.theme : 0);

        setContentView(R.layout.activity_multi_contact_picker);

        tvSelectBtn = (TextView) findViewById(R.id.tvSelect);
        recyclerView = (FastScrollRecyclerView) findViewById(R.id.recyclerView);

        initialiseUI(builder);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MultiContactPickerAdapter(contactList, new MultiContactPickerAdapter.ContactSelectListener() {
            @Override
            public void onContactSelected(Contact contact, int totalSelectedContacts) {
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
                result.putExtra(EXTRA_RESULT_SELECTION, MultiContactPicker.buildResult(adapter.getSelectedContacts()));
                setResult(RESULT_OK, result);
                finish();
            }
        });

    }

    private void initialiseUI(MultiContactPicker.Builder builder){
        if(builder.bubbleColor != 0)
            recyclerView.setBubbleColor(builder.bubbleColor);
        if(builder.handleColor != 0)
            recyclerView.setHandleColor(builder.handleColor);
        if(builder.textColor != 0)
            recyclerView.setBubbleTextColor(builder.textColor);
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
        RxContacts.fetch(this)
                .toSortedList(new Comparator<Contact>() {
                    @Override
                    public int compare(Contact contact, Contact t1) {
                        return contact.getDisplayName().compareToIgnoreCase(t1.getDisplayName());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new SingleObserver<List<Contact>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}
                    @Override
                    public void onSuccess(List<Contact> value) {
                        contactList.clear();
                        contactList.addAll(value);
                        if(adapter != null){
                            adapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }
}
