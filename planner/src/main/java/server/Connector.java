package server;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import core.DataEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connector {

    private static final String sqlSelect = "SELECT * FROM Activities";
    private static final String sqlInsert = "INSERT INTO Activities (GroupName, Activity, Mandatory, PlannedStart, PlannedEnd, Completion, Responsible, CurrStatus) "
            + "VALUES (?,?,?,?,?,?,?,?)";
    private static final String sqlDelete = "DELETE FROM Activities WHERE Activity = ? AND PlannedStart = ?";

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;
    private static boolean autoUpdate = false;

    private final String hostName = "plannersqlserver.database.windows.net"; // set server address
    private final String dbName = "plannerDatabase"; // set database name
    private final String user = "zephyradmin"; // set database username
    private final String password = "VelkommenZephyr2019!"; // set database password
    private final String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
            + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);




    public Connector() {
        ds = getDataSource();
    }

    private static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }


    private HikariDataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(10);
        config.setDataSourceClassName("com.microsoft.sqlserver.jdbc.SQLServerDataSource");
        config.addDataSourceProperty("serverName", this.hostName);
        config.addDataSourceProperty("databaseName", this.dbName);
        config.addDataSourceProperty("user", this.user);
        config.addDataSourceProperty("password", this.password);

        return new HikariDataSource(config);  //pass in HikariConfig to HikariDataSource
    }

    public static ObservableList<DataEntry> getData() {

        PreparedStatement statement = null;
        DataEntry entry = null;
        ObservableList<DataEntry> obsList = FXCollections.observableArrayList();

        try (Connection connection = getConnection()) {

            connection.setAutoCommit(false);
            // Execute a SQL statement.
            statement = connection.prepareStatement(sqlSelect);
            ResultSet rs = statement.executeQuery();
            connection.commit();

                // Save results from select statement
                while (rs.next()) {

                    Date d1 = convertToUtilDate(rs.getDate(5));
                    Date d2 = convertToUtilDate(rs.getDate(6));
                    Date d3 = convertToUtilDate(rs.getDate(7));

                    entry = new DataEntry(rs.getString(2), rs.getString(3), rs.getString(4), d1, d2, d3, rs.getString(8), rs.getString(9));
                    obsList.add(entry);
                }

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(Connector.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        }

        return obsList;
    }

    public void insertData(DataEntry entry) {

        PreparedStatement statement = null;

        try (Connection connection = getConnection()){

            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sqlInsert);

            statement.setString(1, entry.getGroup());
            statement.setString(2, entry.getActivity());
            statement.setString(3, entry.getMandatory());
            statement.setDate(4, convertToSQLDate(entry.getStart()));
            statement.setDate(5, convertToSQLDate(entry.getPlannedEnd()));
            statement.setDate(6, convertToSQLDate(entry.getEnd()));
            statement.setString(7, entry.getResponsible());
            statement.setString(8, entry.getStatus());

            statement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteData(DataEntry entry) {

        PreparedStatement statement = null;

        try (Connection connection = getConnection()){

            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sqlDelete);

            statement.setString(1, entry.getActivity());
            statement.setDate(2, convertToSQLDate(entry.getStart()));

            statement.executeUpdate();
            connection.commit();
            System.out.println("Row deleted");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Return null if the date is null, otherwise return a java.util.Date
    private static Date convertToUtilDate(java.sql.Date date) {
        return (date == null) ? null : new Date(date.getTime());
    }

    // Return null if the date is null, otherwise return a java.sql.Date
    private static java.sql.Date convertToSQLDate(Date date) {
        return (date == null) ? null : new java.sql.Date(date.getTime());
    }

    // Getter and setter for autoUpdate boolean
    public static boolean getAutoUpdate() {
        return autoUpdate;
    }

    public static void setAutoUpdate(boolean autoUpdate) {
        Connector.autoUpdate = autoUpdate;
    }

    // Queries the database after each period. If there are changes made to the database, the changes will be reflected in the local data.
    // autoUpdate boolean must true for loop to run
    public static void autoUpdate(ObservableList<DataEntry> data) {

        java.util.TimerTask task = new java.util.TimerTask() {
            @Override
            public void run() {

                if (getAutoUpdate()) {

                    ObservableList<DataEntry> temp = getData();

                    if (!data.toString().equals(temp.toString())) {
                        data.clear();
                        data.addAll(temp);
                        System.out.println("Data updated.");
                    }
                }
            }
        };
        java.util.Timer timer = new java.util.Timer(true);// true to run timer as daemon thread
        timer.schedule(task, 0, 500);// Run task every 0.5 second
        /*
        try {
            Thread.sleep(60000); // Cancel task after 1 minute.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.cancel();

         */
    }
}
