package professional.service.corporate.com.atcleaningapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.paypal.android.sdk.payments.PayPalItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MilestonesCreation extends Fragment implements
        ProductListAdapter1.ProductListAdapterListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ListView listView;
    private Button btnCheckout;
    SessionManager sm;
    ArrayList<LoginDBClass> lac;
    String user_id;
    // To store all the products
    private List<Product> productsList;
     TextView pay_safe;

    // To store the products those are added to cart
    private List<PayPalItem> productsInCart = new ArrayList<PayPalItem>();

    private ProductListAdapter1 adapter;
    private int project_id;

    // Progress dialog
    private ProgressDialog pDialog;
    View view;
    Bundle b;

    private int assignee_id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_paypal, container, false);
        sm = new SessionManager(getActivity());
        pay_safe = (TextView)view.findViewById(R.id.pay_safe);
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            user_id = f.getUser_id();
        }
        b = getArguments();
        project_id = b.getInt("project_id");
        assignee_id = b.getInt("assignee_id");
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        get_project_details(project_id);
        listView = (ListView)view.findViewById(R.id.list);
        productsList = new ArrayList<Product>();
        adapter = new ProductListAdapter1(getActivity(), productsList, this);

        listView.setAdapter(adapter);

        pay_safe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PayPalActivity.class);
                int assignee = assignee_id;
                int project = project_id;
                i.putExtra("payee",String.valueOf(assignee));
                i.putExtra("project_id",project);
                startActivity(i);
            }
        });

        // Starting PayPal service
        Intent intent = new Intent(getActivity(), PayPalActivity.class);

        getActivity().startService(intent);


        return view;
    }
    private void get_project_details(final int project_id) {



        class RegisterUser extends AsyncTask<Void, Void, String> {




            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                int current_project = project_id;
                String current_user = user_id;
                params.put("project_id", String.valueOf(current_project));
                params.put("user_id", current_user);

                try {
                    JSONObject response = requestHandler.sendPostRequest(Config.URL_JOBS_BID_AMOUNT, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

                    for(int i = 0; i < projectData.length(); i++){
                        JSONObject project = (JSONObject) projectData.get(i);

                        SafeDepositClass data=new SafeDepositClass();
                        data.slNo = i + 1;

                        String id = project.getString("id");
                        String project_name = project.getString("title");
                        String description = project.getString("name");
                        String safe_deposit_amt = project.getString("safe_deposit_amt");
                        String image = project.getString("image");
                        BigDecimal price = null;
                        if(!project.getString("fixed_price").equals("null")) {
                             price = new BigDecimal(project.getString("fixed_price"));
                        }
                        String sku = project.getString("sku");
                        String payee = project.getString("payee");
                        //Product p = new Product(String.valueOf(id), project_name, description, url, new BigDecimal(bid), sku, payee, safe_amt);
                        Product p = new Product(String.valueOf(id), project_name, description, safe_deposit_amt, image, (price), sku, payee);
                        productsList.add(p);
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
                //displaying the progress bar while user registers on the server
               pDialog.setMessage("Please Wait! Loading Details!");
               pDialog.show();
               pDialog.setCanceledOnTouchOutside(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                if(pDialog.isShowing()){
                    pDialog.cancel();
                }

            }
        }
        //executing the async task
        RegisterUser ru = new RegisterUser();
        ru.execute();
    }

    @Override
    public void onAddToCartPressed(final String _id, final String description, final String sku, final String name, final String price, final Double paid, final String payee) {
        // Showing progress dialog before making request
        pDialog.setMessage("Verifying payment...");
        showpDialog();

        class PaymentVerificationManager extends AsyncTask<Void, Void, String> {


            String user_id1;

            @Override
            protected String doInBackground(Void... voids) {
                //creating request handler object
                RequestHandler requestHandler = new RequestHandler(getActivity());

                //creating request parameters
                HashMap<String, String> params = new HashMap<>();
                user_id1 = user_id;
                params.put("id", _id);
                params.put("name", name);
                params.put("description", description);

                params.put("sku", sku);
                params.put("paid", String.valueOf(paid));
                params.put("price", price);
                params.put("assignee_id",payee);
                params.put("hirer_id",user_id1);

                try {
                    JSONObject response = requestHandler.sendPostRequest(Config.URL_EDITED_PAYMENT, params, Request.Method.POST);
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
                hidepDialog();

                Intent i = new Intent(getActivity(), PayPalActivity.class);
                i.putExtra("payee",payee);
                i.putExtra("project_id",project_id);
                startActivity(i);
            }
        }

        //executing the async task
        PaymentVerificationManager ru = new  PaymentVerificationManager();
        ru.execute();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    @Override
    public void onAccessPressed(String payee) {
        Intent i = new Intent(getActivity(), PayPalActivity.class);
        i.putExtra("payee",payee);
        i.putExtra("project_id",project_id);
        startActivity(i);
    }

    @Override
    public void onDestroy() {

        if(pDialog.isShowing()){
            pDialog.cancel();
        }
        super.onDestroy();
    }
}
