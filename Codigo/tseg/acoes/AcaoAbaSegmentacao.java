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
	public ActionListener btnSegmentacaoAutomaticaActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				JMenuItem menuItemPalavras = montaMenuItemPalavras();
				JMenuItem menuItemSentencas = montaMenuItemSentencas();
				JMenuItem menuItemParagrafos = montaMenuItemParagrafos();

				JPopupMenu popupSegmentacaoAutomatica = new JPopupMenu();
				popupSegmentacaoAutomatica.add(menuItemPalavras);
				popupSegmentacaoAutomatica.add(menuItemSentencas);
				popupSegmentacaoAutomatica.add(menuItemParagrafos);

				int popupW = ((JButton) evt.getSource()).getWidth() / 2;
				int popupH = ((JButton) evt.getSource()).getHeight() / 2;

				popupSegmentacaoAutomatica.show((Component) evt.getSource(),
						popupW, popupH);
			}

			private JMenuItem montaMenuItemPalavras() {
				JMenuItem menuItemPalavras = new JMenuItem();
				if (tseg.janelas.JanelaPrincipal.portugues)
				{
					menuItemPalavras.setText("Palavras");
					menuItemPalavras.setMnemonic('a');
				}
				else
				{
					menuItemPalavras.setText("Words");
					menuItemPalavras.setMnemonic('W');
				}
				menuItemPalavras.addActionListener(new ActionListener() {

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
				});

				return menuItemPalavras;
			}

			private JMenuItem montaMenuItemSentencas() {
				JMenuItem menuItemSentencas = new JMenuItem();
				if (tseg.janelas.JanelaPrincipal.portugues)
				{
					menuItemSentencas.setText("Sentenças");
				}
				else
				{
					menuItemSentencas.setText("Sentences");
				}
				menuItemSentencas.setMnemonic('s');
				menuItemSentencas.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						SegmentadorAutomatico segmentador = new SegmentadorAutomatico();

						String texto = Controle.getJanelaPrincipal()
								.getAbaSegmentaca().getTextoTxaSegmentacao();

						texto = segmentador.segmenta(texto,
								SegmentadorAutomatico.SENTENCAS);

						Controle.getJanelaPrincipal().getAbaSegmentaca()
								.setTextoTxaSegmentacao(texto);

					}
				});

				return menuItemSentencas;
			}

			private JMenuItem montaMenuItemParagrafos() {
				JMenuItem menuItemParagrafos = new JMenuItem();
				if (tseg.janelas.JanelaPrincipal.portugues)
				{
					menuItemParagrafos.setText("Parágrafos");
				}
				else
				{
					menuItemParagrafos.setText("Paragraphs");
				}
				menuItemParagrafos.setMnemonic('p');
				menuItemParagrafos.addActionListener(new ActionListener() {

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
				});

				return menuItemParagrafos;
			}
		};
	}

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

			private boolean verificaForaTagDocument(int selecaoInicio,
					int selecaoFim) {
				String texto = Controle.getJanelaPrincipal().getAbaSegmentaca()
						.getTextoTxaSegmentacao();

				ControladorUnidades controladorUnidades = Controle
						.getControladorUnidades();

				String tagDocumentInicio = controladorUnidades
						.getTagDocumentInicio();
				String tagDocumentFim = controladorUnidades.getTagDocumentFim();

				if ((selecaoInicio < tagDocumentInicio.length() + 1)
						|| (selecaoInicio > texto.length()
								- tagDocumentFim.length() - 1)) {
					return false;
				}

				if (selecaoFim > texto.length() - tagDocumentFim.length() - 1) {
					return false;
				}
				return true;
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
												identificador.indexOf("'") + 1,
												identificador.lastIndexOf("'")));

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
									msg = "ID invalid";
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
				ControladorUnidades controladorUnidades = Controle
						.getControladorUnidades();

				ArrayList<Unidade> unidades = controladorUnidades.getUnidades();

				for (Unidade unidade : unidades) {
					int fimUnidade = unidade.getPosicao()
							+ unidade.getIdentificador().length();

					if ((selecaoInicio > unidade.getPosicao())
							&& (selecaoFim < fimUnidade)) {
						return true;
					}
				}
				return false;
			}

			private JMenuItem montaMenuItemUnidade(
					final JTextPane txpSegmentacao) {
				JMenuItem menuItemUnidade = new JMenuItem();
				if (tseg.janelas.JanelaPrincipal.portugues)
				{
					menuItemUnidade.setText("Unidade");
				}
				else
				{
					menuItemUnidade.setText("Unit");
				}
				menuItemUnidade.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						SegmentadorManual segmentador = new SegmentadorManual();

						String texto = Controle.getJanelaPrincipal()
								.getAbaSegmentaca().getTextoTxaSegmentacao();

						texto = segmentador.segmenta(txpSegmentacao, false);

						Controle.getJanelaPrincipal().getAbaSegmentaca()
								.setTextoTxaSegmentacao(texto);

					}
				});

				return menuItemUnidade;
			}

			private JMenuItem montaMenuItemUnidadeIndependente(
					final JTextPane txpSegmentacao) {
				JMenuItem itemUnidadeIndependente;
				if (tseg.janelas.JanelaPrincipal.portugues)
				{
					itemUnidadeIndependente = new JMenuItem("Unidade Independente");
				}
				else
				{
					itemUnidadeIndependente = new JMenuItem("Independent Units");
				}
				itemUnidadeIndependente.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent evt) {
						SegmentadorManual segmentador = new SegmentadorManual();

						String texto = Controle.getJanelaPrincipal()
								.getAbaSegmentaca().getTextoTxaSegmentacao();

						texto = segmentador.segmenta(txpSegmentacao, true);

						Controle.getJanelaPrincipal().getAbaSegmentaca()
								.setTextoTxaSegmentacao(texto);
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
					boolean existe = false;
					for (Unidade unidade : unidades) {
						if (unidadeList.getIdentificador().equals(
								unidade.getIdentificador())) {
							existe = true;
						}
					}

					if (!(existe)) {
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
							SegmentadorManual segmentador = new SegmentadorManual();
							String texto = Controle.getJanelaPrincipal()
									.getAbaSegmentaca()
									.getTextoTxaSegmentacao();

							texto = segmentador.removeUnidade(texto,
									identificador);

							Controle.getJanelaPrincipal().getAbaSegmentaca()
									.setTextoTxaSegmentacao(texto);
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
							"Remover todas as Unidades");
				}
				else
				{
					itemRemoverTodasUnidades = new JMenuItem(
							"Remove all Units");
				}
				itemRemoverTodasUnidades
						.addActionListener(new ActionListener() {

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
						});

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