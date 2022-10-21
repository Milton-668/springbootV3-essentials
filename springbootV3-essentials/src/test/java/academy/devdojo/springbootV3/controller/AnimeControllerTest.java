package academy.devdojo.springbootV3.controller;

import academy.devdojo.springbootV3.domain.Anime;
import academy.devdojo.springbootV3.request.AnimePostRequestBody;
import academy.devdojo.springbootV3.request.AnimePutRequestBody;
import academy.devdojo.springbootV3.service.AnimeService;
import academy.devdojo.springbootV3.util.AnimeCreatorUtil;
import academy.devdojo.springbootV3.util.AnimePostRequestBodyUtil;
import academy.devdojo.springbootV3.util.AnimePutRequestBodyUtil;
import academy.devdojo.springbootV3.util.DateUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

//Habilita a utilização do JUnit com o Spring
@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    //Injeta a dependência da classe a ser testada
    @InjectMocks
    private AnimeController animeController;

    //Injeta a dependência das classes que estão
    //contidas na classe principal a ser testada
    @Mock
    private AnimeService animeServiceMock;

    @Mock
    private DateUtil dateUtil;

    //É executado antes de passar por qualquer método
    @BeforeEach
    void setup() {
        /*O método abaixo fará o seguinte:
         * todas as vezes que for passado pelo teste, será passaddo por esse método primeiramente
         * onde que será instanciando um PageImpl<> passado por uma lista dentro do createValidAnime
         * Abaixo no método when, quando for passado algum argumento detro da lista contida em
         * animeServiceMock ele retornará os dados contidos em um animePage a qual foi construido acima.*/
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreatorUtil.createValidAnime()));
        List<Anime> animes = List.of(AnimeCreatorUtil.createValidAnime());
        Anime anime = AnimeCreatorUtil.createValidAnime();

        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
                .thenReturn(animePage);

        BDDMockito.when(animeServiceMock.listAllNonPageable())
                .thenReturn(animes);

        BDDMockito.when(animeServiceMock.findByIdOrElseThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(anime);

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(animes);

        BDDMockito.when(animeServiceMock.findAllByNameContainingIgnoreCase(ArgumentMatchers.anyString()))
                .thenReturn(animes);

        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(anime);
        //Utilizado para métodos que retornam Void
        BDDMockito.doNothing().when(animeServiceMock)
                .replace(ArgumentMatchers.any(AnimePutRequestBody.class));

        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());


    }

    @Test
    @DisplayName("List returns list of anime inside page object when sucessful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSucessFul() {
        /*Teste do método responsável por retornar os animes paginados*/
        /*Aqui ele puxa o método criado para ser inserido um anime*/
        String expectedName = AnimeCreatorUtil.createValidAnime().getName();
        //É realizada a chamada do listall paginado
        Page<Anime> animePage = animeController.listAll(null).getBody();
        //Verifica se o conteúdo passado não é nulo
        Assertions.assertThat(animePage).isNotNull();
        //Verfica se o contéudo da lista não é vazio e se é do tamanho 1
        Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);
        //Verifica se a posição do anime contido na paginação é a mesma do anime esperado.
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
        System.out.println();

    }

    @Test
    @DisplayName("listAll returns list of anime when sucessful")
    void listAll_ReturnsListOfAnimes_WhenSucessFul() {
        /*Teste do método utilizado para listar todos os animes*/
        //É puxado o método utils que trás um anime
        String expectedName = AnimeCreatorUtil.createValidAnime().getName();
        //é chamado o método que trás consigo a lista de todos os animes
        List<Anime> animes = animeController.listAll().getBody();
        //é realizado uma verificação se o anime contido na lista:
        //não é nulo, não é vazio e o seu tamanho é 1
        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);
        //É verificado se o anime contido na posição 0 é o mesmo anime esperado
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findById returns of anime when sucessful")
    void findById_ReturnOfAnime_WhenSucessFul() {
        /*Teste do método responsável por buscar um anime via Id*/
        //Aqui é chamado o método utils que é buscado via ID
        Long expectedId = AnimeCreatorUtil.createValidAnime().getId();
        //Aqui é chamado o método no controller que busca por ID onde que
        //contém um parâmetro que no caso seria um long
        Anime anime = animeController.findById(1L).getBody();
        //É verificado que o anime não é nulo
        Assertions.assertThat(anime).isNotNull();
        /*É verificado que o anime passado via ID, não é nulo é igual ao anime
         * esperado onde foi passado via ID*/
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);

    }

    @Test
    @DisplayName("findByName returns of anime when sucessful")
    void findByName_ReturnsOfAnimes_WhenSucessFul() {
        /*Teste do método responsável por buscar via nome*/
        //É realizada a chamada do método utils que cria um anime
        String expectedName = AnimeCreatorUtil.createValidAnime().getName();
        //é chamado o método que procura por um nome o qual possui um parâmetro
        //que está sendo passado.
        List<Anime> animes = animeController.findByName("").getBody();
        /*É verificado que o anime passado não é nulo, não é vazio e tbm possui o tamanho 1*/
        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);
        //É verificado se o anime na posição 0 é igual ao do anime esperado.
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findByName returns an empty list of anime when anime is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound() {
        /*Teste realizado para caso o anime não seja encontrado*/
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());
        /*É realizado a chamada do método que procura por nome e é atruibuido ""
         * para dizer que o método não é nulo*/
        List<Anime> animes = animeController.findByName("").getBody();
        /*é realizado a verificação se o método não é nulo e se está vazio*/
        Assertions.assertThat(animes).isNotNull().isEmpty();


    }

    @Test
    @DisplayName("findAllByNameContainingIgnoreCase returns of anime when sucessful")
    void findAllByNameContainingIgnoreCase_ReturnsOfAnime_WhenSucessFul() {
        /*Teste do Método utilizado para encontrar um anime por alguma letra que o compoê*/
        String expectedName = AnimeCreatorUtil.createValidAnime().getName();

        List<Anime> animes = animeController.findAllByNameContainingIgnoreCase("").getBody();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("save returns of anime when sucessful")
    void save_ReturnOfAnime_WhenSucessFul() {
        Anime validAnime = AnimeCreatorUtil.createValidAnime();

        Anime anime = animeController.save(AnimePostRequestBodyUtil.createAnimePostRequestBody()).getBody();

        Assertions.assertThat(anime).isNotNull().isEqualTo(validAnime);

    }


    @Test
    @DisplayName("replace updates anime when sucessful")
    void replace_UpdateAnime_WhenSucessFul() {

        Assertions.assertThatCode(() -> animeController
                        .replace(AnimePutRequestBodyUtil.createAnimePutRequestBody()))
                .doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController
                .replace(AnimePutRequestBodyUtil.createAnimePutRequestBody());

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    @DisplayName("delete remove anime when sucessful")
    void delete_RemovesAnime_WhenSucessFul() {

        Assertions.assertThatCode(() -> animeController
                .delete(1L)).doesNotThrowAnyException();

        ResponseEntity<Void> entity = animeController
                .delete(1L);

        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }


}