package Parsers.JSON;

import Interfaces.CategorizationMaster;
import Interfaces.ParserMaster;
import SentryStack.LogObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONParser implements ParserMaster {

    String JSONstart = "{";

    long timestamp;
    String source;
    String category;
    String severity;
    String message;

    @Override
    public boolean canParse(String rawline) {
        return rawline.trim().startsWith(JSONstart);
    }

    @Override
    public LogObject parse(String rawline) {

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(rawline).getAsJsonObject();

        long timestamp = jsonObject.get("timestamp").getAsLong();
        String source = jsonObject.get("source").getAsString();
        String category = jsonObject.has("category")
                ? jsonObject.get("category").getAsString()
                : "UNCATEGORIZED";
        String severity = jsonObject.has("level")
                ? jsonObject.get("level").getAsString()
                : "INFO";
        String message = jsonObject.get("message").getAsString();

        LogObject rawLog = new LogObject(timestamp, source, severity, category, message);
        LogObject categorized = CategorizationMaster.categorize(rawLog);

        System.out.println(jsonObject);

        return categorized;
    }
}
