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
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.*;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import Ledger.Ledger;
import Ledger.Transactions.Transaction;
import Ledger.Transactions.TransactionEntry;
import Ledger.Transactions.TransactionTable;

public class Main extends Application
{
    private Stage window;
    private Ledger ledger;

    private String version = "Ver. 0.1"; //application version

    @Override
    public void start(Stage primaryStage)
    {
        window = primaryStage;
        window.setTitle("BeanCounter");

        loadLedger();
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

        //Disabled while still being made...
        accountDetail.setDisable(true);
        statements.setDisable(true);

        menuButtonsLayer.getChildren().addAll(journalEntry, accountDetail, statements);
        menuButtonsLayer.setAlignment(Pos.CENTER);

        layout.setCenter(menuButtonsLayer);

        journalEntry.setOnAction(e -> 
        {
            setJournalEntryScene();
        });

        accountDetail.setOnAction(e -> 
        {
            //setledgerHistoryScene();
        });

        statements.setOnAction(e -> 
        {
            //setStatementsScene();
        });

        Label versionLabel = new Label(version);
        versionLabel.setAlignment(Pos.BOTTOM_LEFT);
        layout.setBottom(versionLabel);

        Scene output = new Scene(layout, 640, 480);

        window.setScene(output);
        window.show();
    }

//
// Journal Entry Log View
//

    //setJournalEntryScene() - consists of a table that shows all unposted journal entries. Can create new entries, or click on existing
    //entries to review, edit, or post. Also consists of search tools to find older entries.
    public void setJournalEntryScene()
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

        searchLayer.getChildren().addAll(searchTypeLabel, searchType, searchButton);
        topButtons.getChildren().addAll(backButton, createButton, searchLayer);

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
        TableView<Transaction> transactionTable = TransactionTable.createAccountTable(ledger.getAllUnpostedTransactions());

        transactionTable.setOnMouseClicked(e ->
        {
            if (e.getClickCount() == 2 ) 
            {
                Transaction selectedAccount = transactionTable.getSelectionModel().getSelectedItem();
            
                if (selectedAccount.getPostStatus().compareTo("Review") == 0)
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


//
//  Transaction Entry View
//

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
            setJournalEntryScene();
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
                ledger.setTransactionNumber(transaction);

                if(inTransaction == null)
                {
                    ledger.addTransaction(transaction);
                }

                saveLedger();
                setJournalEntryScene();
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
                setJournalEntryScene();
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
                setJournalEntryScene();
                saveLedger();
            });

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
// Ledger Save/Load Functions
//

    //saveLedger() - saves the "Ledger" object into a file "ledger.dat", to be used in subsequent program usages.
    public void saveLedger()
    {
        try 
        {
            FileOutputStream fileOut = new FileOutputStream("ledger.dat");
            ObjectOutputStream outStream = new ObjectOutputStream(fileOut);

            outStream.writeObject(ledger);
            outStream.close();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //loadLedger() - if a "ledger.dat" file exists, the program will load it's serialized values at runtime.
    public void loadLedger()
    {
        try
        {
            FileInputStream fileIn = new FileInputStream("ledger.dat");
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


//
// Additional functions
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
}