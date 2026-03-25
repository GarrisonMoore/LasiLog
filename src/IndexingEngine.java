import javax.swing.*;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class IndexingEngine{

    static GUI myGui;
    static final HashMap<String, List<LogObject>> HostIndex = new HashMap<>();
    static final TreeMap<Long, List<LogObject>> TimeIndex = new TreeMap<>();

    static void tailFile(Path file) {
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
                    // java bro
                    SyslogParser parser = new SyslogParser();
                    parser.parse(line);
                }
            }
        } catch (Exception e) {
            System.err.println("Log tail stopped: " + e.getMessage());
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