package sample.models;

import java.util.ArrayList;
import java.util.List;

public class Course {
    public int id;
    public String name;
    public int type;//0 lecture,  1 lab
    public List<Section> allSections;

    public Course(int id, String name,int type) {
        this.id = id;
        this.name = name;
        allSections = new ArrayList<>();
        this.type = type;
    }

    @Override
    public String toString() {
        if(this.type==0)
            return "Lecture{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", allSections=" + allSections +
                    '}';
        else return "Lab{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", allSections=" + allSections +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type==0? "Lecture" : "Lab";
    }

    public int getAllSections() {
        return allSections.size();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setAllSections(List<Section> allSections) {
        this.allSections = allSections;
    }
}
