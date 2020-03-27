package container;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.*;

import connector.MSDBConnector;
import loader.QueryPropertyLoader;

public class MSDBQueryContainer extends QueryContainer {
    private QueryPropertyLoader loader;
    public String FLAG_QUERY;
    public String ITEMS_QUERY;
    public String ACCOUNT_BOOK_INSERT_QUERY;
    public String ACCOUNT_BOOK_SELECT_QUERY;
    public String ACCOUNT_BOOK_DELETE_QUERY;
    public String ACCOUNT_BOOK_COLUMNS_QUERY;
    private String connection_url;
    Map<String, String> hashdata = new HashMap<String, String>();

    public MSDBQueryContainer() {
        MSDBConnector msdbConnector = MSDBConnector.getInstance();
        connection_url = msdbConnector.getConnectionUrl();

        loader = new QueryPropertyLoader();
        FLAG_QUERY = loader.getValue("FLAG");
        ITEMS_QUERY = loader.getValue("ITEMS");
        ACCOUNT_BOOK_INSERT_QUERY = loader.getValue("ACCOUNT_BOOK_INSERT_QUERY");
        ACCOUNT_BOOK_SELECT_QUERY = loader.getValue("ACCOUNT_BOOK_SELECT_QUERY");
        ACCOUNT_BOOK_DELETE_QUERY = loader.getValue("ACCOUNT_BOOK_DELETE_QUERY");
        ACCOUNT_BOOK_COLUMNS_QUERY = loader.getValue("ACCOUNT_BOOK_COLUMNS");
        if (ITEMS_QUERY.isEmpty() || FLAG_QUERY.isEmpty()) {
            System.out.println("Failed to get information from query.properties.");
            System.out.println("Please check query.properties.");
        }
    }

    public void executeQuery(String query) {
        try (Connection con = DriverManager.getConnection(connection_url); Statement stmt = con.createStatement();) {
            System.out.println("Executing query...");
            ResultSet result = stmt.executeQuery(query);

            if (query.contains("SELECT")) {
                while (result.next()) {
                    hashdata.put(result.getString(1), result.getString(2));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect database.");
        }
    }

    public Map<String, String> getExecutionResult(String query) {
        this.executeQuery(query);
        if (hashdata.isEmpty()) {
            System.out.println("No result.");
        }
        return hashdata;
    }
}