package br.com.motiva.model;

import jakarta.persistence.*;

@Entity
@Table(name = "EQUIPE_MANUTENCAO")
public class EquipeManutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_equipe")
    @SequenceGenerator(name = "seq_equipe", sequenceName = "SEQ_EQUIPE", allocationSize = 1)
    @Column(name = "ID_EQUIPE")
    private Long id;

    @Column(name = "NOME", nullable = false, length = 80)
    private String nome;

    @Column(name = "ESPECIALIDADE", nullable = false, length = 40)
    private String especialidade;

    @Column(name = "QTD_MEMBROS", nullable = false)
    private Integer qtdMembros;

    @Column(name = "ATIVA", nullable = false)
    private Boolean ativa = true;

    public EquipeManutencao() { }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public Integer getQtdMembros() { return qtdMembros; }
    public void setQtdMembros(Integer qtdMembros) { this.qtdMembros = qtdMembros; }
    public Boolean getAtiva() { return ativa; }
    public void setAtiva(Boolean ativa) { this.ativa = ativa; }
}
