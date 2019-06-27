package ui;


import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.ComboBoxTreeTableCell;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    @FXML
    private JFXTreeTableView<DataEntry> table;
    @FXML
    private JFXTreeTableView<DataEntry> table2;
    @FXML
    private JFXTreeTableView<DataEntry> table3;

    ObservableList<DataEntry> data;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Create all columns
        JFXTreeTableColumn<DataEntry, String> group = new JFXTreeTableColumn("Group");
        group.setPrefWidth(100);

        group.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().group;
            }
        });

        JFXTreeTableColumn<DataEntry, String> activity = new JFXTreeTableColumn("Activity");
        activity.setPrefWidth(100);

        activity.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().activity;
            }
        });

        JFXTreeTableColumn<DataEntry, String> mandatory = new JFXTreeTableColumn("Mandatory");
        activity.setPrefWidth(100);

        mandatory.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().mandatory;
            }
        });

        JFXTreeTableColumn<DataEntry, String> start = new JFXTreeTableColumn("Planned Start");
        start.setPrefWidth(100);

        start.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().start;
            }
        });

        JFXTreeTableColumn<DataEntry, String> plannedEnd = new JFXTreeTableColumn("   Planned \nCompletion");
        plannedEnd.setPrefWidth(100);

        plannedEnd.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().plannedEnd;
            }
        });

        JFXTreeTableColumn<DataEntry, String> end = new JFXTreeTableColumn("Completion");
        end.setPrefWidth(100);

        end.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().end;
            }
        });

        JFXTreeTableColumn<DataEntry, String> responsible = new JFXTreeTableColumn("Responsible");
        responsible.setPrefWidth(100);

        responsible.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().responsible;
            }
        });

        JFXTreeTableColumn<DataEntry, String> status = new JFXTreeTableColumn("Status");
        status.setPrefWidth(100);

        status.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<DataEntry, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<DataEntry, String> param) {
                return param.getValue().getValue().status;
            }
        });

        data = FXCollections.observableArrayList();
        data.add(new DataEntry("Finance", "Pay alimony", "1", "02.10.2019", "16.08.2020", "", "Robin Finstad", "Incomplete"));
        data.add(new DataEntry("Finance", "Gather investors", "1", "02.10.2019", "16.08.2020", "", "Ola Nordmann", "Incomplete"));
        data.add(new DataEntry("Accounting", "Update bank details", "1", "01.10.2019", "16.08.2020", "", "Juliana Johansen", "Complete"));



        final TreeItem<DataEntry> root = new RecursiveTreeItem<DataEntry>(data, RecursiveTreeObject::getChildren);
        ObservableList<DataEntry> data2 = data.filtered(c -> c.getGroup() == "Finance");
        System.out.println(data);
        System.out.println(data2);
        final TreeItem<DataEntry> financeRoot = new RecursiveTreeItem<DataEntry>(data2, RecursiveTreeObject::getChildren);

        table.getColumns().setAll(group, activity, mandatory, start, plannedEnd, end, responsible, status);
        table2.getColumns().setAll(group, activity, mandatory, start, plannedEnd, end, responsible, status);
        table3.getColumns().setAll(group, activity, mandatory, start, plannedEnd, end, responsible, status);
        table.setEditable(true);
        table.setRoot(root);
        table.setShowRoot(false);
        table2.setEditable(true);
        table2.setRoot(financeRoot);
        table2.setShowRoot(false);
        table3.setEditable(true);
        table3.setRoot(root);
        table3.setShowRoot(false);



        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("Complete");
        list.add("Incomplete");
        /*
        status.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(list));

        status.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<DataEntry, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<DataEntry, String> event) {
                TreeItem<DataEntry> currentEdit = table.getTreeItem(event.getTreeTablePosition().getRow());
                currentEdit.getValue().setStatus(event.getNewValue());

            }
        });


         */

        status.setCellFactory(e -> new ComboBoxTreeTableCell<>(list){
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (("Complete").equals(getText())) {

                    this.setStyle("-fx-text-fill: rgb(0,201,0);");

                } else if (("Incomplete").equals(getText())){

                    this.setStyle("-fx-text-fill: rgb(231,0,0)");

                }

            }
        });


    }

    class DataEntry extends RecursiveTreeObject<DataEntry> {

        StringProperty group;
        StringProperty activity;
        StringProperty mandatory;
        StringProperty start;
        StringProperty plannedEnd;
        StringProperty end;
        StringProperty responsible;
        StringProperty status;


        private DataEntry(String group, String activity, String mandatory, String start, String plannedEnd, String end, String responsible, String status) {
            this.group = new SimpleStringProperty(group);
            this.activity = new SimpleStringProperty(activity);
            this.mandatory = new SimpleStringProperty(mandatory);
            this.start = new SimpleStringProperty(start);
            this.plannedEnd = new SimpleStringProperty(plannedEnd);
            this.end = new SimpleStringProperty(end);
            this.responsible = new SimpleStringProperty(responsible);
            this.status = new SimpleStringProperty(status);
        }

        public void setStatus(String status) {
            this.status.set(status);
        }

        public String getGroup() {
            return this.group.toString();
        }
    }




}