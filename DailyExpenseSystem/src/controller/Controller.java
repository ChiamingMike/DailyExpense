package controller;

import java.util.*;
import container.DataContainer;

public class Controller {
    public static String manipulationType;

    public Controller() {
    }

    public static void main(String[] args) {
        DataContainer dataContainer = new DataContainer();
        new Controller().decideManipulation();
        dataContainer.registerInputData(manipulationType);
    }

    public String decideManipulation() {
        String DEFAULT_MANIPULATION = "SELECT";
        Map<Integer, String> manipulation = new HashMap<Integer, String>();
        manipulation.put(1, "INSERT");
        manipulation.put(2, "SELECT");
        manipulation.put(3, "DELETE");
        for (Integer key : manipulation.keySet()) {
            System.out.println(key + " : " + manipulation.get(key));
        }

        Scanner scanner = new Scanner(System.in);
        boolean retry = true;
        while (retry) {
            try {
                System.out.println(String.format("(Default will be : %s)", DEFAULT_MANIPULATION));
                System.out.print("Choose manipulation number: ");
                Integer key = scanner.nextInt();
                manipulationType = manipulation.getOrDefault(key, DEFAULT_MANIPULATION);
                retry = false;

                if (!manipulation.keySet().contains(key) && manipulationType.equals(manipulationType)) {
                    System.out.println("Using default manipulation type.");
                }

            } catch (Exception e) {
                System.out.println("Please enter a manipulation number.");
            }
        }
        // scanner.close();

        return manipulationType;

    }
}