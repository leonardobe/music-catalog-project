package br.com.musiccatalog.repository;

import br.com.musiccatalog.domain.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MusicRepository extends JpaRepository<Music, Long> {
    @Query("""
        SELECT m FROM Music m
        WHERE LOWER(m.artist.name) = LOWER(:artist)
        """)
    List<Music> findMusicsByArtistName(String artist);

    @Query("""
        SELECT m FROM Music m
        WHERE LOWER(m.title) = LOWER(:musicName)
        AND m.artist.id = :artistId
        """)
    Optional<Music> findByNameAndArtist(String musicName, Long artistId);
}
