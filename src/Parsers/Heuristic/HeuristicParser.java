package Parsers.Heuristic;

import Interfaces.CategorizationMaster;
import Interfaces.ParserMaster;
import Parsers.JSON.JSONParser;
import SentryStack.LogObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeuristicParser implements ParserMaster {

    Long timestamp;
    String source;
    String category;
    String severity;
    String message;


    @Override
    public boolean canParse(String rawline) {
        return true;
    }

    @Override
    public LogObject parse(String rawline) {

        // The Universal "Anchor" Pattern
        // This finds: [Date/Time] [Anything] [The Rest]
        // Matches "2026-04-01 19:08:01 Hostname Msg..."
        // OR "Apr 1 19:08:01 Hostname Msg..."
        Pattern universalPattern = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2}[ T]\\d{2}:\\d{2}:\\d{2}|[A-Z][a-z]{2}\\s+\\d{1,2}\\s+\\d{2}:\\d{2}:\\d{2})\\s+(\\S+)\\s+(.*)$");

        Matcher m = universalPattern.matcher(rawline);
        if (m.find()) {
            String timestampSTR = m.group(1);
            source = m.group(2);
            message = m.group(3);

            timestamp = Long.parseLong(timestampSTR.replaceAll("[^0-9]", ""));


        }

        LogObject rawLog = new LogObject(timestamp, source, "INFO", "UNCATEGORIZED", message);

        LogObject categorized = CategorizationMaster.categorize(rawLog);

        return categorized;
    }
}
