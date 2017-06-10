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

	public static final Color COR_TAG_PADRAO = new Color(255, 124, 0);
	public static final Color COR_TEXTO_PADRAO = new Color(0, 14, 255);
	public static final Color COR_INDEPENDENTE_PADRAO = new Color(46, 139, 87);
        public static final Color COR_FUNDO_UNIDADE_PADRAO = new Color(255, 215, 0); //amarelo
	public static final Color COR_FUNDO_UNIDADE_2_PADRAO = new Color(67, 110, 238); //azul
	public static final Color COR_FUNDO_EMBUTIDA_PADRAO = new Color(255, 0, 0); //vermelho
	public static final Color COR_FUNDO_INDEPENDENTE_PADRAO = new Color (224, 102, 255); //meio roxo rs
	public static final Color COR_FUNDO_SOBREPOSTA_PADRAO = new Color(34, 139, 34); //verde
        
        public static Color COR_TAG;
	public static Color COR_TEXTO;
	public static Color COR_INDEPENDENTE;
	public static Color COR_FUNDO_UNIDADE;
	public static Color COR_FUNDO_UNIDADE_2;
	public static Color COR_FUNDO_EMBUTIDA;
	public static Color COR_FUNDO_INDEPENDENTE;
	public static Color COR_FUNDO_SOBREPOSTA;
	
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
						+ COR_INDEPENDENTE_PADRAO.getBlue() + "\n"
                                                + COR_FUNDO_UNIDADE_PADRAO.getRed() + ", "
                                                + COR_FUNDO_UNIDADE_PADRAO.getGreen() + ", "
                                                + COR_FUNDO_UNIDADE_PADRAO.getBlue() + "\n"
                                                + COR_FUNDO_UNIDADE_2_PADRAO.getRed() + ", "
                                                + COR_FUNDO_UNIDADE_2_PADRAO.getGreen() + ", "
                                                + COR_FUNDO_UNIDADE_2_PADRAO.getBlue() + "\n"
                                                + COR_FUNDO_EMBUTIDA_PADRAO.getRed() + ", "
                                                + COR_FUNDO_EMBUTIDA_PADRAO.getGreen() + ", "
                                                + COR_FUNDO_EMBUTIDA_PADRAO.getBlue() + "\n"
                                                + COR_FUNDO_SOBREPOSTA_PADRAO.getRed() + ", "
                                                + COR_FUNDO_SOBREPOSTA_PADRAO.getGreen() + ", "
                                                + COR_FUNDO_SOBREPOSTA_PADRAO.getBlue() + "\n"
                                                + COR_FUNDO_INDEPENDENTE_PADRAO.getRed() + ", "
                                                + COR_FUNDO_INDEPENDENTE_PADRAO.getGreen() + ", "
                                                + COR_FUNDO_INDEPENDENTE_PADRAO.getBlue());
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

        public Color getCorSemUnidade()
        {
                return new Color(255, 255, 255);
        }
        
	public Color getCorUnidade1(){

                if(COR_FUNDO_UNIDADE == null)
                    carregarCores();
                
		return COR_FUNDO_UNIDADE;
            
	}
        
        public void setCorUnidade1(Color cor){
                COR_FUNDO_UNIDADE = cor;
                guardaCores();
        }
	
	public Color getCorUnidade2(){

                if(COR_FUNDO_UNIDADE_2 == null)
                    carregarCores();
                
		return COR_FUNDO_UNIDADE_2;
            
	}
        
        public void setCorUnidade2(Color cor){
                COR_FUNDO_UNIDADE_2 = cor;
                guardaCores();
        }
	
	public Color getCorEmbutida(){

                if(COR_FUNDO_EMBUTIDA == null)
                    carregarCores();
                
		return COR_FUNDO_EMBUTIDA;
            
	}
        
        public void setCorEmbutida(Color cor){
                COR_FUNDO_EMBUTIDA = cor;
                guardaCores();
        }
	
	public Color getCorSobreposta(){

                if(COR_FUNDO_SOBREPOSTA == null)
                    carregarCores();
                
		return COR_FUNDO_SOBREPOSTA;
            
	}
        
        public void setCorSobreposta(Color cor){
                COR_FUNDO_SOBREPOSTA = cor;
                guardaCores();
        }
	
	public Color getCorIndep(){

                if(COR_FUNDO_INDEPENDENTE == null)
                    carregarCores();
                
		return COR_FUNDO_INDEPENDENTE;
            
	}
        
        public void setCorIndep(Color cor){
                COR_FUNDO_INDEPENDENTE = cor;
                guardaCores();
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
        
        public void carregarCores()
        {
            File arquivo = new File(arquivoCores);
            try {
                    String[] linhas;
                    int[][] saida;
                    int i;

                    linhas = new String[8];
                    saida = new int[8][3];

                    Scanner scanner = new Scanner(arquivo);
                    linhas[0] = scanner.nextLine();
                    linhas[1] = scanner.nextLine();
                    linhas[2] = scanner.nextLine();
                    linhas[3] = scanner.nextLine();
                    linhas[4] = scanner.nextLine();
                    linhas[5] = scanner.nextLine();
                    linhas[6] = scanner.nextLine();
                    linhas[7] = scanner.nextLine();

                    for(i=0; i<linhas.length; i++)
                    {
                        String[] rgb = linhas[i].split(",");

                        int r;
                        int g;
                        int b;
                        r = Integer.parseInt(rgb[0].trim());
                        g = Integer.parseInt(rgb[1].trim());
                        b = Integer.parseInt(rgb[2].trim());

                        switch(i)
                        {
                            case 0:
                                COR_TAG = new Color(r,g,b);
                                break;
                            case 1:
                                COR_TEXTO = new Color(r,g,b);
                                break;
                            case 2:
                                COR_INDEPENDENTE = new Color(r,g,b);
                                break;
                            case 3:
                                COR_FUNDO_UNIDADE = new Color(r,g,b);
                                break;
                            case 4:
                                COR_FUNDO_UNIDADE_2 = new Color(r,g,b);
                                break;
                            case 5:
                                COR_FUNDO_EMBUTIDA = new Color(r,g,b);
                                break;
                            case 6:
                                COR_FUNDO_SOBREPOSTA = new Color(r,g,b);
                                break;
                            case 7:
                                COR_FUNDO_INDEPENDENTE = new Color(r,g,b);
                                break;
                        }
                    }
                    scanner.close();

		} catch (Exception e) {
                    
                        if(COR_TAG == null)
                            COR_TAG = COR_TAG_PADRAO;
                        
                        if(COR_TEXTO == null)
                            COR_TEXTO = COR_TEXTO_PADRAO;
                        
                        if(COR_INDEPENDENTE == null)
                            COR_INDEPENDENTE = COR_INDEPENDENTE_PADRAO;
                        
                        if(COR_FUNDO_UNIDADE == null)
                            COR_FUNDO_UNIDADE = COR_FUNDO_UNIDADE_PADRAO;
                        
                        if(COR_FUNDO_UNIDADE_2 == null)
                            COR_FUNDO_UNIDADE_2 = COR_FUNDO_UNIDADE_2_PADRAO;
                        
                        if(COR_FUNDO_EMBUTIDA == null)
                            COR_FUNDO_EMBUTIDA = COR_FUNDO_EMBUTIDA_PADRAO;
                        
                        if(COR_FUNDO_SOBREPOSTA == null)
                            COR_FUNDO_SOBREPOSTA = COR_FUNDO_SOBREPOSTA_PADRAO;
                        
                        if(COR_FUNDO_INDEPENDENTE == null)
                            COR_FUNDO_INDEPENDENTE = COR_FUNDO_INDEPENDENTE_PADRAO;
                        
                        guardaCores();
		}
        }

	public Color getCorTag() {
            
                if(COR_TAG == null)
                    carregarCores();
                
		return COR_TAG;
	}
        
        public void setCorTag(Color cor){
                COR_TAG = cor;
                guardaCores();
        }

	public Color getCorTexto() {
            
                if(COR_TEXTO == null)
                    carregarCores();
                
		return COR_TEXTO;
                
	}
        
        public void setCorTexto(Color cor){
                COR_TEXTO = cor;
                guardaCores();
        }

	public Color getCorIndependente() {

                if(COR_INDEPENDENTE == null)
                    carregarCores();
                
		return COR_INDEPENDENTE;
            
	}
        
        public void setCorIndependente(Color cor){
                COR_INDEPENDENTE = cor;
                guardaCores();
        }

	public void guardaUltimoArquivo(File CaminhoArquivo) throws IOException {
		FileWriter writer = new FileWriter(new File(arquivoUltimoArquivo));
		writer.write(CaminhoArquivo.getAbsolutePath());
		writer.close();
	}

	public void guardaCores() {
		try {
			FileWriter writer = new FileWriter(new File(arquivoCores));
			writer.write(COR_TAG.getRed() + ", " + COR_TAG.getGreen() + ", " + COR_TAG.getBlue() + "\n"
                                   + COR_TEXTO.getRed() + ", " + COR_TEXTO.getGreen() + ", " + COR_TEXTO.getBlue() + "\n"
				   + COR_INDEPENDENTE.getRed() + ", " + COR_INDEPENDENTE.getGreen() + ", " + COR_INDEPENDENTE.getBlue() + "\n"
                                   + COR_FUNDO_UNIDADE.getRed() + ", " + COR_FUNDO_UNIDADE.getGreen() + ", " + COR_FUNDO_UNIDADE.getBlue() + "\n"
                                   + COR_FUNDO_UNIDADE_2.getRed() + ", " + COR_FUNDO_UNIDADE_2.getGreen() + ", " + COR_FUNDO_UNIDADE_2.getBlue() + "\n"
                                   + COR_FUNDO_EMBUTIDA.getRed() + ", " + COR_FUNDO_EMBUTIDA.getGreen() + ", " + COR_FUNDO_EMBUTIDA.getBlue() + "\n"
                                   + COR_FUNDO_SOBREPOSTA.getRed() + ", " + COR_FUNDO_SOBREPOSTA.getGreen() + ", " + COR_FUNDO_SOBREPOSTA.getBlue() + "\n"
                                   + COR_FUNDO_INDEPENDENTE.getRed() + ", " + COR_FUNDO_INDEPENDENTE.getGreen() + ", " + COR_FUNDO_INDEPENDENTE.getBlue());
                        
			writer.close();
		} catch (IOException e) {
			criaArquivosConfiguracao();
		}
	}

	public String getDiretorioPadrao() {
		return diretorioPadrao;
	}
}
