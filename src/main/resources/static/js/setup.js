var index = 0;
var pages;
var totalPages;

function showPage(pageIndex) {
    pages.forEach((page, i) => {
        page.classList.toggle('active', i === pageIndex);
    });
    
    // Update button states
    document.getElementById('prevButton').disabled = (pageIndex === 0);
    document.getElementById('prevButton').hidden = (pageIndex === 0);
    document.getElementById('nextButton').value = (pageIndex === totalPages - 1) ? 'Finish' : 'Next';
}

function navigatePrev() {
    if (index > 0) {
        index--;
        showPage(index);
    }
}

function navigateNext()
{
    if (index < totalPages - 1)
    {
        index++;
        showPage(index);
    }
    else
    {
        // Submit form on last page
        document.getElementById('businessEntry').submit();
    }
}

// Initialize on page load
document.addEventListener('DOMContentLoaded', function()
{
    pages = document.querySelectorAll('.setup-page');
    totalPages = pages.length;
    showPage(index);
    document.getElementById('prevButton').onclick = navigatePrev;
    document.getElementById('nextButton').onclick = navigateNext;
});