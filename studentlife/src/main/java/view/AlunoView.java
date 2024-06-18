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

public class AlunoView extends JFrame {

    private JTextField nomeField;
    private JTextField telefoneField;
    private JTextField raField;
    private JTable alunoTable;
    private DefaultTableModel tableModel;

    public AlunoView() {
        setTitle("Gerenciar Alunos");
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

        JLabel raLabel = new JLabel("RA:");
        raLabel.setBounds(10, 80, 80, 25);
        panel.add(raLabel);

        raField = new JTextField(20);
        raField.setBounds(100, 80, 165, 25);
        panel.add(raField);

        JButton adicionarButton = new JButton("Adicionar");
        adicionarButton.setBounds(10, 110, 100, 25);
        panel.add(adicionarButton);

        JButton atualizarButton = new JButton("Atualizar");
        atualizarButton.setBounds(120, 110, 100, 25);
        panel.add(atualizarButton);

        JButton excluirButton = new JButton("Excluir");
        excluirButton.setBounds(230, 110, 100, 25);
        panel.add(excluirButton);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 150, 560, 200);
        panel.add(scrollPane);

        alunoTable = new JTable();
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Telefone", "RA"}, 0
        );
        alunoTable.setModel(tableModel);
        scrollPane.setViewportView(alunoTable);

        add(panel);

        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                String telefone = telefoneField.getText();
                String ra = raField.getText();
                adicionarAluno(nome, telefone, ra);
            }
        });

        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = alunoTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    String nome = nomeField.getText();
                    String telefone = telefoneField.getText();
                    String ra = raField.getText();
                    atualizarAluno(id, nome, telefone, ra);
                }
            }
        });

        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = alunoTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    excluirAluno(id);
                }
            }
        });

        carregarAlunos();
    }

    private void carregarAlunos() {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM Aluno";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String telefone = rs.getString("telefone");
                String ra = rs.getString("ra");
                tableModel.addRow(new Object[]{id, nome, telefone, ra});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar alunos");
        }
    }

    private void adicionarAluno(String nome, String telefone, String ra) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO Aluno (nome, telefone, ra) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.setString(3, ra);
            stmt.executeUpdate();

            tableModel.setRowCount(0);
            carregarAlunos();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao adicionar aluno");
        }
    }

    private void atualizarAluno(int id, String nome, String telefone, String ra) {
        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE Aluno SET nome = ?, telefone = ?, ra = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.setString(3, ra);
            stmt.setInt(4, id);
            stmt.executeUpdate();

            tableModel.setRowCount(0);
            carregarAlunos();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar aluno");
        }
    }

    private void excluirAluno(int id) {
        try (Connection conn = Database.getConnection()) {
            String sql = "DELETE FROM Aluno WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            tableModel.setRowCount(0);
            carregarAlunos();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao excluir aluno");
        }
    }
}