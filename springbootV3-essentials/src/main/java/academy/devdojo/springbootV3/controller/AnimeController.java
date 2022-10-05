package academy.devdojo.springbootV3.controller;

import academy.devdojo.springbootV3.domain.Anime;
import academy.devdojo.springbootV3.request.AnimePostRequestBody;
import academy.devdojo.springbootV3.request.AnimePutRequestBody;
import academy.devdojo.springbootV3.service.AnimeService;
import academy.devdojo.springbootV3.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        return ResponseEntity.ok(animeService.findByIdOrElseThrowBadRequestException(id));
    }

    /*Aqui é utilizado a anotação de requisão de um parâmetro na URL onde que para relizar
     * essa busca é passando um nome, o qual chamará o método na camada service que baterá
     * na repository para buscar no banco de dados para encontrar o name passado.*/
    @GetMapping(path = "/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam(required = false) String name) {
        log.info("Find by name: " + name + " " + getHour());
        log.info("O status da requisão é: " + getStatus());
        return ResponseEntity.ok(animeService.findByName(name));
    }

    /*Método que busca um anime apartir de qualquer letra passada, ignorando letras
     * maiusculas ou minusculas, basta passar uma letra do anime desejado que ele
     * já a encontra*/
    @GetMapping(path = "/find/")
    public ResponseEntity<List<Anime>> findAllByNameContainingIgnoreCase(@RequestParam String name) {
        log.info("Find by name: " + name + " " + getHour());
        log.info("O status da requisão é: " + getStatus());
        return ResponseEntity.ok(animeService.findAllByNameContainingIgnoreCase(name));
    }

    /*Método que insere um novo anime na lista, onde que é Mapeado para o
     * corpo do dominio Anime, feito isso é chamado o método save passando
     * o anime e o código de 201*/
    //A anotação Valid, habilita a validação dos campos
    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody anime) {
        log.info("The anime is: " + anime + " " + getHour());
        log.info("O status da requisão é: " + HttpStatus.CREATED);
        return new ResponseEntity<>(animeService.save(anime), HttpStatus.CREATED);
    }

    /*Método que delete um anime da lista
     * para isso ele espera um id existente na url
     * e retorna o status 204 após a realização da requisição*/
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /*Método responsável por modificar o objeto, para isso
     * é realizado chamado o método replace que encontra-se
     * no service, é realizado as validações e retornarndo o
     * status 0K*/
    @PutMapping()
    public ResponseEntity<Void> replace(@RequestBody AnimePutRequestBody anime) {
        animeService.replace(anime);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private String getHour() {
        return dateUtil.formatLocalDateTimeToDataBaseStyle(LocalDateTime.now());
    }

    private HttpStatus getStatus() {
        return HttpStatus.OK;
    }
}
