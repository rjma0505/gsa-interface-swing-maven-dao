package controller;

import view.CriarNovoClienteView;
import view.MenuView;
import model.Comprador;
import dao.CompradorDAO;

import javax.swing.*;

public class CriarNovoClienteController {
    private CriarNovoClienteView view;
    private MenuView menuView;
    private CompradorDAO compradorDAO;

    public CriarNovoClienteController(MenuView menuView) {
        this.menuView = menuView;
        this.view = new CriarNovoClienteView();
        this.compradorDAO = new CompradorDAO();

        view.adicionarAcaoCriarCliente(e -> criarCliente());
        view.adicionarAcaoVoltar(e -> voltar());

        view.setVisible(true);
    }

    private void criarCliente() {
        if (!view.validarCampos()) {
            return; // Se falhar a validação, aborta
        }

        Comprador comprador = new Comprador();
        comprador.setNomeCompleto(view.getNomeCompleto().trim());
        comprador.setTelefone(view.getTelefone().trim());
        comprador.setEmail(view.getEmail().trim());

        try {
            compradorDAO.salvar(comprador);
            view.exibirMensagem("Cliente criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            view.exibirMensagem("Erro ao criar cliente: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void voltar() {
        view.dispose();
        menuView.setVisible(true);
    }
}
