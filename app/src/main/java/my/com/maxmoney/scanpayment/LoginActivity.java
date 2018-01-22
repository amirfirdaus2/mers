package my.com.maxmoney.scanpayment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final EditText etUsername = findViewById(R.id.et_username);
        final EditText etPassword = findViewById(R.id.et_password);

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(
                        etUsername.getText().toString(),
                        etPassword.getText().toString()
                );
            }
        });

    }

    private void login(String username, String password) {

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


                try {

                    int status = jsonObject.getInt("STATUS");
                    if(status == 1) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                                .edit()
                                    .putString("BRANCHCODE", jsonObject.getString("BRANCHCODE"))
                                    .putString("NAME", jsonObject.getString("FULLNAME"))
                                .commit();

                        startActivity(intent);
                        finish();
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

}
