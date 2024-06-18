package view;

import model.Database;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DashboardView extends JFrame {

    private JLabel lblTotalAlunos;
    private JLabel lblTotalProfessores;
    private JLabel lblTotalDisciplinas;
    private JLabel lblTotalMateriais;

    public DashboardView() {
        initComponents();
        loadStatistics();
    }

    private void initComponents() {
        setTitle("Dashboard");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        lblTotalAlunos = new JLabel("Total de Alunos: ");
        panel.add(lblTotalAlunos);

        lblTotalProfessores = new JLabel("Total de Professores: ");
        panel.add(lblTotalProfessores);

        lblTotalDisciplinas = new JLabel("Total de Disciplinas: ");
        panel.add(lblTotalDisciplinas);

        lblTotalMateriais = new JLabel("Total de Materiais: ");
        panel.add(lblTotalMateriais);

        getContentPane().add(panel);
    }

    private void loadStatistics() {
        try {
            int totalAlunos = getCount("aluno");
            int totalProfessores = getCount("professor");
            int totalDisciplinas = getCount("disciplina");
            int totalMateriais = getCount("material");

            lblTotalAlunos.setText("Total de Alunos: " + totalAlunos);
            lblTotalProfessores.setText("Total de Professores: " + totalProfessores);
            lblTotalDisciplinas.setText("Total de Disciplinas: " + totalDisciplinas);
            lblTotalMateriais.setText("Total de Materiais: " + totalMateriais);
        } catch (SQLException e) {

        }
    }

    private int getCount(String tableName) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM " + tableName;
        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        }
        return 0;
    }

}
