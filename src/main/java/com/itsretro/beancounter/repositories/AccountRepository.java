//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// AccountRepository: a JPA repository to connect with the 'Account' model.
//

package com.itsretro.beancounter.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itsretro.beancounter.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> 
{
    List<Account> findByAccountCodeGreaterThanEqualAndAccountCodeLessThanEqual(BigDecimal startingAccountCode, BigDecimal endingAccountCode);

    @Query("SELECT account FROM Account account WHERE account.accountCode BETWEEN :startCode AND :endCode")
    List<Account> findByAccountCodeRange(
        @Param("startCode") BigDecimal startCode,
        @Param("endCode") BigDecimal endCode
    );
}
