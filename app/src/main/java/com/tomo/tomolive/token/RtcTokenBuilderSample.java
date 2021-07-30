package com.tomo.tomolive.token;


import android.util.Log;

public class RtcTokenBuilderSample {
    static String appId = "39c90bea014541e7abb29fb1a8df6e29";
    static String appCertificate = "7977a81c1aca4bb694715598945bfb29";
    static String channelName = "channel1";
    static String userAccount = "2082341273";
    static int uid = 2082341273;
    static int expirationTimeInSeconds = 3600;

    public static String main(String chennalName) throws Exception {
        RtcTokenBuilder token = new RtcTokenBuilder();
        int timestamp = (int) (System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        String result = token.buildTokenWithUserAccount(appId, appCertificate,
                chennalName, "0", RtcTokenBuilder.Role.Role_Publisher, timestamp);
        System.out.println(result);
        Log.d("liveact", "main: tkn == " + result);
        return result;

       /* result = token.buildTokenWithUid(appId, appCertificate,
                chennalName, uid, RtcTokenBuilder.Role.Role_Publisher, timestamp);
        System.out.println(result);
        Log.d("TAG", "main: tkn22===  "+result);*/
    }
}
