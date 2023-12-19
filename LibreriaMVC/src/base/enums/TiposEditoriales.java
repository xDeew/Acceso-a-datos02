package base.enums;

public enum TiposEditoriales {
    AUTOREDITOR("Autor-Editor"),
    EDITORIAL("Editorial"),
    ORGANISMOOFICIAL("Organismo Oficial"),
    UNIVERSIDAD("Universidad");

    private String valor;

    TiposEditoriales(String valor) {

        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
