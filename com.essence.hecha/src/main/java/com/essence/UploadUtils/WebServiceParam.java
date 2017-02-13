package com.essence.UploadUtils;

import java.util.Map;

public class WebServiceParam {
	 public WebServiceParam(String methodName,String url){
         this.methodName=methodName;
         this.url=url;
  }
  
  private String methodName;
  private int connectTimeout=1000*10;
  private int readTimeout=1000*60;
  private String url;
  
  private Map RequestProperty;
  private Map<String, String> param;
  
  public Map getRequestProperty() {
         return RequestProperty;
  }
  public void setRequestProperty(Map requestProperty) {
         RequestProperty = requestProperty;
  }
  /**
   * @return the methodName
   */
  public String getMethodName() {
         return methodName;
  }
  /**
   * @param methodName the methodName to set
   */
  public void setMethodName(String methodName) {
         this.methodName = methodName;
  }
  /**
   * @return the connectTimeout
   */
  public int getConnectTimeout() {
         return connectTimeout;
  }
  /**
   * @param connectTimeout the connectTimeout to set
   */
  public void setConnectTimeout(int connectTimeout) {
         this.connectTimeout = connectTimeout;
  }
  /**
   * @return the url
   */
  public String getUrl() {
         return url;
  }
  /**
   * @param url the url to set
   */
  public void setUrl(String url) {
         this.url = url;
  }
  /**
   * @return the readTimeout
   */
  public int getReadTimeout() {
         return readTimeout;
  }
  /**
   * @param readTimeout the readTimeout to set
   */
  public void setReadTimeout(int readTimeout) {
         this.readTimeout = readTimeout;
  }
  /**
   * @return the param
   */
  public Map<String, String> getParam() {
         return param;
  }
  /**
   * @param param the param to set
   */
  public void setParam(Map param) {
         this.param = param;
  }
}
