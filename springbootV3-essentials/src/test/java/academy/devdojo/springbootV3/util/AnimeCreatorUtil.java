package academy.devdojo.springbootV3.util;

import academy.devdojo.springbootV3.domain.Anime;

public class AnimeCreatorUtil {

    /*Método responsável por criar um anime*/
    public static Anime createAnimeToBeSaved() {
        return Anime.builder()
                .name("One Piece")
                .build();
    }

    public static Anime createValidAnime() {
        return Anime.builder()
                .name("One Piece")
                .id(1L)
                .build();
    }
    public static Anime createValidUpdateAnime() {
        return Anime.builder()
                .name("One Piece 2")
                .id(1L)
                .build();
    }
}
