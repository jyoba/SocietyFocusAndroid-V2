package com.zircon.app.model;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by jyotishman on 13/07/17.
 */

public class Emergency {

    public String title;
    public String number;

    public Emergency(String title, String number) {
        this.title = title;
        this.number = number;
    }

    public static ArrayList<Emergency> getEmergency(String emergencyString) {
        String[] strings = emergencyString.split(",");
        ArrayList<Emergency> emergencies = new ArrayList<>();
        Emergency emergency;
        for (String s:strings){
            emergency = new Emergency(s.split("\\|")[0],s.split("\\|")[1]);
            emergencies.add(emergency);
        }
        return emergencies;
    }
}
