package GUI;

import java.awt.*;

public class GUIConstants {
    public static final Color ACCENT_COLOR = new Color(0, 150, 255); // Electric Blue
    public static final Color PANEL_BG = new Color(25, 25, 25);
    public static final Color LOG_BG = new Color(15, 15, 15);
    public static final Color LIST_BG = new Color(20, 20, 20);
    public static final Color BORDER_COLOR = new Color(40, 40, 40);
    
    // Severity colors
    public static final Color INFO_COLOR = new Color(150, 150, 150); // Muted Gray
    public static final Color WARN_COLOR = new Color(255, 180, 0);   // Amber
    public static final Color CRIT_COLOR = new Color(255, 60, 60);    // Red

    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font NAV_LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font MONO_FONT;

    static {
        Font jetbrains = new Font("JetBrains Mono", Font.PLAIN, 13);
        if (jetbrains.getFamily().equals("Dialog")) {
            MONO_FONT = new Font("Monospaced", Font.PLAIN, 13);
        } else {
            MONO_FONT = jetbrains;
        }
    }
}
