package br.com.musiccatalog.client;

import br.com.musiccatalog.domain.dto.ArtistSearchResponseDTO;
import br.com.musiccatalog.domain.dto.MusicSearchResponseDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class DeezerClient {
    private final WebClient webClient;

    public DeezerClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public ArtistSearchResponseDTO getArtistInfo(String artistName) {
        try {
            return webClient
                    .get()
                    .uri(u ->
                            u.path("/search/artist").queryParam("q", artistName).build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, r -> r.bodyToMono(String.class)
                            .flatMap(body -> Mono.error(new RuntimeException(
                                    "Failed to fetch artist from Last.fm API. HTTP Status: " + r.statusCode()))))
                    .bodyToMono(ArtistSearchResponseDTO.class)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new RuntimeException(
                    "Last.fm API responded with error " + ex.getStatusCode() + " while retrieving the artist.");
        } catch (WebClientRequestException ex) {
            throw new RuntimeException(
                    "Connectivity error while accessing Last.fm API to retrieve the artist. Please check your"
                            + " network.");
        } catch (Exception ex) {
            throw new RuntimeException("Unexpected error while retrieving artist: " + ex.getMessage());
        }
    }

    public MusicSearchResponseDTO searchMusic(String musicName) {
        try {
            return webClient
                    .get()
                    .uri(u -> u.path("/search").queryParam("q", musicName).build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, r -> r.bodyToMono(String.class)
                            .flatMap(body -> Mono.error(new RuntimeException(
                                    "Failed to fetch track from Last.fm API. HTTP Status: " + r.statusCode()))))
                    .bodyToMono(MusicSearchResponseDTO.class)
                    .block();

        } catch (WebClientResponseException ex) {
            throw new RuntimeException(
                    "Last.fm API responded with error " + ex.getStatusCode() + " while retrieving the songs.");
        } catch (WebClientRequestException ex) {
            throw new RuntimeException(
                    "Connectivity error while accessing Last.fm API to retrieve the songs. Please check your network.");
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while searching for track: " + e.getMessage());
        }
    }

    public MusicSearchResponseDTO searchMusicInfo(String musicName, String artistName) {
        try {
            String query = String.format("artist:\"%s\" track:\"%s\"", artistName, musicName);

            return webClient
                    .get()
                    .uri(u -> u.path("/search").queryParam("q", query).build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, r -> r.bodyToMono(String.class)
                            .flatMap(body -> Mono.error(new RuntimeException(
                                    "Failed to get song details from the Last.fm API. HTTP status: "
                                            + r.statusCode()))))
                    .bodyToMono(MusicSearchResponseDTO.class)
                    .block();

        } catch (WebClientResponseException ex) {
            throw new RuntimeException(
                    "Last.fm API responded with error " + ex.getStatusCode() + " while retrieving the song details.");
        } catch (WebClientRequestException ex) {
            throw new RuntimeException(
                    "Connectivity error while accessing Last.fm API to retrieve the song details. Please check your"
                            + " network.");
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while searching for song: " + e.getMessage());
        }
    }
}
