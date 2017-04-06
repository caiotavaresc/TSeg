package tseg.controle;

import tseg.configuracoes.Configuracoes;
import tseg.janelas.JanelaPrincipal;
import tseg.segmentacao.ControladorUnidades;

public class Controle {
	private static JanelaPrincipal janelaPrincipal;
	private static ControladorUnidades controladorUnidades;
	private static boolean modificado = false;

	public static void main(String[] args) {
		new Configuracoes().criaArquivosConfiguracao();
		controladorUnidades = new ControladorUnidades();
		janelaPrincipal = new JanelaPrincipal();
		janelaPrincipal.abreJanela();
	}

	public static JanelaPrincipal getJanelaPrincipal() {
		return janelaPrincipal;
	}

	public static ControladorUnidades getControladorUnidades() {
		return controladorUnidades;
	}

	public static void setModificado(boolean modificado) {
		Controle.modificado = modificado;
	}

	public static boolean isModificado() {
		return modificado;
	}

}
