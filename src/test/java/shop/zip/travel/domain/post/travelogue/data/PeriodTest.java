package shop.zip.travel.domain.post.travelogue.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PeriodTest {

    @DisplayName("startDate 가 endDate 보다 나중일 경우 IllegalArgumentException 을 던진다")
    @Test
    void verifyFailTest() {
        LocalDate startDate = LocalDate.of(2023, 2, 10);
        LocalDate endDate = LocalDate.of(2023, 2, 1);

        assertThatThrownBy(() -> new Period(startDate, endDate))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("startDate 가 endDate 와 같거나 이전 일 경우 Period 객체를 생성한다.")
    @ParameterizedTest
    @ValueSource(ints = {10, 19})
    void verifySuccessTest(int dayOfMonth) {

        LocalDate startDate = LocalDate.of(2023, 2, dayOfMonth);
        LocalDate endDate = LocalDate.of(2023, 2, 19);

        Period period = new Period(startDate, endDate);

        assertThat(period.getStartDate()).isEqualTo(startDate);
        assertThat(period.getEndDate()).isEqualTo(endDate);
    }

    @DisplayName("여행 마지막 날짜가 현재날짜보다 나중일 경우 IllegalArgumentException 을 던진다")
    @Test
    void verifyEndDateIsBeforeToday() {
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);

        assertThatThrownBy(() -> new Period(startDate, endDate))
            .isInstanceOf(IllegalArgumentException.class);
    }

}