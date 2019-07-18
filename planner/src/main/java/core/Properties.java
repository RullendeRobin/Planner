package core;

import java.io.*;

public class Properties {

    private String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private String propsPath = rootPath + "config.properties";
    private java.util.Properties configProps;

    public void loadProperties() {
        java.util.Properties defaultProps = new java.util.Properties();
        // sets default properties
        defaultProps.setProperty("summaryDays", "10");

        configProps = new java.util.Properties(defaultProps);

        // Loads properties from file
        try (InputStream inputStream = new FileInputStream(propsPath)){
            configProps.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveProperties(String days) {
        configProps.setProperty("summaryDays", days);

        try (OutputStream outputStream = new FileOutputStream(propsPath)){
            configProps.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public java.util.Properties getConfigProps() {
        return configProps;
    }
}
