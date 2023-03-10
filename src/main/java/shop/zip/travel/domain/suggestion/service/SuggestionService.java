package shop.zip.travel.domain.suggestion.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueCustomSlice;
import shop.zip.travel.domain.post.travelogue.dto.res.TravelogueSimpleRes;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.service.TravelogueService;
import shop.zip.travel.domain.suggestion.entity.Suggestion;
import shop.zip.travel.domain.suggestion.repository.SuggestionRepository;

@Service
@Transactional(readOnly = true)
public class SuggestionService {

  private final Logger log = LoggerFactory.getLogger(Suggestion.class);

  private final SuggestionRepository suggestionRepository;
  private final TravelogueService travelogueService;

  public SuggestionService(SuggestionRepository suggestionRepository,
      TravelogueService travelogueService) {
    this.suggestionRepository = suggestionRepository;
    this.travelogueService = travelogueService;
  }

  @Transactional
  public void save(Travelogue travelogue, Long memberId) {
    Suggestion suggestion = new Suggestion(travelogue, memberId);

    suggestionRepository.save(suggestion);
  }

  @Transactional
  public TravelogueCustomSlice<TravelogueSimpleRes> findByMemberId(Long memberId, Pageable pageable) {
    List<Suggestion> suggestions = suggestionRepository.getSuggestionByMemberId(memberId);
    Map<String, Long> map = new HashMap<>();

    map.put("대한민국", 1L);

    suggestions.forEach(suggestion-> {
      System.out.println("suggestions 만들어짐 ");
      String keyword = suggestion.getTravelogue().getCountry().getName();
      map.put(keyword, map.getOrDefault(keyword, 0L) + 1);
    });

    Comparator<Entry<String, Long>> comparator = new Comparator<Entry<String, Long>>() {
      @Override
      public int compare(Entry<String, Long> e1, Entry<String, Long> e2) {
        return e1.getValue().compareTo(e2.getValue());
      }
    };

    Entry<String, Long> maxEntry = Collections.max(map.entrySet(), comparator);
    log.error("maxEntry.getValue() : " + maxEntry.getValue());

    return travelogueService.search(maxEntry.getKey(), pageable);
  }
}