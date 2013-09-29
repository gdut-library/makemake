package com.gdut.library.makemake.apihelper;

import com.gdut.library.makemake.apihelper.ApiLoginException;


class ApiLoginActivateException extends ApiLoginException {
    public String next;
    public String reason;

    ApiLoginActivateException(String n) {
        reason = "帐户需要激活";
        next = n;
    }
}
