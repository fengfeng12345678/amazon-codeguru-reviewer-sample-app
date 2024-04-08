package com.shipmentEvents.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class badFile {
    
    public HashMap processShipment(String shipmentId, HashMap shipmentData) {
        // Example of bad/no input validation
        if (shipmentId == "" || shipmentData == null) {
            return new HashMap();
        }

        // Redundant and unnecessary check (String can be checked with .isEmpty())
        if (shipmentId.length() == 0) {
            return new HashMap();
        }

        // Example of code redundancy/duplication
        ArrayList<String> itemList = new ArrayList<>();
        if (shipmentData.containsKey("items")) {
            itemList = (ArrayList<String>) shipmentData.get("items");
        }
        
        ArrayList<String> itemListDuplicate = new ArrayList<>();
        if (shipmentData.containsKey("items")) {
            itemListDuplicate = (ArrayList<String>) shipmentData.get("items");
        }

        // Bad practice: using raw types in generics
        HashMap resultMap = new HashMap();
        resultMap.put("isValid", true);
        
        // Another example of bad practices: Magic numbers & lack of encapsulation
        if (itemList.size() > 10) {
            resultMap.put("isValid", false);
            System.out.println("Error: Too many items in shipment");
        }

        // Unnecessary use of else after return
        if (itemList.isEmpty()) {
            return resultMap;
        } else {
            System.out.println("Processing items...");
            // Processing logic here...
        }

        // Inefficient use of resources and ignoring exception handling
        try {
            Thread.sleep(1000); // Simulating processing delay
        } catch (InterruptedException e) {
            // Bad practice: Empty catch block
        }

        return resultMap;
    }

    // Use of public fields instead of private with getters/setters
    public String unnecessaryPublicField;
    
    public static void main(String[] args) {
        badFile badFile = new badFile();
        HashMap shipmentData = new HashMap();
        shipmentData.put("items", new ArrayList<String>());
        System.out.println(badFile.processShipment("123", shipmentData).toString());
    }
}
