package com.essece.networkutils;

public interface NetworkResponseInterface {
 public void networkSucceed(int code, String succeedJson);
 public void networkFailed(int code, String failedJson);
 public void networkException(int code, String exceptionMessage);
}
