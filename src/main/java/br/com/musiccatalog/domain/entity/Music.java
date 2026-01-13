package br.com.musiccatalog.domain.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "musics",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_artist_music",
                    columnNames = {"artist_id", "name"})
        })
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deezer_id")
    private Long deezerId;

    @Column(nullable = false)
    private String title;

    private String link;
    private Integer duration;
    private Integer rank;
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album albums;

    public Long getDeezerId() {
        return deezerId;
    }

    public void setDeezerId(Long deezerId) {
        this.deezerId = deezerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Album getAlbums() {
        return albums;
    }

    public void setAlbums(Album albums) {
        this.albums = albums;
    }

    public Long getId() {
        return id;
    }
}
