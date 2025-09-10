package eg.boldo.accounting.currency.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import eg.boldo.accounting.currency.model.Currency;

public interface CurrencyRepository extends JpaRepository<Currency,String> {
    Optional<Currency> findByName(String name);
}
