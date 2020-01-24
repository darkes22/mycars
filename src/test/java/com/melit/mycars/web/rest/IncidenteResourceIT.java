package com.melit.mycars.web.rest;

import com.melit.mycars.MycarsApp;
import com.melit.mycars.domain.Incidente;
import com.melit.mycars.repository.IncidenteRepository;
import com.melit.mycars.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.melit.mycars.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link IncidenteResource} REST controller.
 */
@SpringBootTest(classes = MycarsApp.class)
public class IncidenteResourceIT {

    private static final String DEFAULT_FALLO = "AAAAAAAAAA";
    private static final String UPDATED_FALLO = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private IncidenteRepository incidenteRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restIncidenteMockMvc;

    private Incidente incidente;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IncidenteResource incidenteResource = new IncidenteResource(incidenteRepository);
        this.restIncidenteMockMvc = MockMvcBuilders.standaloneSetup(incidenteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Incidente createEntity(EntityManager em) {
        Incidente incidente = new Incidente()
            .fallo(DEFAULT_FALLO)
            .descripcion(DEFAULT_DESCRIPCION);
        return incidente;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Incidente createUpdatedEntity(EntityManager em) {
        Incidente incidente = new Incidente()
            .fallo(UPDATED_FALLO)
            .descripcion(UPDATED_DESCRIPCION);
        return incidente;
    }

    @BeforeEach
    public void initTest() {
        incidente = createEntity(em);
    }

    @Test
    @Transactional
    public void createIncidente() throws Exception {
        int databaseSizeBeforeCreate = incidenteRepository.findAll().size();

        // Create the Incidente
        restIncidenteMockMvc.perform(post("/api/incidentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(incidente)))
            .andExpect(status().isCreated());

        // Validate the Incidente in the database
        List<Incidente> incidenteList = incidenteRepository.findAll();
        assertThat(incidenteList).hasSize(databaseSizeBeforeCreate + 1);
        Incidente testIncidente = incidenteList.get(incidenteList.size() - 1);
        assertThat(testIncidente.getFallo()).isEqualTo(DEFAULT_FALLO);
        assertThat(testIncidente.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createIncidenteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = incidenteRepository.findAll().size();

        // Create the Incidente with an existing ID
        incidente.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIncidenteMockMvc.perform(post("/api/incidentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(incidente)))
            .andExpect(status().isBadRequest());

        // Validate the Incidente in the database
        List<Incidente> incidenteList = incidenteRepository.findAll();
        assertThat(incidenteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllIncidentes() throws Exception {
        // Initialize the database
        incidenteRepository.saveAndFlush(incidente);

        // Get all the incidenteList
        restIncidenteMockMvc.perform(get("/api/incidentes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(incidente.getId().intValue())))
            .andExpect(jsonPath("$.[*].fallo").value(hasItem(DEFAULT_FALLO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }
    
    @Test
    @Transactional
    public void getIncidente() throws Exception {
        // Initialize the database
        incidenteRepository.saveAndFlush(incidente);

        // Get the incidente
        restIncidenteMockMvc.perform(get("/api/incidentes/{id}", incidente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(incidente.getId().intValue()))
            .andExpect(jsonPath("$.fallo").value(DEFAULT_FALLO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }

    @Test
    @Transactional
    public void getNonExistingIncidente() throws Exception {
        // Get the incidente
        restIncidenteMockMvc.perform(get("/api/incidentes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIncidente() throws Exception {
        // Initialize the database
        incidenteRepository.saveAndFlush(incidente);

        int databaseSizeBeforeUpdate = incidenteRepository.findAll().size();

        // Update the incidente
        Incidente updatedIncidente = incidenteRepository.findById(incidente.getId()).get();
        // Disconnect from session so that the updates on updatedIncidente are not directly saved in db
        em.detach(updatedIncidente);
        updatedIncidente
            .fallo(UPDATED_FALLO)
            .descripcion(UPDATED_DESCRIPCION);

        restIncidenteMockMvc.perform(put("/api/incidentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedIncidente)))
            .andExpect(status().isOk());

        // Validate the Incidente in the database
        List<Incidente> incidenteList = incidenteRepository.findAll();
        assertThat(incidenteList).hasSize(databaseSizeBeforeUpdate);
        Incidente testIncidente = incidenteList.get(incidenteList.size() - 1);
        assertThat(testIncidente.getFallo()).isEqualTo(UPDATED_FALLO);
        assertThat(testIncidente.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingIncidente() throws Exception {
        int databaseSizeBeforeUpdate = incidenteRepository.findAll().size();

        // Create the Incidente

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIncidenteMockMvc.perform(put("/api/incidentes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(incidente)))
            .andExpect(status().isBadRequest());

        // Validate the Incidente in the database
        List<Incidente> incidenteList = incidenteRepository.findAll();
        assertThat(incidenteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteIncidente() throws Exception {
        // Initialize the database
        incidenteRepository.saveAndFlush(incidente);

        int databaseSizeBeforeDelete = incidenteRepository.findAll().size();

        // Delete the incidente
        restIncidenteMockMvc.perform(delete("/api/incidentes/{id}", incidente.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Incidente> incidenteList = incidenteRepository.findAll();
        assertThat(incidenteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
