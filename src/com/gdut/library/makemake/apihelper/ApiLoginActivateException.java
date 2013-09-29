package com.gdut.library.makemake.apihelper;

import com.gdut.library.makemake.apihelper.ApiLoginException;


/**
 * 帐户未激活异常
 */
class ApiLoginActivateException extends ApiLoginException {

    // 帐户激活地址
    public String next;

    ApiLoginActivateException(String n) {
        reason = "帐户需要激活";
        next = n;
    }
}
