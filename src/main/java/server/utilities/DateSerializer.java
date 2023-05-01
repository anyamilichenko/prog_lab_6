package server.utilities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class DateSerializer implements JsonSerializer<LocalDate> {

    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        obj.addProperty("year", src.getYear());
        obj.addProperty("month", src.getMonthValue());
        obj.addProperty("day", src.getDayOfMonth());
        return obj;
    }
}
