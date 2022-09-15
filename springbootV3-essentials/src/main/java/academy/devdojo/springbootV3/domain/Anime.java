package academy.devdojo.springbootV3.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

//Define get/set/equals and hashcode/toString
@Data
//Define que os atributos serão add no constructor
@AllArgsConstructor
public class Anime {
    private Long id;
    private String name;
}
