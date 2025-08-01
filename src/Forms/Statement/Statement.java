package Forms.Statement;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Statement implements Serializable
{
    private LocalDate startDate;
    private LocalDate endDate;

    private final Date generatedDate;

    private ArrayList<Integer> referencedTransactions;

    public Statement()
    {
        startDate = null;
        endDate = null;

        generatedDate = new Date();

        referencedTransactions = new ArrayList<>();
    }

    public LocalDate getStartDate()
    {
        return startDate;
    }

    public void setStartDate(LocalDate startDate)
    {
        this.startDate = startDate;
    }

    public LocalDate getEndDate()
    {
        return endDate;
    }

    public void setEndDate(LocalDate endDate)
    {
        this.endDate = endDate;
    }

    public Date getGeneratedDate()
    {
        return generatedDate;
    }

    public ArrayList<Integer> getReferencedTransactions()
    {
        return this.referencedTransactions;
    }

//
// Print Functions
//

    public String printStartDate()
    {
        return startDate.toString();
    }

    public String printEndDate()
    {
        return endDate.toString();
    }

    public String printGeneratedDate()
    {
        return generatedDate.toString();
    }

//
// Referenced Transactions Functions
//

    public void insertReferencedTransaction(int reference)
    {
        referencedTransactions.add(reference);
    }

    public void deleteReferencedTransaction(int reference)
    {
        referencedTransactions.remove(reference);
    }

}
