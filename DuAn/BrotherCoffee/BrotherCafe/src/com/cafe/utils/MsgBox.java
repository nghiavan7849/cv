/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.utils;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author NGHIA
 */
public class MsgBox {
    public static void alert(Component parent, String message, int type) {
        JOptionPane.showMessageDialog(parent, message,"Hệ thống quản lý cafe",type);
    }
    public static boolean confirm(Component parent, String message){
        int result = JOptionPane.showConfirmDialog(parent, message,
                "Hệ thống quản lý cafe",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
    public static String prompt(Component parent, String message){ 
        return JOptionPane.showInputDialog(parent,message,"Hệ thống quản lý cafe",JOptionPane.INFORMATION_MESSAGE);
    }
}
