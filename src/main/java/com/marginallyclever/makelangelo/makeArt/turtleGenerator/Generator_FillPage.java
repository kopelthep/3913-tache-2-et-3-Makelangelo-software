package com.marginallyClever.makelangelo.makeArt.turtleGenerator;

import com.marginallyClever.convenience.Clipper2D;
import com.marginallyClever.convenience.Point2D;
import com.marginallyClever.makelangelo.Translator;
import com.marginallyClever.makelangelo.turtle.Turtle;

/**
 * Completely fills the page with ink.
 * @author Dan Royer
 */
public class Generator_FillPage extends TurtleGenerator {
	private static double angle = 0;
	private static double penDiameter = 0.8;// must be greater than zero ! (or else infinit loop)

	@Override
	public String getName() {
		return Translator.get("FillPageName");
	}

	static public double getAngle() {
		return angle;
	}
	static public void setAngle(double value) {
		Generator_FillPage.angle = value;
	}
	
	public static double getPenDiameter() {
		return penDiameter;
	}

	public static void setPenDiameter(double penDiameter) {
		Generator_FillPage.penDiameter = penDiameter;
	}

	@Override
	public TurtleGeneratorPanel getPanel() {
		return new Generator_FillPage_Panel(this);
	}
	
	@Override
	public void generate() {
		double majorX = Math.cos(Math.toRadians(angle));
		double majorY = Math.sin(Math.toRadians(angle));


		// from top to bottom of the margin area...
		double yBottom = myPaper.getMarginBottom();
		double yTop    = myPaper.getMarginTop()   ;
		double xLeft   = myPaper.getMarginLeft()  ;
		double xRight  = myPaper.getMarginRight() ;
		double dy = (yTop - yBottom)/2;
		double dx = (xRight - xLeft)/2;
		double radius = Math.sqrt(dx*dx+dy*dy);

		Turtle turtle = new Turtle();
		turtle.setDiameter(penDiameter);
		Point2D P0=new Point2D();
		Point2D P1=new Point2D();

		Point2D rMax = new Point2D(xRight,yTop);
		Point2D rMin = new Point2D(xLeft,yBottom);
		
		int i=0;
		if ( penDiameter > 0 ){
                    for(double a = -radius;a<radius;a+=penDiameter) {
                            double majorPX = majorX * a;
                            double majorPY = majorY * a;
                            P0.set( majorPX - majorY * radius,
                                            majorPY + majorX * radius);
                            P1.set( majorPX + majorY * radius,
                                            majorPY - majorX * radius);
                            if(Clipper2D.clipLineToRectangle(P0, P1, rMax, rMin)) {
                                    if ((i % 2) == 0) 	{
                                            turtle.moveTo(P0.x,P0.y);
                                            turtle.penDown();
                                            turtle.moveTo(P1.x,P1.y);
                                    } else {
                                            turtle.moveTo(P1.x,P1.y);
                                            turtle.penDown();
                                            turtle.moveTo(P0.x,P0.y);
                                    }
                            }
                            ++i;
                    }
                }else{
                    // TODO throw error message "penDiameter must be greater than zero."
                }

		notifyListeners(turtle);
	}
}
