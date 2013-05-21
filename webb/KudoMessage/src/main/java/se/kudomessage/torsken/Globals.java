package se.kudomessage.torsken;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.icefaces.application.PortableRenderer;

@ApplicationScoped
@ManagedBean
public class Globals {
    public static PortableRenderer pr;
    public static String email = "";
    public static String accessToken = "";
}