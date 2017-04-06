package tseg.janelas;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import tseg.acoes.AcaoAbaEstatisticas;

public class AbaEstatisticas {
	private JPanel abaEstatistica;
	private JScrollPane spnlEstatisticas;
	private JTextArea txaEstatisticas;
	private AcaoAbaEstatisticas acao = new AcaoAbaEstatisticas();

	public AbaEstatisticas() {
		montaPainelEstatisticas();

		abaEstatistica = new JPanel();
		abaEstatistica.setLayout(new BorderLayout());
		abaEstatistica.add(spnlEstatisticas, BorderLayout.CENTER);
	}

	public void atualizaEstatisticas() {
		txaEstatisticas.setText(acao.montaEstatisticas());
	}

	public JPanel getAbaEstatisticas() {
		return abaEstatistica;
	}

	private void montaPainelEstatisticas() {
		txaEstatisticas = new JTextArea();

		spnlEstatisticas = new JScrollPane(txaEstatisticas);
	}

        
}
