package tseg.acoes;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import tseg.arquivo.FiltroArquivo;
import tseg.arquivo.FiltroExtencoes;
import tseg.arquivo.TratamentoArquivo;
import tseg.segmentacao.SegmentadorAutomatico;
import tseg.configuracoes.Configuracoes;
import tseg.controle.Controle;
import tseg.segmentacao.GerenciadorAcoes;
import tseg.segmentacao.SegmentadorManual;
import tseg.segmentacao.SegmentadorUtil;

public class AcaoJanelaPrincipal {

	public ActionListener itemAbrir() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				int confirmaFechamento = confirmaFechamento();

				if (confirmaFechamento != JOptionPane.CANCEL_OPTION) {
					JFileChooser fc = getFileChooserCustomizado();
					TratamentoArquivo arquivo = new TratamentoArquivo();
					Configuracoes configuracoes = new Configuracoes();
					
					String abrir;
					if (tseg.janelas.JanelaPrincipal.portugues)
					{
						abrir = "Abrir Arquivo";
					}	
					else
					{
						abrir = "Open File";
					}
					int retornoFc = fc.showDialog(null, abrir);

					if (retornoFc == JFileChooser.APPROVE_OPTION) {
						String textoArquivo;
						try {
                                                        File arquivoT = fc.getSelectedFile();
							textoArquivo = arquivo.leArquivo(arquivoT);

							Controle.getJanelaPrincipal().getAbaSegmentaca()
									.setTextoTxaSegmentacao(textoArquivo);
                                                        
                                                        //Alterar o titulo da janela
                                                        Controle.getJanelaPrincipal().alterarTituloJanela("T-Seg - " + arquivoT.getAbsolutePath());

                                                        //Limpar o contexto
                                                        GerenciadorAcoes.limparContexto();
                                                        
							Controle.getJanelaPrincipal().getAbaEstatisticas()
									.atualizaEstatisticas();

							if (Controle.getJanelaPrincipal().getAbaAtual() != 0) {
								String message;
								if (tseg.janelas.JanelaPrincipal.portugues)
								{	
									message = "Texto carregado com Sucesso, você será agora redirecionado à aba de segmentação.";
								}
								else
								{
									message = "Successfully loaded text, you will now be redirected to tab targeting.";
								}
								JOptionPane.showMessageDialog(null, message);
								Controle.getJanelaPrincipal().mudaAba(0);
							}

							configuracoes.guardaUltimoArquivo(fc
									.getSelectedFile());
						} catch (IOException e) {
							String message;
							if (tseg.janelas.JanelaPrincipal.portugues)
							{
								message = "Impossível ler este tipo de Arquivo ou o Diretório é inválido";
							}
							else
							{
								message = "Can not read this type of file or directory is invalid";
							}
							JOptionPane.showMessageDialog(null, message,
									"Erro", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			};

		};
	}

	public ChangeListener aoMudarAba() {
		return new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
				if (tabbedPane.getSelectedIndex() == 1) {
					Controle.getJanelaPrincipal().getAbaEstatisticas()
							.atualizaEstatisticas();
				}
			}
		};
	}
        
        public ActionListener segmentarDiretorio(){
            return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				int confirmaFechamento = confirmaFechamento();

				if (confirmaFechamento != JOptionPane.CANCEL_OPTION) {
					JFileChooser fc = getFileChooserCustomizado();
                                        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					TratamentoArquivo arquivo = new TratamentoArquivo();
					Configuracoes configuracoes = new Configuracoes();
					
					String abrir;
					if (tseg.janelas.JanelaPrincipal.portugues)
					{
						abrir = "Selecionar diretório";
					}	
					else
					{
						abrir = "Select directory";
					}
					int retornoFc = fc.showDialog(null, abrir);

					if (retornoFc == JFileChooser.APPROVE_OPTION) {
						File diretorio;
                                                File[] arquivos;
                                                List<File> arquivosFiltrado;
                                                String opcao, mensagem;

                                                diretorio = fc.getSelectedFile();

                                                //Listar todos os arquivos do diretório
                                                arquivos = diretorio.listFiles();
                                                
                                                //Filtrar os arquivos de acordo com o formato
                                                arquivosFiltrado = filtrarArquivos(arquivos);
                                                
                                                if (arquivosFiltrado.isEmpty())
                                                {
                                                    //Dar mensagem de erro
                                                    if(tseg.janelas.JanelaPrincipal.portugues)
                                                        mensagem = "Não existem arquivos de texto aptos para segmentação no diretório escolhido.";
                                                    else
                                                        mensagem = "There are not text files suitable to segmentation at chosen directory";
                                                    
                                                    JOptionPane.showMessageDialog(null, mensagem,
									"Erro", JOptionPane.ERROR_MESSAGE);
                                                }
                                                else
                                                {
                                                    opcao = JOptionPane.showInputDialog("Selecione uma opção de segmentação:\n\n1) Palavras\n2) Sentenças\n3) Parágrafos");
                                                    SegmentadorAutomatico.segmentarDiretorio(diretorio, arquivosFiltrado, Integer.valueOf(opcao));
                                                }

					}
				}
			};
		};
        }
        
        public ActionListener itemSegmentacaoAutomaticaPalavras() {
		return new ActionListener() {

			@Override
                        public void actionPerformed(ActionEvent evt) {
                                SegmentadorAutomatico segmentador = new SegmentadorAutomatico();
                                String texto = Controle.getJanelaPrincipal()
                                                .getAbaSegmentaca().getTextoTxaSegmentacao();

                                texto = segmentador.segmenta(texto,
                                                SegmentadorAutomatico.PALAVRAS);

                                Controle.getJanelaPrincipal().getAbaSegmentaca()
                                                .setTextoTxaSegmentacao(texto);
                        }
		};
	}
        
        public ActionListener itemSegmentacaoAutomaticaSentencas() {
		return new ActionListener() {

			@Override
                        public void actionPerformed(ActionEvent evt) {
                                SegmentadorAutomatico segmentador = new SegmentadorAutomatico();
                                String texto = Controle.getJanelaPrincipal()
                                                .getAbaSegmentaca().getTextoTxaSegmentacao();

                                texto = segmentador.segmenta(texto,
                                                SegmentadorAutomatico.SENTENCAS);

                                Controle.getJanelaPrincipal().getAbaSegmentaca()
                                                .setTextoTxaSegmentacao(texto);
                        }
		};
	}
                
        public ActionListener itemSegmentacaoAutomaticaParagrafos() {
		return new ActionListener() {

			@Override
                        public void actionPerformed(ActionEvent evt) {
                                SegmentadorAutomatico segmentador = new SegmentadorAutomatico();
                                String texto = Controle.getJanelaPrincipal()
                                                .getAbaSegmentaca().getTextoTxaSegmentacao();

                                texto = segmentador.segmenta(texto,
                                                SegmentadorAutomatico.PARAGRAGOS);

                                Controle.getJanelaPrincipal().getAbaSegmentaca()
                                                .setTextoTxaSegmentacao(texto);
                        }
		};
	}
        
        public ActionListener itemDefinirNovaUnidade(JTextPane txpSegmentacao, boolean independente){
         
            return new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        
                            //Verificar se o lugar onde estou tentando definir uma unidade, é um fragmento de unidade
                            int selecaoInicio = txpSegmentacao.getSelectionStart();
                            int selecaoFim = txpSegmentacao.getSelectionEnd();
                            
                            if  (      !(
                                            SegmentadorUtil.verificaForaDeTag(selecaoInicio, selecaoFim) && 
                                            SegmentadorUtil.verificaForaTagDocument(selecaoInicio, selecaoFim)
                                        )
                                )
                            {
                                //Mensagem de erro
                                String message;
                                if (tseg.janelas.JanelaPrincipal.portugues)
                                        message = "Você está tentando estabelecer uma unidade dentro da Tag de uma Unidade. Isso não é possível!";
                                else
                                        message = "You're trying to establish a unit within the tag of a unit. This is not possible!";
                                JOptionPane.showMessageDialog(null, message,
                                                "Ops!", JOptionPane.WARNING_MESSAGE);
                                
                                return;
                            }
                        
                            SegmentadorManual segmentador = new SegmentadorManual();

                            String texto = Controle.getJanelaPrincipal()
                                            .getAbaSegmentaca().getTextoTxaSegmentacao();

                            texto = segmentador.segmenta(txpSegmentacao, independente);

                            Controle.getJanelaPrincipal().getAbaSegmentaca()
                                            .setTextoTxaSegmentacao(texto);

                    }
            };
            
        }
        
        public ActionListener itemRemoverTodasUnidades()
        {
            return new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                            String msg, title;
                            if (tseg.janelas.JanelaPrincipal.portugues)
                            {
                                    msg = "Tem certeza que deseja remover todas as Unidades?";
                                    title = "Aviso";
                            }
                            else
                            {
                                    msg = "Are you sure you want to remove all the units?";
                                    title = "Warning";
                            }
                            int confirmDialog = JOptionPane
                                            .showConfirmDialog(
                                                            null, msg, title,
                                                            JOptionPane.YES_NO_OPTION);

                            if (confirmDialog == JOptionPane.YES_OPTION) {
                                    SegmentadorManual segmentador = new SegmentadorManual();
                                    String texto = Controle
                                                    .getJanelaPrincipal()
                                                    .getAbaSegmentaca()
                                                    .getTextoTxaSegmentacao();

                                    texto = segmentador
                                                    .removeTodasUnidades(texto);

                                    Controle.getJanelaPrincipal()
                                                    .getAbaSegmentaca()
                                                    .setTextoTxaSegmentacao(texto);
                            }
                    }
            };
        }

	public ActionListener itemSalvar() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				salvar();
			}
		};
	}

	public ActionListener itemSalvarComo() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				salvarComo();
			}
		};
	}

	public ActionListener itemSair() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int confirmaFechamento = confirmaFechamento();
				if (confirmaFechamento != JOptionPane.CANCEL_OPTION) {
					System.exit(0);
				}
			}
		};
	}

	public ActionListener itemSobre() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String sobre, title;
				if (tseg.janelas.JanelaPrincipal.portugues)
				{
					sobre = " T-Seg é uma ferramenta para marcação e segmentação de textos"
						+ " \n usada para definição de unidades mínimas de anotação de corpora."
						+ " \n Nele, o usuário define como quer que seja feita automaticamente a segmentação"
						+ " \n (por meio de regras), podendo inclusive marcar manualmente os segmentos em um"
						+ " \n texto-fonte. Caso a marcaçao seja feita manualmente, vários usuários podem"
						+ " \n executar a mesma tarefa, cabendo então ao sistema determinar o"
						+ " \n nível de confiança do esquema de marcação utilizado, com base em uma análise"
						+ " \n do grau de concordância entre os marcadores humanos.";
					title = "Sobre o T-Seg";	
				}
				else
				{
					sobre = "T-Seg is a tool for labeling and segmentation of texts "
						+ "\n used for defining minimum units of annotation corpora."
						+ "\n In it, you define how you want to be automatically segmenting"
						+ "\n (by rules), and may also manually mark the segments in one"
						+ "\n source text. If the marking is done manually, multiple users can"
						+ "\n perform the same task, then fitting the system to determine the"
						+ "\n confidence level marking scheme used, based on an analysis"
						+ "\n the degree of agreement between human markers.";
					title = "About T-Seg";
				}
				JOptionPane.showMessageDialog(null, sobre, title,
						JOptionPane.PLAIN_MESSAGE);
			}
		};
	}

	public ActionListener itemManual() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				File pdf = new File("Manual do Usuario.pdf");  
				try {  
				  Desktop.getDesktop().open(pdf);  
				}
                                catch (IllegalArgumentException ex)
                                {
                                    try
                                    {
                                        // for copying style
                                        JLabel label = new JLabel();
                                        Font font = label.getFont();

                                        // create some css from the label's font
                                        StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
                                        style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
                                        style.append("font-size:" + font.getSize() + "pt;");
                                        
                                        String mensagem = "<html><body style=\"" + style + "\">Não foi possível localizar o manual neste computador. Faça downoad clicando <a href='http://www.each.usp.br/norton/resdial/tools/TSeg_v1.0.zip'>aqui</a>.</body></html>";                                     
                                        JEditorPane je = new JEditorPane();
                                        je.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
                                        je.setText(mensagem);
                                        je.setBackground(label.getBackground());
                                        je.setEditable(false);
                                        
                                        //Criar o manipulador de Hyperlink
                                        je.addHyperlinkListener(new HyperlinkListener() {
                                            public void hyperlinkUpdate(HyperlinkEvent e) {
                                                if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                                                    if(Desktop.isDesktopSupported()) {
                                                        try {
                                                            Desktop.getDesktop().browse(e.getURL().toURI());
                                                        } catch (Exception ex3) {
                                                            //Exceção disparada se a plataforma computacional não tiver navegador
                                                            ex3.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                        
                                        JOptionPane.showMessageDialog(null, je);
                                    }
                                    catch(Exception ex2)
                                    {
                                        ex2.printStackTrace();  
                                        JOptionPane.showMessageDialog(null, "Erro no Desktop: " + ex2);  
                                    }
                                }
                                catch(Exception ex) {  
                                    ex.printStackTrace();  
                                    JOptionPane.showMessageDialog(null, "Erro no Desktop: " + ex);  
				}  
			}
		};
	}

	public ActionListener itemCorTags() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				final JFrame janelaCor = new JFrame();
				if (tseg.janelas.JanelaPrincipal.portugues)
					janelaCor.setTitle("Cores - Tags");
				else
					janelaCor.setTitle("Colors - Tags");
				janelaCor.setResizable(false);
				janelaCor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				JPanel painel = montaPainelCores(janelaCor, 1);

				janelaCor.add(painel);
				janelaCor.pack();
				janelaCor.setLocationRelativeTo(null);
				janelaCor.setVisible(true);

			}
		};
	}

	/*
	 * Tipo 1 = Tag Tipo 2 = Texto Segmentado Tipo 3 = Unidade Independente
	 */
	private JPanel montaPainelCores(final JFrame janelaCor, final int tipo) {
		final Configuracoes configuracoes = new Configuracoes();
		final JColorChooser colorChooser = new JColorChooser();

		if (tipo == 1) {
			colorChooser.setColor(configuracoes.getCorTag());
		} else {
			if (tipo == 2) {
				colorChooser.setColor(configuracoes.getCorTexto());
			} else {
				colorChooser.setColor(configuracoes.getCorIndependente());
			}
		}

		JPanel painel = new JPanel();
		painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
		
		JButton btnCorPadrao;
		if (tseg.janelas.JanelaPrincipal.portugues)
		{
			btnCorPadrao = new JButton("Restaurar cor padrão");
		}
		else
		{
			btnCorPadrao = new JButton("Restore default color");
		}
		btnCorPadrao.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnCorPadrao.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				if (tipo == 1) {
					colorChooser.setColor(configuracoes.COR_TAG_PADRAO);
				} else {
					if (tipo == 2) {
						colorChooser.setColor(configuracoes.COR_TEXTO_PADRAO);
					} else {
						colorChooser
								.setColor(configuracoes.COR_INDEPENDENTE_PADRAO);
					}
				}
			}
		});

		JButton btnOk = new JButton("Ok");
		btnOk.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				if (tipo == 1) {
					configuracoes.guardaCores(colorChooser.getColor(),
							configuracoes.getCorTexto(),
							configuracoes.getCorIndependente());
				} else {
					if (tipo == 2) {
						configuracoes.guardaCores(configuracoes.getCorTag(),
								colorChooser.getColor(),
								configuracoes.getCorIndependente());
					} else {
						configuracoes.guardaCores(configuracoes.getCorTag(),
								configuracoes.getCorTexto(),
								colorChooser.getColor());
					}
				}

				Controle.getJanelaPrincipal().getAbaSegmentaca().coloreTags();
				janelaCor.dispose();

			}
		});

		painel.add(btnCorPadrao);
		painel.add(colorChooser);
		painel.add(btnOk);

		return painel;
	}

	public ActionListener itemCorTextoSeg() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				JFrame janelaCor = new JFrame();
				if (tseg.janelas.JanelaPrincipal.portugues)
				{
					janelaCor.setTitle("Cores - Textos Segmentados");
				}
				else
				{
					janelaCor.setTitle("Colors - Segmented Texts");
				}
				janelaCor.setResizable(false);
				janelaCor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				JPanel painel = montaPainelCores(janelaCor, 2);

				janelaCor.add(painel);
				janelaCor.pack();
				janelaCor.setLocationRelativeTo(null);
				janelaCor.setVisible(true);

			}
		};
	}

	public ActionListener itemCorIndependente() {

		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				JFrame janelaCor = new JFrame();
				if (tseg.janelas.JanelaPrincipal.portugues)
				{
					janelaCor.setTitle("Cores -  Unidades Independentes");
				}
				else
				{
					janelaCor.setTitle("Colors - Independent Units");
				}
				janelaCor.setResizable(false);
				janelaCor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				JPanel painel = montaPainelCores(janelaCor, 3);

				janelaCor.add(painel);
				janelaCor.pack();
				janelaCor.setLocationRelativeTo(null);
				janelaCor.setVisible(true);

			}
		};

	}

	public ActionListener itemIngles() {

		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				JFrame janelaCor = new JFrame();
				if (tseg.janelas.JanelaPrincipal.portugues)
				{
					janelaCor.setTitle("Cores - Unidades Independentes");
				}
				else
				{
					janelaCor.setTitle("Colors - Independent Units");
				}
				janelaCor.setResizable(false);
				janelaCor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				JPanel painel = montaPainelCores(janelaCor, 3);

				janelaCor.add(painel);
				janelaCor.pack();
				janelaCor.setLocationRelativeTo(null);
				janelaCor.setVisible(true);

			}
		};

	}
	
	public WindowListener aoFecharJanela() {
		return new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				int confirmaFechamento = confirmaFechamento();
				if (confirmaFechamento != JOptionPane.CANCEL_OPTION) {
					System.exit(0);
				}
			}
		};
	}

	private int confirmaFechamento() {
		boolean modificado = Controle.isModificado();

		if (modificado) {
			File ultimoArquivo = new Configuracoes().getUltimoArquivo();
			try {
				Scanner scanner = new Scanner(ultimoArquivo);
				if (!(scanner.hasNext())) {
					salvarComo();
				} else {
					ultimoArquivo = new File(scanner.nextLine());
					scanner.close();
					String msg, title;
					if (tseg.janelas.JanelaPrincipal.portugues)
					{
						msg = "Deseja Salvar as alterações em \"";
						title = "Aviso";
					}
					else
					{
						msg = "Want to save the changes in \"";
						title = "Warning";
					}
					int confirmacao = JOptionPane.showConfirmDialog(
							null,
							msg	+ ultimoArquivo.getName() + "\"?", title,
							JOptionPane.YES_NO_CANCEL_OPTION);
					if (confirmacao == JOptionPane.YES_OPTION) {
						salvar();
					}
					return confirmacao;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	private void salvar() {
		File ultimoArquivo = new Configuracoes().getUltimoArquivo();
		try {
			Scanner scanner = new Scanner(ultimoArquivo);
			if (scanner.hasNext()) {
				String texto = Controle.getJanelaPrincipal().getAbaSegmentaca()
						.getTextoTxaSegmentacao();

				ultimoArquivo = new File(scanner.nextLine());
				scanner.close();

				new TratamentoArquivo().gravaArquivo(ultimoArquivo, texto);
				Controle.setModificado(false);
			} else {
				salvarComo();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void salvarComo() {
		JFileChooser fc = getFileChooserCustomizado();

		File ultimoArquivo = new Configuracoes().getUltimoArquivo();

		try {
			Scanner scanner = new Scanner(ultimoArquivo);
			if (scanner.hasNext()) {
				ultimoArquivo = new File(scanner.nextLine());
				fc.setCurrentDirectory(ultimoArquivo);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if (tseg.janelas.JanelaPrincipal.portugues)
		{
			fc.setDialogTitle("Salvar Arquivo Segmentado...");
		}
		else
		{
			fc.setDialogTitle("Save Segmented File...");
		}
		int retornoFc = fc.showSaveDialog(null);

		if (retornoFc == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fc.getSelectedFile();

			String textoSegmentado = Controle.getJanelaPrincipal()
					.getAbaSegmentaca().getTextoTxaSegmentacao();

			new TratamentoArquivo().gravaArquivo(selectedFile, textoSegmentado);
		}
	}
        
        public ActionListener itemDesfazer() {
		return new ActionListener() {

			@Override
                        public void actionPerformed(ActionEvent evt) {
                                GerenciadorAcoes.desfazer();
                        }
		};
	}
        
        public ActionListener itemRefazer() {
		return new ActionListener() {

			@Override
                        public void actionPerformed(ActionEvent evt) {
                                GerenciadorAcoes.refazer();
                        }
		};
	}

	private JFileChooser getFileChooserCustomizado() {
		Configuracoes configuracoes = new Configuracoes();
		JFileChooser fc = new JFileChooser(configuracoes.getDiretorioPadrao());

		try {
			Scanner scanner = new Scanner(configuracoes.getUltimoArquivo());
			if (scanner.hasNext()) {
				fc.setCurrentDirectory(new File(scanner.nextLine()));
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setAcceptAllFileFilterUsed(true);

		int quantidadeExtencoes = new FiltroExtencoes().quantidadeExtencoes();

		for (int i = 0; i <= quantidadeExtencoes; i++) {
			fc.addChoosableFileFilter(new FiltroArquivo().criaFiltro(i));
		}
		return fc;
	}
        
        //Metodo que filtra os arquivos de acordo com uma lista de extensoes validas
        private List<File> filtrarArquivos(File[] arquivos)
        {
            List<File> retorno = new ArrayList<File>();
            int i;
            FiltroArquivo filtro = new FiltroArquivo();
            
            for(i=0; i<arquivos.length; i++)
            {
                if(filtro.acceptNoDir(arquivos[i]))
                {
                    retorno.add(arquivos[i]);
                }
            }
            
            return retorno;
        }
}