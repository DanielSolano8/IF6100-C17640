package ucr.ac.lab02.C17630.room.jpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucr.ac.lab02.C17630.room.jpa.UserEntity;
import ucr.ac.lab02.C17630.room.jpa.MessageEntity;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findByUser(UserEntity user);
}
