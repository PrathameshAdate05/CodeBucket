package com.prathamesh.codebucket;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SingletonAPI {

    private static SingletonAPI singletonAPI;
    private RequestQueue requestQueue;
    private static Context context;

    SingletonAPI(Context con){
        context = con;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null){
            requestQueue =  Volley.newRequestQueue(context.getApplicationContext());
        }

        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }

    public static synchronized SingletonAPI getInstance(Context con){
        if (singletonAPI == null)
            singletonAPI = new SingletonAPI(con);

        return singletonAPI;
    }
}
