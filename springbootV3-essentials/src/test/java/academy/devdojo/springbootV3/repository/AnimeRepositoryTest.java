package academy.devdojo.springbootV3.repository;

import academy.devdojo.springbootV3.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static academy.devdojo.springbootV3.util.CreateAnimeUtil.createAnime;

@DataJpaTest
@DisplayName("Test for Anime repository")
@Log4j2
class AnimeRepositoryTest {

    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save perists anime when Succesful")
    void save_Persist_Anime_WhenSuccesfull() {
        Anime animeToBeSaved = createAnime();
        //Responsável por persistir o dado no banco de dados
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);
        //Verifica se o anime não é nulo
        Assertions.assertThat(animeSaved).isNotNull();
        //Verifica se o id do anime não é nulo
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        //Verifica se o anime passada é o mesmo salvo no banco de dados
        Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());
    }

    @Test
    @DisplayName("Save update anime when Succesful")
    void save_UpdateAnime_WhenSuccesfull() {

        Anime animeToBeSaved = createAnime();
        //Persiste no BD o anime oriundo do método createdAnime
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);
        //Seta em cima do anime salvo um novo anime
        animeSaved.setName("HOTD");
        //Persiste no banco de dados um novo anime
        Anime animeUpdated = this.animeRepository.save(animeSaved);

        log.info("Update {}", animeUpdated);
        //Verifica se o anime atualizado não é nulo
        Assertions.assertThat(animeUpdated).isNotNull();
        //Verifica se o id do anime atualizado não é nulo
        Assertions.assertThat(animeUpdated.getId()).isNotNull();
        //Verifica se o Anime salvo no banco de dados é o mesmo do atualizado
        Assertions.assertThat(animeUpdated.getName()).isEqualTo(animeSaved.getName());

    }

    @Test
    @DisplayName("Delete remove anime when Succesful")
    void delete_RemovesAnime_WhenSuccesfull() {
        Anime animeToBeSaved = createAnime();
        //Persiste um anime no banco de dados
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);
        //Delete o anime salvo no banco de dados
        animeRepository.delete(animeSaved);
        log.info("Deleted {}", animeSaved.getId());
        //Procura via ID um anime no banco de dados
        Optional<Anime> animeOptional = this.animeRepository.findById(animeToBeSaved.getId());
        //Verifica se o anime procurado via id não consta na lista (está empty)
        Assertions.assertThat(animeOptional).isEmpty();

    }

    @Test
    @DisplayName("Find By Name returns list of anime when Succesful")
    void findByName_ReturnListOfAnime_WhenSuccesfull() {
        /*Puxa o anime criado contido em createAnime*/
        Anime animeToBeSaved = createAnime();
        //Persiste o animeToBeSaved no banco de dados
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);
        //Cria uma variável para utilizar na lista
        String name = animeSaved.getName();
        //Chama o método para percorrer a lista e achar o name
        List<Anime> animes = animeRepository.findByName(name);
        //Confere se a lista de anime não está vazia
        Assertions.assertThat(animes).isNotEmpty();
        //Confere se dentro da lista contém o aniSaved(verifica se não está vazia)
        Assertions.assertThat(animes).contains(animeSaved);
    }

    @Test
    @DisplayName("Find By Name returns empty list when no anime is found ")
    void findByName_ReturnEmptyListOfAnime_WhenIsNotFound() {
        /*Nesse caso está sendo procurado dentro da lista um nome que não foi
         * salvo anteriormente.*/
        List<Anime> animes = animeRepository.findByName("XX");
        //É realizado se a lista está vazia e está!
        Assertions.assertThat(animes).isEmpty();
        Assertions.assertThat(animes).isNotNull();
    }

    @Test
    @DisplayName("Save throw anime when ConstraintViolationException when name is empty")
    void save_Throw_ConstraintViolationException_WhenNameIsEmpty() {
        Anime anime = new Anime();
       /* Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
                .isInstanceOf(ConstraintViolationException.class);*/
        /*Esse método é utilizado para lançar a exception ConstraintViolation a qual é lançada
        * quando a divergencia no nome, onde que se for capturada a message conforme está abaixo
        * no withMessageContaining.*/
        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.animeRepository.save(anime))
                .withMessageContaining("The anime name cannot be empty");
    }

}