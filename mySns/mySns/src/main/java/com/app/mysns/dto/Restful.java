package com.app.mysns.dto;

import com.google.gson.Gson;

import lombok.Data;

@Data
public class Restful {

    private static String data;
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
    public Restful Data() { return this; }

    public static <T> T ToData(String value, Class<T> clazz) {
        return new Gson().fromJson(value, clazz);
        // return clazz.cast(value);
    }
    
    public static <T> T ToData(Class<T> clazz) {
        return new Gson().fromJson(data, clazz);
        // return clazz.cast(value);
    }
    
    // public Object ToData(Class<T> c) { return new Gson().fromJson(this.data, c) ; }
    // public Object ToData(String data) { return new Gson().fromJson(data, T.getType()) ; }
    public String ToJson() { return new Gson().toJson(data) ; }
    public String ToJson(Object data) { return new Gson().toJson(data) ; }

    //String -> Class new GsonBuilder().create().fromJson(json, classOfT);
    //Class -> String new GsonBuilder().create().toJson(classOfT);

}
