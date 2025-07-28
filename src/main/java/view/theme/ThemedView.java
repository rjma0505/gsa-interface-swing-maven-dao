package view.theme;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel; // Importação adicionada
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.prefs.Preferences;

public abstract class ThemedView extends JFrame {

    protected Theme currentTheme;
    private static final String THEME_PREF_KEY = "appTheme";
    private final Preferences prefs = Preferences.userNodeForPackage(ThemedView.class);

    // Construtor para JFrame
    public ThemedView(String title) {
        super(title);
        loadThemePreference();
        initializeComponents();
        applyTheme();
    }

    // Nota sobre este construtor:
    // Este construtor (com parent, title, modal) é geralmente para JDialogs.
    // Como ThemedView estende JFrame, não pode ser usado diretamente por um JDialog.
    // Se a intenção é que views como EditarVeiculoView sejam JDialogs modais,
    // elas deveriam estender JDialog diretamente e usar a lógica de tema internamente,
    // ou ter uma classe ThemedDialog separada.
    // Mantenho-o aqui para compatibilidade com o código que me forneceu anteriormente,
    // mas vale a pena revisar esta parte da arquitetura se houver problemas.
    public ThemedView(JFrame parent, String title, boolean modal) {
        super(title); // Chama o construtor do JFrame
        loadThemePreference();
        // initializeComponents() e applyTheme() devem ser chamados pela subclasse
        // ou após a sua própria inicialização, pois a modalidade é do JDialog.
        // As JDialogs modais são "bloqueantes" e esperam uma resposta.
        // A lógica de tema é aplicada no método applyTheme() que é chamado pelo construtor JFrame.
        // O parent e modalidade de JDialog serão tratados na subclasse (EditarVeiculoView, se for um JDialog).
    }

    // Método abstrato a ser implementado pelas subclasses para inicialização de componentes
    protected abstract void initializeComponents();

    // Método para carregar a preferência de tema
    private void loadThemePreference() {
        String themeName = prefs.get(THEME_PREF_KEY, "Dark"); // Padrão: Tema Escuro
        if ("Light".equals(themeName)) {
            currentTheme = new LightTheme();
        } else {
            currentTheme = new DarkTheme();
        }
    }

    // Método para salvar a preferência de tema
    private void saveThemePreference(String themeName) {
        prefs.put(THEME_PREF_KEY, themeName);
    }

    // Método para alternar o tema
    protected void switchTheme() {
        if (currentTheme instanceof DarkTheme) {
            currentTheme = new LightTheme();
        } else {
            currentTheme = new DarkTheme();
        }
        saveThemePreference(currentTheme.getName());
        applyTheme(); // Aplica o novo tema a todos os componentes
        revalidate(); // Recalcula o layout
        repaint();    // Redesenha os componentes
    }

    // Aplica o tema a todos os componentes recursivamente
    protected void applyTheme() {
        applyThemeToComponent(this.getContentPane());
        // Aplica o tema ao JMenuBar, se existir
        if (this.getJMenuBar() != null) {
            applyThemeToMenuBar(this.getJMenuBar());
        }
    }

    // Método genérico para aplicar tema a um componente
    protected void applyThemeToComponent(Component component) {
        if (component == null) {
            return;
        }

        // Aplica tema com base no tipo de componente
        if (component instanceof JPanel) {
            applyThemeToPanel((JPanel) component);
        } else if (component instanceof JLabel) {
            applyThemeToLabel((JLabel) component);
        } else if (component instanceof JButton) {
            applyThemeToButton((JButton) component);
        } else if (component instanceof JTextField) {
            applyThemeToTextField((JTextField) component);
        } else if (component instanceof JPasswordField) {
            applyThemeToPasswordField((JPasswordField) component);
        } else if (component instanceof JComboBox) {
            applyThemeToComboBox((JComboBox<?>) component);
        } else if (component instanceof JTable) {
            applyThemeToTable((JTable) component);
        } else if (component instanceof JScrollPane) {
            applyThemeToScrollPane((JScrollPane) component);
        } else if (component instanceof JCheckBox) {
            applyThemeToCheckBox((JCheckBox) component);
        } else if (component instanceof JRadioButton) {
            applyThemeToRadioButton((JRadioButton) component);
        } else if (component instanceof JTextArea) {
            applyThemeToTextArea((JTextArea) component);
        }
        // Adicione mais tipos de componentes conforme necessário

        // Aplica recursivamente aos filhos (se for um container)
        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                applyThemeToComponent(child);
            }
        }
    }

    // --- Métodos Específicos para Tematização de Componentes ---

    protected void applyThemeToPanel(JPanel panel) {
        panel.setBackground(currentTheme.getBackgroundColor());
        panel.setForeground(currentTheme.getTextColor());
    }

    protected void applyThemeToLabel(JLabel label) {
        label.setForeground(currentTheme.getTextColor());
        label.setFont(currentTheme.getMainFont());
    }

    protected void applyThemeToButton(JButton button) {
        button.setBackground(currentTheme.getAccentBackgroundColor());
        button.setForeground(currentTheme.getButtonTextColor());
        button.setFocusPainted(false);
        button.setFont(currentTheme.getButtonFont());
        button.setBorder(currentTheme.getAccentBorder());
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Adiciona efeitos de hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(currentTheme.getButtonHoverColor());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(currentTheme.getAccentBackgroundColor());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(currentTheme.getButtonPressedColor());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(currentTheme.getButtonHoverColor());
            }
        });
    }

    protected void applyThemeToTextField(JTextField textField) {
        textField.setBackground(currentTheme.getForegroundColor());
        textField.setForeground(currentTheme.getTextColor());
        textField.setCaretColor(currentTheme.getTextColor());
        textField.setBorder(currentTheme.getFieldBorder());
        textField.setFont(currentTheme.getMainFont());
    }

    protected void applyThemeToPasswordField(JPasswordField passwordField) {
        passwordField.setBackground(currentTheme.getForegroundColor());
        passwordField.setForeground(currentTheme.getTextColor());
        passwordField.setCaretColor(currentTheme.getTextColor());
        passwordField.setBorder(currentTheme.getFieldBorder());
        passwordField.setFont(currentTheme.getMainFont());
    }

    protected void applyThemeToComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(currentTheme.getForegroundColor());
        comboBox.setForeground(currentTheme.getTextColor());
        comboBox.setFont(currentTheme.getMainFont());
        comboBox.setBorder(currentTheme.getFieldBorder());

        // Renderer personalizado para os itens da ComboBox
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? currentTheme.getAccentBackgroundColor() : currentTheme.getForegroundColor());
                setForeground(isSelected ? currentTheme.getButtonTextColor() : currentTheme.getTextColor());
                return this;
            }
        });
    }

    protected void applyThemeToTable(JTable table) {
        table.setFont(currentTheme.getMainFont());
        table.setBackground(currentTheme.getForegroundColor());
        table.setForeground(currentTheme.getTextColor());
        table.setSelectionBackground(currentTheme.getAccentColor().darker());
        table.setSelectionForeground(currentTheme.getTextColor());
        table.setGridColor(currentTheme.getBorderColor());
        table.setRowHeight(25); // Altura padrão da linha

        // Cabeçalho da Tabela
        JTableHeader header = table.getTableHeader();
        header.setBackground(currentTheme.getAccentBackgroundColor());
        header.setForeground(currentTheme.getButtonTextColor());
        header.setFont(currentTheme.getButtonFont());
        header.setBorder(BorderFactory.createLineBorder(currentTheme.getBorderColor()));

        // Renderer personalizado para células da tabela (cores alternadas ou estilização específica)
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? currentTheme.getForegroundColor() : currentTheme.getBackgroundColor());
                    c.setForeground(currentTheme.getTextColor());
                } else {
                    c.setBackground(currentTheme.getAccentColor().darker());
                    c.setForeground(currentTheme.getTextColor());
                }
                return c;
            }
        });
    }

    protected void applyThemeToScrollPane(JScrollPane scrollPane) {
        scrollPane.setBackground(currentTheme.getBackgroundColor());
        scrollPane.getViewport().setBackground(currentTheme.getForegroundColor());
        scrollPane.setBorder(BorderFactory.createLineBorder(currentTheme.getBorderColor(), 1));
    }

    protected void applyThemeToCheckBox(JCheckBox checkBox) {
        checkBox.setBackground(currentTheme.getBackgroundColor());
        checkBox.setForeground(currentTheme.getTextColor());
        checkBox.setFont(currentTheme.getMainFont());
    }

    protected void applyThemeToRadioButton(JRadioButton radioButton) {
        radioButton.setBackground(currentTheme.getBackgroundColor());
        radioButton.setForeground(currentTheme.getTextColor());
        radioButton.setFont(currentTheme.getMainFont());
    }

    protected void applyThemeToTextArea(JTextArea textArea) {
        textArea.setBackground(currentTheme.getForegroundColor());
        textArea.setForeground(currentTheme.getTextColor());
        textArea.setCaretColor(currentTheme.getTextColor());
        textArea.setBorder(currentTheme.getFieldBorder());
        textArea.setFont(currentTheme.getMainFont());
    }

    protected void applyThemeToMenuBar(JMenuBar menuBar) {
        menuBar.setBackground(currentTheme.getBackgroundColor());
        menuBar.setForeground(currentTheme.getTextColor());
        menuBar.setBorder(BorderFactory.createLineBorder(currentTheme.getBorderColor()));

        for (Component menuComponent : menuBar.getComponents()) {
            if (menuComponent instanceof JMenu) {
                applyThemeToMenu((JMenu) menuComponent);
            }
        }
    }

    protected void applyThemeToMenu(JMenu menu) {
        menu.setBackground(currentTheme.getBackgroundColor());
        menu.setForeground(currentTheme.getTextColor());
        menu.setFont(currentTheme.getMainFont());

        for (Component itemComponent : menu.getMenuComponents()) {
            if (itemComponent instanceof JMenuItem) {
                applyThemeToMenuItem((JMenuItem) itemComponent);
            } else if (itemComponent instanceof JMenu) { // Sub-menus
                applyThemeToMenu((JMenu) itemComponent);
            }
        }
    }

    protected void applyThemeToMenuItem(JMenuItem menuItem) {
        menuItem.setBackground(currentTheme.getForegroundColor());
        menuItem.setForeground(currentTheme.getTextColor());
        menuItem.setFont(currentTheme.getMainFont());
        menuItem.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Preenchimento

        menuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menuItem.setBackground(currentTheme.getAccentBackgroundColor());
                menuItem.setForeground(currentTheme.getButtonTextColor());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menuItem.setBackground(currentTheme.getForegroundColor());
                menuItem.setForeground(currentTheme.getTextColor());
            }
        });
    }

    // --- Métodos de Fábrica para Componentes Temáticos ---

    protected JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(currentTheme.getTitleFont());
        label.setForeground(currentTheme.getTextColor());
        return label;
    }

    protected JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(currentTheme.getMainFont());
        label.setForeground(currentTheme.getTextColor());
        return label;
    }

    // Sobrecarga: cria JTextField com tamanho padrão
    protected JTextField createStyledTextField() {
        JTextField field = new JTextField(20); // Default columns
        applyThemeToTextField(field);
        return field;
    }

    // Sobrecarga: cria JTextField com número de colunas especificado
    protected JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        applyThemeToTextField(field);
        return field;
    }

    protected JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        applyThemeToPasswordField(field);
        return field;
    }

    protected <T> JComboBox<T> createStyledComboBox() {
        JComboBox<T> comboBox = new JComboBox<>();
        applyThemeToComboBox(comboBox);
        return comboBox;
    }

    protected JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        applyThemeToButton(button);
        return button;
    }

    // NOVO MÉTODO DE FÁBRICA PARA JTable
    protected JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        // Aplica o estilo da tabela através do método existente
        applyThemeToTable(table);
        return table;
    }

    protected JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(currentTheme.getCardBackgroundColor());
        card.setBorder(currentTheme.getCardBorder());
        return card;
    }

    protected Border createRoundedBorder(Color color) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(8, 15, 8, 15) // Ajuste o preenchimento conforme necessário
        );
    }

    // --- Botão para Alternar Tema ---
    protected JButton createThemeToggleButton() {
        JButton toggleButton = new JButton("Tema: " + currentTheme.getName());
        toggleButton.setFont(currentTheme.getMainFont());
        toggleButton.setBackground(currentTheme.getAccentBackgroundColor());
        toggleButton.setForeground(currentTheme.getButtonTextColor());
        toggleButton.setFocusPainted(false);
        toggleButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(currentTheme.getAccentBorderColor(), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        toggleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        toggleButton.addActionListener(e -> {
            switchTheme();
            toggleButton.setText("Tema: " + currentTheme.getName()); // Atualiza o texto do botão
        });

        // Adiciona efeitos de hover para o botão de alternar tema
        toggleButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                toggleButton.setBackground(currentTheme.getButtonHoverColor());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                toggleButton.setBackground(currentTheme.getAccentBackgroundColor());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                toggleButton.setBackground(currentTheme.getButtonPressedColor());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                toggleButton.setBackground(currentTheme.getButtonHoverColor());
            }
        });

        return toggleButton;
    }

    // --- Métodos JOptionPane Tematizados ---
    protected void showThemedMessage(Component parentComponent, String message, String title, int messageType) {
        // Cria um painel personalizado para o conteúdo do diálogo
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(currentTheme.getBackgroundColor());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel messageLabel = new JLabel(message);
        messageLabel.setForeground(currentTheme.getTextColor());
        messageLabel.setFont(currentTheme.getMainFont());
        panel.add(messageLabel, BorderLayout.CENTER);

        // Ícone baseado no tipo de mensagem
        Icon icon = getThemedIcon(messageType);
        if (icon != null) {
            JLabel iconLabel = new JLabel(icon);
            panel.add(iconLabel, BorderLayout.WEST);
        }

        // Estilos para JOptionPane e botões (temporariamente aplicados)
        UIManager.put("OptionPane.background", currentTheme.getBackgroundColor());
        UIManager.put("Panel.background", currentTheme.getBackgroundColor());
        UIManager.put("OptionPane.messageForeground", currentTheme.getTextColor());
        UIManager.put("OptionPane.buttonFont", currentTheme.getButtonFont());
        UIManager.put("OptionPane.messageFont", currentTheme.getMainFont());

        UIManager.put("Button.background", currentTheme.getAccentBackgroundColor());
        UIManager.put("Button.foreground", currentTheme.getButtonTextColor());
        UIManager.put("Button.border", currentTheme.getAccentBorder());
        UIManager.put("Button.focusPainted", false);

        JOptionPane pane = new JOptionPane(panel, messageType, JOptionPane.DEFAULT_OPTION, null, new Object[]{"OK"});
        JDialog dialog = pane.createDialog(parentComponent, title);
        dialog.setBackground(currentTheme.getBackgroundColor()); // Garante que o fundo do diálogo seja tematizado
        dialog.setResizable(false);

        // Aplica o tema aos botões do option pane
        for (Component comp : pane.getComponents()) {
            if (comp instanceof JPanel) {
                for (Component buttonComp : ((JPanel) comp).getComponents()) {
                    if (buttonComp instanceof JButton) {
                        applyThemeToButton((JButton) buttonComp);
                    }
                }
            }
        }

        dialog.setVisible(true);
        dialog.dispose();

        // Reseta as propriedades do UIManager para evitar vazamento de estilos
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);
        UIManager.put("OptionPane.buttonFont", null);
        UIManager.put("OptionPane.messageFont", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);
        UIManager.put("Button.border", null);
        UIManager.put("Button.focusPainted", null);
    }

    protected int showThemedConfirmDialog(Component parentComponent, String message, String title, int optionType, int messageType) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(currentTheme.getBackgroundColor());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel messageLabel = new JLabel(message);
        messageLabel.setForeground(currentTheme.getTextColor());
        messageLabel.setFont(currentTheme.getMainFont());
        panel.add(messageLabel, BorderLayout.CENTER);

        Icon icon = getThemedIcon(messageType);
        if (icon != null) {
            JLabel iconLabel = new JLabel(icon);
            panel.add(iconLabel, BorderLayout.WEST);
        }

        UIManager.put("OptionPane.background", currentTheme.getBackgroundColor());
        UIManager.put("Panel.background", currentTheme.getBackgroundColor());
        UIManager.put("OptionPane.messageForeground", currentTheme.getTextColor());
        UIManager.put("OptionPane.buttonFont", currentTheme.getButtonFont());
        UIManager.put("OptionPane.messageFont", currentTheme.getMainFont());

        UIManager.put("Button.background", currentTheme.getAccentBackgroundColor());
        UIManager.put("Button.foreground", currentTheme.getButtonTextColor());
        UIManager.put("Button.border", currentTheme.getAccentBorder());
        UIManager.put("Button.focusPainted", false);

        JOptionPane pane = new JOptionPane(panel, messageType, optionType);
        JDialog dialog = pane.createDialog(parentComponent, title);
        dialog.setBackground(currentTheme.getBackgroundColor());
        dialog.setResizable(false);

        // Estiliza os botões dentro do option pane
        for (Component comp : pane.getComponents()) {
            if (comp instanceof JPanel) {
                for (Component buttonComp : ((JPanel) comp).getComponents()) {
                    if (buttonComp instanceof JButton) {
                        applyThemeToButton((JButton) buttonComp);
                    }
                }
            }
        }

        dialog.setVisible(true);
        dialog.dispose();

        // Reseta as propriedades do UIManager
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);
        UIManager.put("OptionPane.buttonFont", null);
        UIManager.put("OptionPane.messageFont", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);
        UIManager.put("Button.border", null);
        UIManager.put("Button.focusPainted", null);

        Object selectedValue = pane.getValue();
        if (selectedValue instanceof Integer) {
            return (int) selectedValue;
        }
        return JOptionPane.CLOSED_OPTION;
    }

    private Icon getThemedIcon(int messageType) {
        switch (messageType) {
            case JOptionPane.ERROR_MESSAGE:
                return UIManager.getIcon("OptionPane.errorIcon");
            case JOptionPane.INFORMATION_MESSAGE:
                return UIManager.getIcon("OptionPane.informationIcon");
            case JOptionPane.WARNING_MESSAGE:
                return UIManager.getIcon("OptionPane.warningIcon");
            case JOptionPane.QUESTION_MESSAGE:
                return UIManager.getIcon("OptionPane.questionIcon");
            default:
                return null;
        }
    }
}