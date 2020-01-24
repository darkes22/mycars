package com.melit.mycars.web.rest;

import com.melit.mycars.MycarsApp;
import com.melit.mycars.domain.Propietario;
import com.melit.mycars.repository.PropietarioRepository;
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
 * Integration tests for the {@link PropietarioResource} REST controller.
 */
@SpringBootTest(classes = MycarsApp.class)
public class PropietarioResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDOS = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDOS = "BBBBBBBBBB";

    private static final String DEFAULT_DNI = "AAAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBBB";

    @Autowired
    private PropietarioRepository propietarioRepository;

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

    private MockMvc restPropietarioMockMvc;

    private Propietario propietario;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PropietarioResource propietarioResource = new PropietarioResource(propietarioRepository);
        this.restPropietarioMockMvc = MockMvcBuilders.standaloneSetup(propietarioResource)
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
    public static Propietario createEntity(EntityManager em) {
        Propietario propietario = new Propietario()
            .nombre(DEFAULT_NOMBRE)
            .apellidos(DEFAULT_APELLIDOS)
            .dni(DEFAULT_DNI);
        return propietario;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Propietario createUpdatedEntity(EntityManager em) {
        Propietario propietario = new Propietario()
            .nombre(UPDATED_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .dni(UPDATED_DNI);
        return propietario;
    }

    @BeforeEach
    public void initTest() {
        propietario = createEntity(em);
    }

    @Test
    @Transactional
    public void createPropietario() throws Exception {
        int databaseSizeBeforeCreate = propietarioRepository.findAll().size();

        // Create the Propietario
        restPropietarioMockMvc.perform(post("/api/propietarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(propietario)))
            .andExpect(status().isCreated());

        // Validate the Propietario in the database
        List<Propietario> propietarioList = propietarioRepository.findAll();
        assertThat(propietarioList).hasSize(databaseSizeBeforeCreate + 1);
        Propietario testPropietario = propietarioList.get(propietarioList.size() - 1);
        assertThat(testPropietario.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testPropietario.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testPropietario.getDni()).isEqualTo(DEFAULT_DNI);
    }

    @Test
    @Transactional
    public void createPropietarioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = propietarioRepository.findAll().size();

        // Create the Propietario with an existing ID
        propietario.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPropietarioMockMvc.perform(post("/api/propietarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(propietario)))
            .andExpect(status().isBadRequest());

        // Validate the Propietario in the database
        List<Propietario> propietarioList = propietarioRepository.findAll();
        assertThat(propietarioList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPropietarios() throws Exception {
        // Initialize the database
        propietarioRepository.saveAndFlush(propietario);

        // Get all the propietarioList
        restPropietarioMockMvc.perform(get("/api/propietarios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(propietario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellidos").value(hasItem(DEFAULT_APELLIDOS)))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)));
    }
    
    @Test
    @Transactional
    public void getPropietario() throws Exception {
        // Initialize the database
        propietarioRepository.saveAndFlush(propietario);

        // Get the propietario
        restPropietarioMockMvc.perform(get("/api/propietarios/{id}", propietario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(propietario.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI));
    }

    @Test
    @Transactional
    public void getNonExistingPropietario() throws Exception {
        // Get the propietario
        restPropietarioMockMvc.perform(get("/api/propietarios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePropietario() throws Exception {
        // Initialize the database
        propietarioRepository.saveAndFlush(propietario);

        int databaseSizeBeforeUpdate = propietarioRepository.findAll().size();

        // Update the propietario
        Propietario updatedPropietario = propietarioRepository.findById(propietario.getId()).get();
        // Disconnect from session so that the updates on updatedPropietario are not directly saved in db
        em.detach(updatedPropietario);
        updatedPropietario
            .nombre(UPDATED_NOMBRE)
            .apellidos(UPDATED_APELLIDOS)
            .dni(UPDATED_DNI);

        restPropietarioMockMvc.perform(put("/api/propietarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPropietario)))
            .andExpect(status().isOk());

        // Validate the Propietario in the database
        List<Propietario> propietarioList = propietarioRepository.findAll();
        assertThat(propietarioList).hasSize(databaseSizeBeforeUpdate);
        Propietario testPropietario = propietarioList.get(propietarioList.size() - 1);
        assertThat(testPropietario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testPropietario.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testPropietario.getDni()).isEqualTo(UPDATED_DNI);
    }

    @Test
    @Transactional
    public void updateNonExistingPropietario() throws Exception {
        int databaseSizeBeforeUpdate = propietarioRepository.findAll().size();

        // Create the Propietario

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPropietarioMockMvc.perform(put("/api/propietarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(propietario)))
            .andExpect(status().isBadRequest());

        // Validate the Propietario in the database
        List<Propietario> propietarioList = propietarioRepository.findAll();
        assertThat(propietarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePropietario() throws Exception {
        // Initialize the database
        propietarioRepository.saveAndFlush(propietario);

        int databaseSizeBeforeDelete = propietarioRepository.findAll().size();

        // Delete the propietario
        restPropietarioMockMvc.perform(delete("/api/propietarios/{id}", propietario.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Propietario> propietarioList = propietarioRepository.findAll();
        assertThat(propietarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
