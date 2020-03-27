package loader;

public class QueryPropertyLoader extends PropertyLoader {
    private static String PATH = "/resources/query.properties";

    public QueryPropertyLoader() {
        super(PATH);
    }
}