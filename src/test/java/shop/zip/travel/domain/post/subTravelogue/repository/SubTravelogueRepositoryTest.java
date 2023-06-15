package shop.zip.travel.domain.post.subTravelogue.repository;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.domain.post.subTravelogue.entity.SubTravelogue;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.entity.Travelogue;
import shop.zip.travel.domain.post.travelogue.repository.TravelogueRepository;
import shop.zip.travel.global.config.QuerydslConfig;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Import(QuerydslConfig.class)
class SubTravelogueRepositoryTest {

  @Autowired
  private SubTravelogueRepository subTravelogueRepository;

  @Autowired
  private TravelogueRepository travelogueRepository;

  @Autowired
  private MemberRepository memberRepository;

  @BeforeEach
  void setUp() {
    Member member = memberRepository.save(DummyGenerator.createMember());
    Travelogue travelogue = travelogueRepository.save(
        DummyGenerator.createNotPublishedTravelogueWithSubTravelogues(new ArrayList<>(), member));
  }

  @Test
  void test_save_subTravelogue() {
    SubTravelogue subTravelogue = DummyGenerator.createSubTravelogue(1);

    subTravelogueRepository.save(subTravelogue);
  }

}