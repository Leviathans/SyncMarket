package org.example.binance;

import java.util.List;

public class RequestBody {
    private String method = "SUBSCRIBE";
    private List<String> params;
    private Long id;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RequestBody{" +
                "method='" + method + '\'' +
                ", params=" + params +
                ", id=" + id +
                '}';
    }
}
