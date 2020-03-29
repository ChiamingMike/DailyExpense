package execution;

import connector.MSDBConnector;

abstract public class QueryExecution {

    protected String connection_url;
    protected boolean isExecuted = false;

    public QueryExecution() {
        MSDBConnector msdbConnector = MSDBConnector.getInstance();
        connection_url = msdbConnector.getConnectionUrl();
    }

    public boolean executeInsertQuery(String query) {
        return isExecuted;
    }

    public boolean executeSelectQuery(String query) {
        return isExecuted;
    }

    public boolean executeDeleteQuery(String query) {
        return isExecuted;
    }
}