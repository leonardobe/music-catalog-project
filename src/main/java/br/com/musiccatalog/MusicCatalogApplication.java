package br.com.musiccatalog;

import br.com.musiccatalog.controller.MusicCatalogRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusicCatalogApplication implements CommandLineRunner {

    private final MusicCatalogRunner runner;

    public MusicCatalogApplication(MusicCatalogRunner runner) {
        this.runner = runner;
    }

    public static void main(String[] args) {
        SpringApplication.run(MusicCatalogApplication.class, args);
    }

    @Override
    public void run(String... args) {
        runner.start();
    }
}
