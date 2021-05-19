package com.example.moviesapp;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParseJson {
    private HashMap<String,String> parseJsonObject(JSONObject object){
        //Initializing the hash map
        HashMap<String,String> dataList = new HashMap<>();
        try {
            //Getting name from the object
            String name = object.getString("name");
            //Getting latitude from the object
            String latitude = object.getJSONObject("geometry")
                    .getJSONObject("location").getString("lat");
            //Getting longitude from the object
            String longitude = object.getJSONObject("geometry")
                    .getJSONObject("location").getString("lng");
            //Putting all the values in the hashmap
            dataList.put("name", name);
            dataList.put("lat", latitude);
            dataList.put("lng", longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the hash map
        return dataList;
    }

    private List<HashMap<String,String>> parseJsonArray(JSONArray jsonArray){
        //Initializing hashmap list
        List<HashMap<String,String>> dataList = new ArrayList<>();
        for ( int i = 0; i < jsonArray.length(); i++){
            try {
                //Initializing hashmap
                HashMap<String, String> data = parseJsonObject((JSONObject) jsonArray.get(i));
                //Adding data in hashmap
                dataList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //Returning the hash map list
        return dataList;
    }

    public List<HashMap<String, String>> parseResult(JSONObject object){
        //Initializing json array
        JSONArray jsonArray = null;
        //Get result array
        try {
            jsonArray = object.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning array
        return parseJsonArray(jsonArray);
    }
}
