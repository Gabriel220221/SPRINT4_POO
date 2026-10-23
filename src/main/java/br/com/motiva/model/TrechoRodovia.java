package br.com.motiva.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TRECHO_RODOVIA")
public class TrechoRodovia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trecho")
    @SequenceGenerator(name = "seq_trecho", sequenceName = "SEQ_TRECHO", allocationSize = 1)
    @Column(name = "ID_TRECHO")
    private Long id;

    @Column(name = "RODOVIA", nullable = false, length = 60)
    private String rodovia;

    @Column(name = "KM_INICIAL", nullable = false)
    private Double kmInicial;

    @Column(name = "KM_FINAL", nullable = false)
    private Double kmFinal;

    @Column(name = "TIPO", nullable = false, length = 30)
    private String tipo;

    @Column(name = "NIVEL_VEGETACAO_CM", nullable = false)
    private Double nivelVegetacaoCm;

    public TrechoRodovia() { }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRodovia() { return rodovia; }
    public void setRodovia(String rodovia) { this.rodovia = rodovia; }
    public Double getKmInicial() { return kmInicial; }
    public void setKmInicial(Double kmInicial) { this.kmInicial = kmInicial; }
    public Double getKmFinal() { return kmFinal; }
    public void setKmFinal(Double kmFinal) { this.kmFinal = kmFinal; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Double getNivelVegetacaoCm() { return nivelVegetacaoCm; }
    public void setNivelVegetacaoCm(Double nivelVegetacaoCm) { this.nivelVegetacaoCm = nivelVegetacaoCm; }
}
