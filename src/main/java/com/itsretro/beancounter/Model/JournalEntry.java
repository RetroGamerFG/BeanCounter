package com.itsretro.beancounter.Model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "JournalEntry")
public class JournalEntry 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JournalEntryID")
    private Integer journalEntryID;

    @Column(name = "CreationDate", nullable = false)
    private Timestamp creationDate;// = Timestamp.from(Instant.now());

    @Column(name = "PostDate")
    private Timestamp postDate;

    @Column(name = "TransactionDescription", length = 64)
    private String transactionDescription;

    @Column(name = "IsEditable", nullable = false)
    private boolean isEditable;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "journal_entry_id")
    private List<JournalEntryLine> lines = new ArrayList<>();

    //
    // Getter & Setter Methods
    //

    public Integer getJournalEntryID()
    {
        return this.journalEntryID;
    }

    public void setJournalEntryID(Integer journalEntryID)
    {
        this.journalEntryID = journalEntryID;
    }

    public Timestamp getCreationDate()
    {
        return this.creationDate;
    }

    public void setCreationDate(Timestamp creationDate)
    {
        this.creationDate = creationDate;
    }

    public Timestamp getPostDate()
    {
        return this.postDate;
    }

    public void setPostDate(Timestamp postDate)
    {
        this.postDate = postDate;
    }

    public String getTransactionDescription()
    {
        return this.transactionDescription;
    }

    public void setTransactionDescription(String transactionDescription)
    {
        this.transactionDescription = transactionDescription;
    }

    public boolean getIsEditable()
    {
        return this.isEditable;
    }

    public void setIsEditable(boolean isEditable)
    {
        this.isEditable = isEditable;
    }

    public List<JournalEntryLine> getLines()
    {
        return this.lines;
    }

    public void setLines(List<JournalEntryLine> lines)
    {
        this.lines = lines;
    }
}
