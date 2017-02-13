package com.essence.UploadUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;


public class WebServiceTest {

	 public static String Webservice(String xml, byte[] bytes) throws Exception {
	     String strRead = Base64.encodeToString(bytes, Base64.DEFAULT);
				
        String  SERVICE_URL = "http://pm.essenceimc.com/Service/android/User.asmx";
        String meothd="UploadStoreImgBase64";
       
         WebServiceParam wsParam = new WebServiceParam(meothd,SERVICE_URL);
         wsParam.setUrl(SERVICE_URL);
         Map<String, String> requestProperty= new HashMap<String, String>();
         requestProperty.put("Accept-Charset", "utf-8");
         requestProperty.put("Content-Type", "application/soap+xml; charset=utf-8");
         
         Map<String, String> param = new HashMap<String, String>();
         wsParam.setParam(param);
         param.put("xml", xml);
         param.put("base64Str", strRead);
         wsParam.setRequestProperty(requestProperty);
         InputStream inputStream =HttpPost(wsParam);
         String resultString="";
       //  Log.i("pictrue_up", "WebServiceTest resultString"+resultString);
         XmlPullParser parser = Xml.newPullParser();
         parser.setInput(inputStream, "UTF-8");
         int eventType = parser.getEventType();  
         while (eventType != XmlPullParser.END_DOCUMENT) {  
             switch (eventType) {  
                 case XmlPullParser.START_DOCUMENT:  
                     break;  
                 case XmlPullParser.START_TAG:  
                     if (parser.getName().equals(meothd+"Result")) { 
                    	 eventType = parser.next();
                    	 resultString= parser.getText(); 
                     } 
                     break;  
                 case XmlPullParser.END_TAG:   
                     break;  
             }  
             eventType = parser.next();  
         }  
         inputStream.close();
        return  resultString;
  }
  static int count=0;
  public static InputStream HttpPost(WebServiceParam param) throws Exception{
         URL url = new URL(param.getUrl());
         URLConnection urlCon = url.openConnection();
         urlCon.setConnectTimeout(param.getConnectTimeout()); 
         urlCon.setReadTimeout(param.getReadTimeout());  
         
         HttpURLConnection httpURLConnection = (HttpURLConnection) urlCon;              
         httpURLConnection.setDoOutput(true);
         httpURLConnection.setRequestMethod("POST");
         
         Map<String, String> requestProperty=param.getRequestProperty();
         for(String  key : requestProperty.keySet()){
                String value = requestProperty.get(key );
                httpURLConnection.setRequestProperty(key, value);
         }
         
    StringBuffer htmlbody = new StringBuffer();
   htmlbody.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
   htmlbody.append("<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">");
   htmlbody.append("<soap12:Body>");
   htmlbody.append("<"+param.getMethodName()+" xmlns=\"http://tempuri.org/\">");
  
   Map<String, String> methodParam=param.getParam();
         for(String  key : methodParam.keySet()){
                String value = methodParam.get(key );
                htmlbody.append("<"+key+"><![CDATA["+value+"]]></"+key+">");
         }
   //htmlbody.append("<cfgId>7181</cfgId>");
  
   htmlbody.append("</"+param.getMethodName()+">");
   htmlbody.append("</soap12:Body>");
   htmlbody.append("</soap12:Envelope>");
         
  
  // Log.i("pictrue_up", "htmlbody"+htmlbody.toString());
   OutputStream outputStream = null;
   OutputStreamWriter outputStreamWriter = null;
   InputStream inputStream = null;
  // InputStreamReader inputStreamReader = null;
  // BufferedReader reader = null;
  // StringBuffer resultBuffer = new StringBuffer();
 //  String tempLine = null;
  
   try {
       outputStream = httpURLConnection.getOutputStream();
       outputStreamWriter = new OutputStreamWriter(outputStream);
       outputStreamWriter.write(htmlbody.toString());
       outputStreamWriter.flush();
       count =count+1;
       Log.i("pictrue_begain", "web "+count);
       if (httpURLConnection.getResponseCode() >= 300) {
           throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
       }
      
       inputStream = httpURLConnection.getInputStream();
//       inputStreamReader = new InputStreamReader(inputStream,"utf-8");
//       reader = new BufferedReader(inputStreamReader);
//      
//       while ((tempLine = reader.readLine()) != null) {
//           resultBuffer.append(tempLine);
//       }
      
   } finally {
      
       if (outputStreamWriter != null) {
           outputStreamWriter.close();
       }
      
       if (outputStream != null) {
           outputStream.close();
       }
      
//       if (reader != null) {
//           reader.close();
//       }
//      
//       if (inputStreamReader != null) {
//           inputStreamReader.close();
//       }
      
//       if (inputStream != null) {
//           inputStream.close();
//       }
      
   }
  // return (resultBuffer.toString());
   return inputStream;
  }

}
