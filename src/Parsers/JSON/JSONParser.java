package Parsers.JSON;

import Interfaces.CategorizationMaster;
import Interfaces.ParserMaster;
import SentryStack.LogObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.sql.Timestamp;

public class JSONParser implements ParserMaster {

    String JSONstart = "{";
    String JSONend = "}";

    long timestamp;
    String source;
    String category;
    String severity;
    String message;

    @Override
    public boolean canParse(String rawline) {
        return rawline.contains(JSONstart + JSONend);
    }

    @Override
    public LogObject parse(String rawline) {

        JsonObject jsonObject = JsonParser.parseString(rawline).getAsJsonObject();




        System.out.println(jsonObject);

        return null;
    }
}
