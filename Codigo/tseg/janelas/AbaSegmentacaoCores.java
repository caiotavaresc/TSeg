package tseg.janelas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;  
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import tseg.acoes.AcaoAbaEstatisticas;
import tseg.configuracoes.Configuracoes;
import tseg.controle.Controle;
import tseg.segmentacao.Estatistico;
import tseg.segmentacao.Unidade;

public class AbaSegmentacaoCores {
	private JPanel abaSegCores;
	private JScrollPane spnlSegCores;
	private JPanel pnlBotoes;
	public JTextPane txSegCores;
	private static JButton btnSeg;
	private static AbaSegmentacao abaSegmentacao = new AbaSegmentacao();
	String texto;
	
	
	public AbaSegmentacaoCores() {
		montaPainelSegCores();
		montaPainelBotoes();
		
		abaSegCores = new JPanel();
		abaSegCores.setLayout(new BorderLayout());
		abaSegCores.add(spnlSegCores, BorderLayout.CENTER);
		abaSegCores.add(pnlBotoes, BorderLayout.NORTH);
		txSegCores.setEditable(false);
	}
	
	TreeMap<String, Integer> hmUnit = new TreeMap<>();
	TreeMap<Integer, Integer> hmUnitFinal = new TreeMap<>();
	
	TreeMap<String, Integer> hmIndep = new TreeMap<>();
	TreeMap<Integer, Integer> hmIndepFinal = new TreeMap<>();
	
	
	public ActionListener btnSegActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
						
				texto = Controle.getJanelaPrincipal()
							.getAbaSegmentaca().getTextoTxaSegmentacao();
					
				int retira = 171;
					
				texto = texto.substring(retira);
					
				for(int i=0;i<5;i++)
				{
					if(texto.charAt(i)=='>')
					{
						texto = texto.substring(171);
						retira = retira + i;
					}
				}
					
				
				//txSegCores.setText(texto);
				txSegCores.setEditable(false);
					
				resetarFormatacao();
					
				MutableAttributeSet attributes = txSegCores.getInputAttributes();
	
				Configuracoes configuracoes = new Configuracoes();
	
				StyleConstants.setBackground(attributes, configuracoes.getCorUnidade1());
	
				StyledDocument doc = txSegCores.getStyledDocument();
				
				char[] inicioUnit = {'<'};//,'U','N','I','T',' '}; 
				char[] fimUnit = {'/'};//,'U','N','I','T',' '};//{'<'
				texto = texto.replace("</document>","");
				txSegCores.setText(texto);
				
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
					StyleConstants.setBackground(attributes, Color.blue);
		            doc.setCharacterAttributes(posIni , (posFim-posIni), attributes, false);
		            i++;
		        }  
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
			String txt = "<UNIT id=\""+id+"\">";
			texto = texto.replace(txt, "");
			hmUnit.put(txt, (pos-1));
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
			String txt = "<UNIT IND id=\""+id+"\">";
			texto = texto.replace(txt, "");
			hmIndep.put(txt, (pos-1));
		}
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
			String txt = "</UNIT id=\""+id+"\">";
			texto = texto.replace(txt, "");
			hmUnit.put(txt, (pos-1));
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
			String txt = "</UNIT IND id=\""+id+"\">";
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

		doc.setCharacterAttributes(0, txSegCores.getText().length(),
				attributes, false);
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
	
}

