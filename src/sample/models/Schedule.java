package sample.models;

import java.util.*;

public class Schedule implements Comparable<Schedule> {
    public List<Instructor> instructors;
    public List<Section> sections;
    public HashMap<Room,List<Timeslot>> roomTimeslotHashMap = new HashMap<>();
    public double fitness;
    public int roomCount;
    public int roomConflictCount;
    public int roomsOutsideMsriCount;
    public int consecutiveCount;
    public int teacherConflictCount;
    public int instructorsWithDaysOffCount;
    public int lecturesAt8;
    public int instructorsWithLoadOutsideRangeCount;
    public double soft_fitness;
    public boolean allhaveDay;
    public int optimizeFor;
    /*
    Optimize for :
    0- All have a day off
    1- Minimize Lectures at 8
    2- Minimize Lectures outside the Masri Building
    3- Minimize lectures of the same level being given at the same time
     */
    public Schedule(List<Instructor> instructors, List<Section> sections, int optimizeFor) {
        this.instructors = instructors;
        this.sections = sections;
        this.optimizeFor = optimizeFor;
        setRoomTimeslotHashMap();
        roomConflicts();
        getRoomsOutsideMasri(false);
        teacherConflicts(false);
        countNConsecutive(4,false);
        instructorsWithDaysOffCount();
        FindCountOflecturesAt8();
        instructorsWithLoadOutsideRange(12,18,false);
        setFitness();
        setSoft_fitness();
    }
    public void roomConflicts(){
        roomTimeslotHashMap.forEach((room,list)->{
            boolean flag = false;
            for(Timeslot timeslot: list){
                for(Timeslot timeslot1: list){
                    if(timeslot.confilcts(timeslot1) && timeslot!=timeslot1){
                        roomConflictCount++;
                        flag=true;
                    }
                    if(flag) break;
                }
            }
        });
    }

    public void getRoomsOutsideMasri(boolean print){
        HashSet<String> uniqueRooms = new HashSet<>();
        roomTimeslotHashMap.forEach((room,list)->{
            if(room.name.contains("OUT")){
                uniqueRooms.add(room.name);
            }
        });
        roomsOutsideMsriCount=uniqueRooms.size();
    }

    public int conflictPerRoom(Room room){
        int count = 0;
        List<Timeslot> timeslots = roomTimeslotHashMap.get(room);
        if(timeslots!=null){
            for(Timeslot timeslot: timeslots){
                for(Timeslot timeslot1: timeslots){
                    if(timeslot.confilcts(timeslot1) && timeslot!=timeslot1){
                        count++;

                    }
                }
            }
        }

        return count;
    }

    public void countNConsecutive(int n,boolean print){
        int counter = 0;
        for(Instructor instructor:instructors){
            List<Timeslot> timeslots = new ArrayList<>();
            for(Section section:instructor.assignedSections)timeslots.add(section.timeslot);
            if(Timeslot.hasNConsecutive(timeslots,n)){
               consecutiveCount++;
                if(print)System.out.println(instructor.name+"<<<<");
            }
        }
    }



    public void teacherConflicts(boolean print){
        Set<Instructor> instructorsConflicting = new HashSet<>();
        for(Instructor instructor:instructors){
            boolean flag = false;
            for(Section section:instructor.assignedSections){
                for(Section section1: instructor.assignedSections){
                    if(section.timeslot.confilcts(section1.timeslot)&&section!=section1){
                        instructorsConflicting.add(instructor);
                        flag=true;
                    }
                    if(flag)break;
                }
            }
        }

        teacherConflictCount=instructorsConflicting.size();
       if(print) for(Instructor instructor: instructorsConflicting) System.out.println(instructor.name);

    }

    public void instructorsWithDaysOffCount(){
        for(Instructor instructor:instructors){
            List<Timeslot> timeslots = new ArrayList<>();
            for(Section section:instructor.assignedSections)timeslots.add(section.timeslot);
            if(Timeslot.hasDayOff(timeslots)){
                instructorsWithDaysOffCount++;
            }
        }
        this.allhaveDay = (this.instructorsWithDaysOffCount == this.instructors.size());
    }

    public int conflictPerInstructor(Instructor instructor) {
        int count = 0;
        for(Section section:instructor.assignedSections){
            for(Section section1: instructor.assignedSections){
                if(section.timeslot.confilcts(section1.timeslot)&&section!=section1){
                    count++;
                }
            }
        }
    return count;
    }

    void setRoomTimeslotHashMap(){
        for(Section section: sections){
            if(!roomTimeslotHashMap.containsKey(section.room)){
                ArrayList<Timeslot> slots = new ArrayList<>();
                slots.add(section.timeslot);
                roomTimeslotHashMap.put(section.room, slots);
                roomCount++;
            }
            else{
                roomTimeslotHashMap.get(section.room).add(section.timeslot);
            }
        }
    }

    public void FindCountOflecturesAt8(){
        for(Section section: sections) {
            if(section.timeslot.start==80)lecturesAt8++;
        }
    }
    public void instructorsWithLoadOutsideRange(int lb,int ub,boolean print) {

        for(Instructor instructor: instructors) {
            int lectureHoursCount =0;
            int totalHours = 0;
            for(Section section:instructor.assignedSections) {
                if(section.course.type==0) {
                    totalHours +=3;
                    lectureHoursCount+=3;
                }
                else totalHours+=2;
            }
            if(totalHours>ub || totalHours<lb || lectureHoursCount<6) {
                instructorsWithLoadOutsideRangeCount++;
                if(print) System.out.println(instructor.name);
            }
        }
    }
    public void setFitness(){
        int totalInstructorConflict = 0;
        final int[] totalRoomConflicts = {0};
        for(Instructor instructor:instructors)totalInstructorConflict += conflictPerInstructor(instructor);
        roomTimeslotHashMap.forEach((room,list)->{
            totalRoomConflicts[0] += conflictPerRoom(room);
        });
        if(optimizeFor==0){
            this.fitness = 1000 - (5*(instructors.size()  - instructorsWithDaysOffCount)+3*totalInstructorConflict + consecutiveCount + 3*totalRoomConflicts[0]+3*instructorsWithLoadOutsideRangeCount);
        }
        else if (optimizeFor == 1){
            this.fitness = 1000 - (3*totalInstructorConflict + consecutiveCount + 3*totalRoomConflicts[0]+3*instructorsWithLoadOutsideRangeCount);
            if(this.fitness==1000.0){
          //      System.out.println("OK HARD , ADDING SOFT");
                this.fitness+=5*(sections.size() - lecturesAt8);
            }
        }
        else if(optimizeFor == 2){
            this.fitness = 1000 - (3*totalInstructorConflict + consecutiveCount + 3*totalRoomConflicts[0]+3*instructorsWithLoadOutsideRangeCount);
            if(this.fitness==1000.0){
           //     System.out.println("OK HARD , ADDING SOFT");
                this.fitness+=5*(roomCount - roomsOutsideMsriCount);
            }
        }

    }
    public void setSoft_fitness(){
        soft_fitness = lecturesAt8+roomsOutsideMsriCount;
    }
   public void printFitnessInfo(){
        System.out.println("ROOM CONFLICTS " + this.roomConflictCount);
        System.out.println("FITNESS "+ this.fitness);
        System.out.println("PROFF CONFLIC "+this.teacherConflictCount);
        teacherConflicts(true);
        System.out.println("OUTSIDE  =  "+this.roomsOutsideMsriCount);
        System.out.println("Room Count  =  "+this.roomCount);
        System.out.println("AT 8   =  "+this.lecturesAt8);
        System.out.println("Days OFF   =  "+this.instructorsWithDaysOffCount);
        System.out.println("4-Consec "+this.consecutiveCount);
        countNConsecutive(4,true);
        System.out.println("Bounds "+this.instructorsWithLoadOutsideRangeCount);
        instructorsWithLoadOutsideRange(12,18,true);
        System.out.println("SOFT FITNESS "+this.soft_fitness);
        System.out.println("ALL DAY OFF? "+this.allhaveDay);
        getRoomsOutsideMasri(true);

    }
    public void printAllDetails(){
        System.out.println("BY Instructor: ");
        for(Instructor instructor:instructors){
            System.out.println(instructor.name);
            for(Section section:instructor.assignedSections){
                System.out.println(section.course.name+":"+section.id+" "+section.timeslot+" "+section.room.name);
            }
            System.out.println("-----------------");
        }

        System.out.println("By Section: ");
        for(Section section:sections){
            System.out.println(section.course.name+":"+section.id+" "+section.instructor.name+" "+section.room.name+" "+section.timeslot);
        }
    }
    @Override
    public int compareTo(Schedule o) {
        //System.out.println(this.fitness + " -> "+o.fitness);
        if(this.fitness > o.fitness){
            return 1;
        }
        else if (this.fitness < o.fitness)
            return -1;
        return 0;
    }



}
