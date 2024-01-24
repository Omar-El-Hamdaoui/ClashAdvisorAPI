public class Movie {
    private final String name;
    private final int dateOfRelease;
    private int note;
    private final String kind;

    public Movie(String name,int dateOfRealease,int note,String kind) {
        this.name = name;
        this.dateOfRelease=dateOfRealease;
        this.note=note;
        this.kind=kind;
    }
    public String getName() {return name;}
    public int getDateOfRelease() {return dateOfRelease;}
    public int getNote(){return note;}
    public String getKind(){return kind;}
    public void setNote(int Note){
        this.note=note;
    }

}
