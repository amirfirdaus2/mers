package my.com.maxmoney.scanpayment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import my.com.maxmoney.scanpayment.common.NumberGenerator;

public class PayCompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_complete);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Button buttonPayComplete = findViewById(R.id.btn_pay_complete);
        buttonPayComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showVerificationDialog(NumberGenerator.generateNumber());
            }
        });
    }

    private void showVerificationDialog(String code) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_verification_prompt, null);

        dialogBuilder.setView(view);

        dialogBuilder.setTitle(R.string.dialog_title);

        String message = String.format(getString(R.string.dialog_instruction), code);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton(R.string.dialog_response_proceed, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

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
