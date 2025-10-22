import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
class TodoList extends JFrame {
    DefaultListModel<Task> model = new DefaultListModel<>();
    JList<Task> list = new JList<>(model);
    JTextField input = new JTextField();
    JButton addBtn = new JButton("Add");
    JButton deleteBtn = new JButton("Delete");
    public TodoList() {
        setTitle("To-Do List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420,520);
        setLocationRelativeTo(null);
        JPanel p = new JPanel(new BorderLayout(8,8));
        p.setBorder(new EmptyBorder(10,10,10,10));
        JPanel top = new JPanel(new BorderLayout(6,6));
        input.setFont(new Font("SansSerif", Font.PLAIN, 16));
        top.add(input, BorderLayout.CENTER);
        top.add(addBtn, BorderLayout.EAST);
        p.add(top, BorderLayout.NORTH);
        list.setCellRenderer(new ListCellRenderer<Task>() {
            JLabel lbl = new JLabel();
            @Override
            public Component getListCellRendererComponent(JList<? extends Task> l, Task value, int index, boolean isSelected, boolean cellHasFocus) {
                String text = value.completed ? "<html><strike>" + escapeHtml(value.text) + "</strike></html>" : value.text;
                lbl.setText(text);
                lbl.setOpaque(true);
                if (isSelected) lbl.setBackground(new Color(200,220,255)); else lbl.setBackground(Color.WHITE);
                lbl.setBorder(new EmptyBorder(6,6,6,6));
                return lbl;
            }
        });
        JScrollPane sp = new JScrollPane(list);
        p.add(sp, BorderLayout.CENTER);
        JPanel bottom = new JPanel(new BorderLayout(6,6));
        deleteBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bottom.add(deleteBtn, BorderLayout.WEST);
        JLabel hint = new JLabel("Double-click item to toggle complete. Press Enter to add.");
        hint.setFont(new Font("SansSerif", Font.ITALIC, 12));
        bottom.add(hint, BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);
        add(p);
        addBtn.addActionListener(e -> addTask());
        input.addActionListener(e -> addTask());
        deleteBtn.addActionListener(e -> {
            int i = list.getSelectedIndex();
            if (i != -1) model.remove(i);
        });
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int i = list.locationToIndex(e.getPoint());
                    if (i != -1) {
                        Task t = model.get(i);
                        t.completed = !t.completed;
                        model.set(i, t);
                        list.repaint();
                    }
                }
            }
        });
        JPopupMenu popup = new JPopupMenu();
        JMenuItem edit = new JMenuItem("Edit");
        JMenuItem clearAll = new JMenuItem("Clear All");
        popup.add(edit);
        popup.add(clearAll);
        list.setComponentPopupMenu(popup);
        edit.addActionListener(e -> {
            int i = list.getSelectedIndex();
            if (i != -1) {
                Task t = model.get(i);
                String s = JOptionPane.showInputDialog(this, "Edit task", t.text);
                if (s != null && s.trim().length() > 0) {
                    model.set(i, new Task(s.trim(), t.completed));
                }
            }
        });
        clearAll.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Clear all tasks?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) model.clear();
        });
    }
    void addTask() {
        String s = input.getText().trim();
        if (s.length() == 0) return;
        model.addElement(new Task(s,false));
        input.setText("");
        input.requestFocus();
    }
    static String escapeHtml(String s) {
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TodoList t = new TodoList();
            t.setVisible(true);
        });
    }
}
class Task {
    String text;
    boolean completed;
    Task(String t, boolean c) { text = t; completed = c; }
    public String toString() { return text; }
}
