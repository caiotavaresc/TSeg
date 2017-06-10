package tseg.janelas;

import java.util.logging.Level;
import java.util.logging.Logger;
import tseg.controle.Controle;

public class ModalProgressoSegmentacao extends javax.swing.JFrame {

    /**
     * Creates new form ModalProgressoSegmentacao
     */
    public ModalProgressoSegmentacao() {
        initComponents();
        translateDescr(Controle.getJanelaPrincipal().isPortugues());
        
        //Preferencias de Modal
        setLocationRelativeTo(null);
        this.setIconImage(this.getToolkit().getImage(this.getClass().getResource("/imagens/icone_transp.png")));
        
        this.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barraProgresso = new javax.swing.JProgressBar();
        labelPercentual = new javax.swing.JLabel();
        labelProgresso = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Segmentando texto...");
        setEnabled(false);
        setResizable(false);

        barraProgresso.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));

        labelPercentual.setText("0%");

        labelProgresso.setText("Progresso da segmentação:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelProgresso)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(barraProgresso, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labelPercentual)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelProgresso)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(barraProgresso, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelPercentual, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barraProgresso;
    private javax.swing.JLabel labelPercentual;
    private javax.swing.JLabel labelProgresso;
    // End of variables declaration//GEN-END:variables

    public void atualizarProgresso(int progresso)
    {
        if(progresso <= 100)
        {
            this.barraProgresso.setValue(progresso);
            this.labelPercentual.setText(progresso + "%");
        }
        else
        {
            this.barraProgresso.setValue(100);
            this.labelPercentual.setText("100%");
        }
        paintComponents(this.getGraphics());
    }
    
    private void translateDescr(boolean portugues)
    {
        if(!portugues)
        {
            this.labelProgresso.setText("Segmentation progress");
            this.setTitle("Segmenting the text...");
        }
    }
    
    public void ocultar()
    {
        try 
        {
            Thread.sleep(200);
        } 
        catch (InterruptedException ex) {
            //Nothing
        }
        this.setVisible(false);
    }
}