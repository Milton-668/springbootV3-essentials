package academy.devdojo.springbootV3.repository;

import academy.devdojo.springbootV3.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {

    List<Anime> findByName(String name);
    //Procura pelo nome passado, mesmo que não esteja completo
    List<Anime> findAllByNameContainingIgnoreCase(String name);
}
