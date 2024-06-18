package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {

    private JButton alunosButton;
    private JButton professoresButton;
    private JButton disciplinasButton;
    private JButton materiaisButton;
    private JButton dashboardButton;

    public MainView() {
        setTitle("Tela Principal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        alunosButton = new JButton("Gerenciar Alunos");
        alunosButton.setBounds(50, 30, 300, 30);
        panel.add(alunosButton);

        professoresButton = new JButton("Gerenciar Professores");
        professoresButton.setBounds(50, 70, 300, 30);
        panel.add(professoresButton);

        disciplinasButton = new JButton("Gerenciar Disciplinas");
        disciplinasButton.setBounds(50, 110, 300, 30);
        panel.add(disciplinasButton);

        materiaisButton = new JButton("Gerenciar Materiais");
        materiaisButton.setBounds(50, 150, 300, 30);
        panel.add(materiaisButton);

        dashboardButton = new JButton("Dashboard");
        dashboardButton.setBounds(50, 190, 300, 30);
        panel.add(dashboardButton);

        add(panel);

        alunosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AlunoView().setVisible(true);
            }
        });

        professoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProfessorView().setVisible(true);
            }
        });

        disciplinasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DisciplinaView().setVisible(true);
            }
        });

        materiaisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MaterialView().setVisible(true);
            }
        });

        dashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DashboardView().setVisible(true);
            }
        });
    }
}
