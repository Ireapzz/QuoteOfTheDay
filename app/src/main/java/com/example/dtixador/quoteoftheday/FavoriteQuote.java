package com.example.dtixador.quoteoftheday;

public class FavoriteQuote {
    private String mAuthor;
    private String mQuote;

    FavoriteQuote(Quote quote) {
        mAuthor = quote.getmAuthor();
        mQuote = quote.getmQuote();
    }

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
}
