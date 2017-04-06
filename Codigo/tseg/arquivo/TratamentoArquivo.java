package tseg.arquivo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;

import tseg.configuracoes.Configuracoes;
import tseg.controle.Controle;
import tseg.segmentacao.ControladorUnidades;

public class TratamentoArquivo {
	
	public String leArquivo(File CaminhoArquivo) {
		StringBuilder conteudo = new StringBuilder();
		Scanner scanner;
		try {
			scanner = new Scanner(CaminhoArquivo);

			while (scanner.hasNext()) {
				String nextLine = scanner.nextLine();
				if (scanner.hasNext()) {
					conteudo.append(nextLine + "\n");
				} else {
					conteudo.append(nextLine);
				}
			}
			scanner.close();
					
			conteudo = insereTagDocument(conteudo);

			Controle.getControladorUnidades().recuperaUnidades(
					conteudo.toString());

			// Controle.getJanelaPrincial().setTitulo(CaminhoArquivo.getPath());
		} catch (FileNotFoundException e) {			
			String message,title;
			if (tseg.janelas.JanelaPrincipal.portugues)
			{
				message = "Arquivo não encontrado";
				title = "Erro";
			}
			else
			{
				message = "File not found";
				title = "Error";
			}
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
		}
		return conteudo.toString();
	}

	private StringBuilder insereTagDocument(StringBuilder conteudo) {
		ControladorUnidades controladorUnidades = Controle
				.getControladorUnidades();

		if (conteudo.indexOf(controladorUnidades.getTagDocumentInicio()) == -1) {
			conteudo.insert(0, controladorUnidades.getTagDocumentInicio()
					+ "\n");
			conteudo.append("\n" + controladorUnidades.getTagDocumentFim());
		}

		return conteudo;
	}

	public void gravaArquivo(File CaminhoArquivo, String texto) {
		try {
                        this.gravarArquivo(CaminhoArquivo, texto);
                    
			Controle.setModificado(false);
			// Controle.getJanelaPrincial().setTitulo(file.getAbsolutePath());
			Configuracoes configuracoes = new Configuracoes();
			configuracoes.guardaUltimoArquivo(CaminhoArquivo);

			String message, title;
			if (tseg.janelas.JanelaPrincipal.portugues)
			{
				message = "Arquivo Salvo!";
				title = "Salvando arquivo segmentado";
			}
			else
			{
				message = "Saved File!";
				title = "Saving file segmented";
			}
			JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.INFORMATION_MESSAGE);

		} catch (IOException e) {
			String message, title;
			if (tseg.janelas.JanelaPrincipal.portugues)
			{
				message = "Diretório não encontrado!";
				title = "Erro";
			}
			else
			{
				message = "Directory not found";
				title = "Error";
			}
			 JOptionPane.showMessageDialog(null, message, title,
					JOptionPane.ERROR_MESSAGE);
		}
	}
        
        public void gravaDiretorio(Map<File, String> mapaArquivos)
        {
            Set<File> arquivos = mapaArquivos.keySet();
            Iterator<File> i;
            
            int contadorSucesso, contadorErro;
            contadorSucesso = 0;
            contadorErro = 0;
            
            List<String> arquivosErro = new ArrayList<String>();
            
            File arquivo;
            String textoArq, mensagem, titulo;
            
            i = arquivos.iterator();
            
            while(i.hasNext())
            {
                arquivo = i.next();
                textoArq = mapaArquivos.get(arquivo);
                
                try
                {
                    this.gravarArquivo(arquivo, textoArq);
                    contadorSucesso++;
                }
                catch(IOException e)
                {
                    contadorErro++;
                    arquivosErro.add(arquivo.getName());
                }
            }
            
            //Se não houve erros, dar a mensagem de sucesso
            if(arquivosErro.isEmpty())
            {
                if(tseg.janelas.JanelaPrincipal.portugues)
                {
                    mensagem = "Todos os arquivos do diretório foram segmentados e salvos.";
                    titulo = "Salvando arquivos";
                }
                else
                {
                    mensagem = "All files in directory have been segmented and saved.";
                    titulo = "Saving files";
                }
                
                JOptionPane.showMessageDialog(null, mensagem, titulo,
					JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                if(tseg.janelas.JanelaPrincipal.portugues)
                {
                    mensagem = "Os arquivos abaixo não puderam ser salvos porque o diretório não foi encontrado.";
                    titulo = "Erro";
                }
                else
                {
                    mensagem = "The files below could not be saved because the directory was not found";
                    titulo = "Error";
                }
                
                mensagem = mensagem + "\n\n";
                for(int j = 0; j < arquivosErro.size(); j++)
                    mensagem = mensagem + "- " + arquivosErro.get(j) + "\n";
                
                JOptionPane.showMessageDialog(null, mensagem, titulo,
					JOptionPane.ERROR_MESSAGE);
            }
        }
        
        private void gravarArquivo(File CaminhoArquivo, String texto) throws IOException{
            FileWriter writer = new FileWriter(CaminhoArquivo);
            writer.write(texto);
            writer.close();
        }
}