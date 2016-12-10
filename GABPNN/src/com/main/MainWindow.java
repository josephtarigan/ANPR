/*
 * By Joseph joseph dot tarigan at gmail dot com on GEMINI
 */
package com.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.main.uiobserver.UIObserver;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1111695288236562200L;

	/*
	 * enumerator for status
	 */
	private static enum trainerStatuses {
		NOTREADY("Not Ready"), READY("Ready"), TRAINING("Training"), WRITING("Writing"), PAUSED("Paused"), FINISHED("Finished"), PARENTSELECTION("Selecting Parents"), SURVIVORSELECTION("Selecting Survivors"), CROSSOVER("Cross Overing"), MUTATION("Mutating"), GENERATING("Generating Individuals");
		private String value;
		
		private trainerStatuses(String s){
			this.value = s;
		}
		
		public String toString(){
			return this.value;
		}
	}
	
	/*
	 * panes
	 */
	private JPanel contentPane;
	private JPanel northPane;
	private JPanel centerPane;
	private JPanel southPane;
	
	private JPanel northNBorderLayoutContainer;
	private JPanel northCBorderLayoutContainer;
	private JPanel northSBorderLayoutContainer;
	
	private JPanel centerWGridContainer;
	private JPanel centerEGridContainer;
	private JPanel centerWNBorderLayoutContainer;
	private JPanel centerWCBorderLayoutContainer;
	private JPanel centerWSBorderLayoutContainer;
	private JPanel centerENBorderLayoutContainer;
	//private JPanel centerECBorderLayoutContainer;
	
	private JTabbedPane southTPaneContainer;
	private JScrollPane southCPopulationScrollPaneContainer;
	private JScrollPane southCMatingPoolScrollPaneContainer;
	private JScrollPane southCOffspringScrollPaneContainer;
	private JPanel populationIndividualGridContainer;
	private JPanel matingPoolIndividualGridContainer;
	private JPanel offspringIndividualGridContainer;
	public Component populationIndividualContainer[];
	public Component matingPoolIndividualContainer[];
	public Component offspringIndividualContainer[];
	
	/*
	 * labels
	 */
	private JLabel statusState;
	private JLabel statusLabel;
	private JLabel maxGenerationLabel;
	private JLabel maxGenerationValue;
	private JLabel generationLabel;
	private JLabel generationValue;
	private JLabel saveFilePathLabel;
	private JLabel saveFilePathValue;
	private JLabel elapsedTimeLabel;
	private JLabel elapsedTimeValue;
	private JLabel individualInEvaluateLabel;
	private JLabel individualInEvaluateValue;
	
	/*
	 * buttons
	 */
	private JButton startEvolutionButton;
	//private JButton pauseEvolutionButton;
	private JButton browseSaveFilePathButton;
	
	/*
	 * text box
	 */
	public static JTextField populationGenotype[][];
	public static JTextField populationPhenotype[][];
	public static JTextField populationFitness[];
	public static JTextField matingPoolGenotype[][];
	public static JTextField matingPoolPhenotype[][];
	public static JTextField matingPoolFitness[];
	public static JTextField offspringGenotype[][];
	public static JTextField offspringPhenotype[][];
	public static JTextField offspringFitness[];
	
	/*
	 * save file path
	 * for weight data
	 */
	private static String saveFilePath = new String();
	
	/*
	 * GA operator
	 */
	private static GABPNN gabpnn = new GABPNN();
	
	/*
	 * observer
	 */
	private static GAUIObserver gaUIObserver;
	private static GABPNNUIObserver gabpnnUIObserver;
	
	/*
	 * threads
	 */
	private static Thread gaThread;
	
	/*
	 * execution time
	 */
	private static long startTime, endTime, elapsedTime;
	private static long second, minute, hour;
	
	/*
	 * running boolean
	 */
	private static boolean isRunning = false;
	
	/*
	 * timer
	 */
	private static Timer timer;
	private static TimerTask timerTask;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		
		/*
		 * initialisation =========================================================================
		 */
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 800, 600);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		this.setTitle("GABPNN Trainer");
		
		/*
		 * ========================================================================================
		 */
		
		/*
		 * north pane =============================================================================
		 */
		
		northPane = new JPanel();
		northPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		northPane.setLayout(new GridLayout(3, 1, 0, 0));
		
		/*
		 * north north
		 */
		northNBorderLayoutContainer = new JPanel();
		northNBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
		northNBorderLayoutContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		statusLabel = new JLabel("Status");
		statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		statusState = new JLabel(trainerStatuses.NOTREADY.toString());
		statusState.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		northNBorderLayoutContainer.add(statusLabel, BorderLayout.WEST);
		northNBorderLayoutContainer.add(statusState, BorderLayout.CENTER);
		
		/*
		 * north center
		 */
		northCBorderLayoutContainer = new JPanel();
		northCBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
		northCBorderLayoutContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		maxGenerationLabel = new JLabel("Max Generation");
		maxGenerationLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		maxGenerationValue = new JLabel(String.valueOf(Config.MAX_GENERATION));
		maxGenerationValue.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		northCBorderLayoutContainer.add(maxGenerationLabel, BorderLayout.WEST);
		northCBorderLayoutContainer.add(maxGenerationValue, BorderLayout.CENTER);
		
		/*
		 * north south
		 */
		northSBorderLayoutContainer = new JPanel();
		northSBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
		northSBorderLayoutContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		saveFilePathLabel = new JLabel("Output Directory");
		saveFilePathLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		saveFilePathValue = new JLabel("None");
		saveFilePathValue.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		browseSaveFilePathButton = new JButton("Browse");
		browseSaveFilePathButton.addActionListener(new SaveDialog());
		
		northSBorderLayoutContainer.add(saveFilePathLabel, BorderLayout.WEST);
		northSBorderLayoutContainer.add(saveFilePathValue, BorderLayout.CENTER);
		northSBorderLayoutContainer.add(browseSaveFilePathButton, BorderLayout.EAST);
		
		northPane.add(northNBorderLayoutContainer);
		northPane.add(northCBorderLayoutContainer);
		northPane.add(northSBorderLayoutContainer);
		contentPane.add(northPane, BorderLayout.NORTH);
		
		/*
		 * ========================================================================================
		 */
		
		/*
		 * center pane ============================================================================
		 */
		
		centerPane = new JPanel();
		centerPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		centerPane.setLayout(new BorderLayout());
		
		/*
		 * center west
		 */
		centerWGridContainer = new JPanel();
		centerWGridContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		centerWGridContainer.setLayout(new GridLayout(3, 1, 5, 5));
		centerWGridContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		/*
		 * center west north
		 */
		centerWNBorderLayoutContainer = new JPanel();
		centerWNBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
		centerWNBorderLayoutContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		generationLabel = new JLabel("Generation");
		generationLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		generationValue = new JLabel("1");
		generationValue.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		centerWNBorderLayoutContainer.add(generationLabel, BorderLayout.WEST);
		centerWNBorderLayoutContainer.add(generationValue, BorderLayout.CENTER);
		
		/*
		 * center west center
		 */
		centerWCBorderLayoutContainer = new JPanel();
		centerWCBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
		centerWCBorderLayoutContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		elapsedTimeLabel = new JLabel("Elapsed Time");
		elapsedTimeLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		elapsedTimeValue = new JLabel("00:00:00");
		elapsedTimeValue.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		centerWCBorderLayoutContainer.add(elapsedTimeLabel, BorderLayout.WEST);
		centerWCBorderLayoutContainer.add(elapsedTimeValue, BorderLayout.CENTER);
		
		/*
		 * center west south
		 */
		centerWSBorderLayoutContainer = new JPanel();
		centerWSBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
		centerWSBorderLayoutContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		individualInEvaluateLabel = new JLabel("Individual in Evaluate");
		individualInEvaluateLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		individualInEvaluateValue = new JLabel("None");
		individualInEvaluateValue.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		centerWSBorderLayoutContainer.add(individualInEvaluateLabel, BorderLayout.WEST);
		centerWSBorderLayoutContainer.add(individualInEvaluateValue, BorderLayout.CENTER);
		
		/*
		 * center east
		 */
		centerEGridContainer = new JPanel();
		centerEGridContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		centerEGridContainer.setLayout(new GridLayout(1, 1, 5, 5));
		centerEGridContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		/*
		 * center east north
		 */
		
		centerENBorderLayoutContainer = new JPanel();
		centerENBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
		centerENBorderLayoutContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		startEvolutionButton = new JButton("Start Process");
		startEvolutionButton.addActionListener(new StartProcessListener());
		
		centerENBorderLayoutContainer.add(startEvolutionButton, BorderLayout.CENTER);
		
		/*
		 * center east center
		 */
		//centerECBorderLayoutContainer = new JPanel();
		//centerECBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
		//centerECBorderLayoutContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		//pauseEvolutionButton = new JButton("Pause Process");
		
		//centerECBorderLayoutContainer.add(pauseEvolutionButton, BorderLayout.CENTER);
		
		//
		centerWGridContainer.add(centerWNBorderLayoutContainer);
		centerWGridContainer.add(centerWCBorderLayoutContainer);
		centerWGridContainer.add(centerWSBorderLayoutContainer);
		
		centerEGridContainer.add(centerENBorderLayoutContainer);
		//centerEGridContainer.add(centerECBorderLayoutContainer);
		
		centerPane.add(centerWGridContainer, BorderLayout.WEST);
		centerPane.add(centerEGridContainer, BorderLayout.EAST);
		contentPane.add(centerPane, BorderLayout.CENTER);
		
		/*
		 * ========================================================================================
		 */
		
		/*
		 * south pane =============================================================================
		 */
		
		southPane = new JPanel();
		southPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		southPane.setLayout(new BorderLayout());
		
		southTPaneContainer = new JTabbedPane();
		
		populationIndividualContainer = new PopulationIndividualContainer[50];
		matingPoolIndividualContainer = new MatingPoolIndividualContainer[10];
		offspringIndividualContainer = new OffspringIndividualContainer[10];
		
		populationIndividualGridContainer = new JPanel();
		populationIndividualGridContainer.setLayout(new GridLayout(50, 1));
		populationIndividualGridContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		matingPoolIndividualGridContainer = new JPanel();
		matingPoolIndividualGridContainer.setLayout(new GridLayout(10, 1));
		matingPoolIndividualGridContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		offspringIndividualGridContainer = new JPanel();
		offspringIndividualGridContainer.setLayout(new GridLayout(10, 1));
		offspringIndividualGridContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		populationGenotype = new JTextField[Config.MAX_GENERATION][3];
		populationPhenotype = new JTextField[Config.MAX_GENERATION][3];
		populationFitness = new JTextField[Config.MAX_GENERATION];
		
		matingPoolGenotype = new JTextField[Config.MATING_POOL_SIZE][3];
		matingPoolPhenotype = new JTextField[Config.MATING_POOL_SIZE][3];
		matingPoolFitness = new JTextField[Config.MATING_POOL_SIZE];

		offspringGenotype = new JTextField[Config.MATING_POOL_SIZE][3];
		offspringPhenotype = new JTextField[Config.MATING_POOL_SIZE][3];
		offspringFitness = new JTextField[Config.MATING_POOL_SIZE];
		
		/*
		 * build individual container
		 */
		for(int i=0; i<Config.POPULATION_SIZE; i++){
			
			populationIndividualContainer[i] = new PopulationIndividualContainer(i);
			populationIndividualGridContainer.add(populationIndividualContainer[i]);
		}
		
		/*
		 * build mating pool individual container
		 */
		for(int i=0; i<Config.MATING_POOL_SIZE; i++){
			
			matingPoolIndividualContainer[i] = new MatingPoolIndividualContainer(i);
			matingPoolIndividualGridContainer.add(matingPoolIndividualContainer[i]);
		}
		
		/*
		 * build offspring individual container
		 */
		for(int i=0; i<Config.MATING_POOL_SIZE; i++){
			
			offspringIndividualContainer[i] = new OffspringIndividualContainer(i);
			offspringIndividualGridContainer.add(offspringIndividualContainer[i]);
		}
		
		southCPopulationScrollPaneContainer = new JScrollPane(populationIndividualGridContainer);
		southCPopulationScrollPaneContainer.setPreferredSize(new Dimension(800, 330));
		
		southCMatingPoolScrollPaneContainer = new JScrollPane(matingPoolIndividualGridContainer);
		southCMatingPoolScrollPaneContainer.setPreferredSize(new Dimension(800, 330));
		
		southCOffspringScrollPaneContainer = new JScrollPane(offspringIndividualGridContainer);
		southCOffspringScrollPaneContainer.setPreferredSize(new Dimension(800, 330));
		
		southTPaneContainer.addTab("Population", southCPopulationScrollPaneContainer);
		southTPaneContainer.addTab("Mating Pool", southCMatingPoolScrollPaneContainer);
		southTPaneContainer.addTab("Offsprings", southCOffspringScrollPaneContainer);

		southPane.add(southTPaneContainer, BorderLayout.CENTER);
		contentPane.add(southPane, BorderLayout.SOUTH);
		
		IndividualData.loadPopulation();
		
		/*
		 * ========================================================================================
		 */
		
		/*
		 * add observer
		 */
		gabpnn.geneticAlgorithm.addObserver(gaUIObserver = new GAUIObserver());
		gabpnn.addObserver(gabpnnUIObserver = new GABPNNUIObserver());
	}
	
	/*
	 * timer thread
	 */
	class TimeObserver extends TimerTask{

		@Override
		public void run() {
			
			elapsedTime = System.currentTimeMillis() - startTime;
			
			//second
			second = (elapsedTime/1000) % 60;
			
			//minute
			minute = (elapsedTime/(1000 * 60)) % 60;
			
			//hour
			hour = (elapsedTime/(1000 * 60 * 60));
			
			//update the label
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					
					/*
					 * add 0, jika dibawah dua digit
					 */
					String s, m, h;
					
					s=m=h="";
					
					if(second < 9){
						
						s = "0"+String.valueOf(second);
					}
					else{
						
						s = String.valueOf(second);
					}
					
					if(minute < 9){
						
						m = "0"+String.valueOf(minute);
					}
					else{
						
						m = String.valueOf(minute);
					}
					
					if(hour < 9){
						
						h = "0"+String.valueOf(hour);
					}
					else{
						
						h = String.valueOf(hour);
					}
					
					elapsedTimeValue.setText(String.format("%s:%s:%s", h, m, s));
				}
			});
			
			/*
			 * check gabpnn for finish states
			 * stop timer
			 */
			if(gabpnn.getStatus().equals("Finished")){
				
				isRunning = false;
				timer.cancel();
			}
		}
	}
	
	/*
	 * individualContainer class
	 */
	class PopulationIndividualContainer extends JPanel{

		/**
		 * 
		 */
		private static final long serialVersionUID = -3191979503741521979L;
		/*
		 * pane
		 */
		public JPanel masterBorderLayoutContainer;
		public JPanel labelBorderLayoutContainer;
		public JPanel descriptionBorderLayoutContainer;
		
		public JPanel descriptionWContainer;
		public JPanel descriptionCContainer;
		public JPanel fitnessBorderLayoutContainer;
		
		public JPanel gp1;
		public JPanel gp2;
		public JPanel gp3;
		
		/*
		 * label
		 */
		public JLabel individualLabel;
		public JLabel genotypeLabel;
		public JLabel phenotypeLabel;
		public JLabel fitnessLabel;
		
		public int number;
		
		public PopulationIndividualContainer(int i){
			
			number = i+1;
			masterBorderLayoutContainer = new JPanel();
			masterBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
			masterBorderLayoutContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			masterBorderLayoutContainer.setPreferredSize(new Dimension(730, 140));
			
			/*
			 * label
			 */
			labelBorderLayoutContainer = new JPanel();
			labelBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
			labelBorderLayoutContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
			
			individualLabel = new JLabel("Individual " + number);
			individualLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
			
			labelBorderLayoutContainer.add(individualLabel, BorderLayout.EAST);
			
			/*
			 * description
			 */
			descriptionBorderLayoutContainer = new JPanel();
			descriptionBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
			descriptionBorderLayoutContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
			
			/*
			 * description west
			 */
			descriptionWContainer = new JPanel();
			descriptionWContainer.setLayout(new GridLayout(3, 1));
			descriptionWContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
			
			genotypeLabel = new JLabel("Genotype");
			phenotypeLabel = new JLabel("Phenotype");
			
			descriptionWContainer.add(new JLabel(" "));
			descriptionWContainer.add(genotypeLabel, BorderLayout.NORTH);
			descriptionWContainer.add(phenotypeLabel, BorderLayout.CENTER);
			
			/*
			 * description center
			 */
			
			descriptionCContainer = new JPanel();
			descriptionCContainer.setLayout(new GridLayout(1, 3));
			descriptionCContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
			
			/*
			 * genotype phenotype
			 */
			
			//1
			gp1 = new JPanel();
			gp1.setLayout(new GridLayout(3, 1));
			gp1.setBorder(new EmptyBorder(0, 5, 0, 5));
			
			//2
			gp2 = new JPanel();
			gp2.setLayout(new GridLayout(3, 1));
			gp2.setBorder(new EmptyBorder(0, 5, 0, 5));
			
			//3
			gp3 = new JPanel();
			gp3.setLayout(new GridLayout(3, 1));
			gp3.setBorder(new EmptyBorder(0, 5, 0, 5));
			
			/*
			 * genotype
			 */
			//1
			populationGenotype[i][0] = new JTextField("00000000");
			populationGenotype[i][0].setEditable(false);
			populationGenotype[i][0].setBorder(null);
			gp1.add(new JLabel("Hidden Neuron"));
			gp1.add(populationGenotype[i][0], BorderLayout.CENTER);
			//2
			populationGenotype[i][1] = new JTextField("000000000");
			populationGenotype[i][1].setEditable(false);
			populationGenotype[i][1].setBorder(null);
			gp2.add(new JLabel("Learning Rate"));
			gp2.add(populationGenotype[i][1], BorderLayout.CENTER);
			//3
			populationGenotype[i][2] = new JTextField("000000000");
			populationGenotype[i][2].setEditable(false);
			populationGenotype[i][2].setBorder(null);
			gp3.add(new JLabel("Momentum Rate"));
			gp3.add(populationGenotype[i][2], BorderLayout.CENTER);
			
			/*
			 * phenotype
			 */
			//1
			populationPhenotype[i][0] = new JTextField("0");
			populationPhenotype[i][0].setEditable(false);
			populationPhenotype[i][0].setBorder(null);
			gp1.add(populationPhenotype[i][0], BorderLayout.SOUTH);
			//2
			populationPhenotype[i][1] = new JTextField("0");
			populationPhenotype[i][1].setEditable(false);
			populationPhenotype[i][1].setBorder(null);
			gp2.add(populationPhenotype[i][1], BorderLayout.SOUTH);
			//3
			populationPhenotype[i][2] = new JTextField("0");
			populationPhenotype[i][2].setEditable(false);
			populationPhenotype[i][2].setBorder(null);
			gp3.add(populationPhenotype[i][2], BorderLayout.SOUTH);
			
			descriptionCContainer.add(gp1);
			descriptionCContainer.add(gp2);
			descriptionCContainer.add(gp3);
			
			/*
			 * fitness
			 */
			fitnessBorderLayoutContainer = new JPanel();
			fitnessBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
			fitnessBorderLayoutContainer.setBorder(new EmptyBorder(0, 10, 10, 15));
			
			populationFitness[i] = new JTextField("0");
			populationFitness[i].setEditable(false);
			fitnessLabel = new JLabel("Fitness");
			fitnessBorderLayoutContainer.add(fitnessLabel, BorderLayout.WEST);
			fitnessBorderLayoutContainer.add(populationFitness[i], BorderLayout.CENTER);
			
			descriptionCContainer.add(fitnessBorderLayoutContainer, BorderLayout.SOUTH);
			
			descriptionBorderLayoutContainer.add(descriptionWContainer, BorderLayout.WEST);
			descriptionBorderLayoutContainer.add(descriptionCContainer, BorderLayout.CENTER);
			
			
			masterBorderLayoutContainer.add(labelBorderLayoutContainer, BorderLayout.NORTH);
			masterBorderLayoutContainer.add(descriptionBorderLayoutContainer, BorderLayout.CENTER);
			masterBorderLayoutContainer.add(fitnessBorderLayoutContainer, BorderLayout.SOUTH);
			this.add(masterBorderLayoutContainer);
			this.setBorder(new EmptyBorder(5, 5, 5, 5));
		}
	}
	
	/*
	 * mating pool Container class
	 */
	class MatingPoolIndividualContainer extends JPanel{

		/**
		 * 
		 */
		private static final long serialVersionUID = -3191979503741521979L;
		/*
		 * pane
		 */
		public JPanel masterBorderLayoutContainer;
		public JPanel labelBorderLayoutContainer;
		public JPanel descriptionBorderLayoutContainer;
		
		public JPanel descriptionWContainer;
		public JPanel descriptionCContainer;
		public JPanel fitnessBorderLayoutContainer;
		
		public JPanel gp1;
		public JPanel gp2;
		public JPanel gp3;
		
		/*
		 * label
		 */
		public JLabel individualLabel;
		public JLabel genotypeLabel;
		public JLabel phenotypeLabel;
		public JLabel fitnessLabel;
		
		public int number;
		
		public MatingPoolIndividualContainer(int i){
			
			number = i+1;
			masterBorderLayoutContainer = new JPanel();
			masterBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
			masterBorderLayoutContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			masterBorderLayoutContainer.setPreferredSize(new Dimension(730, 140));
			
			/*
			 * label
			 */
			labelBorderLayoutContainer = new JPanel();
			labelBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
			labelBorderLayoutContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
			
			individualLabel = new JLabel("Individual " + number);
			individualLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
			
			labelBorderLayoutContainer.add(individualLabel, BorderLayout.EAST);
			
			/*
			 * description
			 */
			descriptionBorderLayoutContainer = new JPanel();
			descriptionBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
			descriptionBorderLayoutContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
			
			/*
			 * description west
			 */
			descriptionWContainer = new JPanel();
			descriptionWContainer.setLayout(new GridLayout(3, 1));
			descriptionWContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
			
			genotypeLabel = new JLabel("Genotype");
			phenotypeLabel = new JLabel("Phenotype");
			
			descriptionWContainer.add(new JLabel(" "));
			descriptionWContainer.add(genotypeLabel, BorderLayout.NORTH);
			descriptionWContainer.add(phenotypeLabel, BorderLayout.CENTER);
			
			/*
			 * description center
			 */
			
			descriptionCContainer = new JPanel();
			descriptionCContainer.setLayout(new GridLayout(1, 3));
			descriptionCContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
			
			/*
			 * genotype phenotype
			 */
			
			//1
			gp1 = new JPanel();
			gp1.setLayout(new GridLayout(3, 1));
			gp1.setBorder(new EmptyBorder(0, 5, 0, 5));
			
			//2
			gp2 = new JPanel();
			gp2.setLayout(new GridLayout(3, 1));
			gp2.setBorder(new EmptyBorder(0, 5, 0, 5));
			
			//3
			gp3 = new JPanel();
			gp3.setLayout(new GridLayout(3, 1));
			gp3.setBorder(new EmptyBorder(0, 5, 0, 5));
			
			/*
			 * genotype
			 */
			//1
			matingPoolGenotype[i][0] = new JTextField("00000000");
			matingPoolGenotype[i][0].setEditable(false);
			matingPoolGenotype[i][0].setBorder(null);
			gp1.add(new JLabel("Hidden Neuron"));
			gp1.add(matingPoolGenotype[i][0], BorderLayout.CENTER);
			//2
			matingPoolGenotype[i][1] = new JTextField("000000000");
			matingPoolGenotype[i][1].setEditable(false);
			matingPoolGenotype[i][1].setBorder(null);
			gp2.add(new JLabel("Learning Rate"));
			gp2.add(matingPoolGenotype[i][1], BorderLayout.CENTER);
			//3
			matingPoolGenotype[i][2] = new JTextField("000000000");
			matingPoolGenotype[i][2].setEditable(false);
			matingPoolGenotype[i][2].setBorder(null);
			gp3.add(new JLabel("Momentum Rate"));
			gp3.add(matingPoolGenotype[i][2], BorderLayout.CENTER);
			
			/*
			 * phenotype
			 */
			//1
			matingPoolPhenotype[i][0] = new JTextField("0");
			matingPoolPhenotype[i][0].setEditable(false);
			matingPoolPhenotype[i][0].setBorder(null);
			gp1.add(matingPoolPhenotype[i][0], BorderLayout.SOUTH);
			//2
			matingPoolPhenotype[i][1] = new JTextField("0");
			matingPoolPhenotype[i][1].setEditable(false);
			matingPoolPhenotype[i][1].setBorder(null);
			gp2.add(matingPoolPhenotype[i][1], BorderLayout.SOUTH);
			//3
			matingPoolPhenotype[i][2] = new JTextField("0");
			matingPoolPhenotype[i][2].setEditable(false);
			matingPoolPhenotype[i][2].setBorder(null);
			gp3.add(matingPoolPhenotype[i][2], BorderLayout.SOUTH);
			
			descriptionCContainer.add(gp1);
			descriptionCContainer.add(gp2);
			descriptionCContainer.add(gp3);
			
			/*
			 * fitness
			 */
			fitnessBorderLayoutContainer = new JPanel();
			fitnessBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
			fitnessBorderLayoutContainer.setBorder(new EmptyBorder(0, 10, 10, 15));
			
			matingPoolFitness[i] = new JTextField("0");
			matingPoolFitness[i].setEditable(false);
			fitnessLabel = new JLabel("Fitness");
			fitnessBorderLayoutContainer.add(fitnessLabel, BorderLayout.WEST);
			fitnessBorderLayoutContainer.add(matingPoolFitness[i], BorderLayout.CENTER);
			
			descriptionCContainer.add(fitnessBorderLayoutContainer, BorderLayout.SOUTH);
			
			descriptionBorderLayoutContainer.add(descriptionWContainer, BorderLayout.WEST);
			descriptionBorderLayoutContainer.add(descriptionCContainer, BorderLayout.CENTER);
			
			
			masterBorderLayoutContainer.add(labelBorderLayoutContainer, BorderLayout.NORTH);
			masterBorderLayoutContainer.add(descriptionBorderLayoutContainer, BorderLayout.CENTER);
			masterBorderLayoutContainer.add(fitnessBorderLayoutContainer, BorderLayout.SOUTH);
			this.add(masterBorderLayoutContainer);
			this.setBorder(new EmptyBorder(5, 5, 5, 5));
		}
	}
	
	/*
	 * offspring container class
	 */
	class OffspringIndividualContainer extends JPanel{

		/**
		 * 
		 */
		private static final long serialVersionUID = -3191979503741521979L;
		/*
		 * pane
		 */
		public JPanel masterBorderLayoutContainer;
		public JPanel labelBorderLayoutContainer;
		public JPanel descriptionBorderLayoutContainer;
		
		public JPanel descriptionWContainer;
		public JPanel descriptionCContainer;
		public JPanel fitnessBorderLayoutContainer;
		
		public JPanel gp1;
		public JPanel gp2;
		public JPanel gp3;
		
		/*
		 * label
		 */
		public JLabel individualLabel;
		public JLabel genotypeLabel;
		public JLabel phenotypeLabel;
		public JLabel fitnessLabel;
		
		public int number;
		
		public OffspringIndividualContainer(int i){
			
			number = i+1;
			masterBorderLayoutContainer = new JPanel();
			masterBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
			masterBorderLayoutContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			masterBorderLayoutContainer.setPreferredSize(new Dimension(730, 140));
			
			/*
			 * label
			 */
			labelBorderLayoutContainer = new JPanel();
			labelBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
			labelBorderLayoutContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
			
			individualLabel = new JLabel("Individual " + number);
			individualLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
			
			labelBorderLayoutContainer.add(individualLabel, BorderLayout.EAST);
			
			/*
			 * description
			 */
			descriptionBorderLayoutContainer = new JPanel();
			descriptionBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
			descriptionBorderLayoutContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
			
			/*
			 * description west
			 */
			descriptionWContainer = new JPanel();
			descriptionWContainer.setLayout(new GridLayout(3, 1));
			descriptionWContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
			
			genotypeLabel = new JLabel("Genotype");
			phenotypeLabel = new JLabel("Phenotype");
			
			descriptionWContainer.add(new JLabel(" "));
			descriptionWContainer.add(genotypeLabel, BorderLayout.NORTH);
			descriptionWContainer.add(phenotypeLabel, BorderLayout.CENTER);
			
			/*
			 * description center
			 */
			
			descriptionCContainer = new JPanel();
			descriptionCContainer.setLayout(new GridLayout(1, 3));
			descriptionCContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
			
			/*
			 * genotype phenotype
			 */
			
			//1
			gp1 = new JPanel();
			gp1.setLayout(new GridLayout(3, 1));
			gp1.setBorder(new EmptyBorder(0, 5, 0, 5));
			
			//2
			gp2 = new JPanel();
			gp2.setLayout(new GridLayout(3, 1));
			gp2.setBorder(new EmptyBorder(0, 5, 0, 5));
			
			//3
			gp3 = new JPanel();
			gp3.setLayout(new GridLayout(3, 1));
			gp3.setBorder(new EmptyBorder(0, 5, 0, 5));
			
			/*
			 * genotype
			 */
			//1
			offspringGenotype[i][0] = new JTextField("00000000");
			offspringGenotype[i][0].setEditable(false);
			offspringGenotype[i][0].setBorder(null);
			gp1.add(new JLabel("Hidden Neuron"));
			gp1.add(offspringGenotype[i][0], BorderLayout.CENTER);
			//2
			offspringGenotype[i][1] = new JTextField("000000000");
			offspringGenotype[i][1].setEditable(false);
			offspringGenotype[i][1].setBorder(null);
			gp2.add(new JLabel("Learning Rate"));
			gp2.add(offspringGenotype[i][1], BorderLayout.CENTER);
			//3
			offspringGenotype[i][2] = new JTextField("000000000");
			offspringGenotype[i][2].setEditable(false);
			offspringGenotype[i][2].setBorder(null);
			gp3.add(new JLabel("Momentum Rate"));
			gp3.add(offspringGenotype[i][2], BorderLayout.CENTER);
			
			/*
			 * phenotype
			 */
			//1
			offspringPhenotype[i][0] = new JTextField("0");
			offspringPhenotype[i][0].setEditable(false);
			offspringPhenotype[i][0].setBorder(null);
			gp1.add(offspringPhenotype[i][0], BorderLayout.SOUTH);
			//2
			offspringPhenotype[i][1] = new JTextField("0");
			offspringPhenotype[i][1].setEditable(false);
			offspringPhenotype[i][1].setBorder(null);
			gp2.add(offspringPhenotype[i][1], BorderLayout.SOUTH);
			//3
			offspringPhenotype[i][2] = new JTextField("0");
			offspringPhenotype[i][2].setEditable(false);
			offspringPhenotype[i][2].setBorder(null);
			gp3.add(offspringPhenotype[i][2], BorderLayout.SOUTH);
			
			descriptionCContainer.add(gp1);
			descriptionCContainer.add(gp2);
			descriptionCContainer.add(gp3);
			
			/*
			 * fitness
			 */
			fitnessBorderLayoutContainer = new JPanel();
			fitnessBorderLayoutContainer.setLayout(new BorderLayout(5, 0));
			fitnessBorderLayoutContainer.setBorder(new EmptyBorder(0, 10, 10, 15));
			
			offspringFitness[i] = new JTextField("0");
			offspringFitness[i].setEditable(false);
			fitnessLabel = new JLabel("Fitness");
			fitnessBorderLayoutContainer.add(fitnessLabel, BorderLayout.WEST);
			fitnessBorderLayoutContainer.add(offspringFitness[i], BorderLayout.CENTER);
			
			descriptionCContainer.add(fitnessBorderLayoutContainer, BorderLayout.SOUTH);
			
			descriptionBorderLayoutContainer.add(descriptionWContainer, BorderLayout.WEST);
			descriptionBorderLayoutContainer.add(descriptionCContainer, BorderLayout.CENTER);
			
			
			masterBorderLayoutContainer.add(labelBorderLayoutContainer, BorderLayout.NORTH);
			masterBorderLayoutContainer.add(descriptionBorderLayoutContainer, BorderLayout.CENTER);
			masterBorderLayoutContainer.add(fitnessBorderLayoutContainer, BorderLayout.SOUTH);
			this.add(masterBorderLayoutContainer);
			this.setBorder(new EmptyBorder(5, 5, 5, 5));
		}
	}
	
	static /*
	 * load individual data into individual container
	 */
	class IndividualData{
		
		public static void loadPopulation(){
			
			/*
			 * for each individual
			 */
			for(int i=0; i<Config.POPULATION_SIZE; i++){
				
				/*
				 * genotype
				 * 0-7 hidden neuron
				 * 8-16 learning rate
				 * 17-25 momentum rate
				 */
				populationGenotype[i][0].setText(Arrays.toString(gabpnn.geneticAlgorithm.getIndividual(i).getGenotype().getGenes(0, 8)));
				populationGenotype[i][1].setText(Arrays.toString(gabpnn.geneticAlgorithm.getIndividual(i).getGenotype().getGenes(8, 17)));
				populationGenotype[i][2].setText(Arrays.toString(gabpnn.geneticAlgorithm.getIndividual(i).getGenotype().getGenes(17, 26)));
				
				/*
				 * phenotype
				 */
				populationPhenotype[i][0].setText(String.valueOf(gabpnn.geneticAlgorithm.getIndividual(i).getGenotype().decode().getHiddenNeuronCount()));
				populationPhenotype[i][1].setText(String.valueOf(gabpnn.geneticAlgorithm.getIndividual(i).getGenotype().decode().getLearningRate()));
				populationPhenotype[i][2].setText(String.valueOf(gabpnn.geneticAlgorithm.getIndividual(i).getGenotype().decode().getMomentumRate()));
				
				/*
				 * fitness
				 */
				populationFitness[i].setText(String.valueOf(gabpnn.geneticAlgorithm.getIndividual(i).getFitnessValue()));
			}
			
			/*
			 * for each mating pool individual
			 */
			for(int i=0; i<Config.MATING_POOL_SIZE; i++){
				
				/*
				 * genotype
				 * 0-7 hidden neuron
				 * 8-16 learning rate
				 * 17-25 momentum rate
				 */
				matingPoolGenotype[i][0].setText(Arrays.toString(gabpnn.geneticAlgorithm.getMatingPoolPopulation().getIndividual(i).getGenotype().getGenes(0, 8)));
				matingPoolGenotype[i][1].setText(Arrays.toString(gabpnn.geneticAlgorithm.getMatingPoolPopulation().getIndividual(i).getGenotype().getGenes(8, 17)));
				matingPoolGenotype[i][2].setText(Arrays.toString(gabpnn.geneticAlgorithm.getMatingPoolPopulation().getIndividual(i).getGenotype().getGenes(17, 26)));
				
				/*
				 * phenotype
				 */
				matingPoolPhenotype[i][0].setText(String.valueOf(gabpnn.geneticAlgorithm.getMatingPoolPopulation().getIndividual(i).getGenotype().decode().getHiddenNeuronCount()));
				matingPoolPhenotype[i][1].setText(String.valueOf(gabpnn.geneticAlgorithm.getMatingPoolPopulation().getIndividual(i).getGenotype().decode().getLearningRate()));
				matingPoolPhenotype[i][2].setText(String.valueOf(gabpnn.geneticAlgorithm.getMatingPoolPopulation().getIndividual(i).getGenotype().decode().getMomentumRate()));
				
				/*
				 * fitness
				 */
				matingPoolFitness[i].setText(String.valueOf(gabpnn.geneticAlgorithm.getMatingPoolPopulation().getIndividual(i).getFitnessValue()));
			}
			
			/*
			 * for each offspring individual
			 */
			for(int i=0; i<Config.MATING_POOL_SIZE; i++){
				
				/*
				 * genotype
				 * 0-7 hidden neuron
				 * 8-16 learning rate
				 * 17-25 momentum rate
				 */
				offspringGenotype[i][0].setText(Arrays.toString(gabpnn.geneticAlgorithm.getOffspringPopulation().getIndividual(i).getGenotype().getGenes(0, 8)));
				offspringGenotype[i][1].setText(Arrays.toString(gabpnn.geneticAlgorithm.getOffspringPopulation().getIndividual(i).getGenotype().getGenes(8, 17)));
				offspringGenotype[i][2].setText(Arrays.toString(gabpnn.geneticAlgorithm.getOffspringPopulation().getIndividual(i).getGenotype().getGenes(17, 26)));
				
				/*
				 * phenotype
				 */
				offspringPhenotype[i][0].setText(String.valueOf(gabpnn.geneticAlgorithm.getOffspringPopulation().getIndividual(i).getGenotype().decode().getHiddenNeuronCount()));
				offspringPhenotype[i][1].setText(String.valueOf(gabpnn.geneticAlgorithm.getOffspringPopulation().getIndividual(i).getGenotype().decode().getLearningRate()));
				offspringPhenotype[i][2].setText(String.valueOf(gabpnn.geneticAlgorithm.getOffspringPopulation().getIndividual(i).getGenotype().decode().getMomentumRate()));
				
				/*
				 * fitness
				 */
				offspringFitness[i].setText(String.valueOf(gabpnn.geneticAlgorithm.getOffspringPopulation().getIndividual(i).getFitnessValue()));
			}
		}
	}
	
	/*
	 * start button listener
	 */
	class StartProcessListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			/*
			 * check, whether the saving path is empty, if it's empty, then shows a dialog
			 */
			if(saveFilePath.isEmpty()){
				
				JOptionPane.showMessageDialog(null, "Saving directory hasn't been set!", "Warning", JOptionPane.ERROR_MESSAGE);
			}
			else{
				
				/*
				 * is running
				 */
				isRunning = true;
				
				second = minute = hour = 0;
				
				/*
				 * start time
				 */
				startTime = System.currentTimeMillis();
				
				/*
				 * start thread
				 */
				gaThread = new StartProcess();
				gaThread.start();
				
				/*
				 * start timer
				 */
				timer = new Timer("timer");
				timerTask = new TimeObserver();
				timer.scheduleAtFixedRate(timerTask, 1000, 1000);
			}
		}
		
	}
	
	/*
	 * start process
	 */
	class StartProcess extends Thread{
		
		public void run(){
			
			try {
				gabpnn.evolute(saveFilePath);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * GA UI Observer
	 */
	class GAUIObserver implements UIObserver{

		/*
		 * update generation value & individuals info
		 */
		@SuppressWarnings("static-access")
		@Override
		public void update() {
			
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					
					generationValue.setText(String.valueOf(gabpnn.geneticAlgorithm.getGeneration()));

					IndividualData.loadPopulation();
				}
			});
			
			try {
				gaThread.sleep(500);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		
	}
	
	class GABPNNUIObserver implements UIObserver{

		@SuppressWarnings("static-access")
		@Override
		public void update() {
			
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					
					statusState.setText(gabpnn.getStatus());
					individualInEvaluateValue.setText(String.valueOf(gabpnn.getIndividualInEvalute()));
				}
			});
			
			try {
				gaThread.sleep(500);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		
	}
	
	class SaveDialog implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			/*
			 * open save dialog
			 */
			JFileChooser saveFileChooser = new JFileChooser();
			saveFileChooser.setDialogTitle("Specify the directory to save the files");
			/*
			 * set it to directory only mode
			 */
			saveFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			int userSelection = saveFileChooser.showSaveDialog(contentPane);
			
			if(userSelection == JFileChooser.APPROVE_OPTION){
				
				/*
				 * set path
				 */
				saveFilePath = saveFileChooser.getSelectedFile().getAbsolutePath();
				/*
				 * set label
				 */
				saveFilePathValue.setText(saveFileChooser.getSelectedFile().getAbsolutePath());
			}
		}
	}
}
