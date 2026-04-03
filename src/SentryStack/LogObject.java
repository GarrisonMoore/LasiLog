package SentryStack;

public class LogObject {
    private final long timestamp;
    private final String source;
    private final String severity;
    private final String category;
    private final String pid;
    private final String message;

    public LogObject(long timestamp, String source, String severity, String category, String pid, String message) {
        this.timestamp = timestamp;
        this.source = source;
        this.severity = severity;
        this.category = category;
        this.pid = pid;
        this.message = message;
    }

    // Helpers below
    public long getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }

    public String getCategory() {
        return category;
    }

    public String getPid() {
        return pid;
    }

    public String getMessage() {
        return message;
    }

    public String getSeverity() {
        return severity;
    }

    public String setSeverity(String severity){
        return severity;
    }

    public String setCategory(String category){
        return category;
    }

    public String toString() {

        java.time.LocalDateTime date = java.time.LocalDateTime.ofInstant(
                java.time.Instant.ofEpochSecond(timestamp),
                java.time.ZoneId.systemDefault()
        );

        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "[" + date.format(formatter) + "] " + source + " | " + severity + " | "+category+ " | " + pid+ " : " + message;
    }
}
