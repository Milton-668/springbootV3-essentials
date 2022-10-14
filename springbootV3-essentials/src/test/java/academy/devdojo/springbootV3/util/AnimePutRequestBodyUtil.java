package academy.devdojo.springbootV3.util;

import academy.devdojo.springbootV3.request.AnimePutRequestBody;

public class AnimePutRequestBodyUtil {

    public static AnimePutRequestBody createAnimePutRequestBody() {
        return AnimePutRequestBody.builder()
                .name(AnimeCreatorUtil.createValidUpdateAnime().getName())
                .id(AnimeCreatorUtil.createValidUpdateAnime().getId())
                .build();
    }
}
