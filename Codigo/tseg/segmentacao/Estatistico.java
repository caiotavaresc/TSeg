package tseg.segmentacao;

import java.util.ArrayList;
import java.util.StringTokenizer;

import tseg.controle.Controle;

public class Estatistico {
	public int contaPalavras() {
		ControladorUnidades controladorUnidades = Controle
				.getControladorUnidades();

		String textoSegmentacao = Controle.getJanelaPrincipal()
				.getAbaSegmentaca().getTextoTxaSegmentacao();

		StringTokenizer st = new StringTokenizer(textoSegmentacao);

		int palavras = st.countTokens();
		palavras -= (controladorUnidades.totalUnidades()) * 2;

		ArrayList<Unidade> unidadesInicio = controladorUnidades
				.getUnidadesInicio();

		for (int i = 0; i < unidadesInicio.size(); i++) {
			if (unidadesInicio
					.get(i)
					.getIdentificador()
					.indexOf(controladorUnidades.getIdentificadorSobreposicao()) > -1) {
				palavras -= 2;
			}
		}

		// Tag <document> </document>
		palavras -= 2;

		return palavras;
	}

	public int contaSentencas() {
		String textoSegmentacao = Controle.getJanelaPrincipal()
				.getAbaSegmentaca().getTextoTxaSegmentacao();

		int i = 0;
		int sentencas = 0;

		while (i < (textoSegmentacao.length())) {
			if ('.' == textoSegmentacao.charAt(i)
					|| '!' == textoSegmentacao.charAt(i)
					|| '?' == textoSegmentacao.charAt(i)) {

				if (i + 1 == textoSegmentacao.length()) {
					sentencas++;
				} else {
					while ('.' == textoSegmentacao.charAt(i)
							|| '!' == textoSegmentacao.charAt(i)
							|| '?' == textoSegmentacao.charAt(i)) {
						i++;
					}
					sentencas++;
				}
			}
			i++;
		}
		return sentencas;
	}

	public int contaUnidades() {
		int totalUnidades = Controle.getControladorUnidades().totalUnidades();

		return totalUnidades;
	}

	public int contaUnidadesSobrepostas() {
		int unidadesSobrepostas = 0;
		ControladorUnidades controladorUnidades = Controle
				.getControladorUnidades();

		ArrayList<Unidade> unidadesInicio = controladorUnidades
				.getUnidadesInicio();
		ArrayList<Unidade> unidadesFim = controladorUnidades.getUnidadesFim();

		ArrayList<Unidade> sobrepostas = new ArrayList<Unidade>();

		for (int i = 0; i < unidadesInicio.size(); i++) {
			Unidade unidadeInicioPrincipal = unidadesInicio.get(i);
			Unidade unidadeFimPrincipal = unidadesFim.get(i);

			for (int j = 0; j < unidadesInicio.size(); j++) {
				Unidade unidadeInicioComparada = unidadesInicio.get(j);
				if (!(isIndependente(unidadeInicioComparada))
						&& (sobrepostas.indexOf(unidadeInicioComparada) == -1)) {
					if ((unidadeInicioPrincipal.getPosicao() < unidadeInicioComparada
							.getPosicao())
							&& (unidadeInicioComparada.getPosicao() < unidadeFimPrincipal
									.getPosicao())) {
						sobrepostas.add(unidadeInicioComparada);
						unidadesSobrepostas++;
					}
				}
			}

		}
		return unidadesSobrepostas;
	}

	private boolean isIndependente(Unidade unidade) {
		ControladorUnidades controladorUnidades = Controle
				.getControladorUnidades();
		String identificador = unidade.getIdentificador();
		return identificador.contains(controladorUnidades
				.getIdentificadorSobreposicao());
	}

}
