package michittio.ueh.trifarm_app;

public interface OnProductItemClickListener {
    void onProductItemClick(int position);
    void onProductItemDeleteClick(int position);
    void onCartTotalChanged(int total);
}
