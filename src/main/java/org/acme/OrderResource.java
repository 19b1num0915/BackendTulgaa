package org.acme;


import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.pgclient.PgPool;
import org.acme.Model.Order1;
import org.acme.Model.Users;
import org.apache.sshd.common.config.keys.impl.RSAPublicKeyDecoder;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.NoCache;
import org.jboss.resteasy.reactive.RestResponse;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Permission;
import java.time.LocalDate;
import java.time.LocalTime;

@Path("order")
public class OrderResource {
    private static final Logger logger = Logger.getLogger(OrderResource.class);
    @Inject
    PgPool client;
    @Inject
    Order1 order;

    @Inject
    SecurityIdentity identity;

    @Path("get")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("users")
    @NoCache
    public Multi<Order1> getOrder(Permission permission){
        identity.checkPermission(permission);
        return Order1.findAll(client);
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getOrderID(Long id) {
        return Order1.findByOrderId(client, id)
                .onItem().transform(user -> user != null ? Response.ok(user) : Response.status(RestResponse.Status.NOT_FOUND))
                .onItem().transform(Response.ResponseBuilder::build);
    }
    @Path("/delete/{id}")
    @DELETE
    public Uni<Response> deleteOrderID(Long id){
        return Order1.deleteOrderId(client, id)
                .onItem().transform(deleted -> deleted ? RestResponse.Status.NO_CONTENT : RestResponse.Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }


    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean addOrder(JsonObject body){

        boolean valid = true;
        order.setConditionCode(body.getString("conditionCode"));
        order.setType(body.getString("type"));
        order.setDate(LocalDate.parse(body.getString("date")));
        order.setTime(LocalTime.parse(body.getString("time")));
        order.setText(body.getString("text"));
        order.setAddExplanation(body.getString("addexplantion"));
        order.setSendUsers(body.getInteger("sendusers"));
        order.setToken(body.getString("token"));
        try {
            client.query("insert into Order1(conditioncode, type1, specialnumber, date1, time1, text1, addExplanation, sendUsers, userInfo)" +
                    "values ('"+order.getConditionCode()+"' , '"
                    +order.getType()+"' , '"
                    +order.getSpecialNumber()+"' , '"
                    +order.getDate()+"' , '"
                    +order.getTime()+"' , '"
                    +order.getText()+"' , '"
                    +order.getAddExplanation()+"' , "
                    +order.getSendUsers()+" , "
                    +order.getToken()+")").execute().await().indefinitely();

        } catch (Exception e){
            valid = false;
        }
        return valid;
    }


}
