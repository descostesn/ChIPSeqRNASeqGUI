package ReinbergCore;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.StyledEditorKit.ForegroundAction;



public class reinbergWindow extends JFrame{

	private JPanel container = new JPanel();
	private JFrame frame;
	
	private JLabel sequenceFolderPath = new JLabel("");
	private JLabel sequenceFolderLabel = new JLabel("Fastq folder:");
	private JButton sequenceFolderButton = new JButton("Select");
	
	private JLabel nameLabel = new JLabel("Analysis name:");
	private JLabel nameField = new JLabel("");
	
	private JRadioButton chipseqButton = new JRadioButton("Chip-seq");
	private JRadioButton rnaseqButton = new JRadioButton("RNA-seq");
	private ButtonGroup bgChIPRNA = new ButtonGroup();
	
	private JLabel organismLabel = new JLabel("Organism");
	private JLabel genomeLabel = new JLabel("Genome");
	String[] organismNames = {"human", "mouse"};
	private JComboBox<String> organismCombo = new JComboBox<String>(organismNames);
	String[] genomeNames = {"hg19"};
	private JComboBox<String> genomeCombo = new JComboBox<String>(genomeNames);
	
	private JRadioButton singleButton = new JRadioButton("Single-ended");
	private JRadioButton pairedButton = new JRadioButton("Paired-ended");
	private ButtonGroup bgEnded = new ButtonGroup();
	
	private JPanel fragmentSizePane = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JTextField fragmentSize = new JTextField();
	private JLabel fragmentSizeName = new JLabel("Fragment Size");
	
	private JLabel outputOptionsLabel = new JLabel("Output Options:");
	private JCheckBox wigFixed = new JCheckBox("Wig Fixed");
	private JCheckBox wigVariable = new JCheckBox("Wig Variable");
	private JCheckBox spikedIn = new JCheckBox("Spiked-in");
	
	private JLabel minNbCPULabel = new JLabel("Min nb CPU:");
	private JLabel maxNbCPULabel = new JLabel("Max nb CPU");
	private JComboBox minNbCPU = new JComboBox();
	private JComboBox maxNbCPU = new JComboBox();
	private int maxCPU = 20;
	
	private JButton submitButton = new JButton("Submit");
	private JLabel paramLabel = new JLabel("");
	private JLabel writingFileLabel = new JLabel("");
	private JLabel gridCommand = new JLabel("");
	private String currentDir = System.getProperty("user.dir");
	
	public reinbergWindow(){
		this.setTitle("Reinberg Core Facility");
		this.setSize(700,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		
		// Define two police styles
		Font police = new Font("Arial", Font.PLAIN, 12);
		Font police2 = new Font("Arial", Font.BOLD, 12);
		
		//Creating the field and button for selecting the folder to work with
		JPanel selectFolderPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		sequenceFolderPath.setFont(police);
		sequenceFolderLabel.setFont(police2);
		sequenceFolderPath.setPreferredSize(new Dimension(500, 20));
		sequenceFolderPath.setBorder(BorderFactory.createLineBorder(Color.black));
		sequenceFolderPath.setBackground(Color.white);
		selectFolderPane.setBackground(Color.white);
		sequenceFolderButton.setFont(police2);
		sequenceFolderButton.addActionListener(new SelectFolderListener());
		selectFolderPane.add(sequenceFolderLabel);
		selectFolderPane.add(sequenceFolderPath);
		selectFolderPane.add(sequenceFolderButton);
		
		// Creating a field to enter the analysis name
		JPanel analysisNamePane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		analysisNamePane.setBackground(Color.white);
		nameLabel.setFont(police2);
		nameField.setPreferredSize(new Dimension(200, 20));
		nameField.setBackground(Color.white);
		nameField.setFont(police);
		nameField.setBorder(BorderFactory.createLineBorder(Color.black));
		analysisNamePane.add(nameLabel);
		analysisNamePane.add(nameField);
				
		//Creating a two choices panel to say if experiments are ChIP-seq or RNA-seq
		JPanel experimentTypePane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		experimentTypePane.setBackground(Color.white);
		chipseqButton.setFont(police2);
		rnaseqButton.setFont(police2);
		chipseqButton.addActionListener(new ChipseqRNAseqButtonListener());
		rnaseqButton.addActionListener(new ChipseqRNAseqButtonListener());
		chipseqButton.setBackground(Color.white);
		rnaseqButton.setBackground(Color.white);
		bgChIPRNA.add(chipseqButton);
		bgChIPRNA.add(rnaseqButton);
		experimentTypePane.add(chipseqButton);
		experimentTypePane.add(rnaseqButton);
		
		//Creating panel enabling to choose organism and genome
		JPanel organismGenomePane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		organismGenomePane.setBackground(Color.white);
		genomeLabel.setFont(police2);
		organismLabel.setFont(police2);
		organismCombo.addItemListener(new GenomeListener());
		organismGenomePane.add(organismLabel);
		organismGenomePane.add(organismCombo);
		organismGenomePane.add(genomeLabel);
		organismGenomePane.add(genomeCombo);
		
		//Panel to select if experiments are single or paired-ended
		JPanel singlePairedPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		singlePairedPane.setBackground(Color.white);
		singleButton.setFont(police2);
		pairedButton.setFont(police2);
		singleButton.setBackground(Color.white);
		pairedButton.setBackground(Color.white);
		bgEnded.add(singleButton);
		bgEnded.add(pairedButton);
		singlePairedPane.add(singleButton);
		singlePairedPane.add(pairedButton);
		
		// Creating a field to enter the fragment size: The JPanel is declared at the top to modify its visibility
		fragmentSizePane.setBackground(Color.white);
		fragmentSize.setFont(police);
		fragmentSize.setPreferredSize(new Dimension(50, 20));
		fragmentSize.setBackground(Color.white);
		fragmentSizeName.setFont(police2);
		fragmentSizeName.setBackground(Color.white);
		fragmentSizePane.add(fragmentSizeName);
		fragmentSizePane.add(fragmentSize);
		fragmentSizePane.setVisible(false);
		
		//Creating panel for output options
		JPanel outputOptions = new JPanel(new FlowLayout(FlowLayout.LEFT));
		outputOptions.setBackground(Color.white);
		outputOptionsLabel.setFont(police2);
		wigFixed.setFont(police);
		wigVariable.setFont(police);
		spikedIn.setFont(police);
		outputOptionsLabel.setBackground(Color.white);
		wigFixed.setBackground(Color.white);
		wigVariable.setBackground(Color.white);
		spikedIn.setBackground(Color.white);
		wigFixed.setSelected(true);
		outputOptions.add(outputOptionsLabel);
		outputOptions.add(wigFixed);
		outputOptions.add(wigVariable);
		outputOptions.add(spikedIn);
		
		//Panel for choosing nb of CPU
		JPanel cpuNbPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		cpuNbPane.setBackground(Color.white);
		minNbCPULabel.setFont(police2);
		maxNbCPULabel.setFont(police2);
		minNbCPULabel.setBackground(Color.white);
		maxNbCPULabel.setBackground(Color.white);
		for (int i = 1; i <= maxCPU; i++){
			minNbCPU.addItem(i);
			maxNbCPU.addItem(i);
		}
		minNbCPU.addActionListener(new MinCPUListener());
		cpuNbPane.add(minNbCPULabel);
		cpuNbPane.add(minNbCPU);
		cpuNbPane.add(maxNbCPULabel);
		cpuNbPane.add(maxNbCPU);
		
		//Panel for the submit button
		JPanel submitPane = new JPanel();
		submitPane.setBackground(Color.white);
		submitButton.setBackground(Color.white);
		submitButton.setFont(police2);
		submitButton.addActionListener(new SubmitListener());
		submitPane.add(submitButton);
		
		//Pane to print the parameters
		JPanel paramPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		paramPane.setBackground(Color.white);
		paramLabel.setBackground(Color.white);
		paramLabel.setFont(police2);
		paramPane.add(paramLabel);
		
		//Pane indicating the file written
		JPanel fileWritenPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		fileWritenPane.setBackground(Color.white);
		writingFileLabel.setBackground(Color.white);
		writingFileLabel.setFont(police2);
		fileWritenPane.add(writingFileLabel);
		
		//Pane to indicate the grid command
		JPanel gridPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		gridPane.setBackground(Color.white);
		gridCommand.setBackground(Color.white);
		gridCommand.setFont(police2);
		gridPane.add(gridCommand);
		
		//Placing all JPanels on main container
		container.setBackground(Color.white);
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		container.add(selectFolderPane);
		container.add(analysisNamePane);
		container.add(experimentTypePane);
		container.add(organismGenomePane);
		container.add(singlePairedPane);
		container.add(fragmentSizePane);
		container.add(outputOptions);
		container.add(cpuNbPane);
		container.add(submitPane);
		container.add(paramPane);
		container.add(fileWritenPane);
		container.add(gridPane);
		this.setContentPane(container);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	//Define class to select the input folder
	class SelectFolderListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			
			final JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(reinbergWindow.this);
			
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			if(returnVal == JFileChooser.APPROVE_OPTION){
				File result = fc.getSelectedFile();
				
				if(!result.isDirectory())
					result = result.getParentFile();
				
				String folderPath = result.getAbsolutePath();
				sequenceFolderPath.setText(folderPath);
				
				String[] parts = folderPath.split("/");
				nameField.setText(parts[parts.length-1]);
			}
		}
	}
	
	//Define class to modify the genome combo according to the organism selected
	class GenomeListener implements ItemListener{
		
		public void itemStateChanged(ItemEvent event){
				
			String[] replaceGenomeNames;
			
			if(event.getStateChange() == ItemEvent.SELECTED){
				
				if(organismCombo.getSelectedItem() == "human")
					replaceGenomeNames = new String[]{"hg19"};
				else
					replaceGenomeNames = new String[]{"mm10"};
					
				DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(replaceGenomeNames);
				model.removeAllElements();
				
				for(String item : replaceGenomeNames)
					model.addElement(item);
				genomeCombo.setModel(model);
			}
		}
	}

	class ChipseqRNAseqButtonListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == chipseqButton)
				fragmentSizePane.setVisible(true);
			else
				fragmentSizePane.setVisible(false);
		}
	}

	class MinCPUListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			
			for(int i = 2; i <= (int) minNbCPU.getSelectedItem();i++)
				maxNbCPU.removeItemAt(0);
		}
	}
	
	class SubmitListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e){
			
			//Checking that all parameters have been defined
			if(sequenceFolderPath.getText() == "")
				JOptionPane.showMessageDialog(frame, "Select a folder containing fastq files", "Missing parameter", JOptionPane.ERROR_MESSAGE);
			else if(!chipseqButton.isSelected() && !rnaseqButton.isSelected())
				JOptionPane.showMessageDialog(frame, "Select chip-seq or rna-seq option", "Missing parameter", JOptionPane.ERROR_MESSAGE);
			else if(!singleButton.isSelected() && !pairedButton.isSelected())
				JOptionPane.showMessageDialog(frame, "Select single-ended or paired-ended option", "Missing parameter", JOptionPane.ERROR_MESSAGE);
			else if(!wigFixed.isSelected() && !wigVariable.isSelected())
				JOptionPane.showMessageDialog(frame, "Select at least Wig Fixed or Wig variable", "Missing parameter", JOptionPane.ERROR_MESSAGE);
			else if(chipseqButton.isSelected() && fragmentSize.getText().equals(""))
				JOptionPane.showMessageDialog(frame, "Enter fragment size", "Missing parameter", JOptionPane.ERROR_MESSAGE);
			else{
				
				String chipseq = new String();
				if(chipseqButton.isSelected()) chipseq = "TRUE"; else chipseq = "FALSE";
				String singleOrNot = new String();
				if(singleButton.isSelected()) singleOrNot = "TRUE"; else singleOrNot = "FALSE";
				String wigfixedChoice = "FALSE";
				if(wigFixed.isSelected()) wigfixedChoice = "TRUE";
				String wigvariableChoice = "FALSE";
				if(wigVariable.isSelected()) wigvariableChoice = "TRUE";
				String spikeinChoice = "FALSE";
				if(spikedIn.isSelected()) spikeinChoice = "TRUE";
				
				String fileParam = sequenceFolderPath.getText() + ";" + chipseq + ";" + genomeCombo.getSelectedItem() + ";" 
				                   + organismCombo.getSelectedItem() + ";" + singleOrNot + ";" + fragmentSize.getText() + ";" 
				                   + wigfixedChoice + ";" + wigvariableChoice + ";" + spikeinChoice + ";" + nameField.getText() 
				                   + ";" + minNbCPU.getSelectedItem() + ";" + maxNbCPU.getSelectedItem();
				
				//Writing parameters to file
				try{
					BufferedWriter writer = new BufferedWriter(new FileWriter(nameField.getText() + ".conf"));
					writer.write(fileParam);
					writer.close();
				}catch(IOException eWrite){
					eWrite.printStackTrace();
				}
				
				//Pause the program for 2 second
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				//Loading command on grid
				String optionCWD1 = "#!/bin/bash\n";
				String optionCWD2 = "#$ -S /bin/bash\n";
				String optionCWD3 = "#$ -cwd\n";
				
				String args = "qsub -q all.q -pe threaded " + minNbCPU.getSelectedItem() + "-" + maxNbCPU.getSelectedItem() + " -t 1-1 " + " -N " + nameField.getText() + " " + currentDir + "/pipeline_fastq_to_bigwigs_submission.sh " + currentDir + "/" + nameField.getText() + ".conf";
				
				//Writing the command to a bash file
				try{
					BufferedWriter writer2 = new BufferedWriter(new FileWriter("tmp-command.sh"));
					writer2.write(optionCWD1);
					writer2.write(optionCWD2);
					writer2.write(optionCWD3);
					writer2.write(args);
					writer2.close();
				}catch(IOException eWrite){
					eWrite.printStackTrace();
				}
				
				//Pause the program for 2 second
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}

				try{
					ProcessBuilder procB = new ProcessBuilder("qsub", "tmp-command.sh");
					procB.directory(new File(currentDir));
					procB.redirectInput(new File(currentDir + "/" + "tmp-command.sh"));
					Process proc = procB.start(); 
				}catch(IOException eGrid){
					eGrid.printStackTrace();
				}
				
				
				paramLabel.setText("Parameters: " + fileParam);
				writingFileLabel.setText("Writing " + nameField.getText() + ".conf");
				gridCommand.setText("Loading command on grid: qsub pipeline_fastq_to_bigwigs_submission.sh " + nameField.getText() + ".conf");
				
			}
		}
	}	
}
