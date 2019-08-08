package core;

import java.io.*;

public class Properties {

    //private String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
    private InputStream rootPath = this.getClass().getClassLoader().getResourceAsStream("config.properties");
    private String propsPath = rootPath + "config.properties";
    private java.util.Properties configProps;

    public void loadProperties() {
        java.util.Properties defaultProps = new java.util.Properties();

        // sets default properties
        defaultProps.setProperty("summaryDays", "10");
        defaultProps.setProperty("groupsAlphabetically", "true");

        configProps = new java.util.Properties(defaultProps);

        // Loads properties from file
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config.properties")){

            if (inputStream != null) {
                configProps.load(inputStream);
            } else {
                System.out.println("Can't find config.properties");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveProperties(String[] props) {
        /*
        URL url = this.getClass().getClassLoader().getResource("config.properties");
        if (url == null) {
            System.out.println("Can't find config.properties");
            return;
        }
        File file = null;
        
        try {
            file = new File(url.toURI());
        } catch (Exception e) {
            System.out.println(e);
        }
        if (file == null) {
            return;
        }

         */

        try (OutputStream outputStream = new FileOutputStream("config.properties")){
            configProps.setProperty("summaryDays", props[0]);
            configProps.setProperty("groupsAlphabetically", props[1]);
            configProps.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public java.util.Properties getConfigProps() {
        return configProps;
    }
}
