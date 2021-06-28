package com.hit.server;

import com.google.gson.Gson;

public class Response<T> {
    private int statusCode;
    private T body;

    public Response (int statusCode) {
        this.statusCode = statusCode;
    }

    public Response (int statusCode, T body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public T getBody() {
        return body;
    }
}
