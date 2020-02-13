package com.filipve1994.personalexercisespringbootcrudapi.common.utils;

import com.google.gson.Gson;
import java.io.StringReader;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;

@SuppressWarnings("unused")
public class JsonUtil {

    public static JsonObject convertToJsonObject(String jsonString) {
        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonObject jsonObject = reader.readObject();
        reader.close();

        return jsonObject;
    }

    public static JsonObject createJsonObject(String propertyName, String propertyValue) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        jsonObjectBuilder.add(propertyName, propertyValue);

        return jsonObjectBuilder.build();
    }

    public static String convertToJsonString(Object objectToConvert) {
        return new Gson().toJson(objectToConvert);
    }

    public static JsonObject createJsonObject(List<String> propertyNames,
                                              List<String> propertyValues) {

        if (propertyNames.size() != propertyValues.size()) {
            throw new RuntimeException(
                    "List of propertyNames does not have the same size as the list of propertyValues");
        }

        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        for (int i = 0; i < propertyNames.size(); i++) {
            jsonObjectBuilder.add(propertyNames.get(i), propertyValues.get(i));
        }

        return jsonObjectBuilder.build();
    }

}
