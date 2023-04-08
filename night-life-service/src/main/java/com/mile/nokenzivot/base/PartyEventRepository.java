package com.mile.nokenzivot.base;

import com.mile.nokenzivot.global.entities.Club;
import com.mile.nokenzivot.global.entities.PartyEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
interface PartyEventRepository extends JpaRepository<PartyEvent, Long> {

  @Query("SELECT e.name as name, e.description as description,"
      + " e.thumbnail as thumbnail, e.club.name as clubname"
      + " FROM PartyEvent e WHERE e.date = ?1 AND e.club = ?2")
  PartyEventDTO findDtoByDateAndClub(Date date, Club club);
  @Query("SELECT e FROM PartyEvent e WHERE e.date =?1 AND e.club = ?2")
  Optional<PartyEvent> findByDateAndClub(Date date, Club club);

  @Query("SELECT e.name as name, e.description as description,"
      + " e.thumbnail as thumbnail, e.club.name as clubname"
      + " FROM PartyEvent e WHERE e.date = ?1")
  Set<PartyEventDTO> findAllByDate(String date);
}
