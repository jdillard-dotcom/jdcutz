package edu.guilford;

import java.math.BigDecimal;
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

    public void run(Scanner scanner) {
        System.out.println("=== Barber Shop Appointment & Price Manager ===");
        System.out.println("Choose a haircut package:");
        for (int i = 0; i < packages.size(); i++) {
            HaircutPackage haircutPackage = packages.get(i);
            System.out.printf("%d. %s - %s%n", i + 1, haircutPackage.getName(),
                    formatPrice(haircutPackage.getPrice()));
        }

        HaircutPackage selectedPackage = packages.get(
                readNumber(scanner, "Package number: ", 1, packages.size()) - 1);
        System.out.println("\nChoose any add-ons:");

        List<AddOn> selectedAddOns = new java.util.ArrayList<>();
        for (AddOn addOn : addOns) {
            if (readYesNo(scanner, addOn.name() + " (" + formatPrice(addOn.price()) + ")? (y/n): ")) {
                selectedAddOns.add(addOn);
            }
        }

        printReceipt(selectedPackage, selectedAddOns);
    }

    private void printReceipt(HaircutPackage selectedPackage, List<AddOn> selectedAddOns) {
        BigDecimal total = selectedPackage.getPrice();

        System.out.println("\n========== BARBER SHOP RECEIPT ==========");
        System.out.printf("%-26s %10s%n", selectedPackage.getName(), formatPrice(selectedPackage.getPrice()));
        for (AddOn addOn : selectedAddOns) {
            System.out.printf("%-26s %10s%n", addOn.name(), formatPrice(addOn.price()));
            total = total.add(addOn.price());
        }
        System.out.println("------------------------------------------");
        System.out.printf("%-26s %10s%n", "Total", formatPrice(total));
        System.out.println("==========================================");
    }

    private int readNumber(Scanner scanner, String prompt, int minimum, int maximum) {
        while (true) {
            System.out.print(prompt);
            String input = readLine(scanner);
            try {
                int number = Integer.parseInt(input);
                if (number >= minimum && number <= maximum) {
                    return number;
                }
            } catch (NumberFormatException ignored) {
                // Show the same prompt for non-numeric and out-of-range input.
            }
            System.out.printf("Enter a number from %d to %d.%n", minimum, maximum);
        }
    }

    private boolean readYesNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = readLine(scanner).trim();
            if (input.equalsIgnoreCase("y")) {
                return true;
            }
            if (input.equalsIgnoreCase("n")) {
                return false;
            }
            System.out.println("Enter y or n.");
        }
    }

    private String readLine(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            throw new IllegalStateException("Input ended before the order was complete.");
        }
        return scanner.nextLine().trim();
    }

    private String formatPrice(BigDecimal price) {
        return "$" + price.setScale(2);
    }

    private record AddOn(String name, BigDecimal price) {
    }
}
