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

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
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
import tseg.janelas.ModalProgressoSegmentacao;
import tseg.janelas.ModalRemoverUmaUnidade;
import tseg.janelas.ModalSegmentarDiretorio;
import tseg.janelas.ModalSobre;
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
                                        fc.setAcceptAllFileFilterUsed(false);
                                        
					int retornoFc = fc.showDialog(Controle.getJanelaPrincipal().getFrame(), abrir);

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
                                if (tabbedPane.getSelectedIndex() == 2) {
                                        Controle.getJanelaPrincipal().getAbaSegmentacaoCores().gerarSegmentacaoCores();
                                }
			}
		};
	}
        
        public ActionListener segmentarDiretorio2()
        {
            return new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    boolean portugues = Controle.getJanelaPrincipal().isPortugues();
                    
                    ModalSegmentarDiretorio popup = new ModalSegmentarDiretorio(portugues);
                }
            };
        }
        
        public ActionListener itemSegmentacaoAutomaticaPalavras() {
		return new ActionListener() {

			@Override
                        public void actionPerformed(ActionEvent evt) {
                            
                                GerenciadorAcoes.voltarAbaPrincipal();
                            
                                SegmentadorAutomatico segmentador = new SegmentadorAutomatico();
                                String texto = Controle.getJanelaPrincipal()
                                                .getAbaSegmentaca().getTextoTxaSegmentacao();

                                //Exeibir a janela de progresso
                                ModalProgressoSegmentacao modal = new ModalProgressoSegmentacao();
                                
                                texto = segmentador.segmenta(texto,
                                                SegmentadorAutomatico.PALAVRAS, modal);

                                Controle.getJanelaPrincipal().getAbaSegmentaca()
                                                .setTextoTxaSegmentacao(texto);
                                
                                modal.ocultar();
                                modal = null;
                                
                                Controle.setCursorInicioTexto();
                        }
		};
	}
        
        public ActionListener itemSegmentacaoAutomaticaSentencas() {
		return new ActionListener() {

			@Override
                        public void actionPerformed(ActionEvent evt) {
                                GerenciadorAcoes.voltarAbaPrincipal();
                            
                                SegmentadorAutomatico segmentador = new SegmentadorAutomatico();
                                String texto = Controle.getJanelaPrincipal()
                                                .getAbaSegmentaca().getTextoTxaSegmentacao();

                                //Exibir a janela de progresso
                                ModalProgressoSegmentacao modal = new ModalProgressoSegmentacao();
                                
                                texto = segmentador.segmenta(texto,
                                                SegmentadorAutomatico.SENTENCAS, modal);

                                Controle.getJanelaPrincipal().getAbaSegmentaca()
                                                .setTextoTxaSegmentacao(texto);
                                
                                modal.setVisible(false);
                                modal = null;
                                
                                Controle.setCursorInicioTexto();
                        }
		};
	}
                
        public ActionListener itemSegmentacaoAutomaticaParagrafos() {
		return new ActionListener() {

			@Override
                        public void actionPerformed(ActionEvent evt) {
                                GerenciadorAcoes.voltarAbaPrincipal();
                            
                                SegmentadorAutomatico segmentador = new SegmentadorAutomatico();
                                String texto = Controle.getJanelaPrincipal()
                                                .getAbaSegmentaca().getTextoTxaSegmentacao();

                                //Exibir a janela de progresso
                                ModalProgressoSegmentacao modal = new ModalProgressoSegmentacao();
                                
                                texto = segmentador.segmenta(texto,
                                                SegmentadorAutomatico.PARAGRAGOS, modal);

                                Controle.getJanelaPrincipal().getAbaSegmentaca()
                                                .setTextoTxaSegmentacao(texto);
                                
                                modal.ocultar();
                                modal = null;
                                
                                Controle.setCursorInicioTexto();
                        }
		};
	}
        
        public ActionListener itemDefinirNovaUnidade(JTextPane txpSegmentacao, boolean independente){
         
            return new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        
                            GerenciadorAcoes.voltarAbaPrincipal();
                        
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
                            
                            Controle.getJanelaPrincipal().getAbaSegmentaca().getTxpSegmentacao().setCaretPosition(selecaoInicio);

                    }
            };
            
        }
        
        public ActionListener itemRemoverUmaUnidade()
        {
            return new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    GerenciadorAcoes.voltarAbaPrincipal();
                    
                    boolean portugues = Controle.getJanelaPrincipal().isPortugues();
                    
                    ModalRemoverUmaUnidade popup = new ModalRemoverUmaUnidade(portugues);
                }
            };
        }
        
        public ActionListener itemRemoverTodasUnidades()
        {
            return new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        
                            GerenciadorAcoes.voltarAbaPrincipal();
                        
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
                                    
                                    Controle.setCursorInicioTexto();
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
                            new ModalSobre(Controle.getJanelaPrincipal().isPortugues());
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
                                        String texto, titulo;
                                        
                                        if (Controle.getJanelaPrincipal().isPortugues())
                                        {
                                            texto = "Não foi possível localizar o manual neste computador. Faça download clicando <a href='http://www.each.usp.br/norton/resdial/tools/TSeg_v1.0.zip'>aqui</a>";
                                            titulo = "Erro";
                                        }
                                        else
                                        {   
                                            texto = "The manual could not be found on this computer. Make download clicking <a href='http://www.each.usp.br/norton/resdial/tools/TSeg_v1.0.zip'>here</a>";
                                            titulo = "Error";
                                        }
                                            
                                        // for copying style
                                        JLabel label = new JLabel();
                                        Font font = label.getFont();

                                        // create some css from the label's font
                                        StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
                                        style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
                                        style.append("font-size:" + font.getSize() + "pt;");
                                        
                                        String mensagem = "<html><body style=\"" + style + "\">" + texto + ".</body></html>";                                     
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
                                        
                                        JOptionPane.showMessageDialog(null, je, titulo, JOptionPane.ERROR_MESSAGE);
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
				final JDialog janelaCor = new JDialog();
				if (tseg.janelas.JanelaPrincipal.portugues)
					janelaCor.setTitle("XML - Tags");
				else
					janelaCor.setTitle("XML - Tags");
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
        
	public ActionListener itemCorUnidades() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				final JDialog janelaCor = new JDialog();
				if (tseg.janelas.JanelaPrincipal.portugues)
					janelaCor.setTitle("Esquema de Cores - Unidades");
				else
					janelaCor.setTitle("Color scheme - Units");
				janelaCor.setResizable(false);
				janelaCor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				JPanel painel = montaPainelCores(janelaCor, 4);

				janelaCor.add(painel);
				janelaCor.pack();
				janelaCor.setLocationRelativeTo(null);
				janelaCor.setVisible(true);

			}
		};
	}

	public ActionListener itemCorUnidades2() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				final JDialog janelaCor = new JDialog();
				if (tseg.janelas.JanelaPrincipal.portugues)
					janelaCor.setTitle("Esquema de Cores - Unidades (intercaladas)");
				else
					janelaCor.setTitle("Color scheme - Interleaved units");
				janelaCor.setResizable(false);
				janelaCor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				JPanel painel = montaPainelCores(janelaCor, 5);

				janelaCor.add(painel);
				janelaCor.pack();
				janelaCor.setLocationRelativeTo(null);
				janelaCor.setVisible(true);

			}
		};
	}        
        
	public ActionListener itemCorUnidadesEmbutidas() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				final JDialog janelaCor = new JDialog();
				if (tseg.janelas.JanelaPrincipal.portugues)
					janelaCor.setTitle("Esquema de Cores - Unidades embutidas");
				else
					janelaCor.setTitle("Color scheme - Built-in units");
				janelaCor.setResizable(false);
				janelaCor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				JPanel painel = montaPainelCores(janelaCor, 6);

				janelaCor.add(painel);
				janelaCor.pack();
				janelaCor.setLocationRelativeTo(null);
				janelaCor.setVisible(true);

			}
		};
	}
        
	public ActionListener itemCorUnidadesSobrepostas() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				final JDialog janelaCor = new JDialog();
				if (tseg.janelas.JanelaPrincipal.portugues)
					janelaCor.setTitle("Esquema de Cores - Segmentos sobrepostos");
				else
					janelaCor.setTitle("Color scheme - Overlapping segments");
				janelaCor.setResizable(false);
				janelaCor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				JPanel painel = montaPainelCores(janelaCor, 7);

				janelaCor.add(painel);
				janelaCor.pack();
				janelaCor.setLocationRelativeTo(null);
				janelaCor.setVisible(true);

			}
		};
	}
        
	public ActionListener itemCorUnidadesIndependentes() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				final JDialog janelaCor = new JDialog();
				if (tseg.janelas.JanelaPrincipal.portugues)
					janelaCor.setTitle("Esquema de Cores - Unidades independentes");
				else
					janelaCor.setTitle("Color scheme - Independent units");
				janelaCor.setResizable(false);
				janelaCor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				JPanel painel = montaPainelCores(janelaCor, 8);

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
	private JPanel montaPainelCores(final JDialog janelaCor, final int tipo) {
		final Configuracoes configuracoes = new Configuracoes();
		final JColorChooser colorChooser = new JColorChooser();
                janelaCor.setModal(true);

                switch(tipo)
                {
                    case 1:
                        colorChooser.setColor(configuracoes.getCorTag());
                        break;
                    case 2:
                        colorChooser.setColor(configuracoes.getCorTexto());
                        break;
                    case 3:
                        colorChooser.setColor(configuracoes.getCorIndependente());
                        break;
                    case 4:
                        colorChooser.setColor(configuracoes.getCorUnidade1());
                        break;
                    case 5:
                        colorChooser.setColor(configuracoes.getCorUnidade2());
                        break;
                    case 6:
                        colorChooser.setColor(configuracoes.getCorEmbutida());
                        break;
                    case 7:
                        colorChooser.setColor(configuracoes.getCorSobreposta());
                        break;
                    case 8:
                        colorChooser.setColor(configuracoes.getCorIndep());
                        break;
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
                            
                                switch(tipo)
                                {
                                    case 1:
                                        colorChooser.setColor(configuracoes.COR_TAG_PADRAO);
                                        break;
                                    case 2:
                                        colorChooser.setColor(configuracoes.COR_TEXTO_PADRAO);
                                        break;
                                    case 3:
                                        colorChooser.setColor(configuracoes.COR_INDEPENDENTE_PADRAO);
                                        break;
                                    case 4:
                                        colorChooser.setColor(configuracoes.COR_FUNDO_UNIDADE_PADRAO);
                                        break;
                                    case 5:
                                        colorChooser.setColor(configuracoes.COR_FUNDO_UNIDADE_2_PADRAO);
                                        break;
                                    case 6:
                                        colorChooser.setColor(configuracoes.COR_FUNDO_EMBUTIDA_PADRAO);
                                        break;
                                    case 7:
                                        colorChooser.setColor(configuracoes.COR_FUNDO_SOBREPOSTA_PADRAO);
                                        break;
                                    case 8:
                                        colorChooser.setColor(configuracoes.COR_FUNDO_INDEPENDENTE_PADRAO);
                                        break;
                                }
			}
		});

		JButton btnOk = new JButton("Ok");
		btnOk.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
                                switch(tipo)
                                {
                                    case 1:
                                        configuracoes.setCorTag(colorChooser.getColor());
                                        break;
                                    case 2:
                                        configuracoes.setCorTexto(colorChooser.getColor());
                                        break;
                                    case 3:
                                        configuracoes.setCorIndependente(colorChooser.getColor());
                                        break;
                                    case 4:
                                        configuracoes.setCorUnidade1(colorChooser.getColor());
                                        break;
                                    case 5:
                                        configuracoes.setCorUnidade2(colorChooser.getColor());
                                        break;
                                    case 6:
                                        configuracoes.setCorEmbutida(colorChooser.getColor());
                                        break;
                                    case 7:
                                        configuracoes.setCorSobreposta(colorChooser.getColor());
                                        break;
                                    case 8:
                                        configuracoes.setCorIndep(colorChooser.getColor());
                                        break;
                                }

                                if(tipo <= 3)
                                    Controle.getJanelaPrincipal().getAbaSegmentaca().coloreTags();
                                else
                                    Controle.getJanelaPrincipal().getAbaSegmentacaoCores().gerarSegmentacaoCores();
                                    Controle.getJanelaPrincipal().getAbaSegmentacaoCores().atribuirCores();
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
				JDialog janelaCor = new JDialog();
				if (tseg.janelas.JanelaPrincipal.portugues)
				{
					janelaCor.setTitle("XML - Textos Segmentados");
				}
				else
				{
					janelaCor.setTitle("XML - Segmented Texts");
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
				JDialog janelaCor = new JDialog();
				if (tseg.janelas.JanelaPrincipal.portugues)
				{
					janelaCor.setTitle("XML -  Unidades Independentes");
				}
				else
				{
					janelaCor.setTitle("XML - Independent Units");
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
				JDialog janelaCor = new JDialog();
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
                fc.setAcceptAllFileFilterUsed(false);
		int retornoFc = fc.showSaveDialog(Controle.getJanelaPrincipal().getFrame());

		if (retornoFc == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fc.getSelectedFile();
                        String extensao = FiltroExtencoes.retornaExtencao(selectedFile);
                        
                        if(extensao == null || !extensao.equals("txt"))
                        {
                            selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
                        }
                        
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

	public static JFileChooser getFileChooserCustomizado() {
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
        
}