package dixxe.DRSA;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
    Hello future me!
    Please refactor it and comment it ASAP
 */

public class Main {
    public static void main(String[] args) {

        MainFrame mainFrame = new MainFrame("DRSA machine", 500, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        
    }
}
// Main window of program.
class MainFrame extends JFrame {
    private JButton decodeButton, encodeButton;
    private JPanel buttonsPanel;
    private JPanel textPanel;
    private JTextField inputTextField;
    private  JLabel infoText;
    private final int width, height;
    private String inputText;

    public MainFrame(String winTitle, int width, int height) {
        // Initializing frame.
        super(winTitle);
        this.width = width;
        this.height = height;
        setSize(width, height);
        setMinimumSize(new Dimension(500, 300));

        // Filling frame with components.
        textPanel = new JPanel();
        inputTextField = new JTextField();
        infoText = new JLabel("Interface for dixxes reverse-engineered RSA encryption.", SwingConstants.CENTER);
        inputTextField.setPreferredSize(new Dimension(300, 30));
        textPanel.add(inputTextField);

        buttonsPanel = new JPanel();
        decodeButton = new JButton("Decode");
        encodeButton = new JButton("Encode");

        // Wiring buttons to listener.
        ActionListener myButtonsListener = new ButtonsListener();
        decodeButton.setActionCommand("decode");
        encodeButton.setActionCommand("encode");
        decodeButton.addActionListener(myButtonsListener);
        encodeButton.addActionListener(myButtonsListener);

        buttonsPanel.add(decodeButton);
        buttonsPanel.add(encodeButton);
        getContentPane().add(BorderLayout.SOUTH, buttonsPanel);
        getContentPane().add(BorderLayout.NORTH, textPanel);
        getContentPane().add(BorderLayout.CENTER, infoText);
    }

    private class ButtonsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent action) {
            // Reacting on buttons.
            switch (action.getActionCommand()) {
                case "encode" : EncodeChoiceDialog(); break;
                case "decode" : DecodeTextDialog(); break;

            }
        }
    }
    // Asking user for existing keys.
    private void EncodeChoiceDialog() {
        int reply = JOptionPane.showConfirmDialog(new JFrame(),
                "Do you have RSA keys to work with?",
                "Information request",
                JOptionPane.YES_NO_CANCEL_OPTION);
        switch (reply) {
            case (JOptionPane.YES_OPTION): {
                BigInteger[] keys = readKeysFromUser();
                EncodeTextDialog(keys);
                break;
            }
            case (JOptionPane.NO_OPTION): {
                // Generating own pairs of keys for user.
                BigInteger[] keys = RSA.genKeys(100000);
                EncodeTextDialog(keys);
                break;
            }
            case (JOptionPane.CANCEL_OPTION): {
                infoText.setText("Operation canceled.");
                break;
            }
        }

    }

    private static BigInteger[] readKeysFromUser() {
        String readValue = JOptionPane.showInputDialog("Provide keys");
        BigInteger[] keys = getBigIntegersFromString(readValue);
        System.out.println(Arrays.toString(keys));
        return keys;
    }
    // Convertor method.
    private static BigInteger[] getBigIntegersFromString(String readValue) {
        List<BigInteger> buffer = new ArrayList<>();
        for(String key : readValue.split(" ")) {
            buffer.add(new BigInteger(key));
        }
        return buffer.toArray(new BigInteger[0]);
    }

    private void EncodeTextDialog(BigInteger[] keys) {
        inputText = inputTextField.getText();
        // Warning if user forgot to write text.
        if (inputText.isEmpty()) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "You should write something to encode it.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.out.println("Soft Error: no text provided.");
            return;
        }

        JOptionPane.showMessageDialog(new JFrame(),
                "You should remember keys!\n Or data will be unavailable",
                "Warning",
                JOptionPane.ERROR_MESSAGE);
        // Tedious way to remove some useless stuff.
        String keysString = Arrays.toString(keys)
                .replace(",", "")
                .replace("[", "")
                .replace("]", "")
                .trim();
        JTextArea keysOutput = BuildOutputDialog(keysString);

        pasteToClipboard(keysString);

        JOptionPane.showMessageDialog(new JFrame(),
                keysOutput,
                "Keys. Copied to your clipboard.",
                JOptionPane.INFORMATION_MESSAGE);

        List<BigInteger> encodedTextBigIntsList = DecoderInterface.encodeText(inputText, keys);

        String encodedTextString = listToString(encodedTextBigIntsList);
        System.out.println(encodedTextString);

        JTextArea textOutput = BuildOutputDialog(encodedTextString);
        pasteToClipboard(encodedTextString);
        JOptionPane.showMessageDialog(new JFrame(),
                textOutput,
                "Copied to your clipboard",
                JOptionPane.INFORMATION_MESSAGE);

        infoText.setText("Successfully encoded");

    }

    // Another converter method.
    private static String listToString(List<BigInteger> list) {
        BigInteger[] encodedTextBigIntsArr = list.toArray(BigInteger[]::new);
        System.out.println(Arrays.toString(encodedTextBigIntsArr));
        String[] encodedTextStrings = Arrays.toString(encodedTextBigIntsArr).split("[\\[\\]]")[1].split(", ");

        return String.join("", encodedTextStrings);
    }

    // Shortcut to clipboard.
    private static void pasteToClipboard(String StringToPaste) {
        StringSelection selectionEncoded = new StringSelection(StringToPaste);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selectionEncoded, null);
    }

    // Shortcut to show information for user.
    private static JTextArea BuildOutputDialog(String dialogContent) {
        JTextArea textOutput = new JTextArea();
        textOutput.setText(dialogContent);
        textOutput.setLineWrap(true);
        textOutput.setEditable(false);
        textOutput.setPreferredSize(new Dimension(300,150));
        return textOutput;
    }

    private void DecodeTextDialog() {
        BigInteger[] keys = readKeysFromUser();
        String encodedMessage = JOptionPane.showInputDialog("Write text");
        String resultString = DecoderInterface.DecodeText(encodedMessage, keys);
        JOptionPane.showMessageDialog(new JFrame(),
                resultString,
                "Decoded message",
                JOptionPane.INFORMATION_MESSAGE);
        infoText.setText("Successfully decoded.");
    }

    
}
