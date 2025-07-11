//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// TransactionTable - creates a "TableView" object containing elements from "Transaction" used as part of the
// General Ledger page.


package Ledger.Transactions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TransactionTable
{
    //createAccountTable() - creates a "TableView" object based on passed transactions.
    public static TableView<Transaction> createAccountTable(ArrayList<Transaction> transactions)
    {
        TableView<Transaction> tableView = new TableView<>();

        TableColumn<Transaction, Integer> codeCol = new TableColumn<>("Transaction Num");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("transactionNumber"));

        TableColumn<Transaction, LocalDate> postDateCol = new TableColumn<>("Post Date");
        postDateCol.setCellValueFactory(new PropertyValueFactory<>("postDate"));

        TableColumn<Transaction, Date> creationDateCol = new TableColumn<>("Created Date");
        creationDateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        TableColumn<Transaction, String> totalCol = new TableColumn<>("Total Amount");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("formattedTotal"));

        TableColumn<Transaction, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("transactionDescription"));

        TableColumn<Transaction, String> postStatusCol = new TableColumn<>("Status");
        postStatusCol.setCellValueFactory(new PropertyValueFactory<>("postStatus"));

        tableView.getColumns().addAll(codeCol, postDateCol, creationDateCol, totalCol, descriptionCol, postStatusCol);

        ObservableList<Transaction> transactionsOutput = FXCollections.observableArrayList(transactions);

        tableView.setItems(transactionsOutput);

        //Set column widths
        codeCol.setPrefWidth(100);
        postDateCol.setPrefWidth(100);
        creationDateCol.setPrefWidth(200);
        totalCol.setPrefWidth(100);
        descriptionCol.setPrefWidth(200);
        postStatusCol.setPrefWidth(50);

        return tableView;
    }
}
