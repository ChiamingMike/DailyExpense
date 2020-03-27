package connector;

interface DBConnector {

    public void setProperty();

    public void connectDatabase();

    public void setConnectionUrl();

    public String getConnectionUrl();
}