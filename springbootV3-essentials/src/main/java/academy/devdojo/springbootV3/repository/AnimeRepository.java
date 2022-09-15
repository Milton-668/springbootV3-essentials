package academy.devdojo.springbootV3.repository;

import academy.devdojo.springbootV3.domain.Anime;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeRepository {
    List<Anime> listAll();
}
