package com.codebase.inmateapp.interfaces;

public interface ApiCallback<T> {
    void onSuccess(T result);
    void onError(String error);
}
