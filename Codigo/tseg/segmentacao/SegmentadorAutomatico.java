package tseg.segmentacao;

import tseg.controle.Controle;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import tseg.arquivo.TratamentoArquivo;
import tseg.janelas.ModalProgressoSegmentacao;
import tseg.janelas.ModalSegmentarDiretorio;

public class SegmentadorAutomatico {
	public static int PALAVRAS = 1;
	public static int SENTENCAS = 2;
	public static int PARAGRAGOS = 3;

	public String segmenta(String texto, int tipoSegmentacao, ModalProgressoSegmentacao modal) {
            
                //Antes de adicionar uma unidade, guardar o contexto
                GerenciadorAcoes.guardarContexto();
            
		if (tipoSegmentacao == PALAVRAS) {
			texto = segmentaPalavras(texto, modal);
		} else {
			if (tipoSegmentacao == SENTENCAS) {
				texto = segmentaSentencas(texto, modal);
			} else {
				texto = segmentaParagrafos(texto, modal);
			}
		}
		Controle.setModificado(true);
		return texto;
	}
        
        public static String segmentar(String texto, int tipoSegmentacao) {
            
            SegmentadorAutomatico seg = new SegmentadorAutomatico();
            
		if (tipoSegmentacao == PALAVRAS) {
			texto = seg.segmentaPalavras(texto, null);
		} else {
			if (tipoSegmentacao == SENTENCAS) {
				texto = seg.segmentaSentencas(texto, null);
			} else {
				texto = seg.segmentaParagrafos(texto, null);
			}
		}
		return texto;
	}

	private String segmentaParagrafos(String texto, ModalProgressoSegmentacao modal) {
            
		StringBuilder textoSegmentacao = new StringBuilder(texto);
		SegmentadorUtil sUtil = new SegmentadorUtil();
		ControladorUnidades controladorUnidades = Controle
				.getControladorUnidades();
                
                double valorProgresso;
                int progressoAtual, progressoAnterior, ponto;
                int tamanhoTextoInicio, oQueFalta;
                
                progressoAtual = 0;
                progressoAnterior = 0;
                tamanhoTextoInicio = textoSegmentacao.length() - controladorUnidades.getTagDocumentFim().length() - controladorUnidades.getTagDocumentInicio().length();

		int inicioUnidade = controladorUnidades.getTagDocumentInicio().length() + 1;
		int i = inicioUnidade;

		while (i <= (textoSegmentacao.length() - controladorUnidades
				.getTagDocumentFim().length())) {
                    
			if ('\n' == textoSegmentacao.charAt(i)) {
				inicioUnidade = sUtil.adicionaUnidade(inicioUnidade, i, false,
						textoSegmentacao);
				i = inicioUnidade;
			}
			if (i + 1 == textoSegmentacao.length()) {
				i++;
				inicioUnidade = sUtil.adicionaUnidade(inicioUnidade, i, false,
						textoSegmentacao);
				i = inicioUnidade;
			}

			while (inicioUnidade < textoSegmentacao.length()
					&& '\n' == textoSegmentacao.charAt(inicioUnidade)) {
				i = inicioUnidade++;
			}
			i++;
                        
                        if(modal!=null)
                        {
                            //Atualizar barra de progresso
                            ponto = i - controladorUnidades.getTagDocumentInicio().length();
                            oQueFalta = textoSegmentacao.length() - controladorUnidades.getTagDocumentFim().length() - ponto;
                            valorProgresso = Double.valueOf(1) - (Double.valueOf(oQueFalta)/Double.valueOf(tamanhoTextoInicio));

                            progressoAtual = (int)(valorProgresso*100);
                            if(progressoAtual-progressoAnterior>=2)
                            {
                                modal.atualizarProgresso(progressoAtual);
                                progressoAnterior = progressoAtual;
                            }
                        }
                        
		}
                
                if(modal!=null)
                {
                    modal.atualizarProgresso(100);
                }
                
		return textoSegmentacao.toString();
	}

	private String segmentaSentencas(String texto, ModalProgressoSegmentacao modal) {
		StringBuilder textoSegmentacao = new StringBuilder(texto);
		SegmentadorUtil sUtil = new SegmentadorUtil();
		ControladorUnidades controladorUnidades = Controle
				.getControladorUnidades();

                double valorProgresso;
                int progressoAtual, progressoAnterior, ponto;
                int tamanhoTextoInicio, oQueFalta;
                
                progressoAtual = 0;
                progressoAnterior = 0;
                tamanhoTextoInicio = textoSegmentacao.length() - controladorUnidades.getTagDocumentFim().length() - controladorUnidades.getTagDocumentInicio().length();
                
		int inicioUnidade = controladorUnidades.getTagDocumentInicio().length() + 1;
		int i = inicioUnidade;

		while (i < (textoSegmentacao.length() - controladorUnidades
				.getTagDocumentFim().length())) {
			if (isDelimitadorSentenca(textoSegmentacao.charAt(i))
					|| i + 1 == textoSegmentacao.length()) {
				if (i + 1 == textoSegmentacao.length()) {
					i++;
					inicioUnidade = sUtil.adicionaUnidade(inicioUnidade, i,
							false, textoSegmentacao);
					i = inicioUnidade;
				} else {
					while (isDelimitadorSentenca(textoSegmentacao.charAt(i))) {
						i++;
					}

					while (!(sUtil.verificaForaDeTag(inicioUnidade, i + 1))) {
						i++;
						while (!(sUtil.verificaForaDeTag(inicioUnidade, i))) {
							i++;
						}
					}

					inicioUnidade = sUtil.adicionaUnidade(inicioUnidade, i,
							false, textoSegmentacao);
					i = inicioUnidade;
				}
			}
			while (inicioUnidade < textoSegmentacao.length()
					&& '\n' == textoSegmentacao.charAt(inicioUnidade)) {
				i = inicioUnidade++;
			}
			i++;
                        
                        if(modal!=null)
                        {
                            //Atualizar barra de progresso
                            ponto = i - controladorUnidades.getTagDocumentInicio().length();
                            oQueFalta = textoSegmentacao.length() - controladorUnidades.getTagDocumentFim().length() - ponto;
                            valorProgresso = Double.valueOf(1) - (Double.valueOf(oQueFalta)/Double.valueOf(tamanhoTextoInicio));

                            progressoAtual = (int)(valorProgresso*100);
                            if(progressoAtual-progressoAnterior>=2)
                            {
                                modal.atualizarProgresso(progressoAtual);
                                progressoAnterior = progressoAtual;
                            }
                        }
                        
		}
                
                if(modal!=null)
                {
                    modal.atualizarProgresso(100);
                }
                
		return textoSegmentacao.toString();
	}

	private boolean isDelimitadorSentenca(char caractere) {
		char[] delimitadores = { '.', '!', '?' };

		for (char c : delimitadores) {
			if (caractere == c) {
				return true;
			}
		}
		return false;
	}

	private String segmentaPalavras(String texto, ModalProgressoSegmentacao modal) {
              
		StringBuilder textoSegmentacao = new StringBuilder(texto);
		SegmentadorUtil sUtil = new SegmentadorUtil();
		ControladorUnidades controladorUnidades = Controle
				.getControladorUnidades();
                
                double valorProgresso;
                int progressoAtual, progressoAnterior, ponto;
                int tamanhoTextoInicio, oQueFalta;
                
                progressoAtual = 0;
                progressoAnterior = 0;
                tamanhoTextoInicio = textoSegmentacao.length() - controladorUnidades.getTagDocumentFim().length() - controladorUnidades.getTagDocumentInicio().length();

		int inicioUnidade = controladorUnidades.getTagDocumentInicio().length() + 1;
		int i = inicioUnidade;

		while (i < (textoSegmentacao.length() - controladorUnidades.getTagDocumentFim().length())) {
			if (' ' == textoSegmentacao.charAt(i)
					|| '\n' == textoSegmentacao.charAt(i)
					|| i + 1 == textoSegmentacao.length()) {
				if (i + 1 == textoSegmentacao.length()) {
					i++;
					inicioUnidade = sUtil.adicionaUnidade(inicioUnidade, i,
							false, textoSegmentacao);
					i = inicioUnidade;
				} else {
					if (sUtil.verificaForaDeTag(inicioUnidade, i)) {
						inicioUnidade = sUtil.adicionaUnidade(inicioUnidade, i,
								false, textoSegmentacao);
						i = inicioUnidade;
					}
				}
                                
			}
			while (inicioUnidade < textoSegmentacao.length()
					&& '\n' == textoSegmentacao.charAt(inicioUnidade)) {
				i = inicioUnidade++;
			}
			i++;

                        if(modal!=null)
                        {
                            //Atualizar barra de progresso
                            ponto = i - controladorUnidades.getTagDocumentInicio().length();
                            oQueFalta = textoSegmentacao.length() - controladorUnidades.getTagDocumentFim().length() - ponto;
                            valorProgresso = Double.valueOf(1) - (Double.valueOf(oQueFalta)/Double.valueOf(tamanhoTextoInicio));

                            progressoAtual = (int)(valorProgresso*100);
                            if(progressoAtual-progressoAnterior>=2)
                            {
                                modal.atualizarProgresso(progressoAtual);
                                progressoAnterior = progressoAtual;
                            }
                        }
                        
		}
                
                if(modal!=null)
                {
                    modal.atualizarProgresso(100);
                }
                
		return textoSegmentacao.toString();
	}
}
