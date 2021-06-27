package com.hit.server;

public class Request<T> {
    private Headers headers;
    private T body;

    public Headers getHeaders() {
        return headers;
    }

    public T getBody() {
        return body;
    }

    public Request (Headers headers, T body) {
        this.headers = headers;
        this.body = body;
    }

    public Request (Headers headers) {
        this.headers = headers;
    }
}
