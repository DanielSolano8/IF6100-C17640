package ucr.ac.C17640;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ucr.ac.C17640.handlers.RegisterUserHandler;

@RestController
public class HelloController {

    @Autowired  //inyectar dependencias
    private RegisterUserHandler handler;

    @GetMapping("/hello")
    public String hello() {

var result = handler.registerUser(
        new RegisterUserHandler.Command(
                "Daniel","luisdayluke@gmail.com","pass"
        )
);


        return switch (result) {
            case RegisterUserHandler.Result.Succes success -> success.message();
            case RegisterUserHandler.Result.InvalidData invalidData ->
                    "Invalid data: " + String.join(", ", invalidData.fields());
            case RegisterUserHandler.Result.EmailAlreadyExists emailAlreadyExists -> null;
        };

    }
}
