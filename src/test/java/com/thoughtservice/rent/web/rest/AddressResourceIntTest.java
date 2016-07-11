package com.thoughtservice.rent.web.rest;

import com.thoughtservice.rent.RentApp;
import com.thoughtservice.rent.domain.Address;
import com.thoughtservice.rent.repository.AddressRepository;
import com.thoughtservice.rent.repository.search.AddressSearchRepository;

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


/**
 * Test class for the AddressResource REST controller.
 *
 * @see AddressResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RentApp.class)
@WebAppConfiguration
@IntegrationTest
public class AddressResourceIntTest {


    private static final Long DEFAULT_ADDRESS_ID = 1L;
    private static final Long UPDATED_ADDRESS_ID = 2L;
    private static final String DEFAULT_STREET_NAME = "AAAAA";
    private static final String UPDATED_STREET_NAME = "BBBBB";

    private static final Long DEFAULT_ZIP_CODE = 1L;
    private static final Long UPDATED_ZIP_CODE = 2L;
    private static final String DEFAULT_STATE = "AAAAA";
    private static final String UPDATED_STATE = "BBBBB";

    @Inject
    private AddressRepository addressRepository;

    @Inject
    private AddressSearchRepository addressSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAddressMockMvc;

    private Address address;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AddressResource addressResource = new AddressResource();
        ReflectionTestUtils.setField(addressResource, "addressSearchRepository", addressSearchRepository);
        ReflectionTestUtils.setField(addressResource, "addressRepository", addressRepository);
        this.restAddressMockMvc = MockMvcBuilders.standaloneSetup(addressResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        addressSearchRepository.deleteAll();
        address = new Address();
        address.setAddressId(DEFAULT_ADDRESS_ID);
        address.setStreetName(DEFAULT_STREET_NAME);
        address.setZipCode(DEFAULT_ZIP_CODE);
        address.setState(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void createAddress() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // Create the Address

        restAddressMockMvc.perform(post("/api/addresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(address)))
                .andExpect(status().isCreated());

        // Validate the Address in the database
        List<Address> addresses = addressRepository.findAll();
        assertThat(addresses).hasSize(databaseSizeBeforeCreate + 1);
        Address testAddress = addresses.get(addresses.size() - 1);
        assertThat(testAddress.getAddressId()).isEqualTo(DEFAULT_ADDRESS_ID);
        assertThat(testAddress.getStreetName()).isEqualTo(DEFAULT_STREET_NAME);
        assertThat(testAddress.getZipCode()).isEqualTo(DEFAULT_ZIP_CODE);
        assertThat(testAddress.getState()).isEqualTo(DEFAULT_STATE);

        // Validate the Address in ElasticSearch
        Address addressEs = addressSearchRepository.findOne(testAddress.getId());
        assertThat(addressEs).isEqualToComparingFieldByField(testAddress);
    }

    @Test
    @Transactional
    public void checkStreetNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setStreetName(null);

        // Create the Address, which fails.

        restAddressMockMvc.perform(post("/api/addresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(address)))
                .andExpect(status().isBadRequest());

        List<Address> addresses = addressRepository.findAll();
        assertThat(addresses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkZipCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setZipCode(null);

        // Create the Address, which fails.

        restAddressMockMvc.perform(post("/api/addresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(address)))
                .andExpect(status().isBadRequest());

        List<Address> addresses = addressRepository.findAll();
        assertThat(addresses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setState(null);

        // Create the Address, which fails.

        restAddressMockMvc.perform(post("/api/addresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(address)))
                .andExpect(status().isBadRequest());

        List<Address> addresses = addressRepository.findAll();
        assertThat(addresses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAddresses() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addresses
        restAddressMockMvc.perform(get("/api/addresses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
                .andExpect(jsonPath("$.[*].addressId").value(hasItem(DEFAULT_ADDRESS_ID.intValue())))
                .andExpect(jsonPath("$.[*].streetName").value(hasItem(DEFAULT_STREET_NAME.toString())))
                .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.intValue())))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    public void getAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get the address
        restAddressMockMvc.perform(get("/api/addresses/{id}", address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(address.getId().intValue()))
            .andExpect(jsonPath("$.addressId").value(DEFAULT_ADDRESS_ID.intValue()))
            .andExpect(jsonPath("$.streetName").value(DEFAULT_STREET_NAME.toString()))
            .andExpect(jsonPath("$.zipCode").value(DEFAULT_ZIP_CODE.intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAddress() throws Exception {
        // Get the address
        restAddressMockMvc.perform(get("/api/addresses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);
        addressSearchRepository.save(address);
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address
        Address updatedAddress = new Address();
        updatedAddress.setId(address.getId());
        updatedAddress.setAddressId(UPDATED_ADDRESS_ID);
        updatedAddress.setStreetName(UPDATED_STREET_NAME);
        updatedAddress.setZipCode(UPDATED_ZIP_CODE);
        updatedAddress.setState(UPDATED_STATE);

        restAddressMockMvc.perform(put("/api/addresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAddress)))
                .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addresses = addressRepository.findAll();
        assertThat(addresses).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addresses.get(addresses.size() - 1);
        assertThat(testAddress.getAddressId()).isEqualTo(UPDATED_ADDRESS_ID);
        assertThat(testAddress.getStreetName()).isEqualTo(UPDATED_STREET_NAME);
        assertThat(testAddress.getZipCode()).isEqualTo(UPDATED_ZIP_CODE);
        assertThat(testAddress.getState()).isEqualTo(UPDATED_STATE);

        // Validate the Address in ElasticSearch
        Address addressEs = addressSearchRepository.findOne(testAddress.getId());
        assertThat(addressEs).isEqualToComparingFieldByField(testAddress);
    }

    @Test
    @Transactional
    public void deleteAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);
        addressSearchRepository.save(address);
        int databaseSizeBeforeDelete = addressRepository.findAll().size();

        // Get the address
        restAddressMockMvc.perform(delete("/api/addresses/{id}", address.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean addressExistsInEs = addressSearchRepository.exists(address.getId());
        assertThat(addressExistsInEs).isFalse();

        // Validate the database is empty
        List<Address> addresses = addressRepository.findAll();
        assertThat(addresses).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);
        addressSearchRepository.save(address);

        // Search the address
        restAddressMockMvc.perform(get("/api/_search/addresses?query=id:" + address.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(address.getId().intValue())))
            .andExpect(jsonPath("$.[*].addressId").value(hasItem(DEFAULT_ADDRESS_ID.intValue())))
            .andExpect(jsonPath("$.[*].streetName").value(hasItem(DEFAULT_STREET_NAME.toString())))
            .andExpect(jsonPath("$.[*].zipCode").value(hasItem(DEFAULT_ZIP_CODE.intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }
}
