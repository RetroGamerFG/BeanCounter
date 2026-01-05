package com.itsretro.beancounter.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itsretro.beancounter.Model.JournalEntry;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Integer>
{
    
}
