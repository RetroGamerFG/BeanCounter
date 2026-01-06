function loadJournalEntry(button)
{
    window.location.href = 'http://localhost:8080/general_ledger/journal_entry_view/' + button.value; // Perform the navigation
}

function removeJournalEntry(button)
{
    window.location.href = 'http://localhost:8080/delete_journal_entry/' + button.value;
}