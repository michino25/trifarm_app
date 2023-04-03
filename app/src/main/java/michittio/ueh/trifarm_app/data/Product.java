package michittio.ueh.trifarm_app.data;



public class Product {
    String name;
    String description;
    String image;
    String price;
    String sold;
    String id_category;

    public  Product() {

    }

    public Product( String name, String description, String image, String price, String sold, String id_category) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.sold = sold;
        this.id_category = id_category;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getId_category() {
        return id_category;
    }

    public void setId_category(String id_category) {
        this.id_category = id_category;
    }
}
