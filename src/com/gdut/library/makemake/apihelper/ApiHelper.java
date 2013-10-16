package com.gdut.library.makemake.apihelper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import android.util.Log;

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
import java.net.URLEncoder;

import com.gdut.library.makemake.apihelper.ApiUser;
import com.gdut.library.makemake.apihelper.ApiBook;
import com.gdut.library.makemake.apihelper.ApiNotFoundException;
import com.gdut.library.makemake.apihelper.ApiNetworkException;
import com.gdut.library.makemake.apihelper.ApiLoginException;
import com.gdut.library.makemake.apihelper.ApiLoginActivateException;
import com.gdut.library.makemake.apihelper.ApiLoginPasswordException;


/**
 * 提供 api 调用的接口
 */
public class ApiHelper {
    private final String TAG = "API_HELPER";

    private HttpClient client;
    private final int HTTP_CONNECTION_TIMEOUT = 10 * 1000;
    private final int HTTP_RESPONSE_TIMEOUT = 10 * 1000;

    // api 服务器地址
    private final String server = "http://beta.youknowmymind.com:1944/api";

    // 用户登录相关信息
    private String USERNAME;
    private String PASSWORD;
    private final String USER_HEADER = "X-LIBRARY-USERNAME";
    private final String PASS_HEADER = "X-LIBRARY-PASSWORD";

    /**
     * 默认调用，可访问
     *
     * - `书籍信息`
     * - `查询书籍`
     *
     * 接口
     */
    public ApiHelper() {
        clientSetup();
    }

    /**
     * 带用户权限调用，可访问
     *
     * - `用户登录`
     * - `用户信息`
     * - `用户书单`
     * - `书籍信息`
     * - `查询书籍`
     *
     * 接口
     *
     * @param username 登录用户名
     * @param password 登录密码
     */
    public ApiHelper(String username, String password) {
        clientSetup();

        USERNAME = username;
        PASSWORD = password;
    }

    /* 用户 */

    /**
     * 用户登录接口
     *
     * @return `ApiUser` 用户信息
     * @throws `ApiLoginActivateException` 用户需要激活
     * @throws `ApiLoginPasswordException` 用户密码错误
     * @throws `ApiNetworkException` 网络错误
     */
    public ApiUser login()
        throws ApiLoginActivateException, ApiLoginPasswordException,
               ApiNetworkException {
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

    /**
     * 用户信息接口
     *
     * @return `ApiUser` 用户信息
     * @throws `ApiLoginActivateException` 用户需要激活
     * @throws `ApiLoginPasswordException` 用户密码错误
     * @throws `ApiNetworkException` 网络错误
     */
    public ApiUser me()
        throws ApiLoginActivateException, ApiLoginPasswordException,
               ApiNetworkException {
        return login();
    }

    /**
     * 获取用户借书单接口
     *
     * @return `ApiBook[]` 用户借书单列表
     * @throws `ApiLoginActivateException` 用户需要激活
     * @throws `ApiLoginPasswordException` 用户密码错误
     * @throws `ApiNetworkException` 网络错误
     */
    public ApiBook[] getBooks()
        throws ApiLoginActivateException, ApiLoginPasswordException,
               ApiNetworkException {
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

    /**
     * 用户借书单添加书籍接口
     *
     * @param ctrlno 书籍控制号
     * @return `ApiBook[]` 用户借书单列表
     * @throws `ApiLoginActivateException` 用户需要激活
     * @throws `ApiLoginPasswordException` 用户密码错误
     * @throws `ApiNotFoundException` 添加书籍不存在
     * @throws `ApiNetworkException` 网络错误
     */
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

    /**
     * 用户借书单移除书籍接口
     *
     * @param ctrlno 书籍控制号
     * @throws `ApiLoginActivateException` 用户需要激活
     * @throws `ApiLoginPasswordException` 用户密码错误
     * @throws `ApiNotFoundException` 移除书籍不存在
     * @throws `ApiNetworkException` 网络错误
     */
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

    /**
     * 根据控制号获取书籍信息接口
     *
     * @param ctrlno 书籍控制号
     * @return `ApiBook` 书籍信息
     * @throws `ApiNotFoundException` 请求书籍不存在
     * @throws `ApiNetworkException` 网络错误
     */
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

    /**
     * 根据任意关键字查询书籍接口
     *
     * @param keyword 关键字
     * @return `ApiBook[]` 书籍信息列表
     * @throws `ApiNotFoundException` 请求书籍不存在
     * @throws `ApiNetworkException` 网络错误
     */
    public ApiBook[] searchBooks(String keyword)
        throws ApiNotFoundException, ApiNetworkException {
        try {
            // TODO better query parameters builder
            // TODO fix parameters encoding
            HttpGet request = new HttpGet(apiUrlBuild("book/search?q=" +
                        encodeParameters(keyword)));
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

    /**
     * 初始化 http client
     */
    private void clientSetup() {
        // 设置超时
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params,
                HTTP_CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, HTTP_RESPONSE_TIMEOUT);

        client = new DefaultHttpClient(params);
    }

    /**
     * 构建 api url
     *
     * TODO 使用 Uri 来构建
     * ref: http://stackoverflow.com/questions/3286067/url-encoding-in-android
     *
     * @param namespace 请求部分
     * @return api url
     */
    private String apiUrlBuild(String namespace) {
        return server + '/' + namespace;
    }

    private String encodeParameters(String raw) {
        try {
            return URLEncoder.encode(raw, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return raw;
        }
    }

    /**
     * 向请求加入验证头字段
     *
     * @param request 请求
     * @return request 请求
     * @throws `ApiLoginPasswordException` 用户名密码不能为空
     */
    private HttpPost authHeader(HttpPost request)
        throws ApiLoginPasswordException {
        if (USERNAME == null || PASSWORD == null) {
            throw new ApiLoginPasswordException("用户名和密码不能为空");
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

    /**
     * 对响应进行处理
     *
     * @param response 响应
     * @return `JSONObject` 对象
     * @throws `ApiLoginActivateException` 用户需要激活
     * @throws `ApiLoginPasswordException` 用户密码错误
     * @throws `ApiNotFoundException` 书籍不存在
     * @throws `ApiNetworkException` 网络错误
     */
    private JSONObject handleResponse(HttpResponse response)
        throws ApiLoginActivateException, ApiLoginPasswordException,
               ApiNotFoundException, ApiNetworkException {
        try {
            int code = response.getStatusLine().getStatusCode();
            if (code == 204) {
                // no content 直接返回;
                return null;
            }
            
            String body = EntityUtils.toString(response.getEntity());
            JSONObject o = new JSONObject(body);

            if (code == 403) {
                // 用户鉴权错误
                // TODO 测试其他情况
                if (o.has("next")) {
                    throw new ApiLoginActivateException(o.getString("next"));
                }
                throw new ApiLoginPasswordException();
            }
            
            if (code == 404) {
                throw new ApiNotFoundException();
            }

            if (code / 100 == 2) {
                // 2xx 成功状态
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
