package br.com.musiccatalog.domain.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artists")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deezer_id")
    private Long deezerId;

    @Column(nullable = false)
    private String name;

    private Integer albumCount;
    private Integer fanCount;

    @Column(name = "has_radio")
    private Boolean radio;

    private String type;

    private String link;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Music> musics = new ArrayList<>();

    public void addMusic(Music music) {
        musics.add(music);
        music.setArtist(this);
    }

    public void removeMusic(Music music) {
        musics.remove(music);
        music.setArtist(null);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getId() {
        return id;
    }

    public Long getDeezerId() {
        return deezerId;
    }

    public void setDeezerId(Long deezerId) {
        this.deezerId = deezerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAlbumCount() {
        return albumCount;
    }

    public void setAlbumCount(Integer albumCount) {
        this.albumCount = albumCount;
    }

    public Integer getFanCount() {
        return fanCount;
    }

    public void setFanCount(Integer fanCount) {
        this.fanCount = fanCount;
    }

    public Boolean getRadio() {
        return radio;
    }

    public void setRadio(Boolean radio) {
        this.radio = radio;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Music> getMusics() {
        return musics;
    }

    public void setMusics(List<Music> musics) {
        this.musics = musics;
    }
}
