package shop.zip.travel.domain.suggestion.service;

import static java.util.stream.Collectors.groupingBy;

import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.suggestion.SuggestionCreateDto;
import shop.zip.travel.domain.suggestion.entity.Suggestion;
import shop.zip.travel.domain.suggestion.repository.SuggestionRepository;

@Service
@Transactional(readOnly = true)
public class SuggestionService {

  private static final String DEFAULT_COUNTRY = "대한민국";

  private final SuggestionRepository suggestionRepository;

  public SuggestionService(SuggestionRepository suggestionRepository) {
    this.suggestionRepository = suggestionRepository;
  }

  @Transactional
  //@Async를 대신 달 수 있다
  public void save(SuggestionCreateDto suggestionDto) {
    Suggestion suggestion = new Suggestion(
        suggestionDto.travelogueId(),
        suggestionDto.countryName(),
        suggestionDto.title(),
        suggestionDto.thumbnail(),
        suggestionDto.totalCost(),
        suggestionDto.memberId());

    suggestionRepository.save(suggestion);

    // 백그라운드 실행
    // async 대신 별도의 스레드를 생성함
    // 예외가 발생할수 있는 경우, 수행이 오래걸리는 경우, 또는 Redis가 터질 때 에는
    // redis에 문제가 생길수 있기때문에 그런 상황에서는 async또는 별도의 스레드를 생성하여 해결할 수 있다

//    ExecutorService executor = Executors.newFixedThreadPool(1);
//    Callable<Suggestion> callable = () -> {
//      if(true) {
//        System.out.println("if문 전: 레디스에 저장");
//        throw new RuntimeException("exception 던지");
//      }
//      System.out.println("if문 후 : 레디스에 저장");
//      return suggestionRepository.save(suggestion);
//    };
//
//    executor.submit(callable);
  }

  public String getTopSuggestionCountry(Long memberId) {
    return suggestionRepository.getSuggestionByMemberId(memberId)
        .stream()
        .collect(groupingBy(Suggestion::getCountryName, Collectors.counting()))
        .entrySet()
        .stream()
        .max(Entry.comparingByValue())
        .map(Entry::getKey)
        .orElse(DEFAULT_COUNTRY);
  }

}