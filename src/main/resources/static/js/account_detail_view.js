document.addEventListener('DOMContentLoaded', function() {
    // Add click handlers to year headers
    document.querySelectorAll('.year-block').forEach(yearRow => {
        const yearHeader = yearRow.querySelector('.year-header');
        yearHeader.style.cursor = 'pointer';
        
        yearHeader.addEventListener('click', function() {
            const year = yearRow.getAttribute('data-year');
            const table = yearRow.closest('table');
            toggleYear(year, table);
        });
    });

    // Add click handlers to month headers
    document.querySelectorAll('.month-block').forEach(monthRow => {
        const monthHeader = monthRow.querySelector('.month-header');
        monthHeader.style.cursor = 'pointer';
        
        monthHeader.addEventListener('click', function() {
            const year = monthRow.getAttribute('data-year');
            const month = monthRow.getAttribute('data-month');
            const table = monthRow.closest('table');
            toggleMonth(year, month, table);
        });
    });
});

function toggleYear(year, table) {
    // Find all month blocks and entry lines for this year within this specific table
    const monthBlocks = table.querySelectorAll(`.month-block[data-year="${year}"]`);
    const entryLines = table.querySelectorAll(`.entry-line[data-year="${year}"]`);
    const monthTotals = table.querySelectorAll(`.mtd-totals-line[data-year="${year}"]`);
    
    // Determine if we're collapsing or expanding based on first month block
    const isCollapsed = monthBlocks[0] && monthBlocks[0].style.display === 'none';
    
    // Toggle visibility
    monthBlocks.forEach(row => {
        row.style.display = isCollapsed ? '' : 'none';
    });
    
    entryLines.forEach(row => {
        row.style.display = isCollapsed ? '' : 'none';
    });
    
    monthTotals.forEach(row => {
        row.style.display = isCollapsed ? '' : 'none';
    });
}

function toggleMonth(year, month, table) {
    // Find all entry lines for this specific month within this specific table
    const entryLines = table.querySelectorAll(`.entry-line[data-year="${year}"][data-month="${month}"]`);
    
    // Determine if we're collapsing or expanding based on first entry
    const isCollapsed = entryLines[0] && entryLines[0].style.display === 'none';
    
    // Toggle visibility
    entryLines.forEach(row => {
        row.style.display = isCollapsed ? '' : 'none';
    });
}
