package tseg.acoes;

import tseg.segmentacao.Estatistico;

public class AcaoAbaEstatisticas {

	public String montaEstatisticas() {
		Estatistico estatistico = new Estatistico();

		String estatisticas;
		if (tseg.janelas.JanelaPrincipal.portugues)
		{
			estatisticas = " Total de Palavras: "
				+ estatistico.contaPalavras() + "\n\n Total de Sentenças: "
				+ estatistico.contaSentencas() + "\n\n Total de Unidades: "
				+ estatistico.contaUnidades()
				+ "\n\n Total de Unidades Sobrepostas: "
				+ estatistico.contaUnidadesSobrepostas();
		}
		else
		{
			estatisticas = " Total Words: "
					+ estatistico.contaPalavras() + "\n\n  Total Sentence:: "
					+ estatistico.contaSentencas() + "\n\n  Total Units: "
					+ estatistico.contaUnidades()
					+ "\n\n Total Overlap Units: "
					+ estatistico.contaUnidadesSobrepostas();
		}
		return estatisticas;
	}

}
