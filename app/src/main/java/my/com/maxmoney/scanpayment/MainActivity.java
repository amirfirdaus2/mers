package my.com.maxmoney.scanpayment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import my.com.maxmoney.scanpayment.adapter.ScanAdapter;
import my.com.maxmoney.scanpayment.common.StandardProgressDialog;
import my.com.maxmoney.scanpayment.model.ScanModel;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    private ScanAdapter mAdapter;

    private StandardProgressDialog mProgressDialog;

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

        mProgressDialog = new StandardProgressDialog(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateHistories();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /***
     * Disabled back button
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void populateHistories() {
//        mAdapter.add(new ScanModel("MUHAMMAD IQBAL", "120102102", new Date(), 2001));
//        mAdapter.add(new ScanModel("MUHAMMAD IQBAL", "120102102", new Date(), 2002));

        mAdapter.clear();

        final JSONObject jsonData = new JSONObject();

        try {
            jsonData.put("branchcode",PreferenceManager.getDefaultSharedPreferences(this)
            .getString("BRANCHCODE", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mProgressDialog.show();

        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.POST,
                "https://zeptopay.co/app/scan-pay-list.php",
                jsonData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject object) {

                        mProgressDialog.dismiss();

                        Log.d(TAG, object.toString());

                        try {
                            JSONArray array = object.getJSONArray("data");
                            for(int i=0; i < array.length(); i++) {

                                JSONObject obj = array.getJSONObject(i);

                                ScanModel model = new ScanModel();
                                model.setTransactionId(obj.getString("JOBID"));
                                model.setName(obj.getString("FULLNAME"));
                                model.setAmount(obj.getString("AMOUNT"));
                                model.setStatus(obj.getInt("PAYSTATUS"));

                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                                try {
                                    model.setTimestamp(format.parse(obj.getString("DTTIME")));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                mAdapter.add(model);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mProgressDialog.dismiss();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("User-Agent", "Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Mobile Safari/537.36");
                return super.getHeaders();
            }
        };

        Volley.newRequestQueue(getBaseContext()).add(jsonReq);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:

                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                        .edit()
                            .putString("BRANCHCODE", "")
                            .putString("NAME", "")
                        .apply();

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        return true;
    }
}
