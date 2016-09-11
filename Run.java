import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

public class Run
{
	public static void main(String args[])
	{
		Calculator calc=new Calculator();
	}
}

class Calculator implements ActionListener, KeyListener
{
	private JFrame frame;
	private JPanel panel[],lpanel[];
	private JTextField txt;
	private JLabel lbl_bracket,lbl_mem;
	private JRadioButton rbtn[],gbtn1_sel,gbtn2_sel;
	private JCheckBox cbox[];
	private JButton btn[];
	private ButtonGroup gbtn1,gbtn2,gbtn3,mv_gbtn;
	private JMenuBar mbar;
	private JMenu menu_edit,menu_view,menu_help;
	private JMenuItem mf_copy,mf_paste,mf_exit,mh_about;
	private JRadioButtonMenuItem mv_sta,mv_sci;
	private boolean inp_status,decimal,fe,mode;
	private String ebox;
	private int limit;
	private double num1,mem,base,exp;
	private char op1,pow,mod,or,and,xor,angle,gbtn3_sel;

	private JDialog listDlg;
	private JList list;
	private DefaultListModel listModel;
	private JLabel lblList;
	private JScrollPane listScrollPane;

	Calculator()
	{
		frame=null;
		listDlg=null;
		createControls();
		mode=true;
		standard();
		addActionListener();
	}

	public void actionPerformed(ActionEvent ae)
	{
		JButton button=(JButton)ae.getSource();
		checkButton(button.getText(),button);
	}

	public void keyPressed(KeyEvent e)
	{		}//checkButton(e.getKeyText(e.getKeyCode()),new JButton());}
	public void keyReleased(KeyEvent e)
	{		checkButton(""+(char)e.getKeyCode(),new JButton());}
	public void keyTyped(KeyEvent e)
	{
		//checkButton(e.getKeyText(e.getKeyCode()),new JButton());
	}

	private void init()
	{
		inp_status=true;
		fe=decimal=false;
		exp=base=mem=num1=op1=pow=mod=or=and=xor=angle=0;
		gbtn1_sel=rbtn[1];
		dec();
		gbtn2_sel=rbtn[4];
	}

	private void noFunction()
	{
		btn[5].setEnabled(false);
		btn[6].setEnabled(false);
		btn[16].setEnabled(false);

		mf_paste.setEnabled(false);
	}

	private void addActionListener()
	{
		mf_copy.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){ txt.copy(); } });

//		mf_paste.addActionListener(new ActionListener(){
//		public void actionPerformed(ActionEvent e){} });

		mf_exit.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){ System.exit(0); } });

		mh_about.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e)
		{
			JDialog about=new JDialog(frame,"About Calculator",true);
			JTextArea lbl_about=new JTextArea("\n  This program is designed to\n  imitate the functionality of Windows\n  Calculator.\n\n  Designed by: Ahmed Mobasher Khan\n\n  ID: MCSS07M002\n\n  Email: amubasherk@yahoo.com");
			lbl_about.setEditable(false);
			lbl_about.setBackground(new Color(240,240,240));
			about.setResizable(false);
			about.add(lbl_about);
			about.setSize(218,218);
			about.setVisible(true);
		} });

		mv_sta.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){ standard(); } });

		mv_sci.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){ scientific(); } });

		for(int i=0;i<rbtn.length;i++)
			rbtn[i].addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e){ checkRButton(e); } });

		for(int i=0;i<btn.length;i++)
		{
			btn[i].addActionListener(this);
			btn[i].addKeyListener(this);
		}
/*new ActionListener() {
		public void actionPerformed(ActionEvent e){ checkButton(e); } }*/
	}

	private String reverse(String str)
	{
		String tmp="";
		for(int i=str.length()-1;i>-1;i--)
			tmp+=str.charAt(i);
		return tmp;
	}

	private String scan(String str)
	{
		String val="";
		char ch;
		int i=0;
		if(str.charAt(0)=='-')
			i++;

		for(;i<str.length();i++)
		{
			ch=str.charAt(i);
			if(ch>='0'&&ch<='9'||ch>='A'&&ch<='F'&&gbtn1_sel==rbtn[0])
				val+=ch;
			else if(ch=='e'||ch=='E')
				break;
		}
		return val;
	}

	private double nBaseToDec(String str)
	{
		double value=0,base=1;
		char ch;
		if(gbtn1_sel==rbtn[0])
		{
			str=scan(str);
			for(int i=str.length()-1;i>-1;i--,base*=16)
			{
				ch=str.charAt(i);
				if(ch>='0'&&ch<='9')
					ch-=48;
				else if(ch>='A'&&ch<='F')
					ch-=55;
				value+=ch*base;
			}
		}
		else if(gbtn1_sel==rbtn[1])
			value=Double.parseDouble(str);
		else if(gbtn1_sel==rbtn[2])
		{
			str=scan(str);
			for(int i=str.length()-1;i>-1;i--,base*=8)
				value+=(str.charAt(i)-48)*base;
		}
		else if(gbtn1_sel==rbtn[3])
		{
			str=scan(str);
			for(int i=str.length()-1;i>-1;i--,base*=2)
				value+=(str.charAt(i)-48)*base;
		}
		return value;
	}

	private String decToNbase(double value)
	{
		char rem=1;
		String str="0";

		if(gbtn1_sel==rbtn[0])
		{
			value=Math.abs(value);
			while(value!=0)
			{
				rem=(char)(value%16);
				if(rem>9)
					rem+=55;
				else
					rem+=48;
				str+=rem;
				value=(long)(value/16);
			}
			str=reverse(str);
			if(str.length()>1)
				str=str.substring(0,str.length()-1);
		}
		else if(gbtn1_sel==rbtn[1])
		{
			str=Double.toString(value);
			if(str.charAt(str.length()-2)=='.'&&str.charAt(str.length()-1)=='0')
				str=str.substring(0,str.length()-1);
		}
		else if(gbtn1_sel==rbtn[2])
		{
			value=Math.abs(value);
			while(value!=0)
			{
				rem=(char)(value%8);
				rem+=48;
				str+=rem;
				value=(long)value/8;
			}
			str=reverse(str);
			if(str.length()>1)
				str=str.substring(0,str.length()-1);
		}
		else if(gbtn1_sel==rbtn[3])
		{
			value=Math.abs(value);
			while(value!=0)
			{
				rem=(char)(value%2);
				rem+=48;
				str+=rem;
				value=(long)value/2;
			}
			str=reverse(str);
			if(str.length()>1)
				str=str.substring(0,str.length()-1);
		}

		return str;
	}

	private void enableFunctions(boolean b)
	{
		btn[4].setEnabled(b);
		btn[15].setEnabled(b);
		btn[16].setEnabled(b);
		btn[26].setEnabled(b);
		btn[16].setEnabled(b);
		btn[37].setEnabled(b);
		btn[42].setEnabled(b);
		btn[43].setEnabled(b);
		btn[48].setEnabled(b);
		btn[51].setEnabled(b);
		noFunction();
	}

	private void hex(boolean b)
	{
		enableFunctions(false);
		for(int i=52;i<58;i++)
			btn[i].setEnabled(b);
	}

	private void dec()
	{
		limit=21;
		hex(false);
		oct(true);
		binary(true);
		enableFunctions(true);
	}

	private void oct(boolean b)
	{
		enableFunctions(false);
		btn[9].setEnabled(b);
		btn[10].setEnabled(b);
	}

	private void binary(boolean b)
	{
		oct(b);
		btn[8].setEnabled(b);
		btn[31].setEnabled(b);
		btn[32].setEnabled(b);
		btn[19].setEnabled(b);
		btn[20].setEnabled(b);
		btn[21].setEnabled(b);
	}

	private void updateLimit()
	{
		if(gbtn1_sel==rbtn[0])
		{
			limit=2*gbtn3_sel;
		}
		else if(gbtn1_sel==rbtn[2])
		{
			limit=8*gbtn3_sel;
			limit/=3;
			limit++;
		}
		else if(gbtn1_sel==rbtn[3])
		{
			limit=8*gbtn3_sel;
		}
	}

	private void formatValue()
	{
		if(gbtn1_sel!=rbtn[1])
		{
			if(ebox.length()>limit)
			{
				ebox=ebox.substring(ebox.length()-limit,ebox.length());
				ebox=decToNbase(nBaseToDec(ebox));
			}
		}
	}

	private double acosh(double val)
	{
		if(val<=1)
		return 0;
		return Math.log(val+Math.pow(val*val-1,.5));
	}

	private double asinh(double val)
	{
		double tmp=val;
		if(val<0)
			val*=-1;
		val=Math.log(val+Math.pow(val*val+1,.5));
		if(tmp<0)
			val*=-1;
		return val;
	}

	private double atanh(double val)
	{
		if(val<=-1||val>=1)
			return 0;
		else
			return Math.log((1+val)/(1-val))/2;
	}

	private void checkRButton(ActionEvent ae)
	{
		JRadioButton rbutton=(JRadioButton)ae.getSource();
		double value=nBaseToDec(txt.getText());
		if(rbutton==rbtn[0])
		{
			dec();
			hex(true);
			NondecRButton();
			gbtn1_sel=rbtn[0];
			updateLimit();
		}
		else if(rbutton==rbtn[1])
		{
			dec();
			decRButton();
			gbtn1_sel=rbtn[1];
		}
		else if(rbutton==rbtn[2])
		{
			dec();
			oct(false);
			NondecRButton();
			gbtn1_sel=rbtn[2];
			updateLimit();
		}
		else if(rbutton==rbtn[3])
		{
			dec();
			binary(false);
			NondecRButton();
			gbtn1_sel=rbtn[3];
			updateLimit();
		}

		else if(rbutton==rbtn[4])
		{
			gbtn2_sel=rbtn[4];
			angle=0;
		}

		else if(rbutton==rbtn[5])
		{
			gbtn2_sel=rbtn[5];
			angle=1;
		}

		else if(rbutton==rbtn[6])
		{
			gbtn2_sel=rbtn[6];
			angle=2;
		}

		else if(rbutton==rbtn[7])
		{
			gbtn3_sel=8;
			updateLimit();
		}

		else if(rbutton==rbtn[8])
		{
			gbtn3_sel=4;
			updateLimit();
		}

		else if(rbutton==rbtn[9])
		{
			gbtn3_sel=2;
			updateLimit();
		}

		else if(rbutton==rbtn[10])
		{
			gbtn3_sel=1;
			updateLimit();
		}

		ebox=decToNbase(value);
		formatValue();
		txt.setText(ebox);
		inp_status=true;
	}

	private double arithmetic(char op,double n1,double n2)
	{
		switch(op)
		{
			case '-':
				n1-=n2;
			break;
			case '+':
				n1+=n2;
			break;
			case '/':
				if(n2!=0)
					n1/=n2;
			break;
			case '*':
				n1*=n2;
			break;
		}
		return n1;
	}

	private void operations(char op)
	{
		if(op1==0)
		{
			op1=op;
			num1=nBaseToDec(txt.getText());
		}

		else
		{
			num1=arithmetic(op1,num1,nBaseToDec(txt.getText()));
			ebox=decToNbase(num1);
			formatValue();
			txt.setText(ebox);
			op1=0;
			if(op!='=')
				op1=op;
		}
		decimal=false;
		inp_status=true;
	}

	private double getAngle(double ang)
	{
		if(gbtn2_sel==rbtn[4])
			return Math.toRadians(ang);
		else if(gbtn2_sel==rbtn[5])
			return ang;
		else
			return (2*Math.PI/400)*ang;
	}

	private double setAngle(double ang)
	{
		if(gbtn2_sel==rbtn[4])
			return Math.toDegrees(ang);
		else if(gbtn2_sel==rbtn[5])
			return ang;
		else
			return ang/(2*Math.PI/400);
	}

	private void checkButton(String label,JButton button)
	{
		char ch=label.charAt(0);

		if(ch>='0'&&ch<='9'&&label.length()==1||ch>='A'&&ch<='F'&&label.length()==1&&button!=btn[2]&&mode==true)
		{
			ebox=txt.getText();
			if(inp_status==true)
			{
				ebox="";
				ebox+=ch;
				if(gbtn1_sel==rbtn[1])
					ebox+='.';
				if(ch!='0')
					inp_status=false;
			}
			else if(inp_status==false&&ebox.length()<limit)
			{
				if(decimal==false&&gbtn1_sel==rbtn[1])
					ebox=ebox.substring(0,ebox.length()-1);
				ebox+=ch;
				if(decimal==false&&gbtn1_sel==rbtn[1])
					ebox+='.';
			}
			txt.setText(ebox);
		}

		else if(label.equals("C"))
		{
			ebox="0";
			num1=pow=op1=mod=or=and=xor=0;
			if(gbtn1_sel==rbtn[1])
				ebox+='.';
			txt.setText(ebox);
			inp_status=true;
			fe=decimal=false;
			cbox[0].setSelected(false);
			cbox[1].setSelected(false);
		}

		else if(label.equals("CE"))
		{
			ebox="0";
			if(gbtn1_sel==rbtn[1])
				ebox+='.';
			inp_status=true;
			fe=decimal=false;
			cbox[0].setSelected(false);
			cbox[1].setSelected(false);
			txt.setText(ebox);
		}

		else if(label.equals("Backspace"))
		{
			ebox=txt.getText();
			int elen=ebox.length();
			ch=ebox.charAt(elen-1);

			if(gbtn1_sel==rbtn[1])
			{
				if(ebox.charAt(0)=='-'&&elen==3&&decimal==false||elen==2&&decimal==false)
				{
					ebox="0.";
					inp_status=true;
				}

				else if(ch=='.')
				{
					if(decimal==true)
					{
						decimal=false;
						if(ebox.equals("0.")||ebox.equals("-0."))
						{
							ebox="0.";
							inp_status=true;
						}
					}
					else
					{
						ebox=ebox.substring(0,elen-2);
						ebox+='.';
					}
				}

				else
					ebox=ebox.substring(0,elen-1);
			}

			else
			{
				if(ebox.length()==1)
					ebox="0";
				else
					ebox=ebox.substring(0,elen-1);
			}

			txt.setText(ebox);
		}

		else if(label.equals("."))
		{
			if(decimal==false)
				decimal=true;
			inp_status=false;
		}

		else if(label.equals("+/-"))
		{
			if(inp_status==false)
			{
				ebox=txt.getText();
				if(ebox.charAt(0)!='-')
					ebox="-"+ebox;
				else
					ebox=ebox.substring(1,ebox.length());
				txt.setText(ebox);
			}
		}

		else if(label.equals("+"))
		{
			operations('+');
		}

		else if(label.equals("-"))
		{
			operations('-');
		}

		else if(label.equals("/"))
		{
			operations('/');
		}

		else if(label.equals("*"))
		{
			operations('*');
		}

		else if(label.equals("="))
		{
			if(op1!=0)
				operations('=');
		}

		else if(label.equals("pi"))
		{
			txt.setText(decToNbase(Math.PI));
			inp_status=true;
		}

		else if(label.equals("1/x"))
		{
			double x=nBaseToDec(txt.getText());
			if(x!=0)
			{
				txt.setText(decToNbase(1/x));
			}
			inp_status=true;
		}
		else if(label.equals("n!"))
		{
			double fact=1,n=nBaseToDec(txt.getText());
			for(;n>1;n--)
				fact*=n;
			txt.setText(decToNbase(fact));
			inp_status=true;
		}

		else if(label.equals("M+"))
		{
			mem+=nBaseToDec(txt.getText());
			lbl_mem.setText("M");
			inp_status=true;
		}

		else if(label.equals("MS"))
		{
			mem=nBaseToDec(txt.getText());
			lbl_mem.setText("M");
			inp_status=true;
		}

		else if(label.equals("MR"))
		{
			txt.setText(decToNbase(mem));
			inp_status=true;
		}

		else if(label.equals("MC"))
		{
			mem=0;
			lbl_mem.setText("");
			inp_status=true;
		}

		else if(label.equals("log"))
		{
			double lg=nBaseToDec(txt.getText());
			if(cbox[0].isSelected()!=true)
			{
				if(lg>0)
					txt.setText(decToNbase(Math.log10(lg)));
			}
			else
			{
				txt.setText(decToNbase(Math.exp(lg*Math.log(10.0))));
			}
			cbox[0].setSelected(false);
			inp_status=true;
		}

		else if(label.equals("ln"))
		{
			double ln=nBaseToDec(txt.getText());
			if(cbox[0].isSelected()!=true)
			{
				if(ln>0)
					txt.setText(decToNbase(Math.log(ln)));
			}
			else
				txt.setText(decToNbase(Math.exp(ln)));
			cbox[0].setSelected(false);
			inp_status=true;
		}

		else if(label.equals("sin"))
		{
			double sin=nBaseToDec(txt.getText());
			if(cbox[0].isSelected()!=true)
			{
				if(cbox[1].isSelected()!=true)
					txt.setText(decToNbase(Math.sin(getAngle(sin))));
				else
					txt.setText(decToNbase(Math.sinh(sin)));
			}
			else
			{
				if(cbox[1].isSelected()!=true)
					txt.setText(decToNbase(setAngle(Math.asin(sin))));

				else
					txt.setText(decToNbase(asinh(sin)));
			}
			inp_status=true;
			cbox[0].setSelected(false);
			cbox[1].setSelected(false);
		}

		else if(label.equals("cos"))
		{
			double cos=nBaseToDec(txt.getText());
			if(cbox[0].isSelected()!=true)
			{
				if(cbox[1].isSelected()!=true)
					txt.setText(decToNbase(Math.cos(getAngle(cos))));
				else
					txt.setText(decToNbase(Math.cosh(cos)));
			}
			else
			{
				if(cbox[1].isSelected()!=true)
					txt.setText(decToNbase(setAngle(Math.acos(cos))));

				else
					txt.setText(decToNbase(acosh(cos)));
			}
			inp_status=true;
			cbox[0].setSelected(false);
			cbox[1].setSelected(false);
		}

		else if(label.equals("tan"))
		{
			double tan=nBaseToDec(txt.getText());
			if(cbox[0].isSelected()!=true)
			{
				if(cbox[1].isSelected()!=true)
					txt.setText(decToNbase(Math.tan(getAngle(tan))));
				else
					txt.setText(decToNbase(Math.tanh(tan)));
			}
			else
			{
				if(cbox[1].isSelected()!=true)
					txt.setText(decToNbase(setAngle(Math.atan(tan))));

				else
					txt.setText(decToNbase(atanh(tan)));
			}
			inp_status=true;
			cbox[0].setSelected(false);
			cbox[1].setSelected(false);
		}

		else if(label.equals("x^2"))
		{
			double x2=nBaseToDec(txt.getText());
			if(cbox[0].isSelected()==false)
				txt.setText(decToNbase(Math.pow(x2,2)));
			else
				txt.setText(decToNbase(Math.pow(x2,1/2.0)));
			cbox[0].setSelected(false);
			inp_status=true;
		}

		else if(label.equals("x^3"))
		{
			double x3=nBaseToDec(txt.getText());
			if(cbox[0].isSelected()==false)
				txt.setText(decToNbase(Math.pow(x3,3)));
			else
				txt.setText(decToNbase(Math.pow(x3,1/3.0)));
			cbox[0].setSelected(false);
			inp_status=true;
		}

		else if(label.equals("x^y"))
		{
			if(pow==0)
				base=nBaseToDec(txt.getText());
			else
			{
				exp=nBaseToDec(txt.getText());
				if(cbox[0].isSelected()==false)
					txt.setText(decToNbase(Math.pow(base,exp)));
				else
					txt.setText(decToNbase(Math.pow(base,1/exp)));
				pow=0;
				cbox[0].setSelected(false);
			}
			inp_status=true;
			pow=1;
		}

		else if(label.equals("Int"))
		{
			if(cbox[0].isSelected()==false)
			{
				double val=(long)nBaseToDec(txt.getText());
				txt.setText(decToNbase(val));
			}
			else
			{
				double val=nBaseToDec(txt.getText());
				txt.setText(decToNbase(val-(long)val));
			}
			cbox[0].setSelected(false);
			inp_status=true;
		}

		else if(label.equals("Mod"))
		{
			if(mod==0)
				base=nBaseToDec(txt.getText());
			else
			{
				exp=nBaseToDec(txt.getText());
				txt.setText(decToNbase(base%exp));
				mod=0;
			}
			inp_status=true;
			mod=1;
		}

		else if(label.equals("Or"))
		{
			if(or==0)
				base=nBaseToDec(txt.getText());
			else
			{
				exp=nBaseToDec(txt.getText());
				txt.setText(decToNbase((long)base|(long)exp));
				or=0;
			}
			inp_status=true;
			or=1;
		}

		else if(label.equals("And"))
		{
			if(and==0)
				base=nBaseToDec(txt.getText());
			else
			{
				exp=nBaseToDec(txt.getText());
				txt.setText(decToNbase((long)base&(long)exp));
				and=0;
			}
			inp_status=true;
			and=1;
		}

		else if(label.equals("Xor"))
		{
			if(xor==0)
				base=nBaseToDec(txt.getText());
			else
			{
				exp=nBaseToDec(txt.getText());
				txt.setText(decToNbase((long)base^(long)exp));
				xor=0;
			}
			inp_status=true;
			xor=1;
		}

		else if(label.equals("Not"))
		{
			double not=nBaseToDec(txt.getText());
			txt.setText(decToNbase(~(long)not));
			inp_status=true;
		}

		else if(label.equals("lsh"))
		{
			double shft=nBaseToDec(txt.getText());
			if(cbox[0].isSelected()==false)
				txt.setText(decToNbase((long)shft<<1));
			else
				txt.setText(decToNbase((long)shft>>1));
			inp_status=true;
			cbox[0].setSelected(false);
		}

		else if(label.equals("F-E"))
		{
			double val=nBaseToDec(txt.getText());
			ebox=decToNbase(val);

			if(fe==false)
			{
				if(val==0)
					ebox="0.E+0";
				else
				{
					String str_fe;
					int expo=0,i,deci;
					str_fe=ebox;
					if(str_fe.charAt(0)=='-')
						str_fe=str_fe.substring(1,str_fe.length());
					for(i=str_fe.length()-1;i>-1&&str_fe.charAt(i)!='E';i--);
					if(i>-1)
						expo=Integer.parseInt(str_fe.substring(i+1,str_fe.length()));
					for(i=str_fe.length()-1;i>-1&&str_fe.charAt(i)!='.';i--);
					deci=i;
					str_fe=scan(str_fe);
					if(str_fe.charAt(0)=='0')
					{
						for(i=0;str_fe.charAt(i)=='0';i++)
							expo--;
						str_fe=str_fe.substring(i,str_fe.length());
					}
					else if(str_fe.charAt(str_fe.length()-1)=='0')
					{
						for(i=str_fe.length()-1;str_fe.charAt(i)=='0';i--)
							expo++;
						str_fe=str_fe.substring(0,i+1);
					}
					else
					{
						expo+=deci-1;
					}
					if(ebox.charAt(0)=='-')
						ebox="-"+str_fe.charAt(0)+"."+str_fe.substring(1,str_fe.length())+"E"+expo;
					else
						ebox=str_fe.charAt(0)+"."+str_fe.substring(1,str_fe.length())+"E"+expo;
				}
				fe=true;
			}
			else
			{
				fe=false;
			}
			txt.setText(ebox);
			inp_status=true;
		}

		else if(label.equals("sqrt"))
		{
			num1=Double.parseDouble(txt.getText());
			txt.setText(decToNbase(Math.pow(num1,.5)));
		}

		else if(label.equals("%"))
		{
			if(op1!=0)
			{
				double pct=Double.parseDouble(txt.getText())/100;
				pct*=num1;
				txt.setText(decToNbase(pct));
			}
		}

		else if(label.equals("Sta"))
		{
			stat();
		}

		else if(label.equals("RET"))
		{
			frame.toFront();
		}

		else if(label.equals("Dat"))
		{
			ebox=Double.toString(nBaseToDec(txt.getText()));
			if(gbtn1_sel!=rbtn[1]||ebox.charAt(ebox.length()-2)=='.'&&ebox.charAt(ebox.length()-1)=='0')
			{
				int i;
				for(i=ebox.length()-1;ebox.charAt(i)!='.';i--);
				ebox=ebox.substring(0,i+1);
			}
			listModel.addElement(ebox);
			lblList.setText("n="+Integer.toString(listModel.getSize()));
			inp_status=true;
		}

		else if(label.equals("LOAD"))
		{
			if(list.getSelectedIndex()>-1&&listModel.getSize()>0)
			{
				txt.setText((String)list.getSelectedValue());
			}
		}

		else if(label.equals("CD"))
		{
			int index=list.getSelectedIndex();
			if(index>-1&&listModel.getSize()>0)
			{
				listModel.remove(index);
				if(index==listModel.getSize())
					index--;
				list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
                lblList.setText("n="+Integer.toString(listModel.getSize()));
			}
		}

		else if(label.equals("CAD"))
		{
			int index=0;
			while(index>-1&&listModel.getSize()>0)
			{
				list.setSelectedIndex(index);
				listModel.remove(index);
				if(index==listModel.getSize())
					index--;
			}
            lblList.setText("n="+Integer.toString(listModel.getSize()));
		}

		else if(label.equals("Ave"))
		{
			int size=listModel.getSize();
			if(size>0)
			{
				double ave=0;
				if(cbox[0].isSelected()==false)
				{
					for(int index=0;index<size;index++)
					{
						ave+=Double.parseDouble(((String)listModel.getElementAt(index)));
					}
				}
				else
				{
					for(int index=0;index<size;index++)
					{
						ave+=Math.pow(Double.parseDouble((String)listModel.getElementAt(index)),2);
					}
				}
				txt.setText(decToNbase(ave/listModel.getSize()));
			}
			cbox[0].setSelected(false);
		}

		else if(label.equals("Sum"))
		{
			int size=listModel.getSize();
			if(size>0)
			{
				double sum=0;
				if(cbox[0].isSelected()==false)
				{
					for(int index=0;index<size;index++)
					{
						sum+=Double.parseDouble((String)listModel.getElementAt(index));
					}
				}
				else
				{
					for(int index=0;index<size;index++)
					{
						sum+=Math.pow(Double.parseDouble((String)listModel.getElementAt(index)),2);
					}
				}
				txt.setText(decToNbase(sum));
			}
			cbox[0].setSelected(false);
		}

		else if(label.equals("s"))
		{
			int size=listModel.getSize(),index;
			if(size>0)
			{
				double ave=0,data[]=new double[size],sd=0;
				for(index=0;index<size;index++)
				{
					ave+=data[index]=Double.parseDouble((String)listModel.getElementAt(index));
				}
				ave/=listModel.getSize();
				for(index=0;index<data.length;index++)
					sd+=Math.pow(data[index]-ave,2);
				if(cbox[0].isSelected()==true)
					txt.setText(decToNbase(Math.pow(sd/index,.5)));
				else
				{
					if(index>1)
						txt.setText(decToNbase(Math.pow(sd/(index-1),.5)));
					else
						txt.setText(decToNbase(0.0));
				}
			}
			cbox[0].setSelected(false);
		}

		else if(label.equals("dms"))
		{
			if(cbox[0].isSelected()==false)
			{
				double dms=Double.parseDouble(txt.getText());
				ebox=Double.toString(dms);
				int j;
				for(j=ebox.length()-1;j>-1&&ebox.charAt(j)!='E';j--);
				if(j==-1)
				{
					long i,deg=(long)dms,min=(long)(dms=(dms-deg)*60),sec=(long)((dms-min)*60*1e9);
					ebox=deg+".";
					if(min>=0&&min<10)
						ebox+="0";
					ebox+=min;
					if(sec!=0)
						for(i=sec;i<1e10;i*=10)
							ebox+="0";
					ebox+=sec;
					txt.setText(decToNbase(nBaseToDec(ebox)));
				}
			}
			else
			{
				ebox=txt.getText();
				int j;
				for(j=ebox.length()-1;j>-1&&ebox.charAt(j)!='E';j--);
				if(j==-1)
				{
					for(j=0;ebox.charAt(j)!='.';j++);
					ebox=scan(ebox);
					String ssec="0",smin="0";
					int diff=ebox.length()-j;
					if(diff>0)
					{
						if(diff==1)
							smin=ebox.substring(j,j+1)+"0";
						else
							smin=ebox.substring(j,j+2);
						if(diff>2)
							ssec="."+ebox.substring(j+2,ebox.length());
					}
					double deg=Double.parseDouble(ebox.substring(0,j)),min=Double.parseDouble(smin),sec=Double.parseDouble(ssec)*100;
					sec=(min+sec/60)/60;
					deg+=sec;
					ebox=Double.toString(deg);
					txt.setText(decToNbase(nBaseToDec(ebox)));
				}
			}
			inp_status=true;
			cbox[0].setSelected(false);
		}
	}

	private void NondecRButton()
	{
		panel[0].removeAll();
		for(int i=0;i<4;i++)
			panel[0].add(rbtn[i]);
		for(int i=7;i<11;i++)
			panel[0].add(rbtn[i]);
		panel[0].setVisible(false);
		panel[0].setVisible(true);
	}

	private void decRButton()
	{
		panel[0].removeAll();
		for(int i=0;i<7;i++)
			panel[0].add(rbtn[i]);
//		panel[0].validate();
		panel[0].setVisible(false);
		panel[0].setVisible(true);
	}

	private void scientific()
	{
		if(mode==false)
		{
			frame.dispose();
			frame=new JFrame("Calculator");
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			frame.setLayout(new GridLayout(9,1));
			frame.add(mbar);

			txt.setText("0.");

			panel=new JPanel[7];
			for(int i=0;i<panel.length;i++)
			{
				panel[i]=new JPanel(new GridLayout(1,8));
				frame.add(panel[i]);
			}

			for(int i=0;i<7;i++)
				panel[0].add(rbtn[i]);

			for(int i=0;i<cbox.length;i++)
				panel[1].add(cbox[i]);

			panel[1].add(lbl_bracket);
			panel[1].add(lbl_mem);

			for(int i=0;i<3;i++)
				panel[1].add(btn[i]);

			for(int i=3;i<14;i++)
				panel[2].add(btn[i]);

			for(int i=14;i<25;i++)
				panel[3].add(btn[i]);

			for(int i=25;i<36;i++)
				panel[4].add(btn[i]);

			for(int i=36;i<47;i++)
				panel[5].add(btn[i]);

			for(int i=47;i<58;i++)
				panel[6].add(btn[i]);

			frame.add(txt);
			for(int i=0;i<panel.length;i++)
				frame.add(panel[i]);

			noFunction();
			init();
			statFunctions(false);

			cbox[0].setSelected(false);
			cbox[1].setSelected(false);

			frame.setSize(699,302);
			frame.setVisible(true);

			mode=true;
		}
	}

	private void standard()
	{
		if(mode==true)
		{
			if(frame!=null)
				frame.dispose();
			if(listDlg!=null)
			{
				listDlg.dispose();
				listDlg=null;
			}
			frame=new JFrame("Calculator");
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			frame.setLayout(new GridLayout(7,1));
			frame.add(mbar);

			txt.setText("0.");
			frame.add(txt);

			panel=new JPanel[5];
			for(int i=0;i<panel.length;i++)
			{
				panel[i]=new JPanel(new GridLayout(1,6));
				frame.add(panel[i]);
			}

			panel[0].add(lbl_mem);
			for(int i=0;i<3;i++)
				panel[0].add(btn[i]);

			for(int i=7;i<12;i++)
				panel[1].add(btn[i]);

			panel[1].add(btn[58]);

			for(int i=18;i<23;i++)
				panel[2].add(btn[i]);

			panel[2].add(btn[59]);

			for(int i=29;i<34;i++)
				panel[3].add(btn[i]);

			panel[3].add(btn[50]);

			for(int i=40;i<46;i++)
				panel[4].add(btn[i]);

			for(int i=0;i<panel.length;i++)
				frame.add(panel[i]);

			init();

			frame.setSize(414,214);
			frame.setVisible(true);

			mode=false;
		}
	}

	private void createControls()
	{
		txt=new JTextField("0.");
		txt.setHorizontalAlignment(JTextField.RIGHT);
		txt.setEditable(false);
		txt.setBackground(Color.WHITE);

		rbtn=new JRadioButton[11];
		rbtn[0]=new JRadioButton("Hex");
		rbtn[1]=new JRadioButton("Dec",true);
		rbtn[2]=new JRadioButton("Oct");
		rbtn[3]=new JRadioButton("Bin");
		rbtn[4]=new JRadioButton("Degree",true);
		rbtn[5]=new JRadioButton("Radians");
		rbtn[6]=new JRadioButton("Grads");
		rbtn[7]=new JRadioButton("Qword",true);
		rbtn[8]=new JRadioButton("Dword");
		rbtn[9]=new JRadioButton("Word");
		rbtn[10]=new JRadioButton("Byte");

		mbar=new JMenuBar();
		menu_edit=new JMenu("Edit");
		menu_edit.setMnemonic(KeyEvent.VK_E);
		menu_view=new JMenu("View");
		menu_view.setMnemonic(KeyEvent.VK_V);
		menu_help=new JMenu("Help");
		menu_help.setMnemonic(KeyEvent.VK_H);

		mf_copy=new JMenuItem("Copy");
		mf_copy.setMnemonic(KeyEvent.VK_C);
		mf_paste=new JMenuItem("Paste");
		mf_paste.setMnemonic(KeyEvent.VK_P);
		mf_exit=new JMenuItem("Exit");
		mf_exit.setMnemonic(KeyEvent.VK_E);
		mh_about=new JMenuItem("About");
		mh_about.setMnemonic(KeyEvent.VK_A);
		mv_sta=new JRadioButtonMenuItem("Standard",true);
		mv_sta.setMnemonic(KeyEvent.VK_T);
		mv_sci=new JRadioButtonMenuItem("Scientific");
		mv_sci.setMnemonic(KeyEvent.VK_S);

		menu_edit.add(mf_copy);
		menu_edit.add(mf_paste);
		menu_edit.addSeparator();
		menu_edit.add(mf_exit);

		menu_help.add(mh_about);

		menu_view.add(mv_sta);
		menu_view.add(mv_sci);

		mbar.add(menu_edit);
		mbar.add(menu_view);
		mbar.add(menu_help);

		mv_gbtn=new ButtonGroup();
		mv_gbtn.add(mv_sci);
		mv_gbtn.add(mv_sta);

		gbtn1=new ButtonGroup();
		for(int i=0;i<4;i++)
			gbtn1.add(rbtn[i]);

		gbtn2=new ButtonGroup();
		for(int i=4;i<7;i++)
			gbtn2.add(rbtn[i]);

		gbtn3=new ButtonGroup();
		for(int i=7;i<11;i++)
			gbtn3.add(rbtn[i]);
		gbtn3_sel=8;

		cbox=new JCheckBox[2];
		cbox[0]=new JCheckBox("Inv");
		cbox[1]=new JCheckBox("Hyp");

		lbl_bracket=new JLabel();
		lbl_mem=new JLabel();
		lbl_mem.setHorizontalAlignment(JTextField.CENTER);
		lbl_bracket.setHorizontalAlignment(JTextField.CENTER);

		btn=new JButton[64];
		btn[0]=new JButton("Backspace");
		btn[1]=new JButton("CE");
		btn[2]=new JButton("C");

		btn[3]=new JButton("Sta");
		btn[4]=new JButton("F-E");
		btn[5]=new JButton("(");
		btn[6]=new JButton(")");
		btn[7]=new JButton("MC");
		btn[8]=new JButton("7");
		btn[9]=new JButton("8");
		btn[10]=new JButton("9");
		btn[11]=new JButton("/");
		btn[12]=new JButton("Mod");
		btn[13]=new JButton("And");

		btn[14]=new JButton("Ave");
		btn[15]=new JButton("dms");
		btn[16]=new JButton("Exp");
		btn[17]=new JButton("ln");
		btn[18]=new JButton("MR");
		btn[19]=new JButton("4");
		btn[20]=new JButton("5");
		btn[21]=new JButton("6");
		btn[22]=new JButton("*");
		btn[23]=new JButton("Or");
		btn[24]=new JButton("Xor");

		btn[25]=new JButton("Sum");
		btn[26]=new JButton("sin");
		btn[27]=new JButton("x^y");
		btn[28]=new JButton("log");
		btn[29]=new JButton("MS");
		btn[30]=new JButton("1");
		btn[31]=new JButton("2");
		btn[32]=new JButton("3");
		btn[33]=new JButton("-");
		btn[34]=new JButton("lsh");
		btn[35]=new JButton("Not");

		btn[36]=new JButton("s");
		btn[37]=new JButton("cos");
		btn[38]=new JButton("x^3");
		btn[39]=new JButton("n!");
		btn[40]=new JButton("M+");
		btn[41]=new JButton("0");
		btn[42]=new JButton("+/-");
		btn[43]=new JButton(".");
		btn[44]=new JButton("+");
		btn[45]=new JButton("=");
		btn[46]=new JButton("Int");

		btn[47]=new JButton("Dat");
		btn[48]=new JButton("tan");
		btn[49]=new JButton("x^2");
		btn[50]=new JButton("1/x");
		btn[51]=new JButton("pi");
		btn[52]=new JButton("A");
		btn[53]=new JButton("B");
		btn[54]=new JButton("C");
		btn[55]=new JButton("D");
		btn[56]=new JButton("E");
		btn[57]=new JButton("F");
		btn[58]=new JButton("sqrt");
		btn[59]=new JButton("%");
		btn[60]=new JButton("RET");
		btn[61]=new JButton("LOAD");
		btn[62]=new JButton("CD");
		btn[63]=new JButton("CAD");
	}

	private void stat()
	{
		if(listDlg==null)
		{
			lblList=new JLabel("n=0");

			listModel = new DefaultListModel();
			list = new JList(listModel);
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setVisibleRowCount(5);
			listScrollPane = new JScrollPane(list);

			listDlg=new JDialog(new JFrame(),"Statistics Box");
			listDlg.setResizable(false);
			listDlg.setSize(275,200);
			listDlg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

			listDlg.setLayout(new GridLayout(3,1));
			lpanel=new JPanel[3];

			for(int i=0;i<lpanel.length;i++)
				lpanel[i]=new JPanel(new GridLayout(1,1));

			lpanel[0].add(listScrollPane);
			for(int i=60;i<64;i++)
				lpanel[1].add(btn[i]);
			lpanel[2].add(new JLabel());
			lpanel[2].add(lblList);

			for(int i=0;i<lpanel.length;i++)
				listDlg.add(lpanel[i]);

			listDlg.setVisible(true);
			statFunctions(true);
		}
		else if(cbox[0].isSelected()==true)
		{
			statFunctions(false);
			listDlg.dispose();
			listDlg=null;
		}
		else
		{
			listDlg.toFront();
		}
		cbox[0].setSelected(false);
	}

	private void statFunctions(boolean b)
	{
		btn[14].setEnabled(b);
		btn[16].setEnabled(b);
		btn[25].setEnabled(b);
		btn[36].setEnabled(b);
		btn[47].setEnabled(b);
	}
}