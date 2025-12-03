import javax.swing.SwingUtilities;
import ui.ChatWindow;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChatWindow());
    }
}
