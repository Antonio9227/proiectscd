package com.anto.track;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.StrictMode;

import com.anto.track.api.Api;
import com.anto.track.api.dto.PositionDto;
import com.anto.track.api.dto.RegisterPhoneDto;
import com.anto.track.api.dto.RegisterPhoneResponseDto;
import com.anto.track.api.dto.RequestAccessTokenDto;

import java.util.UUID;

public class TrackService {
    String deviceId;
    UUID phoneId;
    SharedPreferences preferences;

    private Api api;

    public static TrackService getInstance(String deviceId, SharedPreferences sharedPreferences) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (instance == null) {
            instance = new TrackService(deviceId, sharedPreferences);
        }
        return instance;
    }

    public static TrackService getInstance() {
        return instance; // rip if null
    }

    private static TrackService instance;

    private TrackService(String deviceId, SharedPreferences sharedPreferences) {
        this.deviceId = encrypt(deviceId);
        preferences = sharedPreferences;
        api = new Api();

        if(isRegistered()){
            phoneId = UUID.fromString(preferences.getString(Constants.UUID_KEY,null));
        }
    }

    // pretend this does something fancy.
    private String encrypt(String deviceId) {
        byte[] bytes = deviceId.getBytes();

        int cks = 0;
        for (int i = 0; i < deviceId.length(); i++)
            cks += bytes[i];
        return deviceId + "x" + cks % Constants.MAGIC_NUMBER;
    }


    public boolean isRegistered() {
        return !preferences.getString(Constants.UUID_KEY, "").isEmpty();
    }

    @SuppressLint("ApplySharedPref")
    public void register() {
        RegisterPhoneResponseDto phone = api.registerPhone(RegisterPhoneDto.builder()
                .imei(deviceId).build());

        if (phone == null) {
            return;
        }
        phoneId = phone.getUuid();
        preferences.edit().putString(Constants.UUID_KEY, phone.getUuid().toString()).commit();
    }

    public void submitPosition(double longitude, double latitude){
        if(!isRegistered())
        {
            //throw new Exception("How the fk am I supposed to submit your position if your stupid ass forgot to register????");
            return;
        }

        api.submitPosition(PositionDto.builder().latitude(latitude).longitude(longitude).phoneId(phoneId).build());

    }

    public void openBrowser(){
        api.redirectToWeb(RequestAccessTokenDto.builder()
                .imei(deviceId)
                .uuid(phoneId)
                .build());
    }
}
