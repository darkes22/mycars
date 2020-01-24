package com.melit.mycars.web.rest;

import com.melit.mycars.domain.Coche;
import com.melit.mycars.repository.CocheRepository;
import com.melit.mycars.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.melit.mycars.domain.Coche}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CocheResource {

    private final Logger log = LoggerFactory.getLogger(CocheResource.class);

    private static final String ENTITY_NAME = "coche";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CocheRepository cocheRepository;

    public CocheResource(CocheRepository cocheRepository) {
        this.cocheRepository = cocheRepository;
    }

    /**
     * {@code POST  /coches} : Create a new coche.
     *
     * @param coche the coche to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coche, or with status {@code 400 (Bad Request)} if the coche has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/coches")
    public ResponseEntity<Coche> createCoche(@Valid @RequestBody Coche coche) throws URISyntaxException {
        log.debug("REST request to save Coche : {}", coche);
        if (coche.getId() != null) {
            throw new BadRequestAlertException("A new coche cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Coche result = cocheRepository.save(coche);
        return ResponseEntity.created(new URI("/api/coches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /coches} : Updates an existing coche.
     *
     * @param coche the coche to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coche,
     * or with status {@code 400 (Bad Request)} if the coche is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coche couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/coches")
    public ResponseEntity<Coche> updateCoche(@Valid @RequestBody Coche coche) throws URISyntaxException {
        log.debug("REST request to update Coche : {}", coche);
        if (coche.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Coche result = cocheRepository.save(coche);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coche.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /coches} : get all the coches.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coches in body.
     */
    @GetMapping("/coches")
    public List<Coche> getAllCoches() {
        log.debug("REST request to get all Coches");
        return cocheRepository.findAll();
    }

    /**
     * {@code GET  /coches/:id} : get the "id" coche.
     *
     * @param id the id of the coche to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coche, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/coches/{id}")
    public ResponseEntity<Coche> getCoche(@PathVariable Long id) {
        log.debug("REST request to get Coche : {}", id);
        Optional<Coche> coche = cocheRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(coche);
    }

    /**
     * {@code DELETE  /coches/:id} : delete the "id" coche.
     *
     * @param id the id of the coche to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/coches/{id}")
    public ResponseEntity<Void> deleteCoche(@PathVariable Long id) {
        log.debug("REST request to delete Coche : {}", id);
        cocheRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
