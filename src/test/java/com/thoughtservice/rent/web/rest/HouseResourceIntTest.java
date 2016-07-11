package com.thoughtservice.rent.web.rest;

import com.thoughtservice.rent.RentApp;
import com.thoughtservice.rent.domain.House;
import com.thoughtservice.rent.repository.HouseRepository;
import com.thoughtservice.rent.repository.search.HouseSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.thoughtservice.rent.domain.enumeration.GENDERFOR;

/**
 * Test class for the HouseResource REST controller.
 *
 * @see HouseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RentApp.class)
@WebAppConfiguration
@IntegrationTest
public class HouseResourceIntTest {

    private static final String DEFAULT_HOUSE_ID = "AAAAA";
    private static final String UPDATED_HOUSE_ID = "BBBBB";
    private static final String DEFAULT_HOUSE_TYPE = "AAAAA";
    private static final String UPDATED_HOUSE_TYPE = "BBBBB";

    private static final GENDERFOR DEFAULT_HOUSE_FOR = GENDERFOR.BOYS;
    private static final GENDERFOR UPDATED_HOUSE_FOR = GENDERFOR.GIRLS;

    private static final Long DEFAULT_NO_OF_ROOMS = 1L;
    private static final Long UPDATED_NO_OF_ROOMS = 2L;
    private static final String DEFAULT_FOOD_PREFERENCE = "AAAAA";
    private static final String UPDATED_FOOD_PREFERENCE = "BBBBB";
    private static final String DEFAULT_RENT_TO_BE_PAID = "AAAAA";
    private static final String UPDATED_RENT_TO_BE_PAID = "BBBBB";
    private static final String DEFAULT_RULES = "AAAAA";
    private static final String UPDATED_RULES = "BBBBB";

    @Inject
    private HouseRepository houseRepository;

    @Inject
    private HouseSearchRepository houseSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHouseMockMvc;

    private House house;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HouseResource houseResource = new HouseResource();
        ReflectionTestUtils.setField(houseResource, "houseSearchRepository", houseSearchRepository);
        ReflectionTestUtils.setField(houseResource, "houseRepository", houseRepository);
        this.restHouseMockMvc = MockMvcBuilders.standaloneSetup(houseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        houseSearchRepository.deleteAll();
        house = new House();
        house.setHouseId(DEFAULT_HOUSE_ID);
        house.setHouseType(DEFAULT_HOUSE_TYPE);
        house.setHouseFor(DEFAULT_HOUSE_FOR);
        house.setNoOfRooms(DEFAULT_NO_OF_ROOMS);
        house.setFoodPreference(DEFAULT_FOOD_PREFERENCE);
        house.setRentToBePaid(DEFAULT_RENT_TO_BE_PAID);
        house.setRules(DEFAULT_RULES);
    }

    @Test
    @Transactional
    public void createHouse() throws Exception {
        int databaseSizeBeforeCreate = houseRepository.findAll().size();

        // Create the House

        restHouseMockMvc.perform(post("/api/houses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(house)))
                .andExpect(status().isCreated());

        // Validate the House in the database
        List<House> houses = houseRepository.findAll();
        assertThat(houses).hasSize(databaseSizeBeforeCreate + 1);
        House testHouse = houses.get(houses.size() - 1);
        assertThat(testHouse.getHouseId()).isEqualTo(DEFAULT_HOUSE_ID);
        assertThat(testHouse.getHouseType()).isEqualTo(DEFAULT_HOUSE_TYPE);
        assertThat(testHouse.getHouseFor()).isEqualTo(DEFAULT_HOUSE_FOR);
        assertThat(testHouse.getNoOfRooms()).isEqualTo(DEFAULT_NO_OF_ROOMS);
        assertThat(testHouse.getFoodPreference()).isEqualTo(DEFAULT_FOOD_PREFERENCE);
        assertThat(testHouse.getRentToBePaid()).isEqualTo(DEFAULT_RENT_TO_BE_PAID);
        assertThat(testHouse.getRules()).isEqualTo(DEFAULT_RULES);

        // Validate the House in ElasticSearch
        House houseEs = houseSearchRepository.findOne(testHouse.getId());
        assertThat(houseEs).isEqualToComparingFieldByField(testHouse);
    }

    @Test
    @Transactional
    public void checkHouseTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = houseRepository.findAll().size();
        // set the field null
        house.setHouseType(null);

        // Create the House, which fails.

        restHouseMockMvc.perform(post("/api/houses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(house)))
                .andExpect(status().isBadRequest());

        List<House> houses = houseRepository.findAll();
        assertThat(houses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHouseForIsRequired() throws Exception {
        int databaseSizeBeforeTest = houseRepository.findAll().size();
        // set the field null
        house.setHouseFor(null);

        // Create the House, which fails.

        restHouseMockMvc.perform(post("/api/houses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(house)))
                .andExpect(status().isBadRequest());

        List<House> houses = houseRepository.findAll();
        assertThat(houses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNoOfRoomsIsRequired() throws Exception {
        int databaseSizeBeforeTest = houseRepository.findAll().size();
        // set the field null
        house.setNoOfRooms(null);

        // Create the House, which fails.

        restHouseMockMvc.perform(post("/api/houses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(house)))
                .andExpect(status().isBadRequest());

        List<House> houses = houseRepository.findAll();
        assertThat(houses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRentToBePaidIsRequired() throws Exception {
        int databaseSizeBeforeTest = houseRepository.findAll().size();
        // set the field null
        house.setRentToBePaid(null);

        // Create the House, which fails.

        restHouseMockMvc.perform(post("/api/houses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(house)))
                .andExpect(status().isBadRequest());

        List<House> houses = houseRepository.findAll();
        assertThat(houses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHouses() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get all the houses
        restHouseMockMvc.perform(get("/api/houses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(house.getId().intValue())))
                .andExpect(jsonPath("$.[*].houseId").value(hasItem(DEFAULT_HOUSE_ID.toString())))
                .andExpect(jsonPath("$.[*].houseType").value(hasItem(DEFAULT_HOUSE_TYPE.toString())))
                .andExpect(jsonPath("$.[*].houseFor").value(hasItem(DEFAULT_HOUSE_FOR.toString())))
                .andExpect(jsonPath("$.[*].noOfRooms").value(hasItem(DEFAULT_NO_OF_ROOMS.intValue())))
                .andExpect(jsonPath("$.[*].foodPreference").value(hasItem(DEFAULT_FOOD_PREFERENCE.toString())))
                .andExpect(jsonPath("$.[*].rentToBePaid").value(hasItem(DEFAULT_RENT_TO_BE_PAID.toString())))
                .andExpect(jsonPath("$.[*].rules").value(hasItem(DEFAULT_RULES.toString())));
    }

    @Test
    @Transactional
    public void getHouse() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);

        // Get the house
        restHouseMockMvc.perform(get("/api/houses/{id}", house.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(house.getId().intValue()))
            .andExpect(jsonPath("$.houseId").value(DEFAULT_HOUSE_ID.toString()))
            .andExpect(jsonPath("$.houseType").value(DEFAULT_HOUSE_TYPE.toString()))
            .andExpect(jsonPath("$.houseFor").value(DEFAULT_HOUSE_FOR.toString()))
            .andExpect(jsonPath("$.noOfRooms").value(DEFAULT_NO_OF_ROOMS.intValue()))
            .andExpect(jsonPath("$.foodPreference").value(DEFAULT_FOOD_PREFERENCE.toString()))
            .andExpect(jsonPath("$.rentToBePaid").value(DEFAULT_RENT_TO_BE_PAID.toString()))
            .andExpect(jsonPath("$.rules").value(DEFAULT_RULES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHouse() throws Exception {
        // Get the house
        restHouseMockMvc.perform(get("/api/houses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHouse() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);
        houseSearchRepository.save(house);
        int databaseSizeBeforeUpdate = houseRepository.findAll().size();

        // Update the house
        House updatedHouse = new House();
        updatedHouse.setId(house.getId());
        updatedHouse.setHouseId(UPDATED_HOUSE_ID);
        updatedHouse.setHouseType(UPDATED_HOUSE_TYPE);
        updatedHouse.setHouseFor(UPDATED_HOUSE_FOR);
        updatedHouse.setNoOfRooms(UPDATED_NO_OF_ROOMS);
        updatedHouse.setFoodPreference(UPDATED_FOOD_PREFERENCE);
        updatedHouse.setRentToBePaid(UPDATED_RENT_TO_BE_PAID);
        updatedHouse.setRules(UPDATED_RULES);

        restHouseMockMvc.perform(put("/api/houses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedHouse)))
                .andExpect(status().isOk());

        // Validate the House in the database
        List<House> houses = houseRepository.findAll();
        assertThat(houses).hasSize(databaseSizeBeforeUpdate);
        House testHouse = houses.get(houses.size() - 1);
        assertThat(testHouse.getHouseId()).isEqualTo(UPDATED_HOUSE_ID);
        assertThat(testHouse.getHouseType()).isEqualTo(UPDATED_HOUSE_TYPE);
        assertThat(testHouse.getHouseFor()).isEqualTo(UPDATED_HOUSE_FOR);
        assertThat(testHouse.getNoOfRooms()).isEqualTo(UPDATED_NO_OF_ROOMS);
        assertThat(testHouse.getFoodPreference()).isEqualTo(UPDATED_FOOD_PREFERENCE);
        assertThat(testHouse.getRentToBePaid()).isEqualTo(UPDATED_RENT_TO_BE_PAID);
        assertThat(testHouse.getRules()).isEqualTo(UPDATED_RULES);

        // Validate the House in ElasticSearch
        House houseEs = houseSearchRepository.findOne(testHouse.getId());
        assertThat(houseEs).isEqualToComparingFieldByField(testHouse);
    }

    @Test
    @Transactional
    public void deleteHouse() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);
        houseSearchRepository.save(house);
        int databaseSizeBeforeDelete = houseRepository.findAll().size();

        // Get the house
        restHouseMockMvc.perform(delete("/api/houses/{id}", house.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean houseExistsInEs = houseSearchRepository.exists(house.getId());
        assertThat(houseExistsInEs).isFalse();

        // Validate the database is empty
        List<House> houses = houseRepository.findAll();
        assertThat(houses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchHouse() throws Exception {
        // Initialize the database
        houseRepository.saveAndFlush(house);
        houseSearchRepository.save(house);

        // Search the house
        restHouseMockMvc.perform(get("/api/_search/houses?query=id:" + house.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(house.getId().intValue())))
            .andExpect(jsonPath("$.[*].houseId").value(hasItem(DEFAULT_HOUSE_ID.toString())))
            .andExpect(jsonPath("$.[*].houseType").value(hasItem(DEFAULT_HOUSE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].houseFor").value(hasItem(DEFAULT_HOUSE_FOR.toString())))
            .andExpect(jsonPath("$.[*].noOfRooms").value(hasItem(DEFAULT_NO_OF_ROOMS.intValue())))
            .andExpect(jsonPath("$.[*].foodPreference").value(hasItem(DEFAULT_FOOD_PREFERENCE.toString())))
            .andExpect(jsonPath("$.[*].rentToBePaid").value(hasItem(DEFAULT_RENT_TO_BE_PAID.toString())))
            .andExpect(jsonPath("$.[*].rules").value(hasItem(DEFAULT_RULES.toString())));
    }
}
