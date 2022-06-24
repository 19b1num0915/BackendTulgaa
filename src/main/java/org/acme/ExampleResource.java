package org.acme;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.pgclient.PgPool;

import org.acme.Model.Users;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Base64;

@Path("/hello")
public class ExampleResource {
    private static final Logger logger = Logger.getLogger(ExampleResource.class);

    @Inject
    PgPool client;

    @Inject
    Users user;

    @Path("/get")
    @GET
    public Multi<Users> get() {
        return Users.findAll(client);
    }

    @GET
    @Path("{id}")
    public Uni<Response> getID(Long id) {
        logger.infov("id={0}", id);
        return Users.findById(client, id)
                .onItem().transform(user -> user != null ? Response.ok(user) : Response.status(RestResponse.Status.NOT_FOUND))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @DELETE
    @Path("/delete/{id}")
    public Uni<Response> deleteID(Long id) {
        return Users.delete(client, id)
                .onItem().transform(deleted -> deleted ? RestResponse.Status.NO_CONTENT : RestResponse.Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }

    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public RestResponse<JsonObject> addUser(JsonObject body) {
        JsonObject j = new JsonObject();
        user.setToken(body.getString("token"));
        user.setName(body.getString("name"));
        user.setPhone(body.getInteger("phone"));
        user.setEmail(body.getString("e-mail"));
        user.setPassword(body.getString("pass"));
        user.setType(body.getInteger("type"));
        /**
         * password hash
         */
        //user.setPassword(BcryptUtil.bcryptHash(user.getPassword()));
        user.setPassword(Base64.getEncoder().encodeToString(user.getPassword().getBytes()));
        client.query("insert into Users(token1,name1,password1,phone,email,typeNumber) " +
                        "values ('" + user.getToken() + "','" + user.getName() + "', '" + user.getPassword() + "', " + user.getPhone() + ", '" + user.getEmail() + "', " + user.getType() + ")")
                .execute().await().indefinitely();
        return RestResponse.ResponseBuilder.ok(j).build();
    }

//    @Path("login")
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Uni<RestResponse<JsonObject>> login(JsonObject body) {
//        JsonObject jret = new JsonObject();
//
//        String email = body.getString("email");
//        String pass = body.getString("password");
//
//        pass = Base64.getEncoder().encodeToString(pass.getBytes());
//
//
//        return client.query("SELECT id, typenumber FROM Users WHERE email='" + email + "' and password1='" + pass + "'")
//                .execute()
//                .onItem().transformToUni(rowset -> {
//                    return Uni.createFrom().item(rowset.iterator().next());
//                })
//                .onItem()
//                .transform(row -> RestResponse.ResponseBuilder.ok(
//                                new JsonObject()
//                                        .put("id", row.getInteger(0))
//                                        .put("type", row.getInteger(1)))
//                        .build());
//
//    }


    @Path("login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<JsonObject>> ln(JsonObject body) {
        JsonObject jret = new JsonObject();

        String email = body.getString("email");
        String pass = body.getString("password");

        pass = Base64.getEncoder().encodeToString(pass.getBytes());
        return client.query("select id, typenumber from Users where email='" + email + "' and password1='" + pass + "'")
                .execute()
                .onItem()
                .transformToUni(rowSet -> {
                    return Uni.createFrom().item(rowSet.iterator().next());
                })
                .onItem().transform(row ->
                     RestResponse.ResponseBuilder.ok(
                            jret.put("id", row.getInteger(0))
                                    .put("type", row.getInteger(1))
                                    .put("code", "000")
                                    .put("msg", "success")).build()
                )
                .onFailure().recoverWithItem(
                        RestResponse.ResponseBuilder.ok(
                                new JsonObject()
                                        .put("code", "999")
                                        .put("msg", "failed")).build());
    }









}