package test;

import javax.swing.*;
import java.awt.*;

public class TestSwing extends JFrame {

    public TestSwing() throws HeadlessException {
        super();
        this.setVisible(true);
        this.setTitle("TestSwing");
        this.setSize(400,400);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//        this.setLayout(new FlowLayout());
        this.setLayout(new GridLayout(3,2));

        JPanel panel1 = new JPanel();
        JButton b1  = new JButton("Nom");
        JTextField t1 = new JTextField(10);
        this.getContentPane().add(b1);
        this.getContentPane().add(t1);
        b1.addActionListener(e-> System.out.println(t1.getText()));

        JPanel panel2 = new JPanel();
        JButton b2  = new JButton("Téléphone");
        JTextField t2 = new JTextField(10);
        this.getContentPane().add(b2);
        this.getContentPane().add(t2);
        b2.addActionListener(e-> System.out.println(t2.getText()));

        JPanel panel3 = new JPanel();
        JButton b3  = new JButton("Adresse");
        JTextField t3 = new JTextField(10);
        this.getContentPane().add(b3);
        this.getContentPane().add(t3);
        b3.addActionListener(e-> System.out.println(t3.getText()));
        pack();


//        this.getContentPane().add(panel1);
//        this.getContentPane().add(panel2);
//        this.getContentPane().add(panel3);
    }
    public static void main(String[] args) {
        TestSwing ex2 = new TestSwing();
    }
}
