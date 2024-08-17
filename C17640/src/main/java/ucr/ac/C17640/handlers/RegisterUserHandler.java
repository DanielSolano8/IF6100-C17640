package ucr.ac.C17640.handlers;

public interface RegisterUserHandler {

    record Command(String name, String email, String password){}
    sealed interface Result {
        final record Succes(String message) implements Result{}
        final record InvalidData(String...fields) implements Result{}
    }
    void registerUser(Command command);

    //implementar cqrs
}
