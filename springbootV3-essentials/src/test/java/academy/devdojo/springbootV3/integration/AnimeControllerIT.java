package academy.devdojo.springbootV3.integration;

import academy.devdojo.springbootV3.domain.Anime;
import academy.devdojo.springbootV3.repository.AnimeRepository;
import academy.devdojo.springbootV3.request.AnimePostRequestBody;
import academy.devdojo.springbootV3.util.AnimeCreatorUtil;
import academy.devdojo.springbootV3.util.AnimePostRequestBodyUtil;
import academy.devdojo.springbootV3.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {
                "server.port=8081"
        })
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AnimeControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private AnimeRepository animeRepository;
    @LocalServerPort
    private int port;


    @Test
    @DisplayName("List returns list of anime inside page object when sucessful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSucessFul() {
        Anime savedAnime = animeRepository.save(AnimeCreatorUtil.createAnimeToBeSaved());

        String expectedName = savedAnime.getName();

        PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes", HttpMethod.GET, null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();
        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("listAll returns list of anime when sucessful")
    void listAll_ReturnsListOfAnimes_WhenSucessFul() {
        Anime savedAnime = animeRepository.save(AnimeCreatorUtil.createAnimeToBeSaved());

        String expectedName = savedAnime.getName();

        List<Anime> animes = testRestTemplate.exchange("/animes/all", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findById returns of anime when sucessful")
    void findById_ReturnOfAnime_WhenSucessFul() {
        Anime savedAnime = animeRepository.save(AnimeCreatorUtil.createAnimeToBeSaved());

        Long expectedId = savedAnime.getId();

        Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);
        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);

    }

    @Test
    @DisplayName("findByName returns of anime when sucessful")
    void findByName_ReturnsOfAnimes_WhenSucessFul() {
        Anime savedAnime = animeRepository.save(AnimeCreatorUtil.createAnimeToBeSaved());

        String expectedName = savedAnime.getName();
        String url = String.format("/animes/find?name=%s", expectedName);

        List<Anime> animes = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findByName returns an empty list of anime when anime is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound() {
        List<Anime> animes = testRestTemplate.exchange("/animes/find?name=%s=dbz", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();

    }

    @Test
    @DisplayName("findAllByNameContainingIgnoreCase returns of anime when sucessful")
    void findAllByNameContainingIgnoreCase_ReturnsOfAnime_WhenSucessFul() {
        Anime savedAnime = animeRepository.save(Anime.builder().name("DBZ").build());

        String expectedName = savedAnime.getName();
        String url = String.format("/animes/find/?name=%s", "D");

        List<Anime> animes = testRestTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();
        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("save returns of anime when sucessful")
    void save_ReturnOfAnime_WhenSucessFul() {
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyUtil.createAnimePostRequestBody();
        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity("/animes", animePostRequestBody, Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();

    }


    @Test
    @DisplayName("replace updates anime when sucessful")
    void replace_UpdateAnime_WhenSucessFul() {
        Anime savedAnime = animeRepository.save(AnimeCreatorUtil.createAnimeToBeSaved());

        savedAnime.setName("New Anime");

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes",
                HttpMethod.PUT, new HttpEntity<>(savedAnime), Void.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);


    }

    @Test
    @DisplayName("delete remove anime when sucessful")
    void delete_RemovesAnime_WhenSucessFul() {

        Anime savedAnime = animeRepository.save(AnimeCreatorUtil.createAnimeToBeSaved());

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/{id}",
                HttpMethod.DELETE, null, Void.class, savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

}
