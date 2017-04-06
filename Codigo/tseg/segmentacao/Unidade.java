package tseg.segmentacao;

public class Unidade implements Comparable<Unidade> {
	private String identificador;
	private int posicao;

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	@Override
	public int compareTo(Unidade o) {
		int idClasse = Integer.parseInt(this.identificador.substring(
				this.identificador.indexOf("\"") + 1,
				this.identificador.lastIndexOf("\"")));

		int idParametro = Integer.parseInt(o.getIdentificador().substring(
				o.getIdentificador().indexOf("\"") + 1,
				o.getIdentificador().lastIndexOf("\"")));

		return idClasse - idParametro;
	}

	public Unidade clone() {
		Unidade unidade = new Unidade();
		unidade.setIdentificador(identificador);
		unidade.setPosicao(posicao);

		return unidade;
	}
}