//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// JournalEntryLineRepository: a JPA repository to connect with the 'JournalEntryLine' model.
//

package com.itsretro.beancounter.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itsretro.beancounter.model.FinancialStatementLine;
import com.itsretro.beancounter.model.JournalEntryLine;

public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Integer>
{
    @Query("""
        SELECT new com.itsretro.beancounter.model.FinancialStatementLine(
            a.accountName,
            SUM(COALESCE(l.debitAmount, 0)), 
            SUM(COALESCE(l.creditAmount, 0))
        )
        FROM JournalEntry j
        JOIN j.lines l
        JOIN l.account a
        WHERE j.postDate BETWEEN :startDate AND :endDate
        AND j.creationDate <= :cutoffDate
        AND j.status = 'Posted'
        AND a.accountType = :accountType
        GROUP BY a.accountID, a.accountName
    """)
    List<FinancialStatementLine> getAccountsForFinancialStatement(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate, 
        @Param("cutoffDate") LocalDate cutoffDate,
        @Param("accountType") String accountType
    );

    List<JournalEntryLine> findByAccount_AccountID(Integer accountID);
}

/*
For external testing:

SELECT 
    a.account_name,
    SUM(COALESCE(l.debit_amount, 0)) AS total_debit, 
    SUM(COALESCE(l.credit_amount, 0)) AS total_credit
FROM journal_entry j
JOIN journal_entry_line l ON j.journal_entryid = l.journal_entry_id
JOIN account a ON l.accountid = a.ACCOUNTID
WHERE j.post_date BETWEEN '2025-01-01' AND '2026-12-31'
    AND j.creation_date <= '2026-02-13'
    AND j.status = 'Posted'
    AND a.ACCOUNT_TYPE = 'A'
GROUP BY a.ACCOUNTID, a.account_name
ORDER BY a.account_name;

*/