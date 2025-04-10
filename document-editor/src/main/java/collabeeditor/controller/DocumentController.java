package collabeeditor.controller;



import collabeeditor.model.Document;
import collabeeditor.repo.DocumentRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/document")
public class DocumentController {
    private final DocumentRepository repository;
    private final SimpMessagingTemplate messagingTemplate;

    public DocumentController(DocumentRepository repository, SimpMessagingTemplate messagingTemplate) {
        this.repository = repository;
        this.messagingTemplate = messagingTemplate;
    }

    // Get document by ID
    @GetMapping("/{id}")
    public Optional<Document> getDocument(@PathVariable Long id) {
        return repository.findById(id);
    }

    // Save document via REST API
    @PostMapping("/save")
    public Document saveDocument(@RequestBody Document document) {
        Document savedDoc = repository.save(document);
        messagingTemplate.convertAndSend("/topic/document", savedDoc);
        return savedDoc;
    }

    // WebSocket: Real-time document updates
    @MessageMapping("/edit")
    @SendTo("/topic/document")
    public Document editDocument(Document document) {
        return repository.save(document);
    }
}
