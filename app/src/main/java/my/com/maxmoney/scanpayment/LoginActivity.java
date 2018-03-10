package my.com.maxmoney.scanpayment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import my.com.maxmoney.scanpayment.common.StandardProgressDialog;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mTILUsername;
    private TextInputLayout mTILPassword;

    private StandardProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTILUsername = findViewById(R.id.til_username);
        mTILPassword = findViewById(R.id.til_password);

        mTILUsername.setHint(getResources().getString(R.string.form_username));
        mTILPassword.setHint(getResources().getString(R.string.form_password));

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Editable username = mTILUsername.getEditText().getText();
                Editable password = mTILPassword.getEditText().getText();

                if(username != null && password != null) {

                    login(
                            username.toString(),
                            password.toString()
                    );
                }
            }
        });

        mProgress = new StandardProgressDialog(this);
        mProgress.setMessage("Authenticating");
    }

    @Override
    public void onBackPressed() {
        showExitAppAlert();
    }

    private void login(String username, String password) {

        hideKeyboard();

        mProgress.show();

        final JSONObject jsonData = new JSONObject();

        try {
            jsonData.put("username",username);
            jsonData.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonReq = new JsonObjectRequest(
                Request.Method.POST,
                "https://zeptopay.co/app/scan-pay-login.php",
                jsonData,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                mProgress.dismiss();

                try {

                    int status = jsonObject.getInt("STATUS");
                    if(status == 1) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                .edit()
                                    .putString("BRANCHCODE", jsonObject.getString("BRANCHCODE"))
                                    .putString("NAME", jsonObject.getString("FULLNAME"))
                                .apply();

                        startActivity(intent);
                        finish();
                    } else {
                        mTILUsername.setError("Incorrect username");
                        mTILPassword.setError("Incorrect password");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mProgress.dismiss();
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

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void showExitAppAlert() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle(R.string.dialog_exit_app);
        dialogBuilder.setMessage(getString(R.string.dialog_exit_ask));
        dialogBuilder.setPositiveButton(R.string.dialog_response_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAndRemoveTask();
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

}
