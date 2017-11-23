package org.jhipster.store.repository;

import org.jhipster.store.domain.Picture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Picture entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {

    @Query("select pic from Picture pic where pic.restaurant.id =:id")
    Page<Picture> findPictureOfRestaurant(Pageable pageable, @Param("id") Long id);
}
