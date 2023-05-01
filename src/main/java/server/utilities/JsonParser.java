package server.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import common.data.Dragon;

import java.lang.reflect.Type;
import java.util.LinkedList;

public class JsonParser {

    public String serialize(LinkedList<Dragon> collectionData) {
        Gson g = new GsonBuilder().registerTypeAdapter(java.time.LocalDate.class, new DateSerializer()).create();
        return g.toJson(collectionData);
    }

    public LinkedList<Dragon> deSerialize(String strData) throws JsonSyntaxException, IllegalArgumentException {
        Gson g = new GsonBuilder().registerTypeAdapter(java.time.LocalDate.class, new DateDeserializer()).create();
        Type type = new TypeToken<LinkedList<Dragon>>() {
        }.getType();
        if ("".equals(strData)) {
            return new LinkedList<>();
        }
        return g.fromJson(strData, type);
    }
}
