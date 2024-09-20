package ucr.ac.lab02.C17630.room.jpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucr.ac.lab02.C17630.room.jpa.UserEntity;
import ucr.ac.lab02.C17630.room.jpa.RoomEntity;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByRoom(RoomEntity room);

}
