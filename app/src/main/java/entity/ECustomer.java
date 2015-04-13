package entity;

/**
 * Created by Pham Anh Tu on 04/13/2015.
 */
public class ECustomer {
    private int Id;
    private String Name;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return this.Name;
    }
}
