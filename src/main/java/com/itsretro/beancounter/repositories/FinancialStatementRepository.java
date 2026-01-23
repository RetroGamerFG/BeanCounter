package com.itsretro.beancounter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itsretro.beancounter.model.FinancialStatement;

public interface FinancialStatementRepository extends JpaRepository<FinancialStatement, Long>
{
    
}
