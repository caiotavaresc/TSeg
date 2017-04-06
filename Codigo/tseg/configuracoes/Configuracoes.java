package tseg.configuracoes;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Configuracoes {
	private String diretorioConfiguracao = "./Config";
	private String arquivoUltimoArquivo = diretorioConfiguracao
			+ "/Ultimo Arquivo";
	private String arquivoCores = diretorioConfiguracao + "/Cores";
	private String diretorioPadrao = "\\..";

	public final Color COR_TAG_PADRAO = new Color(255, 124, 0);
	public final Color COR_TEXTO_PADRAO = new Color(0, 14, 255);
	public final Color COR_INDEPENDENTE_PADRAO = new Color(46, 139, 87);
	public final Color COR_FUNDO_UNIDADE = new Color(255, 215, 0); //amarelo
	public final Color COR_FUNDO_UNIDADE_2 = new Color(67, 110, 238); //azul
	public final Color COR_FUNDO_EMBUTIDA = new Color(255, 0, 0); //vermelho
	public final Color COR_FUNDO_INDEPENDENTE = new Color (224, 102, 255); //meio roxo rs
	public final Color COR_FUNDO_SOBREPOSTA = new Color(34, 139, 34); //verde
	
	public void criaArquivosConfiguracao() {
		File diretorio = new File(diretorioConfiguracao);
		File ultimoArquivo = new File(arquivoUltimoArquivo);
		File cores = new File(arquivoCores);

		if (!(diretorio.exists())) {
			diretorio.mkdir();
		}
		if (!(ultimoArquivo.exists())) {
			try {
				ultimoArquivo.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!(cores.exists())) {
			try {
				cores.createNewFile();
				FileWriter writer = new FileWriter(cores);
				writer.write(COR_TAG_PADRAO.getRed() + ", "
						+ COR_TAG_PADRAO.getGreen() + ", "
						+ COR_TAG_PADRAO.getBlue() + "\n"
						+ COR_TEXTO_PADRAO.getRed() + ", "
						+ COR_TEXTO_PADRAO.getGreen() + ", "
						+ COR_TEXTO_PADRAO.getBlue() + "\n"
						+ COR_INDEPENDENTE_PADRAO.getRed() + ", "
						+ COR_INDEPENDENTE_PADRAO.getGreen() + ", "
						+ COR_INDEPENDENTE_PADRAO.getBlue());
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Color getCorUnidade1(){
		return COR_FUNDO_UNIDADE;
	}
	
	public Color getCorUnidade2(){
		return COR_FUNDO_UNIDADE_2;
	}
	
	public Color getCorEmbutida(){
		return COR_FUNDO_EMBUTIDA;
	}
	
	public Color getCorSobreposta(){
		return COR_FUNDO_SOBREPOSTA;
	}
	
	public Color getCorIndep(){
		return COR_FUNDO_INDEPENDENTE;
	}
	
	public File getUltimoArquivo() {
		File file = new File(arquivoUltimoArquivo);
		if (!(file.canRead())) {
			criaArquivosConfiguracao();
		}
		return new File(arquivoUltimoArquivo);
	}

	public String getArquivoCores() {
		return arquivoCores;
	}

	public Color getCorTag() {
		File arquivo = new File(arquivoCores);
		try {
			Scanner scanner = new Scanner(arquivo);
			String corTag = scanner.nextLine();
			scanner.close();
			String[] rgb = corTag.split(",");
			int r;
			int g;
			int b;
			r = Integer.parseInt(rgb[0].trim());
			g = Integer.parseInt(rgb[1].trim());
			b = Integer.parseInt(rgb[2].trim());

			return new Color(r, g, b);
		} catch (FileNotFoundException e) {
			criaArquivosConfiguracao();
		}
		return COR_TAG_PADRAO;
	}

	public Color getCorTexto() {
		File arquivo = new File(arquivoCores);
		try {
			Scanner scanner = new Scanner(arquivo);
			scanner.nextLine();
			String corTexto = scanner.nextLine();
			scanner.close();
			String[] rgb = corTexto.split(",");
			int r;
			int g;
			int b;
			r = Integer.parseInt(rgb[0].trim());
			g = Integer.parseInt(rgb[1].trim());
			b = Integer.parseInt(rgb[2].trim());

			return new Color(r, g, b);
		} catch (FileNotFoundException e) {
			criaArquivosConfiguracao();
		}
		return COR_TEXTO_PADRAO;
	}

	public Color getCorIndependente() {
		File file = new File(arquivoCores);
		try {
			Scanner scanner = new Scanner(file);
			scanner.nextLine();
			scanner.nextLine();
			String corIdentificador = scanner.nextLine();
			scanner.close();
			String[] rgb = corIdentificador.split(",");
			int r;
			int g;
			int b;
			r = Integer.parseInt(rgb[0].trim());
			g = Integer.parseInt(rgb[1].trim());
			b = Integer.parseInt(rgb[2].trim());

			return new Color(r, g, b);
		} catch (FileNotFoundException e) {
			criaArquivosConfiguracao();
		}
		return COR_INDEPENDENTE_PADRAO;
	}

	public void guardaUltimoArquivo(File CaminhoArquivo) throws IOException {
		FileWriter writer = new FileWriter(new File(arquivoUltimoArquivo));
		writer.write(CaminhoArquivo.getAbsolutePath());
		writer.close();
	}

	public void guardaCores(Color corTag, Color corTexto,
			Color corIdentificadorSobreposicao) {
		try {
			FileWriter writer = new FileWriter(new File(arquivoCores));
			writer.write(corTag.getRed() + ", " + corTag.getGreen() + ", "
					+ corTag.getBlue() + "\n" + corTexto.getRed() + ", "
					+ corTexto.getGreen() + ", " + corTexto.getBlue() + "\n"
					+ corIdentificadorSobreposicao.getRed() + ", "
					+ corIdentificadorSobreposicao.getGreen() + ", "
					+ corIdentificadorSobreposicao.getBlue());
			writer.close();
		} catch (IOException e) {
			criaArquivosConfiguracao();
		}
	}

	public String getDiretorioPadrao() {
		return diretorioPadrao;
	}
}
