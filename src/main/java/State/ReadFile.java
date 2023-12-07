package State;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReadFile {

    public String ReadInTextFile(String filename) {
        String read = null;
        try {
            Path fileName = Path.of(filename);

            if(Files.exists(fileName)) {
                read = Files.readString(fileName);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return read;
    }

    public XmlMap ReadInXmlFile(String filename) {
        XmlMap wmap = new XmlMap();

        try {

            Path fileName = Path.of(filename);

            if(Files.exists(fileName)) {
                File xmlfile = new File(filename);
                JAXBContext jaxbContext = JAXBContext.newInstance(XmlMap.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                wmap = (XmlMap) jaxbUnmarshaller.unmarshal(xmlfile);
            }
        } catch (Exception e) {
            wmap.setError(true);
        }
        if(!wmap.isError()) {
            //System.out.println(wmap);
            return wmap;
        }
        return wmap;
    }

}
