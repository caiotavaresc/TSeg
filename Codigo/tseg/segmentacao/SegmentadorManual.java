package tseg.segmentacao;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import tseg.controle.Controle;

public class SegmentadorManual {
	public String segmenta(JTextPane txpSegmentacao, boolean independente) {
		StringBuilder textoSegmentacao = new StringBuilder(
				txpSegmentacao.getText());
		SegmentadorUtil sUtil = new SegmentadorUtil();
		int selecaoInicio;
		int selecaoFim;
		selecaoInicio = txpSegmentacao.getSelectionStart();
		selecaoFim = txpSegmentacao.getSelectionEnd();

		if (selecaoInicio == selecaoFim) {
			String message, title;
			if (tseg.janelas.JanelaPrincipal.portugues)
			{
				message = "Não é possível definir uma Unidade vazia!";
				title = "Aviso";
			}
			else
			{
				message = "It is not possible to define an empty Unit!";
				title = "Warning";
			}
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.WARNING_MESSAGE);

		} 
                else {
			if (!(selecaoInicio == 0)) {
				if ('\n' == textoSegmentacao.charAt(selecaoInicio)) {
					selecaoInicio++;
				}
				if ('\n' == textoSegmentacao.charAt(selecaoInicio)) {
					selecaoInicio += 2;
				}
			}

                        //Antes de adicionar uma unidade, guardar o contexto
                        GerenciadorAcoes.guardarContexto();
                        
			sUtil.adicionaUnidade(selecaoInicio, selecaoFim, independente,
					textoSegmentacao);
		}
		Controle.setModificado(true);
		return textoSegmentacao.toString();
	}

	public String removeTodasUnidades(String texto) {
            
                //Memorizar o contexto para a ação de desfazer
                GerenciadorAcoes.guardarContexto();
            
		ArrayList<Unidade> list = Controle.getControladorUnidades()
				.getUnidadesInicio();

		while (!(list.isEmpty())) {
			texto = removeUnidade(texto, list.get(0).getIdentificador(), true);
		}

		return texto;
	}

	public String removeUnidade(String texto, String identificador, boolean lote) {
		ArrayList<Unidade> unidade = Controle.getControladorUnidades()				.getUnidade(identificador);

		Unidade unidadeFim = unidade.get(1);

		texto = texto.replaceAll(identificador, "");
		texto = texto.replaceAll(unidadeFim.getIdentificador(), "");

                //Guardar o contexto para desfazer depois
                if(!lote)
                    GerenciadorAcoes.guardarContexto();
                
		Controle.getControladorUnidades().removeUnidade(identificador);

		Controle.setModificado(true);

		return texto;
	}

}
