package ucr.ac.lab02.C17630.room.handlers;

import org.springframework.stereotype.Component;

import ucr.ac.lab02.C17630.room.jpa.RoomEntity;
import ucr.ac.lab02.C17630.room.jpa.RoomRepository;
import ucr.ac.lab02.C17630.room.jpa.UserRepository;
import ucr.ac.lab02.C17630.room.jpa.UserEntity;
import ucr.ac.lab02.C17630.room.jpa.MessageRepository;
import ucr.ac.lab02.C17630.room.jpa.MessageEntity;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MessageHandler {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public MessageHandler(RoomRepository roomRepository, UserRepository userRepository, MessageRepository messageRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    //Manejo de los errores mediante un String
    public String sendMessage(String roomId, String alias, String messageContent) {

        if (roomId == null || roomId.isEmpty()) {
            return "Error,el identificador de la sala no puede ser vacío."; // Mensaje
        }
        if (alias == null || alias.isEmpty()) {
            return "Error,el alias no puede ser vacío."; // Mensaje
        }
        if (messageContent == null || messageContent.isEmpty()) {
            return "Error,el mensaje no puede ser vacío."; // Mensaje
        }

        RoomEntity room = roomRepository.findByIdentifier(roomId).orElse(null);
        if (room == null) {
            return "Error,el identificador de la sala no es válido."; // Mensaje
        }

        UserEntity user = userRepository.findByRoom(room)
                .stream()
                .filter(u -> u.getAlias().equals(alias))
                .findFirst()
                .orElse(null);

        if (user == null) {
            return "Error,el alias no es válido."; // Mensaje
        }

        MessageEntity message = new MessageEntity();
        message.setMessage(messageContent);
        message.setCreatedOn(LocalDateTime.now());
        message.setUser(user);

        messageRepository.save(message);

        return null; // No hubo error
    }

    public List<MessageEntity> getMessages(String roomId) {
        if (roomId == null || roomId.isEmpty()) {
            return Collections.emptyList(); // Mensaje
        }

        RoomEntity room = roomRepository.findByIdentifier(roomId).orElse(null);
        if (room == null) {
            return Collections.emptyList(); // Sala no existente
        }

        List<MessageEntity> messages = new ArrayList<>();
        for (UserEntity user : userRepository.findByRoom(room)) {
            messages.addAll(messageRepository.findByUser(user));
        }

        return messages; // Devuelve los mensajes dentro de  la sala
    }

}