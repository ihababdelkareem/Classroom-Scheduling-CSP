package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotResult;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import sample.models.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class Main extends Application {


    static HashMap<String,Course> courseNameToObject;
    static List<Section> sections;
    static List<Course> courses ;
    static List<Instructor> instructors;
    static List<Room> roomList;


    static double timeLength = 60;
    static int index = 0;
    ArrayList<Label> labels = new ArrayList<>();
    Label nameI = new Label("");
    Schedule finalS;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    int optimizeFor = 0;


    @Override
    public void start(Stage primaryStage) throws Exception {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        Stage window1 = primaryStage;
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        Label label = new Label("Instructors Timetabling program");
        label.setStyle("-fx-font-weight: bold");
        Button button = new javafx.scene.control.Button("open file");
        final FileChooser fileChooser = new FileChooser();
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    try {
                        //applyDummyAlgorithm();
                        if(rb1.isSelected())
                            optimizeFor = 0;
                        else if(rb2.isSelected())
                            optimizeFor = 1;
                        else
                            optimizeFor = 2;
                        applyIhabsAlgorithm(file);
                        //printAllDetails();
                        showTableStage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        HBox soft = new HBox();


        //
        final ToggleGroup group = new ToggleGroup();

        Label softLabel = new Label("soft constraints:");
        softLabel.setStyle("-fx-font-weight: bold");

        rb1 = new RadioButton("make day off for each instructor");
        rb1.setToggleGroup(group);
        rb1.setSelected(true);

        rb2 = new RadioButton("minimize number of lectures at 8:00");
        rb2.setToggleGroup(group);

        rb3 = new RadioButton("minimize number of rooms at outside masri");
        rb3.setToggleGroup(group);
        //


        vBox.getChildren().addAll(label,button, softLabel,rb1,rb2,rb3);
        Scene scene = new Scene(vBox,400,300);
        window1.setScene(scene);
        window1.setTitle("Instructors Timetabling");
        window1.show();
    }

    private void applyIhabsAlgorithm(File file) throws Exception {
        //int optimizeFor = 0;
        List<Schedule> population = new ArrayList<>();


        for(int i = 0;i<250;i++){
            readInput(file);
            ConstraintGraph graph = new ConstraintGraph(sections,roomList);
            graph.buildGraph();
            sections = graph.sections; // sections assigned times and
            for(Section section: sections){
                String instructorRoomPair = section.instructorRoomPair;
                Instructor instructor = instructors.get(Integer.parseInt(instructorRoomPair.split(",")[0]));
                section.instructor = instructor;
                Timeslot timeslot = Timeslot.codeToSlot(instructorRoomPair);
                instructor.assignedSections.add(section);
                section.timeslot = timeslot;
            }
            Schedule schedule = new Schedule(instructors,sections,optimizeFor);
            population.add(schedule);
        }
        Genetics genetics = new Genetics(population,optimizeFor);
        finalS = genetics.finalSchedule;
    }

    private void showTableStage() {
        VBox vBox = new VBox();
        vBox.setPrefWidth(1500);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,20,10,20));
        hBox.setPrefWidth(1500);
        hBox.setAlignment(Pos.CENTER);
        GridPane gridPane = new GridPane();
        gridPane.setVgap(1);
        gridPane.setHgap(1);

        Label label0 = new Label("day/time");
        label0.setStyle("-fx-font-weight: bold");
        gridPane.add(label0,0,0);
        Label label1 = new Label("8:00");
        label1.setStyle("-fx-font-weight: bold");
        label1.setPrefWidth(timeLength);
        Label label2 = new Label("8:30");
        label2.setStyle("-fx-font-weight: bold");
        label2.setPrefWidth(timeLength);
        Label label3 = new Label("9:00");
        label3.setStyle("-fx-font-weight: bold");
        label3.setPrefWidth(timeLength);
        Label label4 = new Label("9:30");
        label4.setStyle("-fx-font-weight: bold");
        label4.setPrefWidth(timeLength);
        Label label5 = new Label("10:00");
        label5.setStyle("-fx-font-weight: bold");
        label5.setPrefWidth(timeLength);
        Label label6 = new Label("10:30");
        label6.setStyle("-fx-font-weight: bold");
        label6.setPrefWidth(timeLength);
        Label label7 = new Label("11:00");
        label7.setStyle("-fx-font-weight: bold");
        label7.setPrefWidth(timeLength);
        Label label8 = new Label("11:30");
        label8.setStyle("-fx-font-weight: bold");
        label8.setPrefWidth(timeLength);
        Label label9 = new Label("12:00");
        label9.setStyle("-fx-font-weight: bold");
        label9.setPrefWidth(timeLength);
        Label label10 = new Label("12:30");
        label10.setStyle("-fx-font-weight: bold");
        label10.setPrefWidth(timeLength);
        Label label11 = new Label("1:00");
        label11.setStyle("-fx-font-weight: bold");
        label11.setPrefWidth(timeLength);
        Label label12 = new Label("1:30");
        label12.setStyle("-fx-font-weight: bold");
        label12.setPrefWidth(timeLength);
        Label label13 = new Label("2:00");
        label13.setStyle("-fx-font-weight: bold");
        label13.setPrefWidth(timeLength);
        Label label14 = new Label("2:30");
        label14.setStyle("-fx-font-weight: bold");
        label14.setPrefWidth(timeLength);
        Label label15 = new Label("3:00");
        label15.setStyle("-fx-font-weight: bold");
        label15.setPrefWidth(timeLength);
        Label label16 = new Label("3:30");
        label16.setStyle("-fx-font-weight: bold");
        label16.setPrefWidth(timeLength);
        Label label17 = new Label("4:00");
        label17.setStyle("-fx-font-weight: bold");
        label17.setPrefWidth(timeLength);
        Label label18 = new Label("4:30");
        label18.setStyle("-fx-font-weight: bold");
        label18.setPrefWidth(timeLength);
        Label label19 = new Label("5:00");
        label19.setStyle("-fx-font-weight: bold");
        label19.setPrefWidth(timeLength);

        Label label20= new Label(String.format("%-17s","Saturday"));
        label20.setStyle("-fx-font-weight: bold");
        Label label21= new Label(String.format("%-17s","Sunday"));
        label21.setStyle("-fx-font-weight: bold");
        Label label22= new Label(String.format("%-17s","Monday"));
        label22.setStyle("-fx-font-weight: bold");
        Label label23= new Label(String.format("%-17s","Tuesday"));
        label23.setStyle("-fx-font-weight: bold");
        Label label24= new Label(String.format("%-17s","Wednesday"));
        label24.setStyle("-fx-font-weight: bold");
        Label label25= new Label(String.format("%-17s","Thursday"));
        label25.setStyle("-fx-font-weight: bold");

        gridPane.setConstraints(label20,0,1);
        gridPane.setConstraints(label21,0,2);
        gridPane.setConstraints(label22,0,3);
        gridPane.setConstraints(label23,0,4);
        gridPane.setConstraints(label24,0,5);
        gridPane.setConstraints(label25,0,6);

        gridPane.setConstraints(label1,1,0);
        gridPane.setConstraints(label2,2,0);
        gridPane.setConstraints(label3,3,0);
        gridPane.setConstraints(label4,4,0);
        gridPane.setConstraints(label5,5,0);
        gridPane.setConstraints(label6,6,0);
        gridPane.setConstraints(label7,7,0);
        gridPane.setConstraints(label8,8,0);
        gridPane.setConstraints(label9,9,0);
        gridPane.setConstraints(label10,10,0);
        gridPane.setConstraints(label11,11,0);
        gridPane.setConstraints(label12,12,0);
        gridPane.setConstraints(label13,13,0);
        gridPane.setConstraints(label14,14,0);
        gridPane.setConstraints(label15,15,0);
        gridPane.setConstraints(label16,16,0);
        gridPane.setConstraints(label17,17,0);
        gridPane.setConstraints(label18,18,0);
        gridPane.setConstraints(label19,19,0);


        gridPane.getChildren().addAll(label1,label2,label3,label4,label5,label6,label7,label8,label9,label10,label11,label12,label13,label14,label15,label16,label17,label18,label19,label20,label21,label22,label23,label24,label25);
        gridPane.setGridLinesVisible(true);
        showInstructorTable(gridPane,0);

        hBox.getChildren().addAll(gridPane);
        hBox.setStyle("-fx-background-color: white");

        Label title = new Label("Instructors timetables");
        title.setStyle("-fx-font-size: 30");
        HBox titleHbox = new HBox();
        titleHbox.setStyle("-fx-background-color: white");
        titleHbox.setAlignment(Pos.CENTER);
        titleHbox.getChildren().add(title);
        vBox.getChildren().addAll(titleHbox, hBox);

        HBox hbox2 = new HBox();
        Button next = new Button("next Instructor");
        Button prev = new Button("prev Instructor");
        nameI.setStyle("-fx-font-weight: bold");
        nameI.setStyle("-fx-font-size:20px");
        nameI.setTextAlignment(TextAlignment.CENTER);


        prev.setDisable(true);
        next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                index++;
                showInstructorTable(gridPane, index);

                if(index == instructors.size() - 1)
                    next.setDisable(true);

                if(index == 1)
                    prev.setDisable(false);
            }
        });

        prev.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                index--;
                showInstructorTable(gridPane, index);
                if(index == 0)
                    prev.setDisable(true);

                if(index == instructors.size()-2)
                    next.setDisable(false);
            }
        });

        hbox2.getChildren().addAll(prev,nameI,next);
        hbox2.setAlignment(Pos.CENTER);
        hbox2.setSpacing(50);
        vBox.getChildren().add(hbox2);

        //hBox 3
        HBox hBox3 = new HBox();
        hBox3.setAlignment(Pos.CENTER);
        hBox3.setSpacing(20);
        hBox3.setStyle("-fx-background-color: white;-fx-padding: 10");

        TableView instructorsTable = new TableView();
        Label instructorsLabel = new Label("Instructors");
        instructorsLabel.setStyle("-fx-font-weight: bold");
        VBox instructorsVBox = new VBox();
        instructorsVBox.getChildren().addAll(instructorsLabel,instructorsTable);
        TableColumn<Instructor,Integer> tc11 = new TableColumn<Instructor, Integer>("id");
        tc11.setCellValueFactory(new PropertyValueFactory<Instructor, Integer>("id"));
        TableColumn<Instructor,String> tc12 = new TableColumn<Instructor, String>("name");
        tc12.setCellValueFactory(new PropertyValueFactory<Instructor, String>("name"));
        TableColumn actionCol = new TableColumn("Action");
        actionCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
        Callback<TableColumn<Instructor, String>, TableCell<Instructor, String>> cellFactory
                =
                new Callback<TableColumn<Instructor, String>, TableCell<Instructor, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Instructor, String> param) {
                        final TableCell<Instructor, String> cell = new TableCell<Instructor, String>() {

                            final Button btn = new Button("show table");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        showInstructorTable(gridPane, getIndex());
                                        index = getIndex();
                                        if(index == 0) {
                                            prev.setDisable(true);
                                            next.setDisable(false);
                                        }
                                        else if(index == instructors.size() - 1) {
                                            prev.setDisable(false);
                                            next.setDisable(true);
                                        }
                                        else {
                                            prev.setDisable(false);
                                            next.setDisable(false);
                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        actionCol.setCellFactory(cellFactory);
        instructorsTable.getColumns().addAll(tc11, tc12, actionCol);
        instructorsTable.setItems(FXCollections.observableArrayList(instructors));

        TableView roomsTable = new TableView();
        Label roomsLabel = new Label("Rooms");
        roomsLabel.setStyle("-fx-font-weight: bold");
        VBox roomsVBox = new VBox();
        roomsVBox.getChildren().addAll(roomsLabel,roomsTable);
        TableColumn<Room,Integer> tc21 = new TableColumn<Room, Integer>("room id");
        tc21.setCellValueFactory(new PropertyValueFactory<Room, Integer>("roomID"));
        TableColumn<Room,String> tc22 = new TableColumn<Room, String>("room name");
        tc22.setCellValueFactory(new PropertyValueFactory<Room, String>("name"));
        roomsTable.getColumns().addAll(tc21,tc22);
        roomsTable.setItems(FXCollections.observableArrayList(roomList));

        List<Timeslot> timeslots = Timeslot.generateAllLabSlots();
        timeslots.addAll(Timeslot.generateAllLectureSlots());
        TableView timeSlotsTable = new TableView();
        Label timeSlotsLabel = new Label("Time Slots");
        timeSlotsLabel.setStyle("-fx-font-weight: bold");
        VBox timeslotsVBox = new VBox();
        timeslotsVBox.getChildren().addAll(timeSlotsLabel, timeSlotsTable);
        TableColumn<Timeslot,String> tc31 = new TableColumn<Timeslot, String>("days");
        tc31.setCellValueFactory(new PropertyValueFactory<Timeslot, String>("days"));
        TableColumn<Timeslot,String> tc32 = new TableColumn<Timeslot, String>("start");
        tc32.setCellValueFactory(new PropertyValueFactory<Timeslot, String>("start"));
        TableColumn<Timeslot,String> tc33 = new TableColumn<Timeslot, String>("end");
        tc33.setCellValueFactory(new PropertyValueFactory<Timeslot, String>("end"));
        timeSlotsTable.getColumns().addAll(tc31, tc32, tc33);
        timeSlotsTable.setItems(FXCollections.observableArrayList(timeslots));

        TableView coursesTable = new TableView();
        Label coursesLabel = new Label("Courses");
        coursesLabel.setStyle("-fx-font-weight: bold");
        VBox coursesVBox = new VBox();
        coursesVBox.getChildren().addAll(coursesLabel, coursesTable);
        TableColumn<Course,String> tc41 = new TableColumn<Course, String>("course id");
        tc41.setCellValueFactory(new PropertyValueFactory<Course, String>("id"));
        TableColumn<Course,String> tc42 = new TableColumn<Course, String>("course name");
        tc42.setCellValueFactory(new PropertyValueFactory<Course, String>("name"));
        TableColumn<Course,Integer> tc43 = new TableColumn<Course, Integer>("num of sec");
        tc43.setCellValueFactory(new PropertyValueFactory<Course, Integer>("allSections"));
        TableColumn<Course,String> tc44 = new TableColumn<Course, String>("type");
        tc44.setCellValueFactory(new PropertyValueFactory<Course, String>("type"));
        coursesTable.getColumns().addAll(tc41,tc42,tc43,tc44);
        coursesTable.setItems(FXCollections.observableArrayList(courses));



        hBox3.getChildren().addAll(instructorsVBox,roomsVBox,coursesVBox, timeslotsVBox);
        vBox.getChildren().add(hBox3);
        //end hBox3



        vBox.setSpacing(15);


        Scene scene2 = new Scene(vBox, 1300,770);
        Stage window2 = new Stage();

        window2.setTitle("Instructors Tables");

        //window2.setMaximized(true);
        window2.setScene(scene2);
        window2.show();
    }

    private void showInstructorTable(GridPane gridPane, int instructorIndex) {
        int hours = 0;
        gridPane.getChildren().removeAll(labels);
        labels = new ArrayList<>();
        Instructor instructor = finalS.instructors.get(instructorIndex);
        List<Section> sectionsI = instructor.assignedSections;
        int secNum = sectionsI.size();
        for(int i = 0 ; i < secNum ; i++) {
            Section section = sectionsI.get(i);
            if(section.course.type == 0)
                hours+=3;
            else
                hours+=2;
            int start = section.timeslot.start;
            int end = section.timeslot.end;
            int days = section.timeslot.days;

            if(start%10 == 0)
                start = 2*(start/10-8)+1;
            else
                start = 2*(start/10-8)+2;

            if(days == 0) {
                int[] arr = {1,3,5};
                for(int day:arr) {
                    Label label = new Label(section.course.name +":"+ section.id + "    R:" + section.room.roomID);
                    labels.add(label);
                    label.setPrefWidth(timeLength*2);
                    label.setStyle("-fx-border-color: black");
                    label.setStyle("-fx-background-color: greenyellow");
                    label.setTextAlignment(TextAlignment.CENTER);

                    gridPane.add(label,start,day,2,1);
                }
            }
            else if (days == 1) {
                int[] arr = {4,6};
                for(int day:arr) {
                    Label label = new Label(section.course.name +":"+ section.id + "    R:" + section.room.roomID);
                    labels.add(label);
                    label.setPrefWidth(timeLength*3+1);
                    label.setStyle("-fx-border-color: black");
                    label.setStyle("-fx-background-color: aqua");
                    label.setTextAlignment(TextAlignment.CENTER);

                    gridPane.add(label,start,day,3,1);
                }
            }
            else {
                int day;
                if(days == 2)       //s       1
                    day = 1;
                else if(days == 3)  //m       3
                    day = 3;
                else if(days == 4)  //w       5
                    day = 5;
                else if(days == 5)  //t       4
                    day = 4;
                else                //r       6
                    day = 6;
                //Label label = new Label(String.format("%102s",section.course.name +":"+ section.id + "    R:" + section.room.roomID));
                Label label = new Label(section.course.name +":"+ section.id + "    R:" + section.room.roomID);
                labels.add(label);
                label.setPrefWidth(timeLength*6+4);
                label.setStyle("-fx-border-color: black");
                label.setStyle("-fx-background-color: coral");

                gridPane.add(label,start,day,6,1);
            }
        }
        nameI.setText("instructor name: " + instructors.get(instructorIndex).name + "\ninstructor hours: " + hours);
    }

    private void applyDummyAlgorithm() {
        instructors.get(0).assignedSections.add(courses.get(0).allSections.get(0));
        courses.get(0).allSections.get(0).timeslot = new Timeslot(80,90,0);
        courses.get(0).allSections.get(0).room = roomList.get(0);
        instructors.get(0).assignedSections.add(courses.get(1).allSections.get(0));
        courses.get(1).allSections.get(0).timeslot = new Timeslot(90,100,0);
        courses.get(1).allSections.get(0).room = roomList.get(1);
        instructors.get(0).assignedSections.add(courses.get(2).allSections.get(0));
        courses.get(2).allSections.get(0).timeslot = new Timeslot(110,120,0);
        courses.get(2).allSections.get(0).room = roomList.get(2);
        instructors.get(0).assignedSections.add(courses.get(3).allSections.get(0));
        courses.get(3).allSections.get(0).timeslot = new Timeslot(93,110,1);
        courses.get(3).allSections.get(0).room = roomList.get(3);
        instructors.get(0).assignedSections.add(courses.get(4).allSections.get(0));
        courses.get(4).allSections.get(0).timeslot = new Timeslot(110,123,1);
        courses.get(4).allSections.get(0).room = roomList.get(4);
        instructors.get(0).assignedSections.add(courses.get(16).allSections.get(0));
        courses.get(16).allSections.get(0).timeslot = new Timeslot(140,170,3);
        courses.get(16).allSections.get(0).room = roomList.get(5);
        instructors.get(0).assignedSections.add(courses.get(17).allSections.get(0));
        courses.get(17).allSections.get(0).timeslot = new Timeslot(140,170,5);
        courses.get(17).allSections.get(0).room = roomList.get(6);



        instructors.get(1).assignedSections.add(courses.get(0).allSections.get(1));
        courses.get(0).allSections.get(1).timeslot = new Timeslot(90,100,0);
        courses.get(0).allSections.get(1).room = roomList.get(1);
        instructors.get(1).assignedSections.add(courses.get(1).allSections.get(1));
        courses.get(1).allSections.get(1).timeslot = new Timeslot(110,120,0);
        courses.get(1).allSections.get(1).room = roomList.get(2);
        instructors.get(1).assignedSections.add(courses.get(7).allSections.get(1));
        courses.get(7).allSections.get(1).timeslot = new Timeslot(80,93,1);
        courses.get(7).allSections.get(1).room = roomList.get(3);
        instructors.get(1).assignedSections.add(courses.get(8).allSections.get(1));
        courses.get(8).allSections.get(1).timeslot = new Timeslot(110,123,1);
        courses.get(8).allSections.get(1).room = roomList.get(4);
        instructors.get(1).assignedSections.add(courses.get(16).allSections.get(1));
        courses.get(16).allSections.get(1).timeslot = new Timeslot(140,170,2);
        courses.get(16).allSections.get(1).room = roomList.get(5);
        instructors.get(1).assignedSections.add(courses.get(17).allSections.get(1));
        courses.get(17).allSections.get(1).timeslot = new Timeslot(140,170,6);
        courses.get(17).allSections.get(1).room = roomList.get(6);
    }


    static void readInput(File input) throws Exception {
        Scanner s = new Scanner(input);
        int lectureCount = s.nextInt();
        courseNameToObject = new HashMap<>();
        int labCount = s.nextInt();
        s.nextLine();
        courses = new ArrayList<>();
        sections = new ArrayList<>();
        for(int i =0;i<lectureCount+labCount;i++){
            String[] parsed = s.nextLine().split(" ");
            Course course;
            if(i<lectureCount)
                course = new Course(i,parsed[0],0);
            else course = new Course(i,parsed[0],1);

            courseNameToObject.put(parsed[0],course);
            courses.add(course);
            int sectionCount = Integer.parseInt(parsed[1]);
            for(int j=0;j<sectionCount;j++){
                Section section = new Section(j);
                section.course = course;
                course.allSections.add(section);
                sections.add(section);
            }
        }
        int instructorCount = Integer.parseInt(s.nextLine());
        instructors = new ArrayList<>();
        for(int i =0;i<instructorCount;i++){
            String[] line = s.nextLine().split(" ");
            Instructor instructor = new Instructor(i,line[0]);
            instructors.add(instructor);
            for(int k =1;k<line.length;k++){
                Course course = courseNameToObject.get(line[k]);
                instructor.preferedCourses.add(course);
                for(Section section: course.allSections){
                    List<Timeslot> allSlots;
                    if(course.type==0)
                        allSlots = Timeslot.generateAllLectureSlots();
                    else  allSlots = Timeslot.generateAllLabSlots();
                    for(Timeslot slot : allSlots){
                        section.candidateInstructorsTimeSlots.add(instructor.id+","+slot.getCode());
                        section.reservedPair.add(new Boolean(false));
                    }
                    section.instructorList.add(instructor);

                }
            }
        }
        String[] rooms = s.nextLine().split(" ");
        int roomCount = Integer.parseInt(rooms[0]);
        int labRoomCount = Integer.parseInt(rooms[1]);
        String[] outside = s.nextLine().split(" ");
        int roomsOutside = Integer.parseInt(outside[0]);
        int labsOutside = Integer.parseInt(outside[1]);
        int roomsMasri = roomCount - roomsOutside;
        int labsMasri = labCount - labsOutside;
        roomList  = new ArrayList<>();
        int idCount =0;
        for(int i =0;i<roomsMasri;i++)roomList.add(new Room(idCount,("LECT_MASRI_"+(idCount++))));
        for(int i =0;i<labsMasri;i++)roomList.add(new Room(idCount,("LAB_MASRI_"+(idCount++))));
        for(int i =0;i<roomsOutside;i++)roomList.add(new Room(idCount,("LECT_OUT_"+(idCount++))));
        for(int i =0;i<labsOutside;i++)roomList.add(new Room(idCount,("LAB_OUT_"+(idCount++))));

        //
    }

    void printAllDetails() {
        System.out.println("BY Instructor: ");
        for(Instructor instructor:instructors){
            System.out.println(instructor.name);
            for(Section section:instructor.assignedSections) {
                System.out.println(section.course.name+":"+section.id+" "+section.timeslot+" "+section.room.name);
            }
            System.out.println("-----------------");
        }

        /*System.out.println("By Section: ");
        for(Section section:sections){
            System.out.println(section.course.name+":"+section.id+" "+section.instructor.name+" "+section.room.name+" "+section.timeslot);
        }*/
    }



    public static void main(String[] args) {
        launch(args);
    }
}
