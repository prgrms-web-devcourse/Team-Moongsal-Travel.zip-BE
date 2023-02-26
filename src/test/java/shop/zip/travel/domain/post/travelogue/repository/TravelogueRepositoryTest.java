package shop.zip.travel.domain.post.travelogue.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import shop.zip.travel.domain.member.entity.Member;
import shop.zip.travel.domain.member.repository.MemberRepository;
import shop.zip.travel.domain.post.travelogue.DummyGenerator;
import shop.zip.travel.domain.post.travelogue.dto.TravelogueSimple;
import shop.zip.travel.global.config.QuerydslConfig;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(QuerydslConfig.class)
class TravelogueRepositoryTest {

	@Autowired
	private TravelogueRepository travelogueRepository;

	@Autowired
	private MemberRepository memberRepository;

	@BeforeEach
	void setUp() {
		Member member = new Member("user@gmail.com", "password123!", "nickname",1998);
		memberRepository.save(member);
		travelogueRepository.save(DummyGenerator.createTravelogue(member));
		travelogueRepository.save(DummyGenerator.createTravelogue(member));
		travelogueRepository.save(DummyGenerator.createTravelogue(member));
		travelogueRepository.save(DummyGenerator.createTravelogue(member));
	}

	@Test
	@DisplayName("전체 게시물 리스트를 페이지로 가져올 수 있다.")
	void test_get_all_travelogue() {
		PageRequest pageRequest = PageRequest.of(
			0,
			2,
			Sort.by(Sort.Direction.DESC, "createDate")
		);
		Slice<TravelogueSimple> expected = travelogueRepository.findAllBySlice(pageRequest);

		int actualLength = 2;
		assertThat(expected.getSize()).isEqualTo(actualLength);
		assertThat(expected.hasNext()).isTrue();
	}

}