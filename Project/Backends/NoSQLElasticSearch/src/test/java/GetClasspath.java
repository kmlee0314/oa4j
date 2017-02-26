import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by vogler on 2/23/2017.
 */
public class GetClasspath {
    public static void main(String[] args)  {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader)cl).getURLs();
        for(URL url: urls) if ( !url.getFile().startsWith("/C:/Program%20Files")) System.out.println(url.getFile().substring(1));
    }
}
