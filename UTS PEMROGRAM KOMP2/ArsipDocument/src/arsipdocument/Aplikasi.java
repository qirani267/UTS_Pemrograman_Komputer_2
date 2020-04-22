/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arsipdocument;

import java.awt.Frame;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ACER
 */
public class Aplikasi extends javax.swing.JFrame {
    int id= 0;
    String role;
    DefaultTableModel model;
    String filename = null;
    /**
     * Creates new form JavaMySQL
     */
    public Aplikasi() {
        initComponents();
//        KoneksiDB.sambungDB();
        aturModelTabel();
        kategori_dok();
        showForm(false);
        showData("");
    }

    private void aturModelTabel() {
        Object[] kolom = {"id","Kode Dok","Nama Dok","Kategori Dok","lokasi Dok","Deskripsi Dok","tanggal"};
        model = new DefaultTableModel(kolom, 0) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        tblarsip.setModel(model);
        tblarsip.setRowHeight(20);
        tblarsip.getColumnModel().getColumn(0).setMinWidth(0);
        tblarsip.getColumnModel().getColumn(0).setMaxWidth(0);
    }

    private void showForm(boolean b) {
        areaSplit.setDividerLocation(0.3);
        areaSplit.getLeftComponent().setVisible(b);
    }

    private void resetForm() {
        tblarsip.clearSelection();
        jkode.setText("");
        jnama.setText("");
        cmbkategori.setSelectedIndex(0);
        jlokasi.setText("");
        jdeskripsi.setText("");
        jtanggal.setText("");
        jkode.requestFocus();
    }

    private void kategori_dok() {
        cmbkategori.removeAllItems();
        cmbkategori.addItem("Pilih Kategori");
        cmbkategori.addItem("Surat masuk");
        cmbkategori.addItem("Surat keluar");
        
    }

    private void showData(String key) {
        model.getDataVector().removeAllElements();
        String where = "";
        if (!key.isEmpty()) {
            where += "WHERE kode_dok LIKE '%" + key + "%' "
                    + "OR nama_dok LIKE '%" + key + "%' "
                    + "OR kategori_dok LIKE '%" + key + "%' "
                    + "OR lokasi_document LIKE '%" + key + "%' "
                    + "OR deskripsi_dok LIKE '%" + key + "%'"
                    + "OR tanggal LIKE '%" + key + "%'";
        }
        String sql = "SELECT * FROM data_arsip " + where;
        Connection con;
        Statement st;
        ResultSet rs;
        int baris = 0;
        try {
            con = koneksi.MySQL();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                Object id = rs.getInt(1);
                Object kode_dok = rs.getInt(2);
                Object nama_dok = rs.getString(3);
                Object kategori_dok = rs.getString(4);
                Object lokasi_dok = rs.getString(5);
                Object deskripsi_dok = rs.getString(6);
                Object tanggal = rs.getString(7);
                Object[] data = {id, kode_dok, nama_dok, kategori_dok, lokasi_dok, deskripsi_dok, tanggal};
                model.insertRow(baris, data);
                baris++;
            }
            st.close();
            con.close();
            tblarsip.revalidate();
            tblarsip.repaint();
        } catch (SQLException e) {
            System.err.println("showData(): " + e.getMessage());
        }
    }

    private void resetView() {
        resetForm();
        showForm(false);
        showData("");
        btnHapus.setEnabled(false);
        id = 0;
    }

    private void pilihData(String n) {
        btnHapus.setEnabled(true);
        String sql = "SELECT * FROM data_arsip WHERE id='" + n + "'";
        Connection con;
        Statement st;
        ResultSet rs;
        try {
            con = koneksi.MySQL();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt(1);
                String kode_dok = rs.getString(2);
                String nama_dok = rs.getString(3);
                Object kategori_dok = rs.getString(4);
                String lokasi_dok = rs.getString(5);
                String deskripsi_dok = rs.getString(6);
                String tanggal = rs.getString(7);
                id = id;
                jkode.setText(kode_dok);
                jnama.setText(nama_dok);
                cmbkategori.setSelectedItem(kategori_dok);
                jlokasi.setText(lokasi_dok);
                jdeskripsi.setText(deskripsi_dok);
                jtanggal.setText(tanggal);
            }
            st.close();
            con.close();
            showForm(true);
        } catch (SQLException e) {
            System.err.println("pilihData(): " + e.getMessage());
        }
    }

    private void simpanData() {
        String kode_dok = jkode.getText();
        String nama_dok = jnama.getText();
        int kategori_dok = cmbkategori.getSelectedIndex();
        String lokasi_dok = jlokasi.getText();
        String deskripsi_dok =jdeskripsi.getText();
        String tanggal = jtanggal.getText();
        if (kode_dok.isEmpty() || nama_dok.isEmpty() || kategori_dok == 0 || lokasi_dok.isEmpty() || deskripsi_dok.isEmpty()
                || tanggal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lengkapi Data Anda!");
        } else {
            String kategori = cmbkategori.getSelectedItem().toString();
            String sql
                    = "INSERT INTO data_arsip (kode_dok, nama_dok, kategori_dok, lokasi_dok, deskripsi_dok, tanggal)"
                    + "VALUES ('" + kode_dok + "','" + nama_dok + "','" + kategori_dok + "','" + lokasi_dok + "','" + deskripsi_dok + "','"+tanggal+"')";
            Connection con;
            Statement st;
            try {
                con = koneksi.MySQL();
                st = con.createStatement();
                st.executeUpdate(sql);
                st.close();
                con.close();
                resetView();
                JOptionPane.showMessageDialog(this,"Data telah disimpan");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void ubahData() {
        String kode_dok = jkode.getText();
        String nama_dok = jnama.getText();
        int kategori_dok = cmbkategori.getSelectedIndex();
        String lokasi_dok = jlokasi.getText();
        String deskripsi_dok =jdeskripsi.getText();
        String tanggal = jtanggal.getText();
        if (kode_dok.isEmpty() || nama_dok.isEmpty() || kategori_dok == 0 || lokasi_dok.isEmpty() || deskripsi_dok.isEmpty()
                || tanggal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lengkapi Data Anda");
        } else {
            String kategori = cmbkategori.getSelectedItem().toString();
            String sql = "UPDATE data_arsip "
                    + "SET kode_dok='" + kode_dok + "',"
                    + "nama_dok='" + nama_dok + "',"
                    + "kategori_dok='" + kategori_dok + "',"
                    + "lokasi_dok='" +lokasi_dok+ "',"
                    + "deskripsi_dok='" +deskripsi_dok+ "',"
                    + "tanggal='" + tanggal + "' WHERE id='" + id + "'";
            Connection con;
            Statement st;
            try {
                con = koneksi.MySQL();
                st = con.createStatement();
                st.executeUpdate(sql);
                st.close();
                con.close();
                resetView();
                JOptionPane.showMessageDialog(this, "Data telah diubah!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private void hapusData(int baris) {
        Connection con;
        Statement st;
        try {
            con = koneksi.MySQL();
            st = con.createStatement();
            st.executeUpdate("DELETE FROM data_arsip WHERE id=" + baris);
            st.close();
            con.close();
            resetView();
            JOptionPane.showMessageDialog(this, "Data telah dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnTambah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        txtCari = new java.awt.TextField();
        btnCari = new javax.swing.JButton();
        areaSplit = new javax.swing.JSplitPane();
        panelKiri = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jkode = new javax.swing.JTextField();
        jnama = new javax.swing.JTextField();
        cmbkategori = new javax.swing.JComboBox<>();
        jdeskripsi = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        btntutup = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jtanggal = new javax.swing.JTextField();
        jlokasi = new javax.swing.JTextField();
        browse = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblarsip = new javax.swing.JTable();
        btnLogout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        btnTambah.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        btnTambah.setText("Tambah Data");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnHapus.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        btnHapus.setText("Hapus Data");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        txtCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariKeyReleased(evt);
            }
        });

        btnCari.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        btnCari.setText("Cari");

        panelKiri.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel6.setText("Kode Document");

        jLabel7.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel7.setText("Nama Document");

        jLabel8.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel8.setText("Kategori Document");

        jLabel9.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel9.setText("Lokasi Document");

        jLabel10.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel10.setText("Doskripsi Document");

        jkode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jkodeActionPerformed(evt);
            }
        });

        jnama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jnamaActionPerformed(evt);
            }
        });

        cmbkategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btntutup.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        btntutup.setText("TUTUP");
        btntutup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntutupActionPerformed(evt);
            }
        });

        btnSimpan.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Calibri", 1, 14)); // NOI18N
        jLabel11.setText("Tanggal");

        jtanggal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtanggalActionPerformed(evt);
            }
        });

        jlokasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jlokasiActionPerformed(evt);
            }
        });

        browse.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        browse.setText("browse");
        browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelKiriLayout = new javax.swing.GroupLayout(panelKiri);
        panelKiri.setLayout(panelKiriLayout);
        panelKiriLayout.setHorizontalGroup(
            panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKiriLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKiriLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jnama, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKiriLayout.createSequentialGroup()
                        .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelKiriLayout.createSequentialGroup()
                                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelKiriLayout.createSequentialGroup()
                                            .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel8)
                                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jkode, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelKiriLayout.createSequentialGroup()
                                            .addGap(75, 75, 75)
                                            .addComponent(jLabel11)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(cmbkategori, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(panelKiriLayout.createSequentialGroup()
                                                    .addComponent(jlokasi, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                                                    .addComponent(browse))
                                                .addComponent(jdeskripsi, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jtanggal, javax.swing.GroupLayout.Alignment.TRAILING))))
                                    .addGroup(panelKiriLayout.createSequentialGroup()
                                        .addGap(54, 54, 54)
                                        .addComponent(btntutup)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(38, 38, 38)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jSeparator1))
                        .addGap(23, 23, 23))))
        );
        panelKiriLayout.setVerticalGroup(
            panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKiriLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelKiriLayout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jkode)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jnama, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbkategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jlokasi)
                    .addComponent(browse))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jdeskripsi, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtanggal)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelKiriLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btntutup)
                    .addComponent(btnSimpan))
                .addGap(89, 89, 89))
        );

        areaSplit.setLeftComponent(panelKiri);

        tblarsip.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblarsip.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblarsipMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblarsip);

        areaSplit.setRightComponent(jScrollPane1);

        btnLogout.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
        btnLogout.setText("Keluar");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addGap(147, 147, 147)
                        .addComponent(btnHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 209, Short.MAX_VALUE)
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(areaSplit, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(btnLogout)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnCari)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTambah)
                            .addComponent(btnHapus))
                        .addComponent(txtCari, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(areaSplit, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        role = "Tambah";
        btnSimpan.setText("SIMPAN");
        id = 0;
        resetForm();
        showForm(true);
        btnHapus.setEnabled(false);
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        if (id == 0) {
            JOptionPane.showMessageDialog(this, "Dihapus!");
        } else{
            hapusData(id);
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void txtCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariKeyReleased
        // TODO add your handling code here:
        String key = txtCari.getText();
        showData(key);
    }//GEN-LAST:event_txtCariKeyReleased

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        // TODO add your handling code here:
        areaSplit.setDividerLocation(0.3);
    }//GEN-LAST:event_formComponentResized

    private void tblarsipMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblarsipMouseClicked
        // TODO add your handling code here:
        role = "Ubah";
        int row = tblarsip.getRowCount();
        if(row>0){
            int sel = tblarsip.getSelectedRow();
            if(sel != -1){
                pilihData(tblarsip.getValueAt(sel, 0).toString());
                btnSimpan.setText("UBAH DATA");
            }
        }
    }//GEN-LAST:event_tblarsipMouseClicked

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        int pilih = JOptionPane.showConfirmDialog(this,
                    "Apakah anda yakin ingin keluar?",
                    "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION);
        if(pilih == JOptionPane.YES_OPTION){
            Login l = new Login();
            l.setExtendedState(Frame.MAXIMIZED_BOTH);
            this.setVisible(false);
            l.setVisible(true);
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void browseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File browse = chooser.getSelectedFile();
        filename = browse.getAbsolutePath();
        jlokasi.setText(filename);
    }//GEN-LAST:event_browseActionPerformed

    private void jlokasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jlokasiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jlokasiActionPerformed

    private void jtanggalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtanggalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtanggalActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        if (role.equals("Tambah")) {
            simpanData();
        } else if (role.equals("Ubah")) {
            ubahData();
        }
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btntutupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntutupActionPerformed
        // TODO add your handling code here:
        resetForm();
        showForm(false);
        btnHapus.setEnabled(false);
        id = 0;
    }//GEN-LAST:event_btntutupActionPerformed

    private void jkodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jkodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jkodeActionPerformed

    private void jnamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jnamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jnamaActionPerformed

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
            java.util.logging.Logger.getLogger(Aplikasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Aplikasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Aplikasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Aplikasi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Aplikasi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane areaSplit;
    private javax.swing.JButton browse;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btntutup;
    private javax.swing.JComboBox<String> cmbkategori;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jdeskripsi;
    private javax.swing.JTextField jkode;
    private javax.swing.JTextField jlokasi;
    private javax.swing.JTextField jnama;
    private javax.swing.JTextField jtanggal;
    private javax.swing.JPanel panelKiri;
    private javax.swing.JTable tblarsip;
    private java.awt.TextField txtCari;
    // End of variables declaration//GEN-END:variables
}
