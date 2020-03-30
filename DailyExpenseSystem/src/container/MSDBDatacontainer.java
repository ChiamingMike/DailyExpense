package container;

import java.util.*;

import execution.MSDBQueryExecution;
import loader.QueryPropertyLoader;

public class MSDBDatacontainer {
    private QueryPropertyLoader loader;
    private MSDBQueryExecution msdbQueryExecution;

    Map<String, String> columnDescription;

    public MSDBDatacontainer() {
        loader = new QueryPropertyLoader();
        msdbQueryExecution = new MSDBQueryExecution();

    }

    public void registerColumnDescription() {
        columnDescription = new LinkedHashMap<String, String>(
                msdbQueryExecution.executeBasicInfoQuery(loader.getValue("ACCOUNT_BOOK_BASIC_QUERY")));

    }

    public boolean registerInsertData(Map<String, String> insertRawData) {
        boolean isValid = this.isValidValue(insertRawData);
        if (isValid) {
            String query = fitDataIntoQuery(insertRawData, loader.getValue("ACCOUNT_BOOK_INSERT_QUERY"));
            boolean canExecute = msdbQueryExecution.executeInsertQuery(query);
            return canExecute;
        }

        return isValid;
    }

    public boolean registerSelectData(Map<String, String> selectRawData) {
        boolean isValid = this.isValidValue(selectRawData);
        if (isValid) {
            String query = fitDataIntoQuery(selectRawData, loader.getValue("ACCOUNT_BOOK_SELECT_QUERY"));
            boolean canExecute = msdbQueryExecution.executeSelectQuery(query);
            return canExecute;
        }

        return isValid;
    }

    public boolean registerDeleteData(Map<String, String> deleteRawData) {
        boolean isValid = this.isValidValue(deleteRawData);
        if (isValid) {
            String query = fitDataIntoQuery(deleteRawData, loader.getValue("ACCOUNT_BOOK_DELETE_QUERY"));
            boolean canExecute = msdbQueryExecution.executeDeleteQuery(query);
            return canExecute;
        }

        return isValid;
    }

    public boolean getFlagAndItems() {
        boolean canGetFlag = false;
        boolean canGetItems = false;

        try {
            canGetFlag = msdbQueryExecution.executeSelectQuery(loader.getValue("FLAG"));
            canGetItems = msdbQueryExecution.executeSelectQuery(loader.getValue("ITEMS"));

        } catch (Exception e) {
            System.out.println("Failed to get default data");
            System.out.println("Please check the default query.");
        }

        return (canGetFlag && canGetItems);
    }

    public Map<String, String> getColumnDescription() {
        if (columnDescription.isEmpty()) {
            System.out.println("No result of column description.");
        }

        return columnDescription;
    }

    public void getInsertData() {
    }

    public void getSelectData() {
    }

    public void getDeleteData() {
    }

    private boolean isValidValue(Map<String, String> rawData) {
        String intReg = "^\\d+$";
        String dateReg = "^\\d{4}-\\d{1,2}-\\d{1,2}";
        boolean intIsMatch = true;
        boolean charIsMatch = true;
        boolean dateIsMatch = true;

        for (Map.Entry<String, String> aRawData : rawData.entrySet()) {
            if (columnDescription.get(aRawData.getKey()) == null) {
                System.out.print("Unexcepted column : " + aRawData.getKey());
                charIsMatch = false;
                break;
            }

            if (columnDescription.get(aRawData.getKey()).equals("int")) {
                intIsMatch = aRawData.getValue().matches(intReg);

            } else if (columnDescription.get(aRawData.getKey()).contains("date")) {
                dateIsMatch = aRawData.getValue().matches(dateReg);

            } else if (columnDescription.get(aRawData.getKey()).contains("char")) {
                charIsMatch = aRawData.getValue().length() <= 20;
            }
        }

        if (!(intIsMatch && dateIsMatch && charIsMatch)) {
            System.out.println("Invalid data found.\n");
        }

        return (intIsMatch && dateIsMatch && charIsMatch);
    }

    private String fitDataIntoQuery(Map<String, String> rawData, String rawQuery) {
        String query = rawQuery;
        for (Map.Entry<String, String> aRawData : rawData.entrySet()) {
            String value = aRawData.getValue();
            if (columnDescription.get(aRawData.getKey()).contains("char")
                    || columnDescription.get(aRawData.getKey()).contains("date")) {
                value = String.format("\'%s\'", value);
            }
            query = query.replace(String.format("%s_val", aRawData.getKey()), value);
        }

        if (query.isEmpty()) {
            System.out.println("Failed to fit raw data into query.");
        }

        return query;

    }

}