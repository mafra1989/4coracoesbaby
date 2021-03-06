package com.webapp.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.webapp.model.CategoriaProduto;
import com.webapp.repository.filter.CategoriaProdutoFilter;
import com.webapp.util.jpa.Transacional;

public class CategoriasProdutos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public CategoriaProduto porId(Long id) {
		return this.manager.find(CategoriaProduto.class, id);
	}

	@Transacional
	public CategoriaProduto save(CategoriaProduto categoriaProduto) {
		return this.manager.merge(categoriaProduto);
	}

	@Transacional
	public void remove(CategoriaProduto categoriaProduto) {
		CategoriaProduto categoriaProdutoTemp = new CategoriaProduto();
		categoriaProdutoTemp = this.manager.merge(categoriaProduto);

		this.manager.remove(categoriaProdutoTemp);
	}

	public List<CategoriaProduto> todos(String empresa) {
		return this.manager.createQuery("from CategoriaProduto c where c.empresa = :empresa order by c.nome", CategoriaProduto.class)
				.setParameter("empresa", empresa).getResultList();
	}
	
	public List<CategoriaProduto> todosEmDestaque(String empresa) {
		return this.manager.createQuery("from CategoriaProduto c where c.empresa = :empresa order by c.id", CategoriaProduto.class)
				.setParameter("empresa", empresa).getResultList();
	}
	
	public CategoriaProduto porNome(String nome) {
		
		try {
			return this.manager.createQuery("from CategoriaProduto i where i.nome = :nome order by nome", CategoriaProduto.class)
					.setParameter("nome", nome).getSingleResult();
		} catch(NoResultException e) {		
		}
		
		return null;
	}
	
	public CategoriaProduto porNome(String nome, String empresa) {
		
		try {
			return this.manager.createQuery("from CategoriaProduto i where upper(i.nome) = :nome and i.empresa = :empresa order by nome", CategoriaProduto.class)
					.setParameter("nome", nome.toUpperCase()).setParameter("empresa", empresa).getSingleResult();
		} catch(NoResultException e) {		
		}
		
		return null;
	}

	public List<CategoriaProduto> filtrados(CategoriaProdutoFilter filter) {
		
		return this.manager.createQuery("from CategoriaProduto i where i.nome like :nome and empresa = :empresa order by nome", CategoriaProduto.class)
				.setParameter("nome", "%" + filter.getNome() + "%")
				.setParameter("empresa", filter.getEmpresa()).getResultList();	
	}
	
	

	public CategoriaProduto porNomeCadastrado(CategoriaProduto categoriaProduto, String empresa) {
		try {
			return this.manager  
					.createQuery("from CategoriaProduto e where lower(e.nome) = :nome and e.id != :id and e.empresa = :empresa", CategoriaProduto.class)
					.setParameter("nome", categoriaProduto.getNome().toLowerCase())
					.setParameter("id", categoriaProduto.getId())
					.setParameter("empresa", empresa).getSingleResult();
		} catch(NoResultException e) {
			return null;
		}
	}

}