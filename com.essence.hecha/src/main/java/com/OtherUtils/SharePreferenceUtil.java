package com.OtherUtils;

import android.content.Context;
import android.content.SharedPreferences;

import com.com.essence.selfclass.UserInfo;

/**
 * Created by Administrator on 2017/1/13.
 */

public class SharePreferenceUtil {
    public static  final  String sharePreference_userinfo="userInfo";
    public static  final  String user_name="username";
    public static  final  String user_password="password";
    public static  final  String user_id="userid";
    public static  final  String user_nickname="userNickName";
    public static void saveUserInfo(Context context, UserInfo userInfo){
        SharedPreferences sharedPreferences =context.getSharedPreferences(sharePreference_userinfo,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString(user_name,userInfo.getUserName());
        editor.putString(user_password,userInfo.getUserPassword());
        editor.putString(user_nickname,userInfo.getUserNickName());
        editor.putInt(user_id,userInfo.getUserId());
        editor.commit();
    }

    public static UserInfo getUserInfo(Context context){
        SharedPreferences sharedPreferences =context.getSharedPreferences(sharePreference_userinfo,Context.MODE_PRIVATE);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(sharedPreferences.getString(user_name,null));
        userInfo.setUserPassword(sharedPreferences.getString(user_password,null));
        userInfo.setUserNickName(sharedPreferences.getString(user_nickname,null));
        userInfo.setUserId(sharedPreferences.getInt(user_id,-1));
        return  userInfo;
    }

    public  static  int getUserId(Context context){
        SharedPreferences sharedPreferences =context.getSharedPreferences(sharePreference_userinfo,Context.MODE_PRIVATE);
        int userId=sharedPreferences.getInt(user_id,-1);
        return  userId;
    }
    public static void clearUserInfo(Context context){
        SharedPreferences sharedPreferences =context.getSharedPreferences(sharePreference_userinfo,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString(user_name,"");
        editor.putString(user_password,"");
        editor.putString(user_nickname,"");
        editor.putInt(user_id,-1);
        editor.commit();
    }

}
