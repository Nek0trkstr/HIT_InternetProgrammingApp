package com.hit.server;

import com.google.gson.Gson;

public class Response<T> {
    private int statusCode;
    private String body;

    public Response (int statusCode, T body) {
        this.statusCode = statusCode;
        this.body = new Gson().toJson(body);
    }

}
