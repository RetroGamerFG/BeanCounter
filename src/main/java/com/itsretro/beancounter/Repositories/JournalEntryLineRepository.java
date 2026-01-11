//
// BeanCounter
// Copyright (c) 2026 Bailey Manczko
//
// JournalEntryLineRepository: a JPA repository to connect with the 'JournalEntryLine' model.
//

package com.itsretro.beancounter.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itsretro.beancounter.Model.JournalEntryLine;

public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Integer>
{

}
