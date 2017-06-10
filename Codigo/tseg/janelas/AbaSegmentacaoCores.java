package tseg.janelas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;  
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import tseg.acoes.AcaoAbaSegmentacaoCores;
import tseg.configuracoes.Configuracoes;
import tseg.controle.Controle;

public class AbaSegmentacaoCores {
	private JPanel abaSegCores;
	private JScrollPane spnlSegCores;
	private JPanel pnlBotoes;
        private LegendaCores legenda;
	public JTextPane txSegCores;
	private static JButton btnSeg;
	private static AbaSegmentacao abaSegmentacao = new AbaSegmentacao();
	String texto;
	AcaoAbaSegmentacaoCores acao;
	
	public AbaSegmentacaoCores() {
		montaPainelSegCores();
		montaPainelBotoes();
                legenda = new LegendaCores();
                atribuirCores();
                
                acao = new AcaoAbaSegmentacaoCores();
		
		abaSegCores = new JPanel();
		abaSegCores.setLayout(new BorderLayout());
		abaSegCores.add(spnlSegCores, BorderLayout.CENTER);
		//abaSegCores.add(pnlBotoes, BorderLayout.NORTH);
                abaSegCores.add(legenda, BorderLayout.SOUTH);
		//txSegCores.setEditable(false);
                txSegCores.addKeyListener(acao.txpSegmentacaoKeyListener());
	}
	
	TreeMap<String, Integer> hmUnit = new TreeMap<>();
	TreeMap<Integer, Integer> hmUnitFinal = new TreeMap<>();
	
	TreeMap<String, Integer> hmIndep = new TreeMap<>();
	TreeMap<Integer, Integer> hmIndepFinal = new TreeMap<>();
	
	
	public ActionListener btnSegActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
						
                            gerarSegmentacaoCores();
                            
			}
		};
	}
	
	private void trataUnidadeInicio(int pos)
	{
		if (texto.charAt(pos+6)=='i')
		{
			//unidade
			int b = pos+10;
			String id = "";
			while(texto.charAt(b)!='\"')
			{	
				id += texto.charAt(b);
				b++;
			}
			String txt = "<unit id=\""+id+"\">";
			texto = texto.replace(txt, "");
			hmUnit.put(txt, (pos));
		}
		else
		{
			int b = pos+14;
			String id = "";
			while(texto.charAt(b)!='\"')
			{	
				id += texto.charAt(b);
				b++;
			}
			String txt = "<unit IND id=\""+id+"\">";
			texto = texto.replace(txt, "");
			hmIndep.put(txt, (pos));
		}
	}
        
        public void gerarSegmentacaoCores()
        {
            hmUnit = new TreeMap<>();
            hmUnitFinal = new TreeMap<>();

            hmIndep = new TreeMap<>();
            hmIndepFinal = new TreeMap<>();
            
            texto = Controle.getJanelaPrincipal().getAbaSegmentaca().getTextoTxaSegmentacao();
            
            int retira = 172;

            texto = texto.substring(retira);

            for(int i=0;i<5;i++)
            {
                    if(texto.charAt(i)=='>')
                    {
                            texto = texto.substring(172);
                            retira = retira + i;
                    }
            }


            //txSegCores.setText(texto);
            //txSegCores.setEditable(false);

            resetarFormatacao();

            MutableAttributeSet attributes = txSegCores.getInputAttributes();

            Configuracoes configuracoes = new Configuracoes();

            StyleConstants.setBackground(attributes, configuracoes.getCorSemUnidade());

            StyledDocument doc = txSegCores.getStyledDocument();

            char[] inicioUnit = {'<'};//,'U','N','I','T',' '}; 
            char[] fimUnit = {'/'};//,'U','N','I','T',' '};//{'<'
            texto = texto.replace("</document>","");
            //txSegCores.setText(texto);

            for (int x=0;x<texto.length();x++)
            {
                    if (texto.charAt(x)==inicioUnit[0])
                    {
                            //posIni = x;
                            if (texto.charAt(x+1)==fimUnit[0])
                            {
                                    //unidade fim
                                    trataUnidadeFim(x);
                            }
                            else
                            {
                                    //unidade inicio
                                    trataUnidadeInicio(x);
                            }
                    }
            }

            txSegCores.setText(texto);
            Set<Integer> chaves = hmUnitFinal.keySet();  
            int i = 0;
            int fimAnt=0;
            int inicioAnt=0;
            boolean mudei = false;
            TreeMap<Integer,Color> arvoreCor = new TreeMap<Integer,Color>();
            Color prox = configuracoes.getCorUnidade1();

            for (Integer chave : chaves)  
            {  
                int posIni = chave;
                //posIni = (posIni < 0) ? 0 : posIni;
                int posFim = hmUnitFinal.get(chave);

                if (posFim<fimAnt)
                {
                    //embutida
                    StyleConstants.setBackground(attributes, configuracoes.getCorEmbutida());
                    doc.setCharacterAttributes(posIni , (posFim-posIni), attributes, false);
                    mudei  = true;
                }
                else
                {
                    if ((posIni<fimAnt)&&(posFim>fimAnt))
                    {
                            //sobreposta
                            StyleConstants.setBackground(attributes, Color.magenta);//configuracoes.getCorSobreposta());
                            doc.setCharacterAttributes(posIni , (fimAnt-posIni), attributes, false);
                            StyleConstants.setBackground(attributes, prox);
                            doc.setCharacterAttributes(posIni , (posFim-posIni), attributes, false);
                            StyleConstants.setBackground(attributes, configuracoes.COR_FUNDO_SOBREPOSTA);//configuracoes.getCorSobreposta());
                            doc.setCharacterAttributes(posIni , (fimAnt-posIni), attributes, false);
                            mudei = true;
                            if (prox.equals(configuracoes.getCorUnidade1()))
                            {
                                    prox=configuracoes.getCorUnidade2();
                            }
                            else
                            {
                                    prox=configuracoes.getCorUnidade1();
                            }
                    }
                }
                if (mudei==false)
                {
                    StyleConstants.setBackground(attributes, prox);
                    doc.setCharacterAttributes(posIni , (posFim-posIni), attributes, false);
                    if (prox.equals(configuracoes.getCorUnidade1()))
                    {
                            prox=configuracoes.getCorUnidade2();
                    }
                    else
                    {
                            prox=configuracoes.getCorUnidade1();
                    }
                }
                else
                {

                    mudei = false;
                }
                fimAnt = posFim;
                inicioAnt = posIni;
            }  

            //unidades independentes
            Set<Integer> chavess = hmIndepFinal.keySet();  
            for (Integer chave : chavess)  
            {  
                    int posIni = chave;
                int posFim = hmIndepFinal.get(chave);
                            StyleConstants.setBackground(attributes, configuracoes.getCorIndep());
                doc.setCharacterAttributes(posIni , (posFim-posIni), attributes, false);
                i++;
            }  
            
            txSegCores.setCaretPosition(0);
        }
	
	public void trataUnidadeFim(int pos){
		if (texto.charAt(pos+7)=='i')
		{
			//unidade
			int b = pos+11;
			String id = "";
			while(texto.charAt(b)!='\"')
			{	
				id += texto.charAt(b);
				b++;
			}
			String txt = "</unit id=\""+id+"\">";
			texto = texto.replace(txt, "");
			hmUnit.put(txt, (pos));
			String aux = txt.replace("/", "");
			int bola = hmUnit.get(aux);
			hmUnitFinal.put(bola,pos);
		}
		else
		{
			int b = pos+15;
			String id = "";
			while(texto.charAt(b)!='\"')
			{	
				id += texto.charAt(b);
				b++;
			}
			String txt = "</unit IND id=\""+id+"\">";
			texto = texto.replace(txt, "");
			hmIndep.put(txt, (pos));
			String aux = txt.replace("/", "");
			int bola = hmIndep.get(aux);
			hmIndepFinal.put(bola,pos);
		}
	}
	
	private void resetarFormatacao() {
		MutableAttributeSet attributes = txSegCores.getInputAttributes();

		StyleConstants.setBackground(attributes, Color.white);
		StyleConstants.setBold(attributes, false);

		StyledDocument doc = txSegCores.getStyledDocument();

		doc.setCharacterAttributes(0, txSegCores.getText().length(),attributes, false);
	}
				
	public JPanel getAbaSegCores() {
		return abaSegCores;
	}

	private void montaPainelSegCores() {
		txSegCores = new JTextPane();
		spnlSegCores = new JScrollPane(txSegCores);
	}
	
	private void montaPainelBotoes() {
		btnSeg = new JButton();
		btnSeg.setText("Gerar esquema de cores");
		String tooltip = "Gere uma representação por cores da segmentação";
		btnSeg.setMnemonic('g');
		btnSeg.setToolTipText(tooltip);
		btnSeg.addActionListener(btnSegActionListener());
		pnlBotoes = new JPanel();
		pnlBotoes.add(btnSeg);
	}

	public static void setTextButton(String txt, String tooltip)
	{
		btnSeg.setText(txt);
		btnSeg.setToolTipText(tooltip);
	}
        
    public void atribuirCores()
    {
        Configuracoes conf = new Configuracoes();
        legenda.setCorUnidade(conf.getCorUnidade1());
        legenda.setCorUnidadeIntercalada(conf.getCorUnidade2());
        legenda.setCorUnidadeEmbutida(conf.getCorEmbutida());
        legenda.setCorSegmentoSobreposto(conf.getCorSobreposta());
        legenda.setCorUnidadeIndependente(conf.getCorIndep());
    }
    
    public void traduzirLegenda(boolean portugues)
    {
        legenda.translateDescr(portugues);
    }
}

