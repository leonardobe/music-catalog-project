package br.com.musiccatalog.controller;

import br.com.musiccatalog.domain.dto.MusicSearchResponseDTO;
import br.com.musiccatalog.domain.entity.Artist;
import br.com.musiccatalog.domain.entity.Music;
import br.com.musiccatalog.service.ArtistService;
import br.com.musiccatalog.service.MusicService;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MusicCatalogRunner {
    private final ArtistService artistService;
    private final MusicService musicService;
    private final ConfigurableApplicationContext context;
    private final Scanner sc = new Scanner(System.in);

    public MusicCatalogRunner(
            ArtistService artistService, MusicService musicService, ConfigurableApplicationContext context) {
        this.artistService = artistService;
        this.musicService = musicService;
        this.context = context;
    }

    public void start() {

        boolean running = true;

        while (running) {
            printMenu();

            try {
                int option = Integer.parseInt(sc.nextLine().trim());
                ;

                switch (option) {
                    case 1 -> registerArtist();
                    case 2 -> showArtistInfo();
                    case 3 -> registerTrack();
                    case 4 -> listTracksByArtist();
                    case 0 -> {
                        System.out.println("Exiting application...");
                        running = false;
                    }
                    default -> System.out.println("⚠\uFE0F Invalid option. Please choose 0, 1, 2, 3 or 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n⚠️ Invalid input. Please enter a numeric option.");
            } catch (RuntimeException e) {
                System.out.println("\n🚨 " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\n❌ Unexpected error. Please try again.");
            }
        }
        sc.close();
        context.close();
    }

    private void printMenu() {
        System.out.println("\n===============================");
        System.out.println("        MUSIC CATALOG");
        System.out.println("===============================");
        System.out.println("1 - Register artist");
        System.out.println("2 - Show artist information");
        System.out.println("3 - Register track for an artist");
        System.out.println("4 - List tracks by artist");
        System.out.println("0 - Exit\n");
        System.out.print("Choose an option: ");
    }

    private void registerArtist() {

        System.out.print("\n➡\uFE0F Enter artist name: ");
        String name = sc.nextLine();

        Optional<Artist> existingArtist = artistService.findArtistByName(name);

        if (existingArtist.isPresent()) {
            System.out.println("\n⚠\uFE0F Artist already registered.");
            return;
        }

        Artist artistFromApi = artistService.fetchArtistFromApi(name);

        if (artistFromApi == null) {
            System.out.println("\n❌ Artist not found.");
            return;
        }

        printArtistInfo(artistFromApi);

        System.out.print("\n➡\uFE0F Do you want to save this artist? (y/n): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("y")) {
            try {
                artistService.saveArtist(artistFromApi);
                System.out.println("\n✅ Artist saved successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println("\n🚨 " + e.getMessage());
            }
        } else {
            System.out.println("\n❌ Artist not saved.");
        }
    }

    private void showArtistInfo() {

        System.out.print("\n➡\uFE0F Enter artist name: ");
        String name = sc.nextLine();

        Optional<Artist> artist = artistService.findArtistByName(name);

        if (artist.isPresent()) {
            printArtistInfo(artist.get());
        } else {
            System.out.println("\n⚠\uFE0F Artist not found in database.");
        }
    }

    private void registerTrack() {

        System.out.print("\n➡\uFE0F Enter artist name: ");
        String artistName = sc.nextLine();

        Optional<Artist> artistOpt = artistService.findArtistByName(artistName);

        if (artistOpt.isEmpty()) {
            System.out.println("\n❌ Artist not registered, please register the artist to add a new song.");
            return;
        }

        Artist artist = artistOpt.get();

        System.out.print("➡\uFE0F Enter track name: ");
        String musicName = sc.nextLine();

        List<MusicSearchResponseDTO.MusicDTO> musics = musicService.searchMusicFromApi(musicName);

        if (musics.isEmpty()) {
            System.out.println("\n⚠\uFE0F No tracks found.");
            return;
        }

        int limit = Math.min(musics.size(), 5);

        System.out.println("\n\uD83D\uDCCC Search results:");

        AtomicInteger counter = new AtomicInteger(1);

        musics.stream()
                .limit(5)
                .forEach(music -> System.out.println("(" + counter.getAndIncrement() + ") "
                        + music.title() + " | "
                        + music.artist().name() + " [" + formatDuration(music.duration()) + "]"));

        System.out.print("\n➡\uFE0F Choose a track number to save: ");
        int choice = Integer.parseInt(sc.nextLine());

        if (choice < 1 || choice > limit) {
            System.out.println("\n⚠\uFE0F Invalid choice.");
            return;
        }

        MusicSearchResponseDTO.MusicDTO selectedTrack = musics.get(choice - 1);

        MusicSearchResponseDTO musicDetails = musicService.searchMusicInfoFromApi(
                selectedTrack.title(), selectedTrack.artist().name());

        try {
            musicService.saveMusic(musicDetails, artist.getId());
            System.out.println("\n✅ Track " + selectedTrack.title() + " - "
                    + selectedTrack.artist().name() + " saved successfully!");
        } catch (IllegalStateException e) {
            System.out.println("\n🚨 " + e.getMessage());
        }
    }

    private void listTracksByArtist() {

        System.out.print("\n➡\uFE0F Enter artist name: ");
        String artistName = sc.nextLine();

        List<Music> tracks = musicService.listMusicByArtist(artistName);

        if (tracks.isEmpty()) {
            System.out.println("\n❌ No tracks found for this artist.");
            return;
        }

        String trackWord = (tracks.size() == 1) ? "track" : "tracks";
        System.out.println("\n\uD83D\uDCCC Found " + tracks.size() + " " + trackWord + " for '" + artistName + "':");
        System.out.println("------------------------------------");
        tracks.forEach(
                track -> System.out.println(" ▶\uFE0F " + track.getTitle() + " | " + formatDuration(track.getDuration())
                        + " | " + formatPopularity(track.getRank()) + " | " + getRankStatus(track.getRank())));
    }

    private void printArtistInfo(Artist artist) {
        System.out.println("\n\uD83D\uDCCC Artist Information:");
        System.out.println("Name: " + artist.getName());
        System.out.println("URL: " + artist.getLink());
        System.out.println("Albums: " + artist.getAlbumCount());
        System.out.println("Fans: " + formatFansCount(artist.getFanCount()));
    }

    private String formatDuration(int seconds) {
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }

    private String formatFansCount(int fansCount) {
        NumberFormat fmt = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        fmt.setMaximumFractionDigits(1);
        fmt.setMinimumFractionDigits(0);

        return fmt.format(fansCount);
    }

    private String formatPopularity(Integer rank) {
        if (rank == null) return "▫️▫️▫️▫️▫️";
        int filledStars = Math.min(5, rank / 200000);
        int emptyStars = 5 - filledStars;

        return "⭐".repeat(filledStars) + "▪️".repeat(emptyStars);
    }

    private String getRankStatus(Integer rank) {
        if (rank == null) return "N/A";
        if (rank >= 800000) return "HOT";
        if (rank >= 500000) return "TRENDING";
        return "REGULAR";
    }
}
