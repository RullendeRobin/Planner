package ui;


import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import core.CustomDateCell;
import core.CustomStringCell;
import core.DataEntry;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    @FXML
    private JFXTreeTableView<DataEntry> table;

    ObservableList<DataEntry> data;
    TreeItem<DataEntry> root;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    ContextMenu contextMenu;
    TreeTableCell currentCell;

    Connector c;


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
    private void handleOnKeyPressed(KeyEvent event) throws ParseException {

        if (event.getCode().equals(KeyCode.A)) {
            data.add(c.getData());
            System.out.println("space reg");
        } else if (event.getCode().equals(KeyCode.S)) {
            filterChanged("Finance");
        } else if (event.getCode().equals(KeyCode.D)) {
            filterChanged("");
        } else if (event.getCode().equals(KeyCode.ESCAPE)) {
            Platform.exit();
            System.exit(0);
        }
    }


    private void filterChanged(String filter) {
        if (filter.isEmpty()) {
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
            System.out.println(child.getValue().getGroup());
            TreeItem<DataEntry> filteredChild = new TreeItem<>();
            filteredChild.setValue(child.getValue());
            filteredChild.setExpanded(true);
            filter(child, filter, filteredChild );
            if (!filteredChild.getChildren().isEmpty() || isMatch(filteredChild.getValue(), filter)) {
                System.out.println(filteredChild.getValue() + " matches.");
                filteredRoot.getChildren().add(filteredChild);
            }
        }
    }

    private boolean isMatch(DataEntry value, String filter) {
        return value.getGroup().equals(filter);
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        c = new Connector();

        //Create all columns
        JFXTreeTableColumn<DataEntry, String> group = new JFXTreeTableColumn<>("Group");

        group.setPrefWidth(100);

        group.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().groupProperty();
            }
        });


        JFXTreeTableColumn<DataEntry, String> activity = new JFXTreeTableColumn<>("Activity");
        activity.setId("1");
        activity.setPrefWidth(100);

        activity.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().activityProperty();
            }
        });

        JFXTreeTableColumn<DataEntry, String> mandatory = new JFXTreeTableColumn<>("Mandatory");
        activity.setPrefWidth(100);

        mandatory.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().mandatoryProperty();
            }
        });

        JFXTreeTableColumn<DataEntry, Date> start = new JFXTreeTableColumn<>("Planned Start");
        start.setPrefWidth(100);

        start.setCellValueFactory(new Callback<JFXTreeTableColumn.CellDataFeatures<DataEntry, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(JFXTreeTableColumn.CellDataFeatures<DataEntry, Date> param) {
                return param.getValue().getValue().startProperty();
            }
        });

        start.setOnEditCommit(
                (TreeTableColumn.CellEditEvent<DataEntry, Date> event) -> {
                    TreeItem<DataEntry> currentEdit = table.getTreeItem(event.getTreeTablePosition().getRow());
                    currentEdit.getValue().setStart(event.getNewValue());
                });

        JFXTreeTableColumn<DataEntry, Date> plannedEnd = new JFXTreeTableColumn<>("   Planned \nCompletion");
        plannedEnd.setPrefWidth(100);

        plannedEnd.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(TreeTableColumn.CellDataFeatures<DataEntry, Date> param) {
                return param.getValue().getValue().plannedEndProperty();
            }
        });

        JFXTreeTableColumn<DataEntry, Date> end = new JFXTreeTableColumn<>("Completion");
        end.setPrefWidth(100);

        end.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, Date>, ObservableValue<Date>>() {
            @Override
            public ObservableValue<Date> call(TreeTableColumn.CellDataFeatures<DataEntry, Date> param) {
                return param.getValue().getValue().endProperty();
            }
        });

        JFXTreeTableColumn<DataEntry, String> responsible = new JFXTreeTableColumn<>("Responsible");
        responsible.setPrefWidth(100);

        responsible.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().responsibleProperty();
            }
        });

        JFXTreeTableColumn<DataEntry, String> status = new JFXTreeTableColumn<>("Status");
        status.setPrefWidth(100);

        status.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().statusProperty();
            }
        });



        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse("2019-05-05");
            d2 = format.parse("2019-06-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        data = FXCollections.observableArrayList();
        data.add(new DataEntry("Finance", "Pay alimony", "1", new Date(), new Date(), null, "Robin Finstad", "Incomplete"));
        data.add(new DataEntry("Finance", "Gather investors", "0", new Date(), new Date(), null, "Ola Nordmann", "Incomplete"));
        data.add(new DataEntry("Accounting", "Update bank details", "1", new Date(), d1, d2, "Kari Nordmann", "Complete"));


        root = new RecursiveTreeItem<DataEntry>(data, RecursiveTreeObject::getChildren);

        table.getColumns().setAll(group, activity, mandatory, start, plannedEnd, end, responsible, status);
        table.setEditable(true);
        table.setRoot(root);
        table.setShowRoot(false);

        int i = 0;
        for (TreeTableColumn col : table.getColumns()) {
            col.setId(Integer.toString(i));
            i++;
        }



        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("Complete");
        list.add("Incomplete");
        //status.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(list));

        status.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<DataEntry, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<DataEntry, String> event) {
                TreeItem<DataEntry> currentEdit = table.getTreeItem(event.getTreeTablePosition().getRow());
                currentEdit.getValue().setStatus(event.getNewValue());

            }
        });


        Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>> cbCellFactory =
                new Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>>() {
                    @Override
                    public TreeTableCell<DataEntry, String> call(TreeTableColumn jfxTreeTableColumn) {
                        ComboBoxTreeTableCell cell = new ComboBoxTreeTableCell<>(list) {
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

         Callback<TreeTableColumn<DataEntry, Date>, TreeTableCell<DataEntry, Date>> dateCellFactory =
                new Callback<TreeTableColumn<DataEntry, Date>, TreeTableCell<DataEntry, Date>>() {
                    @Override
                    public TreeTableCell<DataEntry, Date> call(TreeTableColumn jfxTreeTableColumn) {
                        CustomDateCell cell = new CustomDateCell();
                        cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                        return cell;
                    }
                };

        Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>> stringCellFactory =
                new Callback<TreeTableColumn<DataEntry, String>, TreeTableCell<DataEntry, String>>() {
                    @Override
                    public TreeTableCell<DataEntry, String> call(TreeTableColumn jfxTreeTableColumn) {
                        CustomStringCell cell = new CustomStringCell();
                        cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new MyEventHandler());
                        return cell;
                    }
                };


        start.setCellFactory(dateCellFactory);
        plannedEnd.setCellFactory(dateCellFactory);
        end.setCellFactory(dateCellFactory);
        activity.setCellFactory(stringCellFactory);
        mandatory.setCellFactory(stringCellFactory);
        responsible.setCellFactory(stringCellFactory);
        status.setCellFactory(cbCellFactory);


        //Initialize rightclick-/contextmenu
        //initContextMenu();

        contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Delete");
        item1.setOnAction((action) -> {
        try {
            String id = currentCell.getId();
            table.getSelectionModel().getSelectedItem().getValue().setNull(id);
        } catch (NullPointerException e) {
            System.out.println("No cell selected.");
        }
            //table.refresh();
        });
        MenuItem item2 = new MenuItem("task2");
        item2.setOnAction((action) -> {
            System.out.println("2");
        });
        contextMenu.getItems().addAll(item1, item2);
    }

    public void showPopup(MouseEvent event) {
        if (event.getButton() == MouseButton.SECONDARY ) {
            System.out.println(event.getSource().getClass());
            contextMenu.show(table, table.localToScreen(table.getBoundsInLocal()).getMinX()+ event.getX(),
                    table.localToScreen(table.getBoundsInLocal()).getMinY() + event.getY());

        }
    }
    class MyEventHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent t) {
            if (t.getButton() != MouseButton.SECONDARY ){
                System.out.println("wrong");
                return;
            }
            try {
                currentCell = (TreeTableCell) t.getSource();
                int index = currentCell.getIndex();
                var v = currentCell.getTableColumn().getCellObservableValue(index).getValue();
                System.out.println(v);
                System.out.println("start = " + data.get(index) + " " + index);

            } catch (NullPointerException e) {
                System.out.println("Nullpointer");
            }
        }
    }

}