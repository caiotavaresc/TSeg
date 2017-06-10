package tseg.janelas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import tseg.acoes.AcaoAbaSegmentacao;
import tseg.configuracoes.Configuracoes;
import tseg.controle.Controle;
import tseg.segmentacao.ControladorUnidades;
import tseg.segmentacao.Unidade;

public class AbaSegmentacao {
	private JPanel abaSegmentacao;
	private JPanel pnlBotoes;
	private JButton btnSegmentacaoAutomatica;
	private JScrollPane spnlSegmentacao;
	private JTextPane txpSegmentacao;
	private AcaoAbaSegmentacao acao = new AcaoAbaSegmentacao();

	public AbaSegmentacao() {
		montaPainelBotoes();

		montaPainelAreaSegmentacao();

		abaSegmentacao = new JPanel();
		abaSegmentacao.setLayout(new BorderLayout());
		//abaSegmentacao.add(pnlBotoes, BorderLayout.NORTH);
		abaSegmentacao.add(spnlSegmentacao, BorderLayout.CENTER);
	}
        
        public JTextPane getTxpSegmentacao()
        {
            return this.txpSegmentacao;
        }
                

	public JPanel getAbaSegmentacao() {
		return abaSegmentacao;
	}

	public String getTextoTxaSegmentacao() {
		return txpSegmentacao.getText();
	}

	public void setTextoTxaSegmentacao(String texto) {
		txpSegmentacao.setText(texto);
		coloreTags();
	}

	private void montaPainelBotoes() {
		btnSegmentacaoAutomatica = new JButton();
		btnSegmentacaoAutomatica.setText("Segmentação Automática");
		btnSegmentacaoAutomatica.setMnemonic('s');

		String tooltip = "Veja as opções de Segmentação Automática.";
		btnSegmentacaoAutomatica.setToolTipText(tooltip);
		//btnSegmentacaoAutomatica.addActionListener(acao.btnSegmentacaoAutomaticaActionListener());

		pnlBotoes = new JPanel();
		pnlBotoes.add(btnSegmentacaoAutomatica);
	}

        /**
     *
     * @param txt
     * @param tooltip
     */
    public void setTextBtnSegAuto(String txt, String tooltip)
        {
            btnSegmentacaoAutomatica.setText(txt);
            btnSegmentacaoAutomatica.setToolTipText(tooltip);
        }
                 
	private void montaPainelAreaSegmentacao() {
		Border borda = BorderFactory.createMatteBorder(0, 4, 0, 0, Color.WHITE);

		txpSegmentacao = new JTextPane();
		txpSegmentacao.setBorder(borda);
		txpSegmentacao.setForeground(Color.BLACK);
		txpSegmentacao.addFocusListener(acao.txpSegmentacaoFocusAdapter());
		txpSegmentacao.addKeyListener(acao.txpSegmentacaoKeyListener());
		txpSegmentacao.addMouseListener(acao.txpSegmentacaoMouseAdapter());

		spnlSegmentacao = new JScrollPane(txpSegmentacao);
	}

	public void coloreTags() {
		resetarFormatacao();

		MutableAttributeSet attributes = txpSegmentacao.getInputAttributes();

		Configuracoes configuracoes = new Configuracoes();

		StyleConstants.setForeground(attributes, configuracoes.getCorTexto());

		StyledDocument doc = txpSegmentacao.getStyledDocument();

		ArrayList<Unidade> unidadesInicio = Controle.getControladorUnidades()
				.getUnidadesInicio();

		ArrayList<Unidade> unidadesFim = Controle.getControladorUnidades()
				.getUnidadesFim();

		for (int i = 0; i < unidadesInicio.size(); i++) {
			int posicaoIni = unidadesInicio.get(i).getPosicao();
			int lengthIni = unidadesInicio.get(i).getIdentificador().length();
			int posicaoFim = unidadesFim.get(i).getPosicao();
			doc.setCharacterAttributes(posicaoIni + lengthIni, posicaoFim
					- (posicaoIni + lengthIni), attributes, false);
		}

		// Unidades
		StyleConstants.setForeground(attributes, configuracoes.getCorTag());

		ControladorUnidades controladorUnidades = Controle
				.getControladorUnidades();

		String tagDocumentInicio = controladorUnidades.getTagDocumentInicio();

		String tagDocumentFim = controladorUnidades.getTagDocumentFim();

		doc.setCharacterAttributes(0, tagDocumentInicio.length(), attributes,
				false);

		int tamanhoTexto = txpSegmentacao.getText().length();
		doc.setCharacterAttributes(tamanhoTexto - tagDocumentFim.length(),
				tamanhoTexto, attributes, false);

		ArrayList<Unidade> unidades = Controle.getControladorUnidades()
				.getUnidades();

		for (Unidade unidade : unidades) {
			doc.setCharacterAttributes(unidade.getPosicao(), unidade
					.getIdentificador().length(), attributes, false);
		}

		// Independentes
		StyleConstants.setForeground(attributes,
				configuracoes.getCorIndependente());

		for (int i = 0; i < unidadesInicio.size(); i++) {
			String identificadorSobreposicao = Controle
					.getControladorUnidades().getIdentificadorSobreposicao();

			if (unidadesInicio.get(i).getIdentificador()
					.indexOf(identificadorSobreposicao) > -1) {
				int iniIND = unidadesInicio.get(i).getIdentificador()
						.indexOf(identificadorSobreposicao);

				int fimIND = identificadorSobreposicao.length() - 1;

				doc.setCharacterAttributes(unidadesInicio.get(i).getPosicao()
						+ iniIND, fimIND, attributes, false);

				iniIND = unidadesFim.get(i).getIdentificador()
						.indexOf(identificadorSobreposicao);

				doc.setCharacterAttributes(unidadesFim.get(i).getPosicao()
						+ iniIND, fimIND, attributes, false);
			}
		}

	}

	private void resetarFormatacao() {
		MutableAttributeSet attributes = txpSegmentacao.getInputAttributes();

		StyleConstants
				.setForeground(attributes, txpSegmentacao.getForeground());
		StyleConstants.setBold(attributes, false);

		StyledDocument doc = txpSegmentacao.getStyledDocument();

		doc.setCharacterAttributes(0, txpSegmentacao.getText().length(),
				attributes, false);
	}
}
