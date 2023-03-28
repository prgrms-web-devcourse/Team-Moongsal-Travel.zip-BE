package shop.zip.travel.domain.post.travelogue.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
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

  private Travelogue travelogue;
  private Travelogue tempTravelogue;
  private List<SubTravelogue> subTravelogues;
  private SubTravelogue tempSubTravelogues;
  private Travelogue notPublishedTravelogue;
  private Member member;

  @BeforeEach
  void setUp() {
    member = memberRepository.save(DummyGenerator.createMember());
    travelogue = travelogueRepository.save(DummyGenerator.createTravelogue(member));
    tempTravelogue = travelogueRepository.save(DummyGenerator.createTempTravelogue(member));
    notPublishedTravelogue = travelogueRepository.save(
        DummyGenerator.createNotPublishedTravelogue(member));
  }

  @Test
  @DisplayName("임시로 저장한 게시물을 발행 시도하면 실패한다.")
  void test_fail_publish() {
    assertThatThrownBy(
        () -> traveloguePublishService.publish(tempTravelogue.getId(), member.getId()))
        .isInstanceOf(InvalidPublishTravelogueException.class);
  }

  @Test
  @DisplayName("작성을 완료한 게시물을 발행 시도하면 성공한다.")
  void test_success_publish() {
    notPublishedTravelogue.addSubTravelogue(DummyGenerator.createSubTravelogue(2));

    traveloguePublishService.publish(notPublishedTravelogue.getId(), member.getId());

    Travelogue findTravelogue = travelogueRepository.findById(notPublishedTravelogue.getId()).
        get();

    boolean actualPublishStatus = true;
    assertThat(actualPublishStatus).isEqualTo(findTravelogue.getIsPublished());
  }
}