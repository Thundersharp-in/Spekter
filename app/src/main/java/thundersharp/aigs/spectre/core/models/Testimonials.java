package thundersharp.aigs.spectre.core.models;

public class Testimonials {

    public String photoUri;
    public String message;
    public String name;
    public String designation;

    public Testimonials(String photoUri, String message, String name, String designation) {
        this.photoUri = photoUri;
        this.message = message;
        this.name = name;
        this.designation = designation;
    }
}
