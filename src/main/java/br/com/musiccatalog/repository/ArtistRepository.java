package br.com.musiccatalog.repository;

import br.com.musiccatalog.domain.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Optional<Artist> findArtistByName(String artistName);
}
