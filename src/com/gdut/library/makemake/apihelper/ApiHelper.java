package com.gdut.library.makemake.apihelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.UserDataHandler;

import android.util.Log;


import com.gdut.library.makemake.apihelper.ApiUser;
import com.gdut.library.makemake.apihelper.ApiBook;
import com.gdut.library.makemake.apihelper.ApiNotFoundException;
import com.gdut.library.makemake.apihelper.ApiLoginActivateException;
import com.gdut.library.makemake.apihelper.ApiLoginPasswordException;


public class ApiHelper {
    private final String TAG = "API_HELPER";

    private HttpClient client;
    private final int HTTP_CONNECTION_TIMEOUT = 10 * 1000;
    private final int HTTP_RESPONSE_TIMEOUT = 10 * 1000;

    // api server url
    private final String server = "http://192.168.22.10:9001/api";

    // user login
    private String USERNAME;
    private String PASSWORD;
    private final String USER_HEADER = "X-LIBRARY-USERNAME";
    private final String PASS_HEADER = "X-LIBRARY-PASSWORD";

    public ApiHelper() {
        clientSetup();
    }

    public ApiHelper(String username, String password) {
        clientSetup();

        USERNAME = username;
        PASSWORD = password;
    }

    /* user */

    public ApiUser login() {}

    public ApiUser me() {}

    public ApiBook[] getBooks() {}

    public void addBook(String ctrlno) {}

    public void removeBook(String ctrlno) {}

    /* book */

    public ApiBook getBookByCtrlno(String ctrlno) {}

    public ApiBook[] searchBooks(String keyword) {}

    private void clientSetup() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params,
                HTTP_CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, HTTP_RESPONSE_TIMEOUT);

        client = new DefaultHttpClient(params);
    }
}
