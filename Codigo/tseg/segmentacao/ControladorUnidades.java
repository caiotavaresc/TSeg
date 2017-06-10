package tseg.segmentacao;

import java.util.ArrayList;
import java.util.Collections;

import tseg.controle.Controle;

public class ControladorUnidades{
	private ArrayList<Unidade> unidadesInicio = new ArrayList<Unidade>();
	private ArrayList<Unidade> unidadesFim = new ArrayList<Unidade>();
	private ArrayList<Unidade> unidadesRemovidas = new ArrayList<Unidade>();
	private String identificadorSobreposicao = "IND ";
	private String tagDocumentInicio = "<document>"
			+ "\n <info type=\"id\" value=\"\">"
			+ "\n <info type=\"scheme\" value=\"\">"
			+ "\n <info type=\"source\" value=\"\">"
			+ "\n <info type=\"source-corpus\" value=\"\">"
			+ "\n <info type=\"annotator\" value=\"\">";
	private String tagDocumentFim = "</document>";

	public ArrayList<Unidade> criaUnidade(int selecaoInicio, int selecaoFim,
			boolean independente) {
		ArrayList<Unidade> inicioFim = new ArrayList<Unidade>();

		String unidade, identificadorGeral;
		if (independente) {
			unidade = criaUnidadeIndependente();
		} else {
			unidade = criaUnidade();
		}
                identificadorGeral = unidade;
                
		Unidade novaUnidadeInicio = new Unidade();
		novaUnidadeInicio.setIdentificador(unidade);
                novaUnidadeInicio.setIdentificadorGeral(identificadorGeral);
		novaUnidadeInicio.setPosicao(selecaoInicio);

		unidade = "</" + unidade.substring(1);
		Unidade novaUnidadeFim = new Unidade();
		novaUnidadeFim.setIdentificador(unidade);
                novaUnidadeFim.setIdentificadorGeral(identificadorGeral);
		novaUnidadeFim.setPosicao(selecaoFim);

		unidadesInicio.add(novaUnidadeInicio);
		unidadesFim.add(unidadesInicio.size() - 1, novaUnidadeFim);
		inicioFim.add(novaUnidadeInicio);

		inicioFim.add(novaUnidadeFim);

		atualizaPosicao(novaUnidadeInicio, false);
		atualizaPosicao(novaUnidadeFim, false);

		return inicioFim;
	}

	private String criaUnidade() {
		if (!(unidadesRemovidas.isEmpty())) {
			return unidadesRemovidas.remove(0).getIdentificador();
		} else {
			return "<unit id=\"" + geraIdUnidade() + "\">";
		}
	}

	private String criaUnidadeIndependente() {
		if (!(unidadesRemovidas.isEmpty())) {
			StringBuilder identificador = new StringBuilder(unidadesRemovidas
					.remove(0).getIdentificador());

			identificador.insert(identificador.indexOf("id"),
					identificadorSobreposicao);

			return identificador.toString();
		} else {
			return "<unit " + identificadorSobreposicao + "id=\""
					+ geraIdUnidade() + "\">";
		}
	}

	private int geraIdUnidade() {
		if (unidadesInicio.isEmpty()) {
			return 0;
		}

		Unidade unidade = unidadesInicio.get(unidadesInicio.size() - 1);

		String identificador = unidade.getIdentificador();
                
		int id = Integer
				.parseInt(identificador.substring(
						identificador.indexOf("\"") + 1,
						identificador.lastIndexOf("\"")));

		id++;

		return id;
	}

	private void atualizaPosicao(Unidade unidade, boolean remove) {
		if (!(remove)) {
			atualizaPosicao(unidade);
		} else {
			atualizaPosicaoAoDeletar(unidade);
		}
	}

	private void atualizaPosicao(Unidade unidade) {
		for (int i = 0; i < unidadesInicio.size(); i++) {
			if (!(unidadesInicio.get(i).getIdentificador().equals(unidade
					.getIdentificador()))) {
				if (unidade.getPosicao() <= unidadesInicio.get(i).getPosicao()) {
					unidadesInicio.get(i).setPosicao(
							unidadesInicio.get(i).getPosicao()
									+ unidade.getIdentificador().length());
				}
			}
		}
		for (int i = 0; i < unidadesFim.size(); i++) {
			if (!(unidadesFim.get(i).getIdentificador().equals(unidade
					.getIdentificador()))) {
				if (unidade.getPosicao() <= unidadesFim.get(i).getPosicao()) {
					unidadesFim.get(i).setPosicao(
							unidadesFim.get(i).getPosicao()
									+ unidade.getIdentificador().length());
				}
			}
		}
	}

	private void atualizaPosicaoAoDeletar(Unidade unidade) {
		for (int i = 0; i < unidadesInicio.size(); i++) {
			if (!(unidadesInicio.get(i).getIdentificador().equals(unidade
					.getIdentificador()))) {
				if (unidadesInicio.get(i).getPosicao() >= unidade.getPosicao()) {
					unidadesInicio.get(i).setPosicao(
							unidadesInicio.get(i).getPosicao()
									- unidade.getIdentificador().length());
				}
			}
		}
		for (int i = 0; i < unidadesFim.size(); i++) {
			if (!(unidadesFim.get(i).getIdentificador().equals(unidade
					.getIdentificador()))) {
				if (unidadesFim.get(i).getPosicao() >= unidade.getPosicao()) {
					unidadesFim.get(i).setPosicao(
							unidadesFim.get(i).getPosicao()
									- unidade.getIdentificador().length());
				}
			}
		}
	}

	public void removeUnidade(String identificador) {
		for (int i = 0; i < unidadesInicio.size(); i++) {
			if (identificador.equals(unidadesInicio.get(i).getIdentificador())) {

				Unidade unidadeDeletada = new Unidade();
				unidadeDeletada.setIdentificador(unidadesInicio.get(i)
						.getIdentificador());
				unidadeDeletada.setPosicao(unidadesInicio.get(i).getPosicao());

				if (unidadesInicio.get(i).getIdentificador()
						.indexOf(identificadorSobreposicao) > -1) {
					String idIND = unidadesInicio.get(i).getIdentificador();

					idIND = idIND.replace(identificadorSobreposicao, "");
					unidadeDeletada.setIdentificador(idIND);
				}

				boolean existe = false;
				for (Unidade unidade : unidadesRemovidas) {
					if (unidade.getIdentificador().equals(identificador)) {
						existe = true;
						break;
					}
				}

				if (!(existe)) {
					unidadesRemovidas.add(unidadeDeletada);
				}

				atualizaPosicao(unidadesInicio.remove(i), true);

				Unidade unidade = unidadesFim.remove(i);
				if (unidade.getPosicao() > 0) {
					atualizaPosicao(unidade, true);
				}
				i--;
			}

		}
		Collections.sort(unidadesInicio);
		Collections.sort(unidadesFim);
		Collections.sort(unidadesRemovidas);
	}

	public int totalUnidades() {
		int unidades = 0;
		for (int i = 0; i < unidadesFim.size(); i++) {
			if (unidadesFim.get(i).getPosicao() != -1) {
				unidades++;
			}
		}
		return unidades;
	}

	public void recuperaUnidades(String texto) {
		unidadesInicio.clear();
		unidadesFim.clear();
		unidadesRemovidas.clear();

		for (int i = 0; i < texto.length();) {
			int inicioUnidade = texto.indexOf("<unit", i);
			int fimUnidade = texto.indexOf('>', inicioUnidade) + 1;

			if (inicioUnidade > -1) {
				Unidade unidade = new Unidade();

				unidade.setIdentificador(texto.substring(inicioUnidade,
						fimUnidade));
				unidade.setPosicao(inicioUnidade);
                                unidade.setIdentificadorGeral(texto.substring(inicioUnidade,
						fimUnidade));
				unidadesInicio.add(unidade);

				i = fimUnidade;
			} else {
				break;
			}
		}

		for (int i = 0; i < texto.length();) {
			int inicioUnidade = texto.indexOf("</unit", i);
			int fimUnidade = texto.indexOf('>', inicioUnidade) + 1;

			if (inicioUnidade > -1) {
				Unidade unidade = new Unidade();

				unidade.setIdentificador(texto.substring(inicioUnidade,
						fimUnidade));
				unidade.setPosicao(inicioUnidade);
                                
                                //Montar o identificador geral
                                String identificadorGeral = "<" + unidade.getIdentificador().substring(2);
                                unidade.setIdentificadorGeral(identificadorGeral);
                                
				unidadesFim.add(unidade);

				i = fimUnidade;
			} else {
				break;
			}
		}
		Collections.sort(unidadesInicio);
		Collections.sort(unidadesFim);

		if (unidadesInicio.size() > 0) {
			ArrayList<Unidade> listDeletadas = new ArrayList<Unidade>();

			Unidade unidade = unidadesInicio.get(unidadesInicio.size() - 1);
			String identificador = unidade.getIdentificador();
			int ultimoID = Integer.parseInt(identificador.substring(
					identificador.indexOf("\"") + 1,
					identificador.lastIndexOf("\"")));

			for (int i = 0; i < ultimoID; i++) {
				Unidade unidade2 = new Unidade();
				unidade2.setIdentificador("<unit id=\"" + i + "\">");
                                unidade2.setIdentificadorGeral("<unit id=\"" + i + "\">");
				listDeletadas.add(unidade2);
			}

			for (int i = 0; i < listDeletadas.size(); i++) {
				boolean existe = false;
				for (int j = 0; j < unidadesInicio.size(); j++) {
					if (listDeletadas.get(i).compareTo(unidadesInicio.get(j)) == 0) {
						existe = true;
						break;
					}
				}

				if (!(existe)) {
					unidadesRemovidas.add(listDeletadas.get(i));
				}
			}
			Collections.sort(unidadesRemovidas);
		}
	}

	public void editarID(int selecaoInicio, int selecaoFim, int id) {
		ArrayList<Unidade> unidades = getUnidades();
		int index = 0;
		for (Unidade unidade : unidades) {
			int fimUnidade = unidade.getPosicao()
					+ unidade.getIdentificador().length();

			if ((selecaoInicio > unidade.getPosicao())
					&& (selecaoFim < fimUnidade)) {
				if (unidadesInicio.indexOf(unidade) > -1) {
					index = unidadesInicio.indexOf(unidade);
				} else {
					index = unidadesFim.indexOf(unidade);
				}
				break;
			}
		}

		Unidade unidadeAntigaInicio = unidadesInicio.get(index).clone();
		Unidade unidadeAntigaFim = unidadesFim.get(index).clone();

		atualizaID(id, index);

		StringBuilder textoBuilder = new StringBuilder(Controle
				.getJanelaPrincipal().getAbaSegmentaca()
				.getTextoTxaSegmentacao());

		int inicioUnidadeAntiga = textoBuilder.indexOf(unidadeAntigaInicio
				.getIdentificador());

		int fimUnidadeAntiga = inicioUnidadeAntiga
				+ unidadeAntigaInicio.getIdentificador().length();

		String texto = textoBuilder.substring(0, inicioUnidadeAntiga)
				+ unidadesInicio.get(index).getIdentificador()
				+ textoBuilder.substring(fimUnidadeAntiga,
						textoBuilder.length());

		textoBuilder = new StringBuilder(texto);

		inicioUnidadeAntiga = textoBuilder.indexOf(unidadeAntigaFim
				.getIdentificador());

		fimUnidadeAntiga = inicioUnidadeAntiga
				+ unidadeAntigaFim.getIdentificador().length();

		texto = textoBuilder.substring(0, inicioUnidadeAntiga)
				+ unidadesFim.get(index).getIdentificador()
				+ textoBuilder.substring(fimUnidadeAntiga,
						textoBuilder.length());

		Controle.getJanelaPrincipal().getAbaSegmentaca()
				.setTextoTxaSegmentacao(texto);

		unidadesRemovidas.add(unidadeAntigaInicio);

		for (int i = 0; i < unidadesRemovidas.size(); i++) {
			if (unidadesInicio.get(index).getIdentificador()
					.equals(unidadesRemovidas.get(i).getIdentificador())) {
				unidadesRemovidas.remove(i);
				break;
			}
		}

		Collections.sort(unidadesInicio);
		Collections.sort(unidadesFim);
		Collections.sort(unidadesRemovidas);
	}

	private void atualizaID(int id, int index) {
		Unidade unidade = unidadesInicio.get(index);
		String identificador = unidade.getIdentificador();

		int lengthAnterior = identificador.length();

		int iniID = identificador.indexOf("\"");
		int fimID = identificador.lastIndexOf("\"");
		identificador = identificador.substring(0, iniID + 1) + id
				+ identificador.substring(fimID);

		unidade.setIdentificador(identificador);

		int lengthAtual = identificador.length();

		if (lengthAtual > lengthAnterior || lengthAtual < lengthAnterior) {
			for (int i = 0; i < unidadesInicio.size(); i++) {
				Unidade uni = unidadesInicio.get(i);
				if (uni.getPosicao() > unidade.getPosicao()) {
					uni.setPosicao(uni.getPosicao()
							+ (lengthAtual - lengthAnterior));
				}
			}

			for (int i = 0; i < unidadesFim.size(); i++) {
				Unidade uni = unidadesFim.get(i);
				if (uni.getPosicao() > unidade.getPosicao()) {
					uni.setPosicao(uni.getPosicao()
							+ (lengthAtual - lengthAnterior));
				}
			}
		}

		unidade = unidadesFim.get(index);
		identificador = unidade.getIdentificador();

		lengthAnterior = identificador.length();

		iniID = identificador.indexOf("\"");
		fimID = identificador.lastIndexOf("\"");
		identificador = identificador.substring(0, iniID + 1) + id
				+ identificador.substring(fimID);

		unidade.setIdentificador(identificador);

		lengthAtual = identificador.length();

		if (lengthAtual > lengthAnterior || lengthAtual < lengthAnterior) {
			for (int i = 0; i < unidadesFim.size(); i++) {
				Unidade uni = unidadesFim.get(i);
				if (uni.getPosicao() > unidade.getPosicao()) {
					uni.setPosicao(uni.getPosicao()
							+ (lengthAtual - lengthAnterior));
				}
			}

			for (int i = 0; i < unidadesInicio.size(); i++) {
				Unidade uni = unidadesInicio.get(i);
				if (uni.getPosicao() > unidade.getPosicao()) {
					uni.setPosicao(uni.getPosicao()
							+ (lengthAtual - lengthAnterior));
				}
			}
		}
	}

	public ArrayList<Unidade> getUnidade(String identificadorUnidadeInicio) {
		int i = 0;
		while (!(identificadorUnidadeInicio.equals(unidadesInicio.get(i)
				.getIdentificador()))) {
			i++;
		}

		ArrayList<Unidade> unidades = new ArrayList<Unidade>();
		unidades.add(unidadesInicio.get(i));
		unidades.add(unidadesFim.get(i));

		return unidades;
	}

	public ArrayList<Unidade> getUnidadesInicio() {
		Collections.sort(unidadesInicio);
		return unidadesInicio;
	}

	public ArrayList<Unidade> getUnidadesFim() {
		Collections.sort(unidadesFim);
		return unidadesFim;
	}

	public ArrayList<Unidade> getUnidades() {
		ArrayList<Unidade> list = new ArrayList<Unidade>();

		for (Unidade unidade : unidadesInicio) {
			list.add(unidade);
		}

		for (Unidade unidade : unidadesFim) {
			list.add(unidade);
		}
		return list;
	}

	public String getTagDocumentInicio() {
		return tagDocumentInicio;
	}

	public String getTagDocumentFim() {
		return tagDocumentFim;
	}

	public String getIdentificadorSobreposicao() {
		return identificadorSobreposicao;
	}
        
        public static boolean unidadeEstaContida(int selecaoInicio, int selecaoFim, Unidade unidadeInicio)
        {            
            //Achar a unidade "fim" correspondente
            int i, inicioUnid, fimUnid;
            ArrayList<Unidade> myUnidadesFim;
            Unidade unidadeFim = null;
            
            myUnidadesFim = Controle.getControladorUnidades().unidadesFim;
            
            for(i=0;i<myUnidadesFim.size();i++)
                if(unidadeInicio.getIdentificadorGeral().equals(myUnidadesFim.get(i).getIdentificadorGeral()))
                {
                    unidadeFim = myUnidadesFim.get(i);
                    break;
                }
            
            if(unidadeFim!=null)
            {
                inicioUnid = unidadeInicio.getPosicao();
                fimUnid = unidadeFim.getPosicao() + unidadeFim.getIdentificador().length();

                if(selecaoInicio > inicioUnid && selecaoInicio < fimUnid)
                    return true;

                if(selecaoFim > inicioUnid && selecaoFim < fimUnid)
                    return true;
                
                if(inicioUnid >= selecaoInicio && inicioUnid <= selecaoFim)
                    return true;
                
                if(fimUnid >= selecaoInicio && fimUnid <= selecaoFim)
                    return true;
            }
            
            return false;
        }
        
        public static boolean isUnidade(int selecaoInicio, int selecaoFim){
            ArrayList<Unidade> unidades = Controle.getControladorUnidades().getUnidades();

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
        
    public ControladorUnidades clone()
    {
        ControladorUnidades clone = new ControladorUnidades();
        clone.unidadesInicio = (ArrayList<Unidade>) this.cloneArrayList(unidadesInicio);
        clone.unidadesFim = (ArrayList<Unidade>) this.cloneArrayList(unidadesFim);
        clone.unidadesRemovidas = (ArrayList<Unidade>) this.cloneArrayList(unidadesRemovidas);
        clone.identificadorSobreposicao = String.valueOf(this.identificadorSobreposicao);
        clone.tagDocumentInicio = String.valueOf(this.tagDocumentInicio);
        clone.tagDocumentFim = String.valueOf(this.tagDocumentFim);
        
        return clone;
    }
    
    private ArrayList<Unidade> cloneArrayList(ArrayList<Unidade> entrada)
    {
        ArrayList<Unidade> retorno = new ArrayList<Unidade>();
        
        int i;
        
        for(i=0;i<entrada.size();i++)
            retorno.add(entrada.get(i).clone());
        
        return retorno;
    }

}
