package com.webapp.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.webapp.model.Fornecedor;
import com.webapp.repository.filter.FornecedorFilter;
import com.webapp.util.jpa.Transacional;

public class Fornecedores implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Fornecedor porId(Long id) {
		return this.manager.find(Fornecedor.class, id);
	}

	@Transacional
	public Fornecedor save(Fornecedor fornecedor) {
		return this.manager.merge(fornecedor);
	}

	@Transacional
	public void remove(Fornecedor fornecedor) {
		Fornecedor fornecedorTemp = new Fornecedor();
		fornecedorTemp = this.manager.merge(fornecedor);

		this.manager.remove(fornecedorTemp);
	}

	public List<Fornecedor> todos(String empresa) {
		return this.manager.createQuery("from Fornecedor f WHERE f.empresa = :empresa order by f.nome", Fornecedor.class)
				.setParameter("empresa", empresa).getResultList();
	}

	public List<Fornecedor> filtrados(FornecedorFilter filter) {
		return this.manager.createQuery("from Fornecedor i where i.nome like :nome AND i.empresa = :empresa order by i.nome", Fornecedor.class)
				.setParameter("nome", "%" + filter.getNome() + "%")
				.setParameter("empresa", filter.getEmpresa()).getResultList();
	}
	
	
	public Fornecedor porNome(String nome, String empresa) {
		
		try {
			return this.manager.createQuery("from Fornecedor i where upper(i.nome) = :nome and i.empresa = :empresa order by nome", Fornecedor.class)
					.setParameter("nome", nome.toUpperCase()).setParameter("empresa", empresa).getSingleResult();
		} catch(NoResultException e) {		
		}
		
		return null;
	}
	
	public Fornecedor porNomeCadastrado(Fornecedor fornecedor, String empresa) {
		try {
			return this.manager
					.createQuery("from Fornecedor e where upper(e.nome) = :nome and e.id != :id and e.empresa = :empresa", Fornecedor.class)
					.setParameter("nome", fornecedor.getNome().toUpperCase())
					.setParameter("id", fornecedor.getId())
					.setParameter("empresa", empresa).getSingleResult();
		} catch(NoResultException e) {
			return null;
		}
	}
}