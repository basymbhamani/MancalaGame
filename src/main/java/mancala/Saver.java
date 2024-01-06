package mancala;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class Saver {
    private static final String ASSETS_FOLDER = "assets/";
    

    public static void saveObject(Serializable toSave, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ASSETS_FOLDER + filename))) {
            oos.writeObject(toSave);
        }
    }

    public static Serializable loadObject(String filename) throws IOException, ClassNotFoundException{
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ASSETS_FOLDER + filename))) {
            return (Serializable) ois.readObject();
        }
    }
}
