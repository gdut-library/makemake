package com.gdut.library.makemake.apihelper;

import com.gdut.library.makemake.apihelper.ApiLoginException;


class ApiLoginPasswordException extends ApiLoginException {
    ApiLoginPasswordException() {
        reason = "密码错误";
    }
}
