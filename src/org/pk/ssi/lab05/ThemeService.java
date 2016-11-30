package org.pk.ssi.lab05;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
 
import org.pk.ssi.lab05.Theme;
 
@ManagedBean(name="themeService", eager = true)
@ApplicationScoped
public class ThemeService {
     
    private List<Theme> themes;
     
    @PostConstruct
    public void init() {
        themes = new ArrayList<Theme>();
        themes.add(new Theme(0, "Mikolaja", "santa"));
        themes.add(new Theme(1, "Gwiazde", "star"));
        themes.add(new Theme(2, "Balwana", "snowman"));
        themes.add(new Theme(3, "Aniola", "angel"));
        themes.add(new Theme(4, "Choinke", "chrismastree"));
    }
     
    public List<Theme> getThemes() {
        return themes;
    } 
}