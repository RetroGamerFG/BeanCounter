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
        document.getElementById("financialStatementForm").action = '/create_financial_statement';
        financialStatementForm.submit();
    });

    // Force starting date to always be the first of the month
    const startingDateInput = document.querySelector('input[name="startingDate"]');
    if (startingDateInput) {
        startingDateInput.addEventListener('change', function() {
            if (this.value) {
                const date = new Date(this.value + 'T00:00:00');
                const year = date.getFullYear();
                const month = String(date.getMonth() + 1).padStart(2, '0');
                this.value = `${year}-${month}-01`;
            }
        });
    }
});

//correct later for other statement types
function viewStatement(button)
{
    window.location.href = 'http://localhost:8080/statements/view/income_statement/' + button.value;
}