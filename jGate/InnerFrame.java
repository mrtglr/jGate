package jGate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class InnerFrame extends JFrame {
    
    public String _title = null;
    public String[] _comboDataSource = null;
    public String _buttonText = null;
    public int _performedAction = 0; // 0:delete, 1:lock, 2:unlock

    private static String selected;

	InnerFrame (String title, String[] comboDataSource, String buttonText, int performedAction) {

        this._title = title;
        this._comboDataSource = comboDataSource;
        this._buttonText = buttonText;
        this._performedAction = performedAction;
		
		final JLabel panel = new JLabel();
		this.setTitle(_title);
		this.setResizable(true);
		this.getContentPane().setBackground(Colors.InnerFrames());
		this.pack();
		this.setSize(450, 150);		
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		JComboBox<String> pathsCombo = new JComboBox<String>(_comboDataSource);
		pathsCombo.setBounds(20, 20, 400, 30);
		panel.add(pathsCombo);
		
		JButton button = new JButton("<html><font color=black size=4><b>" + _buttonText + "</b></html>");
		button.setBounds(180, 70, 90, 30);
		button.setBackground(Colors.Buttons());
		panel.setLayout(null);
		panel.add(button);
		
		this.setVisible(true);
		this.add(panel);
		
		pathsCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {			
				selected = (String) pathsCombo.getSelectedItem();				
			}
		});
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {	

                if(_performedAction == 0){
                    Helper.deletefromFile(selected, Driver.paths);
                }
                if(_performedAction == 1){
                    Helper.Lock(selected, Driver.paths);
                }
                if(_performedAction == 2){
                    Helper.Unlock(selected, Driver.paths);
                }
				dispose();
			}	
		});
    }
}
