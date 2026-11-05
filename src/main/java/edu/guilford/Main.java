package edu.guilford;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ServiceCalculator calculator = new ServiceCalculator();
        calculator.run(new Scanner(System.in));
    }
}