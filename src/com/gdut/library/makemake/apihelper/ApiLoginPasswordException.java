package com.gdut.library.makemake.apihelper;

import com.gdut.library.makemake.apihelper.ApiLoginException;


/**
 * 登录密码错误异常
 */
class ApiLoginPasswordException extends ApiLoginException {
    ApiLoginPasswordException() {
        reason = "密码错误";
    }
}
