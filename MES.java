/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeper;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Admin
 */
public class MES extends JFrame{
    
    private class MineTile extends JButton{
        int r;
        int c;
        
        public MineTile(int r,int c){
            this.r=r;
            this.c=c;
        }
    }
    
    int titleSize =70;
    int numRows=15;
    int numCols=numRows;
    int boardWidth=numCols*titleSize/2;
    int boardHeight=numRows*titleSize/2;
    JLabel textLabel;
    JPanel textPanel,boardPanel;
    
    int mineCount=20;
    Random random=new Random();
    MineTile[][] board=new MineTile[numRows][numCols];
    ArrayList<MineTile> mineList;
    int tilesClicked=0;
    boolean gameOver=false;
    public MES(){
        super("Minesweeper");
        
        textLabel=new JLabel();
        textLabel.setFont(new Font("Time New Roman",Font.BOLD,25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper :"+mineCount);
        textLabel.setOpaque(true);
        
        textPanel=new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        add(textPanel,BorderLayout.NORTH);
        
        boardPanel=new JPanel();
        boardPanel.setLayout(new GridLayout(numRows,numCols));
        
        
        for(int r=0;r<numRows;r++){
            for(int c=0;c<numCols;c++){
                MineTile title=new MineTile(r, c);
                board[r][c]=title;
                
                title.setMargin(new Insets(0,0,0,0));
                title.setFont(new Font("Segoe UI Emoji",Font.PLAIN,15));
                title.setText("");
                title.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mousePressed(MouseEvent e){
                        MineTile tile=(MineTile) e.getSource();
                        
                        if(gameOver){
                            return;
                        }
                        
                        if(e.getButton()==MouseEvent.BUTTON1){
                            if(tile.getText()==""){
                                if(mineList.contains(tile)){
                                    revealMines();
                                }else{
                                    checkMines(tile.r,tile.c);
                                }
                            }
                        }else if(e.getButton()==MouseEvent.BUTTON3){
                            if(tile.getText()=="" && tile.isEnabled()){
                                tile.setText("🚩");
                            }else if(tile.getText()=="🚩"){
                                tile.setText("");
                            }
                        }
                    }
                });
                boardPanel.add(title);
            }
        }
        add(boardPanel);
        
        
        setSize(boardWidth, boardHeight);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        setLayout(new BorderLayout());
        
        setMines();
    }
    
    public void setMines(){
        mineList=new ArrayList<MineTile>();
       /* mineList.add(board[2][2]);
        mineList.add(board[2][3]);
        mineList.add(board[5][6]);
        mineList.add(board[3][4]);
        mineList.add(board[1][1]);*/
       int mineLeft=mineCount;
       while(mineLeft>0){
           int r=random.nextInt(numRows);
           int c=random.nextInt(numCols);
           
           MineTile tile=board[r][c];
           if(!mineList.contains(tile)){
               mineList.add(tile);
               mineLeft-=1;
           }
       }
    }
    
    public void revealMines(){
        for(int i=0;i<mineList.size();i++){
            MineTile tile=mineList.get(i);
            tile.setText("💣");
        }
        gameOver=true;
        textLabel.setText("GAME OVER");
    }
    
    public void checkMines(int r,int c){
        if(r<0||c<0||r>=numRows||c>=numCols){
            return ;
        }
        MineTile tile=board[r][c];
        if(!tile.isEnabled()){
            return;
        }
        tile.setEnabled(false);
        tilesClicked+=1;
        int minesFound=0;
        for(int i=r-1;i<=r+1;i++){
            for(int j=c-1;j<=c+1;j++){
                if(i==r&&j==c){
                    continue;
                }else{
                    minesFound+=countMines(i,j);
                }
            }
        }
        if(minesFound>0){
            tile.setText(String.valueOf(minesFound));
        }else{
            tile.setText("");
            for(int i=r-1;i<=r+1;i++){
            for(int j=c-1;j<=c+1;j++){
                if(i==r&&j==c){
                    continue;
                }else{
                    checkMines(i, j);
                }
            }
        }
        }
        if(tilesClicked==numRows*numCols-mineList.size()){
            gameOver=true;
            textLabel.setText("MINE CLEAR !");
        }
    }
    int countMines(int r,int c){
        if(r<0||c<0||r>=numRows||c>=numCols){
            return 0;
        }
        if(mineList.contains(board[r][c])){
            return 1;
        }
        return 0;
        
    }
    public static void main(String[] args) {
        new MES();
    }
}
