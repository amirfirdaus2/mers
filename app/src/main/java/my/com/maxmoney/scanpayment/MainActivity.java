package my.com.maxmoney.scanpayment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;

import my.com.maxmoney.scanpayment.adapter.ScanAdapter;
import my.com.maxmoney.scanpayment.model.ScanModel;

public class MainActivity extends AppCompatActivity {

    private ScanAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_scan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ScanActivity.class);
                startActivity(intent);
            }
        });

        mAdapter = new ScanAdapter(this);

        RecyclerView recyclerView = findViewById(R.id.rv_history);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, llm.getOrientation()));
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(mAdapter);

        populateHistories();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void populateHistories() {
        mAdapter.add(new ScanModel("MUHAMMAD IQBAL", "120102102", new Date(), 2001));
        mAdapter.add(new ScanModel("MUHAMMAD IQBAL", "120102102", new Date(), 2002));
    }
}
