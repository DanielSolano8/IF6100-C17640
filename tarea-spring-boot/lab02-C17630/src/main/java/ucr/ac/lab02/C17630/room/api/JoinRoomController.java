package ucr.ac.lab02.C17630.room.api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ucr.ac.lab02.C17630.room.handlers.RoomHandler;
import ucr.ac.lab02.C17630.room.jpa.RoomEntity;
import ucr.ac.lab02.C17630.room.jpa.UserEntity;

import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/C17640/room")
public class JoinRoomController {

    private final RoomHandler roomHandler;

    public JoinRoomController(RoomHandler roomHandler) {
        this.roomHandler = roomHandler;
    }

    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> joinRoom(@RequestBody Map<String, String> request) {
        String roomId = request.get("id");
        String alias = request.get("alias");

        // Validación y manejo de errores
        String result = roomHandler.joinRoom(roomId, alias);
        if (result != null && result.startsWith("El")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", result));
        }

        RoomEntity room = roomHandler.getRoomByIdentifier(roomId);
        if (room == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "El identificador de la sala no es válido."));
        }

        // Construir la respuesta con el id de la sala, el nombre y la lista de usuarios
        Map<String, Object> response = new HashMap<>();
        response.put("id", room.getIdentifier());
        response.put("name", room.getName());
        response.put("users", roomHandler.getUsersInRoom(room).stream()
                .map(UserEntity::getAlias)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }
}