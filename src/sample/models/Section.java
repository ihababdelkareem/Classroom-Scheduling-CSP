package sample.models;
import javafx.util.Pair;

import java.util.*;

public class Section {
    public int id;
    public Course course;
    public Instructor instructor;
    public Timeslot timeslot;
    public Room room;
    public String instructorRoomPair;
    public List<String> candidateInstructorsTimeSlots;
    public List<Instructor> instructorList;
    public List<Boolean> reservedPair;

    public Section(int id) {
        this.id = id;
        candidateInstructorsTimeSlots = new ArrayList<>();
        instructorList = new ArrayList<>();
        reservedPair = new ArrayList<>();
    }

    @Override
    public String toString() {
        return course.name + ":" + this.id;
        // return "id=" + id + " "+course.name + " " + "\n"+candidateInstructors+"\n";
    }

    public void freeAllReservedPairs() {
        for (Boolean pair : this.reservedPair) pair = false;
    }

    public int countFree() {
        int count = 0;
        for (Boolean pair : this.reservedPair) {
            if (pair == false) count++;
        }
        return count;
    }

    public int getRandomIndexFree() {
        int freeCount = countFree();
        if (countFree() == 0) return -1;
        Random random = new Random();
        int randInt = random.nextInt(freeCount);
        int next = 0;

        for (int i = 0; i < reservedPair.size(); i++) {
            if (reservedPair.get(i) == false) {
                if (next == randInt) return i;
                else next++;
            }
        }
        return -1;
    }

    public Section getCopy() {
        int id = this.id;
        Course course = this.course;
        Room room = this.room;
        String instructorRoomPair = this.instructorRoomPair;
        List<String> candidateInstructorsTimeSlots = this.candidateInstructorsTimeSlots;
        List<Instructor> instructorList = this.instructorList;
        List<Boolean> reservedPair = this.reservedPair;

        Section section = new Section(id);
        section.course = course;
        section.room = room;
        section.instructorRoomPair = instructorRoomPair;
        section.candidateInstructorsTimeSlots = candidateInstructorsTimeSlots;
        section.instructorList = instructorList;
        section.reservedPair = reservedPair;
        return section;

    }
}