package tseg.segmentacao;

import java.util.Stack;
import tseg.controle.Controle;

/* Classe responsável por gerenciar o conteúdo das ações, preenchendo, assim, as pilhas de desfazer e refazer */
public class GerenciadorAcoes {
    
    public static Stack<String> pilhaDesfazerTexto = new Stack<String>();
    public static Stack<ControladorUnidades> pilhaDesfazerContexto = new Stack<ControladorUnidades>();
    public static Stack<String> pilhaRefazerTexto = new Stack<String>();
    public static Stack<ControladorUnidades> pilhaRefazerContexto = new Stack<ControladorUnidades>();
    
    public static void guardarContexto()
    {
        ControladorUnidades contr = Controle.getControladorUnidades().clone();
        String textoAtual = String.valueOf(Controle.getJanelaPrincipal().getAbaSegmentaca().getTextoTxaSegmentacao());
        
        pilhaDesfazerContexto.add(contr);
        pilhaDesfazerTexto.add(textoAtual);
        
        Controle.getJanelaPrincipal().habilitarDesfazer(true);
        
        Controle.getJanelaPrincipal().habilitarRefazer(false);
        pilhaRefazerContexto.clear();
        pilhaRefazerTexto.clear();
    }
    
    public static void desfazer()
    {
        ControladorUnidades contexto = pilhaDesfazerContexto.pop();
        String texto = pilhaDesfazerTexto.pop();
        
        //Adicionar o contexto atual na pilha de refazer
        pilhaRefazerContexto.add(Controle.getControladorUnidades().clone());
        pilhaRefazerTexto.add(Controle.getJanelaPrincipal().getAbaSegmentaca().getTextoTxaSegmentacao());
        
        //Atualizar o contexto e o texto
        Controle.setControladorUnidades(contexto);
        Controle.getJanelaPrincipal().getAbaSegmentaca().setTextoTxaSegmentacao(texto);
        
        if(pilhaDesfazerContexto.size() > 0)
            Controle.getJanelaPrincipal().habilitarDesfazer(true);
        else
            Controle.getJanelaPrincipal().habilitarDesfazer(false);
        
        //Habilitar o botão de refazer
        Controle.getJanelaPrincipal().habilitarRefazer(true);
    }
    
    public static void refazer()
    {
        ControladorUnidades contexto = pilhaRefazerContexto.pop();
        String texto = pilhaRefazerTexto.pop();
        
        //Adicionar contexto atual na pilha do desfazer
        pilhaDesfazerContexto.add(Controle.getControladorUnidades().clone());
        pilhaDesfazerTexto.add(Controle.getJanelaPrincipal().getAbaSegmentaca().getTextoTxaSegmentacao());
        
        //Atualizar o contexto e o texto
        Controle.setControladorUnidades(contexto);
        Controle.getJanelaPrincipal().getAbaSegmentaca().setTextoTxaSegmentacao(texto);
        
        if(pilhaRefazerContexto.size() > 0)
            Controle.getJanelaPrincipal().habilitarRefazer(true);
        else
            Controle.getJanelaPrincipal().habilitarRefazer(false);
        
        //Habilitar o botão de desfazer na janela principal
        Controle.getJanelaPrincipal().habilitarDesfazer(true);
    }
    
    public static void limparContexto()
    {
        pilhaDesfazerTexto.clear();
        pilhaRefazerTexto.clear();
        
        pilhaDesfazerContexto.clear();
        pilhaRefazerContexto.clear();
        
        Controle.getJanelaPrincipal().habilitarDesfazer(false);
        Controle.getJanelaPrincipal().habilitarRefazer(false);
    }
    
}
