public class Movie {
    private final String name;
    private final int dateOfRelease;
    private int note;
    private final String type;

    public Movie(String name,int dateOfRealease,int note,String type) {
        this.name = name;
        this.dateOfRelease=dateOfRealease;
        this.note=note;
        this.type=type;
    }
    public String getName() {return name;}
    public int getDateOfRelease() {return dateOfRelease;}
    public int getNote(){return note;}
    public String getKind(){return type;}
    public void setNote(int Note){
        this.note=note;
    }
    public String toString(){
        return name+"\n"+"Type: "+type+"\n"+"Date of release: "+dateOfRelease+"\n"+"Note :"+note;
    }
}
