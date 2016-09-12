/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package co.com.reservationsv1.ejb;

import co.com.reservationsv1.controladores.UsuariosJpaController;
import co.com.reservationsv1.modelo.Usuarios;
import co.com.reservationsv1.utilidades.Mensajes;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author disable
 */
@ManagedBean(name="ejbLogin")
@RequestScoped
public class ejbLogin implements Serializable{
    private Logger loger = Logger.getLogger("ejbUsuario");
    public Usuarios usuario;
    public UsuariosJpaController usuarioControlador;
    private boolean cierre;
    private boolean login;
    private String pagina = "principal.reservationsv1";
    public List<String> images;
    
    public ejbLogin() {
        loger.log(Level.INFO, "ejbUsuario");
        
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("user", null);
        System.out.println("usuario: " +sessionMap.get("user"));
        
        usuario = new Usuarios();
        login = true;
        cierre = false;
        usuarioControlador = new UsuariosJpaController();
        images = new ArrayList<String>();
        images.add("imglogin.png");
        images.add("imgreunion.png");
        images.add("imgcumple.png");
        
    }
    
    public void login(){
        System.out.println("login:: "+usuario.getNombre()+"--"+usuario.getPassword());
        try {
            
            if(usuarioControlador.login(usuario)){
                pagina = "principal.reservationsv1";
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                Map<String, Object> sessionMap = externalContext.getSessionMap();
                sessionMap.put("user", usuario.getNombre());
                System.out.println("usuario: " +sessionMap.get("user"));
                
                login = false;
                cierre = true;
                context.redirect(pagina);
            }else{
                pagina = "index.reservationsv1";
                FacesContext fcontext = FacesContext.getCurrentInstance();
         
                fcontext.addMessage(null, new FacesMessage("Successful",  "Your message: " + "invalido") );
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(pagina);
                Mensajes.agregarErrorMensaje("¡Error: Usuario inválido!", null);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ejbLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void logout() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            session.invalidate();
            pagina = "index.reservationsv1";
            context.redirect(pagina);
        } catch (IOException ex) {
            Logger.getLogger(ejbLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public UsuariosJpaController getUsuarioControlador() {
        return usuarioControlador;
    }

    public void setUsuarioControlador(UsuariosJpaController usuarioControlador) {
        this.usuarioControlador = usuarioControlador;
    }

    public boolean isCierre() {
        return cierre;
    }

    public void setCierre(boolean cierre) {
        this.cierre = cierre;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
    
}
