import com.suai.multiuserpaint.server.Room;
import com.suai.multiuserpaint.server.RoomsList;
import com.suai.multiuserpaint.server.User;
import com.suai.multiuserpaint.server.UserThread;
import com.suai.paintclient.MainWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

public class BasicUnitTest {

    @Test(expected = NullPointerException.class)
    public void test(){
        Room room = new Room(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void test1(){
        UserThread userThread = new UserThread(null);
    }


    @Test(expected = NullPointerException.class)
    public void test3() throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/file_not_found_exception.fxml"));
    }


    @Test(expected = NullPointerException.class)
    public void test4(){
        RoomsList roomslist = new RoomsList();
        roomslist.add(null, null);
    }

}
