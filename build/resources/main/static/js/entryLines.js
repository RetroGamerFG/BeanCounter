document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('backButton').addEventListener('click', function(event)
    {
        event.preventDefault();  // Prevent the form from being submitted
        window.location.href = 'http://localhost:8080/general_ledger'; // Perform the navigation
    });

    if(document.getElementById('submitButton').value === 'p') //if page's journalEntry is under review, submit button will have value of 'p'
    {
        setPostView();

        //add event listener for 'editButton' to revert to 'submit' version of page
        document.getElementById('editButton').addEventListener('click', function(event)
        {
            event.preventDefault();  // Prevent form submission
            setEditView();
        });
    }

    document.getElementById('submitButton').addEventListener('click', function(event)
    {
        console.log("listener for submit/post was pressed");
        if(submitButton.value === 'p')
        {
            console.log("Submit was pressed with value post")
            document.getElementById('journalForm').action = '/post_journal_entry';
        }
        else if (submitButton.value === 's')
        {
            console.log("Submit was pressed with value save")
            document.getElementById('journalForm').action = '/save_journal_entry';
        }

        journalForm.submit();
    });
});

function addRow()
{
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

function removeRow(button)
{
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

function reIndexRows()
{
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

function createNewRow()
{
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

function setPostView()
{
    //disable 'postDate' and 'description'
    const postDate = document.getElementById('postDate');
    postDate.classList.add('lock-look');

    const transactionDescription = document.getElementById('transactionDescription');
    transactionDescription.classList.add('lock-look');

    //fetch line items from 'line-row'
    const rows = document.querySelectorAll('.line-row');

    //disable each, using class 'lock-look' to maintain look
    rows.forEach((row) => {
        const select = row.querySelector('select');
        const inputs = row.querySelectorAll('input[type="number"]');
        const button = row.querySelector('button');

        if (select) 
        {
            select.classList.add('lock-look');
        }

        if (inputs)
        {
            inputs[0].classList.add('lock-look');
            inputs[1].classList.add('lock-look');
        }

        if (button)
        {
            button.disabled = true;
        }
    });

    //hide the 'addRow' button
    const addRowButton = document.getElementById('addRowButton');
    addRowButton.disabled = true;
    addRowButton.style.display = "none";
}

function setEditView()
{
    //enable 'postDate' and 'description'
    const postDate = document.getElementById('postDate');
    postDate.classList.remove('lock-look');

    const transactionDescription = document.getElementById('transactionDescription');
    transactionDescription.classList.remove('lock-look');

    //revert 'post' button to 'submit' button
    const submitButton = document.getElementById('submitButton');
    submitButton.value = "s";
    submitButton.innerHTML = "Submit";

    //disable the 'edit' button
    var editButton = document.getElementById('editButton');
    editButton.disabled = true;

    //fetch line items from 'line-row'
    const rows = document.querySelectorAll('.line-row');

    //enable each, removing class 'lock-look'
    rows.forEach((row) => {
        const select = row.querySelector('select');
        const inputs = row.querySelectorAll('input[type="number"]');
        const button = row.querySelector('button');

        if (select) 
        {
            select.classList.remove('lock-look');
        }

        if (inputs)
        {
            inputs[0].classList.remove('lock-look');
            inputs[1].classList.remove('lock-look');
        }

        if (button)
        {
            button.disabled = false;
        }
    });

    //hide the 'addRow' button
    const addRowButton = document.getElementById('addRowButton');
    addRowButton.disabled = false;
    addRowButton.style.display = "block";

    //hide the 'postSignature' field
    const postSection = document.querySelector('.post-signature-section');
    postSection.style.display = "none";
}

// Run once on page load to set initial button visibility
window.onload = function() {
    reIndexRows();
};