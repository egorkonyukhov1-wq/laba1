package konukhov.laba1.repository;

import konukhov.laba1.model.HandoverRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandoverRecordRepository extends JpaRepository<HandoverRecord, Long> {
}