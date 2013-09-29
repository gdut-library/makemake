package com.gdut.library.makemake.apihelper;

import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
import com.gdut.library.makemake.apihelper.ApiLoginActivateException;
import com.gdut.library.makemake.apihelper.ApiLoginPasswordException;


public class ApiHelper {
    private final String TAG = "API_HELPER";

    private HttpClient client;
    private final int HTTP_CONNECTION_TIMEOUT = 10 * 1000;
    private final int HTTP_RESPONSE_TIMEOUT = 10 * 1000;

    // api server url
    private final String server = "http://192.168.1.110:9001/api";

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
        } catch (ApiLoginActivateException e) {
            throw e;
        } catch (ApiLoginPasswordException e) {
            throw e;
        } catch (ApiNotFoundException e) {
            throw e;
        } catch (ApiNetworkException e) {
            throw e;
        }
    }

    public ApiUser me()
        throws ApiLoginActivateException, ApiLoginPasswordException,
               ApiNotFoundException, ApiNetworkException {
        return login();
    }

    //public ApiBook[] getBooks() {}

    //public void addBook(String ctrlno) {}

    //public void removeBook(String ctrlno) {}

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
            throw new ApiNotFoundException();
        } catch (ApiLoginPasswordException e) {
            throw new ApiNetworkException();
        } catch (ApiLoginActivateException e) {
            throw new ApiNetworkException();
        }
    }

    //public ApiBook[] searchBooks(String keyword) {}

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

    private JSONObject handleResponse(HttpResponse response)
        throws ApiLoginActivateException, ApiLoginPasswordException,
               ApiNotFoundException, ApiNetworkException {
        try {
            int code = response.getStatusLine().getStatusCode();
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

            throw new ApiNetworkException();

        } catch (IOException e) {
            throw new ApiNetworkException();
        } catch (JSONException e) {
            throw new ApiNetworkException();
        }
    }
}
