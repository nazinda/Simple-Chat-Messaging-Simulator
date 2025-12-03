package engine;
import java.util.ArrayList;
import java.util.List;
public class ChatEngine {
    private static ChatEngine instance;
    private final List<Observer> observers = new ArrayList<>();
    private ChatEngine() {}
    public static ChatEngine getInstance() {
        if (instance == null) instance = new ChatEngine();
        return instance;
    }
    public void registerObserver(Observer o) { observers.add(o); }
    public void removeObserver(Observer o) { observers.remove(o); }
    public void sendMessage(String msg) {
        for (Observer o : observers) {
            o.update(generateReply(msg));
        }
    }
    private String generateReply(String msg) {
        msg = msg.toLowerCase();
        if (msg.contains("hello") || msg.contains("hi") || msg.contains("Hey")) return "Hi there!";
        if (msg.contains("how are you")) return "I'm good, how about you?";
        if (msg.contains("bye")) return "Goodbye!";
        if (msg.contains("thank")) return "You're welcome!";
        if (msg.contains("Did you do your homework?")) return "Yes i did, how about you!";
        if (msg.contains("Yes")) return "Okayy! thant's good!";
        if (msg.contains("kesi ho?")) return "Alhamdullilah! tum sunao?";
        if (msg.contains("salam")) return "Walaikum asalam!";
        return "Okayy great!";
    }
}
