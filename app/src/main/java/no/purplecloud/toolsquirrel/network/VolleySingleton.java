package no.purplecloud.toolsquirrel.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import no.purplecloud.toolsquirrel.domain.Employee;
import no.purplecloud.toolsquirrel.domain.Loan;
import no.purplecloud.toolsquirrel.domain.Project;
import no.purplecloud.toolsquirrel.domain.Tool;
import no.purplecloud.toolsquirrel.listener.CallbackListener;
import no.purplecloud.toolsquirrel.listener.ResponseListener;
import no.purplecloud.toolsquirrel.listener.VolleyErrorListener;
import no.purplecloud.toolsquirrel.singleton.CacheSingleton;

public class VolleySingleton {

    private static VolleySingleton instance;
    private RequestQueue requestQueue;
    private static Context context;

    private VolleySingleton(Context context) {
        // Specify the application context
        this.context = context;

        requestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            context = context.getApplicationContext();
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /*------------------------------
    Generic request methods
    ----------------------------*/

    public void postRequest(String url, JSONObject data, ResponseListener l, VolleyErrorListener e) {
        String requestBody = data.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                l::onCallback,
                e::onCallback
        ) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }
            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
        addToRequestQueue(stringRequest);
    }

    public void searchGetRequest(String url, String search, String type, CallbackListener l) {
        String requestUrl = url + search;
        List list = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, requestUrl, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            switch (type) {
                                case "tool":
                                    list.add(new Tool(response.getJSONObject(i)));
                                    break;

                                case "project":
                                    list.add(new Project(response.getJSONObject(i)));
                                    break;

                                case "employee":
                                    list.add(new Employee(response.getJSONObject(i)));
                                    break;

                                case "loan":
                                    list.add(new Loan(response.getJSONObject(i)));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    l.onCallback(list);
                }, System.out::println
        );
        addToRequestQueue(jsonArrayRequest);
    }

    public void searchPostRequestWithBody(String url, JSONObject data, String type, CallbackListener l) {
        List list = new ArrayList<>();
        String requestBody = data.toString();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            switch (type) {
                                case "tool":
                                    list.add(new Tool(response.getJSONObject(i)));
                                    break;

                                case "project":
                                    list.add(new Project(response.getJSONObject(i)));
                                    break;

                                case "employee":
                                    list.add(new Employee(response.getJSONObject(i)));
                                    break;

                                case "loan":
                                    list.add(new Loan(response.getJSONObject(i)));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    l.onCallback(list);
                }, System.out::println
        ) {
            @Override
            public String getBodyContentType() {
                return String.format("application/json; charset=utf-8");
            }
            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    return null;
                }
            }
        };
        addToRequestQueue(jsonArrayRequest);
    }

    public void getListRequest(String url, String type, CallbackListener l) {
        List list = new ArrayList<>();
        System.out.println("INSIDE GET LIST REQUEST");
        System.out.println("TYPE: " + type);
        System.out.println("URL: " + url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    System.out.println("SUCCESSFULLY GOT A RESPONSE!");
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            switch (type) {
                                case "tool":
                                    list.add(new Tool(response.getJSONObject(i)));
                                    break;

                                case "project":
                                    list.add(new Project(response.getJSONObject(i)));
                                    break;

                                case "employee":
                                    list.add(new Employee(response.getJSONObject(i)));
                                    break;

                                case "loan":
                                    list.add(new Loan(response.getJSONObject(i)));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    l.onCallback(list);
                }, System.out::println
        );
        addToRequestQueue(jsonArrayRequest);
    }

}
