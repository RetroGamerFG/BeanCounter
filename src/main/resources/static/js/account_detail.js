document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('backButton').addEventListener('click', function(event)
    {
        event.preventDefault();  // Prevent the form from being submitted
        window.location.href = 'http://localhost:8080/general_ledger'; // Perform the navigation
    });

    document.getElementById('createButton').addEventListener('click', function(event)
    {
        event.preventDefault();
        const entryForm = document.getElementById('entry-form');
        entryForm.hidden = false;
    });
});