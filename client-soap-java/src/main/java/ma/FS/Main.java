package ma.FS;

import proxy.BanqueService;
import proxy.BanqueWS;

public class Main {
    public static void main(String[] args) {
        BanqueService proxy = new BanqueWS().getBanqueServicePort();
        System.out.println(proxy.conversionEuroToDH(90));

    }
}