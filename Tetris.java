import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*; 

class Tetris implements World {
  static final int ROWS = 20; 
  static final int COLUMNS = 10; 
  Tetromino t; 
  SetOfBlocks blocks;
  public boolean pause = false; //[C1]
  Tetris(Tetromino t, SetOfBlocks s) {
    this.t = t;
    this.blocks = s; 
  }
  public void draw(Graphics g) { // world->image
    // System.out.println("World being drawn."); 
    t.draw(g); 
    blocks.draw(g); 
    g.drawRect(0, 0, Tetris.COLUMNS * Block.SIZE, Tetris.ROWS * Block.SIZE); 
    if(pause == true){ //[C5]
      g.drawString("Pause", 50, 100);
    }
    if(blocks.overflow() == true){ //[D2]
      g.drawString("Game Over", 50, 100);
      System.out.println(blocks.score); //[A4]
    }
  }
  public void update() { 
    if(pause == false){   //[C2]
// System.out.println("World getting older."); 
    if (this.landed())
      this.touchdown(); 
    else 
      this.t.move(0, 1);
    }else{}
    // System.out.println( t ); // debug 
    // System.out.println( blocks ); // also debug 
  }
  public boolean hasEnded() { //[D1]
    if(blocks.overflow() == true) {
      return true;
    }else{
      return false;
    }
  } 
  public void keyPressed(KeyEvent e) { // world-key-move
    if (this.landed()) 
      this.touchdown(); 
    else if (e.getKeyCode() == KeyEvent.VK_LEFT ) { //[C3]
      if(pause == false){
        this.t.move(-1,  0); } else{}
    }
    else if (e.getKeyCode() == KeyEvent.VK_RIGHT) { //[C3]
      if(pause == false){
        this.t.move( 1,  0); } else{}
    }
    else if (e.getKeyChar() == ' ') { //[C3]
      if(pause == false){
        this.jumpDown(); }else{}
    }
    else if (e.getKeyChar() == 'p'){ //[C4]
      if(pause == true){pause = false;}
      else if (pause == false){pause = true;}
    }
    // else if (e.getKeyChar() == '2') { this.blocks.eliminateRow(2); } // basic test 
    // else if (e.getKeyChar() == '2') { System.out.println( "Row 2 has " + this.blocks.row(2).count() + " blocks currently." ); } // another basic test 
    // else if (e.getKeyChar() == '2') { System.out.println( "Row 2 full? Answer: " + blocks.fullRow(2) ); } // yet another basic test 
    // else if (e.getKeyChar() == 'e') { this.blocks.eliminateFullRows(); } // another basic test 
    else if (e.getKeyChar() == 'r') { //[C3]   // Rotate CW
      if(pause == false){
      this.t.rotateCW();  
      }else{}
    }
      else this.t.move( 0, 0 );     
  }
  public static void main(String[] args) {
    BigBang game = new BigBang(new Tetris(Tetromino.n(), new SetOfBlocks())); //[B5] here i made my new Tetromino the 
    //first one to show up in the game just to make sure it was there
    JFrame frame = new JFrame("Tetris"); 
    frame.getContentPane().add( game ); 
    frame.addKeyListener( game ); 
    frame.setVisible(true); 
    frame.setSize(Tetris.COLUMNS * Block.SIZE + 20, Tetris.ROWS * Block.SIZE + 40); 
    frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    game.start(); 
  }
  void touchdown() {
    this.blocks = this.blocks.union(this.t.blocks);
    this.blocks.eliminateFullRows(); 
    this.t = Tetromino.pickRandom();
  }
  void jumpDown() {
    if (! this.landed()) { 
      this.t.move(0, 1); 
      this.jumpDown(); 
    }      
  }
  boolean landedOnBlocks() { // on the strengths of the functional model 
    this.t.move(0, 1); 
    if (this.t.overlapsBlocks(this.blocks)) {
      this.t.move(0, -1); 
      return true; 
    } else {
      this.t.move(0, -1); 
      return false; 
    }
  }
  boolean landedOnFloor() {
    return this.t.blocks.maxY() == Tetris.ROWS - 1; 
  }
  boolean landed() {
    return this.landedOnFloor() || this.landedOnBlocks();  
  }
}