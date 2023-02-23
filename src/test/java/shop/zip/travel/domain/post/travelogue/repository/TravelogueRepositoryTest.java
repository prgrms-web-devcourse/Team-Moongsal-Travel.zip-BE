package shop.zip.travel.domain.post.travelogue.repository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import shop.zip.travel.global.config.QuerydslConfig;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(QuerydslConfig.class)
class TravelogueRepositoryTest {



}