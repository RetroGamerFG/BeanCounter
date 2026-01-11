//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// Account: a JPA model used to store information for accounts utilized by journal entries.
//

package com.itsretro.beancounter.Model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Account")
public class Account 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountID")
    private Integer accountID;

    @Column(name = "AccountCode", precision = 7, scale = 3, nullable = false)
    private BigDecimal accountCode;

    @Column(name = "AccountName", length = 32, nullable = false)
    private String accountName;

    @Column(name = "AccountDescription", length = 64)
    private String accountDescription;

    @Column(name = "AccountType", length = 1, nullable = false)
    private String accountType;

    @Column(name = "IsContra")
    private boolean isContra;

    //
    // Getter & Setter Methods
    //

    public Integer getAccountID()
    {
        return this.accountID;
    }

    public void setAccountID(Integer accountID)
    {
        this.accountID = accountID;
    }

    public BigDecimal getAccountCode()
    {
        return this.accountCode;
    }

    public void setAccountCode(BigDecimal accountCode)
    {
        this.accountCode = accountCode;
    }

    public String getAccountName()
    {
        return this.accountName;
    }

    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

    public String getAccountDescription()
    {
        return this.accountDescription;
    }

    public void setAccountDescription(String accountDescription)
    {
        this.accountDescription = accountDescription;
    }

    public String getAccountType()
    {
        return this.accountType;
    }

    public void setAccountType(String accountType)
    {
        this.accountType = accountType;
    }

    public boolean getIsContra()
    {
        return this.isContra;
    }

    public void setIsContra(boolean isContra)
    {
        this.isContra = isContra;
    }
}
