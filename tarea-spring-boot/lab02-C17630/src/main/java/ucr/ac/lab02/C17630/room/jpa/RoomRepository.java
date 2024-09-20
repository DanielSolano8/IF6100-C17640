package ucr.ac.lab02.C17630.room.jpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucr.ac.lab02.C17630.room.jpa.RoomEntity;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    Optional<RoomEntity> findByIdentifier(String identifier);
}