package com.gdut.library.makemake.apihelper;

import org.json.JSONObject;
import org.json.JSONException;


/**
 * 用户记录
 */
public class ApiUser {

    // 用户姓名
    public String name;

    // 用户校园卡号
    public String cardno;

    // 用户学院
    public String faculty;

    // 用户专业
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
