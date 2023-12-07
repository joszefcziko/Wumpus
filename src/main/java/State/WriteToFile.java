package State;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class WriteToFile {

    public boolean saveToTextFile(String filename, String data) {

        try {
            FileWriter file = new FileWriter(filename);
            BufferedWriter output = new BufferedWriter(file);

            output.write(data);
            output.close();
            return true;
        }

        catch (Exception e) {
            e.getStackTrace();
        }
        return false;
    }

    public boolean saveGameState(XmlMap gameBoard, String fileName) {
        try {
            JAXBContext context = JAXBContext.newInstance(XmlMap.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File file = new File(fileName);
            marshaller.marshal(gameBoard, file);
            return true;
        } catch (JAXBException e) {
            e.printStackTrace();
            return false;
        }
    }
}
