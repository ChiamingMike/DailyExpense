package loader;

public class ConfPropertyLoader extends PropertyLoader {
    private static String PATH = "/resources/conf.properties";

    public ConfPropertyLoader() {
        super(PATH);
    }
}