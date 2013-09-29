package com.gdut.library.makemake.apihelper;

import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.gdut.library.makemake.apihelper.ApiUser;
import com.gdut.library.makemake.apihelper.ApiBook;
import com.gdut.library.makemake.apihelper.ApiNotFoundException;
import com.gdut.library.makemake.apihelper.ApiNetworkException;
import com.gdut.library.makemake.apihelper.ApiLoginException;
import com.gdut.library.makemake.apihelper.ApiLoginActivateException;
import com.gdut.library.makemake.apihelper.ApiLoginPasswordException;


public class ApiHelper {
    private final String TAG = "API_HELPER";

    private HttpClient client;
    private final int HTTP_CONNECTION_TIMEOUT = 10 * 1000;
    private final int HTTP_RESPONSE_TIMEOUT = 10 * 1000;

    // api server url
    private final String server = "http://beta.youknowmymind.com:1944/api";

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

    /* 用户 */

    public ApiUser login()
        throws ApiLoginActivateException, ApiLoginPasswordException,
               ApiNotFoundException, ApiNetworkException {
        try {
            HttpPost request = authHeader(new HttpPost(apiUrlBuild("user/login")));
            HttpResponse response = client.execute(request);

            return new ApiUser(handleResponse(response).getJSONObject("user"));
        } catch (IOException e) {
            throw new ApiNetworkException();
        } catch (JSONException e) {
            throw new ApiNetworkException();
        } catch (ApiNetworkException e) {
            throw e;
        }
    }

    public ApiUser me()
        throws ApiLoginActivateException, ApiLoginPasswordException,
               ApiNotFoundException, ApiNetworkException {
        return login();
    }

    public ApiBook[] getBooks()
        throws ApiLoginActivateException, ApiLoginPasswordException,
               ApiNotFoundException, ApiNetworkException {
        try {
            HttpGet request = authHeader(new HttpGet(apiUrlBuild("user/books")));
            HttpResponse response = client.execute(request);
            JSONArray ret = handleResponse(response).getJSONArray("books");
            ApiBook[] books = new ApiBook[ret.length()];

            for (int i = 0;i < ret.length();i++) {
                books[i] = new ApiBook(ret.getJSONObject(i));
            }

            return books;
        } catch (IOException e) {
            throw new ApiNetworkException();
        } catch (JSONException e) {
            Log.d(TAG, e.toString());
            throw new ApiNetworkException();
        } catch (ApiNetworkException e) {
            throw e;
        }
    }

    public ApiBook[] addBook(String ctrlno)
        throws ApiLoginActivateException, ApiLoginPasswordException,
               ApiNotFoundException, ApiNetworkException {
        try {
            HttpPut request = authHeader(
                    new HttpPut(apiUrlBuild("user/books/" + ctrlno)));
            HttpResponse response = client.execute(request);
            JSONArray ret = handleResponse(response).getJSONArray("books");
            ApiBook[] books = new ApiBook[ret.length()];

            for (int i = 0;i < ret.length();i++) {
                books[i] = new ApiBook(ret.getJSONObject(i));
            }

            return books;
        } catch (IOException e) {
            throw new ApiNetworkException();
        } catch (JSONException e) {
            throw new ApiNetworkException();
        } catch (ApiNetworkException e) {
            throw e;
        }
    }

    public void removeBook(String ctrlno)
        throws ApiLoginActivateException, ApiLoginPasswordException,
               ApiNotFoundException, ApiNetworkException {
        try {
            HttpDelete request = authHeader(
                    new HttpDelete(apiUrlBuild("user/books/") + ctrlno));
            HttpResponse response = client.execute(request);
            handleResponse(response);
        } catch (IOException e) {
            throw new ApiNetworkException();
        } catch (ApiNetworkException e) {
            throw e;
        }
    }

    /* 书籍 */

    public ApiBook getBookByCtrlno(String ctrlno)
        throws ApiNotFoundException, ApiNetworkException {
        try {
            HttpGet request = new HttpGet(apiUrlBuild("book/" + ctrlno));
            HttpResponse response = client.execute(request);

            return new ApiBook(handleResponse(response).getJSONObject("book"));
        } catch (IOException e) {
            throw new ApiNetworkException();
        } catch (JSONException e) {
            throw new ApiNetworkException();
        } catch (ApiNotFoundException e) {
            throw e;
        } catch (ApiLoginException e) {
            throw new ApiNetworkException();
        }
    }

    public ApiBook[] searchBooks(String keyword)
        throws ApiNotFoundException, ApiNetworkException {
        try {
            // TODO better query parameters builder
            HttpGet request = new HttpGet(apiUrlBuild("book/search?q=" + keyword));
            HttpResponse response = client.execute(request);
            JSONArray ret = handleResponse(response).getJSONArray("books");
            ApiBook[] books = new ApiBook[ret.length()];

            for (int i = 0;i < ret.length();i++) {
                books[i] = new ApiBook(ret.getJSONObject(i));
            }

            return books;
        } catch (IOException e) {
            throw new ApiNetworkException();
        } catch (JSONException e) {
            throw new ApiNetworkException();
        } catch (ApiNotFoundException e) {
            throw e;
        } catch (ApiLoginException e) {
            throw new ApiNetworkException();
        }
    }

    private void clientSetup() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params,
                HTTP_CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, HTTP_RESPONSE_TIMEOUT);

        client = new DefaultHttpClient(params);
    }

    private String apiUrlBuild(String namespace) {
        return server + '/' + namespace;
    }

    // TODO use duck type
    private HttpPost authHeader(HttpPost request)
        throws ApiLoginPasswordException {
        if (USERNAME == null || PASSWORD == null) {
            throw new ApiLoginPasswordException();
        }

        request.setHeader(USER_HEADER, USERNAME);
        request.setHeader(PASS_HEADER, PASSWORD);

        return request;
    }

    private HttpGet authHeader(HttpGet request)
        throws ApiLoginPasswordException {
        if (USERNAME == null || PASSWORD == null) {
            throw new ApiLoginPasswordException();
        }

        request.setHeader(USER_HEADER, USERNAME);
        request.setHeader(PASS_HEADER, PASSWORD);

        return request;
    }

    private HttpPut authHeader(HttpPut request)
        throws ApiLoginPasswordException {
        if (USERNAME == null || PASSWORD == null) {
            throw new ApiLoginPasswordException();
        }

        request.setHeader(USER_HEADER, USERNAME);
        request.setHeader(PASS_HEADER, PASSWORD);

        return request;
    }

    private HttpDelete authHeader(HttpDelete request)
        throws ApiLoginPasswordException {
        if (USERNAME == null || PASSWORD == null) {
            throw new ApiLoginPasswordException();
        }

        request.setHeader(USER_HEADER, USERNAME);
        request.setHeader(PASS_HEADER, PASSWORD);

        return request;
    }

    private JSONObject handleResponse(HttpResponse response)
        throws ApiLoginActivateException, ApiLoginPasswordException,
               ApiNotFoundException, ApiNetworkException {
        try {
            int code = response.getStatusLine().getStatusCode();
            if (code == 204) {
                // no content;
                return null;
            }
            
            String body = EntityUtils.toString(response.getEntity());
            JSONObject o = new JSONObject(body);

            if (code == 403) {
                if (o.has("next")) {
                    throw new ApiLoginActivateException(o.getString("next"));
                }
                throw new ApiLoginPasswordException();
            }
            
            if (code == 404) {
                throw new ApiNotFoundException();
            }

            if (code / 100 == 2) {
                // 2xx
                return o;
            }

            // 其他非 200 状态
            throw new ApiNetworkException();
        } catch (IOException e) {
            throw new ApiNetworkException();
        } catch (JSONException e) {
            throw new ApiNetworkException();
        }
    }
}
