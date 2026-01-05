package com.itsretro.beancounter.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itsretro.beancounter.Model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> 
{
    //List<Account> findByAccountCode(String accountCode);
    //Account findById(long id);
}
