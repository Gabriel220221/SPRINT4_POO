package br.com.motiva.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ITEM_RELATORIO")
public class ItemRelatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_item_relatorio")
    @SequenceGenerator(name = "seq_item_relatorio", sequenceName = "SEQ_ITEM_RELATORIO", allocationSize = 1)
    @Column(name = "ID_ITEM")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_RELATORIO", nullable = false)
    private RelatorioPrioridade relatorio;

    @Column(name = "ID_TRECHO", nullable = false)
    private Long trechoId;

    @Column(name = "RODOVIA", nullable = false, length = 60)
    private String rodovia;

    @Column(name = "NIVEL_VEGETACAO_CM", nullable = false)
    private Double nivelVegetacaoCm;

    @Enumerated(EnumType.STRING)
    @Column(name = "CLASSIFICACAO", nullable = false, length = 15)
    private NivelPrioridade classificacao;

    public ItemRelatorio() { }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public RelatorioPrioridade getRelatorio() { return relatorio; }
    public void setRelatorio(RelatorioPrioridade relatorio) { this.relatorio = relatorio; }
    public Long getTrechoId() { return trechoId; }
    public void setTrechoId(Long trechoId) { this.trechoId = trechoId; }
    public String getRodovia() { return rodovia; }
    public void setRodovia(String rodovia) { this.rodovia = rodovia; }
    public Double getNivelVegetacaoCm() { return nivelVegetacaoCm; }
    public void setNivelVegetacaoCm(Double nivelVegetacaoCm) { this.nivelVegetacaoCm = nivelVegetacaoCm; }
    public NivelPrioridade getClassificacao() { return classificacao; }
    public void setClassificacao(NivelPrioridade classificacao) { this.classificacao = classificacao; }
}
