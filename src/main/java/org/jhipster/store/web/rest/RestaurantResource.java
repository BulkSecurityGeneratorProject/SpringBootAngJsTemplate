package org.jhipster.store.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.jhipster.store.domain.Restaurant;

import org.jhipster.store.repository.RestaurantRepository;
import org.jhipster.store.repository.search.RestaurantSearchRepository;
import org.jhipster.store.web.rest.errors.BadRequestAlertException;
import org.jhipster.store.web.rest.util.HeaderUtil;
import org.jhipster.store.web.rest.util.PaginationUtil;
import org.jhipster.store.service.dto.RestaurantDTO;
import org.jhipster.store.service.mapper.RestaurantMapper;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Restaurant.
 */
@RestController
@RequestMapping("/api")
public class RestaurantResource {

    private final Logger log = LoggerFactory.getLogger(RestaurantResource.class);

    private static final String ENTITY_NAME = "restaurant";

    private final RestaurantRepository restaurantRepository;

    private final RestaurantMapper restaurantMapper;

    private final RestaurantSearchRepository restaurantSearchRepository;

    public RestaurantResource(RestaurantRepository restaurantRepository, RestaurantMapper restaurantMapper, RestaurantSearchRepository restaurantSearchRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantMapper = restaurantMapper;
        this.restaurantSearchRepository = restaurantSearchRepository;
    }

    /**
     * POST  /restaurants : Create a new restaurant.
     *
     * @param restaurantDTO the restaurantDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new restaurantDTO, or with status 400 (Bad Request) if the restaurant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/restaurants")
    @Timed
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody RestaurantDTO restaurantDTO) throws URISyntaxException {
        log.debug("REST request to save Restaurant : {}", restaurantDTO);
        if (restaurantDTO.getId() != null) {
            throw new BadRequestAlertException("A new restaurant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDTO);
        restaurant = restaurantRepository.save(restaurant);
        RestaurantDTO result = restaurantMapper.toDto(restaurant);
        restaurantSearchRepository.save(restaurant);
        return ResponseEntity.created(new URI("/api/restaurants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /restaurants : Updates an existing restaurant.
     *
     * @param restaurantDTO the restaurantDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated restaurantDTO,
     * or with status 400 (Bad Request) if the restaurantDTO is not valid,
     * or with status 500 (Internal Server Error) if the restaurantDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/restaurants")
    @Timed
    public ResponseEntity<RestaurantDTO> updateRestaurant(@RequestBody RestaurantDTO restaurantDTO) throws URISyntaxException {
        log.debug("REST request to update Restaurant : {}", restaurantDTO);
        if (restaurantDTO.getId() == null) {
            return createRestaurant(restaurantDTO);
        }
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDTO);
        restaurant = restaurantRepository.save(restaurant);
        RestaurantDTO result = restaurantMapper.toDto(restaurant);
        restaurantSearchRepository.save(restaurant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, restaurantDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /restaurants : get all the restaurants.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of restaurants in body
     */
    @GetMapping("/restaurants")
    @Timed
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Restaurants");
        Page<Restaurant> page = restaurantRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/restaurants");
        return new ResponseEntity<>(restaurantMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /restaurants/:id : get the "id" restaurant.
     *
     * @param id the id of the restaurantDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the restaurantDTO, or with status 404 (Not Found)
     */
    @GetMapping("/restaurants/{id}")
    @Timed
    public ResponseEntity<RestaurantDTO> getRestaurant(@PathVariable Long id) {
        log.debug("REST request to get Restaurant : {}", id);
        Restaurant restaurant = restaurantRepository.findOne(id);
        RestaurantDTO restaurantDTO = restaurantMapper.toDto(restaurant);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(restaurantDTO));
    }

    /**
     * DELETE  /restaurants/:id : delete the "id" restaurant.
     *
     * @param id the id of the restaurantDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/restaurants/{id}")
    @Timed
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        log.debug("REST request to delete Restaurant : {}", id);
        restaurantRepository.delete(id);
        restaurantSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/restaurants?query=:query : search for the restaurant corresponding
     * to the query.
     *
     * @param query the query of the restaurant search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/restaurants")
    @Timed
    public ResponseEntity<List<RestaurantDTO>> searchRestaurants(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Restaurants for query {}", query);
        Page<Restaurant> page = restaurantSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/restaurants");
        return new ResponseEntity<>(restaurantMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

}
