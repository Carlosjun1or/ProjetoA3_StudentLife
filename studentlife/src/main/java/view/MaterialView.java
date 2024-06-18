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

public class MaterialView extends JFrame {

    private JTextField tituloField;
    private JTextField conteudoField;
    private JTextField disciplinaIdField;
    private JTable materialTable;
    private DefaultTableModel tableModel;

    public MaterialView() {
        setTitle("Gerenciar Materiais");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel tituloLabel = new JLabel("Título:");
        tituloLabel.setBounds(10, 20, 80, 25);
        panel.add(tituloLabel);

        tituloField = new JTextField(20);
        tituloField.setBounds(100, 20, 165, 25);
        panel.add(tituloField);

        JLabel conteudoLabel = new JLabel("Conteúdo:");
        conteudoLabel.setBounds(10, 50, 80, 25);
        panel.add(conteudoLabel);

        conteudoField = new JTextField(20);
        conteudoField.setBounds(100, 50, 165, 25);
        panel.add(conteudoField);

        JLabel disciplinaIdLabel = new JLabel("ID da Disciplina:");
        disciplinaIdLabel.setBounds(10, 80, 100, 25);
        panel.add(disciplinaIdLabel);

        disciplinaIdField = new JTextField(20);
        disciplinaIdField.setBounds(120, 80, 145, 25);
        panel.add(disciplinaIdField);

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

        materialTable = new JTable();
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Título", "Conteúdo", "ID da Disciplina"}, 0
        );
        materialTable.setModel(tableModel);
        scrollPane.setViewportView(materialTable);

        add(panel);

        adicionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titulo = tituloField.getText();
                String conteudo = conteudoField.getText();
                int disciplinaId = Integer.parseInt(disciplinaIdField.getText());
                adicionarMaterial(titulo, conteudo, disciplinaId);
            }
        });

        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = materialTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    String titulo = tituloField.getText();
                    String conteudo = conteudoField.getText();
                    String disciplinaId = disciplinaIdField.getText();
                    atualizarMaterial(id, titulo, conteudo, disciplinaId);
                }
            }
        });

        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = materialTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    excluirMaterial(id);
                }
            }
        });

        carregarMateriais();
    }

    private void carregarMateriais() {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM Material";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String conteudo = rs.getString("conteudo");
                int disciplinaId = rs.getInt("disciplinaId");
                tableModel.addRow(new Object[]{id, titulo, conteudo, disciplinaId});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao carregar materiais");
        }
    }

    private void adicionarMaterial(String titulo, String conteudo, int disciplinaId) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO Material (titulo, conteudo, disciplinaId) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, titulo);
            stmt.setString(2, conteudo);
            stmt.setInt(3, disciplinaId);
            stmt.executeUpdate();

            tableModel.setRowCount(0);
            carregarMateriais();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao adicionar material");
        }
    }

    private void atualizarMaterial(int id, String titulo, String conteudo, String disciplinaId) {
        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE Material SET titulo = ?, conteudo = ?, disciplinaId = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, titulo);
            stmt.setString(2, conteudo);
            stmt.setString(3, disciplinaId);
            stmt.setInt(4, id);
            stmt.executeUpdate();

            tableModel.setRowCount(0);
            carregarMateriais();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar material");
        }
    }

    private void excluirMaterial(int id) {
        try (Connection conn = Database.getConnection()) {
            String sql = "DELETE FROM Material WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            tableModel.setRowCount(0);
            carregarMateriais();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao excluir material");
        }
       }
    }
