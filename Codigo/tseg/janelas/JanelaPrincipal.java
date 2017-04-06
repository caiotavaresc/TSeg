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
        static private JMenuItem itemAbrir;
        static private JMenuItem itemSegmentarTodos;
        static private JMenuItem itemSalvar;
        static private JMenuItem itemSalvarComo;
        static private JMenuItem itemSair;
        static private JMenu menuArquivo;
        static private JMenuItem itemSobre;
        static private JMenuItem itemManual;
        static private JMenu menuAjuda;
        static private JMenu menuConf;
        static private JMenu menuCores;
        static private JMenuItem itemInlges;
        static private JMenuItem itemCorIndependente;
        static private JMenuItem itemCorTextoSeg;
       
        //
        static public boolean portugues = true;
        
	public JanelaPrincipal() {
		Dimension resolucao = Toolkit.getDefaultToolkit().getScreenSize();
		int janelaW = (int) (resolucao.getWidth() / 1.5);
		int janelaH = (int) (resolucao.getHeight() / 1.5);

		montaMenuBar();

		montaPainelApresentacao();

		montaPainelAbas();

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
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		janela.setVisible(true);
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
		JMenu menuAjuda = montaMenuAjuda();

		menuBar.add(menuArquivo);
		menuBar.add(menuAjuda);
	}

	private JMenu montaMenuArquivo() {
		itemAbrir = new JMenuItem("Abrir");
		itemAbrir.setMnemonic('b');
		itemAbrir.addActionListener(acao.itemAbrir());

                itemSegmentarTodos = new JMenuItem("Segmentar diretório");
                itemSegmentarTodos.setMnemonic('d');
                itemSegmentarTodos.addActionListener(acao.segmentarDiretorio());
                
		itemSalvar = new JMenuItem("Salvar");
		itemSalvar.setMnemonic('r');
		itemSalvar.addActionListener(acao.itemSalvar());

		itemSalvarComo = new JMenuItem("Salvar Como...");
		itemSalvarComo.setMnemonic('v');
		itemSalvarComo.addActionListener(acao.itemSalvarComo());

		itemSair = new JMenuItem("Sair");
		itemSair.setMnemonic('s');
		itemSair.addActionListener(acao.itemSair());

		menuArquivo = new JMenu("Arquivo");
		menuArquivo.setMnemonic('A');
		menuArquivo.add(itemAbrir);
                menuArquivo.add(itemSegmentarTodos);
		menuArquivo.addSeparator();
		menuArquivo.add(itemSalvar);
		menuArquivo.add(itemSalvarComo);
		menuArquivo.addSeparator();
		menuArquivo.add(itemSair);

		return menuArquivo;
	}

	private JMenu montaMenuAjuda() {
		itemSobre = new JMenuItem("Sobre o T-Seg");
		itemSobre.setMnemonic('b');
		itemSobre.addActionListener(acao.itemSobre());

		itemManual = new JMenuItem("Manual de Funcionalidades");
		itemManual.setMnemonic('m');
		itemManual.addActionListener(acao.itemManual());

		JMenu menuConfiguracoes = montaMenuConfiguracoes();

		menuAjuda = new JMenu("Ajuda");
		menuAjuda.setMnemonic('u');
		menuAjuda.add(itemSobre);
		menuAjuda.add(itemManual);
		menuAjuda.add(menuConfiguracoes);

		return menuAjuda;
	}

	private JMenu montaMenuConfiguracoes() {
		JMenu menuCores = montaMenuCores();

		menuConf = new JMenu("Configurações");
		menuConf.setMnemonic('c');
		menuConf.add(menuCores);
		itemInlges = new JMenuItem("Inglês");
		itemInlges.setMnemonic('I');
		itemInlges.addActionListener(traduzIngles());
		menuConf.add(itemInlges);

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
							itemInlges.setText("Português");
                            itemAbrir.setText("Open");
                            itemSegmentarTodos.setText("Segment Directory");
                            itemManual.setText("Manual Features");
                            itemSair.setText("Exit");
                            itemSalvar.setText("Save");
                            itemSalvarComo.setText("Save As...");
                            itemSobre.setText("About TSeg");
                            menuAjuda.setText("Help");
                            menuArquivo.setText("File");
                            menuConf.setText("Settings");
                            menuCores.setText("Color");
                            abaSegmentacao.setTextBtnSegAuto("Automatic segmentation","See the options Automatic Segmentation.");
                            itemCorIndependente.setText("Independent Units");
                            itemCorTextoSeg.setText("Segmented Text");
                            tbpAbas.setTitleAt(0, "Segmentation");
                            tbpAbas.setTitleAt(1, "Statistics");
                            tbpAbas.setMnemonicAt(0, 'g');
                            tbpAbas.setMnemonicAt(1, 's');
                            tbpAbas.setTitleAt(2, "Segmentation");
                            tbpAbas.setMnemonicAt(2, 'a');
                            abaSegCores.setTextButton("Generate color scheme ", "Generate a representation of the color segmentation");
					}
					else
					{
						portugues = true;
						//btnIngles.setText("Ingl�s");
						//btnIngles.setMnemonic('I');
						itemInlges.setText("Ingl�s");
                        itemAbrir.setText("Abrir");
                        itemManual.setText("Manual de Funcionalidades");
                        itemSair.setText("Sair");
                        itemSalvar.setText("Salvar");
                        itemSalvarComo.setText("Salvar Como...");
                        itemSobre.setText("Sobre o TSeg");
                        menuAjuda.setText("Ajuda");
                        menuArquivo.setText("Arquivo");
                        menuConf.setText("Configurações");
                        menuCores.setText("Cores");
                        abaSegmentacao.setTextBtnSegAuto("Segmentação Automática","Veja as opções de segmentação automática.");
                        itemCorIndependente.setText("Unidades Independentes");
                        itemCorTextoSeg.setText("Texto Segmentado");
                        tbpAbas.setTitleAt(0, "Segmentação - XML");
                        tbpAbas.setTitleAt(1, "Estatísticas");
                        tbpAbas.setTitleAt(2, "Segmentação");
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
		tbpAbas.addTab("Segmentação", abaSegCores.getAbaSegCores());
		tbpAbas.setMnemonicAt(2, 'a');

		
		tbpAbas.addChangeListener(acao.aoMudarAba());

		Border border = BorderFactory.createEmptyBorder(0, 1, 1, 1);

		pnlAbas = new JPanel();
		pnlAbas.setBorder(border);
		pnlAbas.setLayout(new BorderLayout());
		pnlAbas.add(tbpAbas);
	}
}