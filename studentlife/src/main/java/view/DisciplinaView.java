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

public class DisciplinaView extends JFrame {

    private JTextField nomeField;
    private JTextArea descricaoArea;
    private JTable disciplinaTable;
    private DefaultTableModel tableModel;

    public DisciplinaView() {
        setTitle("Gerenciar Disciplinas");
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

        JLabel descricaoLabel = new JLabel("Descrição:");
        descricaoLabel.setBounds(10, 50, 80, 25);
        panel.add(descricaoLabel);

        descricaoArea = new JTextArea();
        descricaoArea.setBounds(100, 50, 165, 75);
        panel.add(descricaoArea);

        JButton adicionarButton = new JButton("Adicionar");
        adicionarButton.setBounds(10, 130, 100, 25);
        panel.add(adicionarButton);

        JButton atualizarButton = new JButton("Atualizar");
        atualizarButton.setBounds(120, 130, 100, 25);
        panel.add(atualizarButton);

        JButton excluirButton = new JButton("Excluir");
        excluirButton.setBounds(230, 130, 100, 25);
        panel.add(excluirButton);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 170, 560, 180);
        panel.add(scrollPane);

        disciplinaTable = new JTable();
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Descrição"}, 0
        );
        disciplinaTable.setModel(tableModel);
        scrollPane.setViewportView(disciplinaTable);

        add(panel);

        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                String descricao = descricaoArea.getText();
                adicionarDisciplina(nome, descricao);
            }
        });

        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = disciplinaTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    String nome = nomeField.getText();
                    String descricao = descricaoArea.getText();
                    atualizarDisciplina(id, nome, descricao);
                }
            }
        });

        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = disciplinaTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    excluirDisciplina(id);
                }
            }
        });

        carregarDisciplinas();
    }

    private void carregarDisciplinas() {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM Disciplina";
            PreparedStatement stmt = conn.prepareCall(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                tableModel.addRow(new Object[]{id, nome, descricao});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar disciplinas");
        }
    }

    private void adicionarDisciplina(String nome, String descricao) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO Disciplina (nome, descricao) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.executeUpdate();

            tableModel.setRowCount(0);
            carregarDisciplinas();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao adicionar disciplina");
        }
    }

    private void atualizarDisciplina(int id, String nome, String descricao) {
        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE Disciplina SET nome = ?, descricao = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, descricao);
            stmt.setInt(3, id);
            stmt.executeUpdate();

            tableModel.setRowCount(0);
            carregarDisciplinas();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar disciplina");
        }
    }

    private void excluirDisciplina(int id) {
        try (Connection conn = Database.getConnection()) {
            String sql = "DELETE FROM Disciplina WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            tableModel.setRowCount(0);
            carregarDisciplinas();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao excluir disciplina");
        }
    }

}
