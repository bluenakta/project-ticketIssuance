package ticketIssuance;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface TicketIssuanceRepository extends PagingAndSortingRepository<TicketIssuance, Long>{

    TicketIssuance findByBookId(Long bookId);

}