package apro.test.app;


import com.j256.ormlite.field.DatabaseField;

public class CarData {
    @DatabaseField(allowGeneratedIdInsert=true)
    Integer id;

    @DatabaseField
    String make;

    @DatabaseField
    String model;

    @DatabaseField
    Integer year;

    @DatabaseField
    Integer mileage;

    @DatabaseField
    String engType;

    @DatabaseField
    Integer engDisp;

    @DatabaseField
    String transType;

    @DatabaseField
    String bodyType;

    @DatabaseField
    Integer price;

    @DatabaseField
    String color;

    @DatabaseField
    String img;

    @Override
    public String toString() {
        return "CarData{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                '}';
    }

    public CarData(String make, String model, Integer year, Integer mileage, String engType,
                   Integer engDisp, String transType, String bodyType, Integer price,
                   String color, String img) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.engType = engType;
        this.engDisp = engDisp;
        this.transType = transType;
        this.bodyType = bodyType;
        this.price = price;
        this.color = color;
        this.img = img;
    }

    public CarData() {
        //must be empty, required by Ormlite
    }
}


