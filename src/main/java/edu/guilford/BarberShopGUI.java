package edu.guilford;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class BarberShopGUI extends JFrame {
    private final JComboBox<String> barberBox;
    private final JComboBox<String> timeBox;
    private final JRadioButton fadeButton, lineUpButton, beardButton;
    private final JCheckBox towelBox, shampooBox, enhancementBox;
    private final JComboBox<String> tipBox;
    private final JTextArea receiptArea;

    public BarberShopGUI() {
        setTitle("Barber Shop Appointment & Price Manager");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top Panel: Selections
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        // Barber & Time
        JPanel btPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        btPanel.setBorder(BorderFactory.createTitledBorder("Appointment Details"));
        btPanel.add(new JLabel("Select Barber:"));
        barberBox = new JComboBox<>(new String[]{"Fade Master Mike", "Classic Chris", "Fresh Cut Fran"});
        btPanel.add(barberBox);
        btPanel.add(new JLabel("Select Time:"));
        timeBox = new JComboBox<>(new String[]{"10:00 AM", "12:30 PM", "2:00 PM", "4:15 PM"});
        btPanel.add(timeBox);
        inputPanel.add(btPanel);

        // Package Selection
        JPanel pkgPanel = new JPanel(new GridLayout(1, 3));
        pkgPanel.setBorder(BorderFactory.createTitledBorder("Haircut Package"));
        fadeButton = new JRadioButton("Fade ($30)", true);
        lineUpButton = new JRadioButton("Line-Up ($15)");
        beardButton = new JRadioButton("Beard Trim ($20)");
        ButtonGroup group = new ButtonGroup();
        group.add(fadeButton); group.add(lineUpButton); group.add(beardButton);
        pkgPanel.add(fadeButton); pkgPanel.add(lineUpButton); pkgPanel.add(beardButton);
        inputPanel.add(pkgPanel);

        // Add-ons
        JPanel addOnPanel = new JPanel(new GridLayout(1, 3));
        addOnPanel.setBorder(BorderFactory.createTitledBorder("Add-ons"));
        towelBox = new JCheckBox("Hot Towel ($5)");
        shampooBox = new JCheckBox("Shampoo ($7)");
        enhancementBox = new JCheckBox("Enhancement ($5)");
        addOnPanel.add(towelBox); addOnPanel.add(shampooBox); addOnPanel.add(enhancementBox);
        inputPanel.add(addOnPanel);

        // Tip Selection
        JPanel tipPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tipPanel.setBorder(BorderFactory.createTitledBorder("Tip Option"));
        tipPanel.add(new JLabel("Tip %: "));
        tipBox = new JComboBox<>(new String[]{"0%", "15%", "18%", "20%"});
        tipPanel.add(tipBox);
        inputPanel.add(tipPanel);

        add(inputPanel, BorderLayout.NORTH);

        // Center: Receipt Display Area
        receiptArea = new JTextArea();
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(receiptArea), BorderLayout.CENTER);

        // South: Calculate Button
        JButton calcButton = new JButton("Generate Receipt");
        calcButton.addActionListener(e -> calculateReceipt());
        add(calcButton, BorderLayout.SOUTH);
    }

    private void calculateReceipt() {
        String barber = (String) barberBox.getSelectedItem();
        String time = (String) timeBox.getSelectedItem();

        BigDecimal total = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append("========= BARBER SHOP RECEIPT =========\n");
        sb.append("Barber: ").append(barber).append(" | Time: ").append(time).append("\n");
        sb.append("----------------------------------------\n");

        if (fadeButton.isSelected()) {
            total = total.add(new BigDecimal("30.00"));
            sb.append(String.format("%-26s %10s\n", "Fade", "$30.00"));
        } else if (lineUpButton.isSelected()) {
            total = total.add(new BigDecimal("15.00"));
            sb.append(String.format("%-26s %10s\n", "Line-Up", "$15.00"));
        } else if (beardButton.isSelected()) {
            total = total.add(new BigDecimal("20.00"));
            sb.append(String.format("%-26s %10s\n", "Need/Beard Trim", "$20.00"));
        }

        if (towelBox.isSelected()) {
            total = total.add(new BigDecimal("5.00"));
            sb.append(String.format("%-26s %10s\n", "Hot towel", "$5.00"));
        }
        if (shampooBox.isSelected()) {
            total = total.add(new BigDecimal("7.00"));
            sb.append(String.format("%-26s %10s\n", "Shampoo", "$7.00"));
        }
        if (enhancementBox.isSelected()) {
            total = total.add(new BigDecimal("5.00"));
            sb.append(String.format("%-26s %10s\n", "Hair enhancement", "$5.00"));
        }

        String tipStr = (String) tipBox.getSelectedItem();
        BigDecimal tipRate = switch (tipStr) {
            case "15%" -> new BigDecimal("0.15");
            case "18%" -> new BigDecimal("0.18");
            case "20%" -> new BigDecimal("0.20");
            default -> BigDecimal.ZERO;
        };

        BigDecimal tipAmount = total.multiply(tipRate).setScale(2, RoundingMode.HALF_UP);
        if (tipAmount.compareTo(BigDecimal.ZERO) > 0) {
            sb.append(String.format("%-26s %10s\n", "Tip (" + tipStr + ")", "$" + tipAmount));
            total = total.add(tipAmount);
        }

        sb.append("----------------------------------------\n");
        sb.append(String.format("%-26s %10s\n", "Total Due", "$" + total.setScale(2, RoundingMode.HALF_UP)));
        sb.append("========================================");

        receiptArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BarberShopGUI().setVisible(true));
    }
}