public class Motorowka extends Pojazd {
    private final RodzajPaliwa rodzajPaliwa;

    public Motorowka(String nazwa, int rodzajPaliwa) {
        super(nazwa);
        this.rodzajPaliwa = () -> rodzajPaliwa;
    }

    public RodzajPaliwa paliwo() {
        return rodzajPaliwa;
    }
}
