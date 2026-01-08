package com.itsretro.beancounter.Model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "AccountDetail")
public class AccountDetail
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountDetailID")
    private Long accountDetailID;

    @Column(name = "StartingAccountID", nullable = false)
    private Integer startingAccountID;

    @Column(name = "EndingAccountID", nullable = false)
    private Integer endingAccountID;

    @Column(name = "StartingDate", nullable = false)
    private LocalDate startingDate;

    @Column(name = "EndingDate", nullable = false)
    private LocalDate endingDate;

    //
    // Getter & Setter Methods
    //

    public Long getAccountDetailID()
    {
        return this.accountDetailID;
    }

    public void setAccountDetailID(Long accountDetailID)
    {
        this.accountDetailID = accountDetailID;
    }

    public Integer getStartingAccountID()
    {
        return this.startingAccountID;
    }

    public void setStartingAccountID(Integer startingAccountID)
    {
        this.startingAccountID = startingAccountID;
    }

    public Integer getEndingAccountID()
    {
        return this.endingAccountID;
    }

    public void setEndingAccountID(Integer endingAccountID)
    {
        this.endingAccountID = endingAccountID;
    }

    public LocalDate getStartingDate()
    {
        return this.startingDate;
    }

    public void setStartingDate(LocalDate startingDate)
    {
        this.startingDate = startingDate;
    }

    public LocalDate getEndingDate()
    {
        return this.endingDate;
    }

    public void setEndingDate(LocalDate endingDate)
    {
        endingDate = this.endingDate;
    }
}
