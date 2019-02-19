package sample;

import com.sun.tools.corba.se.idl.constExpr.Times;
import javafx.util.Pair;
import sample.models.Room;
import sample.models.Section;
import sample.models.Timeslot;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Time;
import java.util.*;

public class ConstraintGraph {
    List<Section> sections;// internally has candidate instructors(value list for each node in the const. graph)
    List<Room> rooms;
    public ConstraintGraph(List<Section> sections,List<Room> rooms) {
        this.sections = sections;
        this.rooms = rooms;
       // for(Section section: sections) System.out.println(section.instructorRoomPair);
//        System.out.println(sections);
//        System.out.println(rooms);
//        for(Section section: sections){
//            System.out.println(section);
//            System.out.println(section.instructorList);
//        }
    }
    void buildGraph(){
        assignValues();
       // System.exit(0);
        distributeRooms();

    }

    void assignValues(){
        boolean flag = false;
        while (true){
            flag =false;
            for(int i =0;i<sections.size();i++){
                Section chosen;
                if(!allAssigned()){
                    chosen = getMRV();
                   // System.out.println("CHOSE "+chosen);
                }

                else{
                    System.out.println("ALL ASSIGNED");
                    break;
                }
                int randIndexFree = chosen.getRandomIndexFree();
                if(randIndexFree==-1){
                    reinit();
                    flag = true;
                    break;
                }
                String chosenPair  = chosen.candidateInstructorsTimeSlots.get(randIndexFree);
                reserveSlot(chosenPair);
                chosen.instructorRoomPair = chosenPair;
            }
            if(!flag)
                break;
        }


    }

    void reinit(){
        for(Section section: sections){
            for(int i =0;i<section.reservedPair.size();i++)
                section.reservedPair.set(i,false);
            section.instructorRoomPair = null;
        }
    }


    Section getMRV(){
        Section min=null;
        for(Section section: sections){
            if(section.instructorRoomPair == null){
                min = section;
                break;
            }

        }

        for(Section section: sections){
            if(section.countFree() < min.countFree() && section.instructorRoomPair==null){
                min = section;

            }
        }
        return min;
    }

    boolean allAssigned(){
        for(Section section: sections){
            if(section.instructorRoomPair==null){
                return false;
            }
        }
        return true;
    }



    void reserveSlot(String slot){
        for(Section section: sections){
            for(String slot_code : section.candidateInstructorsTimeSlots){
                if(slot_code.split(",")[0].equals(slot.split(",")[0])
                        && Timeslot.codeToSlot(slot_code).confilcts(Timeslot.codeToSlot(slot))){
                    section.reservedPair.set(section.candidateInstructorsTimeSlots.indexOf(slot_code),true);
                }
            }
        }
    }

    void print(){
        for(Section section: sections){
            System.out.println(section.course.name+":"+section.id);
            System.out.println(section.reservedPair);
            System.out.println(section.candidateInstructorsTimeSlots);
        }
    }

    void distributeRooms(){
        List<Room> lectures = new ArrayList<>();
        List<Room> labs = new ArrayList<>();
        for(Room room: rooms){
            if(room.name.contains("LECT"))
                lectures.add(room);
            else labs.add(room);
        }
        for(Section section: sections){
            Room chosenRoom;
            Random random = new Random();
            if(section.course.type==0){
                chosenRoom = lectures.get(random.nextInt(lectures.size()));
            }
            else{
                chosenRoom = labs.get(random.nextInt(labs.size()));
            }

            section.room = chosenRoom;
        }
    }




}
