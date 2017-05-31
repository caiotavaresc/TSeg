package tseg.janelas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;

import tseg.acoes.AcaoJanelaPrincipal;
import tseg.arquivo.TratamentoArquivo;
import tseg.configuracoes.Configuracoes;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.MouseInputAdapter;

public class JanelaPrincipal {
	private JFrame janela;
	private JMenuBar menuBar;
	private JPanel pnlApresentacao;
	private JPanel pnlAbas;
	private JTabbedPane tbpAbas;
	private AbaSegmentacao abaSegmentacao;
	private AbaEstatisticas abaEstatisticas;
	private AbaSegmentacaoCores abaSegCores;
	private AcaoJanelaPrincipal acao = new AcaoJanelaPrincipal();
	
        //dani
        private JPanel pnlBR;
        //private JPanel pnlUS;
        private JButton btnIngles;
        
        //textos traduzidos
        static private JMenu menuArquivo;
        static private JMenuItem itemAbrir;
        static private JMenuItem itemSalvar;
        static private JMenuItem itemSalvarComo;
        static private JMenuItem itemSair;

        static private JMenu menuEditar;
        static private JMenuItem itemDesfazer;
        static private JMenuItem itemRefazer;
        
        static private JMenu menuAjuda;
        static private JMenuItem itemSobre;
        static private JMenuItem itemManual;
        
        static private JMenu menuConf;
        static private JMenu menuLingua;
        static private JMenuItem itemInlges;
        static private JMenuItem itemPortugues;
        
        static private JMenu menuCores;
        static private JMenuItem itemCorIndependente;
        static private JMenuItem itemCorTextoSeg;
        
        static private JMenu menuSegmentacao;
        static private JMenuItem itemDefUnidade;
        static private JMenuItem itemDefUnidadeIndep;
        static private JMenuItem itemRemoverUnidade;
        static private JMenuItem itemRemoverTodasUnidades;
        static private JMenuItem itemSegmentarTodos;
        
        static private JMenu menuSegmentacaoAutomatica;
        static private JMenuItem itemPalavras;
        static private JMenuItem itemSentencas;
        static private JMenuItem itemParagrafos;
       
        //
        static public boolean portugues = true;
        
	public JanelaPrincipal() {
		Dimension resolucao = Toolkit.getDefaultToolkit().getScreenSize();
		int janelaW = (int) (resolucao.getWidth() / 1.5);
		int janelaH = (int) (resolucao.getHeight() / 1.5);

		montaPainelApresentacao();

		montaPainelAbas();
                
                montaMenuBar();

		janela = new JFrame();
		janela.setSize(janelaW, janelaH);
		janela.setLocationRelativeTo(null);
		janela.setTitle("T-Seg");
		janela.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		janela.setLayout(new BorderLayout());

		janela.addWindowListener(acao.aoFecharJanela());

		janela.setJMenuBar(menuBar);
		janela.add(pnlApresentacao, BorderLayout.NORTH);
		janela.add(pnlAbas, BorderLayout.CENTER);
               // janela.add(pnlBR, BorderLayout.EAST);

	}

	public void abreJanela() {
		File ultimoArquivo = new Configuracoes().getUltimoArquivo();

		try {
			Scanner scanner = new Scanner(ultimoArquivo);
			if (scanner.hasNext()) {
				ultimoArquivo = new File(scanner.nextLine());
				scanner.close();
				String texto = new TratamentoArquivo().leArquivo(ultimoArquivo);
				abaSegmentacao.setTextoTxaSegmentacao(texto);
                                this.alterarTituloJanela("T-Seg - " + ultimoArquivo.getAbsolutePath());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		janela.setVisible(true);
	}
        
        public void alterarTituloJanela(String titulo)
        {
            janela.setTitle(titulo);
        }

	public void mudaAba(int aba) {
		tbpAbas.setSelectedIndex(aba);
	}

	public int getAbaAtual() {
		return tbpAbas.getSelectedIndex();
	}

	public AbaSegmentacao getAbaSegmentaca() {
		return abaSegmentacao;
	}

	public AbaEstatisticas getAbaEstatisticas() {
		return abaEstatisticas;
	}

	private void montaMenuBar() {
		menuBar = new JMenuBar();

		JMenu menuArquivo = montaMenuArquivo();
                JMenu menuEditar = montaMenuEditar();
		JMenu menuAjuda = montaMenuAjuda();
                JMenu menuSegmentacao = montaMenuSegmentacao();
                JMenu menuConf = montaMenuConfiguracoes();

		menuBar.add(menuArquivo);
                menuBar.add(menuEditar);
                menuBar.add(menuSegmentacao);
                menuBar.add(menuConf);
		menuBar.add(menuAjuda);
	}

	private JMenu montaMenuArquivo() {
		itemAbrir = new JMenuItem("Abrir");
		itemAbrir.setMnemonic('b');
                itemAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
                itemAbrir.addActionListener(acao.itemAbrir());
                
		itemSalvar = new JMenuItem("Salvar");
		itemSalvar.setMnemonic('r');
                itemSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
                itemSalvar.addActionListener(acao.itemSalvar());

		itemSalvarComo = new JMenuItem("Salvar Como...");
		itemSalvarComo.setMnemonic('v');
		itemSalvarComo.addActionListener(acao.itemSalvarComo());

		itemSair = new JMenuItem("Sair");
		itemSair.setMnemonic('s');
                itemSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
		itemSair.addActionListener(acao.itemSair());

		menuArquivo = new JMenu("Arquivo");
		menuArquivo.setMnemonic('A');
		menuArquivo.add(itemAbrir);
		menuArquivo.addSeparator();
		menuArquivo.add(itemSalvar);
		menuArquivo.add(itemSalvarComo);
		menuArquivo.addSeparator();
		menuArquivo.add(itemSair);

		return menuArquivo;
	}
        
        private JMenu montaMenuEditar(){
            
                itemDesfazer = new JMenuItem("Desfazer");
                itemDesfazer.setAccelerator(KeyStroke.getKeyStroke("control Z"));
                itemDesfazer.setEnabled(false);
                itemDesfazer.addActionListener(acao.itemDesfazer());
                itemDesfazer.setMnemonic('D');
                
                itemRefazer = new JMenuItem("Refazer");
                itemRefazer.setAccelerator(KeyStroke.getKeyStroke("control Y"));
                itemRefazer.setEnabled(false);
                itemRefazer.addActionListener(acao.itemRefazer());
                itemRefazer.setMnemonic('R');
                
                menuEditar = new JMenu("Editar");
                menuEditar.setMnemonic('E');
                menuEditar.add(itemDesfazer);
                menuEditar.add(itemRefazer);
                
                return menuEditar;
        }

	private JMenu montaMenuAjuda() {
		itemSobre = new JMenuItem("Sobre o T-Seg");
		itemSobre.setMnemonic('b');
		itemSobre.addActionListener(acao.itemSobre());

		itemManual = new JMenuItem("Manual de Funcionalidades");
		itemManual.setMnemonic('m');
                itemManual.setAccelerator(KeyStroke.getKeyStroke("F1"));
		itemManual.addActionListener(acao.itemManual());

		menuAjuda = new JMenu("Ajuda");
		menuAjuda.setMnemonic('u');
		menuAjuda.add(itemSobre);
		menuAjuda.add(itemManual);

		return menuAjuda;
	}

	private JMenu montaMenuConfiguracoes() {
		JMenu menuCores = montaMenuCores();
                JMenu menuLingua = montaMenuLingua();

		menuConf = new JMenu("Configurações");
		menuConf.setMnemonic('c');
		menuConf.add(menuCores);
                menuConf.add(menuLingua);

		return menuConf;
	}

	private JMenu montaMenuCores() {
		menuCores = new JMenu("Cores");
		menuCores.setMnemonic('c');

		JMenuItem itemCorTags = new JMenuItem("Tags");
		itemCorTags.setMnemonic('a');
		itemCorTags.addActionListener(acao.itemCorTags());

		itemCorTextoSeg = new JMenuItem("Textos Segmentados");
		itemCorTextoSeg.setMnemonic('e');
		itemCorTextoSeg.addActionListener(acao.itemCorTextoSeg());

		itemCorIndependente = new JMenuItem("Unidades Independentes");
		itemCorIndependente.setMnemonic('i');
		itemCorIndependente.addActionListener(acao.itemCorIndependente());

		menuCores.add(itemCorTags);
		menuCores.add(itemCorTextoSeg);
		menuCores.add(itemCorIndependente);

		return menuCores;
	}
        
        private JMenu montaMenuLingua(){
            
                menuLingua = new JMenu("Língua");
                menuLingua.setMnemonic('L');
                
                itemInlges = new JMenuItem("English");
		itemInlges.setMnemonic('E');
		itemInlges.addActionListener(traduzIngles());
		menuLingua.add(itemInlges);
                
                itemPortugues = new JMenuItem("Português");
                itemPortugues.setMnemonic('P');
                itemPortugues.addActionListener(traduzIngles());
                menuLingua.add(itemPortugues);
                
                //Como a aplicação começa em português, essa opção fica desabilitada
                itemPortugues.setEnabled(false);
                
                return menuLingua;
        }
        
        private JMenu montaMenuSegmentacao(){
            
            JMenu menuSegmentacaoAutomatica;
            
            menuSegmentacao = new JMenu("Segmentação");
            menuSegmentacao.setMnemonic('S');
            
            menuSegmentacaoAutomatica = montaMenuSegmentacaoAutomatica();
            menuSegmentacao.add(menuSegmentacaoAutomatica);
            
            menuSegmentacao.addSeparator();
            
            itemDefUnidade = new JMenuItem("Definir nova unidade");
            itemDefUnidade.setMnemonic('u');
            itemDefUnidade.setAccelerator(KeyStroke.getKeyStroke("control U"));
            itemDefUnidade.addActionListener(acao.itemDefinirNovaUnidade(abaSegmentacao.getTxpSegmentacao(), false));
            menuSegmentacao.add(itemDefUnidade);
            
            itemDefUnidadeIndep = new JMenuItem("Definir nova unidade independente");
            itemDefUnidadeIndep.setMnemonic('p');
            itemDefUnidadeIndep.setAccelerator(KeyStroke.getKeyStroke("control shift U"));
            itemDefUnidadeIndep.addActionListener(acao.itemDefinirNovaUnidade(abaSegmentacao.getTxpSegmentacao(), true));
            menuSegmentacao.add(itemDefUnidadeIndep);
            
            menuSegmentacao.addSeparator();
            
            itemRemoverUnidade = new JMenuItem("Remover uma unidade");
            itemRemoverUnidade.setMnemonic('R');
            menuSegmentacao.add(itemRemoverUnidade);
            
            itemRemoverTodasUnidades = new JMenuItem("Remover todas as unidades");
            itemRemoverTodasUnidades.setMnemonic('t');
            itemRemoverTodasUnidades.addActionListener(acao.itemRemoverTodasUnidades());
            menuSegmentacao.add(itemRemoverTodasUnidades);
            
            menuSegmentacao.addSeparator();
            
            itemSegmentarTodos = new JMenuItem("Segmentar todos os arquivos de um diretório");
            itemSegmentarTodos.setMnemonic('S');
            menuSegmentacao.add(itemSegmentarTodos);
            
            return menuSegmentacao;
        }
        
        private JMenu montaMenuSegmentacaoAutomatica()
        {
            menuSegmentacaoAutomatica = new JMenu("Segmentação automática");
            menuSegmentacaoAutomatica.setMnemonic('a');
            
            itemPalavras = new JMenuItem("Palavras");
            itemPalavras.setMnemonic('P');
            itemPalavras.addActionListener(acao.itemSegmentacaoAutomaticaPalavras());
            menuSegmentacaoAutomatica.add(itemPalavras);
            
            itemSentencas = new JMenuItem("Sentenças");
            itemSentencas.setMnemonic('S');
            itemSentencas.addActionListener(acao.itemSegmentacaoAutomaticaSentencas());
            menuSegmentacaoAutomatica.add(itemSentencas);
            
            itemParagrafos = new JMenuItem("Parágrafos");
            itemParagrafos.setMnemonic('a');
            itemParagrafos.addActionListener(acao.itemSegmentacaoAutomaticaParagrafos());
            menuSegmentacaoAutomatica.add(itemParagrafos);
            
            return menuSegmentacaoAutomatica;
        }

	private void montaPainelApresentacao() {
            
		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/imagens/T-Seg.png"));

		JLabel lblApresentacao = new JLabel(imageIcon);
		
		pnlApresentacao = new JPanel();
		pnlApresentacao.setLayout(new FlowLayout());
		pnlApresentacao.add(lblApresentacao);
		
      //  btnIngles = new JButton();
	//	btnIngles.setText("Inglês");
		//btnIngles.setMnemonic('I');
		//btnIngles.addActionListener(traduzIngles());
        //pnlBR = new JPanel();
		//pnlBR.setLayout(new FlowLayout());
		//pnlBR.add(btnIngles);
	}
        
        public ActionListener traduzIngles() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
					if(portugues)
					{
							portugues = false;
							//btnIngles.setText("Portugu�s");
							//btnIngles.setMnemonic('P');
							itemInlges.setEnabled(false);
                                                        itemPortugues.setEnabled(true);
                            itemAbrir.setText("Open");
                            itemManual.setText("Manual Features");
                            itemSair.setText("Exit");
                            itemSalvar.setText("Save");
                            itemSalvarComo.setText("Save As...");
                            
                            menuEditar.setText("Edit");
                            
                            itemDesfazer.setText("Undo");
                            itemDesfazer.setMnemonic('U');
                            
                            itemRefazer.setText("Redo");
                            itemRefazer.setMnemonic('R');
                            
                            itemSobre.setText("About TSeg");
                            
                            menuAjuda.setText("Help");
                            menuAjuda.setMnemonic('H');
                            
                            menuArquivo.setText("File");
                            menuConf.setText("Settings");
                            menuCores.setText("Color");
                            menuLingua.setText("Language");
                            menuSegmentacao.setText("Segmentation");
                            
                            menuSegmentacaoAutomatica.setText("Automatic segmentation");
                            menuSegmentacaoAutomatica.setMnemonic('A');
                            
                            itemPalavras.setText("Words");
                            itemPalavras.setMnemonic('W');
                            
                            itemSentencas.setText("Sentences");
                            
                            itemParagrafos.setText("Paragraphs");
                            itemParagrafos.setMnemonic('P');
                            
                            itemDefUnidade.setText("Define new unit");
                            itemDefUnidadeIndep.setText("Define new independent unit");
                            itemRemoverUnidade.setText("Remove a unit");
                            
                            itemRemoverTodasUnidades.setText("Remove all units");
                            itemRemoverTodasUnidades.setMnemonic('a');
                            
                            itemSegmentarTodos.setText("Segment all files from a directory");
                            abaSegmentacao.setTextBtnSegAuto("Automatic segmentation","See the options Automatic Segmentation.");
                            itemCorIndependente.setText("Independent Units");
                            itemCorTextoSeg.setText("Segmented Text");
                            tbpAbas.setTitleAt(0, "Segmentation - XML");
                            tbpAbas.setTitleAt(1, "Statistics");
                            tbpAbas.setMnemonicAt(0, 'g');
                            tbpAbas.setMnemonicAt(1, 's');
                            tbpAbas.setTitleAt(2, "Segmentation - Colors");
                            tbpAbas.setMnemonicAt(2, 'a');
                            abaSegCores.setTextButton("Generate color scheme ", "Generate a representation of the color segmentation");
					}
					else
					{
						portugues = true;
						//btnIngles.setText("Inglês");
						//btnIngles.setMnemonic('I');
						itemInlges.setEnabled(true);
                                                itemPortugues.setEnabled(false);
                        itemAbrir.setText("Abrir");
                        itemManual.setText("Manual de Funcionalidades");
                        itemSair.setText("Sair");
                        itemSalvar.setText("Salvar");
                        itemSalvarComo.setText("Salvar Como...");
                        
                        menuEditar.setText("Editar");
                        
                        itemDesfazer.setText("Desfazer");
                        itemDesfazer.setMnemonic('D');
                        
                        itemRefazer.setText("Refazer");
                        itemRefazer.setMnemonic('R');
                        
                        itemSobre.setText("Sobre o TSeg");
                        
                        menuAjuda.setText("Ajuda");
                        menuAjuda.setMnemonic('u');
                        
                        menuArquivo.setText("Arquivo");
                        menuConf.setText("Configurações");
                        menuCores.setText("Cores");
                        menuLingua.setText("Língua");
                        menuSegmentacao.setText("Segmentação");
                        
                        menuSegmentacaoAutomatica.setText("Segmentação automática");
                        menuSegmentacaoAutomatica.setMnemonic('a');
                        
                        itemPalavras.setText("Palavras");
                        itemPalavras.setMnemonic('P');
                        
                        itemSentencas.setText("Sentenças");
                        
                        itemParagrafos.setText("Parágrafos");
                        itemParagrafos.setMnemonic('a');
                        
                        itemDefUnidade.setText("Definir nova unidade");
                        itemDefUnidadeIndep.setText("Definir nova unidade independente");
                        itemRemoverUnidade.setText("Remover uma unidade");
                        
                        itemRemoverTodasUnidades.setText("Remover todas as unidades");
                        itemRemoverTodasUnidades.setMnemonic('t');
                        
                        itemSegmentarTodos.setText("Segmentar todos os arquivos de um diretório");
                        abaSegmentacao.setTextBtnSegAuto("Segmentação Automática","Veja as opções de segmentação automática.");
                        itemCorIndependente.setText("Unidades Independentes");
                        itemCorTextoSeg.setText("Texto Segmentado");
                        tbpAbas.setTitleAt(0, "Segmentação - XML");
                        tbpAbas.setTitleAt(1, "Estatísticas");
                        tbpAbas.setTitleAt(2, "Segmentação - Cores");
                        tbpAbas.setMnemonicAt(0, 'g');
                        tbpAbas.setMnemonicAt(1, 'e');
                        tbpAbas.setMnemonicAt(2, 'a');
                        abaSegCores.setTextButton("Gerar um esquema de cores", "Gere uma representação por cores da segmentação");
					}
			}
                };
        }
        
	private void montaPainelAbas() {
		tbpAbas = new JTabbedPane();
		abaSegmentacao = new AbaSegmentacao();
		abaEstatisticas = new AbaEstatisticas();
		abaSegCores = new AbaSegmentacaoCores();

		tbpAbas.addTab("Segmentação - XML", abaSegmentacao.getAbaSegmentacao());
		tbpAbas.setMnemonicAt(0, 'g');
		tbpAbas.addTab("Estatisticas", abaEstatisticas.getAbaEstatisticas());
		tbpAbas.setMnemonicAt(1, 'e');
		tbpAbas.addTab("Segmentação - Cores", abaSegCores.getAbaSegCores());
		tbpAbas.setMnemonicAt(2, 'a');

		
		tbpAbas.addChangeListener(acao.aoMudarAba());

		Border border = BorderFactory.createEmptyBorder(0, 1, 1, 1);

		pnlAbas = new JPanel();
		pnlAbas.setBorder(border);
		pnlAbas.setLayout(new BorderLayout());
		pnlAbas.add(tbpAbas);
	}
        
        public void habilitarDesfazer(boolean status)
        {
            this.itemDesfazer.setEnabled(status);
        }
        
        public void habilitarRefazer(boolean status)
        {
            this.itemRefazer.setEnabled(status);
        }
}