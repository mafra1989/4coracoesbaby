package com.webapp.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.webapp.model.TipoVenda;
import com.webapp.repository.filter.TipoVendaFilter;
import com.webapp.util.jpa.Transacional;

public class TiposVendas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public TipoVenda porId(Long id) {
		return this.manager.find(TipoVenda.class, id);
	}

	@Transacional
	public TipoVenda save(TipoVenda tipoVenda) {
		return this.manager.merge(tipoVenda);
	}

	@Transacional
	public void remove(TipoVenda tipoVenda) {
		TipoVenda tipoVendaTemp = new TipoVenda();
		tipoVendaTemp = this.manager.merge(tipoVenda);

		this.manager.remove(tipoVendaTemp);
	}

	public List<TipoVenda> todos() {
		return this.manager.createQuery("from TipoVenda order by descricao", TipoVenda.class).getResultList();
	}

	public List<TipoVenda> filtrados(TipoVendaFilter filter) {
		return this.manager.createQuery("from TipoVenda i where lower(i.descricao) like lower(:descricao) order by descricao", TipoVenda.class)
				.setParameter("descricao", "%" + filter.getDescricao() + "%").getResultList();
	}
	
	public TipoVenda porDescricao(String descricao) {
		TipoVenda tipoVenda = null;
		try {
			tipoVenda = this.manager.createQuery("from TipoVenda i where lower(i.descricao) like :descricao order by descricao", TipoVenda.class)
					.setParameter("descricao", "%" + descricao.toLowerCase() + "%").getSingleResult();
			return tipoVenda;
			
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	public TipoVenda porDescricaoCadastrada(TipoVenda tipoVenda) {
		try {
			return this.manager
					.createQuery("from TipoVenda e where upper(e.descricao) = :descricao and e.id != :id", TipoVenda.class)
					.setParameter("descricao", tipoVenda.getDescricao().toUpperCase())
					.setParameter("id", tipoVenda.getId()).getSingleResult();
		} catch(NoResultException e) {
			return null;
		}
	}
	
}