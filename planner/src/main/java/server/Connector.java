package server;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import core.DataEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connector {

    // SQL queries
    private static final String sqlSelect = "SELECT * FROM Activities";
    private static final String sqlInsert = "INSERT INTO Activities (GroupName, Activity, Mandatory, PlannedStart, PlannedEnd, Completion, Responsible, CurrStatus) "
            + "VALUES (?,?,?,?,?,?,?,?)";
    private static final String sqlDelete = "DELETE FROM Activities WHERE ID = ?";
    public static final String groupUpdate = "UPDATE Activities SET GroupName = ? WHERE ID = ?";
    public static final String activityUpdate = "UPDATE Activities SET Activity = ? WHERE ID = ?";
    public static final String mandatoryUpdate = "UPDATE Activities SET Mandatory = ? WHERE ID = ?";
    public static final String startUpdate = "UPDATE Activities SET PlannedStart = ? WHERE ID = ?";
    public static final String endUpdate = "UPDATE Activities SET Completion = ? WHERE ID = ?";
    public static final String responsibleUpdate = "UPDATE Activities SET Responsible = ? WHERE ID = ?";
    public static final String statusUpdate = "UPDATE Activities SET CurrStatus = ? WHERE ID = ?";

    public static final String plannedEndUpdate = "UPDATE Activities SET PlannedEnd = ? WHERE ID = ?";

    private static HikariDataSource ds;
    private static boolean autoUpdate = false;

    // Database information
    private final String hostName = "plannersqlserver.database.windows.net"; // set server address
    private final String dbName = "plannerDatabase"; // set database name
    private final String user = "zephyradmin"; // set database username
    private final String password = "VelkommenZephyr2019!"; // set database password
    private final String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
            + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);



    // Constructor
    public Connector() {
        ds = getDataSource();
    }

    // Sets up the datasource using a HikariConfig with the database information
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

    //Gets the connection for the HikariDataSource
    private static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    // Queries the database for all the rows in the Activities-table and return them as an ObservableList
    public static ObservableList<DataEntry> getData() {

        PreparedStatement statement = null;
        DataEntry entry = null;
        ObservableList<DataEntry> obsList = FXCollections.observableArrayList();

        // Request connection from pool
        try (Connection connection = getConnection()) {

            connection.setAutoCommit(false);

            // Execute a SQL statement.
            statement = connection.prepareStatement(sqlSelect);
            ResultSet rs = statement.executeQuery();
            connection.commit();

                // Loop through results from the select query and add results to a list
                while (rs.next()) {

                    //Setup dates with the correct format (java.util.Date)
                    Date d1 = convertToUtilDate(rs.getDate(5));
                    Date d2 = convertToUtilDate(rs.getDate(6));
                    Date d3 = convertToUtilDate(rs.getDate(7));

                    entry = new DataEntry(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), d1, d2, d3, rs.getString(8), rs.getString(9));
                    obsList.add(entry);
                }

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(Connector.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        }

        return obsList;
    }

    // Inserts a new DataEntry into the database
    public static void insertData(DataEntry entry) {

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

    // Deletes given DataEntry in the database
    public static void deleteData(DataEntry entry) {

        PreparedStatement statement = null;

        try (Connection connection = getConnection()){

            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sqlDelete);

            statement.setInt(1, entry.getId());

            statement.executeUpdate();
            connection.commit();
            System.out.println("Row deleted");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Updates a value in a DataEntry with the given newValue and query
    public static void updateData(Object newValue, DataEntry entry, String query) {

        PreparedStatement statement = null;

        try (Connection connection = getConnection()){

            setAutoUpdate(false);
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(query);

            // Checks which query is given to make sure parameters are set with the correct type
            switch (query){
                case endUpdate:
                    statement.setDate(1, convertToSQLDate((Date) newValue));
                    statement.setInt(2, entry.getId());

                    PreparedStatement statement2 = connection.prepareStatement(statusUpdate);
                    if (newValue == null) {
                        statement2.setString(1, "Incomplete");
                    } else {
                        statement2.setString(1, "Complete");
                    }
                    statement2.setInt(2, entry.getId());
                    statement2.executeUpdate();
                    break;
                case startUpdate:
                case plannedEndUpdate:
                    statement.setDate(1, convertToSQLDate((Date) newValue));
                    statement.setInt(2, entry.getId());
                    break;
                default:
                    statement.setString(1, (String) newValue);
                    statement.setInt(2, entry.getId());
                    break;
            }

            statement.executeUpdate();
            connection.commit();
            System.out.println("updateData");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            setAutoUpdate(true);
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

                    if (!data.equals(temp)) {
                        System.out.println("Difference: " + StringUtils.difference(data.toString(), temp.toString()));

                        data.clear();
                        data.addAll(temp);

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
