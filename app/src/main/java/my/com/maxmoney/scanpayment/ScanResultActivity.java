package my.com.maxmoney.scanpayment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import my.com.maxmoney.scanpayment.common.AppData;
import my.com.maxmoney.scanpayment.common.CodeGenerator;

public class ScanResultActivity extends AppCompatActivity {

    private final static String TAG = ScanResultActivity.class.getSimpleName();

    private Button mBtnPayNow;
    private TextView mTVName;
    private TextView mTVStatus;
    private TextView mTVAmount;

    private String mJobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mJobId = getIntent().getExtras().getString("jobid");
        if(mJobId == null || mJobId.isEmpty()) {
            finish();
        }

        mTVName = findViewById(R.id.tv_name);
        mTVStatus = findViewById(R.id.tv_status);
        mTVAmount = findViewById(R.id.tv_amount);

        getRecord();

        mBtnPayNow = findViewById(R.id.btn_pay_now);
        mBtnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showVerificationDialog(CodeGenerator.generateNumber());
            }
        });
    }

    private void getRecord() {

        final JSONObject jsonData = new JSONObject();

        try {
            jsonData.put("jobid", mJobId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonReq = new JsonObjectRequest(
                Request.Method.POST,
                "http://zeptopay.co/app/scan-pay.php",
                jsonData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        Log.d(TAG, jsonObject.toString());

                        try {

                            int status = jsonObject.getInt("STATUS");
                            if(status == 1) {
                                mTVName.setText(jsonObject.getString("FULLNAME"));

                                String payStatus = jsonObject.getString("PAYSTATUS");
                                if(payStatus.equalsIgnoreCase("1")) {
                                    mTVStatus.setText("PAID");
                                    mTVStatus.setTextColor(Color.parseColor("#27ae60"));
                                    mBtnPayNow.setVisibility(View.GONE);
                                } else {
                                    mTVStatus.setText("NOT PAID");
                                }

                                mTVAmount.setText(jsonObject.getString("AMOUNT"));
                            } else {
                                showWrongId();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

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

    private void updateRecord() {

        final JSONObject jsonData = new JSONObject();

        try {
            jsonData.put("jobid", mJobId);
            jsonData.put("paid", "1");
            jsonData.put("branchcode", PreferenceManager.getDefaultSharedPreferences(this)
            .getString("BRANCHCODE", ""));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonReq = new JsonObjectRequest(
                Request.Method.POST,
                "http://zeptopay.co/app/scan-pay.php",
                jsonData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        showSuccess();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

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

    private void showVerificationDialog(final String code) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_verification_prompt, null);

        final EditText etCode = view.findViewById(R.id.et_code);

        dialogBuilder.setView(view);

        dialogBuilder.setTitle(R.string.dialog_title);

        String message = String.format(getString(R.string.dialog_instruction), code);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton(R.string.dialog_response_proceed, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String enteredCode = etCode.getText().toString();
                if(enteredCode.equalsIgnoreCase(code)) {
                    updateRecord();
                }
            }
        });

        dialogBuilder.setNegativeButton(R.string.dialog_response_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // pass
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void showSuccess() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.dialog_success);
        builder.setPositiveButton(R.string.dialog_response_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showWrongId() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.dialog_wrong_id);
        builder.setPositiveButton(R.string.dialog_response_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
