package ui;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import core.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTreeTableCell;
import javafx.scene.control.cell.ProgressBarTreeTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import server.Connector;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class OverviewController extends Application implements Initializable {

    @FXML
    private JFXTreeTableView<DataEntry> table;

    @FXML
    private TextField textField;

    private TreeTableCell<DataEntry, Object> currentCell;

    private ContextMenu contextMenu;
    private Tooltip tp;

    private ObservableList<String> groupComboList;
    private ObservableList<String> mandatoryComboList;
    private ObservableList<String> responsibleComboList;

    private Properties properties;
    private String summaryDays;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("PlannerOverview.fxml"));
        primaryStage.setTitle("Planner");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().addAll(
                new Image(getClass().getResourceAsStream("zephyr_logo16x16.png")),
                new Image(getClass().getResourceAsStream("zephyr_logo32x32.png")),
                new Image(getClass().getResourceAsStream("zephyr_logo64x64.png")));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    // Methods used by the buttons in PlannerOverview.fxml to filter the data
    @FXML
    private void handleSummary() {
        filterOnDaysUntilCompletion(summaryDays);
        sortColumn(4, true);
    }

    @FXML
    private void handleFinance() {
        filterOnGroup("Finance");
    }

    @FXML
    private void handleAssets() {
        filterOnGroup("Assets");
    }

    @FXML
    private void handleHSE() {
        filterOnGroup("HSE");
    }

    @FXML
    private void handleStakeholders() {
        filterOnGroup("Stakeholders");
    }

    @FXML
    private void handleAuthorities() {
        filterOnGroup("Authorities");
    }

    @FXML
    private void handleGrid() {
        filterOnGroup("Grid");
    }

    @FXML
    private void handleCivil() {
        filterOnGroup("Civil");
    }

    @FXML
    private void handleInsurance() {
        filterOnGroup("Insurance");
    }

    @FXML
    private void handleAccounting() {
        filterOnGroup("Accounting");
    }

    @FXML
    private void handleRevision() {
        filterOnGroup("Revision");
    }

    @FXML
    private void handleAll() {
        filterOnGroup("");
    }

    @FXML
    private void handleOnKeyPressed(KeyEvent event) {

        if (event.getCode().equals(KeyCode.A)) {
            table.refresh();
        } else if (event.getCode().equals(KeyCode.S)) {

        } else if (event.getCode().equals(KeyCode.ESCAPE)) {
            Platform.exit();
            System.exit(0);
        }
    }

    public void showAddEntry() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddEntry.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            AddEntryController controller = fxmlLoader.getController();
            controller.setGroupComboList(groupComboList);
            controller.setResponsibleComboList(Connector.getEmployees());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add entry");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.getIcons().addAll(
                    new Image(getClass().getResourceAsStream("zephyr_logo16x16.png")),
                    new Image(getClass().getResourceAsStream("zephyr_logo32x32.png")),
                    new Image(getClass().getResourceAsStream("zephyr_logo64x64.png")));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void showAbout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("About.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("About");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.getIcons().addAll(
                    new Image(getClass().getResourceAsStream("zephyr_logo16x16.png")),
                    new Image(getClass().getResourceAsStream("zephyr_logo32x32.png")),
                    new Image(getClass().getResourceAsStream("zephyr_logo64x64.png")));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void showEmployees() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ManageEmployees.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Manage employees");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.getIcons().addAll(
                    new Image(getClass().getResourceAsStream("zephyr_logo16x16.png")),
                    new Image(getClass().getResourceAsStream("zephyr_logo32x32.png")),
                    new Image(getClass().getResourceAsStream("zephyr_logo64x64.png")));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void showSettings() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Settings.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            SettingsController controller = fxmlLoader.getController();
            controller.setDaysField(summaryDays);
            controller.setProperties(properties);
            controller.setController(this);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Settings");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.getIcons().addAll(
                    new Image(getClass().getResourceAsStream("zephyr_logo16x16.png")),
                    new Image(getClass().getResourceAsStream("zephyr_logo32x32.png")),
                    new Image(getClass().getResourceAsStream("zephyr_logo64x64.png")));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void showRepeat() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Repeat.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            RepeatController controller = fxmlLoader.getController();
            controller.setEntry(table.getSelectionModel().getSelectedItem().getValue());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Repeat entry");
            stage.setScene(new Scene(root1));
            stage.setResizable(false);
            stage.getIcons().addAll(
                    new Image(getClass().getResourceAsStream("zephyr_logo16x16.png")),
                    new Image(getClass().getResourceAsStream("zephyr_logo32x32.png")),
                    new Image(getClass().getResourceAsStream("zephyr_logo64x64.png")));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void sortColumn(int column, boolean ascending) {

        TreeTableColumn<DataEntry, ?> col = table.getColumns().get(column);
        if (ascending) {
            col.setSortType(TreeTableColumn.SortType.ASCENDING);
        } else {
            col.setSortType(TreeTableColumn.SortType.DESCENDING);
        }
        table.getSortOrder().clear();
        table.getSortOrder().add(col);
        table.sort();
    }

    private void filterOnDaysUntilCompletion(String days) {
        Date date = Date.from(LocalDate.now().plusDays(1 + Integer.parseInt(days)).atStartOfDay(ZoneId.systemDefault()).toInstant());
        table.setPredicate(entry -> entry.getValue().getPlannedEnd().before(date) && entry.getValue().getEnd() == null);
    }

    private void filterOnGroup(String filter) {
        table.setPredicate(entry -> entry.getValue().getGroup().contains(filter));
        table.getSortOrder().clear();
    }

    private void filterOnAll(String filter) {
        table.setPredicate(entry -> entry.getValue().toString().toLowerCase().contains(filter.toLowerCase()));
    }



    @SuppressWarnings("Duplicates")
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        new Connector();

        properties = new Properties();
        properties.loadProperties();

        summaryDays = properties.getConfigProps().getProperty("summaryDays");

        textField.textProperty().addListener((o,oldVal,newVal)-> filterOnAll(newVal));


        // Create all columns
        JFXTreeTableColumn<DataEntry, String> group = new JFXTreeTableColumn<>("Group");
        JFXTreeTableColumn<DataEntry, String> activity = new JFXTreeTableColumn<>("Activity");
        JFXTreeTableColumn<DataEntry, String> mandatory = new JFXTreeTableColumn<>("Mandatory");
        JFXTreeTableColumn<DataEntry, Date> start = new JFXTreeTableColumn<>("Planned Start");
        JFXTreeTableColumn<DataEntry, Date> plannedEnd = new JFXTreeTableColumn<>("   Planned \nCompletion");
        JFXTreeTableColumn<DataEntry, Date> end = new JFXTreeTableColumn<>("Completion");
        JFXTreeTableColumn<DataEntry, String> responsible = new JFXTreeTableColumn<>("Responsible");
        JFXTreeTableColumn<DataEntry, String> status = new JFXTreeTableColumn<>("Status");
        JFXTreeTableColumn<DataEntry, Double> progress = new JFXTreeTableColumn<>("Progress");
        JFXTreeTableColumn<DataEntry, String> repeat = new JFXTreeTableColumn<>("Repeated");

        // Binding together the cells value with the corresponding value in a DateEntry
        group.setCellValueFactory(param -> param.getValue().getValue().groupProperty());
        activity.setCellValueFactory(param -> param.getValue().getValue().activityProperty());
        mandatory.setCellValueFactory(param -> param.getValue().getValue().mandatoryProperty());
        start.setCellValueFactory(param -> param.getValue().getValue().startProperty());
        plannedEnd.setCellValueFactory(param -> param.getValue().getValue().plannedEndProperty());
        end.setCellValueFactory(param -> param.getValue().getValue().endProperty());
        responsible.setCellValueFactory(param -> param.getValue().getValue().responsibleProperty());
        status.setCellValueFactory(param -> param.getValue().getValue().statusProperty());
        progress.setCellValueFactory(param -> param.getValue().getValue().progressProperty().asObject());
        repeat.setCellValueFactory(param -> param.getValue().getValue().repeatProperty());


        // Setting data, root and all table-columns
        ObservableList<DataEntry> data = FXCollections.observableArrayList();
        TreeItem<DataEntry> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);

        table.getColumns().setAll(group, activity, mandatory, repeat, start, plannedEnd, end, responsible, /*progress,*/ status);
        table.setEditable(true);
        table.setRoot(root);
        table.setShowRoot(false);

        // Giving each columns a unique identifier and setting preferred width
        int i = 0;
        for (TreeTableColumn col : table.getColumns()) {
            col.setId(Integer.toString(i));
            col.setPrefWidth(100);

            i++;
        }
        mandatory.setMinWidth(75);
        mandatory.setPrefWidth(100);
        mandatory.setMaxWidth(200);

        repeat.setMinWidth(75);
        repeat.setPrefWidth(100);
        repeat.setMaxWidth(200);

        // List with options for the group-combobox
        groupComboList = FXCollections.observableArrayList();
        groupComboList.add("Finance");
        groupComboList.add("Assets");
        groupComboList.add("HSE");
        groupComboList.add("Stakeholders");
        groupComboList.add("Authorities");
        groupComboList.add("Grid");
        groupComboList.add("Civil");
        groupComboList.add("Insurance");
        groupComboList.add("Accounting");
        groupComboList.add("Revision");

        // Cell factory for the group-column, with a custom combobox-cell
        Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>> groupCellFactory =
                jfxTreeTableColumn -> {
                    CustomComboBoxCell cell = new CustomComboBoxCell(this, 0);
                    cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                    return cell;
                };

        // List with options for the mandatory-combobox
        mandatoryComboList = FXCollections.observableArrayList();
        mandatoryComboList.add("Yes");
        mandatoryComboList.add("No");

        // Cell factory for the mandatory-column, with a custom combobox-cell
        Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>> mandatoryCellFactory =
                jfxTreeTableColumn -> {
                    CustomComboBoxCell cell = new CustomComboBoxCell(this, 1);
                    cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                    return cell;
                };

        // Cell factory for an optional progress-column
        Callback<TreeTableColumn<DataEntry, Double>, TreeTableCell<DataEntry, Double>> progressCellFactory =
                jfxTreeTableColumn -> {
                    ProgressBarTreeTableCell<DataEntry> cell = new ProgressBarTreeTableCell<>();
                    cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                    return cell;
                };

        // Cell factory for responsible-column and fetching of employees from the database
        responsibleComboList = Connector.getEmployees();
        Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>> responsibleCellFactory =
                jfxTreeTableColumn -> {
                    CustomComboBoxCell cell = new CustomComboBoxCell(this, 2);
                    cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                    return cell;
                };


        // List with options for the status-combobox
        ObservableList<String> statusComboList = FXCollections.observableArrayList();
        statusComboList.add("Complete");
        statusComboList.add("Incomplete");

        // Cell factory for the status-column, with a custom combobox-cell
        Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>> statusCellFactory =
                new Callback<>() {
                    @Override
                    public TreeTableCell<DataEntry, String> call(TreeTableColumn jfxTreeTableColumn) {
                        ComboBoxTreeTableCell<DataEntry, String> cell = new ComboBoxTreeTableCell<>(statusComboList) {
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);

                                if (("Complete").equals(getText())) {
                                    this.setStyle("-fx-text-fill: rgb(0,201,0);");
                                } else {
                                    this.setStyle("-fx-text-fill: rgb(231,0,0)");
                                }
                            }
                        };
                        cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                        return cell;
                    }


                };

        // Cell factory for columns containing dates, with a custom date-cell
         Callback<TreeTableColumn<DataEntry, Date>, TreeTableCell<DataEntry, Date>> dateCellFactory =
                 jfxTreeTableColumn -> {
                     CustomDateCell cell = new CustomDateCell();
                     cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                     return cell;
                 };


        // Cell factory for columns containing strings, with a custom string-cell
        Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>> stringCellFactory =
                jfxTreeTableColumn -> {

                    CustomStringCell cell = new CustomStringCell();
                    tp = new Tooltip();

                    cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                    cell.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
                        if (cell.getText() == null) {
                            Tooltip.uninstall(cell, tp);
                        } else {
                            tp.setText(cell.getText());
                            Tooltip.install(cell, tp);
                        }
                    });

                    return cell;
                };

        // Assigning cell factories to all the columns
        group.setCellFactory(groupCellFactory);
        activity.setCellFactory(stringCellFactory);
        mandatory.setCellFactory(mandatoryCellFactory);
        start.setCellFactory(dateCellFactory);
        plannedEnd.setCellFactory(dateCellFactory);
        end.setCellFactory(dateCellFactory);
        responsible.setCellFactory(responsibleCellFactory);
        status.setCellFactory(statusCellFactory);
        progress.setCellFactory(progressCellFactory);

        // Queries the database with any changes made whenever the commitEdit() method is called by a cell
        group.setOnEditCommit(evt -> Connector.updateData(evt.getNewValue(), table.getSelectionModel().getSelectedItem().getValue(), Connector.groupUpdate));
        activity.setOnEditCommit(evt -> Connector.updateData(evt.getNewValue(), table.getSelectionModel().getSelectedItem().getValue(), Connector.activityUpdate));
        mandatory.setOnEditCommit(evt -> Connector.updateData(evt.getNewValue(), table.getSelectionModel().getSelectedItem().getValue(), Connector.mandatoryUpdate));
        start.setOnEditCommit(evt -> Connector.updateData(evt.getNewValue(), table.getSelectionModel().getSelectedItem().getValue(), Connector.startUpdate));
        plannedEnd.setOnEditCommit(evt -> Connector.updateData(evt.getNewValue(), table.getSelectionModel().getSelectedItem().getValue(), Connector.plannedEndUpdate));
        end.setOnEditCommit(evt -> Connector.updateData(evt.getNewValue(), table.getSelectionModel().getSelectedItem().getValue(), Connector.endUpdate));
        responsible.setOnEditCommit(evt -> Connector.updateData(evt.getNewValue(), table.getSelectionModel().getSelectedItem().getValue(), Connector.responsibleUpdate));
        status.setOnEditCommit(evt -> Connector.updateData(evt.getNewValue(), table.getSelectionModel().getSelectedItem().getValue(), Connector.statusUpdate));

        // Gets the newest list of employees from the database each time onEditStart() is called
        responsible.setOnEditStart(evt -> responsibleComboList = Connector.getEmployees());




        // Initialize contextmenu
        initContextMenu();

        // Sets autoUpdate boolean to true and starts the autoUpdate method in Connector
        Connector.setAutoUpdate(true);
        Connector.autoUpdate(data);
    }

    // Creates a contextmenu and fills it with items
    private void initContextMenu() {
        contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Delete content of cell");
        item1.setOnAction(event -> {
            try {
                deleteContent(currentCell);
            } catch (NullPointerException e) {
                System.out.println("No cell selected.");
            }
        });
        MenuItem item2 = new MenuItem("Delete row");
        item2.setOnAction(event -> deleteRow());

        MenuItem item3 = new MenuItem("New entry");
        item3.setOnAction(event -> showAddEntry());

        MenuItem item4 = new MenuItem("Repeat entry");
        item4.setOnAction(event -> showRepeat());

        MenuItem item5 = new MenuItem("Delete repeats");
        item5.setOnAction(event -> deleteRepeat());

        MenuItem separator = new SeparatorMenuItem();
        contextMenu.getItems().addAll(item3, item4, item1, item5, separator, item2);
    }

    // Gets the correct coordinates of the mouse and displays the contextmenu
    public void showContextMenu(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY) {
            contextMenu.show(table, table.localToScreen(table.getBoundsInLocal()).getMinX()+ event.getX(),
                    table.localToScreen(table.getBoundsInLocal()).getMinY() + event.getY());
        }
    }

    public void deleteRow() {
        try {
            Connector.deleteData(table.getSelectionModel().getSelectedItem().getValue());
        } catch (NullPointerException e) {
            System.out.println("No row selected");
        }
    }

    public void deleteRepeat() {
        try {
            DataEntry entry = table.getSelectionModel().getSelectedItem().getValue();
            Connector.updateData("No", entry, Connector.repeatUpdate);
            Connector.deleteRepeat(entry);
        } catch (NullPointerException e) {
            System.out.println("No row selected");
        }
    }

    private void deleteContent(TreeTableCell cell) {
        int columnId = Integer.parseInt(cell.getId());
        switch (columnId) {
            case 0:
            case 2:
            case 7:
                System.out.println("This cell can't be deleted.");
                break;
            default:
                currentCell.startEdit();
                currentCell.commitEdit(null);
        }
    }

    public void close() {
        Platform.exit();
        System.exit(0);
    }

    public ObservableList<String> getResponsibleComboList() {
        return responsibleComboList;
    }

    public ObservableList<String> getGroupComboList() {
        return groupComboList;
    }

    public ObservableList<String> getMandatoryComboList() {
        return mandatoryComboList;
    }

    public void setSummaryDays(String summaryDays) {
        this.summaryDays = summaryDays;
    }

    class MyEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent t) {

            if (t.getButton() == MouseButton.SECONDARY){
                try {
                    currentCell = (TreeTableCell) t.getSource();
                    String id = currentCell.getTableColumn().getId();
                    if (currentCell.getText() == null || id.equals("0") || id.equals("2") || id.equals("7")) {
                        contextMenu.getItems().get(2).setDisable(true);
                        currentCell.getTreeTableRow().getTreeItem().getValue();
                    } else {
                        contextMenu.getItems().get(2).setDisable(false);
                    }
                    contextMenu.getItems().get(5).setDisable(false);
                    if (table.getSelectionModel().getSelectedItem().getValue().getRepeat().equals("Yes")) {
                        contextMenu.getItems().get(3).setDisable(false);
                    } else {
                        contextMenu.getItems().get(3).setDisable(true);
                    }

                } catch (NullPointerException e) {
                    System.out.println("Nullpointer");
                    contextMenu.getItems().get(2).setDisable(true);
                    contextMenu.getItems().get(5).setDisable(true);
                }
            } else {
                currentCell = null;
                contextMenu.hide();

            }
        }
    }
}