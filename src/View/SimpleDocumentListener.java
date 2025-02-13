package View;

import Controller.DatVeController;
import Model.*;
import Service.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.sql.Date;
import java.text.SimpleDateFormat;

class SimpleDocumentListener implements DocumentListener {
    public void update() {
        // To be overridden
    }
    
    @Override
    public void insertUpdate(DocumentEvent e) {
        update();
    }
    
    @Override
    public void removeUpdate(DocumentEvent e) {
        update();
    }
    
    @Override
    public void changedUpdate(DocumentEvent e) {
        update();
    }
}