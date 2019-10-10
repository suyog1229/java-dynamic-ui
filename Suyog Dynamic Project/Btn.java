import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

// dynamic button creation with listener
// note:static vars not serialized
// object contructor not recovered after deserialized
// must implement serialization interface of all class.
// implement Listeners instread of anonymouse class because constructor never called.

class CreateBtn implements java.io.Serializable, ActionListener, MouseMotionListener
{
	JButton btn;
	JFrame frame;
	int x=100,w=100,h=50;
	int y=0,btnno;
	public CreateBtn(JFrame frame,final int btnno)
	{
		this.frame=frame; this.btnno=btnno;
		btn=new JButton("button"+btnno);
		y=y+50;
		btn.setBounds(x,y,w,h);
		btn.addActionListener(this);
		btn.addMouseMotionListener(this);
		
		frame.add(btn);
		
		System.out.println("track: "+btnno);
		frame.repaint();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==btn)
		{
			System.out.println("Btn "+btnno+" clicked");
			System.out.println("btn X: "+btn.getX()+" Y: "+btn.getY());
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{
		if(e.getSource()==btn)
		{
			x=e.getX()+x; y=e.getY()+y;
			btn.setBounds(x, y, w, h);
		}
	}
	public void mouseMoved(MouseEvent e)
	{}
}

class Creator extends JFrame implements java.io.Serializable
{
	
	JMenuBar menubar;
	JMenu options;
	JMenuItem add1,save;
	JFrame frame;
	CreateBtn buttons;
	ArrayList <CreateBtn> btnList=new ArrayList<CreateBtn>();
	int btnno=0;

	void loadStructure()
	{
		try
		{
			FileInputStream fis=new FileInputStream("proj.ser");
			ObjectInputStream ois=new ObjectInputStream(fis);
			btnList=(ArrayList<CreateBtn>) ois.readObject();
			for(int i=0;i<btnList.size();i++)
			{
				add(btnList.get(i).btn);
			}
			fis.close(); ois.close();
			System.out.println("Structure loaded");
		} catch (Exception e)
		{
			System.out.println("Load err: "+e.getMessage());
		}
		
		
	}
	
	Creator()
	{
		
		frame=this;
		setLayout(null);
		menubar=new JMenuBar();
		options=new JMenu("Options");
		add1=new JMenuItem("ADD BTN");
		save=new JMenuItem("Save");
		
		options.add(add1);
		options.add(save);
		menubar.add(options);
		setJMenuBar(menubar);
		
		loadStructure();
		add1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				buttons=new CreateBtn(frame,btnno++);
				btnList.add(buttons);
			}
		});
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					//File file=new File("C:\\Dynamic");
					//file.mkdir();
					FileOutputStream fos=new FileOutputStream("proj.ser");
					ObjectOutputStream oos=new ObjectOutputStream(fos);
					oos.writeObject(btnList);
					fos.close(); oos.close();
					System.out.println("Structure saved.");
					
				} catch (Exception e1)
				{
					System.out.println("save Err: ");
					e1.printStackTrace();
				}
				
			}
		});
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500,500);
		setVisible(true);
	}
}


public class Btn
{
	public static void main(String[] args)
	{
		Creator c=new Creator();
	}
}
