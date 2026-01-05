CREATE TABLE Account (
    AccountID INT AUTO_INCREMENT PRIMARY KEY,
    AccountCode DECIMAL (7, 3) NOT NULL,
    AccountName VARCHAR(32) NOT NULL,
    AccountDescription VARCHAR(64),
    AccountType CHAR(1) NOT NULL,
    isContra BOOLEAN
);

CREATE TABLE JournalEntry (
    JournalEntryID INT AUTO_INCREMENT PRIMARY KEY,
    CreationDate TIMESTAMP NOT NULL,
    PostDate TIMESTAMP,
    TransactionDescription VARCHAR(64),
    IsEditable BOOLEAN
);

CREATE TABLE JournalEntryLine (
    JournalEntryLineID INT AUTO_INCREMENT PRIMARY KEY,
    JournalEntryID INT NOT NULL,
    AccountID INT NOT NULL,
    TransactionType CHAR(1),
    Amount DECIMAL(19, 4),

    FOREIGN KEY (JournalEntryID) REFERENCES JournalEntry(JournalEntryID),
    FOREIGN KEY (AccountID) REFERENCES Account(AccountID)
);
