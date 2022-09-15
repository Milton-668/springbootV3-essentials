package academy.devdojo.springbootV3.controller;

import academy.devdojo.springbootV3.domain.Anime;
import academy.devdojo.springbootV3.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    public List<Anime> anime() {
        log.info(dateUtil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now()));
        return List.of(new Anime("HOTD"), new Anime("Naruto"));
    }
}
