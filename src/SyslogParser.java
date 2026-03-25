import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyslogParser implements LogParser {

    // RFC-5424 format Regex Tokenizer
    private static final Pattern LOG_PATTERN = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[^\\s]*)\\s+(\\S+)\\s+.*:\\s+(.*)$");

    @Override
    public boolean canParse(String rawline) {
        return rawline.matches("^\\d{4}-\\d{2}.*");
    }

    @Override
    public LogObject parse(String rawline) {
        Matcher m = LOG_PATTERN.matcher(rawline);
        if (m.find()) {
            try {
                long epochTime = java.time.OffsetDateTime.parse(m.group(1)).toEpochSecond();
                String host = m.group(2);
                String msg = m.group(3);

                String severity = "INFO";
                String lowerMsg = msg.toLowerCase();
                if (lowerMsg.contains("fail") || lowerMsg.contains("error")) severity = "CRIT";
                if (lowerMsg.contains("warn") || lowerMsg.contains("timeout")) severity = "WARN";

                LogObject logObject = new LogObject(epochTime, host, severity, msg);
                IndexingEngine.TimeIndex.computeIfAbsent(epochTime, k -> new ArrayList<>()).add(logObject);
                IndexingEngine.HostIndex.computeIfAbsent(host, k -> new ArrayList<>()).add(logObject);
            } catch (Exception ignored) {
                // ignore parse errors
            }
        }
        return null;
    }
}

//    public LogObject parse(String rawline) {
//        Matcher matcher = LOG_PATTERN.matcher(rawline);
//        if (matcher.find()) {
//            try {
//                long epochTime = java.time.OffsetDateTime.parse(matcher.group(1)).toEpochSecond();
//                String host = matcher.group(2);
//                String msg = matcher.group(3);
//
//                String severity = "INFO";
//                String lowerMsg = msg.toLowerCase();
//                if (lowerMsg.contains("fail") || lowerMsg.contains("error")) severity = "CRIT";
//                if (lowerMsg.contains("warn") || lowerMsg.contains("timeout")) severity = "WARN";
//
//                LogObject logObject = new LogObject(epochTime, host, severity, msg);
//                IndexingEngine.TimeIndex.computeIfAbsent(epochTime, k -> new ArrayList<>()).add(logObject);
//                IndexingEngine.HostIndex.computeIfAbsent(host, k -> new ArrayList<>()).add(logObject);
//            } catch (Exception ignored) {
//                // ignore parse errors
//            }
//        }
//        return null;
//    }

