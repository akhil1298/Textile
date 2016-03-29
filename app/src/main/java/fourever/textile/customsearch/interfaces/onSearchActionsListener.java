package fourever.textile.customsearch.interfaces;

/**
 * Created by shahroz on 1/12/2016.
 */
public interface onSearchActionsListener {
    void onItemClicked(String item);
    void showProgress(boolean show);
    void Dialog_progressBar(boolean show);
    void listEmpty();
    void onScroll();
    void error(String localizedMessage);
}
