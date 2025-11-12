/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package crud;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import java.sql.ResultSetMetaData;
/**
 *
 * @author User
 */
    public class crud {
    private Connection Koneksidb;
    private String username="root";
    private String password="";
    private String dbname="db_agama"; 
    private String urlKoneksi="jdbc:mysql://localhost/"+dbname;
    public boolean duplikasi=false;

    public String CEK_NAME_ARTIST, CEK_DESC_ARTIST, CEK_STATUS_ARTIST = null;
    public String CEK_ARTIST_ID_ALBUM, CEK_NAME_ALBUM, CEK_DESC_ALBUM, CEK_STATUS_ALBUM = null;
    public String CEK_NAME_CATEGORY, CEK_DESC_CATEGORY, CEK_STATUS_CATEGORY = null;
    public String CEK_CATEGORY_ID_SONG, CEK_ALBUM_ID_SONG, CEK_TITLE_SONG, CEK_DURATION_SONG, CEK_FILE_SONG, CEK_STATUS_SONG = null;

    
    public crud(){
        try {
            Driver dbdriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(dbdriver);
            Koneksidb=DriverManager.getConnection(urlKoneksi,username,password);
            System.out.print("Database Terkoneksi");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e.toString());
        }
    }
    
    public void simpanArtist01(String id, String name, String description, String status) {
        try {
            String sqlsimpan = "insert into Artist(id, name, description, status) value"
                    + " ('" + id + "', '" + name + "', '" + description + "', '" + status + "')";
            String sqlcari = "select*from Artist where id='" + id + "'";

            Statement cari = Koneksidb.createStatement();
            ResultSet data = cari.executeQuery(sqlcari);

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID Artist sudah terdaftar");
            } else {
                Statement perintah = Koneksidb.createStatement();
                perintah.execute(sqlsimpan);
                JOptionPane.showMessageDialog(null, "Data Artist berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void simpanArtist02(String id, String name, String description, String status){
        try {
            String sqlsimpan="INSERT INTO Artist (id, name, description, status) VALUES (?, ?, ?, ?)";
            String sqlcari= "SELECT*FROM Artist WHERE id = ?";
            
            PreparedStatement cari = Koneksidb.prepareStatement(sqlcari);
            cari.setString(1, id);
            ResultSet data = cari.executeQuery();
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Artis sudah terdaftar");
                this.duplikasi = true;
                this.CEK_NAME_ARTIST = data.getString("name");
                this.CEK_DESC_ARTIST = data.getString("description");
                this.CEK_STATUS_ARTIST = data.getString("status");
            } else {
                this.duplikasi = false;
                this.CEK_NAME_ARTIST = null;
                this.CEK_DESC_ARTIST = null;
                this.CEK_STATUS_ARTIST = null;
                
                PreparedStatement perintah = Koneksidb.prepareStatement(sqlsimpan);
                perintah.setString(1, id);
                perintah.setString(2, name);
                perintah.setString(3, description);
                perintah.setString(4, status);
                perintah.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Artis berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void ubahArtist(String id, String name, String description, String status){
        try {
            String sqlubah="UPDATE Artist SET name = ?, description = ?, status = ? WHERE id = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlubah);
            perintah.setString(1, name);
            perintah.setString(2, description);
            perintah.setString(3, status);
            perintah.setString(4, id); 
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Artis berhasil diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void hapusArtist(String id){
        try {
            String sqlhapus="DELETE FROM Artist WHERE id = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlhapus);
            perintah.setString(1, id);
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Artis berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void tampilDataArtist(JTable komponentabel, String SQL){
        try {
            PreparedStatement perintah = Koneksidb.prepareStatement(SQL);
            ResultSet data = perintah.executeQuery();
            ResultSetMetaData meta = data.getMetaData();
            int jumlahkolom = meta.getColumnCount();
            DefaultTableModel modeltabel = new DefaultTableModel();
            
            modeltabel.addColumn("ID");
            modeltabel.addColumn("Name");
            modeltabel.addColumn("Description");
            modeltabel.addColumn("Status");
            
            while(data.next()){
                Object[] row = new Object[jumlahkolom];
                for(int i=1; i<=jumlahkolom; i++){
                    row[i-1]=data.getObject(i);
                }
                modeltabel.addRow(row);
            }
            komponentabel.setModel(modeltabel);
        } catch (Exception e) {
        }
    }

    public void simpanAlbum02(String id, String artist_id, String name, String description, String status){
        try {
            String sqlsimpan="INSERT INTO Album (id, artist_id, name, description, status) VALUES (?, ?, ?, ?, ?)";
            String sqlcari= "SELECT*FROM Album WHERE id = ?";
            
            PreparedStatement cari = Koneksidb.prepareStatement(sqlcari);
            cari.setString(1, id);
            ResultSet data = cari.executeQuery();
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Album sudah terdaftar");
                this.duplikasi = true;
                this.CEK_ARTIST_ID_ALBUM = data.getString("artist_id");
                this.CEK_NAME_ALBUM = data.getString("name");
                this.CEK_DESC_ALBUM = data.getString("description");
                this.CEK_STATUS_ALBUM = data.getString("status");
            } else {
                this.duplikasi = false;
                this.CEK_ARTIST_ID_ALBUM = null;
                this.CEK_NAME_ALBUM = null;
                this.CEK_DESC_ALBUM = null;
                this.CEK_STATUS_ALBUM = null;
                
                PreparedStatement perintah = Koneksidb.prepareStatement(sqlsimpan);
                perintah.setString(1, id);
                perintah.setString(2, artist_id);
                perintah.setString(3, name);
                perintah.setString(4, description);
                perintah.setString(5, status);
                perintah.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Album berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void ubahAlbum(String id, String artist_id, String name, String description, String status){
        try {
            String sqlubah="UPDATE Album SET artist_id = ?, name = ?, description = ?, status = ? WHERE id = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlubah);
            perintah.setString(1, artist_id);
            perintah.setString(2, name);
            perintah.setString(3, description);
            perintah.setString(4, status);
            perintah.setString(5, id); // ID sebagai parameter terakhir
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Album berhasil diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void hapusAlbum(String id){
        try {
            String sqlhapus="DELETE FROM Album WHERE id = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlhapus);
            perintah.setString(1, id);
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Album berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void tampilDataAlbum(JTable komponentabel, String SQL){
        try {
            PreparedStatement perintah = Koneksidb.prepareStatement(SQL);
            ResultSet data = perintah.executeQuery();
            ResultSetMetaData meta = data.getMetaData();
            int jumlahkolom = meta.getColumnCount();
            DefaultTableModel modeltabel = new DefaultTableModel();
            
            modeltabel.addColumn("ID");
            modeltabel.addColumn("Artist ID");
            modeltabel.addColumn("Name");
            modeltabel.addColumn("Description");
            modeltabel.addColumn("Status");
            
            while(data.next()){
                Object[] row = new Object[jumlahkolom];
                for(int i=1; i<=jumlahkolom; i++){
                    row[i-1]=data.getObject(i);
                }
                modeltabel.addRow(row);
            }
            komponentabel.setModel(modeltabel);
        } catch (Exception e) {
        }
    }

    public void simpanCategory01(String id, String name, String description, String status) {
        try {
            String sqlsimpan = "insert into PlaylistSongCategory(id, name, description, status) value"
                    + " ('" + id + "', '" + name + "', '" + description + "', '" + status + "')";
            String sqlcari = "select*from PlaylistSongCategory where id='" + id + "'";

            Statement cari = Koneksidb.createStatement();
            ResultSet data = cari.executeQuery(sqlcari);

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID Category sudah terdaftar");
            } else {
                Statement perintah = Koneksidb.createStatement();
                perintah.execute(sqlsimpan);
                JOptionPane.showMessageDialog(null, "Data Category berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void simpanCategory02(String id, String name, String description, String status){
        try {
            String sqlsimpan="INSERT INTO PlaylistSongCategory (id, name, description, status) VALUES (?, ?, ?, ?)";
            String sqlcari= "SELECT*FROM PlaylistSongCategory WHERE id = ?";
            
            PreparedStatement cari = Koneksidb.prepareStatement(sqlcari);
            cari.setString(1, id);
            ResultSet data = cari.executeQuery();
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Kategori sudah terdaftar");
                this.duplikasi = true;
                this.CEK_NAME_CATEGORY = data.getString("name");
                this.CEK_DESC_CATEGORY = data.getString("description");
                this.CEK_STATUS_CATEGORY = data.getString("status");
            } else {
                this.duplikasi = false;
                this.CEK_NAME_CATEGORY = null;
                this.CEK_DESC_CATEGORY = null;
                this.CEK_STATUS_CATEGORY = null;
                
                PreparedStatement perintah = Koneksidb.prepareStatement(sqlsimpan);
                perintah.setString(1, id);
                perintah.setString(2, name);
                perintah.setString(3, description);
                perintah.setString(4, status);
                perintah.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Kategori berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void ubahCategory(String id, String name, String description, String status){
        try {
            String sqlubah="UPDATE PlaylistSongCategory SET name = ?, description = ?, status = ? WHERE id = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlubah);
            perintah.setString(1, name);
            perintah.setString(2, description);
            perintah.setString(3, status);
            perintah.setString(4, id); // ID sebagai parameter terakhir
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Kategori berhasil diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void hapusCategory(String id){
        try {
            String sqlhapus="DELETE FROM PlaylistSongCategory WHERE id = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlhapus);
            perintah.setString(1, id);
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Kategori berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void tampilDataCategory(JTable komponentabel, String SQL){
        try {
            PreparedStatement perintah = Koneksidb.prepareStatement(SQL);
            ResultSet data = perintah.executeQuery();
            ResultSetMetaData meta = data.getMetaData();
            int jumlahkolom = meta.getColumnCount();
            DefaultTableModel modeltabel = new DefaultTableModel();
            
            modeltabel.addColumn("ID");
            modeltabel.addColumn("Name");
            modeltabel.addColumn("Description");
            modeltabel.addColumn("Status");
            
            while(data.next()){
                Object[] row = new Object[jumlahkolom];
                for(int i=1; i<=jumlahkolom; i++){
                    row[i-1]=data.getObject(i);
                }
                modeltabel.addRow(row);
            }
            komponentabel.setModel(modeltabel);
        } catch (Exception e) {
        }
    }

    public void simpanAlbum01(String id, String artist_id, String name, String description, String status) {
        try {
            String sqlsimpan = "insert into Album(id, artist_id, name, description, status) value"
                    + " ('" + id + "', '" + artist_id + "', '" + name + "', '" + description + "', '" + status + "')";
            String sqlcari = "select*from Album where id='" + id + "'";

            Statement cari = Koneksidb.createStatement();
            ResultSet data = cari.executeQuery(sqlcari);

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID Album sudah terdaftar");
            } else {
                Statement perintah = Koneksidb.createStatement();
                perintah.execute(sqlsimpan);
                JOptionPane.showMessageDialog(null, "Data Album berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void simpanSong01(String id, String playlist_song_category_id, String album_id, String title, String duration, String file, String status) {
        try {
            String sqlsimpan = "insert into PlaylistSong(id, playlist_song_category_id, album_id, title, duration, file, status) value"
                    + " ('" + id + "', '" + playlist_song_category_id + "', '" + album_id + "', '" + title + "', '" + duration + "', '" + file + "', '" + status + "')";
            String sqlcari = "select*from PlaylistSong where id='" + id + "'";

            Statement cari = Koneksidb.createStatement();
            ResultSet data = cari.executeQuery(sqlcari);

            if (data.next()) {
                JOptionPane.showMessageDialog(null, "ID Song sudah terdaftar");
            } else {
                Statement perintah = Koneksidb.createStatement();
                perintah.execute(sqlsimpan);
                JOptionPane.showMessageDialog(null, "Data Song berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void simpanSong02(String id, String playlist_song_category_id, String album_id, String title, String duration, String file, String status){
        try {
            String sqlsimpan="INSERT INTO PlaylistSong (id, playlist_song_category_id, album_id, title, duration, file, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            String sqlcari= "SELECT*FROM PlaylistSong WHERE id = ?";
            
            PreparedStatement cari = Koneksidb.prepareStatement(sqlcari);
            cari.setString(1, id);
            ResultSet data = cari.executeQuery();
            
            if (data.next()){
                JOptionPane.showMessageDialog(null, "ID Lagu sudah terdaftar");
                this.duplikasi = true;
                this.CEK_CATEGORY_ID_SONG = data.getString("playlist_song_category_id");
                this.CEK_ALBUM_ID_SONG = data.getString("album_id");
                this.CEK_TITLE_SONG = data.getString("title");
                this.CEK_DURATION_SONG = data.getString("duration");
                this.CEK_FILE_SONG = data.getString("file");
                this.CEK_STATUS_SONG = data.getString("status");
            } else {
                this.duplikasi = false;
                this.CEK_CATEGORY_ID_SONG = null;
                this.CEK_ALBUM_ID_SONG = null;
                this.CEK_TITLE_SONG = null;
                this.CEK_DURATION_SONG = null;
                this.CEK_FILE_SONG = null;
                this.CEK_STATUS_SONG = null;
                
                PreparedStatement perintah = Koneksidb.prepareStatement(sqlsimpan);
                perintah.setString(1, id);
                perintah.setString(2, playlist_song_category_id);
                perintah.setString(3, album_id);
                perintah.setString(4, title);
                perintah.setString(5, duration);
                perintah.setString(6, file);
                perintah.setString(7, status);
                perintah.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Lagu berhasil disimpan");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void ubahSong(String id, String playlist_song_category_id, String album_id, String title, String duration, String file, String status){
        try {
            String sqlubah="UPDATE PlaylistSong SET playlist_song_category_id = ?, album_id = ?, title = ?, duration = ?, file = ?, status = ? WHERE id = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlubah);
            perintah.setString(1, playlist_song_category_id);
            perintah.setString(2, album_id);
            perintah.setString(3, title);
            perintah.setString(4, duration);
            perintah.setString(5, file);
            perintah.setString(6, status);
            perintah.setString(7, id); // ID sebagai parameter terakhir
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Lagu berhasil diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void hapusSong(String id){
        try {
            String sqlhapus="DELETE FROM PlaylistSong WHERE id = ?";
            PreparedStatement perintah = Koneksidb.prepareStatement(sqlhapus);
            perintah.setString(1, id);
            perintah.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Lagu berhasil dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void tampilDataSong(JTable komponentabel, String SQL){
        try {
            PreparedStatement perintah = Koneksidb.prepareStatement(SQL);
            ResultSet data = perintah.executeQuery();
            ResultSetMetaData meta = data.getMetaData();
            int jumlahkolom = meta.getColumnCount();
            DefaultTableModel modeltabel = new DefaultTableModel();
            
            modeltabel.addColumn("ID");
            modeltabel.addColumn("Category ID");
            modeltabel.addColumn("Album ID");
            modeltabel.addColumn("Title");
            modeltabel.addColumn("Duration");
            modeltabel.addColumn("File");
            modeltabel.addColumn("Status");
            
            while(data.next()){
                Object[] row = new Object[jumlahkolom];
                for(int i=1; i<=jumlahkolom; i++){
                    row[i-1]=data.getObject(i);
                }
                modeltabel.addRow(row);
            }
            komponentabel.setModel(modeltabel);
        } catch (Exception e) {
        }
    }
    
} 
    
