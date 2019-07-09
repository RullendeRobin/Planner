package server;

import core.DataEntry;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.util.Date;

public class Connector {

    public DataEntry getData() {

        // Connect to database
        String hostName = "plannersqlserver.database.windows.net"; // update me
        String dbName = "plannerDatabase"; // update me
        String user = "zephyradmin"; // update me
        String password = "VelkommenZephyr2019!"; // update me
        String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
                + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);
        Connection connection = null;
        DataEntry entry = null;

        try {
            connection = DriverManager.getConnection(url);
            String schema = connection.getSchema();
            System.out.println("Successful connection - Schema: " + schema);

            System.out.println("Query data example:");
            System.out.println("=========================================");

            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT * FROM Activities";

            try (Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery(selectSql)) {

                // Print results from select statement
                while (rs.next())
                {
                    Date d1 = converter(rs.getDate(4));
                    Date d2 = converter(rs.getDate(5));
                    Date d3 = converter(rs.getDate(6));

                    entry = new DataEntry(rs.getString(1), rs.getString(2), rs.getString(3), d1, d2, d3, rs.getString(7), rs.getString(8));
                }
                connection.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return entry;
    }

    private Date converter(java.sql.Date date) {
        if (date == null) {
            return null;
        } else {
            return new Date(date.getTime());
        }
    }

}
