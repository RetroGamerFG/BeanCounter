//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// JournalEntry: a JPA model used to store information on accounting journal entries.
//

package com.itsretro.beancounter.model;

import java.time.LocalDate;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "JournalEntry")
public class JournalEntry 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JournalEntryID")
    private Integer journalEntryID;

    @Column(name = "CreationDate", nullable = false)
    private LocalDate creationDate;

    @NotNull(message = "Post date is required")
    @Column(name = "PostDate", nullable = false)
    private LocalDate postDate;

    @NotBlank(message = "Description is required")
    @Size(max = 64, message = "Description must be under 64 characters")
    @Column(name = "TransactionDescription", length = 64)
    private String transactionDescription;

    @Column(name = "IsEditable", nullable = false)
    private boolean isEditable;

    @NotEmpty(message = "A journal entry must have at least two lines")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "journal_entry_id")
    private List<JournalEntryLine> lines = new ArrayList<>();

    @NotNull(message = "Status is required")
    @Column(name = "Status", nullable = false)
    private String status;

    @Column(name = "PostSignature", nullable = true)
    private String postSignature;

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

    public LocalDate getCreationDate()
    {
        return this.creationDate;
    }

    public void setCreationDate(LocalDate creationDate)
    {
        this.creationDate = creationDate;
    }

    public LocalDate getPostDate()
    {
        return this.postDate;
    }

    public void setPostDate(LocalDate postDate)
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

    public String getStatus()
    {
        return this.status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getPostSignature()
    {
        return this.postSignature;
    }

    public void setPostSignature(String postSignature)
    {
        this.postSignature = postSignature;
    }

    //
    // Additional Methods
    //

    //firstSubmitDefaults() - to ensure a journal entry is submitted for the first time, this method overrides the
    //creationDate, status, and isEditable fields.
    //inputs - none.
    //output - none; updates existing fields.
    public void firstSubmitDefaults()
    {
        this.creationDate = LocalDate.now(); //assign the time at time of save
        this.status = "Open"; //set status to review, then save to database
        this.isEditable = true;
    }

    @SuppressWarnings("")
    public void firstSubmitDefaultsAPI()
    {
        //force removal of journalEntryID and journalEntryLineID's for database compatability
        this.journalEntryID = null;

        for(JournalEntryLine jel : this.lines)
        {
            jel.setJournalEntryLineID(null);
        }

        //assigns post date if one has not been assigned
        if(this.postDate == null)
        {
            this.postDate = LocalDate.now();
        }

        //assigns creationDate, isEditable, and status
        firstSubmitDefaults();
    }
}
