package com.essece.networkutils;

import android.R.integer;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/30.
 */

public class MyHttpClient implements Runnable{
	public static final int uriconnection_post=1;
	public static final int uriconnection_get=2;
	private static final int resultCode_succed=3;
	private static final int resultCode_failed=4;
	private static final int resultCode_exception=5;
    private String urlPath;
    private String  paramStremString;
    private int defaultType=uriconnection_get;
    private int httpType=defaultType;
    private  String result="";
    private NetworkResponseInterface networkResponseInterface;
    private Map<String,String>paramMap=new HashMap<String,String>();
    private int requestCode;
    public MyHttpClient(String urlPath,Map<String,String>paramMap,int requestCode){
        this.paramMap=paramMap;
        this.urlPath=urlPath;
        this.requestCode = requestCode;
    }
	@Override
	public void run() {
		
		switch (httpType) {
		case uriconnection_post:
			postURLConnection();
			break;
      case uriconnection_get:	
    		getURLConnection();
			break;
		default:
			break;
		}
	}
    private  void getURLConnection(){
           HttpURLConnection connection = null;
            try {
                if (paramMap!=null){
                    boolean first=true;
                    for (String key:paramMap.keySet()) {
                        String value= paramMap.get(key);
                        if(first){
                            urlPath=urlPath+"?"+key+"="+value;
                            first=false;
                        }else{
                            urlPath=urlPath+"&"+key+"="+value;
                        }
                    }
                }
                URL url = new URL(urlPath);
                Log.i("test", "urlPath : "+urlPath);
                connection = (HttpURLConnection) url.openConnection();
                // 设置请求方法，默认是GET
                connection.setRequestMethod("GET");

                // 设置字符集
                connection.setRequestProperty("Charset", "UTF-8");
                // 设置文件类型
                connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
                connection.setConnectTimeout(1000*30);
                connection.setReadTimeout(1000*60);
                if (connection.getResponseCode() == 200) {
                    InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                    BufferedReader bufferReader= new BufferedReader(inputStreamReader);
                    String inputLine="";
                    while ((inputLine=bufferReader.readLine())!=null){
                        result = result+inputLine+"\n";
                    }
                    resonseResult(resultCode_succed, result);
                    Log.i("test", "result : "+result);
                
                }else{
                	resonseResult(resultCode_failed, result);
                    Log.i("test", "result code: "+connection.getResponseCode());
                }
            } catch (IOException e) {
                e.printStackTrace();
                   resonseResult(resultCode_exception, e.getMessage());
                Log.i("test", "result e: "+e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

    private void postURLConnection(){
        try {
        	 // 设置自定义参数
            if (paramMap!=null){
                boolean first=true;
                for (String key:paramMap.keySet()) {
                    String value= paramMap.get(key);
                    if(first){
                        urlPath=urlPath+"?"+key+"="+value;
                        first=false;
                    }else{
                        urlPath=urlPath+"&"+key+"="+value;
                    }
                }
            }
            URL url = new URL(urlPath);
            Log.i("test", "urlPath: "+urlPath);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            // 设置请求方式
            urlConnection.setRequestMethod("POST");
            //设置允许向连接中写入数据
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(1000*60);
            urlConnection.setReadTimeout(1000*90);
            // 设置容许输出
            urlConnection.setDoOutput(true);
            //禁止使用缓存
            urlConnection.setUseCaches(false);
            //自动执行HTTP重定向
            urlConnection.setInstanceFollowRedirects(true);
            // 设置编码格式
            urlConnection.setRequestProperty("Charset", "UTF-8");
            //设置内容类型
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            //获取输出流
            if (paramStremString!=null){
                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                Log.i("test", "param stream: "+paramStremString);
                byte[] requestStringBytes = paramStremString.getBytes("utf-8");
                dataOutputStream.write(requestStringBytes);
                dataOutputStream.flush();
                dataOutputStream.close();
            }
            if (urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufferReader= new BufferedReader(inputStreamReader);
                String inputLine="";
                while ((inputLine=bufferReader.readLine())!=null){
                    result = result+inputLine+"\n";
                }
                resonseResult(resultCode_succed, result);
                Log.i("test", "result: "+result);
            }else{
                resonseResult(resultCode_failed, result);
                Log.i("test", "result code: "+urlConnection.getResponseCode());
            }
        } catch (MalformedURLException e) {
            resonseResult(resultCode_exception, e.getMessage());
            e.printStackTrace();
            Log.i("test", "result:MalformedURLException "+e.getMessage());
        } catch (IOException e) {
            Log.i("test", "result:IOException "+e.getMessage());
            e.printStackTrace();
        }

    }


	public void setHttpType(int httpType) {
		if (httpType!=uriconnection_get&&httpType!=uriconnection_post) {
			httpType=defaultType;
			return;
		}
		this.httpType = httpType;
	}
	public NetworkResponseInterface getNetworkResponseInterface() {
		return networkResponseInterface;
	}
	public void setNetworkResponseInterface(NetworkResponseInterface networkResponseInterface) {
		this.networkResponseInterface = networkResponseInterface;
	}
	
	private void resonseResult(int resultCode,String responseJson){
		if (networkResponseInterface!=null) {
			switch (resultCode) {
			case resultCode_succed:
				networkResponseInterface.networkSucceed(requestCode, responseJson);
				break;
            case resultCode_failed:
            	networkResponseInterface.networkFailed(requestCode, responseJson);
				break;
            case resultCode_exception:
            	networkResponseInterface.networkException(requestCode, responseJson);
	            break;

			default:
				break;
			}
		}
	}
	public String getParamStremString() {
		return paramStremString;
	}
	public void setParamStremString(String paramStremString) {
		this.paramStremString = paramStremString;
	}



    public static  String UploadPicture(String urlString,String JsonString){
        try {
            // 设置自定义参数
            URL url = new URL(urlString);
            HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
            // 设置请求方式
            urlConnection.setRequestMethod("POST");
            //设置允许向连接中写入数据
            urlConnection.setDoInput(true);
            // 设置容许输出
            urlConnection.setDoOutput(true);
            //禁止使用缓存
            urlConnection.setUseCaches(false);
            //自动执行HTTP重定向
            urlConnection.setInstanceFollowRedirects(true);
            // 设置编码格式
            urlConnection.setRequestProperty("Charset", "UTF-8");
            //设置内容类型
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            //获取输出流
                DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
                byte[] requestStringBytes = JsonString.getBytes("utf-8");
                dataOutputStream.write(requestStringBytes);
                dataOutputStream.flush();
                dataOutputStream.close();
            if (urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader bufferReader= new BufferedReader(inputStreamReader);
                String inputLine="";
                StringBuffer resultBuffer=new StringBuffer();
                while ((inputLine=bufferReader.readLine())!=null){
                    resultBuffer.append(inputLine+"\n");
                }
                Log.i("test", "result: "+resultBuffer.toString());
                return  resultBuffer.toString();
            }else{
                Log.i("test", "result code: "+urlConnection.getResponseCode());
            }
        } catch (MalformedURLException e) {

            e.printStackTrace();
            Log.i("test", "result:MalformedURLException "+e.getMessage());
        } catch (IOException e) {
            Log.i("test", "result:IOException "+e.getMessage());
            e.printStackTrace();
        }
        return -1+"";
    }
}
