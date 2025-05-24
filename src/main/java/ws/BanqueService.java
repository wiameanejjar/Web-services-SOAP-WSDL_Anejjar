package ws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@WebService(serviceName = "BanqueWS" )
public class BanqueService {
    @WebMethod(operationName = "ConversionEuroToDH")
    public double conversion(@WebParam(name = "montent") double nt){
        return nt*11;
    }
    @WebMethod
    public Compte getCompte(@WebParam(name ="code") int code){
        return new Compte(code,Math.random()*60000, new Date());

    }
    @WebMethod
    public List<Compte> ListComptes(){
        return List.of(
                new Compte(1,Math.random()*60000, new Date()),
                new Compte(2,Math.random()*60000, new Date()),
                new Compte(3,Math.random()*60000, new Date())
        );
    }

}
