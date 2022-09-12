package org.acme.Model;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@ApplicationScoped
@Entity
@Table(name = "Order1")
public class Order1 {
    private Long id;
    private String conditionCode;
    private String type;
    private String specialNumber;
    private LocalDate date;
    private LocalTime time;
    private String text;
    private String addExplanation;
    private int sendUsers;
    private String token;
    public Order1() {}
    public Order1(Long id,
                  String conditionCode,
                  String type,
                  String specialNumber,
                  LocalDate date,
                  LocalTime time,
                  String text,
                  String addExplanation,
                  int sendUsers,
                  String token){
            this.id = id;
            this.conditionCode = conditionCode;
            this.type = type;
            this.specialNumber = specialNumber;
            this.date = date;
            this.time = time;
            this.text = text;
            this.addExplanation = addExplanation;
            this.sendUsers = sendUsers;
            this.token = token;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getConditionCode() {
        return this.conditionCode;
    }
    public void setConditionCode(String conditionCode) {
        this.conditionCode = conditionCode;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
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
    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public int getSendUsers() {
        return this.sendUsers;
    }
    public void setSendUsers(int sendUsers) {
        this.sendUsers = sendUsers;
    }
    public String getAddExplanation() {
        return this.addExplanation;
    }
    public void setAddExplanation(String addExplanation) {
        this.addExplanation = addExplanation;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public static Order1 from(Row row){
        final Order1 or = new Order1();
        or.setId(row.getLong("id"));
        or.setConditionCode(row.getString("conditioncode"));
        or.setType(row.getString("type1"));
        or.setSpecialNumber(row.getString("specialnumber"));
        or.setDate(row.getLocalDate("date1"));
        or.setTime(row.getLocalTime("time1"));
        or.setText(row.getString("text1"));
        or.setAddExplanation(row.getString("addexplanation"));
        or.setSendUsers(row.getInteger("sendusers"));
        or.setToken(row.getString("token"));
        return or;
    }
    public static Multi<Order1> findAll(PgPool client) {
        return client.query("SELECT * FROM Order1").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Order1::from);
    }
    public static Uni<Order1> findByOrderId(PgPool client, Long id) {
        return client.query("SELECT * from Order1 where id="+ id).execute()
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? from((Row) iterator.next()) : null);
    }
    public static Uni<Boolean> deleteOrderId(PgPool client, Long id) {
        return client.query("DELETE FROM Order1 WHERE id =" + id).execute()
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }

}
