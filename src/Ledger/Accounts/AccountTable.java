//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// AccountTable - creates a "TableView" object containing elements from "Account" used as part of the
// account settings.


package Ledger.Accounts;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class AccountTable 
{
    //createAccountTable() - creates a "TableView" object based on passed transactions.
    public static TableView<Account> createAccountTable(ArrayList<Account> accounts)
    {
        TableView<Account> tableView = new TableView<>();

        TableColumn<Account, String> accountCodeCol = new TableColumn<>("Acc Code");
        accountCodeCol.setCellValueFactory(new PropertyValueFactory<>("accountCode"));

        TableColumn<Account, String> accountNameCol = new TableColumn<>("Acc Name");
        accountNameCol.setCellValueFactory(new PropertyValueFactory<>("accountName"));

        TableColumn<Account, String> accountDescriptionCol = new TableColumn<>("Acc Description");
        accountDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("accountDescription"));

        tableView.getColumns().addAll(accountCodeCol, accountNameCol, accountDescriptionCol);

        ObservableList<Account> transactionsOutput = FXCollections.observableArrayList(accounts);

        tableView.setItems(transactionsOutput);

        //Set column widths
        accountCodeCol.setPrefWidth(100);
        accountNameCol.setPrefWidth(100);
        accountDescriptionCol.setPrefWidth(200);

        return tableView;
    }    
}
