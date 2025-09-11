package eg.boldo.accounting.currency.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eg.boldo.accounting.currency.dto.ExchangeRateRequest;
import eg.boldo.accounting.currency.dto.ExchangeRateResponse;
import eg.boldo.accounting.currency.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/exchange-rates")
@RequiredArgsConstructor
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    @GetMapping("/all/today")
    public List<ExchangeRateResponse> getAllToday() {
        return exchangeRateService.getByDate(LocalDate.now());
    }

    @GetMapping("/all/by-date/{date}")
    public ResponseEntity<List<ExchangeRateResponse>> getAllByDate(@PathVariable String date) {
        try {
            LocalDate parsed = LocalDate.parse(date);
            return ResponseEntity.ok(exchangeRateService.getByDate(parsed));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{base}/to/{target}/at/{date}")
    public ResponseEntity<ExchangeRateResponse> getByBaseTargetAndDate(@PathVariable String baseVar, @PathVariable String targetVar,
            @PathVariable String dateVar) {
        try {
            LocalDate parsed = LocalDate.parse(dateVar);
            return ResponseEntity.ok(exchangeRateService.getByBaseTargetAndDate(baseVar, targetVar, parsed).get());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping
    public ResponseEntity<ExchangeRateResponse> postOne(@RequestBody ExchangeRateRequest request) {
        ExchangeRateResponse response = exchangeRateService.create(request);
        return ResponseEntity.ok(response);
    }


}
