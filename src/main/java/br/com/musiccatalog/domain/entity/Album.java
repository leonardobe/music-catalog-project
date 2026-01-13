package br.com.musiccatalog.domain.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deezer_id")
    private Long deezerId;

    private String title;
    private String type;

    @OneToMany(mappedBy = "albums", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Music> musics = new ArrayList<>();

    public void addMusic(Music music) {
        musics.add(music);
        music.setAlbums(this);
    }

    public void removeMusic(Music music) {
        musics.remove(music);
        music.setAlbums(null);
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
