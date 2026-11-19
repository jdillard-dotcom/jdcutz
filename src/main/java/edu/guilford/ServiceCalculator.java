package edu.guilford;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServiceCalculator {
    private final List<HaircutPackage> packages = List.of(
        new HaircutPackage("Fade", new BigDecimal("30.00")),
        new HaircutPackage("Line-Up", new BigDecimal("15.00")),
        new HaircutPackage("Beard Trim", new BigDecimal("20.00"))
    );

    private final List<AddOn> addOns = List.of(
        new AddOn("Hot towel", new BigDecimal("5.00")),
        new AddOn("Shampoo", new BigDecimal("7.00")),
        new AddOn("Hair enhancement", new BigDecimal("5.00"))
    );

    private final List<String> barbers = List.of("Fade Master Mike", "Classic Chris", "Fresh Cut Fran");
    private final List<String> timeSlots = List.of("10:00 AM", "12:30 PM", "2:00 PM", "4:15 PM");

    public void run(Scanner scanner) {
        System.out.println("=== Barber Shop Appointment & Price Manager ===");

        // Feature 1: Barber Selection
        System.out.println("\nSelect a Barber:");
        for (int i = 0; i < barbers.size(); i++) {
            System.out.println((i + 1) + ". " + barbers.get(i));
        }
        int barberChoice = readNumber(scanner, "Barber number: ", 1, barbers.size()) - 1;
        String selectedBarber = barbers.get(barberChoice);

        // Feature 1: Time Slot Selection
        System.out.println("\nSelect a Time Slot:");
        for (int i = 0; i < timeSlots.size(); i++) {
            System.out.println((i + 1) + ". " + timeSlots.get(i));
        }
        int timeChoice = readNumber(scanner, "Time slot number: ", 1, timeSlots.size()) - 1;
        String selectedTime = timeSlots.get(timeChoice);

        // Package Selection
        System.out.println("\nChoose a haircut package:");
        for (int i = 0; i < packages.size(); i++) {
            HaircutPackage hp = packages.get(i);
            System.out.printf("%d. %s - %s\n", (i + 1), hp.getName(), formatPrice(hp.getPrice()));
        }
        int packageChoice = readNumber(scanner, "Package number: ", 1, packages.size()) - 1;
        HaircutPackage selectedPackage = packages.get(packageChoice);

        // Add-ons Selection
        System.out.println("\nChoose any add-ons:");
        List<AddOn> selectedAddOns = new ArrayList<>();
        for (AddOn addOn : addOns) {
            if (readYesNo(scanner, addOn.name() + " (" + formatPrice(addOn.price()) + ")? (y/n): ")) {
                selectedAddOns.add(addOn);
            }
        }

        // Feature 2: Tip Calculation
        System.out.println("\nSelect Tip Percentage:");
        System.out.println("1. 15%\n2. 18%\n3. 20%\n4. Custom / None");
        int tipChoice = readNumber(scanner, "Choice (1-4): ", 1, 4);
        BigDecimal tipRate = switch (tipChoice) {
            case 1 -> new BigDecimal("0.15");
            case 2 -> new BigDecimal("0.18");
            case 3 -> new BigDecimal("0.20");
            default -> BigDecimal.ZERO;
        };

        printReceipt(selectedBarber, selectedTime, selectedPackage, selectedAddOns, tipRate);
    }

    private void printReceipt(String barber, String time, HaircutPackage pkg, List<AddOn> addOns, BigDecimal tipRate) {
        System.out.println("\n========= BARBER SHOP RECEIPT =========");
        System.out.println("Barber: " + barber + " | Time: " + time);
        System.out.println("----------------------------------------");
        
        BigDecimal total = pkg.getPrice();
        System.out.printf("%-26s %10s\n", pkg.getName(), formatPrice(pkg.getPrice()));

        for (AddOn addOn : addOns) {
            System.out.printf("%-26s %10s\n", addOn.name(), formatPrice(addOn.price()));
            total = total.add(addOn.price());
        }

        BigDecimal tipAmount = total.multiply(tipRate).setScale(2, RoundingMode.HALF_UP);
        if (tipAmount.compareTo(BigDecimal.ZERO) > 0) {
            System.out.printf("%-26s %10s\n", "Tip (" + tipRate.multiply(new BigDecimal("100")) + "%)", formatPrice(tipAmount));
            total = total.add(tipAmount);
        }

        System.out.println("----------------------------------------");
        System.out.printf("%-26s %10s\n", "Total Due", formatPrice(total));
        System.out.println("========================================");
    }

    private int readNumber(Scanner scanner, String prompt, int minimum, int maximum) {
        while (true) {
            System.out.print(prompt);
            try {
                int number = Integer.parseInt(scanner.nextLine().trim());
                if (number >= minimum && number <= maximum) return number;
            } catch (NumberFormatException ignored) {}
            System.out.printf("Please enter a number from %d to %d.\n", minimum, maximum);
        }
    }

    private boolean readYesNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("y")) return true;
            if (input.equalsIgnoreCase("n")) return false;
            System.out.println("Enter y or n.");
        }
    }

    private String formatPrice(BigDecimal price) {
        return "$" + price.setScale(2, RoundingMode.HALF_UP);
    }

    private record AddOn(String name, BigDecimal price) {}
}