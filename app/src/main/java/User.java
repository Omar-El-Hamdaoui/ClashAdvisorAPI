import java.util.ArrayList;
import java.util.List;

public class User {
    private final int id;
    private final String name;
    private static int counter=0;

    public User(int id, String name) {
        this.id = ++counter;
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public int getId(){
        return id;
    }


}
