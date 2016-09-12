/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.reservationsv1.ejb;

import co.com.reservationsv1.modelo.Usuarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author disable
 */
@ManagedBean(name="ejbPrincipal")
@RequestScoped
public class ejbPrincipal implements Serializable{
    
    private Logger loger = Logger.getLogger("ejbUsuario");
    public Usuarios usuario;
    public List<Usuarios> listaUsuarios;
    public List<String> images;
    public String pagina = "index.reservationsv1";
    
    public ejbPrincipal() {
        loger.log(Level.INFO, "ejbUsuario");
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        System.out.println("usuario: " +sessionMap.get("user"));
        usuario = new Usuarios();
        images = new ArrayList<String>();
        images.add("imgout.png");
        images.add("imgreunion.png");
        images.add("imgcumple.png");
    }
    
    public void direccion(String image){
        loger.log(Level.INFO, "direccionar...");
        if(image.equalsIgnoreCase("imgout.png")){
            pagina = "index.reservationsv1";
            direccionar(pagina);
        }else if(image.equalsIgnoreCase("imgcumple.png")){
            pagina = "cumple.reservationsv1";
            direccionar(pagina);
        }else if(image.equalsIgnoreCase("imgreunion.png")){
            pagina = "reunion.reservationsv1";
            direccionar(pagina);
        }else{
            
        }
    }
    
    public void direccionar(String pagina){
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        System.out.println("usuario: " +sessionMap.get("user"));
        try {
            context.redirect(pagina);
        } catch (IOException ex) {
            Logger.getLogger(ejbPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Logger getLoger() {
        return loger;
    }

    public void setLoger(Logger loger) {
        this.loger = loger;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public List<Usuarios> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }
    
}
