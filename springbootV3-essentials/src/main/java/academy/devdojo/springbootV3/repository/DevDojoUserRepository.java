package academy.devdojo.springbootV3.repository;

import academy.devdojo.springbootV3.domain.DevDojoUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevDojoUserRepository extends JpaRepository<DevDojoUser, Long> {

    DevDojoUser findByUserName(String username);
}
