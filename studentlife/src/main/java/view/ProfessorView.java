package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Database;

public class ProfessorView extends JFrame {

    private JTextField nomeField;
    private JTextField telefoneField;
    private JTable professorTable;
    private DefaultTableModel tableModel;

    public ProfessorView() {
        setTitle("Gerenciar Professores");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel nomeLabel = new JLabel("Nome:");
        nomeLabel.setBounds(10, 20, 80, 25);
        panel.add(nomeLabel);

        nomeField = new JTextField(20);
        nomeField.setBounds(100, 20, 165, 25);
        panel.add(nomeField);

        JLabel telefoneLabel = new JLabel("Telefone:");
        telefoneLabel.setBounds(10, 50, 80, 25);
        panel.add(telefoneLabel);

        telefoneField = new JTextField(20);
        telefoneField.setBounds(100, 50, 165, 25);
        panel.add(telefoneField);

        JButton adicionarButton = new JButton("Adicionar");
        adicionarButton.setBounds(10, 80, 100, 25);
        panel.add(adicionarButton);

        JButton atualizarButton = new JButton("Atualizar");
        atualizarButton.setBounds(120, 80, 100, 25);
        panel.add(atualizarButton);

        JButton excluirButton = new JButton("Excluir");
        excluirButton.setBounds(230, 80, 100, 25);
        panel.add(excluirButton);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 120, 560, 230);
        panel.add(scrollPane);

        professorTable = new JTable();
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Telefone"}, 0
        );
        professorTable.setModel(tableModel);
        scrollPane.setViewportView(professorTable);

        add(panel);

        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                String telefone = telefoneField.getText();
                adicionarProfessor(nome, telefone);
            }
        });

        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = professorTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    String nome = nomeField.getText();
                    String telefone = telefoneField.getText();
                    atualizarProfessor(id, nome, telefone);
                }
            }
        });

        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = professorTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    excluirProfessor(id);
                }
            }
        });

        carregarProfessores();
    }

    private void carregarProfessores() {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM Professor";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String telefone = rs.getString("telefone");
                tableModel.addRow(new Object[]{id, nome, telefone});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar professores");
        }
    }

    private void adicionarProfessor(String nome, String telefone) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO Professor (nome, telefone) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.executeUpdate();

            tableModel.setRowCount(0);
            carregarProfessores();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao adicionar professor");
        }
    }

    private void atualizarProfessor(int id, String nome, String telefone) {
        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE Professor SET nome = ?, telefone = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.setInt(3, id);
            stmt.executeUpdate();

            tableModel.setRowCount(0);
            carregarProfessores();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar professor");
        }
    }

    private void excluirProfessor(int id) {
        try (Connection conn = Database.getConnection()) {
            String sql = "DELETE FROM Professor WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            tableModel.setRowCount(0);
            carregarProfessores();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao excluir professor");
        }
    }
}