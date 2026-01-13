package br.com.musiccatalog.service;

import br.com.musiccatalog.client.DeezerClient;
import br.com.musiccatalog.domain.dto.ArtistSearchResponseDTO;
import br.com.musiccatalog.domain.entity.Artist;
import br.com.musiccatalog.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final DeezerClient deezerClient;

    public ArtistService(ArtistRepository artistRepository, DeezerClient deezerClient) {
        this.artistRepository = artistRepository;
        this.deezerClient = deezerClient;
    }

    public Optional<Artist> findArtistByName(String artistName) {
        return artistRepository.findArtistByName(artistName);
    }

    public Artist fetchArtistFromApi(String name) {
        ArtistSearchResponseDTO response = deezerClient.getArtistInfo(name);

        var artistDto = response.artist().getFirst();

        Artist artist = new Artist();
        artist.setDeezerId(artistDto.id());
        artist.setName(artistDto.name());
        artist.setAlbumCount(artistDto.nb_album());
        artist.setFanCount(artistDto.nb_fan());
        artist.setRadio(artistDto.radio());
        artist.setType(artistDto.type());
        artist.setLink(artistDto.link());

        return artist;
    }

    public Artist saveArtist(Artist artist) {
        if (artist.getName() == null || artist.getName().isBlank()) {
            throw new IllegalArgumentException("Artist name is required");
        }

        return artistRepository.save(artist);
    }
}
