package com.example.numberunlock;

import com.example.numberunlock.pojo.RequestObject;
import com.example.numberunlock.pojo.ResponseObject;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.*;

// 废弃
public class OkHttpUtil {

    public static ResponseObject post(String url, RequestObject requestObject){
        ResponseObject responseObject = new ResponseObject();
        OkHttpClient client = new OkHttpClient();

        Gson gson = new Gson();
        String json = gson.toJson(requestObject);
        RequestBody body = FormBody.create(MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);

        // 同步请求
//        Response response = call.execute();
//        if (response.isSuccessful()) {
//            String resultJson = response.body() != null ? response.body().string() : null;
//            System.out.println("返回值： "+resultJson);
//            responseObject = gson.fromJson(resultJson, ResponseObject.class);
//        }
//        else {
//            responseObject.setResult(-1);
//        }
//        return responseObject;

//        // 异步请求
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                System.out.println("连接失败！");
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if(response.isSuccessful()){
//                    String resultJson = response.body().string();
//                    System.out.println("返回值： "+resultJson);
//                    responseObject = gson.fromJson(resultJson, ResponseObject.class);
//                    //处理UI需要切换到UI线程处理
//                }
//            }
//        });
        return responseObject;
    }

}
