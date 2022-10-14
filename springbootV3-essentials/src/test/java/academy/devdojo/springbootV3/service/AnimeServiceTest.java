package academy.devdojo.springbootV3.service;

import academy.devdojo.springbootV3.domain.Anime;
import academy.devdojo.springbootV3.exception.BadRequestException;
import academy.devdojo.springbootV3.repository.AnimeRepository;
import academy.devdojo.springbootV3.util.AnimeCreatorUtil;
import academy.devdojo.springbootV3.util.AnimePostRequestBodyUtil;
import academy.devdojo.springbootV3.util.AnimePutRequestBodyUtil;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setup() {

        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreatorUtil.createValidAnime()));
        List<Anime> animes = List.of(AnimeCreatorUtil.createValidAnime());
        Anime anime = AnimeCreatorUtil.createValidAnime();

        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(animes);

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(anime));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(animes);

        BDDMockito.when(animeRepositoryMock.findAllByNameContainingIgnoreCase(ArgumentMatchers.anyString()))
                .thenReturn(animes);

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(anime);

        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));


    }

    @Test
    @DisplayName("listAll returns list of anime inside page object when sucessful")
    void listAll_ReturnsListOfAnimesInsidePageObject_WhenSucessFul() {
        String expectedName = AnimeCreatorUtil.createValidAnime().getName();
        Page<Anime> animePage = animeService.listAll(PageRequest.of(1, 1));

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("listAllNonPageable returns list of anime when sucessful")
    void listAllNonPageable_ReturnsListOfAnimes_WhenSucessFul() {
        String expectedName = AnimeCreatorUtil.createValidAnime().getName();

        List<Anime> animes = animeService.listAllNonPageable();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findByIdOrElseThrowBadRequestException returns of anime when sucessful")
    void findByIdOrElseThrowBadRequestException_ReturnOfAnime_WhenSucessFul() {
        Long expectedId = AnimeCreatorUtil.createValidAnime().getId();

        Anime anime = animeService.findByIdOrElseThrowBadRequestException(1L);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);

    }

    @Test
    @DisplayName("findByIdOrElseThrowBadRequestException throws BadRequestException when anime is not found")
    void findByIdOrElseThrowBadRequestException_BadRequestException_WhenAnimeIsNotFound() {
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> animeService.findByIdOrElseThrowBadRequestException(1L));

    }

    @Test
    @DisplayName("findByName returns of anime when sucessful")
    void findByName_ReturnsOfAnimes_WhenSucessFul() {
        String expectedName = AnimeCreatorUtil.createValidAnime().getName();

        List<Anime> animes = animeService.findByName("");

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("findByName returns an empty list of anime when anime is not found")
    void findByName_ReturnsEmptyListOfAnime_WhenAnimeIsNotFound() {
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animes = animeService.findByName("");

        Assertions.assertThat(animes).isNotNull().isEmpty();

    }

    @Test
    @DisplayName("findAllByNameContainingIgnoreCase returns of anime when sucessful")
    void findAllByNameContainingIgnoreCase_ReturnsOfAnime_WhenSucessFul() {
        String expectedName = AnimeCreatorUtil.createValidAnime().getName();

        List<Anime> animes = animeService.findAllByNameContainingIgnoreCase("");

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);

    }

    @Test
    @DisplayName("save returns of anime when sucessful")
    void save_ReturnOfAnime_WhenSucessFul() {
        Anime validAnime = AnimeCreatorUtil.createValidAnime();

        Anime anime = animeService.save(AnimePostRequestBodyUtil.createAnimePostRequestBody());

        Assertions.assertThat(anime).isNotNull().isEqualTo(validAnime);

    }


    @Test
    @DisplayName("replace updates anime when sucessful")
    void replace_UpdateAnime_WhenSucessFul() {

        Assertions.assertThatCode(() -> animeService
                        .replace(AnimePutRequestBodyUtil.createAnimePutRequestBody()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete remove anime when sucessful")
    void delete_RemovesAnime_WhenSucessFul() {

        Assertions.assertThatCode(() -> animeService
                .delete(1L)).doesNotThrowAnyException();

    }
}
