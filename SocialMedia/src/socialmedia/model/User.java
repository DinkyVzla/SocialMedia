package socialmedia.model;

/**
 * Clase que representa un user en la social media
 * Cada user tiene un name que lo identifica en el sistema
 * Es super simple, solo guarda el nombre del usuario
 * 
 * @author JuanFerreira
 * @version 1.0
 */
public class User {

    private String name;

    /**
     * Constructor pa crear un nuevo user
     * 
     * @param name el name del user, no puede ser null
     */
    public User(String name) {
        this.name = name;
    }

    /**
     * Getter pa obtener el name del user
     * 
     * @return el name del user como string
     */
    public String getName() {
        return name;
    }

    /**
     * Setter pa cambiar el name del user
     * 
     * @param name el nuevo name del user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Convierte el user a string, que es basicamente su name
     * Se usa cuando queres mostrar el user en texto
     * 
     * @return el name del user
     */
    @Override
    public String toString() {
        return name;
    }
}
