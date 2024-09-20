package ucr.ac.lab02.C17630.room.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ucr.ac.lab02.C17630.room.handlers.RoomHandler;

import java.util.Map;


@RestController
@RequestMapping("/api/C17640/room")

public class CreateRoomController {
    private final RoomHandler roomHandler;

    // Constructor de RoomHandler
    public CreateRoomController(RoomHandler roomHandler) {
        this.roomHandler = roomHandler;
    }

    // Metodo post, este endpoint se encarga de la creación de la sala
    @PostMapping("/create")
    public ResponseEntity<String> createRoom(@RequestBody Map<String, String> request) {
        String name = request.get("name");          // Se obtiene el nombre de la sala
        String createdBy = request.get("createdBy"); // Se obitene el alias del creador

        //Manejo de posibles errores con un String
        String result = roomHandler.createRoom(name, createdBy);
        if (result != null && result.startsWith("Error")) {
            // Si el handler devuelve un error, retornar  BAD_REQUEST
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        if (result == null) {
            // Si no se pudo crear la sala, retornar  BAD_REQUEST y un mensaje
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo crear la sala.");
        }


        // Si no hay ningun error, se retorna el ID
        return ResponseEntity.ok(result); // Devuelve el identificador de la sala
    }
}