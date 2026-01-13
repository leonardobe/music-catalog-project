package br.com.musiccatalog.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ArtistSearchResponseDTO(@JsonProperty("data") List<ArtistDTO> artist) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ArtistDTO(Long id, String name, String link, int nb_album, int nb_fan, boolean radio, String type) {}
}
