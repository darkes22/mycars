package com.melit.mycars.repository;

import com.melit.mycars.domain.Incidente;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Incidente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IncidenteRepository extends JpaRepository<Incidente, Long> {

}
