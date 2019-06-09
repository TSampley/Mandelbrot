/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mandelbrot;

import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JColorChooser;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Color;

/**
 *
 * @author Taush Sampley
 */
public class ColorArrayCreator extends JPanel {
    DrawSet parent;
    ArrayList<Color> colors;
    Color selected;
    JList list;
    JButton add, remove, apply;
    JTextField detail;
    int num;
    public ColorArrayCreator(DrawSet set) {
        parent = set;
        colors = new ArrayList<Color>();
        selected = Color.BLACK;
        num = 10;

        SpringLayout layout = new SpringLayout();
        ActionListener listener = new ButtonListener();
        setLayout(layout);
        list = new JList();
        list.setPreferredSize(new Dimension(300, 200));
        list.addListSelectionListener(new ListSelector());
        add(list);
        add = new JButton("Add");
        add.addActionListener(listener);
        add(add);
        remove = new JButton("Remove");
        remove.addActionListener(listener);
        add(remove);
        apply = new JButton("Apply");
        apply.addActionListener(listener);
        add(apply);
        detail = new JTextField(3);
        detail.addActionListener(listener);
        add(detail);
        layout.putConstraint("North", list, 5, "North", this);
        layout.putConstraint("West", list, 5, "West", this);
        layout.putConstraint("North", add, 5, "North", this);
        layout.putConstraint("HorizontalCenter", add, 0, "HorizontalCenter", remove);
        layout.putConstraint("West", remove, 5, "East", list);
        layout.putConstraint("North", remove, 5, "South", add);
        layout.putConstraint("North", apply, 5, "South", remove);
        layout.putConstraint("HorizontalCenter", apply, 0, "HorizontalCenter", remove);
        layout.putConstraint("North", detail, 5, "South", apply);
        layout.putConstraint("HorizontalCenter", detail, 0, "HorizontalCenter", remove);
        layout.putConstraint("South", this, 5, "South", list);
        layout.putConstraint("East", this, 5, "East", remove);
    }

    private class ListSelector implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            try {
                Color c = colors.get(e.getFirstIndex());
                selected = c;
            } catch (java.lang.IndexOutOfBoundsException excep) {
                selected = Color.yellow;
            }
        }
    }

    private class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == add) {
                addColor();
                System.out.println("colors");
                for (Color c : colors)
                    System.out.println(c);
            } else if (source == remove) {
                removeColor();
                System.out.println("colors");
                for (Color c : colors)
                    System.out.println(c);
            } else if (source == apply) {
                Color first = colors.get(0);
                Color[] remaining = new Color[colors.size()-1];
                for (int i = 1; i < remaining.length+1; i++) {
                    remaining[i-1] = colors.get(i);
                }
                parent.resetGradientArray(Integer.parseInt(detail.getText()),first, remaining);
            } else if (source == detail) {
                
            }
        }
    }

    public void addColor() {
        Color c = JColorChooser.showDialog(parent, "Select a Color", Color.yellow);
        if (c != null) {
            colors.add(c);
            Color[] colorArr = new Color[colors.size()];
            System.out.println("ADD");
            for (int i = 0; i < colorArr.length; i++) {
                colorArr[i] = colors.get(i);
                System.out.println(colorArr[i]);
            }
            list.setListData(colorArr);
        }
    }

    public void removeColor() {
        colors.remove(selected);
        Color[] colorArr = new Color[colors.size()];
        for (int i = 0; i < colorArr.length; i++)
            colorArr[i] = colors.get(i);
        list.setListData(colorArr);
    }
}
