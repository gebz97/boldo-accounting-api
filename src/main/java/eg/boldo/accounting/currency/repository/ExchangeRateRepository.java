package eg.boldo.accounting.currency.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import eg.boldo.accounting.currency.model.Currency;
import eg.boldo.accounting.currency.model.ExchangeRate;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    Optional<ExchangeRate> findByBaseCurrencyAndTargetCurrencyAndRateDate(
            Currency baseCurrency, Currency targetCurrency, LocalDate rateDate);

    List<ExchangeRate> findByBaseCurrency(Currency baseCurrency);

    List<ExchangeRate> findByTargetCurrency(Currency targetCurrency);

    List<ExchangeRate> findByRateDateBetween(LocalDate start, LocalDate end);

    List<ExchangeRate> findByBaseCurrencyAndTargetCurrency(Currency baseCurrency, Currency targetCurrency);

    List<ExchangeRate> findByBaseCurrencyAndRateDateBetween(Currency baseCurrency, LocalDate start, LocalDate end);

    List<ExchangeRate> findByTargetCurrencyAndRateDateBetween(Currency targetCurrency, LocalDate start, LocalDate end);

    List<ExchangeRate> findByBaseCurrencyAndTargetCurrencyAndRateDateBetween(
            Currency baseCurrency, Currency targetCurrency, LocalDate start, LocalDate end);

    @Query(value = "SELECT * FROM exchange_rates e " +
            "WHERE e.base_currency = :base AND e.target_currency = :target " +
            "ORDER BY e.rate_date DESC " +
            "LIMIT :n", nativeQuery = true)
    List<ExchangeRate> findTopNByBaseCurrencyAndTargetCurrencyOrderByRateDateDesc(
            @Param("base") Currency baseCurrency,
            @Param("target") Currency targetCurrency,
            @Param("n") int n);
}