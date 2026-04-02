package Parsers.Syslog;

import Interfaces.CategorizationMaster;
import SentryStack.LogObject;
import Interfaces.ParserMaster;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyslogParser implements ParserMaster {

    // RFC-5424 format Regex Tokenizer
    private static final Pattern RFC5424_PATTERN = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[^\\s]*)\\s+(\\S+)\\s+(?:.*:\\s+)?(.*)$");

    // BSD syslog format Regex Tokenizer
    private static final Pattern BSD_PATTERN = Pattern.compile("^([A-Z][a-z]{2}\\s+\\d{1,2}\\s+\\d{2}:\\d{2}:\\d{2})\\s+(\\S+)\\s+(.*)$");

    private static final java.time.format.DateTimeFormatter BSD_FORMATTER = new java.time.format.DateTimeFormatterBuilder()
            .appendPattern("MMM d HH:mm:ss")
            .parseDefaulting(java.time.temporal.ChronoField.YEAR, java.time.LocalDate.now().getYear())
            .toFormatter(java.util.Locale.ENGLISH);

    // override the canParse method in the Interfaces.LogParser interface (do this for any new parser)
    @Override
    public boolean canParse(String rawline) {
        // make sure the line matches the RFC-5424 format
        return rawline.matches("^\\d{4}-\\d{2}.*") || rawline.matches("^[A-Z][a-z]{2}\\s+\\d{1,2}\\s+\\d{2}:\\d{2}:\\d{2}.*");
    }

    // override the parse method in the Interfaces.LogParser interface (do this for any new parser)
    // parse the line using the RFC-5424 and BSD format
    @Override
    public LogObject parse(String rawline) {
        Matcher m = RFC5424_PATTERN.matcher(rawline);
        long epochTime = 0;
        String host = "";
        String msg = "";

        if (m.find()) {
            try {
                epochTime = java.time.OffsetDateTime.parse(m.group(1)).toEpochSecond();
                host = m.group(2);
                msg = m.group(3);
            } catch (Exception e) {
                System.err.println("Error parsing RFC5424 log: " + e.getMessage());
                return null;
            }
        } else {
            m = BSD_PATTERN.matcher(rawline);
            if (m.find()) {
                try {
                    String timestampStr = m.group(1);
                    // Handle single digit day spacing if present (e.g., "Mar  1")
                    timestampStr = timestampStr.replaceAll("\\s+", " ");
                    epochTime = java.time.LocalDateTime.parse(timestampStr, BSD_FORMATTER)
                            .atZone(java.time.ZoneId.systemDefault()).toEpochSecond();
                    host = m.group(2);
                    msg = m.group(3);
                } catch (Exception e) {
                    System.err.println("Error parsing BSD log: " + e.getMessage());
                    return null;
                }
            } else {
                return null;
            }
        }

        String severity = "INFO";
        String category = "UNCATEGORIZED";

        // Raw log object
        LogObject logObject = new LogObject(epochTime, host, severity, category, msg);

        // Categorize the log object
        LogObject categorizedLogObject = CategorizationMaster.categorize(logObject);

        return categorizedLogObject;
    }
}