package org.acme;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.pgclient.PgPool;
import org.acme.Model.Users;
import org.acme.Service.TokenService;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestResponse;


import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Base64;

@Path("/hello")
@RequestScoped
public class ExampleResource {
    private static final Logger logger = Logger.getLogger(ExampleResource.class);
    @Inject
    PgPool client;
    @Inject
    Users user;
    @Inject
    JsonWebToken jwt;
    @Inject
    JWTParser parser;
    @Inject
    TokenService tokenService;
    @Path("/get")
    @GET
    @PermitAll
    public Multi<Users> get() {
        return Users.findAll(client);
    }
    @GET
    @Path("user/{id}")
    @RolesAllowed("Admin")
    public Uni<Response> getID(Long id) {
        return Users.findById(client, id)
                .onItem().transform(user -> user != null ? Response.ok(user)
                        : Response.status(RestResponse.Status.NOT_FOUND))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("Admin")
    public Uni<Response> deleteID(Long id) {
        return Users.delete(client, id)
                .onItem().transform(deleted -> deleted ? RestResponse.Status.NO_CONTENT : RestResponse.Status.NOT_FOUND)
                .onItem().transform(status -> Response.status(status).build());
    }

    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public boolean addUser(JsonObject body) {
        boolean valid = true;
        try {
            user.setName(body.getString("name"));
            user.setPhone(body.getInteger("phone"));
            user.setEmail(body.getString("e-mail"));
            user.setPassword(body.getString("pass"));
            user.setType(body.getInteger("type"));

            /**
             * password hash
             */
            user.setPassword(Base64.getEncoder().encodeToString(user.getPassword().getBytes()));
            /**
             * usert token onooj ogloo
             */
            user.setToken(tokenService.generateToken(user.getEmail(), user.getPassword(), user.getId()));
            client.query("insert into Users(token1,name1,password1,phone,email,typeNumber) " +
                            "values ('" + user.getToken() + "','"
                            + user.getName() + "', '"
                            + user.getPassword() + "', "
                            + user.getPhone() + ", '"
                            + user.getEmail() + "', "
                            + user.getType() + ")")
                    .execute().await().indefinitely();
        } catch (Exception e){
            valid = false;
        }
        return valid;
    }
    @Path("login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<JsonObject>> ln(JsonObject body) {
        JsonObject jret = new JsonObject();
        String email = body.getString("email");
        String pass = body.getString("password");
        pass = Base64.getEncoder().encodeToString(pass.getBytes());
        return client.query("select token from Users where email='"
                        + email + "' and password1='" + pass + "'")
                .execute()
                .onItem()
                .transformToUni(rowSet -> {
                    return Uni.createFrom().item(rowSet.iterator().next());
                })
                .onItem().transform(row ->
                     RestResponse.ResponseBuilder.ok(
                            jret.put("token", row.getInteger(0))
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