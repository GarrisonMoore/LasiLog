package Interfaces;

import SentryStack.LogObject;

public class CategorizationMaster {

    public static LogObject categorize(LogObject log){

        Long timestamp = log.getTimestamp();
        String source = log.getSource();
        String severity = log.getSeverity();
        String category = log.getCategory();
        String message = log.getMessage();

        String msg;

        // throw away windows noise first
        if (message.contains("the locale specific resource for the desired message is not present")) {
            msg = "computer fart noises";
            message = msg.toLowerCase();
        }

        // --- CATEGORIZATION ---
        //String severity = "INFO";
        //String category = "UNCATEGORIZED";

        // Categories - Check more critical first
        if (message.contains("crit") || message.contains("error") || message.contains("exception") ||
                message.contains("err") || message.contains("fail") || message.contains("failed") || message.contains("failure")) {
            severity = "CRIT";
            category = "ERRORS";
        } else if (category.equals("UNCATEGORIZED")) {
            // ---- WARNINGS ----
            if (message.contains("warn") || message.contains("timeout") || message.contains("warning") ||
                    message.contains("blocked") || message.contains("denied")) {
                severity = "WARN";
                category = "WARNINGS";
                // ---- AUTH EVENTS ----
            } else if (message.contains("logon") || message.contains("auth") || message.contains("access") ||
                    message.contains("request") || message.contains("login")) {
                severity = "INFO";
                category = "AUTH EVENTS";
                // ---- AUDIT ----
            } else if (message.contains("audit") || message.contains("auditd")) {
                severity = "WARN";
                category = "AUDIT";
                // ---- GROUP POLICY ----
            } else if (message.contains("kbps") || message.contains("wallpaper") || message.contains("wallpapers") ||
                    message.contains("group") || message.contains("policy") || message.contains(".local") ||
                    message.contains("10.202.69.") || message.contains("{")) {
                severity = "INFO";
                category = "GROUP POLICY";
                // ---- REMOTE MANAGEMENT ----
            }else if (message.contains("winrm") || message.contains("service") || message.contains("remote") ||
                    message.contains("management") || message.contains("powershell") || message.contains("ps")) {
                category = "REMOTE MANAGEMENT";
                severity = "WARN";
            }
        }
        // new categorized log object
        return new LogObject(timestamp, source, severity, category, message);
    }
}
