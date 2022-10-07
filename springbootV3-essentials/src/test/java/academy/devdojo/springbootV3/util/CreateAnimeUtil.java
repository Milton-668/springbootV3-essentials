package academy.devdojo.springbootV3.util;

import academy.devdojo.springbootV3.domain.Anime;

public class CreateAnimeUtil {

    /*Método responsável por criar um anime*/
    public static Anime createAnime() {
        return Anime.builder()
                .name("One Piece")
                .build();
    }
}
