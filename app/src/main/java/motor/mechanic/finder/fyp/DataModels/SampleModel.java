package motor.mechanic.finder.fyp.DataModels;

public class SampleModel {

    private String id, name, pic;

    public SampleModel() {
    }

    public SampleModel(String id, String name, String pic) {
        this.id = id;
        this.name = name;
        this.pic = pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
