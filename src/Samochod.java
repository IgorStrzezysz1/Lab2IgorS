public class Samochod extends Pojazd implements Parkowalny{
    private final RodzajPaliwa rodzajPaliwa;
    private Garaz garaz;

    public Samochod(String nazwa, int rodzajPaliwa) {
        super(nazwa);
        this.rodzajPaliwa = () -> rodzajPaliwa;
    }

    public RodzajPaliwa paliwo() {
        return rodzajPaliwa;
    }

    @Override
    public void ustawGaraz(Garaz garaz) {
        this.garaz = garaz;
    }

    @Override
    public Garaz getGaraz() {
        return garaz;
    }
}
