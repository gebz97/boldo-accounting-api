package eg.boldo.accounting.currency.service;

import eg.boldo.accounting.currency.dto.ExchangeRateRequest;
import eg.boldo.accounting.currency.dto.ExchangeRateResponse;
import eg.boldo.accounting.currency.model.Currency;
import eg.boldo.accounting.currency.model.ExchangeRate;
import eg.boldo.accounting.currency.repository.CurrencyRepository;
import eg.boldo.accounting.currency.repository.ExchangeRateRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExchangeRateService {

        private final ExchangeRateRepository exchangeRateRepository;
        private final CurrencyRepository currencyRepository;

        public Optional<ExchangeRateResponse> getByBaseTargetAndDate(String base, String target, LocalDate date) {
                Optional<Currency> baseRes = currencyRepository.findByName(base);
                Optional<Currency> targetRes = currencyRepository.findByName(target);
                if (targetRes.isEmpty() || baseRes.isEmpty()) {
                        return Optional.empty();
                }
                Optional<ExchangeRate> exchangeRateRes = exchangeRateRepository
                                .findByBaseCurrencyAndTargetCurrencyAndRateDate(
                                                baseRes.get(), targetRes.get(), date);

                if (exchangeRateRes.isEmpty()) {
                        return Optional.empty();
                }

                return exchangeRateRes.map(this::mapToResponse);
        }

        public ExchangeRateResponse create(ExchangeRateRequest request) {
                Currency base = currencyRepository.findById(request.getBaseCurrencyCode())
                                .orElseThrow(
                                                () -> new EntityNotFoundException("Base currency not found: "
                                                                + request.getBaseCurrencyCode()));
                Currency target = currencyRepository.findById(request.getTargetCurrencyCode())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Target currency not found: " + request.getTargetCurrencyCode()));

                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setBaseCurrency(base);
                exchangeRate.setTargetCurrency(target);
                exchangeRate.setRate(request.getRate());
                exchangeRate.setRateDate(
                                request.getRateDate() != null ? request.getRateDate() : java.time.LocalDate.now());

                ExchangeRate saved = exchangeRateRepository.save(exchangeRate);
                return mapToResponse(saved);
        }

        @Transactional(readOnly = true)
        public ExchangeRateResponse getById(Long id) {
                ExchangeRate rate = exchangeRateRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Exchange rate not found with id: " + id));
                return mapToResponse(rate);
        }

        @Transactional(readOnly = true)
        public List<ExchangeRateResponse> getAll() {
                return exchangeRateRepository.findAll()
                                .stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        public ExchangeRateResponse update(Long id, ExchangeRateRequest request) {
                ExchangeRate existing = exchangeRateRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Exchange rate not found with id: " + id));

                Currency base = currencyRepository.findById(request.getBaseCurrencyCode())
                                .orElseThrow(
                                                () -> new EntityNotFoundException("Base currency not found: "
                                                                + request.getBaseCurrencyCode()));
                Currency target = currencyRepository.findById(request.getTargetCurrencyCode())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Target currency not found: " + request.getTargetCurrencyCode()));

                existing.setBaseCurrency(base);
                existing.setTargetCurrency(target);
                existing.setRate(request.getRate());

                if (request.getRateDate() != null) {
                        existing.setRateDate(request.getRateDate());
                }

                ExchangeRate updated = exchangeRateRepository.save(existing);
                return mapToResponse(updated);
        }

        @Transactional
        public ExchangeRateResponse updateOnDate(String baseCurrencyCode,
                        String targetCurrencyCode,
                        LocalDate rateDate,
                        ExchangeRateRequest request) {
                Currency base = currencyRepository.findById(baseCurrencyCode)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Base currency not found: " + baseCurrencyCode));
                Currency target = currencyRepository.findById(targetCurrencyCode)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Target currency not found: " + targetCurrencyCode));

                ExchangeRate existing = exchangeRateRepository
                                .findByBaseCurrencyAndTargetCurrencyAndRateDate(base, target, rateDate)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                String.format("Exchange rate not found for %s/%s on %s",
                                                                baseCurrencyCode, targetCurrencyCode, rateDate)));

                // only update mutable fields (here: rate)
                existing.setRate(request.getRate());

                ExchangeRate updated = exchangeRateRepository.save(existing);
                return mapToResponse(updated);
        }

        public void delete(Long id) {
                if (!exchangeRateRepository.existsById(id)) {
                        throw new EntityNotFoundException("Exchange rate not found with id: " + id);
                }
                exchangeRateRepository.deleteById(id);
        }

        /*
         * Flexible Queries
         */

        @Transactional(readOnly = true)
        public List<ExchangeRateResponse> getByBaseCurrency(String baseCurrencyCode) {
                Currency base = currencyRepository.findById(baseCurrencyCode)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Base currency not found: " + baseCurrencyCode));

                return exchangeRateRepository.findByBaseCurrency(base)
                                .stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<ExchangeRateResponse> getByTargetCurrency(String targetCurrencyCode) {
                Currency target = currencyRepository.findById(targetCurrencyCode)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Target currency not found: " + targetCurrencyCode));

                return exchangeRateRepository.findByTargetCurrency(target)
                                .stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<ExchangeRateResponse> getByDateRange(LocalDate start, LocalDate end) {
                return exchangeRateRepository.findByRateDateBetween(start, end)
                                .stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<ExchangeRateResponse> getByBaseAndTarget(String baseCurrencyCode, String targetCurrencyCode) {
                Currency base = currencyRepository.findById(baseCurrencyCode)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Base currency not found: " + baseCurrencyCode));
                Currency target = currencyRepository.findById(targetCurrencyCode)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Target currency not found: " + targetCurrencyCode));

                return exchangeRateRepository.findByBaseCurrencyAndTargetCurrency(base, target)
                                .stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<ExchangeRateResponse> getByBaseTargetAndDateRange(
                        String baseCurrencyCode, String targetCurrencyCode, LocalDate start, LocalDate end) {

                Currency base = currencyRepository.findById(baseCurrencyCode)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Base currency not found: " + baseCurrencyCode));
                Currency target = currencyRepository.findById(targetCurrencyCode)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Target currency not found: " + targetCurrencyCode));

                return exchangeRateRepository
                                .findByBaseCurrencyAndTargetCurrencyAndRateDateBetween(base, target, start, end)
                                .stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        private ExchangeRateResponse mapToResponse(ExchangeRate exchangeRate) {
                return new ExchangeRateResponse(
                                exchangeRate.getId(),
                                exchangeRate.getBaseCurrency().getCurrencyCode(),
                                exchangeRate.getTargetCurrency().getCurrencyCode(),
                                exchangeRate.getRate(),
                                exchangeRate.getRateDate());
        }

        @Transactional(readOnly = true)
        public List<ExchangeRateResponse> getByDate(LocalDate date) {
                return exchangeRateRepository.findByRateDate(date)
                                .stream().map(this::mapToResponse).collect(Collectors.toList());
        }
}
