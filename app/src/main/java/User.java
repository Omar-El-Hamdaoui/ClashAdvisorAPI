public class User {
    private final int id;
    private final String Name;
    private static int counter=0;

    public User(int id, String name) {
        this.id = ++counter;
        Name = name;
    }


}
