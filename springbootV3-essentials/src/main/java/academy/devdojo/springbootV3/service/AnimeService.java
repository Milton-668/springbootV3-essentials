package academy.devdojo.springbootV3.service;

import academy.devdojo.springbootV3.domain.Anime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AnimeService {

    private final List<Anime> animes = List.of(new Anime(1L, "HOTD"), new Anime(2L, "Naruto"));

    /*Método responsável por listar todos os animes*/
    public List<Anime> listAll() {
        return animes;
    }
    /*Método responsável por procurar um anime apartir de um id passado*/
    public Anime findById(long id ) {
        /*Aqui é realizado uma varredura dentro da váriavel animes, filtrando pelo id e pegando o seu primeiro índice
        caso não seja encotrado um anime pelo Id passado é lançado uma exception 400.*/
        return animes.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Anime not found"));
    }
}
