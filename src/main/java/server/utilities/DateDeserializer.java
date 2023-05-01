package server.utilities;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import common.data.Dragon;
import common.utilities.CollectionManager;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Map;

public class DateDeserializer implements JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson g = new Gson();
        Type type = new TypeToken<Map<String, Integer>>() {
        }.getType();
        Map<String, Integer> map = g.fromJson(json.toString(), type);
        return java.time.LocalDate.of(map.get("year"), map.get("month"), map.get("day"));
    }
}

