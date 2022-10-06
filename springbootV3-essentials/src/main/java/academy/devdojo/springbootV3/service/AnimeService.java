package academy.devdojo.springbootV3.service;

import academy.devdojo.springbootV3.domain.Anime;
import academy.devdojo.springbootV3.exception.BadRequestException;
import academy.devdojo.springbootV3.mapper.AnimeMapper;
import academy.devdojo.springbootV3.repository.AnimeRepository;
import academy.devdojo.springbootV3.request.AnimePostRequestBody;
import academy.devdojo.springbootV3.request.AnimePutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;

    /*Método responsável por buscar todos os elementos contidos no banco de
     * dados com a paginação, passada por parâmetro*/
    public Page<Anime> listAll(Pageable pageable) {
        return animeRepository.findAll(pageable);
    }

    public List<Anime> listAllNonPageable() {
        return animeRepository.findAll();
    }

    /*Método responsável buscar dentro da lista de nomes o nome passado*/
    public List<Anime> findByName(String name) {
        return animeRepository.findByName(name);
    }

    /*Método responsável por buscar um apartir de uma letra que o reconheça*/
    public List<Anime> findAllByNameContainingIgnoreCase(String name) {
        return animeRepository.findAllByNameContainingIgnoreCase(name);
    }
    /*Método responsável por procurar um anime apartir de um id passado*/
    public Anime findByIdOrElseThrowBadRequestException(long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime not found"));
    }
    /*Método responsável por adicionar um novo anime na lista*/
    @Transactional
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
