package professional.service.corporate.com.atcleaningapp;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Belal on 9/5/2017.
 */

public class RequestHandler {
    Context context;
    RequestQueue requestQueue;

    public RequestHandler(Context ctx){
        this.context = ctx;
        this.requestQueue = Volley.newRequestQueue(this.context);
    }

    //this method will send a post request to the specified url
    //in this app we are using only post request
    //in the hashmap we have the data to be sent to the server in keyvalue pairs
    public JSONObject sendPostRequest(String requestURL, HashMap<String, String> postDataParams, int requestMethod)  {
        String response="";
        final JSONObject requestBody = new JSONObject();
        try {
            if(postDataParams != null && postDataParams.size() > 0) {
                for (Map.Entry<String, String> entry : postDataParams.entrySet()) {
                    requestBody.put(entry.getKey(), entry.getValue());
                }
            }

            RequestFuture<JSONObject> future = RequestFuture.newFuture();

            JsonObjectRequest request = new JsonObjectRequest(requestMethod, requestURL, requestBody, future, future);
            this.requestQueue.add(request);

            JSONObject webServiceResponse = future.get();
            return webServiceResponse;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
    //this method is converting keyvalue pairs data into a query string as needed to send to the server
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}