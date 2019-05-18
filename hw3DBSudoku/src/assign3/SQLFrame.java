package assign3;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SQLFrame extends JFrame {

    private JTextField metropolisInput;
    private JTextField continentInput;
    private JTextField populationInput;

    private JTable frameTable;
    private SQLBackend sqlTable;

    private JButton addBtn;
    private JButton searchBtn;
    private JComboBox populationDrop;
    private JComboBox matchDrop;


    public SQLFrame () {
        super("Metropolis Viewer");
        setLayout(new BorderLayout(4,4));

        //Private Methods
        sqlTable = new SQLBackend();
        frameTable = new JTable(sqlTable);
        addTopInputs();
        addSQLTable();
        addRightBox();
        searchPerformed();
        addPerformed();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private void addTopInputs(){
        Box box = Box.createHorizontalBox();
        box.setBorder(new EmptyBorder(10,10,10,10));
        box.add(Box.createHorizontalStrut(20));

        JLabel metroLabel = new JLabel("Metropolis: ");
        metropolisInput = new JTextField(15);
        box.add(metroLabel);
        box.add(metropolisInput);
        box.add(Box.createHorizontalStrut(20));

        JLabel contLabel = new JLabel("Continent: ");
        continentInput = new JTextField(15);
        box.add(contLabel);
        box.add(continentInput);
        box.add(Box.createHorizontalStrut(20));

        JLabel popLabel = new JLabel("Population: ");
        populationInput = new JTextField(15);
        box.add(popLabel);
        box.add(populationInput);
        box.add(Box.createHorizontalStrut(20));

        add(box, BorderLayout.NORTH);
    }

    private void addSQLTable() {
        JTableHeader header = frameTable.getTableHeader();
        TableColumnModel colMod = header.getColumnModel();
        for(int i = 0; i<frameTable.getColumnCount(); i++){
            String str = sqlTable.getHeaderNameAt(i);
            str = str.substring(0, 1).toUpperCase() + str.substring(1);
            colMod.getColumn(i).setHeaderValue(str);
        }
        header.repaint();
        frameTable.setTableHeader(header);
        JScrollPane jsp = new JScrollPane(frameTable);
        add(jsp, BorderLayout.CENTER);
    }

    private void addRightBox() {
        Box boxButtons = new Box(BoxLayout.Y_AXIS);
        //JPanel boxButtons = new JPanel(new BorderLayout());
        Box boxCombos = new Box(BoxLayout.Y_AXIS);

        addBtn = new JButton("Add");
        searchBtn = new JButton("Search");
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        boxButtons.add(addBtn);
        boxButtons.add(searchBtn);

        boxCombos.setBorder(new TitledBorder("Search Options"));
        populationDrop = new JComboBox();
        populationDrop.addItem("Population Larger Than");
        populationDrop.addItem("Population Less or Equal");

        matchDrop = new JComboBox();
        matchDrop.addItem("Exact Match");
        matchDrop.addItem("Partial Match");
        boxCombos.add(populationDrop);
        boxCombos.add(matchDrop);
        boxButtons.add(boxCombos);

        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.add(boxButtons, BorderLayout.NORTH);

        JPanel boxPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(boxPanel, BoxLayout.Y_AXIS);
        boxPanel.setLayout(boxLayout);
        boxPanel.add(panel1);
        add(boxPanel, BorderLayout.EAST);
    }

    private void searchPerformed() {
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long population = -1;
                String metropolis = "";
                String continent = "";

                if(populationInput.getText().length() != 0 && Long.parseLong(populationInput.getText()) > -1){
                    population = Long.parseLong(populationInput.getText());
                }
                if(metropolisInput.getText().length() != 0){
                    metropolis = metropolisInput.getText();
                }
                if(continentInput.getText().length() != 0){
                    continent = continentInput.getText();
                }

                String population_filter = (String) populationDrop.getSelectedItem();
                String else_filter = (String) matchDrop.getSelectedItem();
                sqlTable.search(metropolis, continent, population, population_filter, else_filter);
            }
        });
    }

    public void addPerformed(){
        addBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                long population = population = Long.parseLong(populationInput.getText());
                if(population < 0) population = 0;
                sqlTable.insert(metropolisInput.getText(), continentInput.getText(), population);
            }
        });
    }

    public static void main(String[] args) {
        // GUI Look And Feel
        // Do this incantation at the start of main() to tell Swing
        // to use the GUI LookAndFeel of the native platform. It's ok
        // to ignore the exception.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        SQLFrame frame = new SQLFrame();
    }
}
