package ucr.ac.lab02.C17630.room.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ucr.ac.lab02.C17630.room.jpa.MessageEntity;
import ucr.ac.lab02.C17630.room.handlers.MessageHandler;


@RestController
@RequestMapping("/api/C17640/room")
public class MessageController {

    private final MessageHandler messageHandler;

    public MessageController(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    // Metodo post, este endpoint se encarga de enviar los mensajes
    @PostMapping("/message")
    public ResponseEntity<Object> sendMessage(@RequestBody Map<String, String> request) {
        String roomId = request.get("id");
        String alias = request.get("alias");
        String messageContent = request.get("message");

        //manejo de errores con el string
        String result = messageHandler.sendMessage(roomId, alias, messageContent);
        if (result != null && result.startsWith("Error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);  //  mensaje de error
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

        // Devolver la respuesta con los detalles del mensaje en formato JSON requerido
        return ResponseEntity.ok(response);
    }

    // Metodo get, este endpoint se encarga de devolver todos los mensajes de la sala
    @GetMapping("/message")
    public ResponseEntity<Object> getMessages(@RequestParam String id) {

        if (id == null || id.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error, el identificador de la sala no puede estar vacío.");
        }

        List<MessageEntity> messages = messageHandler.getMessages(id);
        if (messages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error, no se encuentran los mensajes o no es una sala valida.");
        }

        //construcción de la respuesta con todos los mensajes
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
