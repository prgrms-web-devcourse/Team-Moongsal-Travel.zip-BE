package shop.zip.travel.domain.suggestion.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.suggestion.entity.Suggestion;
import shop.zip.travel.domain.suggestion.repository.SuggestionRepository;

@Service
@Transactional(readOnly = true)
public class SuggestionService {

  private final SuggestionRepository suggestionRepository;

  public SuggestionService(SuggestionRepository suggestionRepository) {
    this.suggestionRepository = suggestionRepository;
  }

  @Transactional
  public void save(Travelogue travelogue, Member member) {
    Suggestion suggestion = new Suggestion(travelogue, member);

    suggestionRepository.save(suggestion);
  }

  public void findAll(Long userId) {

  }
}