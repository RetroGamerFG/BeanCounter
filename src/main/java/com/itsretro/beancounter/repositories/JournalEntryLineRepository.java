//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// JournalEntryLineRepository: a JPA repository to connect with the 'JournalEntryLine' model.
//

package com.itsretro.beancounter.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itsretro.beancounter.model.JournalEntryLine;

public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Integer>
{
    List<JournalEntryLine> findByAccount_AccountID(Integer accountID);
}
