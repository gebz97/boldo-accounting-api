package eg.boldo.accounting.currency.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "currencies")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Currency {
    @Id
    @Column(name = "currency_code", length = 3, nullable = false, unique = true)
    private String currencyCode;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "decimal_places", nullable = false)
    private int decimalPlaces = 2;
}
