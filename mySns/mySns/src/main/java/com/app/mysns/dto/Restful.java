package com.app.mysns.dto;

import lombok.Data;

@Data
public class Restful {

    private String data;
    private Boolean isError = false;
    private String reason;


    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public Boolean getIsError() { return isError; }
    public void setIsError(Boolean isError) { this.isError = isError; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Restful Error(String reason) { this.reason = reason; this.isError = true; return this; }
    public Restful Data(String data) { this.data = data; return this; }

}
