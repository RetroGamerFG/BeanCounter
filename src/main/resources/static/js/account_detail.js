document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('backButton').addEventListener('click', function(event)
    {
        event.preventDefault();  // Prevent the form from being submitted
        window.location.href = 'http://localhost:8080/'; // Perform the navigation
    });

    document.getElementById('createButton').addEventListener('click', function(event)
    {
        event.preventDefault();
        const entryForm = document.getElementById('entry-form');
        entryForm.hidden = false;
    });

    document.getElementById('generateButton').addEventListener('click', function(event)
    {
        event.preventDefault();
        document.getElementById("accountDetailForm").action = '/create_account_detail';
        accountDetailForm.submit();
    });
});

function viewDetail(button)
{
    window.location.href = 'http://localhost:8080/account_detail/view/' + button.value;
}