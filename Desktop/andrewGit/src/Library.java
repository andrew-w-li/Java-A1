import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * all methods are for creating the view library page
 */
public class Library {
	HBox libLayout = new HBox();
	VBox swappable = new VBox();
	XMLP read;
	NodeList myNodeList;
	int numPages;
	ObservableList<String> fill = FXCollections.observableArrayList();
	ListView<String> pages;
	
	/**
	 * 0 arg constructor, upon creation of Library object File.xml is parsed
	 * and the pages (as Nodes) and the number of pages are stored
	 */
	public Library(){
		read = new XMLP();
		read.read();
		myNodeList = read.getNodeList();
		numPages = read.getNumPages(); //TO BE REMOVED
	}
	
	/**
	 * creates the clickable scroll pane and swaps out the custom pages on click
	 */
	public VBox makeLibrary(){
		for(int i=0; i<numPages; i++){
			fill.add("Page "+"" + i);
		}
		pages = new ListView<String>(fill);
		ScrollPane sp = new ScrollPane(pages);
		
		pages.setOnMouseClicked(e -> {
			VBox newLib = makeBox(myNodeList, pages.getSelectionModel().getSelectedIndex());	
			swappable.getChildren().clear();
			swappable.getChildren().add(newLib);
		});
		
		libLayout.getChildren().add(sp);
		libLayout.getChildren().add(swappable);
		VBox vlib = new VBox();
		vlib.getChildren().add(libLayout);
		return vlib;
	}
	
	/**
	 * parses through myNodeList to populate a list of options
	 * then uses that list in writePage to get a full VBox of options + text fields
	 */
	public VBox makeBox(NodeList nL, int selectedPage){
		
		List<String> opts = new ArrayList<String>();
		
		//check for the selected listview index which matches the id from the xml
		for(int i=0; i<nL.getLength(); i++){
			Node page = nL.item(i);
			int currentPage = Integer.parseInt(page.getAttributes().item(0).getTextContent());
			if(currentPage == selectedPage){ //TODO ADD A LAST STATEMENT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				NodeList pageChildren = page.getChildNodes();
				//populate opts with all options chosen by user
				for(int j=0; j<pageChildren.getLength(); j++){
					opts.add(pageChildren.item(j).getTextContent());
				}//for j
			}// if current == selected
		}//for i
		return writePage(opts);
	}//makeBox

	/**
	 * uses an array of options under the specific page picked by the user to 
	 * create a VBox containing the appropriate number of labels and textfields
	 * returns that VBox
	 */
	public VBox writePage(List<String> optsArray){
		VBox scriptLayout = new VBox();
		Button write = new Button("Full Command");
		List<TextField> tList = new ArrayList<TextField>();
		List<Label> lList = new ArrayList<Label>();
		TextField display = new TextField();
		display.setMinWidth(500);
		StringBuilder output = new StringBuilder();
		
		//create the appropriate number of labels and textboxes
		for(int i=1; i<optsArray.size(); i++){
			lList.add(new Label(optsArray.get(i)));
			tList.add(new TextField());
		}
		//add each label and textbox into an Hbox to be added into the vertical layout
		for(int i =0; i<lList.size();i++){
			tList.get(i).setMaxWidth(200);;
			HBox hbox = new HBox();
			hbox.getChildren().add(lList.get(i));
			hbox.getChildren().add(tList.get(i));
			scriptLayout.getChildren().add(hbox);
		}
		//output user text
		write.setOnAction(e -> {
			output.append(optsArray.get(0) + " ");
			for (int i=0; i<lList.size(); i++){
				output.append(lList.get(i).getText() + " ");
				if (tList.get(i).getText().length() >0){
					output.append(tList.get(i).getText() + " ");
				}//if
			}//for
			display.setText(output.toString());
		});//lambda e
		
		scriptLayout.getChildren().addAll(write, display);
		return scriptLayout;
	}//write page
}//class