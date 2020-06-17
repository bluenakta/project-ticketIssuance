package ticketIssuance;

import ticketIssuance.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired
    TicketIssuanceRepository ticketIssuanceRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPayed_CreateIssue(@Payload Payed payed){
        if(payed.isMe()){
            System.out.println("##### listener CreateIssue : " + payed.toJson());
            TicketIssuance ticketIssuance = new TicketIssuance();
            ticketIssuance.setBookId(payed.getBookId());
            ticketIssuance.setIssueStatus("Issuable");
            ticketIssuanceRepository.save(ticketIssuance);
            System.out.println("Your ticket is now " + ticketIssuance.getIssueStatus());
        }
    }

}
