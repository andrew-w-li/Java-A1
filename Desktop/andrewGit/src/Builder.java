import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Creates all widgets for Command Builder pages, applies them to a VBox
 */
public class Builder {
	VBox vBoxBuild = new VBox();
	VBox vBoxWipeable = new VBox();
	Button addOption;
	Button compile;
	Button save;
	
	ArrayList<String> ar = new ArrayList<String>();
	ListView<String> lv;
	TextArea receipt = new TextArea();
	Label label = new Label("Choose a program");
	ObservableList<String> programArray = FXCollections.observableArrayList("tophat", "gatk","samtools");
	ComboBox<String> list = new ComboBox<String>(programArray);
	ObservableList<String> optionArray = null;
	
	/**
	 * creates and returns a VBox with a combobox and a hidden VBox
	 * upon clicking an option in the combobox, a page is generated with createpage()
	 * which wipes and populates the hidden VBox
	 */
	public VBox makeBuilder(){
		//depending on list choice, populate lower half of box
		list.setOnAction(e -> {
			fillOptions(list.getValue());
			ar.clear();
			receipt.clear();
			createPage();
		});//lambda e
		vBoxBuild.getChildren().addAll(label, list, vBoxWipeable);
		
		return vBoxBuild;
	}//makeBuilder
	
	/**
	 * populates a list of options shown the viewer depending on the combo box selection
	 */
	public void fillOptions(String choice){
		if(choice == "tophat"){
			optionArray = FXCollections.observableArrayList(
					"--read-mismatches", "--read-gap-length", "--read-edit-dist", "--read-realign-edit-dist",
					"--mate-inner-dist", "--mate-std-dev", "--min-anchor-length", "--splice-mismatches",
					"--min-intron-length", "--max-intron-length", "--max-insertion-length", "--max-deletion-length",
					"--quals", "--num-threads", "--report-secondary-alignments", "--no-discordant", "--no-mixed"
					);
		}else if(choice == "gatk"){
			optionArray = FXCollections.observableArrayList("-T", "--variant","-o", "-I", "-R", "--known", "-knownSites",
				"-BQSR", "--resource", "--expression", "--comp", "-E", "--dbsnp", "-priority", "--genotypeMergeOptions",
				"-select", "--filteredrecordsmergetype");
		}else if(choice == "samtools"){
			optionArray = FXCollections.observableArrayList("view", "flagstat","faidx","index", "-b", "-S", "-o",
					"-b", "-C", "-1", "-u", "-h", "-H", "-c");
		}
	}//fillOptions
	
	/**
	 * dynamically creates a page based on the user's combo box selection, includes
	 * a list of options to be added, a button to add desired options, a receipt to show added options,
	 * and a save button which writes to an XML file on click
	 */
	public void createPage(){
		//populate array with options depending on choice
		lv = new ListView<String>(optionArray);
		lv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ScrollPane sp = new ScrollPane(lv);
		
		//add options button to store selected options in array
		addOption = new Button("add "+list.getValue() +" option");
		addOption.setOnAction(e -> {
			receipt.appendText(lv.getSelectionModel().getSelectedItems()+"\n");
			ar.addAll(lv.getSelectionModel().getSelectedItems());
		});//lambda e
		
		//save button to save to XML
		save = new Button("Save to Library");
		save.setOnAction(e -> {
			if(ar.size() > 0){
				XMLP newFile = new XMLP();
				newFile.write(list.getValue(), ar);
			}else{
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("None Selected");
				alert.setHeaderText("Please select atleast 1 option");
				alert.showAndWait();
			}
		});//lambda e
		
		VBox receiptAndLabel = new VBox();
		receiptAndLabel.getChildren().addAll(new Label("You have chosen the following:"), receipt);
		HBox spAndReceipt = new HBox();
		spAndReceipt.getChildren().addAll(sp, receiptAndLabel);
		vBoxWipeable.getChildren().clear();
		vBoxWipeable.getChildren().addAll(spAndReceipt, addOption, save);
	}//create page
}//class