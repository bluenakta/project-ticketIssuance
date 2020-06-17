package ticketIssuance;

import javax.persistence.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.MimeTypeUtils;
import ticketIssuance.config.kafka.KafkaProcessor;

@Entity
@Table(name="TicketIssuance_table")
public class TicketIssuance {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String issueStatus;
    private Long bookId;

    @PostPersist
    public void onPostPersist() {
        IssueStatusChanged issueStatusChanged = new IssueStatusChanged();
        issueStatusChanged.setId(this.getId());
        issueStatusChanged.setBookId(this.getBookId());
        issueStatusChanged.setIssueStatus(this.getIssueStatus());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(issueStatusChanged);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        KafkaProcessor processor = Application.applicationContext.getBean(KafkaProcessor.class);
        MessageChannel outputChannel = processor.outboundTopic();

        outputChannel.send(MessageBuilder
                .withPayload(json)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

    @PostUpdate
    public void onPostUpdate() {
        IssueStatusChanged issueStatusChanged = new IssueStatusChanged();
        issueStatusChanged.setId(this.getId());
        issueStatusChanged.setBookId(this.getBookId());
        issueStatusChanged.setIssueStatus(this.getIssueStatus());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(issueStatusChanged);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        KafkaProcessor processor = Application.applicationContext.getBean(KafkaProcessor.class);
        MessageChannel outputChannel = processor.outboundTopic();

        outputChannel.send(MessageBuilder
                .withPayload(json)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getIssueStatus() {
        return issueStatus;
    }

    public void setIssueStatus(String issueStatus) {
        this.issueStatus = issueStatus;
    }
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }




}
