package com.upnetix.imagesearch.service.base;

public interface ICallback<T> {

    void onSuccess(T model);

    void onError(String error);
}
