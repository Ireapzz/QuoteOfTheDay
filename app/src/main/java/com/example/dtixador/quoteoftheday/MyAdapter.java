package com.example.dtixador.quoteoftheday;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>  {

    private static int likeFlag;
    private static int dislikeFlag;
    private static boolean favFlag = false;
    private ArrayList<Quote> arrayOfQuote = new ArrayList<>();
    private ArrayList<FavoriteQuote> favoriteQuote = new ArrayList<>();
    private Context mContext;
    private QuotesBDD quotesBDD;
    private String isConnect;

    public MyAdapter(ArrayList<Quote> arrayList, QuotesBDD bddquote, String connection) {
        arrayOfQuote = arrayList;
        quotesBDD = bddquote;
        isConnect = connection;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quote, parent, false);
        mContext = parent.getContext();

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("quotes");

        quotesBDD.insertQuote(position, arrayOfQuote.get(position));

        if (!isConnect.equals("NO_SERVICE")) {

            setOnLineTextView(holder, position);
            setOnlineLikeClick(holder, position, myRef);
            setOnlineDislikeClick(holder, position, myRef);
            setOnlineFavOrShareClick(holder, position, myRef);

        } else {
           setOffLineView(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return arrayOfQuote.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle;
        private TextView txtQuote;
        private ImageView imgLike;
        private ImageView imgDislike;
        private ImageView imgFav;
        private ImageView imgShare;
        private TextView txtNbLike;
        private TextView txtNbDislike;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.quote_title);
            txtQuote = (TextView) itemView.findViewById(R.id.quote_text);
            imgLike = (ImageView) itemView.findViewById(R.id.like);
            imgDislike = (ImageView) itemView.findViewById(R.id.dislike);
            imgFav = (ImageView) itemView.findViewById(R.id.add_fav);
            imgShare = (ImageView) itemView.findViewById(R.id.share);
            txtNbLike = (TextView) itemView.findViewById(R.id.nbLike);
            txtNbDislike = (TextView) itemView.findViewById(R.id.nbDislike);

        }

    }

    private void setOffLineView(final MyViewHolder holder, final int position) {
        holder.txtTitle.setText(arrayOfQuote.get(position).getmAuthor());
        holder.txtQuote.setText(arrayOfQuote.get(position).getmQuote());
        holder.imgLike.setVisibility(View.GONE);
        holder.imgDislike.setVisibility(View.GONE);
        holder.imgFav.setVisibility(View.GONE);
        holder.imgShare.setVisibility(View.GONE);
    }

    private void setOnLineTextView(final MyViewHolder holder, final int position) {
        holder.txtTitle.setText(arrayOfQuote.get(position).getmAuthor());
        holder.txtQuote.setText(arrayOfQuote.get(position).getmQuote());
        holder.txtNbLike.setText(String.valueOf(arrayOfQuote.get(position).getLike()));
        holder.txtNbDislike.setText(String.valueOf(arrayOfQuote.get(position).getDislike()));
    }

    private void setOnlineLikeClick(final MyViewHolder holder, final int position, final DatabaseReference myRef) {
        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeFlag == 0 && dislikeFlag == 0) {
                    likeFlag = 1;
                    holder.imgLike.setImageResource(R.drawable.like);
                    arrayOfQuote.get(position).setLike(arrayOfQuote.get(position).getLike() + 1);
                    myRef.child(arrayOfQuote.get(position).getmAuthor()).child("like")
                            .setValue(arrayOfQuote.get(position).getLike());

                    ////////  recherche quote par author
                    Quote quoteFromBdd = quotesBDD.getQuoteWithAuthor(arrayOfQuote.get(position).getmAuthor());
                    if (quoteFromBdd != null) {
                        quoteFromBdd.setLike(arrayOfQuote.get(position).getLike());
                        quotesBDD.updateQuote(quoteFromBdd.getId(), quoteFromBdd);
                    }

                    holder.txtNbLike.setText(String.valueOf(arrayOfQuote.get(position).getLike()));
                } else if (dislikeFlag == 1) {

                } else {
                    likeFlag = 0;
                    holder.imgLike.setImageResource(R.drawable.add_like);
                    arrayOfQuote.get(position).setLike(arrayOfQuote.get(position).getLike() - 1);
                    myRef.child(arrayOfQuote.get(position).getmAuthor()).child("like")
                            .setValue(arrayOfQuote.get(position).getLike());
                    holder.txtNbLike.setText(String.valueOf(arrayOfQuote.get(position).getLike()));

                    ////////  recherche quote par author
                    Quote quoteFromBdd = quotesBDD.getQuoteWithAuthor(arrayOfQuote.get(position).getmAuthor());
                    if (quoteFromBdd != null) {
                        quoteFromBdd.setLike(arrayOfQuote.get(position).getLike());
                        quotesBDD.updateQuote(quoteFromBdd.getId(), quoteFromBdd);
                    }


                }
            }
        });
    }

    private void setOnlineDislikeClick(final MyViewHolder holder, final int position, final DatabaseReference myRef) {
        holder.imgDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dislikeFlag == 0 && likeFlag == 0) {
                    holder.imgDislike.setImageResource(R.drawable.dislike);
                    arrayOfQuote.get(position).setDislike(arrayOfQuote.get(position).getDislike() + 1);
                    myRef.child(arrayOfQuote.get(position).getmAuthor()).child("dislike")
                            .setValue(arrayOfQuote.get(position).getDislike());

                    ////////  recherche quote par author
                    Quote quoteFromBdd = quotesBDD.getQuoteWithAuthor(arrayOfQuote.get(position).getmAuthor());
                    if (quoteFromBdd != null) {
                        quoteFromBdd.setDislike(arrayOfQuote.get(position).getDislike());
                        quotesBDD.updateQuote(quoteFromBdd.getId(), quoteFromBdd);
                    }

                    holder.txtNbDislike.setText(String.valueOf(arrayOfQuote.get(position).getDislike()));
                    dislikeFlag = 1;
                } else if (likeFlag == 1) {

                } else {
                    holder.imgDislike.setImageResource(R.drawable.add_dislike);
                    arrayOfQuote.get(position).setDislike(arrayOfQuote.get(position).getDislike() - 1);
                    myRef.child(arrayOfQuote.get(position).getmAuthor()).child("dislike")
                            .setValue(arrayOfQuote.get(position).getDislike());

                    Quote quoteFromBdd = quotesBDD.getQuoteWithAuthor(arrayOfQuote.get(position).getmAuthor());
                    if (quoteFromBdd != null) {
                        quoteFromBdd.setDislike(arrayOfQuote.get(position).getDislike());
                        quotesBDD.updateQuote(quoteFromBdd.getId(), quoteFromBdd);
                    }

                    holder.txtNbDislike.setText(String.valueOf(arrayOfQuote.get(position).getDislike()));
                    dislikeFlag = 0;
                }
            }
        });
    }

    private void setOnlineFavOrShareClick(final MyViewHolder holder, final int position, final DatabaseReference myRef) {
        holder.imgFav.setOnClickListener(new View.OnClickListener() {
            @Override


            ////////////////////////////////////////////////////////////////////////////////////////
            //// TODO: 18/05/2017 REVOIR CLICK SUR FAVORI AJOUT DANS BDD                     //////
            //////////////////////////////////////////////////////////////////////////////////////
            public void onClick(View v) {
                if (!favFlag) {
                    holder.imgFav.setImageResource(R.drawable.favo);
                    favFlag = true;
                    FavoriteQuote newFavQuote = new FavoriteQuote(arrayOfQuote.get(position));
                    favoriteQuote.add(newFavQuote);
                    if (favoriteQuote.size() != 0) {
                        for (int i = 0; i < favoriteQuote.size(); i++) {
                            Log.d("test2", favoriteQuote.get(i).getmAuthor());
                        }
                    }

                } else {
                    holder.imgFav.setImageResource(R.drawable.add_favo);
                    favFlag = false;

                    favoriteQuote.remove(arrayOfQuote.get(position));
                    if (favoriteQuote.size() != 0) {
                        for (int i = 0; i < favoriteQuote.size(); i++) {
                            Log.d("test2", favoriteQuote.get(i).getmAuthor());
                        }
                    } else {
                        Log.d("test2", "array vide");
                    }
                }
            }
        });

        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}