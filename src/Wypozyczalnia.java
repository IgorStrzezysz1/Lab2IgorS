import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Wypozyczalnia {
    private static final List<Garaz> GARAZE = new ArrayList<>();

    public Wypozyczalnia(int liczbaGarazy) {
        for (int i = 0; i < liczbaGarazy; i++) {
            GARAZE.add(new Garaz());
        }
    }

    private static final Scanner scanner = new Scanner(System.in);

    public void start() throws Exception {
        File xmlFile = new File("file.xml");

        Document doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(xmlFile);

        List<Pojazd> pojazdy = wczytajPojazdyZDokumentuXml(doc);

        while (true) {
            System.out.println("\n\n\n");
            System.out.println("Program wypozyczalnia");
            System.out.println("1 - Dodaj pojazd");
            System.out.println("2 - Usuń pojazd");
            System.out.println("3 - Wypisz pojazdy");
            System.out.println("4 - Sortuj");
            System.out.println("5 - Parkowanie pojazdu");
            System.out.println("Wybierz opcję (1, 2, 3, 4 lub 5): ");
            String opcja = scanner.nextLine();
            if (opcja.equals("1")) {
                dodajPojazdZKonsoli(pojazdy);
            } else if (opcja.equals("2")) {
                usunPojazdZKonsoli(pojazdy);
            } else if (opcja.equals("3")) {
                wypiszPojazdy(pojazdy);
            } else if (opcja.equals("4")) {
                pojazdy = sortujPojazdy(pojazdy);
            } else {
                zaparkujPojazd(pojazdy);
            }
        }
    }

    private void zaparkujPojazd(List<Pojazd> pojazdy) {
        System.out.println("Wybierz pojazd do zaparkowania- podaj ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Optional<Pojazd> szukanyPojazdOpt = pojazdy.stream()
            .filter(pojazd -> pojazd.getId() == id)
            .findFirst();
        if (szukanyPojazdOpt.isPresent() &&
            szukanyPojazdOpt.get() instanceof Parkowalny) {

            if (((Parkowalny) szukanyPojazdOpt.get()).getGaraz() != null) {
                System.out.println("POJAZD JEST JUZ W GARAZU");
                return;
            }

            Optional<Garaz> wolnyGaraz = GARAZE.stream()
                .filter(garaz -> garaz.isGarazJestWolny())
                .findFirst();
            if (wolnyGaraz.isPresent()) {
                ((Parkowalny) szukanyPojazdOpt.get()).ustawGaraz(wolnyGaraz.get());
                wolnyGaraz.get().setGarazJestWolny(false);
                System.out.println("ZAPARKOWANY");
            } else {
                System.out.println("BRAK WOLNYCH GARAŻY");
            }
        } else {
            System.out.println("Pojazd nie jest parkowalny");
        }
    }

    private List<Pojazd> sortujPojazdy(List<Pojazd> pojazdy) {
        System.out.println("SORTOWANIE: 1- parkowane, 2 - typ pojazdu, 3 - nazwa pojazdu, 4- rodzaj paliwa, 5- ilosc paliwa");
        System.out.println("Wybierz sortowanie: ");
        String sortowanie = scanner.nextLine();
        if (sortowanie.equals("2")) {
            return pojazdy.stream()
                .sorted(Comparator.comparing(pojazd -> {
                    if (pojazd instanceof Samochod) {
                        return 1;
                    } else if (pojazd instanceof Rower) {
                        return 2;
                    } else if (pojazd instanceof Hulajnoga) {
                        return 3;
                    } else {
                        return 4;
                    }
                }))
                .collect(Collectors.toList());
        } else if (sortowanie.equals("3")) {
            return pojazdy.stream()
                .sorted(Comparator.comparing(Pojazd::getName))
                .collect(Collectors.toList());
        } else if (sortowanie.equals("4")) {
            return pojazdy.stream()
                .sorted(Comparator.comparing(pojazd -> {
                    if (pojazd instanceof Samochod) {
                        return ((Samochod) pojazd).paliwo().getRodzajPaliwa();
                    } else if (pojazd instanceof Motorowka) {
                        return ((Motorowka) pojazd).paliwo().getRodzajPaliwa();
                    } else {
                        return 999;
                    }
                }))
                .collect(Collectors.toList());
        }
        return pojazdy;
    }

    private void wypiszPojazdy(List<Pojazd> pojazdy) {
        System.out.println("Pojazdy w wypozyczalni:");
        for (Pojazd pojazd : pojazdy) {
            System.out.println("====================================");
            System.out.println("Nazwa: " + pojazd.getName());
            System.out.println("Id: " + pojazd.getId());

            boolean czyZaparkowany = false;
            if (pojazd instanceof Parkowalny) {
                Garaz garaz = ((Parkowalny) pojazd).getGaraz();
                czyZaparkowany = garaz != null;
            }

            System.out.println("Czy zaparkoany? : " + czyZaparkowany);
            System.out.println("====================================");
        }
    }

    private void dodajPojazdZKonsoli(List<Pojazd> pojazdy) {
        System.out.println("Dodawanie pojazdu");
        System.out.println("Wybierz pojazd (1 - samochod, 2 - motorowka, 3 - rower, 4 - hulajnoga");
        String typPojazdu = scanner.nextLine();
        System.out.println("Wpisz nazwę: ");
        String nazwa = scanner.nextLine();

        int paliwo = 0;
        if (typPojazdu.equals("1") || typPojazdu.equals("2")) {
            System.out.println("Wybierz paliwo (1 - 5)");
            System.out.println("Wybrane paliwo: ");
            paliwo = Integer.parseInt(scanner.nextLine());
        }

        Pojazd pojazd = switch (typPojazdu) {
            case "1" -> new Samochod(nazwa, paliwo);
            case "2" -> new Motorowka(nazwa, paliwo);
            case "3" -> new Rower(nazwa);
            default -> new Hulajnoga(nazwa);
        };

        pojazdy.add(pojazd);
    }

    private void usunPojazdZKonsoli(List<Pojazd> pojazdy) {
        System.out.println("Usuwanie pojazdu");
        System.out.println("Podaj ID pojazdu: ");
        int id = Integer.parseInt(scanner.nextLine());
        Optional<Pojazd> szukanyPojazdOpt = pojazdy.stream()
            .filter(pojazd -> pojazd.getId() == id)
            .findFirst();
        if (szukanyPojazdOpt.isPresent()) {
            pojazdy.remove(szukanyPojazdOpt.get());
            System.out.println("Usunięto pojazd o ID: " + id);
        } else {
            System.out.println("Nie znaleziono pojazdu o podanym ID!");
        }
    }

    private List<Pojazd> wczytajPojazdyZDokumentuXml(Document doc) {
        List<Pojazd> pojazdy = new ArrayList<>();

        NodeList samochody = doc.getElementsByTagName("samochod");

        for (int i = 0; i < samochody.getLength(); i++) {
            Element item = (Element) samochody.item(i);
            String nazwa = item.getElementsByTagName("nazwa").item(0).getTextContent();
            String nazwaPaliwa = item.getElementsByTagName("rodzajPaliwa").item(0).getTextContent();
            int rodzajPaliwa = Integer.parseInt(nazwaPaliwa);

            Samochod samochod = new Samochod(nazwa, rodzajPaliwa);
            pojazdy.add(samochod);
        }

        NodeList motorowki = doc.getElementsByTagName("motorowka");
        Element motorowkaItem = (Element) motorowki.item(0);
        String nazwaMotorowki = motorowkaItem.getElementsByTagName("nazwa").item(0).getTextContent();
        int rodzajPaliwaMotorowki = Integer.parseInt(motorowkaItem.getElementsByTagName("rodzajPaliwa")
            .item(0).getTextContent());

        Motorowka motorowka = new Motorowka(nazwaMotorowki, rodzajPaliwaMotorowki);
        pojazdy.add(motorowka);

        NodeList rowery = doc.getElementsByTagName("rower");
        Element rowerItem = (Element) rowery.item(0);
        String nazwaRoweru = rowerItem.getElementsByTagName("nazwa").item(0).getTextContent();

        Rower rower = new Rower(nazwaRoweru);
        pojazdy.add(rower);

        NodeList hulajnogi = doc.getElementsByTagName("hulajnoga");
        Element hulajnogaItem = (Element) hulajnogi.item(0);
        String nazwaHulajnogi = hulajnogaItem.getElementsByTagName("nazwa").item(0).getTextContent();
        Hulajnoga hulajnoga = new Hulajnoga(nazwaHulajnogi);
        pojazdy.add(hulajnoga);

        return pojazdy;
    }
}
