package shop.zip.travel.domain.suggestion.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import shop.zip.travel.domain.suggestion.entity.Suggestion;

@Repository
public interface SuggestionRepository extends CrudRepository<Suggestion, String> {

  List<Suggestion> getSuggestionByMemberId(Long memberId);
}
