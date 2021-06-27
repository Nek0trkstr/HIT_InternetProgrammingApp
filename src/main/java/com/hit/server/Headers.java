package com.hit.server;

public class Headers {
    private String controller;
    private String action;
    private String objectName;

    public Headers(String controller, String action) {
        this.controller = controller;
        this.action = action;
    }

    public Headers(String controller, String action, String objectName) {
        this.action = action;
        this.controller = controller;
        this.objectName = objectName;
    }

    public String getAction() {
        return action;
    }

    public String getController() {
        return controller;
    }

    public String getObjectName() {
        return objectName;
    }
}
