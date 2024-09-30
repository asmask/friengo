package com.example.asus.freingo.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.asus.freingo.models.Utilisateur;

public class UtilisateurDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "UtilisateurManager";
    private static final String TABLE_UTILISATEUR = "Utilisateur";
    //Colonnes
    private static final String COLONNE_ID = "id";
    private static final String COLONNE_USERNAME = "username";
    private static final String COLONNE_NOM = "nom";
    private static final String COLONNE_PRENOM = "prenom";
    private static final String COLONNE_EMAIL = "email";
    private static final String COLONNE_PASSWORD = "password";
    private static final String COLONNE_LONGITUDE = "longitude";
    private static final String COLONNE_LATITUDE = "latitude";


    private static final String REQUETE_CREATION_BD = "create table " + TABLE_UTILISATEUR +
            " (" + COLONNE_ID + " integer primary key autoincrement, " + COLONNE_USERNAME
            + " TEXT not null, " + COLONNE_NOM + " TEXT not null," + COLONNE_PRENOM + " TEXT not null," +
            COLONNE_EMAIL + " TEXT ," + COLONNE_PASSWORD + " TEXT ," + COLONNE_LONGITUDE + " TEXT ," + COLONNE_LATITUDE + " TEXT );";


    public UtilisateurDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(REQUETE_CREATION_BD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertUtilisateur(Utilisateur user) {

        SQLiteDatabase maBD = this.getWritableDatabase();

        ContentValues valeurs = new ContentValues();
        valeurs.put(COLONNE_USERNAME, user.getUsername());
        valeurs.put(COLONNE_NOM, user.getNom());
        valeurs.put(COLONNE_PRENOM, user.getPrenom());
        valeurs.put(COLONNE_EMAIL, user.getEmail());
        valeurs.put(COLONNE_PASSWORD, user.getPassword());
        valeurs.put(COLONNE_LONGITUDE, user.getLongitude());
        valeurs.put(COLONNE_LATITUDE, user.getLatitude());

        maBD.insert(TABLE_UTILISATEUR, null, valeurs);

        maBD.close();
    }

    public void  removeUtilisateurById(int id) {
        SQLiteDatabase maBD = this.getWritableDatabase();
        maBD.delete(TABLE_UTILISATEUR, COLONNE_ID + " = "+id, null);
        maBD.close();
    }

    public Utilisateur getUtilisateurById(int id) {
        SQLiteDatabase maBD = this.getReadableDatabase();

        Cursor c =maBD.query(TABLE_UTILISATEUR,  new String[] { COLONNE_ID, COLONNE_USERNAME, COLONNE_NOM, COLONNE_PRENOM,COLONNE_EMAIL,COLONNE_PASSWORD,COLONNE_LONGITUDE,COLONNE_LATITUDE }, COLONNE_ID + " =? " , new String[]{String.valueOf(id)},  null, null, null);
        return cursorToUtilisateur(c);
    }

    private Utilisateur cursorToUtilisateur(Cursor c) {
        // Si la requête ne renvoie pas de résultat.
        if (c==null  || c.getCount() == 0)
            return null;
        c.moveToFirst();
        Utilisateur retUtilisateur = new Utilisateur();
        // Extraction des valeurs depuis le curseur.

        retUtilisateur.setId(c.getInt(0));
        retUtilisateur.setUsername(c.getString(1));
        retUtilisateur.setNom(c.getString(2));
        retUtilisateur.setPrenom(c.getString(3));
        retUtilisateur.setEmail(c.getString(4));
        retUtilisateur.setPassword(c.getString(5));
        retUtilisateur.setLongitude(c.getString(6));
        retUtilisateur.setLatitude(c.getString(7));
        // Ferme le curseur pour libérer les ressources.
        c.close();
        return retUtilisateur;
    }
}
