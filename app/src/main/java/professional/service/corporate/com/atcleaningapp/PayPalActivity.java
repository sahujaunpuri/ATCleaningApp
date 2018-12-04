package professional.service.corporate.com.atcleaningapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PayPalActivity extends Activity implements
        ProductListAdapter.ProductListAdapterListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ListView listView;
    private Button btnCheckout;

    // To store all the products
    private List<SafeDepositClass> productsList;
    private String sharedPrefKey3;
    SessionManager sm;
    ArrayList<LoginDBClass> lac;
    // To store the products those are added to cart
    private List<PayPalItem> productsInCart = new ArrayList<PayPalItem>();

    private ProductListAdapter adapter;
    private String user_id;
    TextView release_amt;
    // Progress dialog
    private ProgressDialog pDialog;
    String payee;
    int project_id;
    private static final int REQUEST_CODE_PAYMENT = 1;
    private TextView release_pay;
    // PayPal configuration
    private static PayPalConfiguration paypalConfig = new PayPalConfiguration()
            .environment(Config.PAYPAL_ENVIRONMENT).clientId(
                    Config.PAYPAL_CLIENT_ID);
    private SharedPreferences sharedPref4;
    private String sharedPrefKey4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal1);
//        release_pay = (TextView)findViewById(R.id.release_ms);
//        release_amt = (TextView)findViewById(R.id.release_Amt);
        sharedPref4 = getSharedpreferences3();
        sm = new SessionManager(getApplicationContext());
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        Bundle b = getIntent().getExtras();
        payee = b.getString("payee");
        project_id = b.getInt("project_id");
        for (LoginDBClass f : lac) {
            user_id = f.getUser_id();
        }
        listView = (ListView) findViewById(R.id.list);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        //  btnCheckout = (Button) findViewById(R.id.btnCheckout);
        // getReleasablePay(project_id);
        productsList = new ArrayList<SafeDepositClass>();


        listView.setAdapter(adapter);



        // Starting PayPal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        startService(intent);
//        release_pay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(PayPalActivity.this, PayPalActivity2.class);
//                i.putExtra("payee", payee);
//
//                startActivity(i);
//            }
//        });
        // Checkout button click listener
//        btnCheckout.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                // Check for empty cart
//                if (productsInCart.size() > 0) {
//                    launchPayPalPayment();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Select A Payee Please!.",
//                            Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

        // Fetching products from server
        fetchProducts();
    }

    /**
     * Fetching the products from our server
     * */
    private void fetchProducts() {
        // Showing progress dialog before making request

        //  pDialog.setMessage("Getting Payee List...");
        showpDialog();

        class GetProducts extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {


                RequestHandler requestHandler = new RequestHandler(getApplicationContext());

                HashMap<String, String> params = new HashMap<>();
                String user_id1 = user_id;
                String payee1 = payee;
                int project_id1 = project_id;
                params.put("user_id",user_id1 );
                params.put("assignee_id", payee1);
                params.put("project_id", String.valueOf(project_id1));
                try{
                    JSONObject response = requestHandler.sendPostRequest(Config.URL_MILESTONES, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

//            for(int i = 0; i < projectData.length(); i++){
//                JSONObject product = (JSONObject) projectData.get(i);
//
//
//                String id = product.getString("id");
//                String name = product.getString("payee_name");
//                String description = product
//                        .getString("description");
//
//                BigDecimal milestone_amt = new BigDecimal(product
//                        .getString("milestone_amt"));
//                String sku = product.getString("sku");
//                String status = product.getString("status");
//                SafeDepositClass p = new SafeDepositClass(id, name, description,milestone_amt, sku, status);
//
//                productsList.add(p);
//            }
                    for (int i = 0; i < projectData.length(); i++) {
                        JSONObject project = (JSONObject) projectData.get(i);

                        SafeDepositClass data = new SafeDepositClass();

                        // data.id = project.getInt("id");

                        data.id = project.getInt("id");
                        data.payee = project.getString("assignee_name");
                        //  data.status = project.getString("status");
                        data.description = project.getString("description");
                        data.milestone_amt = new BigDecimal(project.getString("safe_deposit_amt"));
                        data.sku = project.getString("sku");
                        data.status = project.getString("status");



                        //data.bids = ""; //project.getString("bids");
                        productsList.add(0,data);
                    }
                    return mainResponse.getString("status");
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }

                return null;
            }


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                //  adapter = new ProductListAdapter(PayPalActivity.this, productsList, (ProductListAdapter.ProductListAdapterListener) this);
                displayData();

                hidepDialog();

            }
        }
        //executig the async task
        GetProducts ru = new GetProducts();
        ru.execute();
    }

    private void displayData() {
        adapter = new ProductListAdapter(PayPalActivity.this, productsList, this);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    /**
     * Verifying the mobile payment on the server to avoid fraudulent payment
     * */
//    private void getReleasablePay(final int project_id) {
//        // Showing progress dialog before making request
//        pDialog.setMessage("Refreshing...");
//        showpDialog();
//
//        class PaymentVerificationManager extends AsyncTask<Void, Void, String> {
//
//            private ProgressBar progressBar;
//            private String user_id1 = user_id;
//            @Override
//            protected String doInBackground(Void... voids) {
//                //creating request handler object
//                RequestHandler requestHandler = new RequestHandler(getApplicationContext());
//
//                //creating request parameters
//                HashMap<String, String> params = new HashMap<>();
//
//                params.put("project_id", String.valueOf(project_id));
//                String amount_to_be_released = null;
//                try {
//                    JSONObject response = requestHandler.sendPostRequest(URLs.URL_RELEASABLE_PAYMENT, params, Request.Method.POST);
//                    JSONObject mainResponse = (JSONObject) response.get("response");
//                    JSONArray projectData = (JSONArray) mainResponse.get("data");
//
//                    for (int i = 0; i < projectData.length(); i++) {
//                        JSONObject project = (JSONObject) projectData.get(i);
//
//                        Product data = new Product();
//
//                         data.amount_to_be_released = project.getString("to_be_released");
//
//                    }
//
//                        return amount_to_be_released;
//                }catch (JSONException ex){
//                    ex.printStackTrace();
//                }
//
//                return null;
//            }
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                release_amt.setText(s);
//                hidepDialog();
//
//            }
//        }
//
//        //executing the async task
//        PaymentVerificationManager ru = new  PaymentVerificationManager();
//        ru.execute();
//    }
    /**
     * Preparing final cart amount that needs to be sent to PayPal for payment
     * */
    private PayPalPayment prepareFinalCart() {

        PayPalItem[] items = new PayPalItem[productsInCart.size()];
        items = productsInCart.toArray(items);

        // Total amount
        BigDecimal subtotal = PayPalItem.getItemTotal(items);

        // If you have shipping cost, add it here
        BigDecimal shipping = new BigDecimal("0.0");

        // If you have tax, add it here
        BigDecimal tax = new BigDecimal("0.0");

        PayPalPaymentDetails paymentDetails = new PayPalPaymentDetails(
                shipping, subtotal, tax);

        BigDecimal amount = subtotal.add(shipping).add(tax);

        PayPalPayment payment = new PayPalPayment(
                amount,
                Config.DEFAULT_CURRENCY,
                "Payment To Contractors for  work.",
                Config.PAYMENT_INTENT);

        payment.items(items).paymentDetails(paymentDetails);

        // Custom field like invoice_number etc.,
        payment.custom("Payment From User .");

        return payment;
    }

    /**
     * Launching PalPay payment activity to complete the payment
     * */
    private void launchPayPalPayment() {

        PayPalPayment thingsToBuy = prepareFinalCart();

        Intent intent = new Intent(PayPalActivity.this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingsToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    /**
     * Receiving the PalPay payment response
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.e(TAG, confirm.toJSONObject().toString(4));
                        Log.e(TAG, confirm.getPayment().toJSONObject()
                                .toString(4));

                        String paymentId = confirm.toJSONObject()
                                .getJSONObject("response").getString("id");

                        String payment_client = confirm.getPayment()
                                .toJSONObject().toString();

                        Log.e(TAG, "paymentId: " + paymentId
                                + ", payment_json: " + payment_client);

                        // Now verify the payment on the server side
                        verifyPaymentOnServer(paymentId, payment_client);
                        fetchProducts();
                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ",
                                e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e(TAG,
                        "An invalid Payment or PayPalConfiguration was submitted.");
            }
        }
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onAddToCartPressed(SafeDepositClass product) {

        PayPalItem item = new PayPalItem(product.getPayee(), 1,
                product.getMilestone_amt(), Config.DEFAULT_CURRENCY, product.getSku());
        sharedPrefKey4 = getResources().getString(R.string.milestone_id);

        SharedPreferences.Editor mEditor4 = sharedPref4.edit();
        mEditor4.putString(getString(R.string.milestone_id), String.valueOf(product.getId()));
        mEditor4.commit();
        if(productsInCart.size()>0)
        {
            productsInCart.remove(0);
            productsInCart.add(item);
        }
        else{
            productsInCart.add(item);
        }

        launchPayPalPayment();
        Toast.makeText(getApplicationContext(),
                item.getName() + " please wait!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onReleasedPressed(SafeDepositClass product) {

        Intent i = new Intent(PayPalActivity.this, PayPalActivity2.class);
        BigDecimal amt_to_be_Released = product.getMilestone_amt();
        String description = product.getDescription();

        i.putExtra("milestone_amt",String.valueOf(amt_to_be_Released));
        i.putExtra("payee", payee);
        i.putExtra("description", description);
        startActivity(i);
    }



    /**
     * Verifying the mobile payment on the server to avoid fraudulent payment
     * */
    private void verifyPaymentOnServer(final String paymentId,
                                       final String payment_client) {
        // Showing progress dialog before making request
        pDialog.setMessage("Verifying payment...");
        showpDialog();

        class PaymentVerificationManager extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;
            private String user_id1 = user_id;
            private String milestone_id = sharedPref4.getString(getString(R.string.milestone_id), "");
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();

                params.put("paymentId", paymentId);
                params.put("paymentClientJson", payment_client);
                params.put("user_id",user_id1);
                params.put("milestone_id",milestone_id);
                try {
                    JSONObject response = requestHandler.sendPostRequest(Config.URL_VERIFY_PAYMENT, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    int apiResponseStatus = mainResponse.getInt("status");
                    String apiResponseMessage = mainResponse.getString("message");
                    Log.i("Sourav, web response", response.toString());

                    return apiResponseMessage;
                }catch (JSONException ex){
                    ex.printStackTrace();
                }

                return null;
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                verifySafeDepositClassmentOnServer();
                hidepDialog();

            }
        }

        //executing the async task
        PaymentVerificationManager ru = new  PaymentVerificationManager();
        ru.execute();
    }
    private void verifySafeDepositClassmentOnServer() {
        // Showing progress dialog before making request
        pDialog.setMessage("Verifying status...");
        showpDialog();

        class PaymentVerificationManager extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;
            private String user_id1 = user_id;
            private String milestone_id = sharedPref4.getString(getString(R.string.milestone_id), "");
            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(getApplicationContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();

                params.put("milestone_id",milestone_id);
                try {
                    JSONObject response = requestHandler.sendPostRequest(Config.URL_VERIFY_PAYMENT_STATUS, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");


                    for (int i = 0; i < projectData.length(); i++) {
                        JSONObject project = (JSONObject) projectData.get(i);

                        SafeDepositClass data = new SafeDepositClass();

                        data.status = project.getString("status");



                        //data.bids = ""; //project.getString("bids");
                        productsList.add(0,data);
                    }
                }catch (JSONException ex){
                    ex.printStackTrace();
                }

                return null;
            }
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                hidepDialog();

            }
        }

        //executing the async task
        PaymentVerificationManager ru = new  PaymentVerificationManager();
        ru.execute();
    }
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(), NavigationBarUser.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.enter, R.anim.exit);
        //super.onStop();
        System.exit(0);

    }
    private SharedPreferences getSharedpreferences3() {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.milestone_id), Context.MODE_PRIVATE);

        return sharedPref;
    }
}