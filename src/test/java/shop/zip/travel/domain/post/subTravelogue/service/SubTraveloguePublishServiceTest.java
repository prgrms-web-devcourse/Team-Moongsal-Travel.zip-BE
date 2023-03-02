package shop.zip.travel.domain.post.subTravelogue.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.subTravelogue.repository.SubTravelogueRepository;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.exception.InvalidPublishTravelogueException;

@SpringBootTest
@Transactional
class SubTraveloguePublishServiceTest {

  @Autowired
  private SubTravelogueRepository subTravelogueRepository;

  @Autowired
  private SubTraveloguePublishService subTraveloguePublishService;

  private List<SubTravelogue> subTravelogues;

  @BeforeEach
  void setUp() {
    subTravelogues = List.of(
        DummyGenerator.createSubTravelogue(),
        DummyGenerator.createTempSubTravelogue()
    );

    subTravelogueRepository.saveAll(subTravelogues);
  }

  @Test
  @DisplayName("내용이 다 채워지지 않은 서브 트래블로그를 발행 시도 할 수 없도록 한다.")
  void test_verify_subTravelogues() {
    assertThatThrownBy(() ->
        subTraveloguePublishService.verifySubTravelogues(subTravelogues))
        .isInstanceOf(InvalidPublishTravelogueException.class);
  }
}