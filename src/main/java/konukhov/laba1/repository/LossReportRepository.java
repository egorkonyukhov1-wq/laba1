package konukhov.laba1.repository;

import konukhov.laba1.model.LossReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LossReportRepository extends JpaRepository<LossReport, Long> {
}