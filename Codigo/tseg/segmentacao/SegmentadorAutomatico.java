package tseg.segmentacao;

import tseg.controle.Controle;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import tseg.arquivo.TratamentoArquivo;

public class SegmentadorAutomatico {
	public static int PALAVRAS = 1;
	public static int SENTENCAS = 2;
	public static int PARAGRAGOS = 3;

	public String segmenta(String texto, int tipoSegmentacao) {
            
                //Antes de adicionar uma unidade, guardar o contexto
                GerenciadorAcoes.guardarContexto();
            
		if (tipoSegmentacao == PALAVRAS) {
			texto = segmentaPalavras(texto);
		} else {
			if (tipoSegmentacao == SENTENCAS) {
				texto = segmentaSentencas(texto);
			} else {
				texto = segmentaParagrafos(texto);
			}
		}
		Controle.setModificado(true);
		return texto;
	}
        
        public static void segmentarDiretorio(File diretorio, List<File> arquivos, int opcao)
        {
            int ind;
            File arqLer;
            String textoArq, nomeRet;
            String[] nomeRetorno;
            Map<File, String> arquivosTexto = new HashMap<File, String>();
            TratamentoArquivo arquivo = new TratamentoArquivo();

            for(ind=0; ind<arquivos.size(); ind++)
            {
                File retorno;

                arqLer = arquivos.get(ind);

                //Ler o arquivo e segmentar conforme a opção
                textoArq = arquivo.leArquivo(arqLer);

                //Segmentar automaticamente o texto conforme a opcao
                textoArq = SegmentadorAutomatico.segmentar(textoArq, Integer.valueOf(opcao));

                //Abrir um arquivo segmentado e salvar
                nomeRet = arqLer.getName();
                nomeRetorno = nomeRet.split("\\.");

                retorno = new File(diretorio.getAbsolutePath() + File.separator + nomeRetorno[0] + "_tseg." + nomeRetorno[1]);
                
                arquivosTexto.put(retorno, textoArq);
            }
            
            //Pede para a classe responsável pela gravação dos arquivos gravar o lote de arquivos
            arquivo.gravaDiretorio(arquivosTexto);
        }
        
        public static String segmentar(String texto, int tipoSegmentacao) {
            
            SegmentadorAutomatico seg = new SegmentadorAutomatico();
            
		if (tipoSegmentacao == PALAVRAS) {
			texto = seg.segmentaPalavras(texto);
		} else {
			if (tipoSegmentacao == SENTENCAS) {
				texto = seg.segmentaSentencas(texto);
			} else {
				texto = seg.segmentaParagrafos(texto);
			}
		}
		return texto;
	}

	private String segmentaParagrafos(String texto) {
		StringBuilder textoSegmentacao = new StringBuilder(texto);
		SegmentadorUtil sUtil = new SegmentadorUtil();
		ControladorUnidades controladorUnidades = Controle
				.getControladorUnidades();

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
		}
		return textoSegmentacao.toString();
	}

	private String segmentaSentencas(String texto) {
		StringBuilder textoSegmentacao = new StringBuilder(texto);
		SegmentadorUtil sUtil = new SegmentadorUtil();
		ControladorUnidades controladorUnidades = Controle
				.getControladorUnidades();

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

	private String segmentaPalavras(String texto) {
		StringBuilder textoSegmentacao = new StringBuilder(texto);
		SegmentadorUtil sUtil = new SegmentadorUtil();
		ControladorUnidades controladorUnidades = Controle
				.getControladorUnidades();

		int inicioUnidade = controladorUnidades.getTagDocumentInicio().length() + 1;
		int i = inicioUnidade;

		while (i < (textoSegmentacao.length() - controladorUnidades
				.getTagDocumentFim().length())) {
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
		}
		return textoSegmentacao.toString();
	}
}
