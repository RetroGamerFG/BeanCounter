//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// AccountDetailTable - creates a "TableView" object containing elements from "AccountDetail" used as part of the
// Account Detail page.


package Forms;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AccountDetailTable
{
    //createTransactionTable() - creates a "TableView" object based on passed transactions.
    public static TableView<AccountDetail> createAccountDetailTable(ArrayList<AccountDetail> accountDetails)
    {
        TableView<AccountDetail> tableView = new TableView<>();

        TableColumn<AccountDetail, Date> creationDateCol = new TableColumn<>("Created");
        creationDateCol.setCellValueFactory(new PropertyValueFactory<>("detailDate"));

        TableColumn<AccountDetail, String> startAccountCol = new TableColumn<>("Starting Account");
        startAccountCol.setCellValueFactory(new PropertyValueFactory<>("startCode"));

        TableColumn<AccountDetail, String> endAccountCol = new TableColumn<>("Ending Account");
        endAccountCol.setCellValueFactory(new PropertyValueFactory<>("endCode"));

        TableColumn<AccountDetail, LocalDate> startDateCol = new TableColumn<>("Starting Date");
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDateString"));

        TableColumn<AccountDetail, LocalDate> endDateCol = new TableColumn<>("Ending Date");
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDateString"));

        tableView.getColumns().addAll(creationDateCol, startAccountCol, endAccountCol, startDateCol, endDateCol);

        ObservableList<AccountDetail> accountDetailsOutput = FXCollections.observableArrayList(accountDetails);

        tableView.setItems(accountDetailsOutput);

        //Set column widths
        creationDateCol.setPrefWidth(100);
        startAccountCol.setPrefWidth(100);
        endAccountCol.setPrefWidth(100);
        startDateCol.setPrefWidth(100);
        endDateCol.setPrefWidth(100);

        return tableView;
    }
}
