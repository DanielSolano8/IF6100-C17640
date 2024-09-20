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

    // Metodo post, este endpoint se encarga de el ingreso a una sala ya existente
    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> joinRoom(@RequestBody Map<String, String> request) {
        String roomId = request.get("id");
        String alias = request.get("alias");

        //Manejo de posibles errores
        String result = roomHandler.joinRoom(roomId, alias);

        //Si empieza con "El" y no es null hay un error
        if (result != null && result.startsWith("Error")) {
            // Si es un error, devolver una respuesta con código de estado BAD_REQUEST (400) y el mensaje de error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", result));
        }

        // Obtener la sala usando el identificador de la sala (roomId) mediante roomHandler
        RoomEntity room = roomHandler.getRoomByIdentifier(roomId);

        // Verificar si la sala no se encuentra (es decir, room es null)
        if (room == null) {


            // Si la sala no existe, devolver una respuesta con código de estado (400)
            // y un mensaje que indica que el identificador de la sala no es válido
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Error, el identificador de la sala no es válido."));
        }

        // Construir la respuesta con el json requerido: el id de la sala, el nombre y la lista de usuarios
        Map<String, Object> response = new HashMap<>();
        response.put("id", room.getIdentifier());
        response.put("name", room.getName());
        response.put("users", roomHandler.getUsersInRoom(room).stream().map(UserEntity::getAlias).collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }
}