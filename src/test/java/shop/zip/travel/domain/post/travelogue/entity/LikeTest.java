package shop.zip.travel.domain.post.travelogue.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;

class LikeTest {

  private Member member;
  private Travelogue travelogue;

  @BeforeEach
  void setUp() {
    member = DummyGenerator.createMember();
    travelogue = DummyGenerator.createTravelogue(member);
  }

  @Test
  @DisplayName("Travelogue 와 Member 객체로 좋아요 객체를 생성할 수 있다.")
  void create_like() {
    Like like = new Like(travelogue, member);

    Travelogue expectedTravelogue = like.getTravelogue();
    Member expectedMember = like.getMember();

    assertThat(expectedMember).isEqualTo(member);
    assertThat(expectedTravelogue).isEqualTo(travelogue);
  }

  @Test
  @DisplayName("Travelogue 없이 좋아요 객체를 생성할 수 없다.")
  void create_fail_without_travelogue() {
    assertThatThrownBy(() -> new Like(null, member))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("Member 없이 좋아요 객체를 생성할 수 없다.")
  void create_fail_without_member() {
    assertThatThrownBy(() -> new Like(travelogue, null))
        .isInstanceOf(IllegalArgumentException.class);
  }

}