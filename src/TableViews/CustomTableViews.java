//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// CustomTableViews - a class consisting of static functions that create TableView objects based on specific
// object classe.


package TableViews;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;

import Forms.Statement.AccountDetail;
import Forms.Statement.DatedStatement;
import Ledger.Accounts.Account;
import Ledger.Transactions.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CustomTableViews {
    //createTransactionTable() - creates a "TableView" object based on passed transactions.
    @SuppressWarnings("unchecked")
    public static TableView<Transaction> createTransactionTable(ArrayList<Transaction> transactions)
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

    //createAccountTable() - creates a "TableView" object based on passed transactions.
    @SuppressWarnings("unchecked")
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

    //createTransactionTable() - creates a "TableView" object based on passed transactions.
    @SuppressWarnings("unchecked")
    public static TableView<AccountDetail> createAccountDetailTable(ArrayList<AccountDetail> accountDetails)
    {
        TableView<AccountDetail> tableView = new TableView<>();

        TableColumn<AccountDetail, Date> creationDateCol = new TableColumn<>("Created");
        creationDateCol.setCellValueFactory(new PropertyValueFactory<>("generatedDate"));

        TableColumn<AccountDetail, String> startAccountCol = new TableColumn<>("Starting Account");
        startAccountCol.setCellValueFactory(new PropertyValueFactory<>("startCode"));

        TableColumn<AccountDetail, String> endAccountCol = new TableColumn<>("Ending Account");
        endAccountCol.setCellValueFactory(new PropertyValueFactory<>("endCode"));

        TableColumn<AccountDetail, LocalDate> startDateCol = new TableColumn<>("Starting Date");
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<AccountDetail, LocalDate> endDateCol = new TableColumn<>("Ending Date");
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));

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

    //createDatedStatementTable() - creates a "TableView" object based on passed dated statements.
    @SuppressWarnings("unchecked")
    public static TableView<DatedStatement> createDatedStatementTable(ArrayList<DatedStatement> datedStatements)
    {
        TableView<DatedStatement> tableView = new TableView<>();

        TableColumn<DatedStatement, Date> creationDateCol = new TableColumn<>("Created");
        creationDateCol.setCellValueFactory(new PropertyValueFactory<>("generatedDate"));

        TableColumn<DatedStatement, String> statementTypeCol = new TableColumn<>("Type");
        statementTypeCol.setCellValueFactory(new PropertyValueFactory<>("statementType"));

        TableColumn<DatedStatement, String> statementRangeCol = new TableColumn<>("Period Type");
        statementRangeCol.setCellValueFactory(new PropertyValueFactory<>("statementRange"));

        TableColumn<DatedStatement, String> statementDateCol = new TableColumn<>("Period");
        statementDateCol.setCellValueFactory(new PropertyValueFactory<>("statementPeriodDesc"));

        TableColumn<DatedStatement, Year> fiscalYearCol = new TableColumn<>("FY");
        fiscalYearCol.setCellValueFactory(new PropertyValueFactory<>("fiscalYear"));

        //placeholder for debugging
        TableColumn<DatedStatement, LocalDate> startDateCol = new TableColumn<>("Starting Date");
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<DatedStatement, LocalDate> endDateCol = new TableColumn<>("Ending Date");
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        tableView.getColumns().addAll(creationDateCol, statementTypeCol, statementRangeCol, statementDateCol, fiscalYearCol, startDateCol, endDateCol);

        ObservableList<DatedStatement> datedStatementOutput = FXCollections.observableArrayList(datedStatements);

        tableView.setItems(datedStatementOutput);

        return tableView;
    }
}
