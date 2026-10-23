package br.com.motiva.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RELATORIO_PRIORIDADE")
public class RelatorioPrioridade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_relatorio")
    @SequenceGenerator(name = "seq_relatorio", sequenceName = "SEQ_RELATORIO", allocationSize = 1)
    @Column(name = "ID_RELATORIO")
    private Long id;

    @Column(name = "DATA_GERACAO", nullable = false)
    private LocalDateTime dataGeracao;

    @Column(name = "TOTAL_TRECHOS", nullable = false)
    private Integer totalTrechos;

    @Column(name = "QTD_URGENTE", nullable = false)
    private Integer qtdUrgente;

    @Column(name = "QTD_CRITICO", nullable = false)
    private Integer qtdCritico;

    @Column(name = "QTD_ATENCAO", nullable = false)
    private Integer qtdAtencao;

    @Column(name = "QTD_NORMAL", nullable = false)
    private Integer qtdNormal;

    @OneToMany(mappedBy = "relatorio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemRelatorio> itens = new ArrayList<>();

    public RelatorioPrioridade() { }

    public void adicionarItem(ItemRelatorio item) {
        item.setRelatorio(this);
        itens.add(item);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDataGeracao() { return dataGeracao; }
    public void setDataGeracao(LocalDateTime dataGeracao) { this.dataGeracao = dataGeracao; }
    public Integer getTotalTrechos() { return totalTrechos; }
    public void setTotalTrechos(Integer totalTrechos) { this.totalTrechos = totalTrechos; }
    public Integer getQtdUrgente() { return qtdUrgente; }
    public void setQtdUrgente(Integer qtdUrgente) { this.qtdUrgente = qtdUrgente; }
    public Integer getQtdCritico() { return qtdCritico; }
    public void setQtdCritico(Integer qtdCritico) { this.qtdCritico = qtdCritico; }
    public Integer getQtdAtencao() { return qtdAtencao; }
    public void setQtdAtencao(Integer qtdAtencao) { this.qtdAtencao = qtdAtencao; }
    public Integer getQtdNormal() { return qtdNormal; }
    public void setQtdNormal(Integer qtdNormal) { this.qtdNormal = qtdNormal; }
    public List<ItemRelatorio> getItens() { return itens; }
    public void setItens(List<ItemRelatorio> itens) { this.itens = itens; }
}
