package academy.devdojo.springbootV3.service;

import academy.devdojo.springbootV3.domain.Anime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AnimeService {

    private static List<Anime> animes;

    static {
        animes = new ArrayList<>(List.of(new Anime(1L, "HOTD"), new Anime(2L, "Naruto")));
    }


    /*Método responsável por listar todos os animes*/
    public List<Anime> listAll() {
        return animes;
    }

    /*Método responsável por procurar um anime apartir de um id passado*/
    public Anime findById(long id) {
        /*Aqui é realizado uma varredura dentro da váriavel animes, filtrando pelo id e pegando o seu primeiro índice
        caso não seja encotrado um anime pelo Id passado é lançado uma exception 400.*/
        return animes.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found"));
    }
    /*Método responsável por adicionar um novo anime na lista*/
    public Anime save(Anime anime) {
        //Seta um novo ID randômico para identificar na lista
        anime.setId(ThreadLocalRandom.current().nextLong(3,20));
        animes.add(anime);
        return anime;
    }
    /*Método responsável por deletar os animes a partir de um id
    * para isso ele acessa o método findById para procurar se existe
    * um anime com o dado id ou não.*/
    public void delete(Long id) {
        animes.remove(findById(id));
    }
}
