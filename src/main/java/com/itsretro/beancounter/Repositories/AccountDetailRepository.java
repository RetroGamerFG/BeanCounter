package com.itsretro.beancounter.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itsretro.beancounter.Model.AccountDetail;

public interface AccountDetailRepository extends JpaRepository<AccountDetail, Long> 
{
    
}
