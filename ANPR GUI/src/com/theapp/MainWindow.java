/*
 * By Joseph joseph dot tarigan at gmail dot com on GEMINI
 */
package com.theapp;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.imgscalr.Scalr;

import com.charset.Alphanumeric;
import com.imageoperation.BinaryProcessing;
import com.imageoperation.ImagePixelArray;
import com.neuralnetworks.LayerInterface;

@SuppressWarnings("unused")
public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Main Panel
	 */
	private JPanel contentPane;
	
	/*
	 * Menus
	 */
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnWeightData;
	private JMenuItem mntmAbout;
	private JMenuItem mntmExit;
	private JMenuItem mntmFirstHidden;
	private JMenuItem mntmHiddenOutput;
	
	/*
	 * Labels
	 */
	private JLabel statusLabel;
	private JLabel statusValue;
	private JLabel inputImageContainer;
	private JLabel detectedLicensePlateContainer;
	private JLabel detectedLicensePlateLabel;
	private JLabel resultLabel;
	private JLabel inputImageLabel;
	private JLabel inputImageLocationLabel;
	private JLabel preprocessTimeLabel;
	private JLabel feedforwardTimeLabel;
	private JLabel preprocessTimeValue;
	private JLabel feedforwardTimeValue;
	
	/*
	 * Panels
	 */
	private JPanel mainWest;
	private JPanel mainEast;

	private JPanel statusPanel;
	private JPanel statusContainer;
	private JPanel inputChooserContainer;
	private JPanel candidatePanel;
	private JPanel candidatePanelContainer;
	private JPanel resultPanel;
	private JPanel resultPanelContainer;
	private JPanel buttonPanel;
	private JPanel inputImagePanel;
	private JPanel inputImagePanelContainer;
	private JPanel resultContainer;
	private JPanel preprocessTimeContainer;
	private JPanel feedforwardTimeContainer;
	
	/*
	 * TextField
	 */
	private JTextField recognisedCharacter;
	
	/*
	 * ImageIcon
	 */
	private ImageIcon inputImage, candidateImage;
	
	/*
	 * Buttons
	 */
	private JButton inputBrowse;
	private JButton resetButton;
	private JButton recogniseButton;
	
	/*
	 * Status Enumeration
	 */
	private static enum recogniserStatus{
		NOTREADY("Not Ready"), READY("Ready"), EXTRACTING("Extracting"), RECOGNIZING("Recognizing"), DONE("Done");
		private String value;
		
		private recogniserStatus(String s){
			this.value = s;
		}
		
		public String toString(){
			return value;
		}
	}
	
	/*
	 * Dummy Image Enumerator
	 */
	private static enum dummyImage{
		INPUTIMAGE("inputImageDummy.jpg"), DETECTEDIMAGE("detectedPlateDummy.jpg"), ALPHANUMERICIMAGE("");
		private String value;
		
		private dummyImage(String s){
			this.value = s;
		}
		
		public String toString(){
			return value;
		}
	}
	
	/*
	 * Temporary Directory & Create The Folder
	 */
	private final String temporaryDir = System.getProperty("java.io.tmpdir") + "ALPR\\";
	boolean tempDirInit = new File(temporaryDir).mkdir();
	
	/*
	 * Directories
	 */
	private String inputImageLocation;
	private final String srcDir = "src/";
	private final String srcImageDir = srcDir + "images/dummy/";
	
	/*
	 * JFileChooser
	 */
	private JFileChooser fileChooser;
	
	/*
	 * Temporary BufferedImage
	 */
	private BufferedImage inputImageObject, inputImageObjectDisplay, detectedPlate;
	
	/*
	 * Default Weight Data
	 */
	private final String defaultInputHiddenData = srcDir + "weightdata/inputLayer-hiddenLayer.DAT";
	private final String defaultHiddenOutputData = srcDir + "weightData/hiddenLayer-outputLayer.DAT";
	
	/*
	 * The Neural Networks
	 */
	private AlphaNumericNeuralNetworks alphaNumericNeuralNetworks = new AlphaNumericNeuralNetworks(defaultInputHiddenData, defaultHiddenOutputData);
	
	/*
	 * Plate Extractor
	 */
	private PlateExtractorCCA plateExtractor;
	
	/*
	 * AlphaNumeric Characters Holder
	 */
	private List<ImagePixelArray> detectedCharacters;
	private Iterator<ImagePixelArray> detectedCharactersIterator;
	
	/*
	 * Output Layer Interface
	 */
	private LayerInterface outputLayerInterface;
	
	/*
	 * Detected String of Character
	 */
	private String plateCharacter = "";
	
	/**
	 * Launch the application.
	 * ====================================================================================================
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

		setTitle("Automatic License Plate Recognizer");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 820, 600);
		setSize(820, 660);
		
		/*
		 * Menus
		 * ==========================================================================
		 */
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mnWeightData = new JMenu("Weight Data");
		mnFile.add(mnWeightData);
		
		mntmFirstHidden = new JMenuItem("First -> Hidden");
		mntmFirstHidden.addActionListener(new FirstHiddenDialog());
		mnWeightData.add(mntmFirstHidden);
		
		mntmHiddenOutput = new JMenuItem("Hidden -> Output");
		mntmHiddenOutput.addActionListener(new HiddenOutputDialog());
		mnWeightData.add(mntmHiddenOutput);
		
		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ShowAbout());
		mnFile.add(mntmAbout);
		
		/*
		 * Exit
		 */
		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(NORMAL);
			}
		});
		mnFile.add(mntmExit);
		
		/*
		 * ==========================================================================
		 */
		
		/*
		 * Main Panel
		 * ==========================================================================
		 */
		
		mainWest = new JPanel();
		mainWest.setLayout(new BorderLayout());
		mainWest.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		mainEast = new JPanel();
		mainEast.setLayout(new BorderLayout(10, 10));
		mainEast.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		/*
		 * ==========================================================================
		 */		
		
		/*
		 * Input Image Panel
		 * ==========================================================================
		 */
		
		inputImagePanel = new JPanel();
		inputImagePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		BorderLayout bl_inputImagePanel = new BorderLayout();
		bl_inputImagePanel.setHgap(5);
		bl_inputImagePanel.setVgap(5);
		inputImagePanel.setLayout(bl_inputImagePanel);
		
		inputImagePanelContainer = new JPanel();
		BorderLayout bl_inputImagePanelContainer = new BorderLayout();
		bl_inputImagePanelContainer.setVgap(5);
		bl_inputImagePanelContainer.setHgap(5);
		inputImagePanelContainer.setLayout(bl_inputImagePanelContainer);
		inputImagePanelContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		inputImage = new ImageIcon(srcImageDir + dummyImage.INPUTIMAGE.toString());
		inputImageContainer = new JLabel(inputImage);
		inputImageContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		inputImageLabel = new JLabel("Input Image");
		inputImageLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		inputImagePanelContainer.add(inputImageLabel, BorderLayout.NORTH);
		inputImagePanelContainer.add(inputImageContainer, BorderLayout.CENTER);
		
		inputImagePanel.add(inputImagePanelContainer, BorderLayout.NORTH);
		
		mainWest.add(inputImagePanel, BorderLayout.NORTH);
		
		/*
		 * ==========================================================================
		 */
		
		/*
		 * Status Panel 
		 * ==========================================================================
		 */
		
		statusPanel = new JPanel();
		statusPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		statusPanel.setLayout(new GridLayout(4, 1, 0, 0));
		
		statusContainer = new JPanel(new BorderLayout());
		statusContainer.setSize(380, 50);
		statusContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		statusLabel = new JLabel("Status ");
		statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		statusValue = new JLabel(recogniserStatus.NOTREADY.toString());
		statusValue.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		statusContainer.add(statusLabel, BorderLayout.WEST);
		statusContainer.add(statusValue, BorderLayout.EAST);
		
		preprocessTimeContainer = new JPanel();
		preprocessTimeContainer.setLayout(new BorderLayout());
		preprocessTimeContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		preprocessTimeLabel = new JLabel("Preprocess Time ");
		preprocessTimeLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		preprocessTimeValue = new JLabel("0 ms");
		preprocessTimeValue.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		preprocessTimeContainer.add(preprocessTimeLabel, BorderLayout.WEST);
		preprocessTimeContainer.add(preprocessTimeValue, BorderLayout.EAST);
		
		feedforwardTimeContainer = new JPanel();
		feedforwardTimeContainer.setLayout(new BorderLayout());
		feedforwardTimeContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		feedforwardTimeLabel = new JLabel("Feedforward Time ");
		feedforwardTimeLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		feedforwardTimeValue = new JLabel("0 ms");
		feedforwardTimeValue.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		feedforwardTimeContainer.add(feedforwardTimeLabel, BorderLayout.WEST);
		feedforwardTimeContainer.add(feedforwardTimeValue, BorderLayout.EAST);
		
		inputChooserContainer = new JPanel(new BorderLayout());
		inputChooserContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		inputImageLabel = new JLabel("Input Image ");
		inputImageLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		inputImageLocation = "None";
		inputImageLocationLabel = new JLabel(inputImageLocation);
		inputImageLocationLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		inputBrowse = new JButton("Browse");
		inputBrowse.addActionListener(new OpenImageDialog());
		
		inputChooserContainer.add(inputImageLabel, BorderLayout.WEST);
		inputChooserContainer.add(inputImageLocationLabel, BorderLayout.CENTER);
		inputChooserContainer.add(inputBrowse, BorderLayout.EAST);
		
		statusPanel.add(statusContainer);
		statusPanel.add(preprocessTimeContainer);
		statusPanel.add(feedforwardTimeContainer);
		statusPanel.add(inputChooserContainer);
		
		mainEast.add(statusPanel, BorderLayout.NORTH);
		
		/*
		 * ==========================================================================
		 */
		
		/*
		 * Candidate Panel
		 * ==========================================================================
		 */
		
		BorderLayout bl_resultContainer = new BorderLayout();
		bl_resultContainer.setVgap(10);
		resultContainer = new JPanel(bl_resultContainer);
		
		candidatePanel = new JPanel();
		candidatePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		candidatePanel.setLayout(new GridLayout(1, 1, 0, 10));
		
		candidatePanelContainer = new JPanel();
		BorderLayout bl_candidatePanelContainer = new BorderLayout();
		bl_candidatePanelContainer.setVgap(5);
		bl_candidatePanelContainer.setHgap(5);
		candidatePanelContainer.setLayout(bl_candidatePanelContainer);
		candidatePanelContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		detectedLicensePlateLabel = new JLabel("Detected License Plate");
		detectedLicensePlateLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		candidateImage = new ImageIcon(srcImageDir + dummyImage.DETECTEDIMAGE.toString());
		detectedLicensePlateContainer = new JLabel(candidateImage, SwingConstants.CENTER);
		detectedLicensePlateContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		candidatePanelContainer.add(detectedLicensePlateLabel, BorderLayout.NORTH);
		candidatePanelContainer.add(detectedLicensePlateContainer, BorderLayout.CENTER);
		
		candidatePanel.add(candidatePanelContainer);
		
		resultContainer.add(candidatePanel, BorderLayout.NORTH);	
		
		/*
		 * ==========================================================================
		 */
		
		/*
		 * Result Panel
		 * ==========================================================================
		 */
		
		resultPanel = new JPanel();
		resultPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		resultPanel.setLayout(new GridLayout(1, 1, 0, 10));
		
		resultPanelContainer = new JPanel();
		resultPanelContainer.setBorder(new EmptyBorder(5, 5, 5, 5));
		BorderLayout bl_resultPanelContainer = new BorderLayout();
		bl_resultPanelContainer.setVgap(5);
		bl_resultPanelContainer.setHgap(5);
		resultPanelContainer.setLayout(bl_resultPanelContainer);
		
		resultLabel = new JLabel("Result");
		resultLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		resultPanelContainer.add(resultLabel, BorderLayout.NORTH);
		
		recognisedCharacter = new JTextField("None");
		recognisedCharacter.setEditable(false);
		resultPanelContainer.add(recognisedCharacter, BorderLayout.CENTER);
		
		resultPanel.add(resultPanelContainer);
		resultContainer.add(resultPanel, BorderLayout.CENTER);
		
		mainEast.add(resultContainer);	
		
		/*
		 * ==========================================================================
		 */
		
		/*
		 * Button Panel
		 * ==========================================================================
		 */
		
		buttonPanel = new JPanel();
		BorderLayout bl_buttonPanel = new BorderLayout();
		bl_buttonPanel.setVgap(10);
		bl_buttonPanel.setHgap(10);
		buttonPanel.setLayout(bl_buttonPanel);
		
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ResetAll());
		recogniseButton = new JButton("Recognise");
		recogniseButton.addActionListener(new RecogniseInputImage());
		
		buttonPanel.add(resetButton, BorderLayout.WEST);
		buttonPanel.add(recogniseButton, BorderLayout.CENTER);
		
		mainEast.add(buttonPanel, BorderLayout.SOUTH);
		
		/*
		 * ==========================================================================
		 */

		
		/*
		 * Finally
		 */
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		
		contentPane.add(mainEast, BorderLayout.CENTER);
		contentPane.add(mainWest, BorderLayout.WEST);
		
		setContentPane(contentPane);
	}
	
	/*
	 * Open Image Dialog
	 */
	class OpenImageDialog implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			/*
			 * Open BrowseDialog
			 */
			
			fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Select input image");
			
			FileFilter jpgFilter = new FileNameExtensionFilter("JPEG Image", new String[] {"jpg", "jpeg"});
			
			fileChooser.setFileFilter(jpgFilter);
			
			int userSelection = fileChooser.showSaveDialog(contentPane);
			
			if(userSelection == JFileChooser.APPROVE_OPTION){
				
				/*
				 * Save path into variable
				 */
				inputImageLocation = fileChooser.getSelectedFile().getAbsolutePath();
				
				/*
				 * Resize Input Image
				 */
				File inputFile = new File(inputImageLocation);
				
				try {
					inputImageObject = ImageIO.read(inputFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				if(inputImageObject != null){
					
					/*
					 * One for display, one as input
					 */
					inputImageObject = Scalr.resize(inputImageObject, 1024);
					inputImageObjectDisplay = Scalr.resize(inputImageObject, 380);

					/*
					 * Render ImageIcon
					 */
					Thread tempThread1 = new RenderInputImage();
					tempThread1.run();
					
					/*
					 * Update String
					 */
					Thread tempThread2 = new UpdateFilePathLabel();
					tempThread2.run();
				}
			}
		}
	}
	
	/*
	 * Open Weight Dialog & Loading Process
	 */
	class FirstHiddenDialog implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			/*
			 * Open BrowseDialog
			 */
			fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Select the weight data");
			String path;
			
			FileFilter dataFilter = new FileNameExtensionFilter("Weight Data", new String[] {"DAT"});
			
			fileChooser.setFileFilter(dataFilter);
			
			int userSelection = fileChooser.showSaveDialog(contentPane);
			
			if(userSelection == JFileChooser.APPROVE_OPTION){
				
				/*
				 * Save path into variable
				 */
				path = fileChooser.getSelectedFile().getAbsolutePath();
				
				/*
				 * Load into neural networks
				 */
				alphaNumericNeuralNetworks.changeInputHiddenData(path);
			}
		}
	}
	
	/*
	 * Open Weight Dialog & Loading Process
	 */
	class HiddenOutputDialog implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			/*
			 * Open BrowseDialog
			 */
			fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Select the weight data");
			String path;
			
			FileFilter dataFilter = new FileNameExtensionFilter("Weight Data", new String[] {"DAT"});
			
			fileChooser.setFileFilter(dataFilter);
			
			int userSelection = fileChooser.showSaveDialog(contentPane);
			
			if(userSelection == JFileChooser.APPROVE_OPTION){
				
				/*
				 * Save path into variable
				 */
				path = fileChooser.getSelectedFile().getAbsolutePath();
				
				/*
				 * Load into neural networks
				 */
				alphaNumericNeuralNetworks.changeHiddenOutputData(path);
			}
		}

	}

	/*
	 * Render Image
	 */
	class RenderInputImage extends Thread{
		
		public RenderInputImage(){
			
			/*
			 * Swing Thread Safe
			 */
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {

					inputImageContainer.removeAll();
					inputImageContainer.revalidate();
					
					inputImagePanelContainer.remove(inputImageContainer);
					inputImagePanelContainer.revalidate();
					
					inputImage = new ImageIcon(inputImageObjectDisplay);

					inputImageContainer = new JLabel(inputImage);
					inputImageContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
					
					inputImagePanelContainer.add(inputImageContainer, BorderLayout.CENTER);
				}
			});
		}
	}
	
	/*
	 * Render Candidate Plate
	 */
	class RenderCandidatePlate extends Thread{
		
		public RenderCandidatePlate(){
			
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {

					detectedLicensePlateContainer.removeAll();
					detectedLicensePlateContainer.revalidate();
					
					candidatePanelContainer.remove(detectedLicensePlateContainer);
					candidatePanelContainer.revalidate();
					
					candidateImage = new ImageIcon(detectedPlate);
					detectedLicensePlateContainer = new JLabel(candidateImage, SwingConstants.CENTER);
					detectedLicensePlateContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
					
					candidatePanelContainer.add(detectedLicensePlateContainer, BorderLayout.CENTER);
				}
			});
		}
	}
	
	/*
	 * Update Path File Label
	 */
	class UpdateFilePathLabel extends Thread{
		
		public UpdateFilePathLabel(){
			
			/*
			 * Swing Thread Safe
			 */
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					
					inputImageLocationLabel.setText(inputImageLocation);
				}
			});
		}
	}
	
	class RecogniseInputImage extends Thread implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {

			/*
			 * Check input image
			 */
			if(inputImageLocation.equals("None")){
				
				/*
				 * Exit
				 */
				JOptionPane.showMessageDialog(null, "Input image hasn't been set!", "Warning", JOptionPane.ERROR_MESSAGE);
			}
			else{
				
				/*
				 * Go
				 */
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						
						statusValue.setText(recogniserStatus.EXTRACTING.toString());
					}
				});
				
				/*
				 * extract plate
				 */
				plateExtractor = new PlateExtractorCCA();
				plateExtractor.setInputImage(inputImageObject);
				plateExtractor.extract();
				
				/*
				 * set time
				 */
				preprocessTimeValue.setText(String.valueOf(plateExtractor.getProcessingTime() + " ms"));
				
				/*
				 * if a plate is detected
				 */
				if(plateExtractor.getDetectedPlate() != null){
					
					detectedPlate = Scalr.resize(plateExtractor.getDetectedPlate(), 380);
					
					/*
					 * Update detected plate ImageIcon
					 */
					Thread tt = new RenderCandidatePlate();
					tt.run();
					
					/*
					 * NeuralNetworks Begins 
					 * For each ImagePixelArray, scale it to 12 x 9, do forward propagate, get the layer interface and voila
					 */
					detectedCharacters = plateExtractor.getDetectedCharacters();
					detectedCharactersIterator = detectedCharacters.iterator();
					
					/*
					 * Reset
					 */
					plateCharacter = "";
					int h=1;
					
					/*
					 * local variable to hold feedforward time
					 */
					long feedforwardTime = 0;
					
					while(detectedCharactersIterator.hasNext()){
						
						/*
						 * Resize into 9x12
						 */
						BufferedImageOp opnull = null;
						ImagePixelArray tempArray = new ImagePixelArray(Scalr.resize(detectedCharactersIterator.next().convertIntoBufferedImage(), Scalr.Mode.FIT_EXACT, 9, 12, Scalr.OP_BRIGHTER));
						
						/*
						 * Experimental Threshold 
						 */
						//tempArray = BinaryProcessing.threshold(tempArray, 100);
						tempArray = BinaryProcessing.otsuThreshold(tempArray);
						
						
						/*
						 * Experimental 
						 */
						//writeJPEG.write(tempArray, "src/images/output/" + "alphanumericCandidate" + h + ".jpg");
						//h++;
						
						/*
						 * Fetch into neural networks
						 */
						SwingUtilities.invokeLater(new Runnable() {
							
							@Override
							public void run() {
								
								statusValue.setText(recogniserStatus.RECOGNIZING.toString());
							}
						});
						
						/*
						 * calculate time and do feedforward
						 */
						alphaNumericNeuralNetworks.forwardPropagate(tempArray.getSerializedNormalizedPixelLevels());
						feedforwardTime += alphaNumericNeuralNetworks.getProcessingTime();
						
						/*
						 * Get output layer interface
						 */
						outputLayerInterface = alphaNumericNeuralNetworks.neuralNetworks.getOutputValues();
						
						/*
						 * Get Character
						 */
						String temp;
						Alphanumeric alphaNumeric = new Alphanumeric();
						
						temp = alphaNumeric.getCharacter(outputLayerInterface.getSerializedOutput());
						
						//System.out.printf("%s ", temp);
						
						plateCharacter += temp;
					}
					System.out.println();
					
					/*
					 * Update result panel
					 */
					feedforwardTimeValue.setText(String.valueOf(feedforwardTime) + " ms");
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							
							recognisedCharacter.setText(plateCharacter);
						}
					});
					
					/*
					 * Update status label
					 */
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							
							statusValue.setText(recogniserStatus.DONE.toString());
						}
					});
				}
			}
		}
	}
	
	/*
	 * Reset all fields
	 */
	class ResetAll implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			/*
			 * Reset input image, detected license plate image, result, status, input image path and weights
			 * 
			 */
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					
					/*
					 * Input Image
					 */
					inputImageContainer.removeAll();
					inputImageContainer.revalidate();
					inputImagePanelContainer.remove(inputImageContainer);
					inputImagePanelContainer.revalidate();
					inputImage = new ImageIcon(srcImageDir + dummyImage.INPUTIMAGE.toString());
					inputImageContainer = new JLabel(inputImage);
					inputImageContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
					inputImagePanelContainer.add(inputImageContainer, BorderLayout.CENTER);
					
					/*
					 *  Time
					 */
					preprocessTimeValue.setText("0 ms");
					feedforwardTimeValue.setText("0 ms");
					
					/*
					 * Status
					 */
					statusValue.setText(recogniserStatus.READY.toString());
					
					/*
					 * Detected Plate
					 */
					detectedLicensePlateContainer.removeAll();
					detectedLicensePlateContainer.revalidate();
					candidatePanelContainer.remove(detectedLicensePlateContainer);
					candidatePanelContainer.revalidate();
					candidateImage = new ImageIcon(srcImageDir + dummyImage.DETECTEDIMAGE.toString());
					detectedLicensePlateContainer = new JLabel(candidateImage, SwingConstants.CENTER);
					detectedLicensePlateContainer.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
					candidatePanelContainer.add(detectedLicensePlateContainer, BorderLayout.CENTER);
					
					/*
					 * Recognised Character
					 */
					recognisedCharacter.setText("None");
					
					/*
					 * Input Path
					 */
					inputImageLocationLabel.setText("None");
					
					/*
					 * Weight Data
					 */
					alphaNumericNeuralNetworks.changeInputHiddenData(defaultInputHiddenData);
					alphaNumericNeuralNetworks.changeHiddenOutputData(defaultHiddenOutputData);
				}
			});
		}
	}
	
	/*
	 * About Window
	 */
	class AboutWindow extends JDialog{

		public AboutWindow(JFrame parent){
			
			super(parent, "About", true);
		    setSize(350, 150);
			
			Box box = Box.createVerticalBox();
			box.setBorder(new EmptyBorder(15, 15, 15, 15));
			 
			box.add(Box.createGlue());
		    box.add(new JLabel("ALPR"));
		    box.add(new JLabel("Automatic License Plate Recognition"));
		    box.add(new JLabel("2015 - joseph.tarigan@gmail.com"));
		    box.add(new JLabel("RESEARCH USE ONLY"));
		    box.add(Box.createGlue());

		    getContentPane().add(box, "Center");

		    JPanel panel = new JPanel();
		    JButton ok = new JButton("Ok");
		    panel.add(ok);
		    getContentPane().add(panel, "South");

		    ok.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent evt) {
		        setVisible(false);
		      }
		    });
		}
	}
	
	class ShowAbout implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			AboutWindow about = new AboutWindow(new JFrame());
			about.show();
		}
		
	}
}
