import javax.swing.*;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.formdev.flatlaf.FlatDarkLaf;

public class ProcessingEngine {

    private static GUI myGui;
    private static final HashMap<String, List<LogObject>> HostIndex = new HashMap<>();
    private static final TreeMap<Long, List<LogObject>> TimeIndex = new TreeMap<>();

    private static final Pattern LOG_PATTERN = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[^\\s]*)\\s+(\\S+)\\s+.*:\\s+(.*)$");
    private static final Path LOG_FILE = Paths.get("/var/log/windows_5141.log");

    public static void main(String[] args) {
        FlatDarkLaf.setup();

        SwingUtilities.invokeLater(() -> {
            myGui = new GUI();
            myGui.setHosts(HostIndex.keySet());
        });

        Thread logThread = new Thread(() -> tailFile(LOG_FILE), "log-tail");
        logThread.setDaemon(true);
        logThread.start();
    }

    private static void tailFile(Path file) {
        try {
            while (!Files.exists(file)) {
                Thread.sleep(500);
            }

            try (RandomAccessFile raf = new RandomAccessFile(file.toFile(), "r")) {
                long position = raf.length();
                raf.seek(position);

                while (true) {
                    String line = raf.readLine();

                    if (line == null) {
                        Thread.sleep(100);
                        continue;
                    }

                    // RandomAccessFile reads bytes; this is usually fine for log files,
                    // but if you need full UTF-8 safety, use a different reader strategy.
                    parseAndProcess(line);
                    scheduleGuiRefresh();
                }
            }
        } catch (Exception e) {
            System.err.println("Log tail stopped: " + e.getMessage());
        }
    }

    private static void scheduleGuiRefresh() {
        if (myGui != null) {
            SwingUtilities.invokeLater(() -> {
                myGui.setHosts(HostIndex.keySet());
                myGui.refreshDisplay();
            });
        }
    }

    private static void parseAndProcess(String rawLine) {
        Matcher matcher = LOG_PATTERN.matcher(rawLine);
        if (matcher.find()) {
            try {
                long epochTime = java.time.OffsetDateTime.parse(matcher.group(1)).toEpochSecond();
                String host = matcher.group(2);
                String msg = matcher.group(3);

                String severity = "INFO";
                String lowerMsg = msg.toLowerCase();
                if (lowerMsg.contains("fail") || lowerMsg.contains("error")) severity = "CRIT";
                if (lowerMsg.contains("warn") || lowerMsg.contains("timeout")) severity = "WARN";

                LogObject logObject = new LogObject(epochTime, host, severity, msg);
                TimeIndex.computeIfAbsent(epochTime, k -> new ArrayList<>()).add(logObject);
                HostIndex.computeIfAbsent(host, k -> new ArrayList<>()).add(logObject);
            } catch (Exception ignored) {
                // ignore parse errors
            }
        }
    }

    public static List<LogObject> getLogsBySeverity(String level) {
        List<LogObject> filtered = new ArrayList<>();
        for (List<LogObject> logList : TimeIndex.values()) {
            for (LogObject log : logList) {
                if (log.getLevel().equalsIgnoreCase(level)) filtered.add(log);
            }
        }
        return filtered;
    }

    public static List<LogObject> getLogsByTime(int minutes) {
        long cutoff = (System.currentTimeMillis() / 1000) - (minutes * 60L);
        List<LogObject> results = new ArrayList<>();
        for (List<LogObject> logList : TimeIndex.tailMap(cutoff).values()) results.addAll(logList);
        return results;
    }

    public static List<LogObject> getLogsForHost(String host) {
        return HostIndex.getOrDefault(host, new ArrayList<>());
    }

    public static Set<String> getHostKeys() {
        return HostIndex.keySet();
    }
}