package sample;

import sample.models.*;

import java.util.*;

public class Genetics {
    public Schedule finalSchedule;
    List<Schedule> population;
    final double MUTATION_RATE = 0.1;
    int optFor;
    public Genetics(List<Schedule> population, int optFor) {
        this.optFor = optFor;
        this.population = population;
        Collections.sort(population);
for(int i =0;i<300;i++) {
    population = generateOffspring(population);
    System.out.println(population.get(population.size()-1).fitness);
    System.out.flush();
}
Collections.sort(population);
this.finalSchedule=population.get(population.size()-1);
System.out.println(finalSchedule.fitness);
    }
    List<Schedule> generateOffspring(List<Schedule> population) {
        Collections.sort(population);
        List<Schedule> offspring = new ArrayList<>();
        Schedule elite = population.get(population.size() - 1);
        offspring.add(elite);
        List<Integer> parent_choice_index = new ArrayList<>();
        for(int i =0;i<population.size();i++){
            for(int j =0;j<1+(int)(Math.sqrt(i));j++)parent_choice_index.add(i);
        }
        for(int i =0;i<300;i++){
            Random random = new Random();
            int p1index,p2index;
            Schedule parent1;
            Schedule parent2;
           do {
               p1index = parent_choice_index.get(random.nextInt(parent_choice_index.size()));
               p2index = parent_choice_index.get(random.nextInt(parent_choice_index.size()));
                parent1 = population.get(p1index);
                parent2 = population.get(p2index);
           }while (parent1==parent2);
            Schedule child = crossover(parent1,parent2);
            offspring.add(child);
        }
        Collections.sort(offspring);
        for(int i =0;i<(int)(MUTATION_RATE*1.0*offspring.size());i++){
            Random random = new Random();
            int mutationIndex = random.nextInt(offspring.size());
            Schedule mutated = offspring.get(mutationIndex);
            mutated = mutate(mutated);
            offspring.set(mutationIndex,mutated);

        }
        return offspring;
    }

    public Schedule crossover(Schedule schedule1, Schedule schedule2){
        List<Section> sections = new ArrayList<>();

        Random random = new Random();
        int firstHalf = random.nextInt();
        for(int i =0;i<schedule1.sections.size();i++){
           int c = random.nextInt(2);
           if(c == 1) sections.add(schedule1.sections.get(i).getCopy());
           else sections.add(schedule2.sections.get(i).getCopy());
        }

        List<Instructor> instructors = new ArrayList<>();
        for(Instructor instructor:schedule1.instructors)instructors.add(instructor.getCopy());
        for(Section section: sections){
            String instructorRoomPair = section.instructorRoomPair;
            Instructor instructor = instructors.get(Integer.parseInt(instructorRoomPair.split(",")[0]));
            section.instructor = instructor;
            Timeslot timeslot = Timeslot.codeToSlot(instructorRoomPair);
            instructor.assignedSections.add(section);
            section.timeslot = timeslot;
        }

        return new Schedule(instructors,sections,optFor);
    }
    Schedule mutate(Schedule schedule) {
           Random random = new Random();
           int sectionIdToChange = random.nextInt(schedule.sections.size());
           Section sectionToModify = schedule.sections.get(sectionIdToChange);
    //    //System.out.println("CHANGING "+ sectionToModify.course.name+": "+ sectionToModify.id);
      //  //System.out.println("OLD ROOM : "+sectionToModify.room.name);
        Set<Room> roomSet = population.get(random.nextInt(population.size())).roomTimeslotHashMap.keySet();
        ArrayList<Room> roomList = new ArrayList<>(roomSet);
        for(Room room: roomList){
            if(sectionToModify.course.type==1 && room.name.contains("LAB") || sectionToModify.course.type==0 && room.name.contains("LECT"))sectionToModify.room=room;
        }
      //  //System.out.println("NEW ROOM : "+sectionToModify.room.name);
      //  //System.out.println("OLD DOC : "+sectionToModify.instructor.name);

        sectionToModify.instructor.assignedSections.remove(sectionToModify);
           sectionToModify.instructor = sectionToModify.instructorList.get(random.nextInt(sectionToModify.instructorList.size()));
           sectionToModify.instructor.assignedSections.add(sectionToModify);
      //  //System.out.println("NEW DOC : "+sectionToModify.instructor.name);

       // //System.out.println("OLD SLOT "+sectionToModify.timeslot);
        if(sectionToModify.course.type==0) sectionToModify.timeslot = Timeslot.generateAllLectureSlots().get(random.nextInt(Timeslot.generateAllLectureSlots().size()));
           else sectionToModify.timeslot = Timeslot.generateAllLabSlots().get(random.nextInt(Timeslot.generateAllLabSlots().size()));
     //   //System.out.println("NEW SLOT "+sectionToModify.timeslot);


           return new Schedule(schedule.instructors,schedule.sections,schedule.optimizeFor);
    }

    Schedule getBest(){
        return population.get(population.size() - 1);
    }
}
