/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author hp
 */
public class MediaPlayer extends Application {

    int i=1;
    int play_num=1;
    Player newPlayer;
    FileChooser filechooser;
    String playlist[] = new String[100];

    @Override
    public void start(final Stage primaryStage) throws UnsupportedEncodingException {

        primaryStage.centerOnScreen();
        primaryStage.setMinWidth(700);
        playlist[0] = "null";
        while (playlist[0].endsWith("null") || !playlist[0].endsWith("mp4")){
            playlist[0] = new File(setUp()).toURI().toString();
            if(playlist[0].endsWith("null") || !playlist[0].endsWith("mp4"))
                JOptionPane.showMessageDialog(null, "Please select .mp4 file only", "InfoBox: " + "Invalid Selection", JOptionPane.INFORMATION_MESSAGE);
        }

        MenuItem open = new MenuItem("Open");
        Menu File = new Menu("File");
        Menu Playlist = new Menu("Playlist");
        MenuItem addPlaylist = new MenuItem("Add to Playlist");
        final MenuBar menu = new MenuBar();

        File.getItems().add(open);
        menu.getMenus().add(File);
        Playlist.getItems().add(addPlaylist);
        menu.getMenus().add(Playlist);

        filechooser = new FileChooser();

        open.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                newPlayer.player.pause();
                File file = filechooser.showOpenDialog(primaryStage);
                if(file!=null){
                    try{
                        newPlayer = new Player(file.toURI().toURL().toExternalForm());
                        newPlayer.setTop(menu);
                        Scene newScene = new Scene(newPlayer,720,535,Color.BLACK);
                        newPlayer.view.fitWidthProperty().bind(newScene.widthProperty());
                        primaryStage.setScene(newScene);
                        primaryStage.show();
                    }
                    catch(MalformedURLException ex){
                    }
                }
            }
        });
        addPlaylist.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                newPlayer.player.pause();
                File file = filechooser.showOpenDialog(primaryStage);
                if(file!=null){
                    try{
                        playlist[i]=file.toURI().toURL().toExternalForm();
                        i++;
                    }
                    catch(MalformedURLException ex){
                    }
                }
            }
        });

        newPlayer = new Player(playlist[0]);
        newPlayer.setTop(menu);
        Scene newScene = new Scene(newPlayer,720,535,Color.BLACK);
        newPlayer.view.fitWidthProperty().bind(newScene.widthProperty());
        primaryStage.setScene(newScene);
        primaryStage.show();

        newPlayer.player.setOnEndOfMedia(new Runnable(){
            @Override
            public void run(){
                check(newPlayer,primaryStage,menu);
            }
        });
        newPlayer.view.setOnMouseClicked(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent click){
                if(click.getClickCount()==2){
                    if(primaryStage.isFullScreen()){
                        primaryStage.setFullScreen(false);
                    }
                    else{
                        primaryStage.setFullScreen(true);
                    }
                }
            }
        });
    }
    public void check(Player player,final Stage primaryStage,final MenuBar menu){
        if(playlist[play_num]==null){
            play_num=0;
        }
        if(player.player.getCurrentTime().greaterThanOrEqualTo(player.player.getTotalDuration())){
            newPlayer = new Player(playlist[play_num]);
            play_num++;
            newPlayer.setTop(menu);
            Scene newScene = new Scene(newPlayer,720,535,Color.BLACK);
            newPlayer.view.fitWidthProperty().bind(newScene.widthProperty());
            primaryStage.setScene(newScene);
            primaryStage.show();
            newPlayer.view.setOnMouseClicked(new EventHandler<MouseEvent>(){
                public void handle(MouseEvent click){
                    if(click.getClickCount()==2){
                        if(primaryStage.isFullScreen()){
                            primaryStage.setFullScreen(false);
                        }
                        else{
                            primaryStage.setFullScreen(true);
                        }
                    }
                }
            });
            newPlayer.player.setOnEndOfMedia(new Runnable(){
                public void run(){
                    check(newPlayer,primaryStage,menu);
                }
            });
        }
    }

    public static String setUp(){
        JFileChooser openFile = new JFileChooser();
        openFile.setCurrentDirectory(new java.io.File("/"));
        openFile.showOpenDialog(null);
        openFile.setDialogTitle("Select New Video File");
        openFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
        openFile.setAcceptAllFileFilterUsed(false);
        return("" + openFile.getSelectedFile());
    }

    public static void main(String[] args) {
        launch(args);
    }
}