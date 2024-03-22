public class Rower extends Pojazd implements Parkowalny {
    private Garaz garaz;

    public Rower(String nazwa) {
        super(nazwa);
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
