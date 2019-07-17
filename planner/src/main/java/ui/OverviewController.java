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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class OverviewController extends Application implements Initializable {

    @FXML
    private JFXTreeTableView<DataEntry> table;
    private ObservableList<DataEntry> data;
    private TreeItem<DataEntry> root;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private ContextMenu contextMenu;
    private Tooltip tp;
    private TreeTableCell<DataEntry, Object> currentCell;

    private JFXTreeTableColumn<DataEntry, Date> plannedEnd;
    private ObservableList<String> groupComboList;
    private ObservableList<String> responsibleComboList;

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
        filterChanged("10", true);
        sortColumn(4, true);
    }

    @FXML
    private void handleFinance() {
        filterChanged("Finance", false);
    }

    @FXML
    private void handleAssets() {
        filterChanged("Assets", false);
    }

    @FXML
    private void handleHSE() {
        filterChanged("HSE", false);
    }

    @FXML
    private void handleStakeholders() {
        filterChanged("Stakeholders", false);
    }

    @FXML
    private void handleAuthorities() {
        filterChanged("Authorities", false);
    }

    @FXML
    private void handleGrid() {
        filterChanged("Grid", false);
    }

    @FXML
    private void handleCivil() {
        filterChanged("Civil", false);
    }

    @FXML
    private void handleInsurance() {
        filterChanged("Insurance", false);
    }

    @FXML
    private void handleAccounting() {
        filterChanged("Accounting", false);
    }

    @FXML
    private void handleRevision() {
        filterChanged("Revision", false);
    }

    @FXML
    private void handleOnKeyPressed(KeyEvent event) {

        DataEntry de2 = new DataEntry("Finance", "Gather investors", "No", new Date(), new Date(), null, "Ola Nordmann", "Incomplete");

        if (event.getCode().equals(KeyCode.A)) {
            filterChanged("", false);
        } else if (event.getCode().equals(KeyCode.S)) {
            Connector.insertData(de2);
            System.out.println("Uploaded data");
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

            AddEntryController controller = fxmlLoader.getController();

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

    private void sortColumn(int column, boolean ascending) {

        TreeTableColumn<DataEntry, ?> col = table.getColumns().get(column);
        col.setSortType(TreeTableColumn.SortType.ASCENDING);
        table.getSortOrder().clear();
        table.getSortOrder().add(col);
        table.sort();
    }

    private void filterChanged(String filter, boolean filterDates) {
        if (filter.isEmpty()) {
            table.setRoot(root);
        }
        else {
            TreeItem<DataEntry> filteredRoot = new TreeItem<>();
            filter(root, filter, filteredRoot, filterDates);
            table.setRoot(filteredRoot);

        }
    }

    private void filter(TreeItem<DataEntry> root, String filter, TreeItem<DataEntry> filteredRoot, boolean filterDates) {
        for (TreeItem<DataEntry> child : root.getChildren()) {
            TreeItem<DataEntry> filteredChild = new TreeItem<>();
            filteredChild.setValue(child.getValue());
            filteredChild.setExpanded(true);
            filter(child, filter, filteredChild, filterDates);
            if (filterDates) {
                if (!filteredChild.getChildren().isEmpty() || daysUntilMatch(filteredChild.getValue(), filter)) {
                    filteredRoot.getChildren().add(filteredChild);
                }
            } else {
                if (!filteredChild.getChildren().isEmpty() || isMatch(filteredChild.getValue(), filter)) {
                    filteredRoot.getChildren().add(filteredChild);
                }
            }
        }
    }

    private boolean isMatch(DataEntry value, String filter) {
        return value.getGroup().equals(filter);
    }

    private boolean daysUntilMatch(DataEntry value, String days) {
        Date date = Date.from(LocalDate.now().plusDays(Integer.parseInt(days)).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return value.getPlannedEnd().before(date) && value.getEnd() == null;
    }



    @SuppressWarnings("Duplicates")
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        new Connector();

        // Create all columns
        JFXTreeTableColumn<DataEntry, String> group = new JFXTreeTableColumn<>("Group");
        JFXTreeTableColumn<DataEntry, String> activity = new JFXTreeTableColumn<>("Activity");
        JFXTreeTableColumn<DataEntry, String> mandatory = new JFXTreeTableColumn<>("Mandatory");
        JFXTreeTableColumn<DataEntry, Date> start = new JFXTreeTableColumn<>("Planned Start");
        plannedEnd = new JFXTreeTableColumn<>("   Planned \nCompletion");
        JFXTreeTableColumn<DataEntry, Date> end = new JFXTreeTableColumn<>("Completion");
        JFXTreeTableColumn<DataEntry, String> responsible = new JFXTreeTableColumn<>("Responsible");
        JFXTreeTableColumn<DataEntry, String> status = new JFXTreeTableColumn<>("Status");
        JFXTreeTableColumn<DataEntry, Double> progress = new JFXTreeTableColumn<>("Progress");

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


        data = FXCollections.observableArrayList();
        root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);

        table.getColumns().setAll(group, activity, mandatory, start, plannedEnd, end, responsible, /*progress,*/ status);
        table.setEditable(true);
        table.setRoot(root);
        table.setShowRoot(false);


        int i = 0;
        for (TreeTableColumn col : table.getColumns()) {
            col.setId(Integer.toString(i));
            col.setPrefWidth(100);
            i++;
        }

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
                    CustomComboBoxCell cell = new CustomComboBoxCell(groupComboList);
                    cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                    return cell;
                };

        // List with options for the mandatory-combobox
        ObservableList<String> mandatoryComboList = FXCollections.observableArrayList();
        mandatoryComboList.add("Yes");
        mandatoryComboList.add("No");

        // Cell factory for the mandatory-column, with a custom combobox-cell
        Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>> mandatoryCellFactory =
                jfxTreeTableColumn -> {
                    CustomComboBoxCell cell = new CustomComboBoxCell(mandatoryComboList);
                    cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                    return cell;
                };

        Callback<TreeTableColumn<DataEntry, Double>, TreeTableCell<DataEntry, Double>> progressCellFactory =
                jfxTreeTableColumn -> {
                    ProgressBarTreeTableCell<DataEntry> cell = new ProgressBarTreeTableCell<>();
                    cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                    return cell;
                };

        responsibleComboList = Connector.getEmployees();

        Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>> responsibleCellFactory =
                jfxTreeTableColumn -> {
                    CustomComboBoxCell cell = new CustomComboBoxCell(responsibleComboList);
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

        MenuItem separator = new SeparatorMenuItem();
        contextMenu.getItems().addAll(item3, item1, separator, item2);
    }

    // Gets the correct coordinates of the mouse and displays the contextmenu
    public void showContextMenu(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY ) {
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


    class MyEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent t) {

            if (t.getButton() == MouseButton.SECONDARY){
                try {
                    currentCell = (TreeTableCell) t.getSource();
                    String id = currentCell.getTableColumn().getId();
                    if (currentCell.getText() == null || id.equals("0") || id.equals("2") || id.equals("7")) {
                        contextMenu.getItems().get(1).setDisable(true);
                        currentCell.getTreeTableRow().getTreeItem().getValue();
                    } else {
                        contextMenu.getItems().get(1).setDisable(false);
                    }
                    contextMenu.getItems().get(3).setDisable(false);

                } catch (NullPointerException e) {
                    System.out.println("Nullpointer");
                    contextMenu.getItems().get(1).setDisable(true);
                    contextMenu.getItems().get(3).setDisable(true);
                }
            } else {
                contextMenu.hide();

            }
        }
    }
}