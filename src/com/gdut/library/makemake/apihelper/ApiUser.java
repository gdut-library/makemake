package com.gdut.library.makemake.apihelper;

import org.json.JSONObject;
import org.json.JSONException;

public class ApiUser {
    public String name;
    public String cardno;
    public String faculty;
    public String major;

    ApiUser() {}

    ApiUser(JSONObject infos)
        throws JSONException {
        try {
            name = infos.getString("name");
            cardno = infos.getString("cardno");
            faculty = infos.getString("faculty");
            major = infos.getString("major");
        } catch (JSONException e) {
            throw e;
        }
    }
}
