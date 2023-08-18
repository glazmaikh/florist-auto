package helpers;

public enum CurrencyType {
    EUR("€"),
    USD("$"),
    KZT("₸"),
    RUB("₽");

    private final String symbol;
    CurrencyType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}