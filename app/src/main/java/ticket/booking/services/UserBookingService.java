package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private User user;
    private List<User> userList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERS_PATH = "users.json";

    public UserBookingService() throws IOException {
        loadUsers();
    }

    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUsers();
    }

//    public UserBookingService(User user) throws IOException {
//        this.user = user;
//        File users = new File(USERS_PATH);
//        this.userList = objectMapper.readValue(users, new TypeReference<List<User>>() {
//        });
//    }

    public void loadUsers() throws IOException {
        System.out.println("I am here");
        File users = new File(USERS_PATH);
        System.out.println("I reached here");
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
////        List<User> list = new ArrayList<>();
////        return list;

//        File users = new File(USERS_PATH);
//
//        if (!users.exists()) {
//            System.out.println("users.json not found, creating new file");
//            userList = new ArrayList<>();
//            saveUserListToFile();
//            return;
//        }
//
//        userList = objectMapper.readValue(
//                users,
//                new TypeReference<List<User>>() {}
//        );
    }

    public Boolean userLogin() {
        Optional<User> userFound = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        return userFound.isPresent();
    }

    public Boolean userSignUp(User user1) {
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }
        catch (Exception E) {
            return Boolean.FALSE;
        }
    }

    public void saveUserListToFile() throws IOException{
        File userFile = new File(USERS_PATH);
        objectMapper.writeValue(userFile, userList);
    }

    public void fetchBooking() {
        user.printTickets();
    }

    public Boolean cancelBooking(String ticketId) {
        // from bookedTickets(stream on it), update file
        try {
            List<Ticket> bookedTickets = user.getBookedTickets();
            return bookedTickets.removeIf(ticket -> ticketId.equals(ticket.getTicketId()));
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }
}
