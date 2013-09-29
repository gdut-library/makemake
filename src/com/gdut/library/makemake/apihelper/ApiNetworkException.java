package com.gdut.library.makemake.apihelper;


/**
 * 网络异常
 */
class ApiNetworkException extends Exception {
    public String reason;

    ApiNetworkException() {
        reason = "网络错误";
    }

    ApiNetworkException(String message) {
        reason = message;
    }
}
