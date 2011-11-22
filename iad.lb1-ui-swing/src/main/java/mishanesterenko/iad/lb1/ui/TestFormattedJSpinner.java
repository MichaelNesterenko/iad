/*
 * [TestFormattedJSpinner.java]
 *
 * Summary: demonstrate the use of javax.swing.JSpinner with a formatted dollar field.
 *
 * Copyright: (c) 2009-2011 Roedy Green, Canadian Mind Products, http://mindprod.com
 *
 * Licence: This software may be copied and used freely for any purpose but military.
 *          http://mindprod.com/contact/nonmil.html
 *
 * Requires: JDK 1.7+
 *
 * Created with: JetBrains IntelliJ IDEA IDE http://www.jetbrains.com/idea/
 *
 * Version History:
 *  1.0 2009-01-01 initial version
 */
package mishanesterenko.iad.lb1.ui;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import static java.lang.System.out;

/**
 * demonstrate the use of javax.swing.JSpinner with a formatted dollar field.
 *
 * @author Roedy Green, Canadian Mind Products
 * @version 1.0 2009-01-01 initial version
 * @since 2009-01-01
 */
public final class TestFormattedJSpinner
    {
    // --------------------------- main() method ---------------------------

    /**
     * Debugging harness for a Frame
     *
     * @param args command line arguments are ignored.
     */
    public static void main( String args[] )
        {
        SwingUtilities.invokeLater( new Runnable()
        {
        /**
         * fire up a JFrame on the Swing thread
         */
        public void run()
            {
            final JFrame jFrame = new JFrame();
            final Container contentPane = jFrame.getContentPane();
            final JSpinner saleSpinner = new JSpinner();
            saleSpinner.setBackground( Color.BLACK );
            saleSpinner.setForeground( Color.YELLOW );
            saleSpinner.setFont( new Font( "Dialog", Font.BOLD, 15 ) );
            saleSpinner.setEnabled( true );
            // how much space you want to freeze the spinner at.
            Dimension d = new Dimension( 40, 25 );
            saleSpinner.setMinimumSize( d );
            saleSpinner.setPreferredSize( d );
            saleSpinner.setMaximumSize( d );
            final SpinnerNumberModel saleSpinnerModel =
                    new SpinnerNumberModel( /* initial value */ 100.00d,
                            /* min */ 0.00d,
                            /* max */  999999.99d,
                            /* step */ 0.01d );
            saleSpinner.setModel( saleSpinnerModel );
            // wants a String, not a DecimalFormat.
            final JSpinner.NumberEditor saleNumberEditor =
                    new JSpinner.NumberEditor( saleSpinner, "$###,##0.00" );
            saleSpinner.setEditor( saleNumberEditor );
            JFormattedTextField tf = saleNumberEditor.getTextField();
            tf.setBackground( Color.BLACK );
            tf.setForeground( Color.YELLOW );
            tf.setFont( new Font( "Dialog", Font.BOLD, 15 ) );
            saleSpinner.addChangeListener( new ChangeListener()
            {
            /**
             * Invoked when the JSpinner changes, even one notch in
             * moving to another value
             *
             * @param e a ChangeEvent object
             */
            public void stateChanged( ChangeEvent e )
                {
                // do something when user changes the value.
                double setting = saleSpinnerModel.getNumber().doubleValue();
                out.println( setting );
                }
            } );
            contentPane.add( saleSpinner );
            jFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            jFrame.pack();
            jFrame.setVisible( true );
            }
        } );
        }// end main
    }

