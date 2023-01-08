package com.supergianlu.controlli.activity.servizi;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.kyanogen.signatureview.SignatureView;
import com.supergianlu.controlli.R;

import java.io.ByteArrayOutputStream;

import static com.supergianlu.controlli.util.Helper.EXTRA_SIGNATURE;

public class SignatureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        final Button confirmButton = findViewById(R.id.confirmButton);
        final Button redoButton = findViewById(R.id.redoButton);
        final SignatureView signatureView = findViewById(R.id.signature_view);

        redoButton.setOnClickListener(e -> signatureView.clearCanvas());

        confirmButton.setOnClickListener(e -> {
            if(!signatureView.isBitmapEmpty()) {
                new AlertDialog.Builder(this)
                        .setMessage(getString(R.string.confirm_sign_question))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), (dialog, id) -> {
                            Intent intent = new Intent();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            signatureView.getSignatureBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            intent.putExtra(EXTRA_SIGNATURE, byteArray);
                            setResult(RESULT_OK, intent);
                            finish();
                        })
                        .setNegativeButton(getString(R.string.no), null)
                        .show();
            } else {
                Toast.makeText(this, getString(R.string.do_signature), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
