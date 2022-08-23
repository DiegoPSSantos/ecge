package br.com.ecge.ecgefoods.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.annimon.stream.Stream;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import br.com.ecge.ecgefoods.R;
import br.com.ecge.ecgefoods.domain.Categoria;
import br.com.ecge.ecgefoods.domain.Impressora;
import br.com.ecge.ecgefoods.domain.Observacao;
import br.com.ecge.ecgefoods.domain.Pessoa;
import br.com.ecge.ecgefoods.domain.Produto;
import br.com.ecge.ecgefoods.domain.Usuario;

public class ECGEFoodsUtils {

    public static Snackbar exibirMensagemComAcao(View layout, String mensagem, String acao, View.OnClickListener clickListener) {
        Snackbar sb = Snackbar.make(layout, mensagem, Snackbar.LENGTH_LONG);
        TextView tv = sb.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        sb.setAction(acao, clickListener);
        sb.setActionTextColor(Color.YELLOW);
        sb.show();
        return sb;
    }

    public static List<Categoria> ordenarCategorias(List<Categoria> categorias) {
        categorias = Stream.of(categorias).sorted((categoria1, categoria2) -> categoria1.getNome().compareTo(categoria2.getNome())).toList();
        return categorias;
    }

    public static List<Produto> ordenarProdutos(List<Produto> produtos) {
        produtos = Stream.of(produtos).sorted((produto1, produto2) -> produto1.getNome().compareTo(produto2.getNome())).toList();
        return produtos;
    }

    public static List<Observacao> ordenarObservacoes(List<Observacao> observacoes) {
        observacoes = Stream.of(observacoes).sorted((obs1, obs2) -> obs1.getDescricao().compareTo(obs2.getDescricao())).toList();
        return observacoes;
    }

    public static void setUsuarioLogado(Context contexto, Usuario usuario) {
        ConfigUtils.setUsuario(contexto, usuario.getPessoa().getId().intValue(), usuario.getPessoa().getNome() != null ? usuario.getPessoa().getNome() : "Nome do garçom");
        setAcoesUsuarioLogado(contexto, usuario);
    }

    public static void setImpressora(Context contexto, Impressora impressora) {
        ConfigUtils.setImpressora(contexto, impressora.getConfiguracao());
    }

    public static String getImpressora(Context contexto) {
        return ConfigUtils.getImpressora(contexto);
    }

    private static List<String> getAceosUsuario(Context contexto) {
        return ConfigUtils.getListaSession(contexto, StringUtils.ACOES);
    }

    private static void setAcoesUsuarioLogado(Context contexto, Usuario usuario) {
        List<String> acoes = StringUtils.getListString(usuario.getAcoesPermitidas(), StringUtils.ESPACO);
        ConfigUtils.setListaSession(contexto, acoes, StringUtils.ACOES);
    }

    public static Usuario getUsuarioLogado(Context contexto) {
        Usuario usuario = new Usuario();
        int id = ConfigUtils.getInt(contexto,StringUtils.ID);
        usuario.setId(new Long(id));
        if (usuario.getPessoa() == null) {
            Pessoa pessoa = new Pessoa();
            pessoa.setId(usuario.getId().intValue());
            pessoa.setNome(ConfigUtils.getString(contexto,StringUtils.NOME));
            usuario.setPessoa(pessoa);
        }
        usuario.setAcoes(getAceosUsuario(contexto));
        return usuario;
    }

    public static boolean exibirExcecao(Context contexto, Exception ex) {
        if (ex != null && (ex instanceof SocketTimeoutException || ex instanceof ConnectException)) {
            InfoUtils.toast(contexto, contexto.getString(R.string.falha_conexao_servidor));
            return true;
        }

        return false;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }
}
