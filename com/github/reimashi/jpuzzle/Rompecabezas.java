/**
 * @author Aitor González Fernández
 * @version 7
 */

package com.github.reimashi.jpuzzle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.filechooser.FileNameExtensionFilter;

class Rompecabezas extends JFrame implements ActionListener {
    private final JPanel panelVentana = new JPanel ();
    private final JPanel panelPpal = new JPanel ();
    private final JPanel panelMenu = new JPanel ();
    private final JPanel panelTablero = new JPanel ();

    private JMenuBar barra = new JMenuBar();
    private JMenu menuA = new JMenu(ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("MENU_GAME"));
    private JMenu menuB = new JMenu(ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("MENU_IMAGE"));
    private JMenu menuC = new JMenu(ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("MENU_HELP"));
    private JMenuItem menuAA = new JMenuItem(ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("MENU_NEW_GAME"));
    private JMenuItem menuAB = new JMenuItem(ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("MENU_EXIT"));
    private JMenuItem menuBA = new JMenuItem(ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("MENU_PERSONALIZE_IMAGE"));
    private JMenuItem menuCA = new JMenuItem(ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("MENU_ABOUT"));
    private JButton[] botones = new JButton[16];

    /* Vector que guarda el numero de la ficha que se encuentra en cada posicion */
    private int[] estadoBotones = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
    private JButton bNuevo = new JButton (ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("BUTTON_NEW_GAME"));
    private int movimientos = 0;
    private JLabel info = new JLabel (ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("LABEL_MOVEMENTS") + movimientos);
    private BufferedImage[] subimagenes = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};

    private final String[] rutas = {"com/github/reimashi/jpuzzle/images/paisaje.jpg", "com/github/reimashi/jpuzzle/images/cuna.png", "com/github/reimashi/jpuzzle/images/montanas.png"};
    private final String nombreRutas[] = {ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("IMAGE_LIST_CAR"), ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("IMAGE_LIST_COT"), ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("IMAGE_LIST_MOUNTAIN")};
    private final JRadioButtonMenuItem listaImagenes[] = new JRadioButtonMenuItem[nombreRutas.length];
    private final ButtonGroup listaImagenesGroup = new ButtonGroup();

    public Rompecabezas() {

        setTitle( ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("APP_TITLE") );
        setSize( 538,642 );
        setIco();

        panelVentana.setLayout( new BorderLayout () );
        panelPpal.setLayout( new BorderLayout () );
        panelTablero.setLayout( new GridLayout( 4, 4 ) );

        for (int i = 0; i < 16; i++ ){
            botones[i] = new JButton();
            panelTablero.add( botones[i] );
            botones[i].addActionListener( Rompecabezas.this );
        }

        for (int i = 0; i < nombreRutas.length; i++) {
            listaImagenes[i] = new JRadioButtonMenuItem( nombreRutas[i] );
            menuB.add( listaImagenes[i] );
            listaImagenesGroup.add( listaImagenes[i] );
            listaImagenes[i].addActionListener( Rompecabezas.this );
        }
        listaImagenes[0].setSelected( true );

        cambiarImagen( rutas[0] );

        menuA.add( menuAA );
        menuA.add( new JSeparator() );
        menuA.add( menuAB );
        barra.add( menuA );
        menuB.add( new JSeparator() );
        menuB.add( menuBA );
        barra.add( menuB );
        menuC.add( menuCA );
        barra.add( menuC );

        menuAA.addActionListener( Rompecabezas.this );
        menuAB.addActionListener( Rompecabezas.this );
        menuBA.addActionListener( Rompecabezas.this );
        menuCA.addActionListener( Rompecabezas.this );
        bNuevo.addActionListener( Rompecabezas.this );

        panelMenu.add( bNuevo );

        panelPpal.add( panelMenu, BorderLayout.NORTH );
        panelPpal.add( panelTablero, BorderLayout.CENTER );
        panelPpal.add( info, BorderLayout.SOUTH );

        panelVentana.add( barra, BorderLayout.NORTH );
        panelVentana.add( panelPpal, BorderLayout.CENTER );

        panelPpal.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );

        add( panelVentana );
    }

    @Override
    public void actionPerformed( ActionEvent evento ) {
        int i;

        for (i = 0; i < 16; i++){
            if (evento.getSource() == botones[i]) {
                if (moverFicha( i )) {
                    if (comprobarColocado()) {
                        info.setText( ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("MSG_WIN") );
                        deshabilitarBotones();
                    }
                    else {
                        info.setText( ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("LABEL_MOVEMENTS") + movimientos );
                    }
                }
                else {
                    info.setText( ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("LABEL_INCORRECT_MOVEMENT") );
                }
            }
        }

        if (evento.getSource() == bNuevo || evento.getSource() == menuAA) {
            nuevoJuego();
        }

        if (evento.getSource() == menuAB) {
            Runtime.getRuntime().exit(0);
        }

        if (evento.getSource() == menuBA) {
            cambiarImagen();
        }

        if (evento.getSource() == menuCA) {
            JOptionPane.showMessageDialog( Rompecabezas.this,
                  ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("DIAG_ABOUT_TEXT"),
                  ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("DIAG_ABOUT"), JOptionPane.PLAIN_MESSAGE );
        }

        for (i = 0; i < listaImagenes.length; i++){
            if (evento.getSource() == listaImagenes[i]) {
                cambiarImagen( rutas[i] );
            }
        }
    }

    /*
     * Metodo que muestra una ventana para escoger una imagen
     */
    private void cambiarImagen() {
        // Se crea un objeto JFileChooser, que crea la ventana
        JFileChooser fileChooser = new JFileChooser();
        // Se crea un objeto FileNameExtensionFilter y se le asigna al JFileChooser para que filtre los archivos que nos interesan
        fileChooser.setFileFilter( new FileNameExtensionFilter( ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("FILTER_IMAGES"), "jpg", "png", "bmp", "gif" ) );

        // Se muestra la ventana
        int retVal = fileChooser.showOpenDialog( this );

        // Si al cerrarse devuelve que SI se ha escojido un archivo
        if (retVal == JFileChooser.APPROVE_OPTION) {
            try {
                getImagenes ( fileChooser.getSelectedFile().getAbsolutePath(), true );
                nuevoJuego ();
                for ( int i = 0; i < listaImagenes.length; i++ ){
                    listaImagenes[i].setSelected(false);
                    listaImagenes[i].revalidate();
                }
            }
            // Si no encuentra la imagen se muestra un dialogo de error
            catch (Exception ioe) {
                JOptionPane.showMessageDialog( Rompecabezas.this,
                  ioe.getMessage(),
                  ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("MSG_ERROR"), JOptionPane.PLAIN_MESSAGE );
            }
        }
    }

    private void cambiarImagen( String ruta ) {
        getImagenes( ruta, false );
        nuevoJuego ();
    }

    private void revolverBotones() {
        Random rnd = new Random();
        int i, j;

        for (i = 0; i < 15; i++){
            estadoBotones[i] = rnd.nextInt( 15 );
            for (j = 0; j < i; j++) {
                if (estadoBotones[i] == estadoBotones[j]) {
                    i--;
                    break;
                }
            }
        }
        estadoBotones[15] = 15;
    }

    private void nuevoJuego() {
        int i = 0;
        revolverBotones();

        movimientos = 0;
        info.setText( ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("LABEL_MOVEMENTS") + movimientos );

        if (subimagenes[i] == null) {
            deshabilitarBotones();
            info.setText( ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("LABEL_NO_IMAGE") );
            return;
        }

        for (i = 0; i < 16; i++){
            if (estadoBotones[i] != 15) {
                botones[i].setIcon( new ImageIcon (subimagenes[estadoBotones[i]]) );
                botones[i].setEnabled( true );
            }
            else {
                botones[i].setIcon( null );
                botones[i].setBackground( Color.black );
                botones[i].setEnabled( false );
            }
        }
    }


    private void deshabilitarBotones() {
        int i;

        for (i = 0; i < 16; i++){
            botones[i].setEnabled( false );
        }
    }

    /*
     * Metodo que comprueba si el vector estadoBotones esta ordenado, es decir,
     * comprueba si todas las imagenes están ordenadas
     */
    private boolean comprobarColocado() {
        int i;

        for ( i = 0; i < 16; i++ ){
            if (estadoBotones[i] != i) {
                return false;
            }
        }

        return true;
    }

    private void repintarBoton( int i ) {
        if (estadoBotones[i] != 15) {
            botones[i].setIcon( new ImageIcon (subimagenes[estadoBotones[i]]) );
            botones[i].setEnabled( true );
        }
        else {
            botones[i].setBackground( Color.black );
            botones[i].setIcon( null );
            botones[i].setEnabled( false );
        }
    }

    private boolean moverFicha( int pos ) {
        int i = 0;

        if (pos + 1 < 16){
            if (estadoBotones[pos + 1] == 15 && ((pos + 1) % 4) != 0) {
                estadoBotones[pos + 1] = estadoBotones[pos];
                estadoBotones[pos] = 15;

                repintarBoton( pos );
                repintarBoton( pos + 1 );

                movimientos++;
                return true;
            }

            if (pos + 4 < 16){
                if (estadoBotones[pos + 4] == 15) {
                    estadoBotones[pos + 4] = estadoBotones[pos];
                    estadoBotones[pos] = 15;

                    repintarBoton( pos );
                    repintarBoton( pos + 4 );

                    movimientos++;
                    return true;
                }
            }
        }


        if (pos - 1 >= 0){
            if (estadoBotones[pos - 1] == 15 && (pos - 1) / 4 == pos / 4) {
                estadoBotones[pos - 1] = estadoBotones[pos];
                estadoBotones[pos] = 15;

                repintarBoton( pos );
                repintarBoton( pos - 1 );

                movimientos++;
                return true;
            }

            if (pos - 4 >= 0){
                if (estadoBotones[pos - 4] == 15) {
                    estadoBotones[pos - 4] = estadoBotones[pos];
                    estadoBotones[pos] = 15;

                    repintarBoton( pos );
                    repintarBoton( pos - 4 );

                    movimientos++;
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Metodo que redimension la imagen
     */
    private BufferedImage redimensionarImagen( BufferedImage i, int width, int height ) {
        BufferedImage ret;
        int w = i.getWidth();
        int h = i.getHeight();

        // Se crea un nuevo BufferedImage del tamaño final
        ret = new BufferedImage( width, height, BufferedImage.TYPE_3BYTE_BGR );

        // Se le asigna una zona de dibujado
        Graphics2D g = ret.createGraphics();

        // Se le asigna un renderizador (Si no al redimensionar la imagen se verá mal)
        g.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );

        // Se dibuja la imagen original en la zona de dibujo con la resolucion final
        g.drawImage( i, 0, 0, width, height, null );

        return ret;
    }


    private BufferedImage getImagen( String file ){
        BufferedImage imagen = null;

        try {
            imagen = ImageIO.read( new File( file ) );
            imagen = redimensionarImagen( imagen, 520, 520 );
        } catch( java.io.IOException e ){
            JOptionPane.showMessageDialog( Rompecabezas.this,
                  ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("MSG_ERROR_TEXT") + "\n" + e.getMessage(),
                  ResourceBundle.getBundle("com.github.reimashi.jpuzzle/Inter").getString("MSG_ERROR"), JOptionPane.PLAIN_MESSAGE );
        }

        return imagen;
    }

    private BufferedImage getImagenFromPacket ( String file ){
        BufferedImage imagen = null;

        try {
            imagen = ImageIO.read( getClass().getClassLoader().getResource( file ) );
        } catch( java.io.IOException e ){
            JOptionPane.showMessageDialog( this,
                  e.getMessage(),
                  "Error!!!", JOptionPane.PLAIN_MESSAGE );
        }

        return imagen;
    }

    private void setIco() {
        Image imagen;

        try {
            imagen = ImageIO.read( getClass().getClassLoader().getResource( "com/github/reimashi/jpuzzle/resources/icon.png" ) );
            setIconImage(imagen);
        } catch( java.io.IOException e ){
        }
    }

    /*
     * Metodo que obtiene 15 partes de una imagen mas grande
     */
    private void getImagenes( String file , boolean source){
        BufferedImage original;

        if (source) {
            // Se carga la imagen original
            original = getImagen( file );
        }
        else {
            original = getImagenFromPacket( file );
        }

        if (original == null) {
            return;
        }

        // Se crean 15 segmentos con el metodo getSubimage( x, y, ancho, alto)
        subimagenes[0] = original.getSubimage( 0, 0, original.getWidth() / 4, original.getHeight() / 4 );
        subimagenes[1] = original.getSubimage( (original.getWidth() / 4) * 1, 0, original.getWidth() / 4, original.getHeight() / 4 );
        subimagenes[2] = original.getSubimage( (original.getWidth() / 4) * 2, 0, original.getWidth() / 4, original.getHeight() / 4 );
        subimagenes[3] = original.getSubimage( (original.getWidth() / 4) * 3, 0, original.getWidth() / 4, original.getHeight() / 4 );
        subimagenes[4] = original.getSubimage( 0, (original.getHeight() / 4) * 1, original.getWidth() / 4, original.getHeight() / 4 );
        subimagenes[5] = original.getSubimage( (original.getWidth() / 4) * 1, (original.getHeight() / 4) * 1, original.getWidth() / 4, original.getHeight() / 4 );
        subimagenes[6] = original.getSubimage( (original.getWidth() / 4) * 2, (original.getHeight() / 4) * 1, original.getWidth() / 4, original.getHeight() / 4 );
        subimagenes[7] = original.getSubimage( (original.getWidth() / 4) * 3, (original.getHeight() / 4) * 1, original.getWidth() / 4, original.getHeight() / 4 );
        subimagenes[8] = original.getSubimage( 0, (original.getHeight() / 4) * 2, original.getWidth() / 4, original.getHeight() / 4 );
        subimagenes[9] = original.getSubimage( (original.getWidth() / 4) * 1, (original.getHeight() / 4) * 2, original.getWidth() / 4, original.getHeight() / 4 );
        subimagenes[10] = original.getSubimage( (original.getWidth() / 4) * 2, (original.getHeight() / 4) * 2, original.getWidth() / 4, original.getHeight() / 4 );
        subimagenes[11] = original.getSubimage( (original.getWidth() / 4) * 3, (original.getHeight() / 4) * 2, original.getWidth() / 4, original.getHeight() / 4 );
        subimagenes[12] = original.getSubimage( 0, (original.getHeight() / 4) * 3, original.getWidth() / 4, original.getHeight() / 4 );
        subimagenes[13] = original.getSubimage( (original.getWidth() / 4) * 1, (original.getHeight() / 4) * 3, original.getWidth() / 4, original.getHeight() / 4 );
        subimagenes[14] = original.getSubimage( (original.getWidth() / 4) * 2, (original.getHeight() / 4) * 3, original.getWidth() / 4, original.getHeight() / 4 );
    }
}