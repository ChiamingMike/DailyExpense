package controller;

import java.util.*;

import container.MSDBDatacontainer;

public class Trigger {
    public static String manipulationType;
    public String DEFAULT_TYPE = "SELECT";
    public MSDBDatacontainer msdbDatacontainer;
    public Scanner scanner = new Scanner(System.in);
    private Map<Integer, String> manipulation;

    public Trigger() {
        msdbDatacontainer = new MSDBDatacontainer();
        this.initilizeManipulation();
    }

    public static void main(String[] args) {
        Trigger trigger = new Trigger();
        trigger.decideManipulationType();
        trigger.manipulateDatabase();
    }

    private void initilizeManipulation() {
        manipulation = new HashMap<Integer, String>();
        manipulation.put(1, "INSERT");
        manipulation.put(2, "SELECT");
        manipulation.put(3, "DELETE");

        for (Integer typeNumber : manipulation.keySet()) {
            System.out.println(typeNumber + " : " + manipulation.get(typeNumber));
        }

        System.out.println(String.format("(Default will be : %s)\n", DEFAULT_TYPE));
    }

    public void decideManipulationType() {
        try {
            System.out.print("Choose manipulation number: ");
            Integer typeNumber = scanner.nextInt();
            manipulationType = manipulation.getOrDefault(typeNumber, DEFAULT_TYPE);

            if (!manipulation.keySet().contains(typeNumber)) {
                System.out.println("Using default manipulation type.");
            }

        } catch (Exception e) {
            manipulationType = "ERROR";
        }
    }

    public void manipulateDatabase() {
        msdbDatacontainer.registerColumnDescription();

        String[] tmpColumns = { "from_date", "to_date" };
        boolean retry = true;
        Map<String, String> RawData = new LinkedHashMap<String, String>();
        boolean canRegister;

        switch (manipulationType) {
            case "INSERT":
                Map<String, String> columnDescription = msdbDatacontainer.getColumnDescription();

                if (!(msdbDatacontainer.getFlagAndItems())) {
                    System.out.println("Failed to get flag and items.");
                    break;
                }

                while (retry) {
                    for (String columnName : columnDescription.keySet()) {
                        if (Arrays.asList(tmpColumns).contains(columnName)) {
                            continue;
                        }

                        System.out.print(String.format("Please enter %s : ", columnName));
                        String columnValue = scanner.next();
                        RawData.put(columnName, columnValue);
                    }

                    canRegister = msdbDatacontainer.registerInsertData(RawData);
                    retry = this.needRetry(canRegister);
                }

                break;

            case "SELECT":
                while (retry) {
                    for (String columnName : tmpColumns) {
                        System.out.print(String.format("Please enter %s : ", columnName));
                        String columnValue = scanner.next();
                        RawData.put(columnName, columnValue);
                    }

                    canRegister = msdbDatacontainer.registerSelectData(RawData);
                    retry = this.needRetry(canRegister);
                }

                break;

            case "DELETE":
                while (retry) {
                    for (String columnName : tmpColumns) {
                        System.out.print(String.format("Please enter %s : ", columnName));
                        String columnValue = scanner.next();
                        RawData.put(columnName, columnValue);
                    }

                    canRegister = msdbDatacontainer.registerInsertData(RawData);
                    retry = this.needRetry(canRegister);
                }

                break;

            default:
                System.out.println("Unexcepted manipulation type.");
        }

        scanner.close();
    }

    private boolean needRetry(boolean canRegisterData) {
        if (canRegisterData) {
            return false;
        } else {
            return true;
        }
    }

}