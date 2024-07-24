/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.cafe.form;

import com.cafe.dao.NhanVienDAO;
import com.cafe.dao.PhanCongDAO;
import com.cafe.model.NhanVien;
import com.cafe.model.PhanCong;
import com.cafe.utils.Auth;
import com.cafe.utils.MsgBox;
import com.cafe.utils.XDate;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class PhanCongJPanel extends javax.swing.JPanel {

    public PhanCongJPanel() {
        initComponents();
        init();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        btnTimKiem = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        txtMaPC = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        txtTenCa = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        dateNgayLam = new com.toedter.calendar.JDateChooser();
        cboMaNV = new javax.swing.JComboBox<>();
        txtGhiChu = new javax.swing.JTextField();
        dateGioBatDau = new javax.swing.JTextField();
        dateGioKetThuc = new javax.swing.JTextField();
        btnXoa = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblPhanCong = new javax.swing.JTable();

        jPanel4.setBackground(new java.awt.Color(230, 213, 193));
        jPanel4.setPreferredSize(new java.awt.Dimension(1536, 864));

        jLabel33.setBackground(new java.awt.Color(97, 67, 67));
        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(97, 67, 67));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("QUẢN LÝ PHÂN CÔNG");
        jLabel33.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        btnTimKiem.setBackground(new java.awt.Color(191, 158, 117));
        btnTimKiem.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnTimKiem.setForeground(new java.awt.Color(255, 255, 255));
        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/cafe/icon/search.png"))); // NOI18N
        btnTimKiem.setText("Tìm Kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(97, 67, 67));
        jLabel35.setText("DANH SÁCH PHÂN CÔNG");

        jPanel2.setBackground(new java.awt.Color(230, 213, 193));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(97, 67, 67));
        jLabel32.setText("Mã phân công *");

        txtMaPC.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtMaPC.setPreferredSize(new java.awt.Dimension(300, 22));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(97, 67, 67));
        jLabel31.setText("Ngày làm *");

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(97, 67, 67));
        jLabel40.setText("Tên ca*");

        txtTenCa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTenCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenCaActionPerformed(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(97, 67, 67));
        jLabel41.setText("Giờ bắt đầu * ");

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(97, 67, 67));
        jLabel42.setText("Giờ kết thúc * ");

        jLabel43.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(97, 67, 67));
        jLabel43.setText("Ghi chú *");

        jLabel44.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(97, 67, 67));
        jLabel44.setText("Tên nhân viên*");

        dateNgayLam.setDateFormatString("yyyy-MM-dd");

        cboMaNV.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtGhiChu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        dateGioBatDau.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dateGioBatDau.setPreferredSize(new java.awt.Dimension(300, 22));

        dateGioKetThuc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dateGioKetThuc.setPreferredSize(new java.awt.Dimension(300, 22));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cboMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMaPC, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(dateGioBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(86, 86, 86)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dateNgayLam, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(94, 94, 94)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtTenCa, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dateGioKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtGhiChu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTenCa, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32)
                            .addComponent(jLabel31))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateNgayLam, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaPC, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel41)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(dateGioBatDau, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dateGioKetThuc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel43)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtGhiChu, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel44))
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cboMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        btnXoa.setBackground(new java.awt.Color(191, 158, 117));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/cafe/icon/delete.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(191, 158, 117));
        btnSua.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnSua.setForeground(new java.awt.Color(255, 255, 255));
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/cafe/icon/update.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnThem.setBackground(new java.awt.Color(191, 158, 117));
        btnThem.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/cafe/icon/add.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnLamMoi.setBackground(new java.awt.Color(191, 158, 117));
        btnLamMoi.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnLamMoi.setForeground(new java.awt.Color(255, 255, 255));
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/cafe/icon/lammoi.png"))); // NOI18N
        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        tblPhanCong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã phân công", "Ngày làm", "Tên ca", "Giờ bắt đầu", "Giờ kết thúc", "Ghi chú", "Tên nhân viên"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPhanCong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPhanCongMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblPhanCong);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)
                        .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 1148, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        if (txtTimKiem.getText().isEmpty()) {
            fillAllTable();
            this.clearForm();
            this.row = -1;
            updateStatus();
        } else {
            timKiem();
        }
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        delete();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        if (row != -1) {
            row = -1;
            rowUpdate = 1;
            updateStatus();
            btnThem.setText("Lưu");
            txtMaPC.setEditable(false);
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        String src = evt.getActionCommand();
        if (src.equalsIgnoreCase("Thêm")) {
            insert();
        } else if (src.equalsIgnoreCase("Lưu")) {
            update();
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed

        clearForm();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void tblPhanCongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPhanCongMouseClicked
        if (evt.getClickCount() == 1) {
            this.row = tblPhanCong.getSelectedRow();
            this.edit();
        }
    }//GEN-LAST:event_tblPhanCongMouseClicked

    private void txtTenCaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenCaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenCaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboMaNV;
    private javax.swing.JTextField dateGioBatDau;
    private javax.swing.JTextField dateGioKetThuc;
    private com.toedter.calendar.JDateChooser dateNgayLam;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable tblPhanCong;
    private javax.swing.JTextField txtGhiChu;
    private javax.swing.JTextField txtMaPC;
    private javax.swing.JTextField txtTenCa;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
     PhanCongDAO pcdao = new PhanCongDAO();
    NhanVienDAO nvdao = new NhanVienDAO();
    int row = -1;
    int rowUpdate = -1;

    private void init() {
        this.fillAllTable();
        fillComboBoxNhanVien();
        this.row = -1;
        this.rowUpdate = -1;
        this.updateStatus();

        setBorderInput();
        focusInput();
    }

    void insert() {
        if (checkValidateForm()) {
            PhanCong pc = getForm();
            try {
                pcdao.insert(pc);
                this.fillAllTable();
                this.clearForm();
                MsgBox.alert(this, "Thêm mới thành công!", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
//            throw new RuntimeException(e);
                MsgBox.alert(this, "Thêm mới thất bại", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    void update() {
        if (checkValidateForm()) {
            PhanCong pc = getForm();
            try {
                pcdao.update(pc);
                this.fillAllTable();
                MsgBox.alert(this, "Cập nhật thành công!", JOptionPane.INFORMATION_MESSAGE);
                this.clearForm();
            } catch (Exception e) {
                //throw new RuntimeException(e);
                MsgBox.alert(this, "Cập nhật thất bại", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    void delete() {
        if (!Auth.isManager()) {
            MsgBox.alert(this, "Bạn không có quyền xóa đơn vị!", JOptionPane.WARNING_MESSAGE);
        } else if (MsgBox.confirm(this, "Bạn thực sự muốn xóa đơn vị này?")) {
            String maPC = txtMaPC.getText();
            try {
                pcdao.delete(maPC);
                this.fillAllTable();
                this.clearForm();
                MsgBox.alert(this, "Xóa thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
//                throw new RuntimeException(e);
                MsgBox.alert(this, "Xóa thất bại", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    void clearForm() {
        PhanCong pc = new PhanCong();
        this.setForm(pc);
        this.row = -1;
        this.rowUpdate = -1;
        this.updateStatus();
        tblPhanCong.clearSelection();
        dateGioBatDau.setText("");
        dateGioKetThuc.setText("");
        txtGhiChu.setText("");
        txtTenCa.setText("");
    }

    void edit() {
        String pcString = (String) tblPhanCong.getValueAt(this.row, 0);
        PhanCong pc = pcdao.selectById(pcString);
        this.setForm(pc);
        this.updateStatus();
    }

    void fillAllTable() {
        DefaultTableModel model = (DefaultTableModel) tblPhanCong.getModel();
        model.setRowCount(0);
        try {
            List<PhanCong> list = pcdao.selectAll();
            for (PhanCong pc : list) {
                NhanVien nv = nvdao.selectById(pc.getMaNV());
                String gioBatDauFormatted = pc.getGioBatDau().substring(0, 5); // Extract first 5 chars (HH:mm)
                String gioKetThucFormatted = pc.getGioKetThuc().substring(0, 5); // Extract first 5 chars (HH:mm)
                Object[] row = {pc.getMaPC(), pc.getNgayLam(), pc.getTenCa(), gioBatDauFormatted, gioKetThucFormatted, pc.getGhiChu(), nv.getTenNV()};
                model.addRow(row);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void filltimkiemtable() {
        DefaultTableModel model = (DefaultTableModel) tblPhanCong.getModel();
        model.setRowCount(0);
        try {
            String keyWord = txtTimKiem.getText();
            List<PhanCong> list = pcdao.selectByKeyWord(keyWord);
            if (list.isEmpty()) {
                MsgBox.alert(this, "Không có phân công nào!", JOptionPane.WARNING_MESSAGE);
            } else {
                for (PhanCong pc : list) {
                    NhanVien nv = nvdao.selectById(pc.getMaNV());
                    String gioBatDauFormatted = pc.getGioBatDau().substring(0, 5); // Extract first 5 chars (HH:mm)
                    String gioKetThucFormatted = pc.getGioKetThuc().substring(0, 5); // Extract first 5 chars (HH:mm)
                    Object[] row = {pc.getMaPC(), pc.getNgayLam(), pc.getTenCa(), gioBatDauFormatted, gioKetThucFormatted, pc.getGhiChu(), nv.getTenNV()};
                    model.addRow(row);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String layMaPC(String id) {
        List<PhanCong> list = pcdao.selectAll();
        String ma = id;
        if (list.isEmpty()) {
            ma = ma + "01";
        } else {
            String maBCuoiList = list.get(list.size() - 1).getMaPC();
            int sauMaPC = Integer.valueOf(maBCuoiList.substring(2, maBCuoiList.length())) + 1;
            if (sauMaPC < 10) {
                ma = ma + "0" + sauMaPC;
            } else {
                ma = ma + sauMaPC;
            }
        }
        return ma;
    }

    PhanCong getForm() {
        PhanCong pc = new PhanCong();
        if (rowUpdate == -1) {
            // Insert
            pc.setMaPC(layMaPC("PC"));
        } else {
            // Update
            pc.setMaPC(txtMaPC.getText());
        }
        pc.setTenCa(Integer.parseInt(txtTenCa.getText()));
        pc.setNgayLam(dateNgayLam.getDate());
        pc.setGioBatDau(dateGioBatDau.getText());
        pc.setGioKetThuc(dateGioKetThuc.getText());
        pc.setGhiChu(txtGhiChu.getText());

        List<NhanVien> list = nvdao.selectByTenNV((String) cboMaNV.getSelectedItem());
        for (NhanVien nv : list) {
            pc.setMaNV(nv.getMaNV());
        }
        return pc;
    }

    void setForm(PhanCong pc) {
        txtMaPC.setText(pc.getMaPC());
        dateNgayLam.setDate(pc.getNgayLam());
        txtTenCa.setText(Integer.valueOf((int) pc.getTenCa()) + "");
        if (pc.getGioBatDau() != null) {
            String gioBatDauFormatted = pc.getGioBatDau().substring(0, 5);  // Extract the first 5 characters (HH:mm)
            dateGioBatDau.setText(gioBatDauFormatted);
        } else {
            dateGioBatDau.setText("N/A");
        }

        if (pc.getGioKetThuc() != null) {
            String gioKetThucFormatted = pc.getGioKetThuc().substring(0, 5);  // Extract the first 5 characters (HH:mm)
            dateGioKetThuc.setText(gioKetThucFormatted);
        } else {
            dateGioKetThuc.setText("N/A");
        }
        txtGhiChu.setText(pc.getGhiChu());

        if (pc.getMaNV() == null) {
            cboMaNV.setSelectedIndex(0);
        } else {
            NhanVien nv = nvdao.selectById(pc.getMaNV());
            cboMaNV.setSelectedItem(nv.getTenNV());
        }
    }

    void updateStatus() {
        boolean edit = (this.row >= 0);
        //Trạng thái form
        txtMaPC.setEditable(false);

        btnThem.setEnabled(!edit);
        btnSua.setEnabled(edit);
        btnXoa.setEnabled(edit);
        btnThem.setText("Thêm");
    }

    boolean checkValidateForm() {
        if (dateNgayLam.getDate() == null) {
            MsgBox.alert(this, "Vui lòng nhập ngày làm!", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        Date now = new Date();
        Date ngayLam1 = XDate.toDate(XDate.toString(dateNgayLam.getDate(), "yyyy-MM-dd"), "yyyy-MM-dd");
        if (ngayLam1.after(now)) {
            MsgBox.alert(this, "Ngày làm không được sau ngày hiện tại", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (txtTenCa.getText().isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập tên ca!", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (dateGioBatDau.getText().isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập giờ bắt đầu", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (dateGioKetThuc.getText().isEmpty()) {
            MsgBox.alert(this, "Vui lòng nhập giờ kết thúc", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        try {
            timeFormat.parse(dateGioBatDau.getText());
            timeFormat.parse(dateGioKetThuc.getText());
        } catch (ParseException e) {
            MsgBox.alert(this, "Định dạng giờ không hợp lệ. Vui lòng nhập theo định dạng HH:mm", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            Date startTime = timeFormat.parse(dateGioBatDau.getText());
            Date endTime = timeFormat.parse(dateGioKetThuc.getText());

            if (endTime.before(startTime)) {
                MsgBox.alert(this, "Giờ kết thúc không được trước giờ bắt đầu", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void timKiem() {
        this.filltimkiemtable();
        this.clearForm();
        this.row = -1;
        updateStatus();
    }

    private void focusInput() {
        Border borderNhanVao = BorderFactory.createLineBorder(new Color(227, 188, 140), 10, true);
        Border borderKhongNhan = BorderFactory.createLineBorder(new Color(255, 255, 255), 10, true);
        txtMaPC.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                txtMaPC.setBackground(new Color(227, 188, 140));
                txtMaPC.setBorder(borderNhanVao);
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtMaPC.setBackground(new Color(255, 255, 255));
                txtMaPC.setBorder(borderKhongNhan);
            }
        });
        dateNgayLam.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                dateNgayLam.setBackground(new Color(227, 188, 140));
                dateNgayLam.setBorder(borderNhanVao);
            }

            @Override
            public void focusLost(FocusEvent e) {
                dateNgayLam.setBackground(new Color(255, 255, 255));
                dateNgayLam.setBorder(borderKhongNhan);
            }
        });
        txtTenCa.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                txtTenCa.setBackground(new Color(227, 188, 140));
                txtTenCa.setBorder(borderNhanVao);
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtTenCa.setBackground(new Color(255, 255, 255));
                txtTenCa.setBorder(borderKhongNhan);
            }
        });
        dateGioBatDau.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                dateGioBatDau.setBackground(new Color(227, 188, 140));
                dateGioBatDau.setBorder(borderNhanVao);
            }

            @Override
            public void focusLost(FocusEvent e) {
                dateGioBatDau.setBackground(new Color(255, 255, 255));
                dateGioBatDau.setBorder(borderKhongNhan);
            }
        });
        dateGioKetThuc.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                dateGioKetThuc.setBackground(new Color(227, 188, 140));
                dateGioKetThuc.setBorder(borderNhanVao);
            }

            @Override
            public void focusLost(FocusEvent e) {
                dateGioKetThuc.setBackground(new Color(255, 255, 255));
                dateGioKetThuc.setBorder(borderKhongNhan);
            }
        });
        txtGhiChu.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                txtGhiChu.setBackground(new Color(227, 188, 140));
                txtGhiChu.setBorder(borderNhanVao);
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtGhiChu.setBackground(new Color(255, 255, 255));
                txtGhiChu.setBorder(borderKhongNhan);
            }
        });
        txtTimKiem.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                txtTimKiem.setBackground(new Color(227, 188, 140));
                txtTimKiem.setBorder(borderNhanVao);
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtTimKiem.setBackground(new Color(255, 255, 255));
                txtTimKiem.setBorder(borderKhongNhan);
            }
        });
    }

    private void setBorderInput() {
        Border border = BorderFactory.createLineBorder(new Color(255, 255, 255), 10, true);
        txtTenCa.setBorder(border);
        txtGhiChu.setBorder(border);
        txtTimKiem.setBorder(border);

    }

    void fillComboBoxNhanVien() {
        List<NhanVien> nv = nvdao.selectAll();
        cboMaNV.removeAllItems();
        for (NhanVien NhanVien : nv) {
            cboMaNV.addItem(NhanVien.getTenNV());
        }
    }

}
