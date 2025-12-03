package ui;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import engine.ChatEngine;
import engine.Observer;
import patterns.Message;
import patterns.Decorators;

public class ChatWindow {
    private final JFrame frame;
    private final JTextPane chatPane;
    private final JTextField inputField;
    private final JButton sendButton;
    private JList<String> contacts;
    private Map<String, java.util.List<String>> chatLogs = new HashMap<>();
    private Map<String, Integer> unreadCount = new HashMap<>();
    private String currentContact = null;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    @SuppressWarnings("Convert2Lambda")
    public ChatWindow() {
        frame = new JFrame("Chat Simulator");
        frame.setSize(900, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(200, 0));
        leftPanel.setBackground(Color.LIGHT_GRAY);
        JLabel contactsLabel = new JLabel("Contacts", SwingConstants.CENTER);
        contactsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftPanel.add(contactsLabel, BorderLayout.NORTH);
        DefaultListModel<String> contactListModel = new DefaultListModel<>();
        contactListModel.addElement("Arifa");
        contactListModel.addElement("Zunaira");
        contactListModel.addElement("Aazain");
        contacts = new JList<>(contactListModel);
        contacts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contacts.setFixedCellHeight(50);
        contacts.setFont(new Font("Arial", Font.PLAIN, 14));
        leftPanel.add(new JScrollPane(contacts), BorderLayout.CENTER);
        frame.add(leftPanel, BorderLayout.WEST);
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        JLabel chatLabel = new JLabel("Messages", SwingConstants.CENTER);
        chatLabel.setFont(new Font("Arial", Font.BOLD, 16));
        rightPanel.add(chatLabel, BorderLayout.NORTH);
        chatPane = new JTextPane();
        chatPane.setEditable(false);
        chatPane.setBackground(Color.WHITE);
        rightPanel.add(new JScrollPane(chatPane), BorderLayout.CENTER);
        inputField = new JTextField();
        inputField.setText("Write a message...");
        inputField.setForeground(Color.GRAY);
        inputField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (inputField.getText().equals("Write a message...")) {
                    inputField.setText("");
                    inputField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (inputField.getText().isEmpty()) {
                    inputField.setForeground(Color.GRAY);
                    inputField.setText("Write a message...");
                }
            }
        });
        sendButton = new JButton("Send");
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        rightPanel.add(inputPanel, BorderLayout.SOUTH);
        frame.add(rightPanel, BorderLayout.CENTER);
        for (int i = 0; i < contactListModel.size(); i++) {
            unreadCount.put(contactListModel.getElementAt(i), 0);
            chatLogs.put(contactListModel.getElementAt(i), new ArrayList<>());
        }
        contacts.addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                currentContact = contacts.getSelectedValue().split(" ")[0]; // remove count
                unreadCount.put(currentContact, 0);
                refreshContacts();
                displayMessages(currentContact);
            }
        });
        sendButton.addActionListener(e -> sendMessage());
        ChatEngine.getInstance().registerObserver(new Observer() {
            @Override
            public void update(String message) {
                if (currentContact != null) {
                    String time = timeFormat.format(new Date());
                    addMessage(currentContact, currentContact + ": " + message + " (" + time + ")");
                    displayMessages(currentContact);
                } else {
                    // For unseen messages from contacts
                    String contact = chatLogs.keySet().iterator().next();
                    String time = timeFormat.format(new Date());
                    addMessage(contact, contact + ": " + message + " (" + time + ")");
                    unreadCount.put(contact, unreadCount.getOrDefault(contact, 0) + 1);
                    refreshContacts();
                }
            }
        });
        frame.setVisible(true);
    }
    private void sendMessage() {
        if (currentContact == null) {
            JOptionPane.showMessageDialog(frame, "Please select a contact first!");
            return;
        }
        String msg = inputField.getText().trim();
        if (!msg.isEmpty() && !msg.equals("Write a message...")) {
            // New code with decorator pattern
            Message decoratedMsg = Decorators.createMessage(msg, true, false); // timestamp yes, emoji no
            String finalMsg = decoratedMsg.getContent();

            addMessage(currentContact, "You: " + finalMsg);
            displayMessages(currentContact);
            inputField.setText("");
            ChatEngine.getInstance().sendMessage(finalMsg);
        }
    }
    private void addMessage(String contact, String msg) {
        chatLogs.get(contact).add(msg);
    }
    @SuppressWarnings({"UseSpecificCatch", "CallToPrintStackTrace"})
    private void displayMessages(String contact) {
        chatPane.setText("");
        StyledDocument doc = chatPane.getStyledDocument();
        for (String msg : chatLogs.get(contact)) {
            try {
                SimpleAttributeSet attr = new SimpleAttributeSet();
                boolean isUser = msg.startsWith("You:");
                if (isUser) {
                    StyleConstants.setBackground(attr, new Color(0, 120, 215));
                    StyleConstants.setForeground(attr, Color.WHITE);
                    StyleConstants.setAlignment(attr, StyleConstants.ALIGN_RIGHT);
                } else {
                    StyleConstants.setBackground(attr, new Color(220, 220, 220));
                    StyleConstants.setForeground(attr, Color.BLACK);
                    StyleConstants.setAlignment(attr, StyleConstants.ALIGN_LEFT);
                }
                StyleConstants.setFontSize(attr, 14);
                StyleConstants.setFontFamily(attr, "Arial");
                StyleConstants.setSpaceAbove(attr, 4);
                StyleConstants.setSpaceBelow(attr, 4);
                StyleConstants.setLeftIndent(attr, 10);
                StyleConstants.setRightIndent(attr, 10);
                StyleConstants.setLineSpacing(attr, 0.2f);

                int len = doc.getLength();
                doc.insertString(len, msg + "\n", attr);
                doc.setParagraphAttributes(len, msg.length(), attr, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        chatPane.setCaretPosition(doc.getLength());
    }
    private void refreshContacts() {
        DefaultListModel<String> model = (DefaultListModel<String>) contacts.getModel();
        for (int i = 0; i < model.size(); i++) {
            String name = model.getElementAt(i).split(" ")[0]; // remove old count
            int count = unreadCount.getOrDefault(name, 0);
            model.setElementAt(count > 0 ? name + " (" + count + ")" : name, i);
        }
    }
}
