package com.anto.track.api;

import android.content.Intent;
import android.net.Uri;

import com.anto.track.Constants;
import com.anto.track.MainActivity;
import com.anto.track.R;
import com.anto.track.api.dto.DeletePhoneDto;
import com.anto.track.api.dto.PositionDto;
import com.anto.track.api.dto.RegisterPhoneDto;
import com.anto.track.api.dto.RegisterPhoneResponseDto;
import com.anto.track.api.dto.RequestAccessTokenDto;
import com.anto.track.api.dto.RequestAccessTokenResponseDto;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Api {

    OkHttpClient client;
    String base;
    Gson gson;


    public Api() {
        client = new OkHttpClient();
        base = Constants.API_ENDPOINT;
        gson = new Gson();
    }

    private ApiResponse request(String method, String url, String content) {
        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), content);
        Request.Builder request = new Request.Builder()
                .method(method, body)
                .url(base + url);

        try (Response response = client.newCall(request.build()).execute()) {
            if (!response.isSuccessful()) {
                Snackbar.make(MainActivity.currentInstance.findViewById(R.id.footer), "DEBUG: " + method + ":" + url + " -> " + response.code(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            String stringBody = "";
            ResponseBody responseBody = response.body();
            if(responseBody!=null)
                stringBody = responseBody.string();
            return ApiResponse.builder().statusCode(response.code()).body(stringBody).build();
        } catch (IOException e) {
            return ApiResponse.builder().statusCode(500).build();
        }
    }

    public RegisterPhoneResponseDto registerPhone(RegisterPhoneDto phone) {
        ApiResponse r = request("POST", "phone/", gson.toJson(phone));
        if (!r.isSuccessful()) {
            return null;
        }

        return gson.fromJson(r.getBody(), RegisterPhoneResponseDto.class);
    }

    public boolean deleteData(DeletePhoneDto phone) {
        return request("DELETE", "phone/", gson.toJson(phone)).isSuccessful();
    }

    public void submitPosition(PositionDto position) {
        request("POST", "position/", gson.toJson(position));
    }

    public void redirectToWeb(RequestAccessTokenDto req){
        ApiResponse token = request("POST","phone/token",gson.toJson(req));

        RequestAccessTokenResponseDto resp = gson.fromJson(token.getBody(), RequestAccessTokenResponseDto.class);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.ENDPOINT +
                "?token=" + resp.getToken().toString() //+
                //"&resourceId=" + req.getUuid()
        ));
        MainActivity.currentInstance.startActivity(browserIntent);
    }
}
