package nl.Databeest.TabItems.Room.Create;

import nl.Databeest.TabItems.SubMenuItem;

import javax.swing.*;
import java.awt.*;

/**
 * Created by timok on 15-12-16.
 */
public class CreateRoom extends SubMenuItem {
    private JPanel mainPanel;
    private JTextField textField1;

    @Override
    protected String getMenuItemName() {
        return "Create";
    }

    @Override
    protected Component getMainPanel() {
        return mainPanel;
    }
}
