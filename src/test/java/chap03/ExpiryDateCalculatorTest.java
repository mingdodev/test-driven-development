package chap03;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class ExpiryDateCalculatorTest {

    @Test
    void 만원_납부하면_한_달_뒤가_만료일이_된다() {

        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2025, 4, 22))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2025,5,22)
        );
    }

    @Test
    void 납부일과_한_달_뒤_일자가_같지_않다() {
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2025, 1, 31))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2025, 2, 28)
        );
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2025, 5, 31))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2025, 6, 30)
        );
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2024, 1, 31))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2024, 2, 29)
        );
    }

    @Test
    void 첫_납부일과_만료일_일자가_다를때_만원_납부() {
        PayData payData = PayData.builder()
                .firstBillingDate(LocalDate.of(2019,1,31))
                .billingDate(LocalDate.of(2019,2,28))
                .payAmount(10_000)
                .build();

        assertExpiryDate(payData, LocalDate.of(2019,3,31));

        PayData payData2 = PayData.builder()
                .firstBillingDate(LocalDate.of(2019,1,30))
                .billingDate(LocalDate.of(2019,2,28))
                .payAmount(10_000)
                .build();

        assertExpiryDate(payData2, LocalDate.of(2019,3,30));

        PayData payData3 = PayData.builder()
                .firstBillingDate(LocalDate.of(2019,5,31))
                .billingDate(LocalDate.of(2019,6,30))
                .payAmount(10_000)
                .build();

        assertExpiryDate(payData3, LocalDate.of(2019,7,31));
    }

    private void assertExpiryDate(PayData payData, LocalDate expectedExpiryDate) {
        ExpiryDateCalculator cal = new ExpiryDateCalculator();
        LocalDate realExpiryDate = cal.calculateExpiryDate(payData);

        assertEquals(realExpiryDate, expectedExpiryDate);
    }
}
