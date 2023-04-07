package com.mile.nokenzivot.base;

import com.mile.nokenzivot.global.entities.PartyEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
interface PartyEventRepository extends JpaRepository<PartyEvent, Long> {
}
