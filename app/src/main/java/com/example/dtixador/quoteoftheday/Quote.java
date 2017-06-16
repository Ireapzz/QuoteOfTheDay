package com.example.dtixador.quoteoftheday;

public class Quote {

    private int id;
    private String mAuthor;
    private String mQuote;
    private int like;
    private int dislike;
    private boolean fav;

    Quote() {}

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmQuote() {
        return mQuote;
    }

    public void setmQuote(String mQuote) {
        this.mQuote = mQuote;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        this.fav = fav;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
