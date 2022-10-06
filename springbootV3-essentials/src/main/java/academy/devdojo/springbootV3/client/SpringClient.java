package academy.devdojo.springbootV3.client;

import academy.devdojo.springbootV3.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {

        /*O RestTemplate é utilizado para quando é necessário fazer uma chamada externa em outra API/endpoint
        * Nesse caso abaixo estamos tentando acessar os dados retornados na url passada abaixo trazendo consigo
        * todas as informações apartir do id passado atraés do método getForEntity.*/
        ResponseEntity<Anime> entity = new RestTemplate()
                .getForEntity("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info(entity);
        System.out.println("\n\n");
        //Aqui nesse caso será retornado somente o contéudo existente dentro do corpo apartir do id passado.
        Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info(object);
    }
}
