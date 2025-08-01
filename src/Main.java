//
// BeanCounter
// Copyright (c) 2025 Bailey "itsRetro" Manczko
//
// Permission is hereby granted, free of charge, to any person or organization obtaining a copy
// of this software and associated documentation files (the "Software"), to use, copy, modify, and 
// merge copies of the Software for personal or internal business use, including commercial internal use, 
// subject to the following conditions:
//
// Redistribution or sale of the Software or derivative works as part of a paid product or service
// is not permitted without explicit written permission from the author.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
// LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
// IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
// WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
// SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.*;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import Business.Business;
import Forms.Form;
import Forms.Statement.AccountDetail;
import Forms.Statement.AccountDetailTable;
import Ledger.Ledger;
import Ledger.Transactions.Transaction;
import Ledger.Transactions.TransactionEntry;
import Ledger.Transactions.TransactionTable;

public class Main extends Application
{
    private Stage window; //main window used by JavaFX
    private Ledger ledger; //holds all information regarding transactions and accounts
    private Form form; //holds all information regarding generated reports
    private Business business; //holds all information regarding the user/business used in forms

    private String version = "Ver. 0.2"; //application version

    @Override
    public void start(Stage primaryStage)
    {
        window = primaryStage;
        window.setTitle("BeanCounter");

        loadBusinessDetail();
        loadLedger();
        loadForm();

        setStartingScene();
    }

    public static void main(String[] args)
    {
        launch();
    }

//
// Main Menu / Starting Scene
//

    //setStartingScene() - the first page the user sees during program start. Includes navigation buttons for core program functions.
    public void setStartingScene()
    {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(0, 0, 0, 14));

        //
        //Logo
        try 
        {
            FileInputStream logoStream = new FileInputStream("img/logo.png");
            Image logo = new Image(logoStream);
            ImageView logoView = new ImageView(logo);

            logoView.setFitWidth(400);
            logoView.setPreserveRatio(true);

            layout.setTop(logoView);
        }
        catch (FileNotFoundException readError)
        {
            System.out.println("Error: Failed to get logo image.");
        }

        //
        //Menu Buttons
        VBox menuButtonsLayer = new VBox(4);
        Button journalEntry, accountDetail, statements;

        journalEntry = new Button("General Ledger");
        accountDetail = new Button("Account Detail");
        statements = new Button("Statements");

        menuButtonsLayer.getChildren().addAll(journalEntry, accountDetail, statements);
        menuButtonsLayer.setAlignment(Pos.CENTER);

        layout.setCenter(menuButtonsLayer);

        journalEntry.setOnAction(e -> 
        {
            setGeneralLedgerScene();
        });

        accountDetail.setOnAction(e -> 
        {
            setAccountDetailScene();
        });

        statements.setOnAction(e -> 
        {
            setStatementsScene();
        });

        Label versionLabel = new Label(version);
        versionLabel.setAlignment(Pos.BOTTOM_LEFT);
        layout.setBottom(versionLabel);

        Scene output = new Scene(layout, 640, 480);

        window.setScene(output);
        window.show();
    }

//
// General Ledger Scenes
//

    //setGeneralLedgerScene() - consists of a table that shows all unposted journal entries. Can create new entries, or click on existing
    //entries to review, edit, or post. Also consists of search tools to find older entries.
    public void setGeneralLedgerScene()
    {
        VBox layout = new VBox(16);

        //
        //Top Buttons Layer
        HBox topButtons = new HBox(8);

        Button backButton, createButton, searchButton;

        backButton = new Button("Home");
        createButton = new Button("New Transaction");
        searchButton = new Button("🔍");

        HBox searchLayer = new HBox(4);
        Label searchTypeLabel = new Label("Search: ");
        ArrayList<String> searchTerms = new ArrayList<>();
        searchTerms.add("Number");
        searchTerms.add("Creation Date");
        searchTerms.add("Post Date");
        searchTerms.add("Description");
        searchTerms.add("Total Value");

        ChoiceBox<String> searchType = new ChoiceBox<>(FXCollections.observableArrayList(searchTerms));

        HBox postCheckBoxLayer = new HBox(4);
        CheckBox seePosted = new CheckBox();
        Label postedCheckBoxLabel = new Label("Show Posted");

        postCheckBoxLayer.getChildren().addAll(seePosted, postedCheckBoxLabel);

        searchLayer.getChildren().addAll(searchTypeLabel, searchType, searchButton);
        topButtons.getChildren().addAll(backButton, createButton, searchLayer, postCheckBoxLayer);

        searchLayer.setDisable(true); //disabled while still being implemented

        //
        //Top Buttons Actions
        backButton.setOnAction(e -> 
        {
            setStartingScene();
        });

        //creates a new transaction from scratch
        createButton.setOnAction(e ->
        {
            setTransactionScene(null, true);
        });

        //performs search based on included search inputs
        searchButton.setOnAction(e ->
        {
            //needs implementation
        });

        //
        //Transaction History Table
        TableView<Transaction> transactionTable = TransactionTable.createTransactionTable(ledger.getAllUnpostedTransactions());

        //seePosted checkbox, which can show/hide posted transactions.
        seePosted.setOnAction(e ->
        {
            if(seePosted.isSelected())
            {
                transactionTable.setItems(FXCollections.observableArrayList(ledger.getTransactions()));
            }
            else
            {
                transactionTable.setItems(FXCollections.observableArrayList(ledger.getAllUnpostedTransactions()));
            }
        });

        transactionTable.setOnMouseClicked(e ->
        {
            if (e.getClickCount() == 2 ) 
            {
                Transaction selectedAccount = transactionTable.getSelectionModel().getSelectedItem();
            
                //"Review" will add extra controls but prevent entry changes
                //"Posted" will prevent any controls and prevent entry changes
                if (selectedAccount.getPostStatus().compareTo("Review") == 0)
                {
                    setTransactionScene(selectedAccount, false);
                }
                else if (selectedAccount.getPostStatus().compareTo("Posted") == 0)
                {
                    setTransactionScene(selectedAccount, false);
                }
                else
                {
                    setTransactionScene(selectedAccount, true);
                }
            }
        });

        //
        //Top Layer / Scene
        layout.getChildren().addAll(topButtons, transactionTable);

        Scene output = new Scene(layout, 640, 480);
        window.setScene(output);
        window.show();
    }

    //setTransactionScene() - used to create journal entries or view unposted entries. If a "Transaction" object is passed, values will be
    //autofilled, otherwise a new object will be created and stored upon successful submission.
    public void setTransactionScene(Transaction inTransaction, boolean allowEdits)
    {
        Transaction transaction;
        boolean existingEntry = true;

        //if passed Transaction object is null, treat scene as creating a new entry. Otherwise, existing fields will be
        //filled in place of each Transaction input
        if(inTransaction == null)
        {
            transaction = new Transaction();
            existingEntry = false;
        }
        else
        {
            transaction = inTransaction;
        }

        VBox layout = new VBox(16);
        VBox inputLayers = new VBox(16);
        
        //
        //Back Button
        Button backButton = new Button("Back");

        backButton.setOnAction(e->
        {
            setGeneralLedgerScene();
        });

        //
        //Post Date
        VBox infoInputLayer = new VBox(4);
        HBox postDateLayer = new HBox(4);
        Label postDateLabel = new Label("Post Date: ");

        DatePicker postDate;
        
        if(existingEntry)
        {
            postDate = new DatePicker(transaction.getPostDate());
        }
        else
        {
            postDate = new DatePicker();
        }

        postDateLayer.getChildren().addAll(postDateLabel, postDate);

        postDate.setOnAction(e ->
        {
            if(postDate.getValue() != null)
            {
                transaction.setPostDate(postDate.getValue());
            }
        });

        //
        //Description TextField
        HBox descriptionLayer = new HBox(4);
        Label descriptionLabel = new Label("Description: ");

        TextField transactionDescription;

        if(existingEntry)
        {
            transactionDescription = new TextField(transaction.getTransactionDescription());
        }
        else
        {
            transactionDescription = new TextField();
        }
        
        transactionDescription.textProperty().addListener((observable, oldValue, newValue) -> 
        {
            if(!newValue.isBlank())
            {
                transaction.setTransactionDescription(newValue);
            }
            else
            {
                transaction.setTransactionDescription(null);
            }
        });

        descriptionLayer.getChildren().addAll(descriptionLabel, transactionDescription);
        infoInputLayer.getChildren().addAll(postDateLayer, descriptionLayer);

        //
        //Entry Layers
        HBox transactionLabels = new HBox(16);
        Label accountLabel = new Label("Account Code");
        Label debitLabel = new Label("Debit");
        Label creditLabel = new Label("Credit");
        
        transactionLabels.getChildren().addAll(accountLabel, debitLabel, creditLabel);

        //if passed transaction is null, call to create new entry inputs. Otherwise call to output existing.
        if(existingEntry)
        {
            ArrayList<TransactionEntry> currentEntry = transaction.getTransactionEntries();

            for(int c = 0; c < currentEntry.size(); c++)
            {
                if(c > 1)
                {
                    addExistingEntryLayer(inputLayers, transaction, currentEntry.get(c), true);
                }
                else
                {
                    addExistingEntryLayer(inputLayers, transaction, currentEntry.get(c), false);
                }
            }
        }
        else
        {
            for(int c = 0; c < 2; c++)
            {
                addEntryLayer(inputLayers, transaction, false);
            }
        }

        //
        //Add Input Button
        Button addInputButton = new Button ("+");

        if(!allowEdits)
        {
            addInputButton.setDisable(true);
        }

        addInputButton.setOnAction(e ->
        {
            addEntryLayer(inputLayers, transaction, true);
        });

        //
        //Submit Button / Validation
        Button submitButton = new Button("Submit");

        if(!allowEdits)
        {
            submitButton.setDisable(true);
            submitButton.setVisible(false);
        }

        submitButton.setOnAction(e ->
        {
            if(ledger.validateTransaction(transaction))
            {
                transaction.setPostStatus("Review");

                if(transaction.getTransactionNumber() == -1)
                {
                    ledger.setTransactionNumber(transaction);
                }

                if(inTransaction == null)
                {
                    ledger.addTransaction(transaction);
                }

                saveLedger();
                setGeneralLedgerScene();
            }
        });

        //disables all input layers when edits are not allowed
        if(!allowEdits)
        {
            infoInputLayer.setDisable(true);
            inputLayers.setDisable(true);
            submitButton.setDisable(true);
            submitButton.setVisible(false);
        }

        //Additional inputs only if reviewing existing transactions
        if(existingEntry && !allowEdits)
        {
            //
            //Review Inputs
            VBox reviewLayer = new VBox(4);
            HBox userReviewLayer = new HBox(4);
            Label userSignatureLabel = new Label("Approval Signature: ");
            TextField userSignatureInput = new TextField();

            userReviewLayer.getChildren().addAll(userSignatureLabel, userSignatureInput);

            HBox reviewButtonsLayer = new HBox(4);
            Button postButton = new Button("Post");
            Button editButton = new Button("Edit");
            Button trashButton = new Button("Trash");

            //when pressed, transaction changes status to "Posted", and is no longer able to be updated
            postButton.setOnAction(e -> 
            {
                inTransaction.setPostStatus("Posted");
                inTransaction.setPostSignature(userSignatureInput.getText());
                setGeneralLedgerScene();
                saveLedger();
            });

            //when pressed, transaction will return to "Open" status, and scene will alter to allow edits and hide review inputs
            editButton.setOnAction(e ->
            {
                transaction.setPostStatus("Open");

                infoInputLayer.setDisable(false);
                inputLayers.setDisable(false);
                submitButton.setDisable(false);
                submitButton.setVisible(true);

                reviewLayer.setDisable(true);
                reviewLayer.setVisible(false);                
            });

            //when pressed, transaction is abandoned, and will be removed from the ledger
            trashButton.setOnAction(e -> 
            {
                //need to add a confirmation alert before proceeding
                ledger.removeTransaction(inTransaction);
                setGeneralLedgerScene();
                saveLedger();
            });

            //if transaction is already posted, append the post signature, and prevent the review buttons from appearing
            if(inTransaction.getPostStatus().compareTo("Posted") == 0)
            {
                userSignatureInput.setText(inTransaction.getPostSignature());
                reviewButtonsLayer.setVisible(false);
                reviewLayer.setDisable(true);
            }

            reviewButtonsLayer.getChildren().addAll(postButton, editButton, trashButton);
            reviewLayer.getChildren().addAll(userReviewLayer, reviewButtonsLayer);

            //
            //Top Layer / Scene
            layout.getChildren().addAll(backButton, infoInputLayer, transactionLabels, inputLayers, addInputButton, submitButton, reviewLayer);
        }
        else
        {
            //
            //Top Layer / Scene
            layout.getChildren().addAll(backButton, infoInputLayer, transactionLabels, inputLayers, addInputButton, submitButton);
        }
        
        //
        //Output
        Scene output = new Scene(layout, 640, 480);
        window.setScene(output);
        window.show();
    }

//
// Error View
//

    //setErrorScene() - a placeholder error page that can detail an error message and code for debug purposes.
    public void setErrorScene(String error)
    {
        StackPane layout = new StackPane();

        //
        //Labels
        VBox errorMessageLayer = new VBox(4);

        Label errorReport = new Label("Program Error. Please restart the program.");
        Label errorMessage = new Label(error);

        errorMessageLayer.getChildren().addAll(errorReport, errorMessage);
        layout.getChildren().add(errorMessageLayer);

        //
        //Output
        Scene output = new Scene(layout, 640, 480);
        window.setScene(output);
        window.show();
    }

//
// Account Detail Scenes
//

    //setAccountDetailScene() - displays a user input scene that can filter what condition(s) to view an account's full detail of posted transactions.
    public void setAccountDetailScene()
    {
        BorderPane layout = new BorderPane();

        //
        //Back Button
        Button backButton = new Button("Back");

        backButton.setOnAction(e -> 
        {
            setStartingScene();
        });

        layout.setTop(backButton);

        //
        //Search Dates Inputs
        VBox dateEntryLayer = new VBox(4);
        HBox startingDateLayer = new HBox(12);
        HBox endingDateLayer = new HBox(12);

        DatePicker startingDateInput = new DatePicker();
        DatePicker endingDateInput = new DatePicker();

        Label startingDateLabel = new Label("Date Start: ");
        Label endingDateLabel = new Label("Date End: ");

        startingDateLayer.getChildren().addAll(startingDateLabel, startingDateInput);
        endingDateLayer.getChildren().addAll(endingDateLabel, endingDateInput);
        dateEntryLayer.getChildren().addAll(startingDateLayer, endingDateLayer);

        layout.setRight(dateEntryLayer);

        //
        //Account Range Inputs
        VBox accountEntryLayer = new VBox(4);
        HBox startingAccountLayer = new HBox(12);
        HBox endingAccountLayer = new HBox(12);

        ComboBox<String> startingAccountEntry = new ComboBox<>();
        startingAccountEntry.setItems(FXCollections.observableArrayList(ledger.getAccountDescriptions()));

        ComboBox<String> endingAccountEntry = new ComboBox<>();
        endingAccountEntry.setItems(FXCollections.observableArrayList(ledger.getAccountDescriptions()));
        
        Label startingAccountLabel = new Label("Starting Account: ");
        Label endingAccountLabel = new Label("Ending Account: ");

        startingAccountLayer.getChildren().addAll(startingAccountLabel, startingAccountEntry);
        endingAccountLayer.getChildren().addAll(endingAccountLabel, endingAccountEntry);

        //
        //Generate Button
        Button generateButton = new Button("Generate");

        //
        //Account Detail History (Pulls up previous searches)
        TableView<AccountDetail> accountDetailTable = AccountDetailTable.createAccountDetailTable(form.getAccountDetail());

        accountDetailTable.setOnMouseClicked(e ->
        {
            //on double click, generate form for previous instance, opens as new window.
            if (e.getClickCount() == 2 )
            {
                setGeneratedAccountDetailScene(accountDetailTable.getSelectionModel().getSelectedItem());
            }
        });

        layout.setBottom(accountDetailTable);

        //generate button action (needs to occur after table creation)
        generateButton.setOnAction(e ->
        {
            //validate dates are not empty
            if(startingDateInput.getValue() == null || endingDateInput.getValue() == null)
            {
                Alert alert = new Alert(AlertType.ERROR, "Date input(s) left empty. Please add dates.");
                alert.showAndWait();
                return;
            }


            //validate date input for range
            if(startingDateInput.getValue().compareTo(endingDateInput.getValue()) > 0)
            {
                Alert alert = new Alert(AlertType.ERROR, "Start date is greater than end date...");
                alert.showAndWait();
                return;
            }

            //validate accounts were selected
            if(startingAccountEntry.getValue() == null || endingAccountEntry.getValue() == null)
            {
                Alert alert = new Alert(AlertType.ERROR, "Account(s) input is empty. Add a starting and ending account.");
                alert.showAndWait();
                return;
            }

            //retrieve account codes from entries
            String startingAccountCode = ledger.getAccountEntries().getAccountCodeByDescription(startingAccountEntry.getValue());
            String endingAccountCode = ledger.getAccountEntries().getAccountCodeByDescription(endingAccountEntry.getValue());

            //validate account input for not null and range
            if(Double.valueOf(startingAccountCode) > Double.valueOf(endingAccountCode))
            {
                Alert alert = new Alert(AlertType.ERROR, "Bounds Error: Starting account exceeds ending account.");
                alert.showAndWait();
                return;
            }
            
            //fetch posted transactions within date range
            ArrayList<Transaction> fetchedTransactions = ledger.getTransactionsByDateRange(startingDateInput.getValue(), endingDateInput.getValue());
            ledger.filterTransactionsByStatus(fetchedTransactions, "Posted");

            //filter remaining transactions by starting/ending code ranges
            ledger.filterTransactionsByAccountCodeRange(fetchedTransactions, startingAccountCode, endingAccountCode);

            //confirm at least one transaction was found by filtered search
            if(fetchedTransactions.size() <= 0)
            {
                Alert alert = new Alert(AlertType.ERROR, "No transactions matched search criteria. Try revising your search conditions.");
                alert.showAndWait();
                return;
            }

            //create ArrayList that holds transaction numbers, which is stored in AccountDetail
            ArrayList<Integer> transactionNumbers = new ArrayList<>();

            for(Transaction current : fetchedTransactions)
            {
                transactionNumbers.add(current.getTransactionNumber());
            }

            //initialize AccountDetail object
            AccountDetail createdAccountDetail = new AccountDetail(startingDateInput.getValue(), endingDateInput.getValue(), startingAccountCode, endingAccountCode);

            //set the referenced account codes based on all existing codes (function creates the array by passing all codes)
            createdAccountDetail.setReferencedAccountCodes(ledger.getAccountCodes());

            //set the referenced transaction numbers
            for(int refTransaction : transactionNumbers)
            {
                createdAccountDetail.insertReferencedTransaction(refTransaction);
            }

            //add the new AccountDetail to the form object, then call to save
            form.insertAccountDetail(createdAccountDetail);
            saveForm();

            setGeneratedAccountDetailScene(createdAccountDetail);

            //update the table view
            accountDetailTable.setItems(FXCollections.observableArrayList(form.getAccountDetail()));
        });

        accountEntryLayer.getChildren().addAll(startingAccountLayer, endingAccountLayer, generateButton);
        layout.setLeft(accountEntryLayer);

        //
        //Output
        Scene output = new Scene(layout, 640, 480);
        window.setScene(output);
        window.show();
    }

    //setGeneratedAccountDetailScene() - when an account detail is selected, a trial balance is created for every account that was included.
    //This appears in a separate window from the main window.
    public void setGeneratedAccountDetailScene(AccountDetail inDetail)
    {
        //setup the second window
        Stage secondWindow = new Stage();
        VBox mainView = new VBox();

        //setup the form and VBox to hold all pages
        ScrollPane document = new ScrollPane();
        VBox allPages = new VBox();

        Button printButton = new Button("Print");
        mainView.getChildren().addAll(printButton, document);

        printButton.setOnAction(e ->
        {
            printDocument(allPages);
        });

        //retrieve transactions contained within the account detail's referenced account array.
        //note that it is still possible no entries can exist with the given search criteria
        ArrayList<Transaction> fetchedTransactions = ledger.getTransactionByCodeArray(inDetail.getReferencedTransactions());
        
        //for every code that was referenced in the account detail...
        for(String currentCode : inDetail.getReferencedAccountCodes())
        {
            //setup the current "page" and set the styling
            BorderPane currentPage = new BorderPane(); //contains all elements of page
            currentPage.setPrefSize(595, 842); // A4 size in points (72 dpi)
            currentPage.setStyle("-fx-border-color: black; -fx-background-color: white;");
            currentPage.setPadding(new Insets(20));

            //calls function to create page header
            currentPage.setTop(createAccountDetailHeader(inDetail, currentCode));

            //
            //Content Layer w/ Styling
            BigDecimal codeDebitTotal = new BigDecimal(0);
            BigDecimal codeCreditTotal = new BigDecimal(0);
            //BigDecimal codeGrandTotal = new BigDecimal(0);  //will need to determine how to handle different accounts to determine (+) or (-)

            VBox contentLayer = new VBox();

            HBox contentHeaderLayer = new HBox();
            Label transactionNumberHeaderLabel = new Label("TransNum");
            Label transactionDescriptionHeaderLabel = new Label("Description");
            Label debitHeaderLabel = new Label("Debit");
            Label creditHeaderLabel = new Label("Credit");

            //preset widths used for content layer
            double setWidth1 = 250;
            double setWidth2 = 140;
            double setWidth3 = 90;

            //set label spacing
            transactionNumberHeaderLabel.setPrefWidth(setWidth3);
            transactionDescriptionHeaderLabel.setPrefWidth(setWidth1);
            debitHeaderLabel.setPrefWidth(setWidth2);
            creditHeaderLabel.setPrefWidth(setWidth2);

            //add labels to layer(s)
            contentHeaderLayer.getChildren().addAll(transactionNumberHeaderLabel, transactionDescriptionHeaderLabel, debitHeaderLabel, creditHeaderLabel);
            contentLayer.getChildren().add(contentHeaderLayer);

            //rowCount and rowLimit for page spacing; 17 rows is page maximum to keep dimensions
            int rowCount = 0;
            int rowLimit = 17;

            //for each transaction in the fetched array...
            for(Transaction currentTransaction : fetchedTransactions)
            {
                //for each entry line in the transaction...
                for(TransactionEntry currentEntry : currentTransaction.getTransactionEntries())
                {
                    //if an entry line contains the matching account code, include the entry into the form
                    if(currentEntry.getAccountCode().compareTo(currentCode) == 0)
                    {
                        //
                        //Entry Row w/ styling
                        HBox entryRow = new HBox();
                        entryRow.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");
                        entryRow.setPadding(new Insets(10));
                        entryRow.setSpacing(5);

                        Label transactionNumberLabel = new Label(Integer.toString(currentTransaction.getTransactionNumber()));
                        Label transactionDescriptionLabel = new Label(currentTransaction.getTransactionDescription());

                        Label debitValueLabel, creditValueLabel;
                        
                        if(currentEntry.getDebitAmount() != null)
                        {
                            debitValueLabel = new Label(currentEntry.printDebitAmount());
                            codeDebitTotal = codeDebitTotal.add(currentEntry.getDebitAmount());
                        }
                        else
                        {
                            debitValueLabel = new Label(""); //keep empty
                        }

                        if(currentEntry.getCreditAmount() != null)
                        {
                            creditValueLabel = new Label(currentEntry.printCreditAmount());
                            codeCreditTotal = codeCreditTotal.add(currentEntry.getCreditAmount());
                        }
                        else
                        {
                            creditValueLabel = new Label("");
                        }

                        transactionNumberLabel.setPrefWidth(setWidth3);
                        transactionDescriptionLabel.setPrefWidth(setWidth1);
                        debitValueLabel.setPrefWidth(setWidth2);
                        creditValueLabel.setPrefWidth(setWidth2);
                        entryRow.getChildren().addAll(transactionNumberLabel, transactionDescriptionLabel, debitValueLabel, creditValueLabel);

                        contentLayer.getChildren().add(entryRow);
                        rowCount++;

                        //if the maximum amount of entry rows have been added, create a new page, and call to print header and footer again
                        if(rowCount >= rowLimit)
                        {
                            //add current contentLayer to the page
                            currentPage.setCenter(contentLayer);

                            //calls function to create page footer, then add page to "allPages" VBox
                            currentPage.setBottom(createAccountDetailFooter(inDetail));
                            allPages.getChildren().add(currentPage);

                            //create new instance of the page, call function to create page header, then create new contentLayer
                            currentPage = new BorderPane();
                            currentPage.setPrefSize(595, 842); // A4 size in points (72 dpi)
                            currentPage.setStyle("-fx-border-color: black; -fx-background-color: white;");
                            currentPage.setPadding(new Insets(20));

                            currentPage.setTop(createAccountDetailHeader(inDetail, currentCode));

                            contentLayer = new VBox();

                            //reset the rowCount variable
                            rowCount = 0;
                        }
                    }
                }
            }

            //
            //Total Row w/ Styling
            HBox totalRow = new HBox();
            totalRow.setStyle("-fx-background-color:rgb(187, 187, 187); -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");
            totalRow.setPadding(new Insets(10));
            totalRow.setSpacing(5);

            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US); //used for currency formatting

            Label totalLabel = new Label("Totals: ");
            Label debitValueTotalLabel = new Label(currencyFormatter.format(codeDebitTotal));
            Label creditValueTotalLabel = new Label(currencyFormatter.format(codeCreditTotal));
            Label spacerLabel = new Label(""); //used to keep consistant with entry rows

            totalLabel.setPrefWidth(setWidth3);
            spacerLabel.setPrefWidth(setWidth1);
            debitValueTotalLabel.setPrefWidth(setWidth2);
            creditValueTotalLabel.setPrefWidth(setWidth2);
            totalRow.getChildren().addAll(totalLabel, spacerLabel, debitValueTotalLabel, creditValueTotalLabel);

            contentLayer.getChildren().add(totalRow);
            currentPage.setCenter(contentLayer);

            //calls function to create page footer, then add page to "allPages" VBox
            currentPage.setBottom(createAccountDetailFooter(inDetail));
            allPages.getChildren().add(currentPage);
        }

        document.setContent(allPages);

        Scene output = new Scene(mainView, 640, 480);
        secondWindow.setScene(output);
        secondWindow.show();
    }

//
// Statements Scenes
//

    //setStatementsScene() - displays a user input scene for choosing the type of statement to generate and a specified date range of which
    //transactions should be included. These include the income statement, balance sheet, and statement of retained earnings.
    public void setStatementsScene()
    {
        BorderPane layout = new BorderPane();

        VBox topLayer = new VBox();
        Button backButton = new Button("Back");

        backButton.setOnAction(e ->
        {
            setStartingScene();
        });

        topLayer.getChildren().add(backButton);
        layout.setTop(topLayer);

        VBox centerLayer = new VBox();

        ComboBox<String> formSelection = new ComboBox<>();
        ArrayList<String> formOptions = new ArrayList<>();
        formOptions.add("Income Statement");
        formOptions.add("Balance Sheet");
        formOptions.add("Statement of Retained Earnings");
        formSelection.setItems(FXCollections.observableList(null));


    }

    public void setGeneratedIncomeStatementScene()
    {

    }

    public void setGeneratedBalanceSheetScene()
    {

    }

    public void setGeneratedStatementOfRetainedEarningsScene()
    {

    }


//
// Ledger Save/Load Functions
//

    //saveLedger() - saves the "Ledger" object into a file "Ledger.dat", to be used in subsequent program usages.
    public void saveLedger()
    {
        try 
        {
            FileOutputStream fileOut = new FileOutputStream("Ledger.dat");
            ObjectOutputStream outStream = new ObjectOutputStream(fileOut);

            outStream.writeObject(ledger);
            outStream.close();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //loadLedger() - if a "Ledger.dat" file exists, the program will load it's serialized values at runtime.
    public void loadLedger()
    {
        try
        {
            FileInputStream fileIn = new FileInputStream("Ledger.dat");
            ObjectInputStream inStream = new ObjectInputStream(fileIn);

            ledger = (Ledger) inStream.readObject();
            inStream.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Ledger.dat not found. Creating new instance.");
            ledger = new Ledger();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    //saveForm() - saves the "Form" object into a file "Form.dat", to be used in subsequent program usages.
    public void saveForm()
    {
        try 
        {
            FileOutputStream fileOut = new FileOutputStream("Form.dat");
            ObjectOutputStream outStream = new ObjectOutputStream(fileOut);

            outStream.writeObject(form);
            outStream.close();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //loadForm() - if a "Form.dat" file exists, the program will load it's serialized values at runtime.
    public void loadForm()
    {
        try
        {
            FileInputStream fileIn = new FileInputStream("Form.dat");
            ObjectInputStream inStream = new ObjectInputStream(fileIn);

            form = (Form) inStream.readObject();
            inStream.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Form.dat not found. Creating new instance.");
            form = new Form();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    //loadBusinessDetail() - if a "business.dat" file exists, the program will load it's serialized values at runtime.
    //If it does not exist, program should flag first runtime instance and run setup.
    public void loadBusinessDetail()
    {
        try
        {
            FileInputStream fileIn = new FileInputStream("business.dat");
            ObjectInputStream inStream = new ObjectInputStream(fileIn);

            business = (Business) inStream.readObject();
            inStream.close();
        }
        catch (FileNotFoundException e)
        {
            //NEED TO IMPLEMEMENT FIRST TIME SETUP SCENE
            business = new Business();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

//
// Additional Transaction Functions
//

    //addEntryLayer() - adds a new input for a "TransactionEntry" used as part of the journal entry. Two are always required, but
    //additional can also opt for the remove button to eliminate unwanted lines.
    private void addEntryLayer(VBox inputLayer, Transaction newTransaction, boolean addRemoveButton)
    {
        TransactionEntry currentTransaction = new TransactionEntry();

        HBox output = new HBox(8);

        TextField accountCodeEntry = new TextField();
        AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(accountCodeEntry, ledger.getAccountCodes());

        TextField debitEntry = new TextField();
        TextField creditEntry = new TextField();

        //handles account code entries
        accountCodeEntry.textProperty().addListener((observable, oldValue, newValue) -> 
        {
            if(!newValue.isBlank())
            {
                currentTransaction.setAccountCode(newValue);
            }
            else
            {
                currentTransaction.setAccountCode(null);
            }
        });

        //handles debit entries
        debitEntry.textProperty().addListener((observable, oldValue, newValue) -> 
        {
            if(!newValue.isBlank())
            {
                currentTransaction.setDebitAmount(new BigDecimal(newValue));
            }
            else
            {
                currentTransaction.setDebitAmount(null);
            }
        });

        //handles credit entries
        creditEntry.textProperty().addListener((observable, oldValue, newValue) -> 
        {
            if(!newValue.isBlank())
            {
                currentTransaction.setCreditAmount(new BigDecimal(newValue));
            }
            else
            {
                currentTransaction.setCreditAmount(null);
            }
        });

        output.getChildren().addAll(accountCodeEntry, debitEntry, creditEntry);

        if(addRemoveButton)
        {
            Button removeButton = new Button("-");
            output.getChildren().add(removeButton);

            removeButton.setOnAction(e ->
            {
                newTransaction.removeTransactionEntry(currentTransaction);
                inputLayer.getChildren().remove(output);
            });
        }
        
        inputLayer.getChildren().add(output);
        newTransaction.addTransactionEntry(currentTransaction);
    }

    //addExistingEntryLayer() - for existing transactions that are "Open" or under "Review", creates inputs that fill existing values while also
    //enabling/disabling based on the status. Can revert to the regular edit mode upon button press.
    private void addExistingEntryLayer(VBox inputLayer, Transaction transaction, TransactionEntry transactionEntry, boolean addRemoveButton)
    {
        HBox output = new HBox(8);

        TextField accountCodeEntry = new TextField(transactionEntry.getAccountCode());
        AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(accountCodeEntry, ledger.getAccountCodes());

        TextField debitEntry;
        TextField creditEntry;

        if(transactionEntry.getDebitAmount() != null)
        {
            debitEntry = new TextField(transactionEntry.getDebitAmount().toString());
        }
        else
        {
            debitEntry = new TextField();
        }

        if(transactionEntry.getCreditAmount() != null)
        {
            creditEntry = new TextField(transactionEntry.getCreditAmount().toString());
        }
        else
        {
            creditEntry = new TextField();
        }

        //handles account code entries
        accountCodeEntry.textProperty().addListener((observable, oldValue, newValue) -> 
        {
            if(!newValue.isBlank())
            {
                transactionEntry.setAccountCode(newValue);
            }
            else
            {
                transactionEntry.setAccountCode(null);
            }
        });

        //handles debit entries
        debitEntry.textProperty().addListener((observable, oldValue, newValue) -> 
        {
            if(!newValue.isBlank())
            {
                transactionEntry.setDebitAmount(new BigDecimal(newValue));
            }
            else
            {
                transactionEntry.setDebitAmount(null);
            }
        });

        //handles credit entries
        creditEntry.textProperty().addListener((observable, oldValue, newValue) -> 
        {
            if(!newValue.isBlank())
            {
                transactionEntry.setCreditAmount(new BigDecimal(newValue));
            }
            else
            {
                transactionEntry.setCreditAmount(null);
            }
        });

        output.getChildren().addAll(accountCodeEntry, debitEntry, creditEntry);

        if(addRemoveButton)
        {
            Button removeButton = new Button("-");
            removeButton.setDisable(true);
            output.getChildren().add(removeButton);

            removeButton.setOnAction(e ->
            {
                transaction.removeTransactionEntry(transactionEntry);
                inputLayer.getChildren().remove(output);
            });
        }

        inputLayer.getChildren().add(output);
    }

//
// Additional Account Detail Functions
//

    //createAccountDetailHeader() - used for generating an Account Detail form, creates a header that contains the account code, account description, start date,
    //and end date. Returns as a VBox, which is then set to the BorderPane setTop().
    public VBox createAccountDetailHeader(AccountDetail inDetail, String currentCode)
    {
        //
        //Header Layer w/ Styling
        VBox headerLayer = new VBox();
        headerLayer.setPrefWidth(500);
        headerLayer.setStyle("-fx-background-color:rgb(187, 187, 187); -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");
        headerLayer.setPadding(new Insets(10));

        VBox codeAndDescriptionLayer = new VBox();
        codeAndDescriptionLayer.setAlignment(Pos.CENTER);
        codeAndDescriptionLayer.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 16px;");

        Label accountCodeLabel = new Label(currentCode);
        Label accountDescriptionLabel = new Label(ledger.getAccountDescriptionByCode(currentCode));
        codeAndDescriptionLayer.getChildren().addAll(accountCodeLabel, accountDescriptionLabel);

        Label detailStartDateRangeLabel = new Label("Start Date: " + inDetail.printStartDate());
        Label detailEndDateRangeLabel = new Label("End Date: " + inDetail.printEndDate());
            
        headerLayer.getChildren().addAll(codeAndDescriptionLayer, detailStartDateRangeLabel, detailEndDateRangeLabel);
        return headerLayer;
    }

    //createAccountDetailFooter() - used for generating an Account Detail form, creates a footer that contains the business name and generation date.
    //Returns as an HBox, which is then set to the BorderPane setBottom().
    public HBox createAccountDetailFooter(AccountDetail inDetail)
    {
        //
        //Footer Layer
        HBox footerLayer = new HBox();
        footerLayer.setPrefWidth(500);
        footerLayer.setStyle("-fx-background-color:rgb(187, 187, 187); -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");
        footerLayer.setPadding(new Insets(10));

        Label businessNameLabel = new Label("Placeholder");
        Label formDateLabel = new Label(inDetail.printGeneratedDate());
        
        businessNameLabel.setPrefWidth(375);
        formDateLabel.setPrefWidth(220);
        footerLayer.getChildren().addAll(businessNameLabel, formDateLabel);
        return footerLayer;
    }

//
// Additional Form Functions
//

    //printDocument() - used with all forms, calls to print the contents of a VBox using system commands.
    //This function will likely need further work as more is being developed in this program.
    //Printing does also support saving to PDF, but a dedicated feature may be added in the future.
    public void printDocument(VBox allPages)
    {
        PrinterJob job = PrinterJob.createPrinterJob();

        if (job == null)
        {
            Alert alert = new Alert(AlertType.ERROR, "No printer found.");
            alert.showAndWait();
            return;
        }

        //get and store the width and height. This will be needed to adjust the VBox dimensions.
        double printableWidth = job.getJobSettings().getPageLayout().getPrintableWidth();
        double printableHeight = job.getJobSettings().getPageLayout().getPrintableHeight();

        boolean proceed = job.showPrintDialog(null);

        if (proceed) 
        {
            for (Node page : allPages.getChildren())
            {
                double scaleFactor = 2.0; // or 3.0 for even higher DPI

                SnapshotParameters params = new SnapshotParameters();
                params.setTransform(new Scale(scaleFactor, scaleFactor));

                // Get high-res snapshot of the page
                WritableImage snapshot = page.snapshot(params, null);

                // Scale down the image view to print at original size
                ImageView imageView = new ImageView(snapshot);
                //imageView.setFitWidth(page.getBoundsInParent().getWidth());
                //imageView.setFitHeight(page.getBoundsInParent().getHeight());
                imageView.setFitWidth(printableWidth);
                imageView.setFitHeight(printableHeight);


                boolean success = job.printPage(imageView);

                if (!success) 
                {
                    Alert alert = new Alert(AlertType.ERROR, "Failed to print a page.");
                    alert.showAndWait();
                    break;
                }
            }

            job.endJob();
        }
    }
}

//Programmer Notes for future reference
    //Consider revising account detail section to incorporate different page sizes, allowing different dimensions as necessary
    //Make a Save to PDF button. Will likely require additional dependencies.