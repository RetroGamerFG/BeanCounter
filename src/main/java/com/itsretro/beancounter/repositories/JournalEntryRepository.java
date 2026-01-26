//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// JournalEntryRepository: a JPA repository to connect with the 'JournalEntry' model.
//

package com.itsretro.beancounter.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itsretro.beancounter.model.JournalEntry;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Integer>
{
    //fetches all journal entry lines with the journal entry data joined by SQL that were:
        //1 - have a post date found within the range
        //2 - have a journal entry with a status of 'posted'
        //3 - were created before the account detail's generated date
        //4 - have a journal entry that matches the account range
    @Query("SELECT je, jel FROM JournalEntry je " +
        "JOIN je.lines jel " +
        "JOIN jel.account a " +
        "WHERE je.postDate BETWEEN :startDate AND :endDate " +
        "AND je.status = 'Posted' " +
        "AND je.creationDate <= :creationLimit " +
        "AND a.accountCode = :accountCode " +
        "ORDER BY je.postDate")
    List<Object[]> findForAccountDetail(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("creationLimit") LocalDate creationLimit,
        @Param("accountCode") BigDecimal accountCode
    );

    //fetches all journal entry lines with the journal entry data joined by SQL that were:
        //1 - have a post date found within the range
        //2 - have a journal entry with a status of 'Posted'
        //3 - were created before the financial statement's generated date
        //4 - include an account of a specified accountType
    @Query("SELECT je, jel FROM JournalEntry je " +
        "JOIN je.lines jel " +
        "JOIN jel.account a " +
        "WHERE je.postDate BETWEEN :startDate AND :endDate " +
        "AND je.status = 'Posted' " +
        "AND je.creationDate <= :creationLimit " +
        "AND a.accountType = :accountType " +
        "ORDER BY je.postDate")
    List<Object[]> findForFinancialStatement(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("creationLimit") LocalDate creationLimit,
        @Param("accountType") String accountType
    );

    List<JournalEntry> findByStatusNot(String status);
}
