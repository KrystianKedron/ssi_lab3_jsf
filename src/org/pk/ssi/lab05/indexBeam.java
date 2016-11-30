package org.pk.ssi.lab05;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.primefaces.event.RateEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.google.gson.JsonObject;

import org.lightcouch.CouchDbClient;

@ManagedBean(name="indexBeam")
@SessionScoped
public class indexBeam {
	
	public String name = "";
	public String second_name = "";
	
	public boolean bool = true;
	private Integer rating;
	private List<String> selectedOptions;
	private Theme theme;   
    private List<Theme> themes;
    private String value;
    private Integer valueRank[] = new Integer[5];
    
	private StreamedContent chart; 
    
    @ManagedProperty("#{themeService}")
    private ThemeService service;
    
    @PostConstruct
    public void init() {
    	themes = service.getThemes();
    	fillNull();
    }
    
    //getters and setters
    public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	 public List<String> getSelectedOptions() {
	    return selectedOptions;
	}
	 
    public void setSelectedOptions(List<String> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }
    
	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	public List<Theme> getThemes() {
		return themes;
	}

	public void setThemes(List<Theme> themes) {
		this.themes = themes;
	}

	public ThemeService getService() {
		return service;
	}

	public void setService(ThemeService service) {
		this.service = service;
	}
	
    public String getSecond_name() {
		return second_name;
	}

	public void setSecond_name(String second_name) {
		this.second_name = second_name;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public boolean isBool() {
		return bool;
	}

	public void setBool(boolean bool) {
		this.bool = !bool;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
    public StreamedContent getChart() {
        return chart;
    }
	
	//----------------
    
    public void fillNull(){
    	for (int i = 0; i < valueRank.length; i++){
    		valueRank[i] = 0;
    	}
    }
    
    public void updateSetValue(){
    	
    	int i = 0;
    	for (Theme the: themes){
    		if (theme.getDisplayName().equals(the.getDisplayName())){
    			valueRank[i] += 1;
    		}
    		i++;
    	}
    }
    
    public void updateDb(){
    	
    	CouchDbClient dbClient = new CouchDbClient(); 
    	String id = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionId(true);
    	JsonObject json = new JsonObject();
    	json.addProperty("_id", id);
		json.addProperty("Imie", this.name);
		json.addProperty("Nazwisko", this.second_name);
		dbClient.save(json); 
		System.out.println("Dokument o id " + dbClient.find(JsonObject.class, id).getAsJsonObject().get("_id") + " zostal utowrzony w bazie");
		dbClient.shutdown();
    }
    
    private PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Mikolaj", new Double(valueRank[0]));
        dataset.setValue("Gwiazde", new Double(valueRank[1]));
        dataset.setValue("Balwana", new Double(valueRank[2]));
        dataset.setValue("Aniola", new Double(valueRank[3]));
        dataset.setValue("Choinke", new Double(valueRank[4]));
        
        return dataset;
    }
    
    public void updateChar(){
    	//Chart
    	updateSetValue();
    	updateDb();
        JFreeChart jfreechart = ChartFactory.createPieChart("Statystyki", createDataset(), true, true, false);
        File chartFile = new File("dynamichart");
        try {
        	ChartUtilities.saveChartAsPNG(chartFile, jfreechart, 375, 300);
			chart = new DefaultStreamedContent(new FileInputStream(chartFile), "image/png");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public void onrate(RateEvent rateEvent) {
		System.out.println(((Integer) rateEvent.getRating()).intValue());
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Twoja odpowiedz to "+ (((Integer) rateEvent.getRating()).intValue()),"");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
     
    public void oncancel() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancel Event", "Rate Reset");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
	
	public void addMessage() {
        String summary = !bool ? "W jakiej skali?" : "TO NIE DOBRZE!";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
    }
}
