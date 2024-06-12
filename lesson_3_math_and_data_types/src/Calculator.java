import javax.swing.*;
import expression.Expr;
import parse.Parser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Calculator {

    private JFrame frame;
    private JTextField textField;
    private StringBuilder currentInput;

    public Calculator() {
        currentInput = new StringBuilder();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("THE NUMB'A KRUNCH'A");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 24));
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        textField.setEnabled(false);
        textField.setDisabledTextColor(Color.BLACK);
        frame.add(textField, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 5, 0, 0));
        frame.add(panel, BorderLayout.CENTER);

        String[] buttons = {
            "abs(", "sqrt(", "pow(", ",", " ",
            "sumTo(", "gcf(", "isPrime(", "hypot(", "toBinary(",
            "7", "8", "9", "(", ")",
            "4", "5", "6", "*", "/",
            "1", "2", "3", "+", "-",
            " ", "0", "<-", "C", "="
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 24));
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
            bindKey(button, text);
        }

        frame.setVisible(true);

        // Bind Enter key for "="
        bindKey(null, "=");
    }

    private void bindKey(JButton button, String key) {
        KeyStroke keyStroke = null;

        switch (key) {
            case "0": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_0, 0); break;
            case "1": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_1, 0); break;
            case "2": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_2, 0); break;
            case "3": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_3, 0); break;
            case "4": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_4, 0); break;
            case "5": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_5, 0); break;
            case "6": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_6, 0); break;
            case "7": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_7, 0); break;
            case "8": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_8, 0); break;
            case "9": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_9, 0); break;
            case "+": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.SHIFT_DOWN_MASK, false); break;
            case "-": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0); break;
            case "*": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_8, KeyEvent.SHIFT_DOWN_MASK, false); break;
            case "/": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, 0); break;
            case "(": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_9, KeyEvent.SHIFT_DOWN_MASK, false); break;
            case ")": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_0, KeyEvent.SHIFT_DOWN_MASK, false); break;
            case "C": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0); break;
            case "=": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0); break;
            case "<-": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0); break;
            case ",": keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, 0); break;
        }

        if (keyStroke != null) {
            Action action = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (button != null) {
                        button.doClick();
                    } else {
                        String command = key;
                        if (command.charAt(0) == '=') {
                            try {
                                String result = evaluate(textField.getText());
                                textField.setText(result);
                                currentInput.setLength(0);
                            } catch (Exception ex) {
                                textField.setText("Error: " + ex.getMessage());
                                currentInput.setLength(0);
                            }
                        } else {
                            currentInput.append(command);
                            textField.setText(currentInput.toString());
                        }
                    }
                }
            };

            InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            inputMap.put(keyStroke, key);
            ActionMap actionMap = frame.getRootPane().getActionMap();
            actionMap.put(key, action);
        }
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.charAt(0) == 'C') {
                currentInput.setLength(0);
                textField.setText("");
            } else if (command.charAt(0) == '=') {
                try {
                    String result = evaluate(textField.getText());
                    textField.setText(result);
                    currentInput.setLength(0);
                } catch (Exception ex) {
                    textField.setText("Error: " + ex.getMessage());
                    currentInput.setLength(0);
                }
            } else if (command.endsWith("<-")) {
                currentInput.setLength(Math.max(0, currentInput.length() - 1));
                textField.setText(currentInput.toString());
            } else if (command.charAt(0) == ' ') {
                // do nothing
            } else {
                currentInput.append(command);
                textField.setText(currentInput.toString());
            }
        }
    }

    private String evaluate(String expression) {
        Parser p = new Parser(expression);
        Expr expr = p.parse();
        final int MAX_EVALS = 100;
        for (int i = 0; i < MAX_EVALS; i++) {
            if (expr.isTerminal()) {
                try {
                    return String.valueOf(expr.getValue());
                } catch (Exception e) {
                    return expr.toString();
                }
            }

            expr = expr.evaluate();
        }
        System.out.println(expr.toString());
        return "Evaluation took too many steps! See terminal.";
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Calculator calculator = new Calculator();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
