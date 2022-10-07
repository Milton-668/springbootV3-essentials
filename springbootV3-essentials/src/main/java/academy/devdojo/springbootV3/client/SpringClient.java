package academy.devdojo.springbootV3.client;

import academy.devdojo.springbootV3.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {

        /*O RestTemplate é utilizado para quando é necessário fazer uma chamada externa em outra API/endpoint
         * Nesse caso abaixo estamos tentando acessar os dados retornados na url passada abaixo trazendo consigo
         * todas as informações apartir do id passado através do método getForEntity.*/
        ResponseEntity<Anime> entity = new RestTemplate()
                .getForEntity("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info(entity);
        System.out.println("Busca o anime dentro através do getForEntity\n\n");

        //Aqui nesse caso será retornado somente o contéudo existente dentro do corpo apartir do id passado.
        Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info(object);
        System.out.println("Busca o anime via Id através do getForObject\n\n");

        /*Caso queira buscar por todos os animes contidos, podemos buscar pelo Array*/
        Anime[] animes = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);
        log.info(Arrays.toString(animes));
        System.out.println("Busca todos os dados contidos no Array através do getForObject\n\n");

        /*Trás uma lista tbm de todos os animes */
        //@formatter:off
        ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange("http://localhost:8080/animes/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
        //@formatter:on
        log.info(exchange.getBody());
        System.out.println("Busca todos os animes através do método exchange e ParameterizedTypeReference\n\n");

       /* *//*Utilizado para acessar o endpoint e postar um novo anime apartir dele*//*
        Anime kingdom = Anime.builder().name("Kingdom").build();
        Anime kingdomSaved = new RestTemplate().postForObject("http://localhost:8080/animes/", kingdom, Anime.class);
        log.info("Saved anime {}", kingdomSaved);
        System.out.println("Utiliza a url para postar um novo anime através do postForObject\n\n");*/

        /*Utilizado para acessar o endpoint passado abaixo e inserir um novo anime de modo que ao acessar o método
        * static é passado um Header do tipo applicationJson*/
        Anime blayBlade = Anime.builder().name("Blay blade").build();
        ResponseEntity<Anime> blayBadeSaved = new RestTemplate()
                .exchange("http://localhost:8080/animes/",
                        HttpMethod.POST,
                        new HttpEntity<>(blayBlade, createJsonHeader()),
                        Anime.class);
        System.out.println("Cria um novo anime através do exchange especificando o mediaType como Json\n\n");

        log.info("Saved anime {}", blayBadeSaved);

        /*Utilizado para atualizar o nome do anime da seguinte forma:*/
        /*É pego através do anime postado anteriormente e é setado em cima dele
        * um novo nome, feito isso é montado a estrutra do exchange retornando
        * uma ResponseEntity vazia, com a sua url, e um HttpEntity passando
        * o animeToBeUpdated e o header, logo é passado o Void.class */
        Anime animeToBeUpdated = blayBadeSaved.getBody();
        animeToBeUpdated.setName("Blay blade 2");

        ResponseEntity<Void> blayBadeUpdated = new RestTemplate()
                .exchange("http://localhost:8080/animes/",
                        HttpMethod.PUT,
                        new HttpEntity<>(animeToBeUpdated, createJsonHeader()),
                        Void.class);
        System.out.println("Atualiza um anime através do exchange especificando o mediaType como Json\n\n");
        log.info(blayBadeUpdated);
        /*Utilizado para deletar um anime atualizado anteriormente. onde que é passado uma url apontando como
        * parâmetro um id, é passado o tipo do verbo http, logo abaixo a construção do header, o Void.Class e
        * por fim é passado o id do animeToBeUpdated.*/
        ResponseEntity<Void> blayBadeDeleted = new RestTemplate()
                .exchange("http://localhost:8080/animes/{id}",
                        HttpMethod.DELETE,
                        new HttpEntity<>(createJsonHeader()),
                        Void.class,
                        animeToBeUpdated.getId());
        System.out.println("Deleta um anime através do exchange especificando o mediaType como Json\n\n");
        log.info(blayBadeDeleted);
    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
