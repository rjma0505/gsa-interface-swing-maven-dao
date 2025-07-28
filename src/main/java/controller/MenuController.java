package controller;

import service.UserSession;
import view.MenuView;
import model.Perfil;
import javax.swing.JOptionPane;

public class MenuController {

    private MenuView menuView; // Removido o 'final' para permitir inicialização segura no try-catch

    public MenuController() {
        System.out.println("MenuController: A inicializar...");
        try {
            this.menuView = new MenuView(); // Tenta criar a MenuView
            System.out.println("MenuController: MenuView criada com sucesso.");

            // --- Lógica de Permissões ---
            System.out.println("MenuController: A configurar visibilidade dos botões com base no perfil.");
            Perfil perfil = UserSession.getInstance().getPerfil();
            if (perfil != null) {
                String descricaoPerfil = perfil.getDescricao();
                System.out.println("MenuController: Perfil do utilizador logado: " + descricaoPerfil);

                // Por padrão, esconde o botão Inserir e depois mostra os permitidos
                menuView.setInserirButtonVisible(false); // Esconde por padrão
                menuView.setCriarClienteButtonVisible(false);

                menuView.setListarButtonVisible(true); // Listar é sempre visível
                menuView.setLogoutButtonVisible(true); // Logout é sempre visível

                if (descricaoPerfil.equalsIgnoreCase("Administrador")) {
                    menuView.setInserirButtonVisible(true); // Administrador pode inserir
                    menuView.setCriarClienteButtonVisible(true);
                    menuView.setListarButtonVisible(true);      // <-- Ativa o botão
                    menuView.setLogoutButtonVisible(true);

                    System.out.println("MenuController: Visibilidade: 'Administrador' pode Inserir, Listar e Criar Cliente");
                } else if (descricaoPerfil.equalsIgnoreCase("Vendedor")) {
                    // O botão Inserir já está escondido por padrão (setInserirButtonVisible(false) acima)
                    // Não precisamos fazer nada aqui para o Vendedor, apenas confirmar que não pode inserir.
                    menuView.setCriarClienteButtonVisible(true);
                    System.out.println("MenuController: Visibilidade: 'Vendedor' pode apenas Listar (não pode Inserir).");
                } else if (descricaoPerfil.equalsIgnoreCase("Responsavel de Frota")) {
                    // O botão Inserir já está escondido por padrão
                    System.out.println("MenuController: Visibilidade: 'Responsavel de Frota' pode apenas Listar (não pode Inserir).");
                } else {
                    menuView.setInserirButtonVisible(false);
                    menuView.setCriarClienteButtonVisible(false);
                    menuView.setListarButtonVisible(false);
                    System.out.println("MenuController: Visibilidade: Perfil desconhecido ou não autorizado. Botões de gestão escondidos.");
                }
            } else {
                // Caso não haja sessão ativa ou perfil nulo (situação de erro, mas para segurança)
                menuView.setInserirButtonVisible(false);
                menuView.setListarButtonVisible(false);
                menuView.setLogoutButtonVisible(false);
                JOptionPane.showMessageDialog(menuView,
                        "Erro: Perfil do utilizador não encontrado na sessão. Acesso restrito.",
                        "Erro de Sessão",
                        JOptionPane.ERROR_MESSAGE);
                System.err.println("MenuController: Erro: Perfil do utilizador não encontrado na sessão. Todos os botões do menu escondidos.");
            }
            // --- Fim da Lógica de Permissões ---

            this.menuView.mostrar(); // Torna a janela visível
            System.out.println("MenuController: MenuView exibida.");

            // --- Adicionar ActionListeners para os botões ---
            System.out.println("MenuController: A configurar ações dos botões.");

            // Ação para o botão "Inserir Veículo"
            menuView.getBtnInserir().addActionListener(e -> {
                System.out.println("MenuController: Botão 'Inserir Veículo' clicado. A tentar abrir InserirController.");
                menuView.dispose(); // Fecha a janela do menu
                new InserirController(); // Abre o controlador para inserir veículo
                System.out.println("MenuController: InserirController provavelmente aberto (ou erro ocorreu).");
            });

            // Ação para o botão "Listar Veículos"
            menuView.getBtnListar().addActionListener(e -> {
                System.out.println("MenuController: Botão 'Listar Veículos' clicado. A tentar abrir ListarController.");
                menuView.dispose(); // Fecha a janela do menu
                new ListarController(false); // Passa false para indicar modo de listagem normal
                System.out.println("MenuController: ListarController provavelmente aberto (ou erro ocorreu).");
            });

            // Ação para o botão "Voltar" (Logout)
            menuView.getBtnLogout().addActionListener(e -> {
                System.out.println("MenuController: Botão 'Voltar' (Logout) clicado. A tentar efetuar logout.");
                UserSession.getInstance().cleanUserSession(); // Limpa a sessão do utilizador
                menuView.dispose(); // Fecha a janela do menu
                new LoginController(); // Reabre a janela de login
                System.out.println("MenuController: Logout efetuado e LoginController provavelmente aberto.");
            });

            menuView.getBtnCriarCliente().addActionListener(e -> {
                System.out.println("MenuController: Botão 'Criar Novo Cliente' clicado. A tentar abrir CriarNovoClienteController.");
                menuView.dispose(); // Fecha esta janela
                new CriarNovoClienteController(menuView); // Passa a MenuView atual
                System.out.println("MenuController: CriarNovoClienteController provavelmente aberto.");
            });



        } catch (Exception e) {
            System.err.println("MenuController: ERRO CRÍTICO na inicialização do MenuController!");
            System.err.println("Mensagem: " + e.getMessage());
            e.printStackTrace(); // Imprime a stack trace completa
            JOptionPane.showMessageDialog(null,
                    "Ocorreu um erro crítico ao carregar o menu. Por favor, contacte o suporte.",
                    "Erro Crítico",
                    JOptionPane.ERROR_MESSAGE);
            // Se a view foi criada, tentar descartá-la
            if (this.menuView != null) { // Verifica se menuView foi inicializada antes de tentar dispose
                this.menuView.dispose();
            }
            // Tentar voltar para a tela de login como fallback
            new LoginController();
        }
    }
}
