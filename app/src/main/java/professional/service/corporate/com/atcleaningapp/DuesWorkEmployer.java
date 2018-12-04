package professional.service.corporate.com.atcleaningapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class DuesWorkEmployer extends Fragment {
    private ArrayList<SafeDepositClass> m_parts;
    private Runnable viewParts;
    private PaidBidAdapter m_adapter;
    ListView listView;
    ImageView seller_log;
    ImageView hirer_log;
    //DataBaseHelper dbh;
    String type = "Arm";
    private int cont_id;
    private int hirer_id;
    ImageView released_log;
    /** Called when the activity is first created. */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.paid_bids, container, false);

        listView = (ListView)view.findViewById(R.id.list1);

        SessionManager sm;
        ArrayList<LoginDBClass> lac;
        // released_log = (ImageView)view.findViewById(R.id.released_log);
        getPaidPayment();
        sm = new SessionManager(getActivity());
        m_parts = new ArrayList<>();
        lac = (ArrayList<LoginDBClass>) sm.getUserId();
        for (LoginDBClass f : lac) {
            cont_id = Integer.parseInt(f.getUser_id());
        }
        hirer_id = cont_id;

        return  view;
    }

    private void getPaidPayment() {




        class GetProducts extends AsyncTask<Void, Void, String> {

            private ProgressBar progressBar;

            @Override
            protected String doInBackground(Void... voids) {


                RequestHandler requestHandler = new RequestHandler(AppController.getAppContext());

                HashMap<String, String> params = new HashMap<>();

                String payer = String.valueOf(hirer_id);

                params.put("assignee_id", payer);

                try{
                    JSONObject response = requestHandler.sendPostRequest(Config.URL_UNPAID_MILESTONES_H, params, Request.Method.POST);
                    JSONObject mainResponse = (JSONObject) response.get("response");
                    //  int apiResponseStatus = mainResponse.getInt("status");
                    JSONArray projectData = (JSONArray) mainResponse.get("data");

                    for (int i = 0; i < projectData.length(); i++) {
                        JSONObject project = (JSONObject) projectData.get(i);

                        SafeDepositClass data = new SafeDepositClass();

                        data.slNo = i + 1;
                        data.payee = project.getString("user_name");
                        data.status = project.getString("status");
                        data.project_name = project.getString("title");
                        data.milestone_amt = new BigDecimal(project.getString("safe_deposit_amt"));
                        data.payment_date = project.getString("paymentDate");

                        m_parts.add(0,data);
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

                // hidepDialog();

            }
        }
        //executig the async task
        GetProducts ru = new GetProducts();
        ru.execute();
    }
    private void displayData() {

        if (getActivity()!=null) {
            m_adapter = new PaidBidAdapter(getActivity(), R.layout.paid_work_item, m_parts);
            m_adapter.notifyDataSetChanged();
            listView.setAdapter(m_adapter);
        }

        //Result rItem= resultlist.get(i);

    }
}
