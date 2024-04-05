package com.shipmentEvents.handlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class badFile {
    private static String AWS_ACCESS_KEY_ID = "AKIAIOSFODNN7EXAMPLE"; // Sensitive information leak
    private static String AWS_SECRET_ACCESS_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"; // Sensitive information leak
    
    public static void main(String[] args) {
        // Resource leak: FileReader and BufferedReader are not closed in a finally block or using try-with-resources
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("someFile.txt"));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // AWS best practice violation: ExecutorService should be properly shut down.
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            executor.submit(() -> {
                System.out.println("Running task " + finalI);
                // Simulate task
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        // Concurrency issue: ExecutorService is never shut down, which could lead to resource leaks.
        // Proper shutdown like executor.shutdown() or shutdownNow() should be called.

        // Common coding error: No input validation
        printUsername(null);

        // Code duplication: Similar or same blocks of code found in multiple locations can lead to maintenance issues.
        duplicateCode();
        duplicateCode();
    }

    public static void printUsername(String username) {
        if (username == null) {
            System.out.println("Username must not be null!"); // Error handling should be more graceful.
        } else {
            System.out.println("Username is: " + username);
        }
    }
    
    public static void duplicateCode() {
        System.out.println("This is a duplicated method");
        // Implementing logic that's repeated elsewhere increases maintenance overhead and potential for errors.
    }
    
    // Example to represent input validation issues
    public static void unsafeAddition(String input) {
        try {
            int result = Integer.parseInt(input) + 1;
            System.out.println("Result: " + result);
        } catch (NumberFormatException e) {
            System.out.println("Input must be a number.");
            // Catching general exception without proper input validation or error handling.
        }
    }
}
