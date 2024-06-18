package model;

public class Material {

    private int id;
    private String titulo;
    private String conteudo;
    private int disciplinaId;

    public Material(int id, String titulo, String conteudo, int disciplinaId) {
        this.id = id;
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.disciplinaId = disciplinaId;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the conteudo
     */
    public String getConteudo() {
        return conteudo;
    }

    /**
     * @param conteudo the conteudo to set
     */
    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    /**
     * @return the disciplinaId
     */
    public int getDisciplinaId() {
        return disciplinaId;
    }

    /**
     * @param disciplinaId the disciplinaId to set
     */
    public void setDisciplinaId(int disciplinaId) {
        this.disciplinaId = disciplinaId;
    }
}
