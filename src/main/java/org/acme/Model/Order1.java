package org.acme.Model;
import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.LocalTime;

@ApplicationScoped
public class Order1 {
    private Long id;
    private int getState;
    private LocalDate date;
    private LocalTime time;
    private String specialNumber;
    private String text1;
    private String segmentCheck;
    private String removeSegment;
    private int sendUsers;
    private int segmentEmp;
    private int sentEmp;
    private String addExplanation;
    private int userInfo;


    public Order1() {

    }

    public Order1(Long id,
                  int get_state,
                  LocalDate date,
                  LocalTime time,
                  String special_number,
                  String text1,
                  String segmentCheck,
                  String removeSegment,
                  int sendUsers,
                  int segmentEmp,
                  int sentEmp,
                  String addExplanation,
                  int userInfo){
        this.id = id;
        this.getState = get_state;
        this.date = date;
        this.time = time;
        this.specialNumber = special_number;
        this.text1 = text1;
        this.segmentCheck = segmentCheck;
        this.removeSegment = removeSegment;
        this.sendUsers = sendUsers;
        this.segmentEmp = segmentEmp;
        this.sentEmp = sentEmp;
        this.addExplanation = addExplanation;
        this.userInfo = userInfo;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGetState() {
        return this.getState;
    }

    public void setGetState(int getState) {
        this.getState = getState;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return this.time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getSpecialNumber() {
        return this.specialNumber;
    }

    public void setSpecialNumber(String specialNumber) {
        this.specialNumber = specialNumber;
    }

    public String getText1() {
        return this.text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getSegmentCheck() {
        return this.segmentCheck;
    }

    public void setSegmentCheck(String segmentCheck) {
        this.segmentCheck = segmentCheck;
    }

    public String getRemoveSegment() {
        return this.removeSegment;
    }

    public void setRemoveSegment(String removeSegment) {
        this.removeSegment = removeSegment;
    }

    public int getSendUsers() {
        return this.sendUsers;
    }

    public void setSendUsers(int sendUsers) {
        this.sendUsers = sendUsers;
    }

    public int getSegmentEmp() {
        return this.segmentEmp;
    }

    public void setSegmentEmp(int segmentEmp) {
        this.segmentEmp = segmentEmp;
    }

    public int getSentEmp() {
        return this.sentEmp;
    }

    public void setSentEmp(int sentEmp) {
        this.sentEmp = sentEmp;
    }

    public String getAddExplanation() {
        return this.addExplanation;
    }

    public void setAddExplanation(String addExplanation) {
        this.addExplanation = addExplanation;
    }

    public int getUserInfo() {
        return this.userInfo;
    }

    public void setUserInfo(int userInfo) {
        this.userInfo = userInfo;
    }


}
