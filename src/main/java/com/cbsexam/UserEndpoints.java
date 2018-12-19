package com.cbsexam;

import cache.UserCache;
import com.google.gson.Gson;
import controllers.UserController;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.User;
import utils.Encryption;
import utils.Log;

@Path("user")
public class UserEndpoints  {


  /**
   * @param idUser
   * @return Responses
   */
  @GET
  @Path("/{idUser}")
  public Response getUser(@PathParam("idUser") int idUser) {

    // Use the ID to get the user from the controller.
    User user = UserController.getUser(idUser);

    // TODO: Add Encryption to JSON (FIXED)
    // Convert the user object to json in order to return the object
    String json = new Gson().toJson(user);

    //json= Encryption.encryptDecryptXOR(json);

    // Return the user with the status code 200
    // TODO: What should happen if something breaks down? (FIXED)

    if (user != null)

    { return Response.status(200).type(MediaType.APPLICATION_JSON_TYPE).entity(json).build();
    } else {
      return Response.status(400).entity("There is a problem with finding what you are looking for. Try again later.").build();}





  }

  /** @return Responses */
  @GET
  @Path("/")
  public Response getUsers() {

    // Write to log that we are here
    Log.writeLog(this.getClass().getName(), this, "Get all users", 0);

    // Get a list of users
    ArrayList<User> users = userCache.getUsers(false);

    // TODO: Add Encryption to JSON (FIXED)
    // Transfer users to json in order to return it to the user
    String json = new Gson().toJson(users);

    //json=Encryption.encryptDecryptXOR(json);

    // Return the users with the status code 200
    return Response.status(200).type(MediaType.APPLICATION_JSON).entity(json).build();
  }

  UserCache userCache = new UserCache();

  @POST
  @Path("/")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response createUser(String body) {

    // Read the json from body and transfer it to a user class
    User newUser = new Gson().fromJson(body, User.class);

    // Use the controller to add the user
    User createUser = UserController.createUser(newUser);

      ArrayList<User> users = userCache.getUsers(true);

    // Get the user back with the added ID and return it to the user
    String json = new Gson().toJson(createUser);

    // Return the data to the user
    if (createUser != null) {
      // Return a response with status 200 and JSON as type
      return Response.status(200).type(MediaType.APPLICATION_JSON_TYPE).entity(json).build();
    } else {
      return Response.status(400).entity("Could not create user").build();
    }
  }

  // TODO: Make the system able to login users and assign them a token to use throughout the system. (FIXED)
  @POST
  @Path("/login")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response loginUser(String current) {

      User LoggedinUser= new Gson().fromJson(current, User.class);

      if(UserController.loginUser(LoggedinUser)) {
        return Response.status(200).entity("Welcome, we hope you enjoy your experience").build();
      } else {
          return Response.status(400).entity("There seems to be a problem. Try refreshing the page and trying again og contact support").build();
      }

    // Return a response with status 200 and JSON as type
  }

  // TODO: Make the system able to delete users(FIXED)
  public Response deleteUser(String gone) {

      User UserToDelete = new Gson().fromJson(gone, User.class);


      UserController.deleteUser(UserToDelete);

    // Return a response with status 200 and JSON as type
    return Response.status(200).entity("You user has now been deleted. Goodbye buddy, you meant a lot to us.").build();
  }

  // TODO: Make the system able to update users (FIXED)


  public Response updateUser(String updated) {


      User user = new Gson().fromJson(updated, User.class);

      // Return the data to the user
      if (UserController.updateUser(user, user.getToken())) {

          //Opdatere Cache
          userCache.getUsers(true);

          // Return a response with status 200 and JSON as type
          return Response.status(200).entity("User has been updated").build();
      } else { return Response.status(400).entity("No user found").build(); }


  }}