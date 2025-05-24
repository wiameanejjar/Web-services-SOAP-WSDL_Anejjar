
package proxy;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour ConversionEuroToDH complex type.</p>
 * 
 * <p>Le fragment de sch�ma suivant indique le contenu attendu figurant dans cette classe.</p>
 * 
 * <pre>{@code
 * <complexType name="ConversionEuroToDH">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="montent" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConversionEuroToDH", propOrder = {
    "montent"
})
public class ConversionEuroToDH {

    protected double montent;

    /**
     * Obtient la valeur de la propri�t� montent.
     * 
     */
    public double getMontent() {
        return montent;
    }

    /**
     * D�finit la valeur de la propri�t� montent.
     * 
     */
    public void setMontent(double value) {
        this.montent = value;
    }

}
