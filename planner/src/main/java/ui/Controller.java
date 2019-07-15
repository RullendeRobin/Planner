package ui;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import core.CustomDateCell;
import core.CustomStringCell;
import core.DataEntry;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTreeTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import server.Connector;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private JFXTreeTableView<DataEntry> table;
    private ObservableList<DataEntry> data;
    private TreeItem<DataEntry> root;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private ContextMenu contextMenu;
    private Tooltip tp;
    private TreeTableCell<DataEntry, Object> currentCell;

    private JFXTreeTableColumn<DataEntry, Date> plannedEnd;

    // Methods used by the buttons in PlannerOverview.fxml to filter the data
    @FXML
    private void handleSummary() {
        filterChanged("");
    }

    @FXML
    private void handleFinance() {
        filterChanged("Finance");
    }

    @FXML
    private void handleAssets() {
        filterChanged("Assets");
    }

    @FXML
    private void handleHSE() {
        filterChanged("HSE");
    }

    @FXML
    private void handleStakeholders() {
        filterChanged("Stakeholders");
    }

    @FXML
    private void handleAuthorities() {
        filterChanged("Authorities");
    }

    @FXML
    private void handleGrid() {
        filterChanged("Grid");
    }

    @FXML
    private void handleCivil() {
        filterChanged("Civil");
    }

    @FXML
    private void handleInsurance() {
        filterChanged("Insurance");
    }

    @FXML
    private void handleAccounting() {
        filterChanged("Accounting");
    }

    @FXML
    private void handleRevision() {
        filterChanged("Revision");
    }

    @FXML
    private void handleOnKeyPressed(KeyEvent event) {

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse("2019-05-05");
            d2 = format.parse("2019-06-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DataEntry de1 = new DataEntry("Finance", "Pay alimony", "Yes", new Date(), new Date(), null, "Robin Finstad", "Incomplete");
        DataEntry de2 = new DataEntry("Finance", "Gather investors", "No", new Date(), new Date(), null, "Ola Nordmann", "Incomplete");
        DataEntry de3 = new DataEntry("Accounting", "Update bank details", "Yes", new Date(), d1, d2, "Kari Nordmann", "Complete");


        if (event.getCode().equals(KeyCode.A)) {
            data.clear();
            data.addAll(Connector.getData());
            System.out.println("Fetched data");
        } else if (event.getCode().equals(KeyCode.S)) {
            Connector.insertData(de2);
            System.out.println("Uploaded data");
        } else if (event.getCode().equals(KeyCode.D)) {

            plannedEnd.setSortType(TreeTableColumn.SortType.ASCENDING);
            table.getSortOrder().clear();
            table.getSortOrder().add(plannedEnd);

            table.sort();
        } else if (event.getCode().equals(KeyCode.ESCAPE)) {
            Platform.exit();
            System.exit(0);
        }
    }


    private void filterChanged(String filter) {
        if (filter.isEmpty()) {
            TreeItem<DataEntry> filteredRoot = new TreeItem<>();
            System.out.println(LocalDate.now().plusDays(3));
            table.setRoot(root);
        }
        else {
            System.out.println(root.getChildren());
            TreeItem<DataEntry> filteredRoot = new TreeItem<>();
            filter(root, filter, filteredRoot);
            table.setRoot(filteredRoot);
            System.out.println("Root has been filtered.");
            System.out.println(filteredRoot.getChildren());
            System.out.println(table.getRoot());

        }
    }

    private void filter(TreeItem<DataEntry> root, String filter, TreeItem<DataEntry> filteredRoot) {
        for (TreeItem<DataEntry> child : root.getChildren()) {
            TreeItem<DataEntry> filteredChild = new TreeItem<>();
            filteredChild.setValue(child.getValue());
            filteredChild.setExpanded(true);
            filter(child, filter, filteredChild );
            if (!filteredChild.getChildren().isEmpty() || daysUntilMatch(filteredChild.getValue(), 0)) {
                System.out.println(filteredChild.getValue() + " matches.");
                filteredRoot.getChildren().add(filteredChild);
            }
        }
    }

    private boolean isMatch(DataEntry value, String filter) {
        return value.getGroup().equals(filter);
    }

    private boolean daysUntilMatch(DataEntry value, int days) {
        Date d = Date.from(LocalDate.now().plusDays(days).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return value.getPlannedEnd().before(d) && value.getEnd() == null;
    }



    @SuppressWarnings("Duplicates")
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        new Connector();

        /*
        JFXTreeTableColumn<DataEntry, Integer> id = new JFXTreeTableColumn<>("id");
        id.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TreeTableColumn.CellDataFeatures<DataEntry, Integer> param) {
                return param.getValue().getValue().idProperty().asObject();
            }
        });
        */
        //Create all columns
        JFXTreeTableColumn<DataEntry, String> group = new JFXTreeTableColumn<>("Group");
        group.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().groupProperty();
            }
        });

        JFXTreeTableColumn<DataEntry, String> activity = new JFXTreeTableColumn<>("Activity");
        activity.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().activityProperty();
            }
        });

        JFXTreeTableColumn<DataEntry, String> mandatory = new JFXTreeTableColumn<>("Mandatory");
        mandatory.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().mandatoryProperty();
            }
        });

        JFXTreeTableColumn<DataEntry, Date> start = new JFXTreeTableColumn<>("Planned Start");
        start.setCellValueFactory(new Callback<JFXTreeTableColumn.CellDataFeatures<DataEntry, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(JFXTreeTableColumn.CellDataFeatures<DataEntry, Date> param) {
                return param.getValue().getValue().startProperty();
            }
        });

        plannedEnd = new JFXTreeTableColumn<>("   Planned \nCompletion");
        plannedEnd.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(TreeTableColumn.CellDataFeatures<DataEntry, Date> param) {
                return param.getValue().getValue().plannedEndProperty();
            }
        });

        JFXTreeTableColumn<DataEntry, Date> end = new JFXTreeTableColumn<>("Completion");
        end.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(TreeTableColumn.CellDataFeatures<DataEntry, Date> param) {
                return param.getValue().getValue().endProperty();
            }
        });

        JFXTreeTableColumn<DataEntry, String> responsible = new JFXTreeTableColumn<>("Responsible");
        responsible.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().responsibleProperty();
            }
        });

        JFXTreeTableColumn<DataEntry, String> status = new JFXTreeTableColumn<>("Status");
        status.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().statusProperty();
            }
        });

        data = FXCollections.observableArrayList();
        root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);

        table.getColumns().setAll(group, activity, mandatory, start, plannedEnd, end, responsible, status);
        table.setEditable(true);
        table.setRoot(root);
        table.setShowRoot(false);


        int i = 0;
        for (TreeTableColumn col : table.getColumns()) {
            col.setId(Integer.toString(i));
            col.setPrefWidth(100);
            i++;
        }

        //List with options for the group-combobox
        ObservableList<String> groupComboList = FXCollections.observableArrayList();
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

        //Cell factory for the group-column, with a custom combobox-cell
        Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>> groupCellFactory =
                new Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>>() {
                    @Override
                    public TreeTableCell<DataEntry, String> call(TreeTableColumn jfxTreeTableColumn) {
                        ComboBoxTreeTableCell cell = new ComboBoxTreeTableCell<>(groupComboList);
                        cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                        return cell;
                    }
                };

        //List with options for the mandatory-combobox
        ObservableList<String> mandatoryComboList = FXCollections.observableArrayList();
        mandatoryComboList.add("Yes");
        mandatoryComboList.add("No");

        //Cell factory for the mandatory-column, with a custom combobox-cell
        Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>> mandatoryCellFactory =
                new Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>>() {
                    @Override
                    public TreeTableCell<DataEntry, String> call(TreeTableColumn jfxTreeTableColumn) {
                        ComboBoxTreeTableCell cell = new ComboBoxTreeTableCell<>(mandatoryComboList);
                        cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                        return cell;
                    }
                };

        //List with options for the status-combobox
        ObservableList<String> statusComboList = FXCollections.observableArrayList();
        statusComboList.add("Complete");
        statusComboList.add("Incomplete");

        //Cellfactory for the status-column, with a custom combobox-cell
        Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>> statusCellFactory =
                new Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>>() {
                    @Override
                    public TreeTableCell<DataEntry, String> call(TreeTableColumn jfxTreeTableColumn) {
                        ComboBoxTreeTableCell cell = new ComboBoxTreeTableCell<>(statusComboList) {
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

        //Cellfactory for columns containing dates, with a custom date-cell
         Callback<TreeTableColumn<DataEntry, Date>, TreeTableCell<DataEntry, Date>> dateCellFactory =
                new Callback<TreeTableColumn<DataEntry, Date>, TreeTableCell<DataEntry, Date>>() {
                    @Override
                    public TreeTableCell<DataEntry, Date> call(TreeTableColumn jfxTreeTableColumn) {
                        CustomDateCell cell = new CustomDateCell();
                        cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                        return cell;
                    }
                };


        //Cell factory for columns containing strings, with a custom string-cell
        Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>> stringCellFactory =
                new Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>>() {

                    @Override
                    public TreeTableCell<DataEntry, String> call(TreeTableColumn jfxTreeTableColumn) {

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
                    }
                };

        // Settings factories for all the columns
        group.setCellFactory(groupCellFactory);
        activity.setCellFactory(stringCellFactory);
        mandatory.setCellFactory(mandatoryCellFactory);
        start.setCellFactory(dateCellFactory);
        plannedEnd.setCellFactory(dateCellFactory);
        end.setCellFactory(dateCellFactory);
        responsible.setCellFactory(stringCellFactory);
        status.setCellFactory(statusCellFactory);

        // Queries the database with any changes made whenever the commitEdit() method is called by a cell
        group.setOnEditCommit(evt -> Connector.updateData(evt.getNewValue(), table.getSelectionModel().getSelectedItem().getValue(), Connector.groupUpdate));
        activity.setOnEditCommit(evt -> Connector.updateData(evt.getNewValue(), table.getSelectionModel().getSelectedItem().getValue(), Connector.activityUpdate));
        mandatory.setOnEditCommit(evt -> Connector.updateData(evt.getNewValue(), table.getSelectionModel().getSelectedItem().getValue(), Connector.mandatoryUpdate));
        start.setOnEditCommit(evt -> Connector.updateData(evt.getNewValue(), table.getSelectionModel().getSelectedItem().getValue(), Connector.startUpdate));
        plannedEnd.setOnEditCommit(evt -> Connector.updateData(evt.getNewValue(), table.getSelectionModel().getSelectedItem().getValue(), Connector.plannedEndUpdate));
        end.setOnEditCommit(evt -> Connector.updateData(evt.getNewValue(), table.getSelectionModel().getSelectedItem().getValue(), Connector.endUpdate));
        responsible.setOnEditCommit(evt -> Connector.updateData(evt.getNewValue(), table.getSelectionModel().getSelectedItem().getValue(), Connector.responsibleUpdate));
        status.setOnEditCommit(evt -> Connector.updateData(evt.getNewValue(), table.getSelectionModel().getSelectedItem().getValue(), Connector.statusUpdate));




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
        item1.setOnAction((action) -> {
            try {
                deleteContent(currentCell);
            } catch (NullPointerException e) {
                System.out.println("No cell selected.");
            }
        });
        MenuItem item2 = new MenuItem("Delete row");
        item2.setOnAction((action) -> Connector.deleteData(table.getSelectionModel().getSelectedItem().getValue()));

        MenuItem separator = new SeparatorMenuItem();
        contextMenu.getItems().addAll(item1, separator, item2);
    }

    // Gets the correct coordinates of the mouse and displays the contextmenu
    public void showContextMenu(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY ) {
            contextMenu.show(table, table.localToScreen(table.getBoundsInLocal()).getMinX()+ event.getX(),
                    table.localToScreen(table.getBoundsInLocal()).getMinY() + event.getY());
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


    class MyEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent t) {

            if (t.getButton() == MouseButton.SECONDARY){
                try {

                    currentCell = (TreeTableCell) t.getSource();
                    currentCell.getTreeTableRow().getTreeItem().getValue();
                    contextMenu.getItems().get(0).setDisable(false);
                    contextMenu.getItems().get(2).setDisable(false);

                } catch (NullPointerException e) {
                    System.out.println("Nullpointer");
                    contextMenu.getItems().get(0).setDisable(true);
                    contextMenu.getItems().get(2).setDisable(true);
                }
            } else {
                contextMenu.hide();

            }
        }
    }
}