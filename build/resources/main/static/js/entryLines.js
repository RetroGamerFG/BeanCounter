document.getElementById('backButton').addEventListener('click', function(event)
{
    event.preventDefault();  // Prevent the form from being submitted
    window.location.href = 'http://localhost:8080/general_ledger'; // Perform the navigation
});

document.getElementById('editButton').addEventListener('click', function(event)
{
    event.preventDefault();  // Prevent form submission
    // Your edit logic here
});

function addRow() {
    const tbody = document.getElementById('lineItems');
    const rows = tbody.getElementsByClassName('line-row');

    // Clone the first row as it already has the populated <select> options
    const newRow = rows[0].cloneNode(true);
    
    // Clear the values
    const inputs = newRow.querySelectorAll('input');
    inputs.forEach(input => input.value = '');
    
    // Reset selection to first option
    const select = newRow.querySelector('select');
    if(select) select.selectedIndex = 0;

    tbody.appendChild(newRow);
    reIndexRows();
}

function removeRow(button) {
    const tbody = document.getElementById('lineItems');
    const rows = tbody.getElementsByClassName('line-row');

    // Business rule: Maintain at least two rows for a valid journal entry
    if (rows.length > 2) {
        button.closest('tr').remove();
        reIndexRows();
    } else {
        alert("A journal entry must have at least two lines.");
    }
}

function reIndexRows() {
    const rows = document.querySelectorAll('.line-row');
    rows.forEach((row, index) => {
        const select = row.querySelector('select');
        // Find inputs specifically by their position or class
        const inputs = row.querySelectorAll('input[type="number"]');
        
        if (select) {
            select.name = `lines[${index}].account`;
            select.id = `lines${index}.account`;
        }

        // Assuming index 0 is Debit and index 1 is Credit based on your HTML
        if (inputs.length >= 2) {
            inputs[0].name = `lines[${index}].debitAmount`;
            inputs[0].id = `lines${index}.debitAmount`;
            
            inputs[1].name = `lines[${index}].creditAmount`;
            inputs[1].id = `lines${index}.creditAmount`;
        }

        const removeBtn = row.querySelector('.btn-remove');
        removeBtn.style.display = (rows.length > 2) ? 'inline-block' : 'none';
    });
}

function createNewRow() {
    const row = document.createElement('tr');
    row.classList.add('line-row');
    row.innerHTML = `
        <td>
            <select name="lines[0].account" id="lines0.account">
                <option th:each="acc : ${allAccounts}" th:value="${acc.AccountID}" th:text="${acc.AccountName}"></option>
            </select>
        </td>
        <td><input type="number" step="0.01" name="lines[0].amount" id="lines0.amount" /></td>
        <td><input type="number" step="0.01" name="lines[0].amount" id="lines0.amount" /></td>
        <td><button type="button" onclick="removeRow(this)" class="btn-remove">Remove</button></td>
    `;
    return row;
}

// Run once on page load to set initial button visibility
window.onload = function() {
    reIndexRows();
};
