package ucr.ac.lab02.C17630.room.handlers;

import org.springframework.stereotype.Component;
import ucr.ac.lab02.C17630.room.jpa.RoomEntity;
import ucr.ac.lab02.C17630.room.jpa.RoomRepository;
import ucr.ac.lab02.C17630.room.jpa.UserRepository;
import ucr.ac.lab02.C17630.room.jpa.UserEntity;
import java.util.List;
import java.util.UUID;

@Component
public class RoomHandler {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public RoomHandler(RoomRepository roomRepository, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public String createRoom(String name, String createdBy) {
        if (name == null || name.isEmpty()) {
            return "El nombre de la sala no puede ser vacío."; // Mensaje descriptivo
        }
        if (createdBy == null || createdBy.isEmpty()) {
            return "El alias del creador no puede ser vacío."; // Mensaje descriptivo
        }

        RoomEntity room = new RoomEntity();
        room.setName(name);
        room.setCreatedBy(createdBy);
        room.setIdentifier(UUID.randomUUID().toString());

        roomRepository.save(room);

        // Agregar al creador automáticamente a la sala
        UserEntity creator = new UserEntity();
        creator.setAlias(createdBy);
        creator.setRoom(room);

        userRepository.save(creator);

        return room.getIdentifier(); // Retorna el identificador único generado
    }

    public String joinRoom(String roomId, String alias) {
        if (roomId == null || roomId.isEmpty()) {
            return "El identificador de la sala no puede ser vacío."; // Mensaje descriptivo
        }
        if (alias == null || alias.isEmpty()) {
            return "El alias no puede ser vacío."; // Mensaje descriptivo
        }

        RoomEntity room = roomRepository.findByIdentifier(roomId).orElse(null);
        if (room == null) {
            return "El identificador de la sala no es válido."; // Mensaje descriptivo
        }

        boolean aliasExists = userRepository.findByRoom(room)
                .stream()
                .anyMatch(user -> user.getAlias().equals(alias));
        if (aliasExists) {
            return "El alias ya existe en la sala."; // Mensaje descriptivo
        }

        // Crear nuevo usuario y añadirlo a la sala
        UserEntity user = new UserEntity();
        user.setAlias(alias);
        user.setRoom(room);

        userRepository.save(user);

        return null; // Retorna null si no hubo errores
    }

    public RoomEntity getRoomByIdentifier(String roomId) {
        return roomRepository.findByIdentifier(roomId).orElse(null);
    }

    public List<UserEntity> getUsersInRoom(RoomEntity room) {
        return userRepository.findByRoom(room);
    }
}