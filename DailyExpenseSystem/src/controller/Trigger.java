package controller;

import java.util.*;
import container.MSDBDatacontainer;

public class Trigger {
    MSDBDatacontainer msdbDatacontainer;
    public static String manipulationType;

    public Trigger() {
        msdbDatacontainer = new MSDBDatacontainer();
    }

    public static void main(String[] args) {
        Trigger test = new Trigger();
        String rs = test.decideManipulationType();
        test.inputData(rs);
    }

    public String decideManipulationType() {
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

    public void inputData(String manipulationType) {
        msdbDatacontainer.registerColumnDescription();

        String[] tmpColumns = { "from_date", "to_date" };
        boolean retry = true;
        boolean canRegister;
        Scanner scanner = new Scanner(System.in);

        switch (manipulationType) {
            case "INSERT":
                Map<String, String> columnDescription = msdbDatacontainer.getColumnDescription();

                if (!(msdbDatacontainer.getFlagAndItems())) {
                    System.out.println("Failed to get flag and items.");
                    break;
                }

                while (retry) {
                    Map<String, String> insertRawData = new LinkedHashMap<String, String>();

                    for (String columnName : columnDescription.keySet()) {
                        if (Arrays.asList(tmpColumns).contains(columnName)) {
                            continue;
                        }

                        System.out.print(String.format("Please enter %s : ", columnName));
                        String rawData = scanner.next();
                        insertRawData.put(columnName, rawData);
                    }
                    canRegister = msdbDatacontainer.registerInsertData(insertRawData);
                    retry = this.needRetry(canRegister);
                }

                break;

            case "SELECT":
                while (retry) {
                    Map<String, String> selectRawData = new LinkedHashMap<String, String>();
                    for (String columnName : tmpColumns) {
                        System.out.println(String.format("Please enter %s : ", columnName));
                        String rawData = scanner.next();
                        selectRawData.put(columnName, rawData);

                    }
                    canRegister = msdbDatacontainer.registerSelectData(selectRawData);
                    retry = this.needRetry(canRegister);
                }
                break;

            case "DELETE":
                while (retry) {
                    Map<String, String> deleteRawData = new LinkedHashMap<String, String>();
                    for (String columnName : tmpColumns) {
                        System.out.println(String.format("Please enter %s : ", columnName));
                        String rawData = scanner.next();
                        deleteRawData.put(columnName, rawData);

                    }
                    canRegister = msdbDatacontainer.registerInsertData(deleteRawData);
                    retry = this.needRetry(canRegister);
                }
                break;

            default:
                System.out.println("Unexcepted manipulation type.");
        }
        scanner.close();
    }

    private boolean needRetry(boolean status) {
        if (status) {
            return false;

        } else {
            return true;
        }

    }

}