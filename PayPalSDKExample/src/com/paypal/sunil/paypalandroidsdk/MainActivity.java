package com.paypal.sunil.paypalandroidsdk;

import java.math.BigDecimal;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class MainActivity extends Activity {
    
    // set to PaymentActivity.ENVIRONMENT_LIVE to move real money.
    // set to PaymentActivity.ENVIRONMENT_SANDBOX to use your test credentials from https://developer.paypal.com
    // set to PaymentActivity.ENVIRONMENT_NO_NETWORK to kick the tires without communicating to PayPal's servers.
    private static final String CONFIG_ENVIRONMENT = PaymentActivity.ENVIRONMENT_SANDBOX;
    
    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AaPCFRBNgsIi-zh3qyjlXBmP0VF5Q2cnTBnkfuJ8ymBnLaXijzgvFQ05sMXt";
    // when testing in sandbox, this is likely the -facilitator email address. EMAIL DI CHI RICEVE IL PAGAMENTO !!
    private static final String CONFIG_RECEIVER_EMAIL = "wedmerchant@gmail.com"; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Intent intent = new Intent(this, PayPalService.class);
        
        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, CONFIG_ENVIRONMENT);
        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
        intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, CONFIG_RECEIVER_EMAIL);
        
        startService(intent);
    }

    public void onBuyPressed(View pressed) {
        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal("1.00"), "USD", "Gift Reservation");
        
        Intent intent = new Intent(this, PaymentActivity.class);
        
        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, CONFIG_ENVIRONMENT);
        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
        intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, CONFIG_RECEIVER_EMAIL);
        
        // It's important to repeat the clientId here so that the SDK has it if Android restarts your 
        // app midway through the payment UI flow.
        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "AaPCFRBNgsIi-zh3qyjlXBmP0VF5Q2cnTBnkfuJ8ymBnLaXijzgvFQ05sMXt");
        //In EXTRA_PAYER_ID ci va email di chi compra!
        intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, "wedbuyer@gmail.com");
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        
        startActivityForResult(intent, 0);
    }
    
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));
                    Toast.makeText(getApplicationContext(), "Pagamento completato", Toast.LENGTH_LONG).show();

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    Toast.makeText(getApplicationContext(), "ERRORE!", Toast.LENGTH_LONG).show();
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
            Toast.makeText(getApplicationContext(), "Pagamento cancellato", Toast.LENGTH_LONG).show();
        }
        else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
            Log.i("paymentExample", "An invalid payment was submitted. Please see the docs.");
            Toast.makeText(getApplicationContext(), "ERRORE!", Toast.LENGTH_LONG).show();
        }
    }
    
    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}
