
package com.gdut.library.makemake.apihelper;

import org.json.JSONObject;
import org.json.JSONException;


/**
 * 书籍记录
 */
public class ApiBook {

    // 书籍标题
    public String name;

    // 书籍控制号
    public String ctrlno;

    // 书籍 isbn
    public String isbn;

    // 书籍作者
    public String author;

    // 书籍出版社
    public String publisher;

    // 书籍位置
    public String location;

    // 书籍剩余数量
    public int available;

    // 书籍总数
    public int total;

    public ApiBook() {}

    public ApiBook(JSONObject infos)
        throws JSONException {
        try {
            name = infos.getString("name");
            ctrlno = infos.getString("ctrlno");
            isbn = infos.optString("isbn");
            author = infos.optString("author");
            publisher = infos.optString("publisher");
            available = infos.optInt("available");
            total = infos.optInt("total");
            location = infos.optString("location");

            // 兼容两种不同的结果
            JSONObject locationInfos = infos.optJSONObject("location");
            if (locationInfos != null) {
                available = locationInfos.getInt("available");
                total = locationInfos.getInt("total");
                location = locationInfos.getString("location");
            }
        } catch (JSONException e) {
            throw e;
        }
    }
}
