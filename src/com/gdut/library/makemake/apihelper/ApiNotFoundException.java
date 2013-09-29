package com.gdut.library.makemake.apihelper;

import com.gdut.library.makemake.apihelper.ApiNetworkException;


class ApiNotFoundException extends ApiNetworkException {
    ApiNotFoundException() {
        reason = "未找到请求资源";
    }
}
