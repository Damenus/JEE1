package pl.darczuk.studia.java.view.converters;

import pl.darczuk.studia.java.ForestService;
import pl.darczuk.studia.java.entities.Elf;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.servlet.http.HttpServletResponse;

@ManagedBean
@RequestScoped
public class ElfConverter implements Converter {

    @ManagedProperty("#{forestService}")
    private ForestService forestService;

    public void setForestService(ForestService forestService) { this.forestService = forestService; }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        if (s == null) {
            return null;
        }

        Elf elf = forestService.findElf(Integer.valueOf(s));

        if (elf == null) {
            facesContext.getExternalContext().setResponseStatus(HttpServletResponse.SC_NOT_FOUND);
            facesContext.responseComplete();
        }

        return elf;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        Elf elf = (Elf) o;
        return elf.getId() != null ? Integer.toString(elf.getId()) : null;
    }
}
