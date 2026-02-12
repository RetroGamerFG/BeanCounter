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
