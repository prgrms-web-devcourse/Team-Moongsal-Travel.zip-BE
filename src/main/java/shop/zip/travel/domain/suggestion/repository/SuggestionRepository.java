package shop.zip.travel.domain.suggestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.zip.travel.domain.suggestion.entity.Suggestion;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

}
