package common.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;



public class Dragon implements Comparable<Dragon>, Serializable {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long age; //Значение поля должно быть больше 0
    private Integer wingspan; //Значение поля должно быть больше 0, Поле не может быть null
    private Double weight; //Значение поля должно быть больше 0, Поле не может быть null
    private DragonCharacter character; //Поле не может быть null
    private DragonCave cave; //Поле может быть null

    public Dragon(Long id, String name, Coordinates coordinates, java.time.LocalDateTime creationDate, long age, Integer wingspan, Double weight, DragonCharacter character, DragonCave cave){
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.age = age;
        this.wingspan = wingspan;
        this.weight = weight;
        this.character = character;
        this.cave = cave;
    }

    public Dragon(LocalDateTime creationDate){
        this.creationDate = creationDate;
    }

    /**
     * @return ID дракона
     */

    public Long getID(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    /**
     * @return Имя дракона
     */

    public String getName(){
        return name;
    }

    /**
     * @return координаты дракона
     */
    public Coordinates getCoordinates(){
        return coordinates;
    }

    /**
     * @return дата занесения в протокол
     */

    public java.time.LocalDateTime getCreationDate(){
        return creationDate;
    }

    /**
     * @return возраст дракона
     */

    public long getAge(){
        return (int)age;
    }

    /**
     * @return сила размаха крыльев
     */

    public Integer getWingspan(){
        return wingspan;
    }

    /**
     * @return вес дракона
     */

    public Double getWeight(){
        return weight;
    }

    /**
     * @return характер дракона
     */

    public DragonCharacter getCharacter(){
        return character;
    }

    /**
     * @return пещера дракона
     */

    public DragonCave getCave(){
        return cave;
    }

    //@Override
    //public int compareTo(Dragon d){
    //    return age.compareTo(d.getAge());
    //}

    @Override
    public String toString(){
        String info = "";
        info += "\u001B[37m"+"\u001B[36m"+"Дракон № " + id + "\u001B[36m"+"\u001B[37m";
        info += " (дата занесения в протокол " + creationDate.toLocalDate() + " " + creationDate.toLocalTime() + ")";
        info += "\n Имя дракона: " + name;
        info += "\n Местоположение: " + coordinates;
        info += "\n Возраст: " + age;
        info += "\n Размах крыльев: " + wingspan;
        info += "\n Вес: " + weight;
        info += "\n Характер: " + character;
        info += "\n Пещера дракона: " + cave;
        return info;
    }

    @Override
    public boolean equals(Object f) {
        if (this == f) return true;
        if (f == null || getClass() != f.getClass()) return false;
        Dragon that = (Dragon) f;
        return name.equals(that.getName()) && coordinates.equals(that.getCoordinates()) && creationDate.equals(that.getCreationDate()) &&
                (age == that.getAge()) && (wingspan == that.getWingspan()) &&
                (weight == that.getWeight()) && (character == that.getCharacter()) &&
                (cave == that.getCave());
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, age, wingspan, weight, character, cave);
    }

    @Override
    public int compareTo(Dragon dragon) {
        return getName().compareTo(dragon.getName());
    }

//    @Override
//    public boolean validate(){
//        if (id == null || id <=0) return false;
//        if (name == null || name.isEmpty()) return false;
//        if (coordinates == null) return false;
//        if (creationDate == null) return false;
//        if (age >= 0) return false;
//        if (wingspan >=0 || wingspan == null) return false;
//        if (weight >=0 || weight == null) return false;
//        if (character == null) return false;
//        return true;
//    }


}


