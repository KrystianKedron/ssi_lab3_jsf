package org.pk.ssi.lab05;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import org.pk.ssi.lab05.indexBeam;

public class DishesValueListener implements ValueChangeListener{

	private String values[] = new String[]{"Barszu czerwonego", "Pierogow", "Karpia"};
	
	@Override
	public void processValueChange(ValueChangeEvent event)
			throws AbortProcessingException {

		indexBeam beam = (indexBeam) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("indexBeam");
		
	    String tablica = event.getNewValue().toString();
	    char[] tab = tablica.toCharArray();
	    String out = "";
	    for (char t: tab){
	    	if (t=='[' || t == ',' || t==']' || t==' '){
	    		continue;
	    	}
	    	out += values[Character.getNumericValue(t)-1] + ", ";
	    }
	    out += "i czego jeszcze dusza zapragnie!";
	    beam.setValue(out);
	}
}