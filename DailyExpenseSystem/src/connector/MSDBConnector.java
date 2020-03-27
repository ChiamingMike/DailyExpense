package connector;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import loader.ConfPropertyLoader;

public class MSDBConnector implements DBConnector {
    private static final MSDBConnector instance = new MSDBConnector();

    static String CONNECTION_URL;
    static String USER;
    static String PASSWORD;
    static String SERVER_NAME;
    static String PORT;
    static String DB_NAME;

    private MSDBConnector() {
        this.setProperty();
        this.connectDatabase();
        this.setConnectionUrl();
    }

    public static MSDBConnector getInstance() {
        return instance;
    }

    public void setProperty() {
        try {
            System.out.println("Loading authorization infromation from properties...");
            ConfPropertyLoader loader = new ConfPropertyLoader();
            USER = loader.getValue("USER");
            PASSWORD = loader.getValue("PASSWORD");
            SERVER_NAME = loader.getValue("SERVER_NAME");
            PORT = loader.getValue("PORT");
            DB_NAME = loader.getValue("DATABASE_NAME");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to get infromation from properties.");
        }
    }

    public void connectDatabase() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser(USER);
        ds.setPassword(PASSWORD);
        ds.setServerName(SERVER_NAME);
        ds.setPortNumber(Integer.parseInt(PORT));
        ds.setDatabaseName(DB_NAME);

        try (Connection con = ds.getConnection();) {
            DatabaseMetaData dbmd = con.getMetaData();
            System.out.println("Login to " + dbmd.getDatabaseProductName() + " Successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect database.");
        }
    }

    public void setConnectionUrl() {
        CONNECTION_URL = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;user=%s;password=%s", SERVER_NAME, PORT,
                DB_NAME, USER, PASSWORD);
    }

    public String getConnectionUrl() {
        if (CONNECTION_URL.isEmpty()) {
            System.out.println("Connection URL doesn't exist.");
        }

        return CONNECTION_URL;
    }
}
