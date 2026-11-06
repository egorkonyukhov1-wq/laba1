package konukhov.laba1.repository;

import konukhov.laba1.model.ItemClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemClaimRepository extends JpaRepository<ItemClaim, Long> {
    List<ItemClaim> findByLossReportId(Long lossReportId);
}