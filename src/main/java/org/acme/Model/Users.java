package org.acme.Model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.oidc.IdToken;
import io.quarkus.security.User;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.ws.rs.core.SecurityContext;

@ApplicationScoped
@Entity
@Table(name = "Users")
@UserDefinition
public class Users  extends PanacheEntity {

    private static final Logger logger = Logger.getLogger(Users.class);

    private String token;
    private Long id;
    private String name;
    @Password
    private String password;
    private int phone;

    @Username
    private String email;

    private int type;
    public Users(){

    }
    public Users(String token,Long id, String name, String password, int phone, String email, int type){
        this.token = token;
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.type = type;
    }

    public String getToken(){
        return this.token;
    }

    public void setToken(String x){
        this.token = x;
    }

    public Long getId(){
        return this.id;
    }

    public void setId(Long x){
        this.id = x;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String x){
        this.name = x;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String x){
        this.password = x;
    }

    public int getPhone(){
        return this.phone;
    }

    public void setPhone(int x){
        this.phone = x;
    }

    public String getEmail(){
        return this.email;
    }
    public  void setEmail(String x){
        this.email = x;
    }
    public int getType(){
        return this.type;
    }
    public void setType(int x){
        this.type = x;
    }
    public static Users from(Row row){
        final Users users = new Users();
        Long id = row.getLong("id");
        String token =  row.getString("token1");
        String name = row.getString("name1");
        String pass = row.getString("password1");
        int phone = row.getInteger("phone");
        String email = row.getString("email");
        int type = row.getInteger("typenumber");
        users.setId(id);
        users.setToken(token);
        users.setName(name);
        users.setPassword(pass);
        users.setPhone(phone);
        users.setEmail(email);
        users.setType(type);
        return users;
    }
    public static Multi<Users> findAll(PgPool client) {
        return client.query("SELECT * FROM Users").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Users::from);
    }
    public static Uni<Users> findById(PgPool client,Long id) {
        return client.query("SELECT * from Users where id="+ id).execute()
                .onItem().transform(RowSet::iterator)
                .onItem().transform(iterator -> iterator.hasNext() ? from((Row) iterator.next()) : null);
    }
    public static Uni<Boolean> delete(PgPool client, Long id) {
        return client.query("DELETE FROM Users WHERE id =" + id).execute()
                .onItem().transform(pgRowSet -> pgRowSet.rowCount() == 1);
    }


}