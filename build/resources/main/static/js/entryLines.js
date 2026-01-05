function addRow() {
    const tbody = document.getElementById('lineItems');
    const rows = tbody.getElementsByClassName('line-row');
    
    // Clone the first row to use as a template
    const newRow = rows[0].cloneNode(true);
    
    // Clear the inputs in the cloned row
    const inputs = newRow.querySelectorAll('input, select');
    inputs.forEach(input => input.value = '');

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
        // Update the name attributes so Spring can bind to the List
        // Example: lines[0].account, lines[1].account...
        const select = row.querySelector('select');
        const input = row.querySelector('input');
        
        if (select) {
            select.name = `lines[${index}].account`;
            select.id = `lines${index}.account`;
        }
        if (input) {
            input.name = `lines[${index}].amount`;
            input.id = `lines${index}.amount`;
        }

        // Toggle remove button visibility
        const removeBtn = row.querySelector('.btn-remove');
        removeBtn.style.display = (rows.length > 2) ? 'inline-block' : 'none';
    });
}

// Run once on page load to set initial button visibility
document.addEventListener('DOMContentLoaded', reIndexRows);