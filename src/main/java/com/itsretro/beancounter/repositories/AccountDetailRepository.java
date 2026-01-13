package com.itsretro.beancounter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itsretro.beancounter.model.AccountDetail;

public interface AccountDetailRepository extends JpaRepository<AccountDetail, Long> 
{
    
}
