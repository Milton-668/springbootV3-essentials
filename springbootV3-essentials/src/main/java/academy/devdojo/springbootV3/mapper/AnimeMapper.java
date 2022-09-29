package academy.devdojo.springbootV3.mapper;

import academy.devdojo.springbootV3.domain.Anime;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import request.AnimePostRequestBody;
import request.AnimePutRequestBody;

@Mapper(componentModel = "spring")
public abstract class AnimeMapper {
    /*Classe utilizada para converter o AnimePostRequestBody para Anime
    * essa conversão é realizada para não expor todos os atributos da camada
    * de dominio da aplicação, sendo apenas exposta os atributos necessários*/

    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    public abstract Anime AnimePostRequestBodyToAnime(AnimePostRequestBody animePostRequestBody);

    public abstract Anime AnimePutRequestBodyToAnime(AnimePutRequestBody animePutRequestBody);


}
