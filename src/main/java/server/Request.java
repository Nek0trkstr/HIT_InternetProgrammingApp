package server;

public class Request<T> {
    private Headers headers;
    private T body;

    public Headers getHeaders() {
        return headers;
    }

    public T getBody() {
        return body;
    }

    class Headers {
        private String action;
        private String controller;

        public Headers(String action, String controller) {
            this.action = action;
            this.controller = controller;
        }

        public String getAction() {
            return action;
        }

        public String getController() {
            return controller;
        }
    }
}
