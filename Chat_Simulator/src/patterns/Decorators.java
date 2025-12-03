package patterns;
import java.text.SimpleDateFormat;
import java.util.Date;
class TextMessage implements Message {
    private final String content;
    public TextMessage(String content) {
        this.content = content;
    }
    @Override
    public String getContent() {
        return content;
    }
}
abstract class MessageDecorator implements Message {
    protected Message message;

    public MessageDecorator(Message message) {
        this.message = message;
    }
}
class TimestampDecorator extends MessageDecorator {
    public TimestampDecorator(Message message) {
        super(message);
    }
    @Override
    public String getContent() {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return message.getContent() + " (" + time + ")";
    }
}
class EmojiDecorator extends MessageDecorator {
    public EmojiDecorator(Message message) {
        super(message);
    }
    @Override
    public String getContent() {
        return message.getContent() + " 😊";
    }
}
public class Decorators {
    public static Message createMessage(String content, boolean withTimestamp, boolean withEmoji) {
        Message msg = new TextMessage(content);
        if (withTimestamp) msg = new TimestampDecorator(msg);
        if (withEmoji) msg = new EmojiDecorator(msg);
        return msg;
    }
}
