package br.com.motiva.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "INTERVENCAO")
public class Intervencao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_intervencao")
    @SequenceGenerator(name = "seq_intervencao", sequenceName = "SEQ_INTERVENCAO", allocationSize = 1)
    @Column(name = "ID_INTERVENCAO")
    private Long id;

    @Column(name = "DESCRICAO", nullable = false, length = 200)
    private String descricao;

    @Column(name = "STATUS", nullable = false, length = 20)
    private String status;

    @Column(name = "DATA_PREVISTA", nullable = false)
    private LocalDate dataPrevista;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_TRECHO", nullable = false)
    private TrechoRodovia trecho;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_EQUIPE", nullable = false)
    private EquipeManutencao equipe;

    public Intervencao() { }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getDataPrevista() { return dataPrevista; }
    public void setDataPrevista(LocalDate dataPrevista) { this.dataPrevista = dataPrevista; }
    public TrechoRodovia getTrecho() { return trecho; }
    public void setTrecho(TrechoRodovia trecho) { this.trecho = trecho; }
    public EquipeManutencao getEquipe() { return equipe; }
    public void setEquipe(EquipeManutencao equipe) { this.equipe = equipe; }
}
