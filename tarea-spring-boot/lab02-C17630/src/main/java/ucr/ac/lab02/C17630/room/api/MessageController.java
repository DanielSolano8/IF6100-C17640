package ucr.ac.lab02.C17630.room.api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ucr.ac.lab02.C17630.room.jpa.MessageEntity;
import ucr.ac.lab02.C17630.room.handlers.MessageHandler;
import ucr.ac.lab02.C17630.room.jpa.RoomEntity;
import ucr.ac.lab02.C17630.room.jpa.UserEntity;

@RestController
@RequestMapping("/api/C17640/room")
public class MessageController {

    private final MessageHandler messageHandler;

    public MessageController(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @PostMapping("/message")
    public ResponseEntity<Object> sendMessage(@RequestBody Map<String, String> request) {
        String roomId = request.get("id");
        String alias = request.get("alias");
        String messageContent = request.get("message");

        // Validación y manejo de errores
        String result = messageHandler.sendMessage(roomId, alias, messageContent);
        if (result != null && result.startsWith("El")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);  // Devolver un mensaje de error
        }

        // Obtener el último mensaje enviado por el usuario en la sala
        List<MessageEntity> messages = messageHandler.getMessages(roomId);
        if (messages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se pudo encontrar el mensaje.");
        }

        // Obtener el último mensaje de la lista
        MessageEntity lastMessage = messages.get(messages.size() - 1);

        // Crear la respuesta con solo el mensaje recién enviado
        Map<String, Object> response = new HashMap<>();
        response.put("id", lastMessage.getId());
        response.put("createdOn", lastMessage.getCreatedOn().toString());  // Fecha y hora en formato ISO-8601
        response.put("message", lastMessage.getMessage());

        // Devolver la respuesta con los detalles del mensaje en formato JSON
        return ResponseEntity.ok(response);
    }

    @GetMapping("/message")
    public ResponseEntity<Object> getMessages(@RequestParam String id) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El identificador de la sala no puede ser vacío.");
        }

        List<MessageEntity> messages = messageHandler.getMessages(id);
        if (messages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No hay mensajes o la sala no es válida.");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("messages", messages.stream().map(msg -> {
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("alias", msg.getUser().getAlias());
            messageMap.put("message", msg.getMessage());
            messageMap.put("createdOn", msg.getCreatedOn());
            return messageMap;
        }).collect(Collectors.toList()));




        return ResponseEntity.ok(response);
    }
}
