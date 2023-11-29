package Models;

import javafx.beans.property.SimpleStringProperty;

public class Schedule {
    private final SimpleStringProperty doctorFIO;
    private final SimpleStringProperty position;
    private final SimpleStringProperty mondaySchedule;
    private final SimpleStringProperty tuesdaySchedule;
    private final SimpleStringProperty wednesdaySchedule;
    private final SimpleStringProperty thursdaySchedule;
    private final SimpleStringProperty fridaySchedule;
    private final SimpleStringProperty saturdaySchedule;
    private final SimpleStringProperty sundaySchedule;

    public Schedule(String doctorFIO, String position, String mondaySchedule, String tuesdaySchedule, String wednesdaySchedule, String thursdaySchedule, String fridaySchedule, String saturdaySchedule, String sundaySchedule) {
        this.doctorFIO = new SimpleStringProperty(doctorFIO);
        this.position = new SimpleStringProperty(position);
        this.mondaySchedule = new SimpleStringProperty(mondaySchedule);
        this.tuesdaySchedule = new SimpleStringProperty(tuesdaySchedule);
        this.wednesdaySchedule = new SimpleStringProperty(wednesdaySchedule);
        this.thursdaySchedule = new SimpleStringProperty(thursdaySchedule);
        this.fridaySchedule = new SimpleStringProperty(fridaySchedule);
        this.saturdaySchedule = new SimpleStringProperty(saturdaySchedule);
        this.sundaySchedule = new SimpleStringProperty(sundaySchedule);
    }

    public String getDoctorFIO() {
        return doctorFIO.get();
    }

    public void setDoctorFIO(String doctorFIO) {
        this.doctorFIO.set(doctorFIO);
    }

    public String getPosition() {
        return position.get();
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public String getMondaySchedule() {
        return mondaySchedule.get();
    }

    public void setMondaySchedule(String mondaySchedule) {
        this.mondaySchedule.set(mondaySchedule);
    }

    public String getTuesdaySchedule() {
        return tuesdaySchedule.get();
    }

    public void setTuesdaySchedule(String tuesdaySchedule) { this.tuesdaySchedule.set(tuesdaySchedule);}

    public String getWednesdaySchedule() {
        return wednesdaySchedule.get();
    }

    public void setWednesdaySchedule(String wednesdaySchedule) {
        this.wednesdaySchedule.set(wednesdaySchedule);
    }

    public String getThursdaySchedule() {
        return thursdaySchedule.get();
    }

    public void setThursdaySchedule(String thursdaySchedule) {
        this.thursdaySchedule.set(thursdaySchedule);
    }

    public String getFridaySchedule() {
        return fridaySchedule.get();
    }

    public void setFridaySchedule(String fridaySchedule) {
        this.fridaySchedule.set(fridaySchedule);
    }

    public String getSaturdaySchedule() {
        return saturdaySchedule.get();
    }

    public void setSaturdaySchedule(String saturdaySchedule) {
        this.saturdaySchedule.set(saturdaySchedule);
    }

    public String getSundaySchedule() {
        return sundaySchedule.get();
    }

    public void setSundaySchedule(String sundaySchedule) {
        this.sundaySchedule.set(sundaySchedule);
    }

}
