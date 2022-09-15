package academy.devdojo.springbootV3.controller;

import academy.devdojo.springbootV3.domain.Anime;
import academy.devdojo.springbootV3.service.AnimeService;
import academy.devdojo.springbootV3.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

//Define que a classe é um controlador
@RestController
//Define que a classe será uma requisição mapeada no endereço passado
@RequestMapping("animes")
//Define os logs
@Log4j2
//Utilizada para instanciar uma classe, onde requer o constructor
@RequiredArgsConstructor
public class AnimeController {

    private final DateUtil dateUtil;
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<List<Anime>> list() {
        //Responsável por logar o tempo exato que a requisição foi feita
        log.info(getHour());
        //Responsável por logar o status da requisição
        log.info("O status da requisão é: " + getStatus());
        //Retornar a lista com os animes alimentados e o status da aplicação
        return ResponseEntity.ok(animeService.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable Long id) {
        log.info("Find by id: " + id + " " + getHour());
        log.info("O status da requisão é: " + getStatus());
        return ResponseEntity.ok(animeService.findById(id));
    }

    private String getHour() {
        return dateUtil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now());
    }

    private HttpStatus getStatus() {
        return HttpStatus.OK;
    }
}
