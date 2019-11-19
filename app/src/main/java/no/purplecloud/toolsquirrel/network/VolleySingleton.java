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
import no.purplecloud.toolsquirrel.domain.Project;
import no.purplecloud.toolsquirrel.domain.Tool;
import no.purplecloud.toolsquirrel.listener.ResponseListener;
import no.purplecloud.toolsquirrel.listener.VolleyErrorListener;

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

    public <T> List<T> searchPostRequest(String url, String search, String type) {
        String requestUrl = url + search;
        List<T> list = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, requestUrl, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            // TODO MAKE THIS WORK
                            switch (type) {
                                case "tool":
                                    list.add((T) new Tool(response.getJSONObject(i)));
                                    break;

                                case "project":
                                    list.add((T) new Project(response.getJSONObject(i)));
                                    break;

                                case "employee":
                                    list.add((T) new Employee(response.getJSONObject(i)));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                System.out::println
        );
        addToRequestQueue(jsonArrayRequest);
        return list;
    }

    public <T> List<T> getListRequest(String url, String type) {
        List<T> list = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            // TODO MAKE THIS WORK
                            switch (type) {
                                case "tool":
                                    list.add((T) new Tool(response.getJSONObject(i)));
                                    break;

                                case "project":
                                    list.add((T) new Project(response.getJSONObject(i)));
                                    break;

                                case "employee":
                                    list.add((T) new Employee(response.getJSONObject(i)));
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, System.out::println
        );
        addToRequestQueue(jsonArrayRequest);
        return list;
    }

}
