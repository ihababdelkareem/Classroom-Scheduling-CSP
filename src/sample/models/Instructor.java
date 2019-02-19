package sample.models;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

public class Instructor {

    public List<Course> preferedCourses;
    public List<Section> assignedSections;
    public int id;
    public String name;
    public Button button = new Button("show talbe");

    public Instructor(int id, String name) {
        this.id = id;
        this.name = name;
        preferedCourses = new ArrayList<>();
        assignedSections = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Instructor{" +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public void printAssignedSections(){
        System.out.println(this.name);
        for(Section section: assignedSections){
            System.out.println( section.course.name+":"+section.id+" "+Timeslot.codeToSlot(section.instructorRoomPair));
        }
    }

    public Instructor getCopy(){
        return new Instructor(id,name);
    }

    public List<Course> getPreferedCourses() {
        return preferedCourses;
    }

    public List<Section> getAssignedSections() {
        return assignedSections;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Button getButton() {
        return button;
    }

    public void setPreferedCourses(List<Course> preferedCourses) {
        this.preferedCourses = preferedCourses;
    }

    public void setAssignedSections(List<Section> assignedSections) {
        this.assignedSections = assignedSections;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setButton(Button button) {
        this.button = button;
    }
}
