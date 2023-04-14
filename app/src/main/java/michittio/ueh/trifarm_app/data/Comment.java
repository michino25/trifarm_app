package michittio.ueh.trifarm_app.data;


public class Comment {

    String idUser;
    String username;
    String avt;
    String cmt;
    String like;

    public Comment() {
    }

    public Comment(String idUser, String username, String avt, String cmt, String like) {
        this.idUser = idUser;
        this.username = username;
        this.avt = avt;
        this.cmt = cmt;
        this.like = like;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getUsername() {
        return username;
    }

    public String getAvt() {
        return avt;
    }

    public String getCmt() {
        return cmt;
    }

    public String getLike() {
        return like;
    }
}
