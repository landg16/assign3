package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;


public class SudokuFrame extends JFrame {

 	private JTextArea sudoku;
 	private JTextArea solution;
 	private JCheckBox autoCheck;
 	private JButton checkSolution;
	
	public SudokuFrame() {
		super("Sudoku Solver");
		setLayout(new BorderLayout(4,4));
		sudokuFrame();
		buttonsFrame();
		addListeners();
		// setLocationByPlatform(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private void sudokuFrame() {
		sudoku = new JTextArea(15,20);
		solution = new JTextArea(15,20);
		sudoku.setBorder(new TitledBorder("Puzzle"));
		solution.setBorder(new TitledBorder("Solution"));
		add(sudoku, BorderLayout.WEST);
		add(solution, BorderLayout.EAST);
	}

	private void buttonsFrame() {
		Box bottomBox = new Box(BoxLayout.X_AXIS);
		checkSolution = new JButton("Check");
		autoCheck = new JCheckBox("Auto Check");
		autoCheck.setSelected(true);
		bottomBox.add(checkSolution);
		bottomBox.add(autoCheck);
		add(bottomBox, BorderLayout.SOUTH);
	}

	private void addListeners(){
		checkSolution.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				findSolutions();
			}
		});

		Document sudokuDoc = sudoku.getDocument();
		sudokuDoc.addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(autoCheck.isSelected()) findSolutions();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if(autoCheck.isSelected()) findSolutions();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if(autoCheck.isSelected()) findSolutions();
			}
		});
	}

	private void findSolutions() {
		try {
			Sudoku solver = new Sudoku(Sudoku.textToGrid(sudoku.getText()));
			int solNumber = solver.solve();
			long solTime = solver.getElapsed();
			String solText = solver.getSolutionText();
			solText += "Solutions:" + solNumber
					+  "\nElapsed:" + solTime + "ms";
			solution.setText(solText);
		} catch (Exception e) {
			solution.setText("Parsing problem\nTry Again");
		}
	}

	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }

		SudokuFrame frame = new SudokuFrame();
	}
}