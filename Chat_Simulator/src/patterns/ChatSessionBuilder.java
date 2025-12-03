package patterns;
import java.util.ArrayList;
import java.util.List;
public class ChatSessionBuilder {
    private String contact;
    private List<String> messages = new ArrayList<>();
    public ChatSessionBuilder withContact(String contact) {
        this.contact = contact;
        return this;
    }
    public ChatSessionBuilder withMessages(List<String> msgs) {
        this.messages = msgs;
        return this;
    }
    public ChatSession build() {
        return new ChatSession(contact, messages);
    }
    public static class ChatSession {
        private final String contact;
        private final List<String> messages;
        public ChatSession(String contact, List<String> messages) {
            this.contact = contact;
            this.messages = messages;
        }
        public String getContact() { return contact; }
        public List<String> getMessages() { return messages; }
    }
}
