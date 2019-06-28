package com.upnetix.imagesearch.service.base;

public class Result<T> {

    private T model;
    private String error;

    public Result(T model) {
        this.model = model;
    }

    public Result(String error) {
        this.error = error;
    }

    public T getModel() {
        return model;
    }

    public String getError() {
        return error;
    }
}
