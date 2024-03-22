public abstract class Pojazd {
    private static int globalId = 1;
    private final int id;
    private final String name;

    public Pojazd(String name) {
        this.id = Pojazd.globalId++;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}