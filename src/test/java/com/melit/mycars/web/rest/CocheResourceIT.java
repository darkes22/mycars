package com.melit.mycars.web.rest;

import com.melit.mycars.MycarsApp;
import com.melit.mycars.domain.Coche;
import com.melit.mycars.repository.CocheRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.melit.mycars.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CocheResource} REST controller.
 */
@SpringBootTest(classes = MycarsApp.class)
public class CocheResourceIT {

    private static final String DEFAULT_MARCA = "AAAAAAAAAA";
    private static final String UPDATED_MARCA = "BBBBBBBBBB";

    private static final String DEFAULT_MODELO = "AAAAAAAAAA";
    private static final String UPDATED_MODELO = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULA = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULA = "BBBBBBBBBB";

    private static final Float DEFAULT_PRECIO = 1F;
    private static final Float UPDATED_PRECIO = 2F;

    private static final Boolean DEFAULT_VENDIDO = false;
    private static final Boolean UPDATED_VENDIDO = true;

    private static final LocalDate DEFAULT_FECHAVENTA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHAVENTA = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private CocheRepository cocheRepository;

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

    private MockMvc restCocheMockMvc;

    private Coche coche;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CocheResource cocheResource = new CocheResource(cocheRepository);
        this.restCocheMockMvc = MockMvcBuilders.standaloneSetup(cocheResource)
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
    public static Coche createEntity(EntityManager em) {
        Coche coche = new Coche()
            .marca(DEFAULT_MARCA)
            .modelo(DEFAULT_MODELO)
            .matricula(DEFAULT_MATRICULA)
            .precio(DEFAULT_PRECIO)
            .vendido(DEFAULT_VENDIDO)
            .fechaventa(DEFAULT_FECHAVENTA);
        return coche;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coche createUpdatedEntity(EntityManager em) {
        Coche coche = new Coche()
            .marca(UPDATED_MARCA)
            .modelo(UPDATED_MODELO)
            .matricula(UPDATED_MATRICULA)
            .precio(UPDATED_PRECIO)
            .vendido(UPDATED_VENDIDO)
            .fechaventa(UPDATED_FECHAVENTA);
        return coche;
    }

    @BeforeEach
    public void initTest() {
        coche = createEntity(em);
    }

    @Test
    @Transactional
    public void createCoche() throws Exception {
        int databaseSizeBeforeCreate = cocheRepository.findAll().size();

        // Create the Coche
        restCocheMockMvc.perform(post("/api/coches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coche)))
            .andExpect(status().isCreated());

        // Validate the Coche in the database
        List<Coche> cocheList = cocheRepository.findAll();
        assertThat(cocheList).hasSize(databaseSizeBeforeCreate + 1);
        Coche testCoche = cocheList.get(cocheList.size() - 1);
        assertThat(testCoche.getMarca()).isEqualTo(DEFAULT_MARCA);
        assertThat(testCoche.getModelo()).isEqualTo(DEFAULT_MODELO);
        assertThat(testCoche.getMatricula()).isEqualTo(DEFAULT_MATRICULA);
        assertThat(testCoche.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testCoche.isVendido()).isEqualTo(DEFAULT_VENDIDO);
        assertThat(testCoche.getFechaventa()).isEqualTo(DEFAULT_FECHAVENTA);
    }

    @Test
    @Transactional
    public void createCocheWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cocheRepository.findAll().size();

        // Create the Coche with an existing ID
        coche.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCocheMockMvc.perform(post("/api/coches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coche)))
            .andExpect(status().isBadRequest());

        // Validate the Coche in the database
        List<Coche> cocheList = cocheRepository.findAll();
        assertThat(cocheList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCoches() throws Exception {
        // Initialize the database
        cocheRepository.saveAndFlush(coche);

        // Get all the cocheList
        restCocheMockMvc.perform(get("/api/coches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coche.getId().intValue())))
            .andExpect(jsonPath("$.[*].marca").value(hasItem(DEFAULT_MARCA)))
            .andExpect(jsonPath("$.[*].modelo").value(hasItem(DEFAULT_MODELO)))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())))
            .andExpect(jsonPath("$.[*].vendido").value(hasItem(DEFAULT_VENDIDO.booleanValue())))
            .andExpect(jsonPath("$.[*].fechaventa").value(hasItem(DEFAULT_FECHAVENTA.toString())));
    }
    
    @Test
    @Transactional
    public void getCoche() throws Exception {
        // Initialize the database
        cocheRepository.saveAndFlush(coche);

        // Get the coche
        restCocheMockMvc.perform(get("/api/coches/{id}", coche.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coche.getId().intValue()))
            .andExpect(jsonPath("$.marca").value(DEFAULT_MARCA))
            .andExpect(jsonPath("$.modelo").value(DEFAULT_MODELO))
            .andExpect(jsonPath("$.matricula").value(DEFAULT_MATRICULA))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.doubleValue()))
            .andExpect(jsonPath("$.vendido").value(DEFAULT_VENDIDO.booleanValue()))
            .andExpect(jsonPath("$.fechaventa").value(DEFAULT_FECHAVENTA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCoche() throws Exception {
        // Get the coche
        restCocheMockMvc.perform(get("/api/coches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoche() throws Exception {
        // Initialize the database
        cocheRepository.saveAndFlush(coche);

        int databaseSizeBeforeUpdate = cocheRepository.findAll().size();

        // Update the coche
        Coche updatedCoche = cocheRepository.findById(coche.getId()).get();
        // Disconnect from session so that the updates on updatedCoche are not directly saved in db
        em.detach(updatedCoche);
        updatedCoche
            .marca(UPDATED_MARCA)
            .modelo(UPDATED_MODELO)
            .matricula(UPDATED_MATRICULA)
            .precio(UPDATED_PRECIO)
            .vendido(UPDATED_VENDIDO)
            .fechaventa(UPDATED_FECHAVENTA);

        restCocheMockMvc.perform(put("/api/coches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCoche)))
            .andExpect(status().isOk());

        // Validate the Coche in the database
        List<Coche> cocheList = cocheRepository.findAll();
        assertThat(cocheList).hasSize(databaseSizeBeforeUpdate);
        Coche testCoche = cocheList.get(cocheList.size() - 1);
        assertThat(testCoche.getMarca()).isEqualTo(UPDATED_MARCA);
        assertThat(testCoche.getModelo()).isEqualTo(UPDATED_MODELO);
        assertThat(testCoche.getMatricula()).isEqualTo(UPDATED_MATRICULA);
        assertThat(testCoche.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testCoche.isVendido()).isEqualTo(UPDATED_VENDIDO);
        assertThat(testCoche.getFechaventa()).isEqualTo(UPDATED_FECHAVENTA);
    }

    @Test
    @Transactional
    public void updateNonExistingCoche() throws Exception {
        int databaseSizeBeforeUpdate = cocheRepository.findAll().size();

        // Create the Coche

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCocheMockMvc.perform(put("/api/coches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coche)))
            .andExpect(status().isBadRequest());

        // Validate the Coche in the database
        List<Coche> cocheList = cocheRepository.findAll();
        assertThat(cocheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCoche() throws Exception {
        // Initialize the database
        cocheRepository.saveAndFlush(coche);

        int databaseSizeBeforeDelete = cocheRepository.findAll().size();

        // Delete the coche
        restCocheMockMvc.perform(delete("/api/coches/{id}", coche.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Coche> cocheList = cocheRepository.findAll();
        assertThat(cocheList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
