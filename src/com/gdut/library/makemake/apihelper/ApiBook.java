
package com.gdut.library.makemake.apihelper;

import org.json.JSONObject;
import org.json.JSONException;


public class ApiBook {
    public String name;
    public String ctrlno;
    public String isbn;
    public String author;
    public String publisher;
    public String location;
    public int available;
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
