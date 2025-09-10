package eg.boldo.accounting.currency.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExchangeRateResponse {
    private Long id;
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal rate;
    private LocalDate rateDate;
}
