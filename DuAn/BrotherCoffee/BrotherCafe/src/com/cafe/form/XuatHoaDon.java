package com.cafe.form;

import com.cafe.dao.BanDAO;
import com.cafe.dao.ChiTietHoaDonDAO;
import com.cafe.dao.HoaDonDAO;
import com.cafe.dao.KhachHangDAO;
import com.cafe.dao.NhanVienDAO;
import com.cafe.dao.SanPhamDAO;
import com.cafe.model.Ban;
import com.cafe.model.ChiTietHoaDon;
import com.cafe.model.HoaDon;
import com.cafe.model.KhachHang;
import com.cafe.model.NhanVien;
import com.cafe.model.SanPham;
import com.cafe.utils.XDate;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author ADMIN
 */
public class XuatHoaDon extends javax.swing.JFrame {
    HoaDonDAO hddao = new HoaDonDAO();
    KhachHangDAO khdao = new KhachHangDAO();
    NhanVienDAO nvdao = new NhanVienDAO();
    ChiTietHoaDonDAO cthddao = new ChiTietHoaDonDAO();
    SanPhamDAO spdao = new SanPhamDAO();
    BanDAO bdao = new BanDAO();
    
     public void XHDLayDuLieu(int ma){
//        ed_Page.setEditable(false);
//        txtLoad.setEditable(false);
        
        DecimalFormat formatTienVND = new DecimalFormat("###,###.###");
        HoaDon hd = hddao.selectById(ma);
        NhanVien nv = nvdao.selectById(hd.getMaNV());
        Ban b = bdao.selectById(hd.getMaBan());
        String khachHang = null;
        if (hd.getMaKH() != null) {
            KhachHang kh = khdao.selectById(hd.getMaKH());
            khachHang = kh.getTenKH();
        } else {
            khachHang = "Khách lẻ";
        }
        
        String html = "<html lang=\"en\" style=\"font-family:Arial, Helvetica, sans-serif; font-weight: normal; width: 300px;\">\n"
            + "  <body style=\"margin: 0px;\">\n"
            + "      <h2 style=\"text-align: center; margin-bottom: 5px; font-size: 10px;\">BROTHER'S COFFEE SHOP</h2>\n"
            + "      <p style=\"text-align: center;margin: 0px 0px 0px 0px; font-size: 8pt; \">\n"
            + "        Đ. Số 22, Thường Thạnh, Cái Răng,<br>Cần Thơ\n"
            + "      </p>\n"
            + "      <p style=\"text-align: center; margin: 5px 0px 0px 0px; font-size: 8pt; \" >SĐT: 0791234567<br /></p>\n"
            + "      <h3 style=\"text-align: center; margin: 10px 0px 0px 0px; font-size: 8pt;\">HÓA ĐƠN THANH TOÁN</h3>\n"
            + "      <h3 style=\"text-align: center; margin: 5px 0px 0px 0px; font-size: 8pt;\">"+b.getTenBan()+"</h3>\n"
            + "      \n"
            + "      <table style=\"margin-left: auto; margin-right: auto; margin-top: 5px;\">\n"
            + "      <tr>\n"
            + "        <td style=\"font-weight: bold; font-size: 7pt; padding-right: -10px;\">Số:</td>\n"
            + "        <td style=\"font-size: 7pt;font-weight: bold; padding-left: -10px;\">"+hd.getMaHD()+"</td>\n"
            + "        <td style=\"font-weight: bold; font-size: 7pt;font-weight: bold; padding-right: -10px; \">KH:</td>\n"
            + "        <td style=\"font-size: 7pt;font-weight: bold; padding-left:-10px;\">"+khachHang+"</td>\n"
            + "      </tr>\n"
            + "      <tr>\n"
            + "        <td style=\"font-weight: bold;  font-size: 7pt; width: 5px; padding-right: -10px;\" >Ngày:</td>\n"
            + "        <td style=\"font-size: 7pt;font-weight: bold; padding-left: -10px;\">"+ XDate.toString(hd.getNgayThanhToan(), "yyyy/MM/dd")+"</td>\n"
            + "        <td style=\" font-weight: bold;  font-size: 7pt; width: 5px\">NV:</td>\n"
            + "        <td style=\"font-size: 7pt;font-weight: bold; padding-left:-10px;\">"+ nv.getTenNV() +"</td>\n"
            + "      </tr>\n"
            + "      <tr>\n"
            + "        <td style=\" font-weight: bold;  font-size: 7pt; padding-right: -10px;\">Vào:</td>\n"
            + "        <td style=\"padding-left: -5px;font-size: 7pt;font-weight: bold; padding-left: -10px;\">"+hd.getThoiGianTaoHD().substring(0, 8)+"</td>\n"
            + "        <td style=\" font-weight: b1old;  font-size: 7pt;\">Ra:</td>\n"
            + "        <td style=\"padding-left: -5px;font-size: 7pt;font-weight: bold; padding-left:-10px;\">"+hd.getThoiGianThanhToan().substring(0, 8)+"</td>\n"
            + "    </tr>\n"
            + "    </table>                       \n"
            + "    <div style=\"text-align: center; max-width:90px\">--------------------------------</div>\n"
            + "        <table style=\"width: 120px;\">\n"
            + "          <tr  style=\"height: 20px;\"> \n"
            + "                <th style=\"font-weight:bold; width: 32px; font-size: 7pt; \">T.UỐNG</th>\n"
            + "                <th style=\"font-weight:bold; width: 18px; font-size: 7pt;\">SL</th>\n"
            + "                <th style=\"font-weight:bold; width: 20px; font-size: 7pt;\">Đ.GIÁ</th>\n"
            + "                <th style=\"font-weight:bold; width: 20px; font-size: 7pt;\">T.TIỀN</th>\n"
            + "            </tr>\n";
            List<ChiTietHoaDon> cthd = cthddao.selectByMaHD(hd.getMaHD());
            for (ChiTietHoaDon cthdd : cthd) {
                SanPham sp = spdao.selectById(cthdd.getMaSP());
                String ghiChu = null;
                if(cthdd.getGhiChu()!= null){ 
                    ghiChu = cthdd.getGhiChu();
                } else { 
                    ghiChu = "";
                }

                html += "<tr style=\"text-align: center;\">\n";
                html += "<td style=\"font-size: 7pt;font-weight: bold;  width: 32px; \">" +sp.getTenSP()+ "</td>\n";
                html += "<td style=\"font-size: 7pt;font-weight: bold;  width: 18px;\">"+ cthdd.getSoLuong()+"</td>\n";
                html += "<td style=\"font-size: 7pt;font-weight: bold; width: 20px;\">" +formatTienVND.format(sp.getGia())+"</td>\n";
                html += "<td style=\"font-size: 7pt;font-weight: bold; width: 20px;\">"+formatTienVND.format(cthdd.getTongTienSP())+"</td>\n";
                html += "</tr>";
            }                             
            html +="            </tr>\n"
            + "          <tr>\n"
            + "            <th colspan=\"3\" style=\"text-align: left; padding-left: 5px; font-size: 8pt;\">Tổng tiền:</th>\n"
            + "            <td colspan=\"2\" style=\"padding-left: -5px; font-size: 8pt; font-weight: bold; margin-left:2px;\" >"+formatTienVND.format(hd.getTongTien()) + "</td>\n"
            + "          </tr>\n"
            + "        </table>\n"
            + "        <div style=\"text-align: center;font-weight: normal;\">--------------------------------</div>\n"
            + "        <h4 style=\"text-align: center; margin: 7pt 0px 0px 0px;font-size: 8px;\">XIN CẢM ƠN - HẸN GẶP LẠI!<br></h4> \n"
            + "</body>\n"
            + "</html>";
        ed_Page.setContentType("text/html");
        ed_Page.setText(html);
     }
    /**
     * Creates new form xuatHoaDon1
     */
    public XuatHoaDon() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnHuy = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtLoad = new javax.swing.JTextArea();
        btn_Print = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        ed_Page = new javax.swing.JEditorPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnHuy.setText("Hủy");
        btnHuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyActionPerformed(evt);
            }
        });

        txtLoad.setColumns(20);
        txtLoad.setRows(5);
        jScrollPane2.setViewportView(txtLoad);

        btn_Print.setText("Print");
        btn_Print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_PrintActionPerformed(evt);
            }
        });

        jScrollPane3.setPreferredSize(new java.awt.Dimension(80, 100));

        ed_Page.setMinimumSize(new java.awt.Dimension(80, 20));
        ed_Page.setPreferredSize(new java.awt.Dimension(80, 20));
        jScrollPane3.setViewportView(ed_Page);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(160, 160, 160)
                .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn_Print, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Print, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHuy, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane3.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    

String html = "";
    private void btn_PrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_PrintActionPerformed
        try {
            ed_Page.print();
        } catch (PrinterException ex) {
            Logger.getLogger(XuatHoaDon.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btn_PrintActionPerformed

    private void btnHuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnHuyActionPerformed

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
            java.util.logging.Logger.getLogger(XuatHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(XuatHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(XuatHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(XuatHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new XuatHoaDon().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHuy;
    private javax.swing.JButton btn_Print;
    private javax.swing.JEditorPane ed_Page;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea txtLoad;
    // End of variables declaration//GEN-END:variables

}
