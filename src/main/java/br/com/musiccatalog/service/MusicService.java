package br.com.musiccatalog.service;

import br.com.musiccatalog.client.DeezerClient;
import br.com.musiccatalog.domain.dto.MusicSearchResponseDTO;
import br.com.musiccatalog.domain.entity.Album;
import br.com.musiccatalog.domain.entity.Artist;
import br.com.musiccatalog.domain.entity.Music;
import br.com.musiccatalog.repository.AlbumRepository;
import br.com.musiccatalog.repository.ArtistRepository;
import br.com.musiccatalog.repository.MusicRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MusicService {
    private final MusicRepository musicRepository;
    private final DeezerClient deezerClient;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

    public MusicService(
            MusicRepository musicRepository,
            DeezerClient deezerClient,
            ArtistRepository artistRepository,
            AlbumRepository albumRepository) {
        this.musicRepository = musicRepository;
        this.deezerClient = deezerClient;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
    }

    public List<MusicSearchResponseDTO.MusicDTO> searchMusicFromApi(String music) {
        MusicSearchResponseDTO response = deezerClient.searchMusic(music);

        return response.music();
    }

    public MusicSearchResponseDTO searchMusicInfoFromApi(String music, String artist) {
        return deezerClient.searchMusicInfo(music, artist);
    }

    public List<Music> listMusicByArtist(String artist) {
        return musicRepository.findMusicsByArtistName(artist);
    }

    public Optional<Album> listByTitleAndArtistName(Long id) {
        return albumRepository.findAlbumByDeezerId(id);
    }

    @Transactional
    public Music saveMusic(MusicSearchResponseDTO dto, Long artistId) {

        var musicDto = dto.music().getFirst();
        var musicName = musicDto.title();

        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new RuntimeException("Artist not found"));

        boolean exists =
                musicRepository.findByNameAndArtist(musicName, artistId).isPresent();

        if (exists) {
            throw new IllegalStateException("Track already registered for this artist.");
        }

        Album album = null;
        if (musicDto.album() != null) {
            String albumTitle = musicDto.album().title();
            long albumId = musicDto.album().id();

            album = albumRepository.findAlbumByDeezerId(albumId).orElseGet(() -> {
                Album newAlbum = new Album();
                newAlbum.setDeezerId(albumId);
                newAlbum.setTitle(albumTitle);
                newAlbum.setType(musicDto.album().type());
                return albumRepository.save(newAlbum);
            });
        }

        Music music = new Music();
        music.setDeezerId(musicDto.id());
        music.setTitle(musicDto.title());
        music.setLink(musicDto.link());
        music.setType(musicDto.type());

        if (musicDto.rank() != null && musicDto.rank() > 0) {
            music.setRank(musicDto.rank());
        }

        if (musicDto.duration() != null && musicDto.duration() > 0) {
            music.setDuration(musicDto.duration());
        }

        if (album != null) {
            album.addMusic(music);
        }

        artist.addMusic(music);

        return musicRepository.save(music);
    }
}
