package com.gdut.library.makemake.apihelper;


class ApiLoginActivateException extends Exception {
    public String next;

    ApiLoginActivateException(String n) {
        next = n;
    }
}
