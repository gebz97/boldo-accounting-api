package eg.boldo.accounting.currency.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
    name = "exchange_rates",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"base_currency", "target_currency", "rate_date"}
        )
    }
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exchange_rate_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_currency", nullable = false)
    private Currency baseCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_currency", nullable = false)
    private Currency targetCurrency;

    @Column(name = "rate", precision = 24, scale = 12, nullable = false)
    private BigDecimal rate;

    @Column(name = "rate_date", nullable = false)
    private LocalDate rateDate;
}
