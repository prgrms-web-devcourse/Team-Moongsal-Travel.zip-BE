package shop.zip.travel.domain.post.travelogue.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.exception.InvalidPublishTravelogueException;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;

@SpringBootTest
@Transactional
class TraveloguePublishServiceTest {

  @Autowired
  private TraveloguePublishService traveloguePublishService;

  @Autowired
  private TravelogueRepository travelogueRepository;

  @Autowired
  private MemberRepository memberRepository;

  private Travelogue tempTravelogue;
  private Member member;

  @BeforeEach
  void setUp() {
    member = memberRepository.save(DummyGenerator.createMember());
  }

  @Test
  @DisplayName("임시로 저장한 게시물을 발행 시도하면 실패한다.")
  void test_fail_publish() {
    tempTravelogue = travelogueRepository.save(
        DummyGenerator.createTravelogueWithSubTravelogues(new ArrayList<>(), member)
    );

    assertThatThrownBy(
        () -> traveloguePublishService.publish(tempTravelogue.getId(), member.getId()))
        .isInstanceOf(InvalidPublishTravelogueException.class);
  }

  @Test
  @DisplayName("작성을 완료한 게시물을 발행 시도하면 성공한다.")
  void test_success_publish() {

    Travelogue notPublishedTravelogue = travelogueRepository.save(
        DummyGenerator.createNotPublishedTravelogueWithSubTravelogues(
            List.of(
                DummyGenerator.createSubTravelogue(1),
                DummyGenerator.createSubTravelogue(2)
            ),
            member));

    traveloguePublishService.publish(notPublishedTravelogue.getId(), member.getId());

    Travelogue findTravelogue = travelogueRepository.findById(notPublishedTravelogue.getId()).
        get();

    boolean actualPublishStatus = true;
    assertThat(actualPublishStatus).isEqualTo(findTravelogue.isPublished());
  }
}