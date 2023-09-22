import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// Class representing a person with a name and last name
class Person {
    private String name;
    private String lastName;

    public Person(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return name + " " + lastName;
    }
}

// Class representing an applicant, extends Person and implements Comparable for sorting
class Applicant extends Person implements Comparable<Applicant> {
    private int applicantID;
    private double alesScore;
    private double gpa;
    private double examScore;
    private double[] interviewScores;

    // Define weights for calculating the final score
    private static final double ALES_WEIGHT = 0.35;
    private static final double GPA_WEIGHT = 0.2;
    private static final double EXAM_WEIGHT = 0.3;
    private static final double INTERVIEW_WEIGHT = 0.15;

    public Applicant(String name, String lastName, int applicantID, double alesScore, double gpa, double examScore, double[] interviewScores) {
        super(name, lastName);
        this.applicantID = applicantID;
        this.alesScore = alesScore;
        this.gpa = gpa;
        this.examScore = examScore;
        this.interviewScores = interviewScores;
    }

    // Calculate the final score based on defined weights
    public double getFinalScore() {
        // Preprocess scores and bring them to the 0-100 interval
        double processedAlesScore = Math.min((alesScore / 100.0) * 100.0, 100.0);
        double processedGpa = Math.min((gpa - 2.0) * 25.0 + 50.0, 100.0);
        double processedExamScore = Math.min((examScore / 100.0) * 100.0, 100.0);
        double processedInterviewScores = Math.min(((interviewScores[0] + interviewScores[1]) / 10.0) * 100.0, 100.0);

        double finalScore = (processedAlesScore * ALES_WEIGHT + processedGpa * GPA_WEIGHT + processedExamScore * EXAM_WEIGHT + processedInterviewScores * INTERVIEW_WEIGHT) / (ALES_WEIGHT + GPA_WEIGHT + EXAM_WEIGHT + INTERVIEW_WEIGHT);

        return finalScore;
    }

    // Implement Comparable interface for sorting applicants by final score
    @Override
    public int compareTo(Applicant other) {
        double thisFinalScore = this.getFinalScore();
        double otherFinalScore = other.getFinalScore();

        if (thisFinalScore < otherFinalScore) {
            return 1;
        } else if (thisFinalScore > otherFinalScore) {
            return -1;
        } else {
            return 0;
        }
    }

    // Override toString to display applicant information along with the final score
    @Override
    public String toString() {
        return super.toString() + " - Final Score: " + getFinalScore();
    }
}

// Main application class extending JFrame
public class ApplicantEvaluationApp extends JFrame {
    private JTextArea outputTextArea;
    private List<Applicant> applicants;

    // Constructor to initialize the UI
    public ApplicantEvaluationApp() {
        setTitle("Applicant Evaluation");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        applicants = new ArrayList<>();

        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton addApplicantButton = new JButton("Add Applicant");
        addApplicantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addApplicant();
            }
        });

        JButton evaluateApplicantsButton = new JButton("Evaluate Applicants");
        evaluateApplicantsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                evaluateApplicants();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addApplicantButton);
        buttonPanel.add(evaluateApplicantsButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Method to add an applicant to the list and display their information
    private void addApplicant() {
        String name = JOptionPane.showInputDialog("Enter name for the applicant:");
        String lastName = JOptionPane.showInputDialog("Enter last name for the applicant:");
        double alesScore = Double.parseDouble(JOptionPane.showInputDialog("Enter ALES score for the applicant:"));
        double gpa = Double.parseDouble(JOptionPane.showInputDialog("Enter GPA for the applicant:"));
        double examScore = Double.parseDouble(JOptionPane.showInputDialog("Enter exam score for the applicant:"));
        double interviewScore1 = Double.parseDouble(JOptionPane.showInputDialog("Enter interview score for Jury Member 1 for the applicant:"));
        double interviewScore2 = Double.parseDouble(JOptionPane.showInputDialog("Enter interview score for Jury Member 2 for the applicant:"));

        double[] interviewScores = {interviewScore1, interviewScore2};

        Applicant applicant = new Applicant(name, lastName, applicants.size() + 1, alesScore, gpa, examScore, interviewScores);
        applicants.add(applicant);

        outputTextArea.append("Applicant " + applicants.size() + " - " + name + " " + lastName + "\n");
        outputTextArea.append("ALES Score: " + alesScore + "\n");
        outputTextArea.append("GPA: " + gpa + "\n");
        outputTextArea.append("Exam Score: " + examScore + "\n");
        outputTextArea.append("Interview Scores: " + interviewScore1 + ", " + interviewScore2 + "\n\n");
    }

    // Method to evaluate applicants and display the sorted results
    private void evaluateApplicants() {
        if (applicants.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No applicants to evaluate. Please add applicants first.");
            return;
        }

        // Sort applicants in descending order of final scores
        applicants.sort((a1, a2) -> Double.compare(a2.getFinalScore(), a1.getFinalScore()));

        outputTextArea.append("Sorted Applicants (in descending order of final score):\n");
        for (Applicant applicant : applicants) {
            outputTextArea.append(applicant.toString() + "\n");
        }
    }

    // Main method to launch the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ApplicantEvaluationApp().setVisible(true);
            }
        });
    }
}
