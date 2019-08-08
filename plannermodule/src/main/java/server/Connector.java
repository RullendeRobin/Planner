package server;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import core.DataEntry;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ui.OverviewController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connector {

    // SQL queries
    // Table - Activities
    private static final String sqlSelectActivities = "SELECT * FROM Activities WHERE Project = ?";
    private static final String sqlInsert = "INSERT INTO Activities (GroupName, Activity, Mandatory, PlannedStart, PlannedEnd, Completion, Responsible, CurrStatus, RepeatID, Repeat, Project) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
    private static final String sqlDeleteEntry = "DELETE FROM Activities WHERE ID = ?";
    private static final String sqlDeleteRepeat = "DELETE FROM Activities WHERE RepeatID = ?";
    public static final String groupUpdate = "UPDATE Activities SET GroupName = ? WHERE ID = ?";
    public static final String activityUpdate = "UPDATE Activities SET Activity = ? WHERE ID = ?";
    public static final String mandatoryUpdate = "UPDATE Activities SET Mandatory = ? WHERE ID = ?";
    public static final String startUpdate = "UPDATE Activities SET PlannedStart = ? WHERE ID = ?";
    public static final String endUpdate = "UPDATE Activities SET Completion = ? WHERE ID = ?";
    public static final String responsibleUpdate = "UPDATE Activities SET Responsible = ? WHERE ID = ?";
    public static final String statusUpdate = "UPDATE Activities SET CurrStatus = ? WHERE ID = ?";
    public static final String plannedEndUpdate = "UPDATE Activities SET PlannedEnd = ? WHERE ID = ?";
    public static final String repeatUpdate = "UPDATE Activities SET Repeat = ? WHERE ID = ?";

    // Table - Employees
    private static final String sqlSelectEmployees = "SELECT * FROM Employees";
    private static final String sqlInsertEmployee = "INSERT INTO Employees (FullName) "
            + "VALUES (?)";
    private static final String sqlDeleteEmployee = "DELETE FROM Employees WHERE FullName = ?";

    // Table - Projects
    private static final String sqlSelectProjects = "SELECT * FROM Projects";
    private static final String sqlInsertProject = "INSERT INTO Projects (Project, Created)" + "VALUES (?,?)";
    //private static final String sqlDeleteProject = "DELETE FROM Projects WHERE Project = ?";

    //Table - Groups
    private static final String sqlSelectGroups = "SELECT GroupName FROM Groups WHERE Project = ?";
    private static final String sqlInsertGroup = "INSERT INTO Groups (GroupName, Project) "
            + "VALUES (?,?)";
    private static final String sqlDeleteGroup = "DELETE FROM Groups WHERE GroupName = ? AND Project = ?";
    private static final String groupByAsc = " GROUP BY GroupName";

    private static HikariDataSource ds;
    private static boolean autoUpdate = false;
    private static String project;

    private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    // Database information
    private static final String hostName = "plannersqlserver.database.windows.net"; // set server address
    private static final String dbName = "plannerDatabase"; // set database name
    private static final String user = "zephyradmin"; // set database username
    private static final String password = "VelkommenZephyr2019!"; // set database password
    private static final String url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
            + "hostNameInCertificate=*.database.windows.net;loginTimeout=30;", hostName, dbName, user, password);


    // Sets up the datasource using a HikariConfig with the database information
    static {
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(10);
        config.setDataSourceClassName("com.microsoft.sqlserver.jdbc.SQLServerDataSource");
        config.addDataSourceProperty("serverName", hostName);
        config.addDataSourceProperty("databaseName", dbName);
        config.addDataSourceProperty("user", user);
        config.addDataSourceProperty("password", password);

        ds = new HikariDataSource(config);  //pass in HikariConfig to HikariDataSource
    }

    // Constructor
    private Connector() {
    }

    //Gets the connection for the HikariDataSource
    private static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    // Queries the database for all the rows in the Activities-table and returns them as an ObservableList
    public static ObservableList<DataEntry> getData() {

        PreparedStatement statement = null;
        DataEntry entry = null;
        ObservableList<DataEntry> obsList = FXCollections.observableArrayList();

        // Request connection from pool
        try (Connection connection = getConnection()) {

            connection.setAutoCommit(false);

            // Execute a SQL statement.
            statement = connection.prepareStatement(sqlSelectActivities);
            statement.setString(1, project);
            ResultSet rs = statement.executeQuery();
            connection.commit();

                // Loop through results from the select query and add results to a list
                while (rs.next()) {

                    //Setup dates with the correct format (java.util.Date)
                    Date d1 = convertToUtilDate(rs.getDate(5));
                    Date d2 = convertToUtilDate(rs.getDate(6));
                    Date d3 = convertToUtilDate(rs.getDate(7));

                    entry = new DataEntry(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), d1, d2, d3, rs.getString(8), rs.getString(9), rs.getInt(10), rs.getString(11));
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
            statement.setObject(9, null);
            statement.setString(10, "No");
            statement.setString(11, project);

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
            statement = connection.prepareStatement(sqlDeleteEntry);

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

    // Queries the database for all the ros in the Employees-table and returns them as an ObservableList
    public static ObservableList<String> getEmployees() {
        PreparedStatement statement = null;

        ObservableList<String> obsList = FXCollections.observableArrayList();

        // Request connection from pool
        try (Connection connection = getConnection()) {

            connection.setAutoCommit(false);

            // Execute a SQL statement.
            statement = connection.prepareStatement(sqlSelectEmployees);
            ResultSet rs = statement.executeQuery();
            connection.commit();

            // Loop through results from the select query and add results to a list
            while (rs.next()) {

                obsList.add(rs.getString(1));
            }

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(Connector.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        }

        return obsList;
    }

    // Inserts a new Employee into the database
    public static void insertEmployee(String employee) {

        PreparedStatement statement = null;

        try (Connection connection = getConnection()){

            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sqlInsertEmployee);

            statement.setString(1, employee);

            statement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteEmployee(String employee) {

        PreparedStatement statement = null;

        try (Connection connection = getConnection()){

            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sqlDeleteEmployee);

            statement.setString(1, employee);

            statement.executeUpdate();
            connection.commit();
            System.out.println("Employee deleted");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertBatchData(DataEntry entry, int timeframe, TemporalUnit unit, int repetitions) {

        PreparedStatement statement = null;

        try (Connection connection = getConnection()){

            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sqlInsert);

            for (int i = 1; i <= repetitions; i++) {
                statement.setString(1, entry.getGroup());
                statement.setString(2, entry.getActivity());
                statement.setString(3, entry.getMandatory());
                statement.setDate(4, convertToSQLDate(addToDate(entry.getStart(), i*timeframe, unit)));
                statement.setDate(5, convertToSQLDate(addToDate(entry.getPlannedEnd(), i*timeframe, unit)));
                statement.setDate(6, null);
                statement.setString(7, entry.getResponsible());
                statement.setString(8, "Incomplete");
                statement.setInt(9, entry.getId());
                statement.setString(10, "REPEAT");
                statement.setString(11, project);
                statement.addBatch();
            }

            statement.executeBatch();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRepeat(DataEntry entry) {

        PreparedStatement statement = null;

        try (Connection connection = getConnection()){

            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sqlDeleteRepeat);

            statement.setInt(1, entry.getId());

            statement.executeUpdate();
            connection.commit();
            System.out.println("Repeats deleted");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> getProjects() {
        PreparedStatement statement = null;

        ObservableList<String> obsList = FXCollections.observableArrayList();

        // Request connection from pool
        try (Connection connection = getConnection()) {

            connection.setAutoCommit(false);

            // Execute a SQL statement.
            statement = connection.prepareStatement(sqlSelectProjects);
            ResultSet rs = statement.executeQuery();
            connection.commit();

            // Loop through results from the select query and add results to a list
            while (rs.next()) {
                obsList.add(rs.getString(1));
            }

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(Connector.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        }

        return obsList;
    }

    public static void insertProject(String project) {

        PreparedStatement statement = null;

        try (Connection connection = getConnection()){

            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sqlInsertProject);

            statement.setString(1, project);
            statement.setDate(2, convertToSQLDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())));

            statement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> getGroups(boolean sortAsc) {
        PreparedStatement statement = null;

        ObservableList<String> obsList = FXCollections.observableArrayList();

        // Request connection from pool
        try (Connection connection = getConnection()) {

            connection.setAutoCommit(false);

            // Execute a SQL statement.
            if (sortAsc) {
                statement = connection.prepareStatement(sqlSelectGroups + groupByAsc);
            } else {
                statement = connection.prepareStatement(sqlSelectGroups);
            }

            statement.setString(1, project);
            ResultSet rs = statement.executeQuery();
            connection.commit();

            // Loop through results from the select query and add results to a list
            while (rs.next()) {

                obsList.add(rs.getString(1));
            }

        } catch (SQLException e) {
            Logger lgr = Logger.getLogger(Connector.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        }

        return obsList;
    }

    // Inserts a new Group into the database
    public static void insertGroup(String group) {

        PreparedStatement statement = null;

        try (Connection connection = getConnection()){

            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sqlInsertGroup);

            statement.setString(1, group);
            statement.setString(2, project);

            statement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteGroup(String group) {

        PreparedStatement statement = null;

        try (Connection connection = getConnection()){

            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sqlDeleteGroup);

            statement.setString(1, group);
            statement.setString(2, project);

            statement.executeUpdate();
            connection.commit();
            System.out.println("Group deleted");

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

    private static Date addToDate(Date date, int timeFrame, TemporalUnit unit) {

        if (date == null) {
            return null;
        }
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plus(timeFrame, unit);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static void setProject(String project) {
        Connector.project = project;
    }

    // Getter and setter for autoUpdate boolean
    public static boolean getAutoUpdate() {
        return autoUpdate;
    }

    public static void setAutoUpdate(boolean autoUpdate) {
        Connector.autoUpdate = autoUpdate;
    }

    // Queries the database after each period. If there are changes made to the database, the changes will be reflected in the local data.
    // autoUpdate boolean must be true for the loop to run
    public static void autoUpdate(ObservableList<DataEntry> data, OverviewController overviewController) {

        java.util.TimerTask queryTask = new java.util.TimerTask() {
            @Override
            public void run() {

                if (getAutoUpdate()) {

                    ObservableList<DataEntry> temp = getData();

                    if (!data.equals(temp)) {

                        //Platform.runLater(overviewController::saveSort);
                        overviewController.saveSort();
                        overviewController.saveSelection();
                        data.clear();
                        data.addAll(temp);

                        // Apply current filter to the new data
                        // As refreshFilter() modifies UI elements it has to be called using the JavaFX thread
                        Platform.runLater(() -> {
                            overviewController.refreshFilter();
                            overviewController.refreshSort();
                            overviewController.refreshSelection();
                        });
                    }
                }
            }
        };
        // executorService runs queryTask every 0.5 seconds
        executorService.scheduleAtFixedRate(queryTask, 0, 500, TimeUnit.MILLISECONDS);

    }

}
