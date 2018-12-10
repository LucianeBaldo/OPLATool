/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.br.opla.gui;

import br.ufpr.dinf.gres.loglog.Level;
import br.ufpr.dinf.gres.loglog.Logger;
import com.ufpr.br.opla.indicators.HypervolumeData;
import com.ufpr.br.opla.indicators.HypervolumeGenerateObjsData;
import com.ufpr.br.opla.utils.GuiUtils;
import org.bounce.event.DoubleClickListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author elf
 */
public class HypervolumeWindow extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableHypervolume;

    /**
     * Creates new form HypervolumeWindow
     */
    public HypervolumeWindow() {
        initComponents();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
		 */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting
        // code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.
		 * html
		 */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HypervolumeWindow.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HypervolumeWindow.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HypervolumeWindow.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HypervolumeWindow.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        }
        // </editor-fold>

		/*
		 * Create and display the form
		 */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new HypervolumeWindow().setVisible(true);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableHypervolume = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        tableHypervolume
                .setModel(new javax.swing.table.DefaultTableModel(
                        new Object[][]{{null, null, null, null}, {null, null, null, null},
                                {null, null, null, null}, {null, null, null, null}},
                        new String[]{"Title 1", "Title 2", "Title 3", "Title 4"}));
        jScrollPane1.setViewportView(tableHypervolume);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
                org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                795, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // End of variables declaration//GEN-END:variables

    public void loadData(String ids[]) throws IOException {
        try {

            Map<String, List<Double>> content = db.Database.getAllObjectivesForDominatedSolutions(ids);
            List<HypervolumeData> hypers = HypervolumeGenerateObjsData.generate(content);

            GuiUtils.makeTableNotEditable(tableHypervolume);
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID Execution");
            model.addColumn("PLA");
            model.addColumn("Algoritm");
            model.addColumn("Mean");
            model.addColumn("StdDev");
            tableHypervolume.setModel(model);

            for (HypervolumeData hyper : hypers) {
                Object[] row = new Object[5];
                row[0] = hyper.getIdExperiment();
                row[1] = hyper.getPlaName();
                row[2] = hyper.getAlgorithm();
                row[3] = hyper.getMean();
                row[4] = hyper.getStDev();
                model.addRow(row);
            }


            JMenuItem details = new JMenuItem("Details");
            add(details);
            tableHypervolume.addMouseListener(new DoubleClickListener() {
                @Override
                public void doubleClicked(MouseEvent mouseEvent) {
                    HypervolumeData hypervolumeData = hypers.get(tableHypervolume.getSelectedRow());
                    JTextArea jta = new JTextArea(hypervolumeData.getValues().stream().map(v -> BigDecimal.valueOf(v).setScale(8, BigDecimal.ROUND_HALF_UP)).collect(Collectors.toList()).toString());
                    JScrollPane jsp = new JScrollPane(jta) {
                        @Override
                        public Dimension getPreferredSize() {
                            return new Dimension(700, 320);
                        }
                    };
                    JOptionPane.showMessageDialog(null, jsp, hypervolumeData.getAlgorithm() + " - " + hypervolumeData.getIdExperiment(), JOptionPane.INFORMATION_MESSAGE);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger().putLog(ex.getMessage(), Level.ERROR);
        }
    }
}