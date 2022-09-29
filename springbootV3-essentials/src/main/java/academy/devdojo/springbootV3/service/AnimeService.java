package academy.devdojo.springbootV3.service;

import academy.devdojo.springbootV3.domain.Anime;
import academy.devdojo.springbootV3.mapper.AnimeMapper;
import academy.devdojo.springbootV3.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import request.AnimePostRequestBody;
import request.AnimePutRequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;
    public List<Anime> listAll() {
        return animeRepository.findAll();
    }

    /*Método responsável por procurar um anime apartir de um id passado*/
    public Anime findByIdOrElseThrowBadRequestException(long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found"));
    }

    /*Método responsável por adicionar um novo anime na lista*/
    public Anime save(AnimePostRequestBody animePostRequestBody) {
        //Utilizada para mapear a conversão do DTO para a classe Anime, expondo apenas o necessário
        Anime mapper = AnimeMapper.INSTANCE.AnimePostRequestBodyToAnime(animePostRequestBody);
        return animeRepository.save(mapper);
    }


    public void delete(Long id) {
        animeRepository.delete(findByIdOrElseThrowBadRequestException(id));
    }

    public void replace(AnimePutRequestBody animePutRequestBody) {
        Anime savedAnime = findByIdOrElseThrowBadRequestException(animePutRequestBody.getId());
        Anime mapper = AnimeMapper.INSTANCE.AnimePutRequestBodyToAnime(animePutRequestBody);
        mapper.setId(savedAnime.getId());

        animeRepository.save(mapper);
    }
}
