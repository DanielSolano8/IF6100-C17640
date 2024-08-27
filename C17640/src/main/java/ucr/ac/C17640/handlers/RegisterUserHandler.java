package ucr.ac.C17640.handlers;

public interface RegisterUserHandler {

    record Command(String name, String email, String password){}

    sealed interface Result {


         record Succes(String message) implements Result{}
         record InvalidData(String...fields) implements Result{}
    }
    Result registerUser(Command command);


}
