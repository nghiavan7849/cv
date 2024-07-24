/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.cafe.main;

import com.cafe.component.Menu;
import com.cafe.dao.DonViDao;
import com.cafe.event.EventMenuSelected;
import com.cafe.form.BanJPanel;
import com.cafe.form.DangNhapJDialog;
import com.cafe.form.DoiMatKhauJPanel;
import com.cafe.form.DonViJPanel;
import com.cafe.form.GiaoDienChaoJDialog;
import com.cafe.form.KhachHangJPanel;
import com.cafe.form.KhuVucJPanel;
import com.cafe.form.NhanVienJPanel;
import com.cafe.form.PhanCongJPanel;
import com.cafe.form.SanPhamJPanel;
import com.cafe.form.ThongKeBaoCaoJPanel;
import com.cafe.form.TrangChuJPanel;
import com.cafe.model.DonVi;
import com.cafe.model.ModelMenu;
import com.cafe.utils.Auth;
import com.cafe.utils.MsgBox;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

/**
 *
 * @author NGHIA
 */
public class MainJFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainJFrame
     */
    private Menu menu = new Menu();
    private JPanel main = new JPanel();
    private MigLayout layout;
    private Animator animator;
    private boolean menuShow;

    public MainJFrame() {
        initComponents();
        new DangNhapJDialog(this, true).setVisible(true);
        new GiaoDienChaoJDialog(this, true).setVisible(true);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        this.setSize(screenWidth, screenHeight - 50);
//        System.out.println(screenWidth + "|" + screenHeight);
        init();

    }

    private void init() {
        layout = new MigLayout("fill", "0[]10[]0", "0[fill]0");
        body.setLayout(layout);
        main.setOpaque(false);
        main.setLayout(new BorderLayout());

        menu.addEventMenu(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!animator.isRunning()) {
                    animator.start();
                }
            }

        });

        menu.setEvent(new EventMenuSelected() {
            @Override
            public void selected(int index) {
                if (index == 0) {
                    showForm(new TrangChuJPanel());
                }
                if (index == 1) {
                    showForm(new SanPhamJPanel());
                }
                if (index == 2) {
                    if (Auth.isManager()) {
                        showForm(new NhanVienJPanel());
                    } else {
                        MsgBox.alert(null, "Bạn không có quyền xem nhân viên!", JOptionPane.WARNING_MESSAGE);
                    }
                }
                if (index == 3) {
                    if (Auth.isManager()) {
                        showForm(new KhachHangJPanel());
                    } else {
                        MsgBox.alert(null, "Bạn không có quyền xem khu vực!", JOptionPane.WARNING_MESSAGE);
                    }
                }
                if (index == 4) {
                    if (Auth.isManager()) {
                        showForm(new BanJPanel());
                    } else {
                        MsgBox.alert(null, "Bạn không có quyền xem bàn!", JOptionPane.WARNING_MESSAGE);
                    }
                }
                if (index == 5) {
                    showForm(new KhuVucJPanel());
                }
                if (index == 6) {
                    if (Auth.isManager()) {
                        showForm(new ThongKeBaoCaoJPanel());
                    } else {
                        MsgBox.alert(null, "Bạn không có quyền xem thống kê!", JOptionPane.WARNING_MESSAGE);
                    }
                }
                if (index == 7) {
                    showForm(new DoiMatKhauJPanel());
                }
                if (index == 8) {
                    if (Auth.isManager() && Auth.user.getMaDV().equals("DV01")) {
                        showForm(new DonViJPanel());   
                    } else {
                        MsgBox.alert(null, "Bạn không có quyền xem đơn vị!", JOptionPane.WARNING_MESSAGE);
                    }
                }
                if (index == 9) {
                    if (Auth.isManager()) {
                        showForm(new PhanCongJPanel());
                    } else {
                        MsgBox.alert(null, "Bạn không có quyền xem phân công!", JOptionPane.WARNING_MESSAGE);

                    }
                }
                if (index == 10) {
                    new MainJFrame();
                }
                if (index == 11) {
                    System.exit(0);
                }
            }

        });
        menu.addMenu(new ModelMenu("Trang chủ", new ImageIcon(getClass().getResource("/com/cafe/icon/home.png"))));
        menu.addMenu(new ModelMenu("Sản phẩm", new ImageIcon(getClass().getResource("/com/cafe/icon/sanpham.png"))));
        menu.addMenu(new ModelMenu("Nhân viên", new ImageIcon(getClass().getResource("/com/cafe/icon/nhanvien.png"))));
        menu.addMenu(new ModelMenu("Khách hàng", new ImageIcon(getClass().getResource("/com/cafe/icon/customer.png"))));
        menu.addMenu(new ModelMenu("Bàn", new ImageIcon(getClass().getResource("/com/cafe/icon/table.png"))));
        menu.addMenu(new ModelMenu("Khu vực", new ImageIcon(getClass().getResource("/com/cafe/icon/khuvuc.png"))));
        menu.addMenu(new ModelMenu("Thông kê và báo cáo", new ImageIcon(getClass().getResource("/com/cafe/icon/analytics.png"))));
        menu.addMenu(new ModelMenu("Đổi mật khẩu", new ImageIcon(getClass().getResource("/com/cafe/icon/refresh.png"))));
        menu.addMenu(new ModelMenu("Đơn vị", new ImageIcon(getClass().getResource("/com/cafe/icon/donvi.png"))));
        menu.addMenu(new ModelMenu("Phân công", new ImageIcon(getClass().getResource("/com/cafe/icon/phancong.png"))));
        menu.addMenu(new ModelMenu("Đăng xuất", new ImageIcon(getClass().getResource("/com/cafe/icon/logout.png"))));
        menu.addMenu(new ModelMenu("Thoát", new ImageIcon(getClass().getResource("/com/cafe/icon/exit.png"))));
        
        DonViDao dvdao = new DonViDao();
        DonVi dv = dvdao.selectById(Auth.user.getMaDV());
        menu.addMenu(new ModelMenu(" ",null));
        menu.addMenu(new ModelMenu(" ",null));
        menu.addMenu(new ModelMenu("Đơn vị: " + dv.getTenDV(),null));
        body.add(menu, "w 55!");
        body.add(main, "w 100%");
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                double width;
                if (menuShow) {
                    width = 55 + (150 * (1f - fraction));
                    menu.setAlpha(1f - fraction);
                } else {
                    width = 100 + (150 * fraction);
                    menu.setAlpha(fraction);
                }
                layout.setComponentConstraints(menu, "w " + width + "!");
                body.revalidate();
            }

            @Override
            public void end() {
                menuShow = !menuShow;
            }

        };
        animator = new Animator(450, target);
        animator.setResolution(50);
        animator.setAcceleration(0.1f);
        animator.setDeceleration(0.1f);
        showForm(new TrangChuJPanel());
    }

    private void showForm(Component com) {
        main.removeAll();
        main.add(com);
        main.repaint();
        main.revalidate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        body = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        body.setBackground(new java.awt.Color(230, 213, 193));

        javax.swing.GroupLayout bodyLayout = new javax.swing.GroupLayout(body);
        body.setLayout(bodyLayout);
        bodyLayout.setHorizontalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 665, Short.MAX_VALUE)
        );
        bodyLayout.setVerticalGroup(
            bodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel body;
    // End of variables declaration//GEN-END:variables
}
