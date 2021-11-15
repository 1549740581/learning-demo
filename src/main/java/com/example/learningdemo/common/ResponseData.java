package com.example.learningdemo.common;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
public class ResponseData<T> implements Serializable {

    String requestId;

    String errorCode;

    String errorMsg;

    T data;

    Boolean success;

    public static <T> ResponseData<T> success(T data) {
        return new ResponseData<T>().setData(data).setSuccess(true);
    }

    public static <T> ResponseData<T> fail(String errorCode, String errorMsg) {
        return new ResponseData<T>().setData(null).setSuccess(false).setErrorCode(errorCode).setErrorMsg(errorMsg);
    }

}
