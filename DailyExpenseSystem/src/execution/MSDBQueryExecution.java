package execution;

import java.sql.*;
import java.util.*;

public class MSDBQueryExecution extends QueryExecution {

    public MSDBQueryExecution() {
    }

    public boolean executeInsertQuery(String query) {
        try (Connection con = DriverManager.getConnection(connection_url); Statement stmt = con.createStatement();) {
            stmt.executeUpdate(query);
            isExecuted = true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(String.format("Failed to execute insert query as below:\n%s", query));
        }

        return isExecuted;
    }

    public boolean executeSelectQuery(String query) {
        try (Connection con = DriverManager.getConnection(connection_url); Statement stmt = con.createStatement();) {
            ResultSet result = stmt.executeQuery(query);
            ResultSetMetaData meta = result.getMetaData();

            List<String> columns = new ArrayList<String>();
            for (int columnIndex = 1; columnIndex <= meta.getColumnCount(); columnIndex++) {
                columns.add(meta.getColumnName(columnIndex));
            }

            List<List<String>> records = new ArrayList<List<String>>();
            while (result.next()) {
                List<String> record = new ArrayList<String>();
                for (int dataIndex = 1; dataIndex <= meta.getColumnCount(); dataIndex++) {
                    record.add(result.getString(dataIndex));
                }
                records.add(record);
            }

            System.out.println(String.join(" : ", columns));
            for (List<String> oneRecord : records) {
                System.out.println(String.join(",", oneRecord));
            }

            isExecuted = true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(String.format("Failed to execute select query as below:\n%s", query));
        }

        return isExecuted;
    }

    public boolean executeDeleteQuery(String query) {
        try (Connection con = DriverManager.getConnection(connection_url); Statement stmt = con.createStatement();) {
            stmt.executeUpdate(query);
            isExecuted = true;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(String.format("Failed to execute delete query as below:\n%s", query));
        }

        return isExecuted;
    }

    public Map<String, String> executeBasicInfoQuery(String query) {
        Map<String, String> columnDescription = new LinkedHashMap<String, String>();

        try (Connection con = DriverManager.getConnection(connection_url); Statement stmt = con.createStatement();) {
            ResultSet result = stmt.executeQuery(query);
            ResultSetMetaData meta = result.getMetaData();

            for (int index = 1; index <= meta.getColumnCount(); index++) {
                columnDescription.put(meta.getColumnName(index), meta.getColumnTypeName(index));
            }

            columnDescription.remove("id");
            columnDescription.put("from_date", "datetime");
            columnDescription.put("to_date", "datetime");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to reach columns description.");
        }

        return columnDescription;
    }
}