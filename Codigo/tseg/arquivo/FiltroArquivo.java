package tseg.arquivo;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FiltroArquivo {
	FiltroExtencoes filtroExtencoes = new FiltroExtencoes();

	public FileFilter criaFiltro(final int filtro) {
		return new FileFilter() {

			@Override
			public String getDescription() {
				String extencoes = filtroExtencoes.getDescricao(filtro) + " (.";
				extencoes = extencoes + filtroExtencoes.getExtencao(filtro);
				return extencoes + ")";
			}

			@Override
			public boolean accept(File CaminhoDoArquivo) {

				if (CaminhoDoArquivo.isDirectory()) {
					return true;
				}

				String extencao = filtroExtencoes
						.retornaExtencao(CaminhoDoArquivo);

				if (extencao != null) {
					if (filtroExtencoes.getExtencao(filtro).equals(extencao)) {
						return true;
					}
				}
				return false;
			}
		};
	}
        
        public boolean acceptNoDir(File CaminhoDoArquivo){
            if (CaminhoDoArquivo.isDirectory()) {
                    return false;
            }

            String extencao = filtroExtencoes
                            .retornaExtencao(CaminhoDoArquivo);

            if (extencao != null) {
                    if (filtroExtencoes.getExtencao(0).equals(extencao)) {
                            return true;
                    }
            }
            return false;
        }
}
