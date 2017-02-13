package com.OtherUtils;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/1/13.
 */

public class MapToJson {
    public  static  String MapToJson(HashMap<String,Object> hashMap){
        if (hashMap!=null){
            StringBuffer jsonBuffer=new StringBuffer();
            jsonBuffer.append("{");
            boolean isFirst=true;
            for (String key:hashMap.keySet()){
                if (isFirst){
                    isFirst=false;
                }else{
                    jsonBuffer.append(",");
                }
                jsonBuffer.append("\""+key+"\":");
                Object value=hashMap.get(key);
               if (value instanceof Integer){
                   jsonBuffer.append(hashMap.get(key));
               }else {
                   jsonBuffer.append("\""+hashMap.get(key)+"\"");
               }

            }
            jsonBuffer.append("}");
            return  jsonBuffer.toString();
        }
        return null;
    }
}
