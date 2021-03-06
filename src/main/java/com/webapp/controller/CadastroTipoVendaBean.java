package com.webapp.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import com.webapp.model.TipoVenda;
import com.webapp.repository.TiposVendas;
import com.webapp.repository.filter.TipoVendaFilter;
import com.webapp.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroTipoVendaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TiposVendas tiposVendas;

	@Inject
	private TipoVenda tipoVenda;

	private List<TipoVenda> todosTiposVendas;

	private TipoVenda tipoVendaSelecionado;

	private TipoVendaFilter filtro = new TipoVendaFilter();

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			listarTodos();
		}
	}

	public void prepararNovoCadastro() {
		tipoVenda = new TipoVenda();
	}

	public void prepararEditarCadastro() {
		tipoVenda = tiposVendas.porId(tipoVendaSelecionado.getId());
	}

	public void salvar() {

		
		if (tipoVenda.getId() == null) {
			
			TipoVenda tipoVendaTemp = tiposVendas.porDescricao(tipoVenda.getDescricao());
		
			if(tipoVendaTemp == null) {
				
				tiposVendas.save(tipoVenda);
	
				tipoVendaSelecionado = null;
	
				listarTodos(); 
	
				PrimeFaces.current().executeScript(
						"PF('downloadLoading').hide(); PF('tipoVenda-dialog').hide();swal({ type: 'success', title: 'Concluído!', text: 'Tipo de venda salvo com sucesso!' });");
	
				PrimeFaces.current().ajax().update("form");
				
			} else {
						
				PrimeFaces.current().executeScript(
						"swal({ type: 'error', title: 'Erro!', text: 'Já existe um tipo de venda com o nome informado!' });");
			}
		
		} else {
			
			TipoVenda tipoVenda = tiposVendas.porDescricaoCadastrada(this.tipoVenda);
			
			if(tipoVenda == null) {
				
				tiposVendas.save(this.tipoVenda);
	
				tipoVendaSelecionado = null;
	
				listarTodos(); 
	
				PrimeFaces.current().executeScript(
						"PF('downloadLoading').hide();PF('tipoVenda-dialog').hide(); swal({ type: 'success', title: 'Concluído!', text: 'Tipo de venda atualizado com sucesso!' });");

				PrimeFaces.current().ajax().update("form");
				
			} else {
				PrimeFaces.current().executeScript(
						"swal({ type: 'error', title: 'Erro!', text: 'Já existe um tipo de venda com o nome informado!' });");	
			}
			
		}
	}

	public void excluir() {

		tiposVendas.remove(tipoVendaSelecionado);

		tipoVendaSelecionado = null;

		pesquisar();

		PrimeFaces.current().executeScript(
				"swal({ type: 'success', title: 'Concluído!', text: 'Tipo de venda excluído com sucesso!' });");

	}

	public void pesquisar() {
		todosTiposVendas = tiposVendas.filtrados(filtro);
		tipoVendaSelecionado = null;
	}

	private void listarTodos() {
		todosTiposVendas = tiposVendas.todos();
	}

	public List<TipoVenda> getTodosTiposVendas() {
		return todosTiposVendas;
	}

	public TipoVendaFilter getFiltro() {
		return filtro;
	}

	public void setFiltro(TipoVendaFilter filtro) {
		this.filtro = filtro;
	}

	public TipoVenda getTipoVendaSelecionado() {
		return tipoVendaSelecionado;
	}

	public void setTipoVendaSelecionado(TipoVenda tipoVendaSelecionado) {
		this.tipoVendaSelecionado = tipoVendaSelecionado;
	}

	public TipoVenda getTipoVenda() {
		return tipoVenda;
	}

	public void setTipoVenda(TipoVenda tipoVenda) {
		this.tipoVenda = tipoVenda;
	}

}