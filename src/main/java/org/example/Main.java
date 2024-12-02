package org.example;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        MainFrame mainFrame = new MainFrame("RSA machine", 500, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }
}

class MainFrame extends JFrame {
    private JButton decodeButton, encodeButton;
    private JPanel buttonsPanel;
    private JPanel textPanel;
    private JTextField inputTextField;
    private  JLabel infoText;
    private final int width, height;
    private String inputedText;
    public MainFrame(String winTitle, int width, int height) {
        super(winTitle);
        this.width = width;
        this.height = height;
        setSize(width, height);
        setMinimumSize(new Dimension(500, 300));

        textPanel = new JPanel();
        inputTextField = new JTextField();
        infoText = new JLabel("Interface for dixxes reverse-engineered RSA encrytpyion.", SwingConstants.CENTER);
        inputTextField.setPreferredSize(new Dimension(300, 30));
        textPanel.add(inputTextField);


        buttonsPanel = new JPanel();
        decodeButton = new JButton("Decode");
        encodeButton = new JButton("Encode");

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
            // Reaction on events
            switch (action.getActionCommand()) {
                case "encode" : EncodeChoice(); break;
                case "decode" : DecodeText(); break;

            }
        }
    }

    private void EncodeChoice() {
        int reply = JOptionPane.showConfirmDialog(new JFrame(),
                "Do you have RSA keys to work with?",
                "Information request",
                JOptionPane.YES_NO_CANCEL_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            BigInteger[] keys = readValueFromUser("Provide keys");
            EncodeText(keys);
        } else {
            BigInteger[] keys = RSA.genKeys(100000);
            EncodeText(keys);
        }
    }

    private static BigInteger[] readValueFromUser(String informationContext) {
        String readedValue = JOptionPane.showInputDialog(informationContext);
        List<BigInteger> buffer = new ArrayList<>();
        for(String key : readedValue.split(" ")) {
            buffer.add(new BigInteger(key));
        }
        BigInteger[] keys = buffer.toArray(new BigInteger[0]);
        System.out.println(Arrays.toString(keys));
        return keys;
    }

    private void EncodeText(BigInteger[] keys) {
        JOptionPane.showMessageDialog(new JFrame(),
                "You should remember keys!\n Or data will be unavailable",
                "Warning",
                JOptionPane.ERROR_MESSAGE);

        JTextArea keysOutput = new JTextArea();
        String keysString = Arrays.toString(keys)
                .replace(",", "")
                .replace("[", "")
                .replace("]", "")
                .trim();
        keysOutput.setText(keysString);
        keysOutput.setEditable(false);

        StringSelection selectionEncoded = new StringSelection(keysString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selectionEncoded, null);

        JOptionPane.showMessageDialog(new JFrame(),
                keysOutput,
                "Keys. Copied to your clipboard.",
                JOptionPane.INFORMATION_MESSAGE);

        inputedText = inputTextField.getText();
        List<BigInteger> encodedText = DecoderInterface.encodeText(inputedText, keys);

        JTextArea textOutput = new JTextArea();
        String encodedTextString = encodedText.toString()
                .replace(",", "")
                .replace("[", "")
                .replace("]", "")
                .trim();
        textOutput.setText(encodedTextString);
        textOutput.setLineWrap(true);
        textOutput.setEditable(false);
        textOutput.setPreferredSize(new Dimension(300,150));
        selectionEncoded = new StringSelection(encodedTextString);
        clipboard.setContents(selectionEncoded, null);
        JOptionPane.showMessageDialog(new JFrame(),
                textOutput,
                "Copied to your clipboard",
                JOptionPane.INFORMATION_MESSAGE);

        infoText.setText("Successfully encoded");

    }

    private void DecodeText() {
        BigInteger[] keys = readValueFromUser("Provide keys D/N keys respectively");
        BigInteger[] encodedMessage = readValueFromUser("Provide coded message");
        List<BigInteger> decodedMessage = new ArrayList<>();
        for (BigInteger code : encodedMessage) {
            decodedMessage.add(RSA.decrypt(code, keys[1], keys[0]));
        }
        String resultString = "";
        for (BigInteger wrongByte : decodedMessage) {
            resultString = resultString.concat(new String(wrongByte.toByteArray(), StandardCharsets.UTF_8));
        }
        JOptionPane.showMessageDialog(new JFrame(),
                resultString,
                "Decoded message",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
