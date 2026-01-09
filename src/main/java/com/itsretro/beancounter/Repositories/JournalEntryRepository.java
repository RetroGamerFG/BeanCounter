package com.itsretro.beancounter.Repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itsretro.beancounter.Model.JournalEntry;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Integer>
{
    @Query("SELECT je, jel FROM JournalEntry je " +
           "JOIN je.lines jel " +
           "JOIN jel.account a " +
           "WHERE je.postDate BETWEEN :startDate AND :endDate " +
           "AND je.status = 'Posted' " +
           "AND je.creationDate <= :creationLimit " +
           "AND a.accountCode = :accountCode")
    List<Object[]> findForAccountDetail(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("creationLimit") LocalDate creationLimit,
        @Param("accountCode") BigDecimal accountCode
    );
}
