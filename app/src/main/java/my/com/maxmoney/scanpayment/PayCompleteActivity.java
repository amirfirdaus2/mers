package my.com.maxmoney.scanpayment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import my.com.maxmoney.scanpayment.common.CodeGenerator;

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
                showVerificationDialog(CodeGenerator.generateHex());
            }
        });
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
                    showSuccess();
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
}
