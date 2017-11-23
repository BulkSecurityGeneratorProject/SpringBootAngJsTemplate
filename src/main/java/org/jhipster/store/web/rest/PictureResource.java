package org.jhipster.store.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.jhipster.store.domain.Picture;

import org.jhipster.store.repository.PictureRepository;
import org.jhipster.store.repository.search.PictureSearchRepository;
import org.jhipster.store.web.rest.errors.BadRequestAlertException;
import org.jhipster.store.web.rest.util.HeaderUtil;
import org.jhipster.store.web.rest.util.PaginationUtil;
import org.jhipster.store.service.dto.PictureDTO;
import org.jhipster.store.service.mapper.PictureMapper;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.json.JSONException;
import org.json.JSONObject;
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
 * REST controller for managing Picture.
 */
@RestController
@RequestMapping("/api")
public class PictureResource {

    private final Logger log = LoggerFactory.getLogger(PictureResource.class);

    private static final String ENTITY_NAME = "picture";

    private final PictureRepository pictureRepository;

    private final PictureMapper pictureMapper;

    private final PictureSearchRepository pictureSearchRepository;

    public PictureResource(PictureRepository pictureRepository, PictureMapper pictureMapper, PictureSearchRepository pictureSearchRepository) {
        this.pictureRepository = pictureRepository;
        this.pictureMapper = pictureMapper;
        this.pictureSearchRepository = pictureSearchRepository;
    }

    /**
     * POST  /pictures : Create a new picture.
     *
     * @param pictureDTO the pictureDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pictureDTO, or with status 400 (Bad Request) if the picture has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pictures")
    @Timed
    public ResponseEntity<PictureDTO> createPicture(@RequestBody PictureDTO pictureDTO) throws URISyntaxException {
        log.debug("REST request to save Picture : {}", pictureDTO);
        if (pictureDTO.getId() != null) {
            throw new BadRequestAlertException("A new picture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Picture picture = pictureMapper.toEntity(pictureDTO);
        picture = pictureRepository.save(picture);
        PictureDTO result = pictureMapper.toDto(picture);
        pictureSearchRepository.save(picture);
        return ResponseEntity.created(new URI("/api/pictures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pictures : Updates an existing picture.
     *
     * @param pictureDTO the pictureDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pictureDTO,
     * or with status 400 (Bad Request) if the pictureDTO is not valid,
     * or with status 500 (Internal Server Error) if the pictureDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pictures")
    @Timed
    public ResponseEntity<PictureDTO> updatePicture(@RequestBody PictureDTO pictureDTO) throws URISyntaxException {
        log.debug("REST request to update Picture : {}", pictureDTO);
        if (pictureDTO.getId() == null) {
            return createPicture(pictureDTO);
        }
        Picture picture = pictureMapper.toEntity(pictureDTO);
        picture = pictureRepository.save(picture);
        PictureDTO result = pictureMapper.toDto(picture);
        pictureSearchRepository.save(picture);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pictureDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pictures : get all the pictures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of pictures in body
     */
    @GetMapping("/pictures")
    @Timed
    public ResponseEntity<List<PictureDTO>> getAllPictures(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Pictures");
        Page<Picture> page = pictureRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pictures");
        return new ResponseEntity<>(pictureMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /pictures/:id : get the "id" picture.
     *
     * @param id the id of the pictureDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pictureDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pictures/{id}")
    @Timed
    public ResponseEntity<PictureDTO> getPicture(@PathVariable Long id) {
        log.debug("REST request to get Picture : {}", id);
        Picture picture = pictureRepository.findOne(id);
        PictureDTO pictureDTO = pictureMapper.toDto(picture);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pictureDTO));
    }

    /**
     * DELETE  /pictures/:id : delete the "id" picture.
     *
     * @param id the id of the pictureDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pictures/{id}")
    @Timed
    public ResponseEntity<Void> deletePicture(@PathVariable Long id) {
        log.debug("REST request to delete Picture : {}", id);
        pictureRepository.delete(id);
        pictureSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/pictures?query=:query : search for the picture corresponding
     * to the query.
     *
     * @param query the query of the picture search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/pictures")
    @Timed
    public ResponseEntity<List<PictureDTO>> searchPictures(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Pictures for query {}", query);
        Page<Picture> page = pictureSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/pictures");
        return new ResponseEntity<>(pictureMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

    @GetMapping("/getCollection/{json}")
    @Timed
    public ResponseEntity<List<PictureDTO>> getCollection(@ApiParam Pageable pageable, @PathVariable String json) {

        String mode;
        try {
            JSONObject obj = new JSONObject(json);
            mode = obj.getString("mode");

            switch (mode) {
                case "getPicRes":
                    Long resId = obj.getLong("resId");
                    return getPicOfRestaurant(pageable, resId);
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ResponseEntity<List<PictureDTO>> getPicOfRestaurant(@ApiParam Pageable pageable, Long id) {
        log.debug("REST request to getPicOfRestaurant : {}", id);
        Page<Picture> page = pictureRepository.findPictureOfRestaurant(pageable, id);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/getCollection");
        return new ResponseEntity<>(pictureMapper.toDto(page.getContent()), headers, HttpStatus.OK);
    }

}
