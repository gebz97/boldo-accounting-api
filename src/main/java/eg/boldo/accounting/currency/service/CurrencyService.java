package eg.boldo.accounting.currency.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import eg.boldo.accounting.currency.dto.CurrencyDto;
import eg.boldo.accounting.currency.model.Currency;
import eg.boldo.accounting.currency.repository.CurrencyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public Optional<CurrencyDto> findByName(String name) {
        return currencyRepository.findByName(name)
                .map(this::toDto);
    }

    public Optional<CurrencyDto> findByCode(String code) {
        return currencyRepository.findById(code)
                .map(this::toDto);
    }

    public List<CurrencyDto> findAll() {
        return currencyRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public CurrencyDto create(CurrencyDto request) {
        Currency saved = currencyRepository.save(fromDto(request));
        return toDto(saved);
    }

    public Optional<CurrencyDto> update(String code, CurrencyDto updatedCurrency) {
        return currencyRepository.findById(code)
                .map(existing -> {
                    existing.setName(updatedCurrency.getName());
                    existing.setDecimalPlaces(updatedCurrency.getDecimalPlaces());
                    Currency saved = currencyRepository.save(existing);
                    return toDto(saved);
                });
    }

    public boolean delete(String code) {
        if (currencyRepository.existsById(code)) {
            currencyRepository.deleteById(code);
            return true;
        }
        return false;
    }

    private Currency fromDto(CurrencyDto dto) {
        Currency currency = new Currency();
        currency.setCurrencyCode(dto.getCurrencyCode());
        currency.setName(dto.getName());
        return currency;
    }

    private CurrencyDto toDto(Currency currency) {
        return new CurrencyDto(
                currency.getCurrencyCode(),
                currency.getName(),
                currency.getDecimalPlaces());
    }
}

// package eg.boldo.accounting.currency.service;

// import java.util.Optional;

// import org.springframework.stereotype.Service;

// import eg.boldo.accounting.currency.dto.CurrencyDto;
// import eg.boldo.accounting.currency.model.Currency;
// import eg.boldo.accounting.currency.repository.CurrencyRepository;
// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class CurrencyService {

// private final CurrencyRepository currencyRepository;

// public Optional<CurrencyDto> findByName(String name) {
// Optional<Currency> currencyOptional = currencyRepository.findByName(name);
// if (currencyOptional.isEmpty()) {
// return Optional.empty();
// }

// return Optional.of(
// new CurrencyDto(currencyOptional., name)
// );
// }
// }
