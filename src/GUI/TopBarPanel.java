package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.event.DocumentListener;

public class TopBarPanel extends JPanel {
    private final JComboBox<String> pivotBox = new JComboBox<>(new String[]{"Hostnames", "Category", "Severity", "Time"});
    private final JTextField searchField = new JTextField();
    private final JButton pauseLiveFeedButton = new JButton("Pause Live Feed");

    public TopBarPanel() {
        setLayout(new BorderLayout(15, 0));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        initComponents();
    }

    private void initComponents() {
        JPanel leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftControls.setOpaque(false);
        pivotBox.setPreferredSize(new Dimension(180, 40));
        pivotBox.setFont(GUIConstants.MAIN_FONT);
        pivotBox.putClientProperty("JComponent.outline", GUIConstants.ACCENT_COLOR);
        leftControls.add(pivotBox);

        searchField.setFont(GUIConstants.MAIN_FONT);
        searchField.putClientProperty("JTextField.placeholderText", "Search logs...");
        searchField.putClientProperty("JComponent.outline", GUIConstants.ACCENT_COLOR);

        pauseLiveFeedButton.setFont(GUIConstants.MAIN_FONT);
        pauseLiveFeedButton.putClientProperty("JComponent.outline", GUIConstants.ACCENT_COLOR);
        pauseLiveFeedButton.setFocusPainted(false);

        JPanel rightControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightControls.setOpaque(false);
        rightControls.add(pauseLiveFeedButton);

        add(leftControls, BorderLayout.WEST);
        add(searchField, BorderLayout.CENTER);
        add(rightControls, BorderLayout.EAST);
    }

    public void addPauseLiveFeedActionListener(ActionListener listener) {
        pauseLiveFeedButton.addActionListener(listener);
    }

    public void setLiveFeedPaused(boolean paused) {
        pauseLiveFeedButton.setText(paused ? "Resume Live Feed" : "Pause Live Feed");
    }

    public void addPivotActionListener(ActionListener listener) {
        pivotBox.addActionListener(listener);
    }

    public void addSearchDocumentListener(DocumentListener listener) {
        searchField.getDocument().addDocumentListener(listener);
    }

    public String getSearchText() {
        return searchField.getText().trim();
    }

    public String getSelectedPivot() {
        return (String) pivotBox.getSelectedItem();
    }
}
