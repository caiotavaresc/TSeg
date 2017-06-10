package tseg.acoes;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;

import tseg.controle.Controle;
import tseg.segmentacao.ControladorUnidades;
import tseg.segmentacao.SegmentadorAutomatico;
import tseg.segmentacao.SegmentadorManual;
import tseg.segmentacao.SegmentadorUtil;
import tseg.segmentacao.Unidade;

public class AcaoAbaSegmentacao {
    
        private AcaoJanelaPrincipal acao = new AcaoJanelaPrincipal();
    
	public MouseAdapter txpSegmentacaoMouseAdapter() {
		return new MouseAdapter() {
			private int countErro = 0;

			@Override
			public void mouseReleased(MouseEvent evt) {
				SegmentadorUtil sUtil = new SegmentadorUtil();
				JTextPane txpSegmentacao = (JTextPane) evt.getSource();
				int selecaoInicio = txpSegmentacao.getSelectionStart();
				int selecaoFim = txpSegmentacao.getSelectionEnd();

				if (evt.getButton() == MouseEvent.BUTTON3) {
					if (sUtil.verificaForaDeTag(selecaoInicio, selecaoFim)
							&& verificaForaTagDocument(selecaoInicio,
									selecaoFim)) {
						countErro = 0;
						JMenuItem menuItemUnidade = montaMenuItemUnidade(txpSegmentacao);
						JMenuItem menuItemUnidadeIndependente = montaMenuItemUnidadeIndependente(txpSegmentacao);
						JMenu menuRemoverUnidade = montaMenuRemoverUnidade();
						JMenuItem itemRemoverTodasUnidades = montaMenuItemRemoverTodasUnidades();

						JPopupMenu popupSegmentacaoManual = new JPopupMenu();
						popupSegmentacaoManual.add(menuItemUnidade);
						popupSegmentacaoManual.add(menuItemUnidadeIndependente);
						popupSegmentacaoManual.add(menuRemoverUnidade);
						popupSegmentacaoManual.add(itemRemoverTodasUnidades);

						int popupX = evt.getX();
						int popupY = evt.getY();

						popupSegmentacaoManual.show(txpSegmentacao, popupX,
								popupY);
					} else {
						if (isUnidade(selecaoInicio, selecaoFim)) {
							countErro = 0;
							JMenuItem itemEditarID = montaItemEditarID(
									selecaoInicio, selecaoFim);
							JPopupMenu popupEditarID = new JPopupMenu();

							popupEditarID.add(itemEditarID);

							int popupX = evt.getX();
							int popupY = evt.getY();

							popupEditarID.show(txpSegmentacao, popupX, popupY);
						} else {
							countErro++;
							if (countErro == 3) {
								String message;
								if (tseg.janelas.JanelaPrincipal.portugues)
									message = "Você está tentando estabelecer uma unidade dentro da Tag de uma Unidade. Isso não é possível!";
								else
									message = "You're trying to establish a unit within the tag of a unit. This is not possible!";
								JOptionPane.showMessageDialog(null, message,
										"Ops!", JOptionPane.WARNING_MESSAGE);
								countErro = 0;
							}
						}
					}
				}
			}

			private boolean verificaForaTagDocument(int selecaoInicio, int selecaoFim) {
                            return SegmentadorUtil.verificaForaTagDocument(selecaoInicio, selecaoFim);
			}

			private JMenuItem montaItemEditarID(final int selecaoInicio,
					final int selecaoFim) {
				JMenuItem itemEditarID;
				if (tseg.janelas.JanelaPrincipal.portugues)
					itemEditarID = new JMenuItem("Editar ID");
				else
					itemEditarID = new JMenuItem("Edit ID");
				itemEditarID.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent evt) {
						ArrayList<Unidade> unidades = Controle
								.getControladorUnidades().getUnidades();

						String identificadorMensagem = "";

						for (Unidade unidade : unidades) {
							int fimUnidade = unidade.getPosicao()
									+ unidade.getIdentificador().length();

							if ((selecaoInicio > unidade.getPosicao())
									&& (selecaoFim < fimUnidade)) {
								identificadorMensagem = unidade
										.getIdentificador();
							}
						}

						String message,title;
						if (tseg.janelas.JanelaPrincipal.portugues)
						{
							message = "Digite o novo ID para a unidade: "
								+ identificadorMensagem;
							title = "Editar ID";
						}
						else
						{
							message = "Enter the new ID for the unit: "
									+ identificadorMensagem;
							title = "Edit ID";
						}
						String inputDialog = JOptionPane.showInputDialog(null,
								message, title, JOptionPane.PLAIN_MESSAGE);

						if (inputDialog != null) {
							try {
								int id = Integer.parseInt(inputDialog);

								ArrayList<Unidade> unidadesInicio = Controle
										.getControladorUnidades()
										.getUnidadesInicio();

								Unidade unidade = unidadesInicio
										.get(unidadesInicio.size() - 1);

								String identificador = unidade
										.getIdentificador();
								int numeroID = Integer.parseInt(identificador
										.substring(
												identificador.indexOf("\"") + 1,
												identificador.lastIndexOf("\"")));

								if (id <= numeroID) {
									Controle.setModificado(true);
									Controle.getControladorUnidades().editarID(
											selecaoInicio, selecaoFim, id);
								} else {
									String msg, titulo;
									if (tseg.janelas.JanelaPrincipal.portugues)
									{
										msg = "ID inválido";
										titulo = "Erro";
									}
									else
									{
										msg = "ID invalid";
										titulo = "Error";
									}
									JOptionPane.showMessageDialog(null,
											msg, titulo,
											JOptionPane.ERROR_MESSAGE);
								}
							} catch (NumberFormatException e) {
								String msg, titulo;
								if (tseg.janelas.JanelaPrincipal.portugues)
								{
									msg = "ID inválido";
									titulo = "Erro";
								}
								else
								{
									msg = "Invalid ID";
									titulo = "Error";
								}
								JOptionPane.showMessageDialog(null,
										msg, titulo,
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				});
				return itemEditarID;
			}

			private boolean isUnidade(int selecaoInicio, int selecaoFim) {
				return ControladorUnidades.isUnidade(selecaoInicio, selecaoFim);
			}

			private JMenuItem montaMenuItemUnidade(
					final JTextPane txpSegmentacao) {
				JMenuItem menuItemUnidade = new JMenuItem();
				if (tseg.janelas.JanelaPrincipal.portugues)
				{
					menuItemUnidade.setText("Definir nova unidade");
				}
				else
				{
					menuItemUnidade.setText("Define new unit");
				}
				menuItemUnidade.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						SegmentadorManual segmentador = new SegmentadorManual();

						String texto = Controle.getJanelaPrincipal()
								.getAbaSegmentaca().getTextoTxaSegmentacao();
                                                int inicioSelecao = Controle.getJanelaPrincipal().getAbaSegmentaca().getTxpSegmentacao().getSelectionStart();

						texto = segmentador.segmenta(txpSegmentacao, false);

						Controle.getJanelaPrincipal().getAbaSegmentaca()
								.setTextoTxaSegmentacao(texto);
                                                
                                                Controle.getJanelaPrincipal().getAbaSegmentaca().getTxpSegmentacao().setCaretPosition(inicioSelecao);

					}
				});

				return menuItemUnidade;
			}

			private JMenuItem montaMenuItemUnidadeIndependente(
					final JTextPane txpSegmentacao) {
				JMenuItem itemUnidadeIndependente;
				if (tseg.janelas.JanelaPrincipal.portugues)
				{
					itemUnidadeIndependente = new JMenuItem("Definir nova unidade independente");
				}
				else
				{
					itemUnidadeIndependente = new JMenuItem("Define new independent unit");
				}
				itemUnidadeIndependente.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent evt) {
						SegmentadorManual segmentador = new SegmentadorManual();
                                                int inicioSelecao = Controle.getJanelaPrincipal().getAbaSegmentaca().getTxpSegmentacao().getSelectionStart();

						String texto = Controle.getJanelaPrincipal()
								.getAbaSegmentaca().getTextoTxaSegmentacao();

						texto = segmentador.segmenta(txpSegmentacao, true);

						Controle.getJanelaPrincipal().getAbaSegmentaca()
								.setTextoTxaSegmentacao(texto);
                                                
                                                Controle.getJanelaPrincipal().getAbaSegmentaca().getTxpSegmentacao().setCaretPosition(inicioSelecao);
					}
				});
				return itemUnidadeIndependente;
			}

			private JMenu montaMenuRemoverUnidade() {
				JMenu menuRemoverUnidade = new JMenu();
				if (tseg.janelas.JanelaPrincipal.portugues)
				{
					menuRemoverUnidade.setText("Remover Unidade...");
				}
				else
				{
					menuRemoverUnidade.setText("Remove Unit...");
				}
				ArrayList<Unidade> list = Controle.getControladorUnidades()
						.getUnidadesInicio();

				ArrayList<Unidade> unidades = new ArrayList<Unidade>();

				for (Unidade unidadeList : list) {
                                    
                                        JTextPane txpSegmentacao = Controle.getJanelaPrincipal().getAbaSegmentaca().getTxpSegmentacao();
                                        int selecaoInicio = txpSegmentacao.getSelectionStart();
                                        int selecaoFim = txpSegmentacao.getSelectionEnd();
                                    
                                        //Antes de tentar inserir, fazer uma validação se a unidade se aplica à área clicada
                                        boolean valido = ControladorUnidades.unidadeEstaContida(selecaoInicio, selecaoFim, unidadeList);
                                    
					boolean existe = false;
					for (Unidade unidade : unidades) {
						if (unidadeList.getIdentificador().equals(
								unidade.getIdentificador())) {
							existe = true;
						}
					}

					if (!(existe) && valido) {
						unidades.add(unidadeList);
					}
				}

				if (unidades != null) {
					for (int i = 0; i < unidades.size(); i++) {
						JMenuItem itemUnidade = montaMenuItemUnidadeRemover(unidades
								.get(i).getIdentificador());

						menuRemoverUnidade.add(itemUnidade);
					}
				}
                                
                                //Se não houver unidades a remover - desabilitar
                                if(unidades.size()==0)
                                    menuRemoverUnidade.setEnabled(false);

				return menuRemoverUnidade;
			}

			private JMenuItem montaMenuItemUnidadeRemover(
					final String identificador) {
				JMenuItem menuItemUnidaRemover = new JMenuItem();
				menuItemUnidaRemover.setText(identificador);
				menuItemUnidaRemover.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent evt) {
						String msg, title;
						if (tseg.janelas.JanelaPrincipal.portugues)
						{
							msg = "Deseja realmente remover a Unidade:";
							title = "Aviso";
						}
						else
						{
							msg = "Do you really want to remove the unit:";
							title = "Warning";
						}
						int confirmDialog = JOptionPane.showConfirmDialog(null,
								msg	+ identificador + " ?", title,
								JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);

						if (confirmDialog == JOptionPane.YES_OPTION) {
                                                        int inicioSelecao = Controle.getJanelaPrincipal().getAbaSegmentaca().getTxpSegmentacao().getSelectionStart();
                                                       
							SegmentadorManual segmentador = new SegmentadorManual();
							String texto = Controle.getJanelaPrincipal()
									.getAbaSegmentaca()
									.getTextoTxaSegmentacao();

							texto = segmentador.removeUnidade(texto,
									identificador, false);

							Controle.getJanelaPrincipal().getAbaSegmentaca()
									.setTextoTxaSegmentacao(texto);
                                                        
                                                        Controle.getJanelaPrincipal().getAbaSegmentaca().getTxpSegmentacao().setCaretPosition(inicioSelecao);
						}
					}
				});

				return menuItemUnidaRemover;
			}

			private JMenuItem montaMenuItemRemoverTodasUnidades() {
				JMenuItem itemRemoverTodasUnidades;
				if (tseg.janelas.JanelaPrincipal.portugues)
				{	
					itemRemoverTodasUnidades = new JMenuItem(
							"Remover todas as unidades");
				}
				else
				{
					itemRemoverTodasUnidades = new JMenuItem(
							"Remove all units");
				}
				itemRemoverTodasUnidades
						.addActionListener(acao.itemRemoverTodasUnidades());

				return itemRemoverTodasUnidades;
			}
		};
	}

	public KeyListener txpSegmentacaoKeyListener() {
		KeyListener keyListener = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent evt) {
				((JTextPane) evt.getSource()).setEditable(false);
			}

			@Override
			public void keyReleased(KeyEvent evt) {
				((JTextPane) evt.getSource()).setEditable(true);
			}

			@Override
			public void keyPressed(KeyEvent evt) {
				((JTextPane) evt.getSource()).setEditable(false);
			}
		};
		return keyListener;
	}

	// Problema do TAB
	public FocusAdapter txpSegmentacaoFocusAdapter() {
		FocusAdapter focusAdapter = new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent evt) {
				((JTextPane) evt.getSource()).setEditable(true);
			}
		};
		return focusAdapter;
	}

}