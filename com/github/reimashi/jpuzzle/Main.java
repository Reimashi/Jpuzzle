/**
 * @author Aitor González Fernández
 * @version 7
 */

package com.github.reimashi.jpuzzle;

import javax.swing.JFrame;

public class Main {
    public static void main( String[] args ) {
        Rompecabezas v = new Rompecabezas();
        v.setLocationRelativeTo( null );
        v.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        v.setResizable( false );
        v.setVisible( true );
    }
}