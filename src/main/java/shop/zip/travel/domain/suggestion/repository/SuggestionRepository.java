package shop.zip.travel.domain.suggestion.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import shop.zip.travel.domain.suggestion.entity.Suggestion;

public interface SuggestionRepository extends CrudRepository<Suggestion, String> {

  List<Suggestion> getSuggestionByMemberId(Long memberId);
}
