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
            return "Error,el nombre de la sala no puede estar vacío."; // Se envía un mensaje
        }
        if (createdBy == null || createdBy.isEmpty()) {
            return "Error,el alias del creador no puede estar vacío."; // Se envía un mensaje
        }

        RoomEntity room = new RoomEntity();

        room.setName(name);
        room.setCreatedBy(createdBy);

        // Generar un identificador único para la sala usando UUID y convertirlo a String
        room.setIdentifier(UUID.randomUUID().toString());

        // Guardar la entidad RoomEntity en la base de datos utilizando el repositorio
        roomRepository.save(room);

        // Agregar al creador automáticamente a la sala
        UserEntity creator = new UserEntity();
        creator.setAlias(createdBy);
        creator.setRoom(room);

        userRepository.save(creator);

        return room.getIdentifier(); // Se retorna el identificador
    }

    //Manejo de excepciones mediante un String para el controller
    public String joinRoom(String roomId, String alias) {
        if (roomId == null || roomId.isEmpty()) {
            return "Error,el identificador de la sala no puede estar vacío."; //  Se envía un mensaje
        }
        if (alias == null || alias.isEmpty()) {
            return "Error,el alias no puede estar vacío."; //  Se envía un mensaje
        }

        RoomEntity room = roomRepository.findByIdentifier(roomId).orElse(null);
        if (room == null) {
            return "Error,el identificador de la sala no es válido, intentelo de nuevo."; // Se envía un mensaje
        }

        //Verificar si el alias se repite
        boolean aliasExists = userRepository.findByRoom(room).stream().anyMatch(user -> user.getAlias().equals(alias));
        if (aliasExists) {
            return "Error,el alias ingresado ya se encuentra en la sala."; //  Se envía un mensaje
        }

        // Creación del usuario e ingreso a la sala existente
        UserEntity user = new UserEntity();
        user.setAlias(alias);
        user.setRoom(room);

        userRepository.save(user);

        return null; //Null si no hubo algún error
    }

    public RoomEntity getRoomByIdentifier(String roomId) {
        return roomRepository.findByIdentifier(roomId).orElse(null);
    }

    public List<UserEntity> getUsersInRoom(RoomEntity room) {
        return userRepository.findByRoom(room);
    }
}
