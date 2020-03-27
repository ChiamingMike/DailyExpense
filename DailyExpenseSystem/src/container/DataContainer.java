package container;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataContainer {
    private MSDBQueryContainer msdbQueryContainer;
    Map<Integer, String> flag = new HashMap<Integer, String>();
    Map<Integer, String> items = new HashMap<Integer, String>();

    public DataContainer() {
        msdbQueryContainer = new MSDBQueryContainer();
    }

    private void getExpenseFlag() {
        flag = msdbQueryContainer.getExecutionResult(msdbQueryContainer.FLAG_QUERY);
        System.out.println("+--------------CATEGORY_ID--------------+");
        for (Integer key : flag.keySet()) {
            System.out.println(key + " : " + flag.get(key));
        }
        System.out.println("+---------------------------------------+");
    }

    private void getFinancialItmes() {
        items = msdbQueryContainer.getExecutionResult(msdbQueryContainer.ITEMS_QUERY);
        System.out.println("+----------------ITEM_ID----------------+");
        for (Integer key : items.keySet()) {
            System.out.println(key + " : " + items.get(key));
        }
        System.out.println("+---------------------------------------+");
    }

    public void registerInputData(String manipulationType) {
        switch (manipulationType) {
            case "INSERT":
                this.getExpenseFlag();
                this.getFinancialItmes();
                this.insertRecord();
                break;
            case "SELECT":
                this.selectRecord();
                break;
            case "DELETE":
                this.deleteRecord();
                break;
            default:
                System.out.println("Unexpected manipulation type.");
        }

    }

    public void insertRecord() {
        Scanner scanner = new Scanner(System.in);
        String INSERT_QUERY = msdbQueryContainer.ACCOUNT_BOOK_INSERT_QUERY;
        Pattern pattern = Pattern.compile("INSERT INTO.*\\((.*)\\).*VALUES\\((.*)\\)");
        Matcher matches = pattern.matcher(INSERT_QUERY);
        if (matches.find()) {
            String[] tmp_columns = matches.group(1).replaceAll("\\s", "").split(",", 0);
            String[] tmp_values = matches.group(2).replaceAll("\\s", "").split(",", 0);
            Integer OFFSET = 0;
            for (String tmp : tmp_values) {
                System.out.print(String.format("Please enter %s : ", tmp));
                String value = scanner.nextLine();
                value = this.checkValue(tmp_columns[OFFSET], value);
                INSERT_QUERY = INSERT_QUERY.replaceAll(tmp, value);
                OFFSET++;
            }
            scanner.close();
        }
        System.out.println("INSERT_QUERY: " + INSERT_QUERY);
        msdbQueryContainer.executeQuery(INSERT_QUERY);

    }

    public void deleteRecord() {
        System.out.println("Under construction");
    }

    public void selectRecord() {
        System.out.println("Under construction");
    }

    public String checkValue(String column, String value) {
        String intColumns[] = { "id", "category_id", "item_id", "amount" };
        String checkedValue;
        if (!Arrays.asList(intColumns).contains(column) && value.isEmpty()) {
            checkedValue = "NULL";
        } else if (!Arrays.asList(intColumns).contains(column)) {
            checkedValue = String.format("\'%s\'", value);
        } else {
            checkedValue = value;
        }
        return checkedValue;
    }

}