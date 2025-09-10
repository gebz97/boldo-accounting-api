package eg.boldo.accounting.currency.api;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import eg.boldo.accounting.currency.dto.CurrencyDto;
import eg.boldo.accounting.currency.service.CurrencyService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public List<CurrencyDto> getAll() {
        return currencyService.findAll();
    }

    @GetMapping("/{code}")
    public ResponseEntity<CurrencyDto> getByCode(@PathVariable String code) {
        Optional<CurrencyDto> result = currencyService.findByCode(code);
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<CurrencyDto> getByName(@PathVariable String name) {
        Optional<CurrencyDto> result = currencyService.findByName(name);
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CurrencyDto> create(@RequestBody CurrencyDto currency) {
        CurrencyDto saved = currencyService.create(currency);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{code}")
    public ResponseEntity<CurrencyDto> update(@PathVariable String code,
            @RequestBody CurrencyDto currency) {
        Optional<CurrencyDto> updated = currencyService.update(code, currency);
        return updated.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        boolean deleted = currencyService.delete(code);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}