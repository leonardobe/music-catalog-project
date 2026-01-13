package br.com.musiccatalog.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MusicSearchResponseDTO(@JsonProperty("data") List<MusicDTO> music) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MusicDTO(
            Long id,
            String title,
            String link,
            Integer duration,
            Integer rank,
            ArtistDTO artist,
            AlbumDTO album,
            String type) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AlbumDTO(Long id, String title, String type) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ArtistDTO(Long id, String name, String link, String type) {}
}
