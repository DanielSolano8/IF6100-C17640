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

    // Constructor con inyección de dependencias para el handler
    public CreateRoomController(RoomHandler roomHandler) {
        this.roomHandler = roomHandler;
    }

    // Método que maneja la creación de una sala
    @PostMapping("/create")
    public ResponseEntity<String> createRoom(@RequestBody Map<String, String> request) {
        String name = request.get("name");          // Obtener el nombre de la sala
        String createdBy = request.get("createdBy"); // Obtener el alias del creador

        // Validación y manejo de errores
        String result = roomHandler.createRoom(name, createdBy);
        if (result != null && result.startsWith("El")) {
            // Si el handler devuelve un mensaje de error, retornar 400 BAD_REQUEST con el mensaje de error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }

        if (result == null) {
            // Si no se pudo crear la sala, retornar 400 BAD_REQUEST con un mensaje genérico
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo crear la sala.");
        }

        // Si todo sale bien, devolver el ID de la sala creada (que es el resultado del handler)
        return ResponseEntity.ok(result); // Devuelve el identificador de la sala
    }
}