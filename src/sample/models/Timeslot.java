package sample.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Timeslot implements Comparable<Timeslot> {
        /*
        Timeslots are atomic units of
        Type(Lab,Lecture),
        Day(s) SMW/TR or a single day for each lab,
        Time-interval (1230 - 1400 , 0800 - 0900 , 1400 - 1700 .. )
        SMTWR -> "01234"
         */


    public String[] daysList = {"SMW","TR","S","M","W","T","R"};
    public static int[] TR_Times = {80,93,110,123,140,153,170};

    public int start,end; //24 hr format
    public int days; //0: SMW , 1 : TR , 2,3,4,5,6: SMWTR ( 1 day for each lab)

    public Timeslot( int start, int end, int days) {
        this.start = start;
        this.end = end;
        this.days = days;
    }

    public boolean confilcts(Timeslot t){
        //different days dont conflict
        if(     this.days==0&&t.days==1 ||
                this.days==0&&t.days==5 ||
                this.days==0&&t.days==6 ||
                this.days==1&&t.days==0 ||
                this.days==1&&t.days==2 ||
                this.days==1&&t.days==3 ||
                this.days==1&&t.days==4 ||
                this.days==5&&t.days==0 ||
                this.days==6&&t.days==0 ||
                this.days==2&&t.days==1 ||
                this.days==3&&t.days==1 ||
                this.days==4&&t.days==1
                ) return false;

        //same days, check overlapping of intervals(quick maffs)
        return Math.max(this.end,t.end) - Math.min(this.start,t.start) < (this.end - this.start) + (t.end - t.start);
    }

    public static List<Timeslot> generateAllLabSlots(){
        ArrayList<Timeslot> slots = new ArrayList<>();
        for(int i =2;i<=6;i++){
            if(i==2||i==3||i==4) slots.add(new Timeslot(140,170,i));
            else if (i==5||i==6) {
                slots.add(new Timeslot(80,110,i));
                slots.add(new Timeslot(110,140,i));
                slots.add(new Timeslot(140,170,i));
            }

        }

        return slots;
    }


    public static List<Timeslot> generateAllLectureSlots(){
        ArrayList<Timeslot> slots = new ArrayList<>();
        for(int i =0;i<=1;i++){
            if(i==0){
                for(int time=80;time<=160;time+=10){
                    slots.add(new Timeslot(time,time+10,0));
                }
            }
            else if(i==1){
                for(int indx=0;indx<TR_Times.length-1;indx++)
                    slots.add(new Timeslot(TR_Times[indx],TR_Times[indx+1],1));

            }
        }
        return slots;
    }

    @Override
    public String toString() {
        return this.daysList[this.days] + ","+this.start+"->"+this.end;
    }

    public String  getCode(){
        return (this.days+","+this.start+","+this.end);
    }

    public static Timeslot codeToSlot(String code){
        //parses[0] is the instructor code , used in other places
        String[] parsed = code.split(",");
        int days = Integer.parseInt(parsed[1]);
        int start = Integer.parseInt(parsed[2]);
        int end = Integer.parseInt(parsed[3]);
        return new Timeslot(start,end,days);
    }

    public boolean isConsecutiveWith(Timeslot t){
        //if days arent different , check if start = end
        if(  !(  this.days==0&&t.days==1 ||
                this.days==0&&t.days==5 ||
                this.days==0&&t.days==6 ||
                this.days==1&&t.days==0 ||
                this.days==1&&t.days==2 ||
                this.days==1&&t.days==3 ||
                this.days==1&&t.days==4 ||
                this.days==5&&t.days==0 ||
                this.days==6&&t.days==0 ||
                this.days==2&&t.days==1 ||
                this.days==3&&t.days==1 ||
                this.days==4&&t.days==1)
                ) return this.start==t.end || t.start==this.end;
        return false;
    }

    public Timeslot hasConsecutive(List<Timeslot> timeslots){
        for(Timeslot timeslot: timeslots){
            if(this.isConsecutiveWith(timeslot)&& this != timeslot)
                return timeslot;
        }
        return null;
    }

    public static boolean hasNConsecutive(List<Timeslot> timeslots,int N){
        List<Timeslot> s = new ArrayList<>();
        List<Timeslot> m =  new ArrayList<>();
        List<Timeslot> w = new ArrayList<>();
        List<Timeslot> t =  new ArrayList<>();
        List<Timeslot> r = new ArrayList<>();
        for(Timeslot timeslot: timeslots){
            if(timeslot.days==0||timeslot.days==2)s.add(timeslot);
            if(timeslot.days==0||timeslot.days==3)m.add(timeslot);
            if(timeslot.days==0||timeslot.days==4)w.add(timeslot);
            if(timeslot.days==1||timeslot.days==5)t.add(timeslot);
            if(timeslot.days==1||timeslot.days==6)r.add(timeslot);
        }
        Collections.sort(s);
        Collections.sort(m);
        Collections.sort(w);
        Collections.sort(t);
        Collections.sort(r);
        boolean s4 = false;
        boolean m4 = false;
        boolean w4 = false;
        boolean t4 = false;
        boolean r4 = false;

        int foundN = 1;
        for(int i =0;i<s.size()-1;i++){
            if(s.get(i).isConsecutiveWith(s.get(i+1)))foundN++;
        }
        if(foundN>=N) s4=true;

        foundN = 1;
        for(int i =0;i<m.size()-1;i++){
            if(m.get(i).isConsecutiveWith(m.get(i+1)))foundN++;
        }
        if(foundN>=N) m4=true;

        foundN = 1;
        for(int i =0;i<w.size()-1;i++){
            if(w.get(i).isConsecutiveWith(w.get(i+1)))foundN++;
        }
        if(foundN>=N) w4=true;

        foundN = 1;
        for(int i =0;i<t.size()-1;i++){
            if(t.get(i).isConsecutiveWith(t.get(i+1)))foundN++;
        }
        if(foundN>=N) t4=true;

        foundN = 1;
        for(int i =0;i<r.size()-1;i++){
            if(r.get(i).isConsecutiveWith(r.get(i+1)))foundN++;
        }
        if(foundN>=N) r4=true;





        return s4||m4||w4||t4||r4;

    }

    @Override
    public int compareTo(Timeslot o) {
        if(this.start>o.start)return 1;
        else if(this.start < o.start) return -1;
        return 0;
    }

    public static boolean hasDayOff(List<Timeslot> timeslots){
        List<Integer> days = new ArrayList<>();
        for(Timeslot timeslot: timeslots)days.add(timeslot.days);
        if(!days.contains(0)){
            if(!days.contains(2) || !days.contains(3)|| !days.contains(4)) return true;
        }
        if(!days.contains(1)){
            if(!days.contains(5) || !days.contains(6)) return true;
        }
        return false;

    }

    public String[] getDaysList() {
        return daysList;
    }

    public static int[] getTR_Times() {
        return TR_Times;
    }

    public String getStart() {
        return start%10==0? start/10+":00" : start/10+":30";
    }

    public String getEnd() {
        return end%10==0? end/10+":00" : end/10+":30";
    }

    public String getDays() {
        return daysList[days];
    }

    public void setDaysList(String[] daysList) {
        this.daysList = daysList;
    }

    public static void setTR_Times(int[] TR_Times) {
        Timeslot.TR_Times = TR_Times;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setDays(int days) {
        this.days = days;
    }


}
