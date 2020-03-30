package loader;

import java.io.IOException;
import java.util.Properties;

abstract class PropertyLoader {
    protected Properties conf = null;

    protected PropertyLoader(String path) {
        try {
            conf = new Properties();
            conf.load(this.getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("Failed to load property.");
        }
    }

    public String getValue(String key) {
        return conf.getProperty(key);
    }
}
