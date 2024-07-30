package cs2024.inf.CineView.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Setter
public class GenericPageableList {
    List<Object> results;
    Pageable pageable;

    public GenericPageableList(List<Object> results, Pageable pageable) {
        this.results = results;
        this.pageable = pageable;
    }
}