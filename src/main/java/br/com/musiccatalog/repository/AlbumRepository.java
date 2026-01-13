package br.com.musiccatalog.repository;

import br.com.musiccatalog.domain.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    @Query("SELECT a FROM Album a WHERE a.deezerId = :deezerId")
    Optional<Album> findAlbumByDeezerId(@Param("deezerId") Long deezerId);
}
