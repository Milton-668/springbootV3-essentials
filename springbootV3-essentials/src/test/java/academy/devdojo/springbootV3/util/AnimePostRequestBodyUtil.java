package academy.devdojo.springbootV3.util;

import academy.devdojo.springbootV3.request.AnimePostRequestBody;

public class AnimePostRequestBodyUtil {

    public static AnimePostRequestBody createAnimePostRequestBody() {
        return AnimePostRequestBody.builder()
                .name(AnimeCreatorUtil.createAnimeToBeSaved().getName())
                .build();
    }
}
