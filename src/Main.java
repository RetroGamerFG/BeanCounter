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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import Business.Business;
import Business.User;
import Forms.Form;
import Forms.Statement.AccountDetail;
import Forms.Statement.DatedStatement;
import Ledger.Ledger;
import Ledger.Accounts.ProcessedAccount;
import Ledger.Transactions.Transaction;
import Ledger.Transactions.TransactionEntry;
import TableViews.CustomTableViews;
import UI.FontPresets;
import UI.SceneTabs;

public class Main extends Application
{
    private Stage window; //main window used by JavaFX

    private Ledger ledger; //holds all information regarding transactions and accounts
    private Form form; //holds all information regarding generated reports
    private Business business; //holds all information regarding the user/business used in forms

    private String version = "Ver. 0.4"; //application version

    @Override
    public void start(Stage primaryStage)
    {
        window = primaryStage;
        window.setTitle("BeanCounter");

        loadBusiness();

        //if no business was loaded, initialize to first-time setup
        if(business == null)
        {
            setFirstTimeSetupScene();
        }
        else
        {
            if(business.hasMultipleUsers())
            {
                setLoginScene();
            }
            else
            {
                loadLedger();
                loadForm();
                setMainMenuScene();
            }
        }
    }

    public static void main(String[] args)
    {
        launch();
    }

//
// Main Menu / Starting Scene
//

    public void setLoginScene()
    {
        BorderPane layout = new BorderPane();

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
            BorderPane.setAlignment(logoView, Pos.CENTER);
        }
        catch (FileNotFoundException readError)
        {
            System.out.println("Error: Failed to get logo image.");
        }

        VBox userInputLayer = new VBox();
        TextField usernameInput = new TextField("username");
        TextField passwordInput = new TextField("password");

        Button loginButton = new Button("Log In");

        userInputLayer.getChildren().addAll(usernameInput, passwordInput, loginButton);
        layout.setCenter(userInputLayer);
        BorderPane.setAlignment(userInputLayer, Pos.CENTER);

        Label registeredLabel = new Label("Initialized for use by: " + business.getBusinessName());
        FontPresets.setContent(registeredLabel);

        layout.setBottom(registeredLabel);
        BorderPane.setAlignment(registeredLabel, Pos.CENTER);

        loginButton.setOnAction(e ->
        {
            //validate the user is registered and password matches
            if(business.validateLogin(usernameInput.getText(), passwordInput.getText()))
            {
                loadLedger();
                loadForm();
                setMainMenuScene();
            }
            else
            {
                Alert alert = new Alert(AlertType.ERROR, "Sign-in credentials are incorrect.");
                alert.showAndWait();
            }
        });

        Scene output = new Scene(layout, 640, 480);

        window.setScene(output);
        window.show();
    }

    //setMainMenuScene() - the software's main menu. Includes navigation buttons for core program functions.
    public void setMainMenuScene()
    {
        BorderPane layout = new BorderPane();

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
            BorderPane.setAlignment(logoView, Pos.CENTER);
        }
        catch (FileNotFoundException readError)
        {
            System.out.println("Error: Failed to get logo image.");
        }

        //
        //Menu Buttons
        VBox menuButtonsLayer = new VBox(4);
        Button journalEntry, accountDetail, statements, settingsButton;

        journalEntry = new Button("General Ledger");
        accountDetail = new Button("Account Detail");
        statements = new Button("Statements");
        settingsButton = new Button("Settings");

        menuButtonsLayer.getChildren().addAll(journalEntry, accountDetail, statements, settingsButton);
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

        settingsButton.setOnAction(e -> 
        {
            setSettingsScene();
        });

        Label versionLabel = new Label(version);

        layout.setBottom(versionLabel);
        BorderPane.setAlignment(versionLabel, Pos.BOTTOM_LEFT);

        Scene output = new Scene(layout, 640, 480);

        window.setScene(output);
        window.show();
    }

//
// Settings Scenes
//

    //setFirstTimeSetupScene() - if a "Business.dat" file fails to initialize, the software will boot into the first-time setup.
    public void setFirstTimeSetupScene()
    {
        SceneTabs setupSceneTab = new SceneTabs(5);
        BorderPane layout = new BorderPane();
        VBox[] contentArray = new VBox[setupSceneTab.getMaxTabs()];

        //initialize the contentArray and add alignment and padding presets
        for(int c = 0; c < setupSceneTab.getMaxTabs(); c++)
        {
            contentArray[c] = new VBox();
            contentArray[c].setAlignment(Pos.CENTER);
            contentArray[c].setPadding(new Insets(15));
        }

        layout.setCenter(contentArray[0]);

        //
        //Page 1
        //Welcome Message

        //
        //Welcome Layer
        TextFlow welcomeTextFlow = new TextFlow();
        Text welcomeText1 = new Text("Welcome to BeanCounter!\n");
        Text welcomeText2 = new Text("Please fill out the following details for the first time setup.");

        FontPresets.setTitle(welcomeText1);
        FontPresets.setContent(welcomeText2);

        welcomeTextFlow.getChildren().add(welcomeText1);
        welcomeTextFlow.getChildren().add(welcomeText2);
        contentArray[0].getChildren().addAll(welcomeTextFlow);

        //
        //Page 2
        //Business Info

        //
        //Business Info Message
        TextFlow businessMessageTextFlow = new TextFlow();
        Text businessMessageText = new Text("Enter your business information.\n");

        FontPresets.setTitle(businessMessageText);
        businessMessageTextFlow.getChildren().add(businessMessageText);

        //
        //Business Name Input
        HBox businessNameLayer = new HBox();
        Label businessNameLabel = new Label("Business Name: ");
        TextField businessNameInput = new TextField();

        FontPresets.setTextField(businessNameInput);

        businessNameLayer.getChildren().addAll(businessNameLabel, businessNameInput);

        //
        //Business Start Date Input
        HBox businessStartMonthLayer = new HBox();
        Label businessStartMonthLabel = new Label("Start Month: ");

        ComboBox<Month> selectedMonthInput = new ComboBox<>();
        selectedMonthInput.getItems().addAll(Month.values());

        FontPresets.setComboBox(selectedMonthInput);

        businessStartMonthLayer.getChildren().addAll(businessStartMonthLabel, selectedMonthInput);

        HBox businessStartYearLayer = new HBox();
        Label businessStartYearLabel = new Label("Start Year: ");
        
        ComboBox<Year> selectedYearInput = new ComboBox<>();

        //add only from a default of 1969 to the current year.
        for(int current = 1969; current <= Year.now().getValue(); current++)
        {
            selectedYearInput.getItems().add(Year.of(current));
        }

        FontPresets.setComboBox(selectedYearInput);

        businessStartYearLayer.getChildren().addAll(businessStartYearLabel, selectedYearInput);

        contentArray[1].getChildren().addAll(businessMessageTextFlow, businessNameLayer, businessStartMonthLayer, businessStartYearLayer);

        businessNameInput.setOnKeyPressed(textFieldStyleHandler);
        selectedMonthInput.setOnAction(comboBoxStyleHandler);
        selectedYearInput.setOnAction(comboBoxStyleHandler);

        //
        //Page 3
        //User Type (Multiple Users)

        //
        //User Type Layer
        HBox userTypeDescriptionLayer = new HBox();
        TextFlow userTypeTextFlow = new TextFlow();
        Text userTypeText1 = new Text("How many users will access this software?\n");
        Text userTypeText2 = new Text("(You can change this later in the settings)\n");

        userTypeTextFlow.getChildren().add(userTypeText1);
        userTypeTextFlow.getChildren().add(userTypeText2);

        FontPresets.setFormatting(userTypeTextFlow);
        FontPresets.setTitle(userTypeText1);
        FontPresets.setContent(userTypeText2);

        userTypeDescriptionLayer.getChildren().add(userTypeTextFlow);

        VBox userTypeSelectionLayer = new VBox(16);

        HBox userTypeMultipleLayer = new HBox(8);
        RadioButton userTypeSelectionMultiple = new RadioButton();
        TextFlow userTypeMultipleTextFlow = new TextFlow();
        Text userTypeMultipleText1 = new Text("Multiple Users\n");
        Text userTypeMultipleText2 = new Text("Can specify multiple users with sign-in and passwords.\n");
        Text userTypeMultipleText3 = new Text("Recommended for businesses.\n");

        userTypeMultipleTextFlow.getChildren().add(userTypeMultipleText1);
        userTypeMultipleTextFlow.getChildren().add(userTypeMultipleText2);
        userTypeMultipleTextFlow.getChildren().add(userTypeMultipleText3);
        
        FontPresets.setFormatting(userTypeMultipleTextFlow);
        FontPresets.setHeader(userTypeMultipleText1);
        FontPresets.setContent(userTypeMultipleText2);
        FontPresets.setContent(userTypeMultipleText3);

        userTypeMultipleLayer.getChildren().addAll(userTypeSelectionMultiple, userTypeMultipleTextFlow);

        HBox userTypeSingleLayer = new HBox(8);
        RadioButton userTypeSelectionSingle = new RadioButton();
        TextFlow userTypeSingleTextFlow = new TextFlow();
        Text userTypeSingleText1 = new Text("Single User\n");
        Text userTypeSingleText2 = new Text("Use the software freely without any user account(s).\n");
        Text userTypeSingleText3 = new Text("Recommended for individuals.");

        userTypeSingleTextFlow.getChildren().add(userTypeSingleText1);
        userTypeSingleTextFlow.getChildren().add(userTypeSingleText2);
        userTypeSingleTextFlow.getChildren().add(userTypeSingleText3);
        
        FontPresets.setFormatting(userTypeSingleTextFlow);
        FontPresets.setHeader(userTypeSingleText1);
        FontPresets.setContent(userTypeSingleText2);
        FontPresets.setContent(userTypeSingleText3);

        userTypeSingleLayer.getChildren().addAll(userTypeSelectionSingle, userTypeSingleTextFlow);
        userTypeSelectionLayer.getChildren().addAll(userTypeMultipleLayer, userTypeSingleLayer);

        contentArray[2].getChildren().addAll(userTypeDescriptionLayer, userTypeSelectionLayer);

        //
        //Page 4
        //First User Entry (Optional)

        //
        //First User Creation Message
        TextFlow firstUserTextFlow = new TextFlow();
        Text firstUserText1 = new Text("Create the first user.\n");
        Text firstUserText2 = new Text("Note: This user will also have admin privileges!\n");

        FontPresets.setFormatting(firstUserTextFlow);
        FontPresets.setTitle(firstUserText1);
        FontPresets.setContent(firstUserText2);

        firstUserTextFlow.getChildren().add(firstUserText1);
        firstUserTextFlow.getChildren().add(firstUserText2);

        //
        //First User Creation
        HBox firstUsernameLayer = new HBox();
        Label firstUsernameLabel = new Label("Username: ");
        TextField firstUsernameInput = new TextField();

        FontPresets.setTextField(firstUsernameInput);

        HBox firstUserPasswordLayer = new HBox();
        Label firstUserPasswordLabel = new Label("Password: ");
        TextField firstUserPasswordInput = new TextField();

        FontPresets.setTextField(firstUserPasswordInput);

        HBox firstUserFirstNameLayer = new HBox();
        Label firstUserFirstNameLabel = new Label("First Name: ");
        TextField firstUserFirstNameInput = new TextField();

        FontPresets.setTextField(firstUserFirstNameInput);

        HBox firstUserLastNameLayer = new HBox();
        Label firstUserLastNameLabel = new Label("Last Name: ");
        TextField firstUserLastNameInput = new TextField();

        FontPresets.setTextField(firstUserLastNameInput);

        firstUsernameLayer.getChildren().addAll(firstUsernameLabel, firstUsernameInput);
        firstUserPasswordLayer.getChildren().addAll(firstUserPasswordLabel, firstUserPasswordInput);
        firstUserFirstNameLayer.getChildren().addAll(firstUserFirstNameLabel, firstUserFirstNameInput);
        firstUserLastNameLayer.getChildren().addAll(firstUserLastNameLabel, firstUserLastNameInput);

        contentArray[3].getChildren().addAll(firstUserTextFlow, firstUsernameLayer, firstUserPasswordLayer, firstUserFirstNameLayer, firstUserLastNameLayer);

        firstUsernameInput.setOnKeyPressed(textFieldStyleHandler);
        firstUserPasswordInput.setOnKeyPressed(textFieldStyleHandler);
        firstUserFirstNameInput.setOnKeyPressed(textFieldStyleHandler);
        firstUserLastNameInput.setOnKeyPressed(textFieldStyleHandler);

        //
        //Page 5
        //Final Message

        //
        //Finalize Message
        TextFlow finalMessageTextFlow = new TextFlow();
        Text finalMessageText1 = new Text("You're all set!\n");
        Text finalMessageText2 = new Text("Click \"Finish\" to start program.");

        FontPresets.setTitle(finalMessageText1);
        FontPresets.setContent(finalMessageText2);

        finalMessageTextFlow.getChildren().add(finalMessageText1);
        finalMessageTextFlow.getChildren().add(finalMessageText2);

        contentArray[4].getChildren().add(finalMessageTextFlow);

        //
        //Navigation Buttons
        HBox navigationLayer = new HBox();
        Button previousButton = new Button("Prev");
        Button nextButton = new Button("Next");

        HBox progressBar = setupSceneTab.createProgressBar();

        navigationLayer.getChildren().addAll(previousButton, progressBar, nextButton);
        layout.setBottom(navigationLayer);

        previousButton.setOnAction(e->
        {
            setupSceneTab.decrementScene();

            //if the single user option is selected, bypass the following page, as it is not needed.
            if(userTypeSelectionSingle.isSelected() && setupSceneTab.getCurrentTab() == 3)
            {
                setupSceneTab.decrementScene();
            }

            setupSceneTab.updateProgressBar(progressBar);

            setupSceneTab.setPreviousButtonActive(previousButton);
            setupSceneTab.setNextButtonText(nextButton);

            layout.setCenter(setupSceneTab.setActiveTab(contentArray));
        });

        nextButton.setOnAction(e ->
        {
            //validate inputs to determine proceeding
            boolean validEntries = true;

            switch(setupSceneTab.getCurrentTab())
            {
                case 1:
                    if(businessNameInput.getText().isBlank())
                    {
                        validEntries = false;
                        FontPresets.setIncorrectTextField(businessNameInput);
                    }

                    if(selectedMonthInput.getValue() == null)
                    {
                        validEntries = false;
                        FontPresets.setIncorrectComboBox(selectedMonthInput);
                    }

                    if(selectedYearInput.getValue() == null)
                    {
                        validEntries = false;
                        FontPresets.setIncorrectComboBox(selectedYearInput);
                    }

                    break;

                case 2:
                    if(!userTypeSelectionMultiple.isSelected() && !userTypeSelectionSingle.isSelected())
                    {
                        validEntries = false;
                    }
                    
                    break;

                case 3:
                    if(firstUsernameInput.getText().isBlank())
                    {
                        validEntries = false;
                        FontPresets.setIncorrectTextField(firstUsernameInput);
                    }

                    if(firstUserPasswordInput.getText().isBlank())
                    {
                        validEntries = false;
                        FontPresets.setIncorrectTextField(firstUserPasswordInput);
                    }

                    if(firstUserFirstNameInput.getText().isBlank())
                    {
                        validEntries = false;
                        FontPresets.setIncorrectTextField(firstUserFirstNameInput);
                    }

                    if(firstUserLastNameInput.getText().isBlank())
                    {
                        validEntries = false;
                        FontPresets.setIncorrectTextField(firstUserLastNameInput);
                    }

                    break;

                case 4:
                    if(userTypeSelectionMultiple.isSelected())
                    {
                        business = new Business(businessNameInput.getText(), selectedMonthInput.getValue(), selectedYearInput.getValue(), true);
                        
                        User firstUser = new User(firstUsernameInput.getText(), firstUserPasswordInput.getText(), firstUserFirstNameInput.getText(), firstUserLastNameInput.getText());
                        business.addNewUser(firstUser);
                        
                        saveBusiness();
                        setLoginScene();
                    }
                    else
                    {
                        business = new Business(businessNameInput.getText(), selectedMonthInput.getValue(), selectedYearInput.getValue(), false);

                        saveBusiness();
                        loadLedger();
                        loadForm();
                        setMainMenuScene();
                    }

                    break;

                default:
                    //do nothing
                    break;
            }

            if(validEntries)
            {
                setupSceneTab.incrementScene();

                //if the single user option is selected, bypass the following page, as it is not needed.
                if(userTypeSelectionSingle.isSelected() && setupSceneTab.getCurrentTab() == 3)
                {
                    setupSceneTab.incrementScene();
                }

                setupSceneTab.updateProgressBar(progressBar);

                setupSceneTab.setPreviousButtonActive(previousButton);
                setupSceneTab.setNextButtonText(nextButton);

                layout.setCenter(setupSceneTab.setActiveTab(contentArray));
            }
            else
            {
                Alert alert = new Alert(AlertType.ERROR, "One or more required fields are missing.");
                alert.showAndWait();
            }
        });

        Scene output = new Scene(layout, 640, 480);
        window.setScene(output);
        window.show();
    }

    //setSettingsScene() - the main page for the settings, includes navigation buttons to specific software settings to change.
    public void setSettingsScene()
    {
        BorderPane layout = new BorderPane();

        Button backButton = new Button("Back");
        layout.setTop(backButton);

        VBox navigationButtonsLayer = new VBox();
        
        Button businessSettingsButton = new Button("Business Settings");
        Button accountsSettingsButton = new Button("Accounts Settings");
        Button uiSettings = new Button("UI Settings");

        navigationButtonsLayer.getChildren().addAll(businessSettingsButton, accountsSettingsButton, uiSettings);
        navigationButtonsLayer.setAlignment(Pos.CENTER);
        layout.setCenter(navigationButtonsLayer);

        backButton.setOnAction(e ->
        {
            setMainMenuScene();
        });

        businessSettingsButton.setOnAction(e ->
        {
            //setBusinessSettingsScene();
        });

        accountsSettingsButton.setOnAction(e -> 
        {
            //setAccountsSettingsScene();
        });

        uiSettings.setOnAction(e -> 
        {
            //to be implemented later...
        });

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
            setMainMenuScene();
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
        TableView<Transaction> transactionTable = CustomTableViews.createTransactionTable(ledger.getAllUnpostedTransactions());

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
    //Will need to incorporate a better error page that can save trace logs leading to error.
    public void setErrorScene(int errorCode, String error)
    {
        StackPane layout = new StackPane();

        //
        //Labels
        VBox errorMessageLayer = new VBox(4);

        Label errorCodeLabel = new Label("ERR " + errorCode);
        Label errorReport = new Label("A fatal error has caused the program to stop.");
        Label errorMessage = new Label(error);

        errorMessageLayer.getChildren().addAll(errorCodeLabel, errorReport, errorMessage);
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
            setMainMenuScene();
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
        TableView<AccountDetail> accountDetailTable = CustomTableViews.createAccountDetailTable(form.getAccountDetail());

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

            Label totalLabel = new Label("Totals: ");
            Label debitValueTotalLabel = new Label(printCurrency(codeDebitTotal));
            Label creditValueTotalLabel = new Label(printCurrency(codeCreditTotal));
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

        //
        //Back Button
        Button backButton = new Button("Back");

        backButton.setOnAction(e ->
        {
            setMainMenuScene();
        });

        topLayer.getChildren().add(backButton);
        layout.setTop(topLayer);

        VBox centerLayer = new VBox();

        //preset widths for labels/inputs
        int setWidth1 = 150;
        int setWidth2 = 300;

        //
        //Statement Type Input
        HBox statementTypeLayer = new HBox();
        Label statementTypeLabel = new Label("Statement Type: ");

        ComboBox<String> statementTypeInput = new ComboBox<>();
        ArrayList<String> statementOptions = new ArrayList<>();
        statementOptions.add("Income Statement");
        statementOptions.add("Balance Sheet");
        statementOptions.add("Statement of Retained Earnings");

        statementTypeInput.setItems(FXCollections.observableList(statementOptions));

        statementTypeLayer.setPrefWidth(setWidth1);
        statementTypeInput.setPrefWidth(setWidth2);

        statementTypeLayer.getChildren().addAll(statementTypeLabel, statementTypeInput);

        //
        //Statement Range/Type
        HBox datedRangeLayer = new HBox();
        Label datedRangeLabel = new Label("Date Range");

        ComboBox<String> datedRangeInput = new ComboBox<>();
        ArrayList<String> rangeOptions = new ArrayList<>();
        rangeOptions.add("Month");
        rangeOptions.add("Quarter");
        rangeOptions.add("Year");

        datedRangeInput.setItems(FXCollections.observableList(rangeOptions));

        datedRangeLabel.setPrefWidth(setWidth1);
        datedRangeInput.setPrefWidth(setWidth2);

        datedRangeLayer.getChildren().addAll(datedRangeLabel, datedRangeInput);

        //
        //Note: Based on the statement range, the following options may not all be available.

        //
        //Selected Month Input
        HBox selectedMonthLayer = new HBox();
        Label selectedMonthLabel = new Label("Selected Month: ");

        ComboBox<Month> selectedMonthInput = new ComboBox<>();
        selectedMonthInput.getItems().addAll(Month.values());

        selectedMonthLabel.setPrefWidth(setWidth1);
        selectedMonthInput.setPrefWidth(setWidth2);

        selectedMonthLayer.getChildren().addAll(selectedMonthLabel, selectedMonthInput);

        //
        //Selected Quarter Input
        HBox selectedQuarterLayer = new HBox();
        Label selectedQuarterLabel = new Label("Selected Quarter: ");

        ComboBox<String> selectedQuarterInput = new ComboBox<>();
        ArrayList<String> quarterOptions = new ArrayList<>();
        quarterOptions.add("Q1");
        quarterOptions.add("Q2");
        quarterOptions.add("Q3");
        quarterOptions.add("Q4");
        selectedQuarterInput.setItems(FXCollections.observableList(quarterOptions));

        selectedQuarterLabel.setPrefWidth(setWidth1);
        selectedQuarterInput.setPrefWidth(setWidth2);

        selectedQuarterLayer.getChildren().addAll(selectedQuarterLabel, selectedQuarterInput);

        //
        //Selected Year Input
        HBox selectedYearLayer = new HBox();
        Label selectedYearLabel = new Label("Selected Fiscal Year: ");

        ComboBox<Year> selectedYearInput = new ComboBox<>();

        //add only from the business's starting point to the current year.
        for(int current = business.getStartingYear().getValue(); current <= Year.now().getValue(); current++)
        {
            selectedYearInput.getItems().add(Year.of(current));
        }

        selectedYearLayer.getChildren().addAll(selectedYearLabel, selectedYearInput);

        //set all input layers other than type and range to hidden.
        selectedMonthLayer.setVisible(false);
        selectedMonthLayer.setManaged(false);
        selectedQuarterLayer.setVisible(false);
        selectedQuarterLayer.setManaged(false);
        selectedYearLayer.setVisible(false);
        selectedYearLayer.setManaged(false);

        //This control must occur after all inputs are created.
        datedRangeInput.setOnAction(e ->
        {
            //Month-to-Date: includes the month and year to generate.
            if(datedRangeInput.getValue().compareTo("Month") == 0)
            {
                selectedMonthLayer.setVisible(true);
                selectedMonthLayer.setManaged(true);
                selectedQuarterLayer.setVisible(false);
                selectedQuarterLayer.setManaged(false);
                selectedYearLayer.setVisible(true);
                selectedYearLayer.setManaged(true);
            }
            //Quarter-to-Date: includes the quarter and year to generate.
            else if(datedRangeInput.getValue().compareTo("Quarter") == 0)
            {
                selectedMonthLayer.setVisible(false);
                selectedMonthLayer.setManaged(false);
                selectedQuarterLayer.setVisible(true);
                selectedQuarterLayer.setManaged(true);
                selectedYearLayer.setVisible(true);
                selectedYearLayer.setManaged(true);
            }
            //Year-to-Date: includes only the year to generate.
            else if(datedRangeInput.getValue().compareTo("Year") == 0)
            {
                selectedMonthLayer.setVisible(false);
                selectedMonthLayer.setManaged(false);
                selectedQuarterLayer.setVisible(false);
                selectedQuarterLayer.setManaged(false);
                selectedYearLayer.setVisible(true);
                selectedYearLayer.setManaged(true);
            }
        });

        //assign widths for all inputs and labels
        statementTypeLabel.setPrefWidth(setWidth1);
        statementTypeInput.setPrefWidth(setWidth2);
        selectedQuarterLabel.setPrefWidth(setWidth1);
        selectedQuarterInput.setPrefWidth(setWidth2);
        selectedMonthLabel.setPrefWidth(setWidth1);
        selectedMonthInput.setPrefWidth(setWidth2);
        selectedQuarterLabel.setPrefWidth(setWidth1);
        selectedQuarterInput.setPrefWidth(setWidth2);
        selectedYearLabel.setPrefWidth(setWidth1);
        selectedYearInput.setPrefWidth(setWidth2);

        TableView<DatedStatement> statementTable = CustomTableViews.createDatedStatementTable(form.getDatedStatement());
        layout.setBottom(statementTable);

        statementTable.setOnMouseClicked(e ->
        {
            //on double click, generate form for previous instance, opens as new window.
            if (e.getClickCount() == 2 )
            {
                determineStatementScene(statementTable.getSelectionModel().getSelectedItem());
            }
        });

        //
        //Generate Button
        Button generateButton = new Button("Generate");

        generateButton.setOnAction(e -> 
        {
            //
            //DatedStatement Object Creation

            //create the DatedStatement object, assign values based on type
            DatedStatement datedStatement = new DatedStatement();

            //validate a statement type was selected, then assign to datedStatement if not null.
            if(statementTypeInput.getValue() == null)
            {
                Alert alert = new Alert(AlertType.ERROR, "No statement type was selected...");
                alert.showAndWait();
                return;
            }
            
            datedStatement.setStatementType(statementTypeInput.getValue());

            //determine date range type based on inputs
            if(datedRangeInput.getValue().compareTo("Month") == 0)
            {
                //validate a month and year were selected
                if(selectedMonthInput.getValue() == null || selectedYearInput.getValue() == null)
                {
                    Alert alert = new Alert(AlertType.ERROR, "One or more inputs are empty.");
                    alert.showAndWait();
                    return;
                }

                //if the selected month is less than the business's starting month, roll forward a year, due to the fiscal year still ocurring
                //I.E. if a business starts in April, their fiscal year ends in March of the next year.
                if(selectedMonthInput.getValue().getValue() <= business.getStartingMonth().getValue())
                {
                    datedStatement.setDateRangeMonth(selectedMonthInput.getValue(), selectedYearInput.getValue(), true);
                }
                else
                {
                    datedStatement.setDateRangeMonth(selectedMonthInput.getValue(), selectedYearInput.getValue(), false);
                }

                datedStatement.setStatementPeriodDesc(selectedMonthInput.getValue().toString());
            }
            else if(datedRangeInput.getValue().compareTo("Quarter") == 0)
            {
                //validate a quarter and year were selected
                if(selectedQuarterInput.getValue() == null || selectedYearInput.getValue() == null)
                {
                    Alert alert = new Alert(AlertType.ERROR, "One or more inputs are empty.");
                    alert.showAndWait();
                    return;
                }

                if(business.getQuarterStartMonth(selectedQuarterInput.getValue()).getValue() <= business.getStartingMonth().getValue())
                {
                    datedStatement.setDateRangeQuarter(business.getQuarterStartMonth(selectedQuarterInput.getValue()), selectedYearInput.getValue(), true);
                }
                else
                {
                    datedStatement.setDateRangeQuarter(business.getQuarterStartMonth(selectedQuarterInput.getValue()), selectedYearInput.getValue(), false);
                }

                datedStatement.setStatementPeriodDesc(selectedQuarterInput.getValue());
            }
            else if(datedRangeInput.getValue().compareTo("Year") == 0)
            {
                //validate a year was selected
                if(selectedYearInput.getValue() == null)
                {
                    Alert alert = new Alert(AlertType.ERROR, "One or more inputs are empty.");
                    alert.showAndWait();
                    return;
                }

                datedStatement.setDateRangeYear(business.getStartingMonth(), selectedYearInput.getValue());
            }
            else //if this point is reached, no type was selected, so show alert.
            {
                Alert alert = new Alert(AlertType.ERROR, "No date range was selected...");
                alert.showAndWait();
                return;
            }

            //if working with a balance sheet, override the start date to the company's starting date.
            //the balance sheet is a snapshot of all a company's transactions to the specified date.
            if(datedStatement.getStatementType().compareTo("Balance Sheet") == 0)
            {
                datedStatement.setStartDate(LocalDate.of(business.getStartingYear().getValue(), business.getStartingMonth(), 1));
            }

            //fetch posted transactions within date range
            ArrayList<Transaction> fetchedTransactions = ledger.getTransactionsByDateRange(datedStatement.getStartDate(), datedStatement.getEndDate());
            ledger.filterTransactionsByStatus(fetchedTransactions, "Posted");

            //filter transactions based on the statement type; not all accounts are needed for specific statements. A statement should be selected by this point.
            if(statementTypeInput.getValue().compareTo("Income Statement") == 0)
            {
                ledger.filterTransactionsByAccountCodeRange(fetchedTransactions, "6000.001", "8999.999");
            }
            else if(statementTypeInput.getValue().compareTo("Balance Sheet") == 0)
            {
                ledger.filterTransactionsByAccountCodeRange(fetchedTransactions, "1000.001", "4999.999");
            }
            else if(statementTypeInput.getValue().compareTo("Statement of Retained Earnings") == 0)
            {
                ledger.filterTransactionsByAccountCodeRange(fetchedTransactions, "5000.001", "5999.999");
            }
            else
            {
                setErrorScene(2, "An unexpected error in generating a statement has ocurred. Please note this error, then restart the program.");
            }

            //confirm at least one transaction was found by filtered search
            if(fetchedTransactions.size() <= 0)
            {
                Alert alert = new Alert(AlertType.ERROR, "No transactions were in the dated range. Confirm the date range has posted transactions and statement" + " type has transactions that would appear, then try again.");
                alert.showAndWait();
                return;
            }

            //for each transaction that was found, add to the IncomeStatement object's ArrayList
            for(Transaction current : fetchedTransactions)
            {
                datedStatement.insertReferencedTransaction(current.getTransactionNumber());
            }

            //add the new AccountDetail to the form object, then call to save
            form.insertStatement(datedStatement);
            saveForm();
            System.out.println("Created a statement with " + datedStatement.getReferencedTransactions().size() + " ref transactions");

            determineStatementScene(datedStatement);
            statementTable.setItems(FXCollections.observableArrayList(form.getDatedStatement()));
        });

        centerLayer.getChildren().addAll(statementTypeLayer, datedRangeLayer, selectedMonthLayer, selectedQuarterLayer, selectedYearLayer, generateButton);
        layout.setCenter(centerLayer);

        Scene output = new Scene(layout, 640, 480);
        window.setScene(output);
        window.show();
    }

    //setGeneratedIncomeStatementScene() - creates an income statement for an applicable DatedStatement selected.
    //This appears in a separate window from the main window.
    public void setGeneratedIncomeStatementScene(DatedStatement passedStatement)
    {
        //setup the second window
        Stage secondWindow = new Stage();
        VBox mainView = new VBox();

        //preset widths used for content layer, array for functions
        int[] setWidth = new int[3];
        setWidth[0] = 300;
        setWidth[1] = 20;
        setWidth[2] = 100;

        //setup the form and VBox to hold all pages
        ScrollPane document = new ScrollPane();
        VBox allPages = new VBox();

        //
        //Print Button
        Button printButton = new Button("Print");
        mainView.getChildren().addAll(printButton, document);

        printButton.setOnAction(e ->
        {
            printDocument(allPages);
        });

        //
        //Header Layer
        VBox headerLayer = new VBox();
        Label businessNameLabel = new Label(business.getBusinessName());
        Label statementTypeLabel = new Label("Income Statement");
        Label statementDateLabel = new Label(passedStatement.printDateHeader());

        headerLayer.setAlignment(Pos.CENTER);
        headerLayer.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 16px;");

        headerLayer.getChildren().addAll(businessNameLabel, statementTypeLabel, statementDateLabel);
        allPages.getChildren().add(headerLayer);

        //retrieve transactions contained within the account detail's referenced account array.
        //note that it is still possible no entries can exist with the given search criteria.
        ArrayList<Transaction> fetchedTransactions = ledger.getTransactionByCodeArray(passedStatement.getReferencedTransactions());
        ArrayList<ProcessedAccount> processedAccounts = ledger.processReferencedTransactions(fetchedTransactions);

        //separate processed accounts into revenue and expense ArrayList
        ArrayList<ProcessedAccount> revenueAccounts = new ArrayList<>();
        ArrayList<ProcessedAccount> expenseAccounts = new ArrayList<>();

        for(ProcessedAccount currentAccount : processedAccounts)
        {
            //if the processed account is a revenue account (codes starting in 6), assign to revenue ArrayList.
            if(currentAccount.getAccountRef().compareTo("6000.001") >= 0 && currentAccount.getAccountRef().compareTo("6999.999") <= 0)
            {
                revenueAccounts.add(currentAccount);
            }
            //else if the processed account is an expense account (codes starting in 7 or 8), assign to expense ArrayList.
            else if(currentAccount.getAccountRef().compareTo("7000.001") >= 0 && currentAccount.getAccountRef().compareTo("8999.999") <= 0)
            {
                expenseAccounts.add(currentAccount);
            }
            
            //if neither, the processed account is ignored, as it is not applicable to the income statement.
        }

        //
        //Revenue & Expense Headers
        VBox revenueLayer = new VBox();
        Label revenueLabel = new Label("Revenue:");
        revenueLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 16px;");

        VBox expenseLayer = new VBox();
        Label expenseLabel = new Label("Expenses:");
        expenseLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 16px;");

        revenueLayer.getChildren().add(revenueLabel);
        expenseLayer.getChildren().add(expenseLabel);

        //Row spacers used during creation of entry rows.
        Label[] spacer = new Label[2];
        spacer[0] = new Label("");
        spacer[1] = new Label("");

        spacer[0].setPrefWidth(setWidth[1]);
        spacer[1].setPrefWidth(setWidth[1]);

        //BigDecimals to hold total amounts
        BigDecimal revBigDecimal = new BigDecimal(0);
        BigDecimal expBigDecimal = new BigDecimal(0);

        //for each item in the revenue and expense ArrayList, an HBox is generated and stored in their respective VBox layer, and BigDecimal total is incremented/decremented.
        Iterator<ProcessedAccount> revIterator = revenueAccounts.iterator();
        ArrayList<ProcessedAccount> skipList = new ArrayList<>();

        //for each revenue item, create an entry row. Increment revenue subtotal.
        while(revIterator.hasNext())
        {
            ProcessedAccount currentRevAccount = revIterator.next();

            if(!skipList.contains(currentRevAccount))
            {
                //if an account has references to contra-revenue accounts, extra work will be necessary, otherwise handle account normally.
                if(currentRevAccount.getHasContra())
                {
                    createStatementLayers(currentRevAccount, revenueLayer, spacer, setWidth, false);

                    ArrayList<ProcessedAccount> matchedContraAccounts = ProcessedAccount.getProcessedContraAccounts(revenueAccounts, currentRevAccount.getAccountRef());

                    for(ProcessedAccount contraAccount : matchedContraAccounts)
                    {
                        createStatementLayers(contraAccount, revenueLayer, spacer, setWidth, true);
                        revBigDecimal.subtract(contraAccount.getBalance());
                        skipList.add(contraAccount);
                    }
                }
                else
                {
                    createStatementLayers(currentRevAccount, revenueLayer, spacer, setWidth, false);
                }
            }

            revBigDecimal = revBigDecimal.add(currentRevAccount.getBalance());
            revIterator.remove();
        }

        //repeat the process for the expense processed accounts.
        Iterator<ProcessedAccount> expIterator = expenseAccounts.iterator();
        skipList = new ArrayList<>(); //reset the skipList;

        //for each expense item, create an entry row. Increment expense subtotal.
        while(expIterator.hasNext())
        {
            ProcessedAccount currentExpAccount = expIterator.next();

            if(!skipList.contains(currentExpAccount))
            {
                //if an account has references to contra-expense accounts (rare), extra work will be necessary, otherwise handle account normally.
                if(currentExpAccount.getHasContra())
                {
                    createStatementLayers(currentExpAccount, expenseLayer, spacer, setWidth, false);

                    ArrayList<ProcessedAccount> matchedContraAccounts = ProcessedAccount.getProcessedContraAccounts(revenueAccounts, currentExpAccount.getAccountRef());

                    for(ProcessedAccount contraAccount : matchedContraAccounts)
                    {
                        createStatementLayers(contraAccount, expenseLayer, spacer, setWidth, true);
                        expBigDecimal.subtract(contraAccount.getBalance());
                        skipList.add(contraAccount);
                    }
                }
                else
                {
                    createStatementLayers(currentExpAccount, expenseLayer, spacer, setWidth, false);
                }
            }

            expBigDecimal = revBigDecimal.add(currentExpAccount.getBalance());
            expIterator.remove();
        }

        //
        //Revenue Total Layer w/ Styling
        HBox revTotalLayer = new HBox();
        revTotalLayer.setStyle("-fx-background-color:rgb(187, 187, 187); -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");
        revTotalLayer.setSpacing(5);

        Label revTotalDescLabel = new Label("Total Revenue: ");
        Label revTotalLabel = new Label(printCurrency(revBigDecimal));

        revTotalDescLabel.setPrefWidth(setWidth[0]);
        revTotalLabel.setPrefWidth(setWidth[2]);
        revTotalLabel.setAlignment(Pos.CENTER_RIGHT);

        revTotalLayer.getChildren().addAll(revTotalDescLabel, revTotalLabel);

        //
        //Expense Total Layer w/ Styling
        HBox expTotalLayer = new HBox();
        expTotalLayer.setStyle("-fx-background-color:rgb(187, 187, 187); -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");
        expTotalLayer.setSpacing(5);

        Label expTotalDescLabel = new Label("Total Expenses: ");
        Label expTotalLabel = new Label(printCurrency(expBigDecimal));

        expTotalDescLabel.setPrefWidth(setWidth[0]);
        expTotalLabel.setPrefWidth(setWidth[2]);
        expTotalLabel.setAlignment(Pos.CENTER_RIGHT);

        expTotalLayer.getChildren().addAll(expTotalDescLabel, expTotalLabel);

        //
        //Net Income Layer
        HBox netIncomeLayer = new HBox();
        netIncomeLayer.setStyle("-fx-background-color:rgb(187, 187, 187); -fx-border-color: #cccccc; -fx-border-width: 0 0 1 0;");
        netIncomeLayer.setSpacing(5);

        BigDecimal netIncome = new BigDecimal(0);
        netIncome = netIncome.add(revBigDecimal);
        netIncome = netIncome.subtract(expBigDecimal);

        Label netIncomeDescLabel = new Label("Net Income (Loss): ");
        Label netIncomeLabel = new Label(printCurrency(netIncome));

        netIncomeDescLabel.setPrefWidth(setWidth[0]);
        netIncomeLabel.setPrefWidth(setWidth[2]);
        netIncomeLabel.setAlignment(Pos.CENTER_RIGHT);

        netIncomeLayer.getChildren().addAll(netIncomeDescLabel, netIncomeLabel);

        //
        // Current implementation does not account for multiple pages. This will need to be revisited following testing with extensive journal entry data.
        //

        //Combine all layers and create document
        allPages.getChildren().addAll(revenueLayer, revTotalLayer, expenseLayer, expTotalLayer, netIncomeLayer);
        document.setContent(allPages);

        Scene output = new Scene(mainView, 640, 480);
        secondWindow.setScene(output);
        secondWindow.show();
    }

    //setGeneratedBalanceSheetScene() - creates a balance sheet for an applicable DatedStatement selected.
    //This appears in a separate window from the main window.
    public void setGeneratedBalanceSheetScene(DatedStatement passedStatement)
    {
        //to be added in future update
    }

    //setGeneratedStatementOfRetainedEarningsScene() - creates a statement of retained earnings for an applicable DatedStatement selected.
    //This appears in a separate window from the main window.
    public void setGeneratedStatementOfRetainedEarningsScene(DatedStatement passedStatement)
    {
        //to be added in future update
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

    public void saveBusiness()
    {
        try 
        {
            FileOutputStream fileOut = new FileOutputStream("Business.dat");
            ObjectOutputStream outStream = new ObjectOutputStream(fileOut);

            outStream.writeObject(business);
            outStream.close();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //loadBusinessDetail() - if a "business.dat" file exists, the program will load it's serialized values at runtime.
    //If it does not exist, program should flag first runtime instance and run setup.
    public void loadBusiness()
    {
        try
        {
            FileInputStream fileIn = new FileInputStream("Business.dat");
            ObjectInputStream inStream = new ObjectInputStream(fileIn);

            business = (Business) inStream.readObject();
            inStream.close();
        }
        catch (FileNotFoundException e)
        {
            //force business to null to flag for first-time setup
            business = null;
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

    //determineStatementScene() - takes a DatedStatement and based on statement type calls function to set appropriate statement scene.
    public void determineStatementScene(DatedStatement passedStatement)
    {
        if(passedStatement.getStatementType().compareTo("Income Statement") == 0)
        {
            setGeneratedIncomeStatementScene(passedStatement);
        }
        else if(passedStatement.getStatementType().compareTo("Balance Sheet") == 0)
        {
            setGeneratedBalanceSheetScene(passedStatement);
        }
        else if(passedStatement.getStatementType().compareTo("Statement of Retained Earnings") == 0)
        {
            setGeneratedStatementOfRetainedEarningsScene(passedStatement);
        }
        else
        {
            Alert alert = new Alert(AlertType.ERROR, "Failed to determine the statement type.");
            alert.showAndWait();
        }
    }

    //createStatementLayers() - creates an HBox of a processed account item row for forms, stores into passed VBox layer.
    public void createStatementLayers(ProcessedAccount inAccount, VBox passedLayer, Label[] spacers, int[] setWidth, boolean isContra)
    {
        HBox currentAccountLayer = new HBox();
        Label accountDescLabel, accountTotalLabel;
        
        if(isContra)
        {
            accountDescLabel = new Label("Less: " + ledger.getAccountDescriptionByCode(inAccount.getAccountRef()));
            accountTotalLabel = new Label(printCurrency(inAccount.getBalance().multiply(new BigDecimal(-1))));
        }
        else
        {
            accountDescLabel = new Label(ledger.getAccountDescriptionByCode(inAccount.getAccountRef()));
            accountTotalLabel = new Label(printCurrency(inAccount.getBalance()));
        }

        accountDescLabel.setPrefWidth(setWidth[0]);
        accountTotalLabel.setPrefWidth(setWidth[2]);

        accountTotalLabel.setAlignment(Pos.CENTER_RIGHT);

        //currentAccountLayer.getChildren().addAll(accountDescLabel, spacers[0], accountTotalLabel, spacers[1]);    //may need to swap
        currentAccountLayer.getChildren().addAll(accountDescLabel, accountTotalLabel);
        passedLayer.getChildren().add(currentAccountLayer);
    }

//
// Other Functions
//

    //printCurrency() - takes a BigDecimal and returns as a string formatted to accounting currency including parenthesis for negatives.
    public String printCurrency(BigDecimal value)
    {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US); //used for currency formatting
        DecimalFormat df = (DecimalFormat) nf;

        df.applyPattern("$#,##0.00;($#,##0.00)");
        return df.format(value);
    }

//
// Event Handlers
//

    EventHandler<KeyEvent> textFieldStyleHandler = event -> {
        TextField tf = (TextField) event.getSource();
        FontPresets.setTextField(tf);
    };

    EventHandler<ActionEvent> comboBoxStyleHandler = event -> {
        ComboBox cb = (ComboBox) event.getSource();
        FontPresets.setComboBox(cb);
    };

}

//Programmer Notes for future reference
    //Consider revising account detail section to incorporate different page sizes, allowing different dimensions as necessary
    //Make a Save to PDF button. Will likely require additional dependencies.
    //Need to sort processed accounts for use with statements