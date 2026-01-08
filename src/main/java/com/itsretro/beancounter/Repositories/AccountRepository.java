package com.itsretro.beancounter.Repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itsretro.beancounter.Model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> 
{
    List<Account> findByAccountCodeGreaterThanEqualAndAccountCodeLessThanEqual(BigDecimal startingAccountCode, BigDecimal endingAccountCode);

    @Query("SELECT account FROM Account account WHERE account.accountCode BETWEEN :startCode AND :endCode")
    List<Account> findByAccountCodeRange(
        @Param("startCode") BigDecimal startCode,
        @Param("endCode") BigDecimal endCode
    );
}
