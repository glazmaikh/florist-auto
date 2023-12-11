package helpers;

public enum CurrencyType {
    EUR("€", "EUR"),
    USD("$", "USD"),
    KZT("₸", "KZT"),
    RUB("₽", "RUB");

    private final String symbol;
    private final String name;
    CurrencyType(String symbol, String name) {
        this.symbol = symbol;
        this.name = name();
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }
}