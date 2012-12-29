package jhelp.util.math.formal;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test of {@link Function}
 * 
 * @author JHelp
 */
public class FunctionTest
{
   /**
    * Simplify test
    * 
    * @param start
    *           Expected function
    * @param end
    *           Tested function
    */
   private void symplifyTest(final String start, final String end)
   {
      Assert.assertEquals(Function.parse(end).simplifyMaximum(), Function.parse(start).simplifyMaximum());
   }

   /**
    * Test of drivate
    */
   @Test
   public void testBaseDerive()
   {
      Assert.assertEquals(Constant.ONE, Function.parse("x").derive("x").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.ZERO, Function.parse("y").derive("x").simplifyMaximum(System.out));

      Assert.assertEquals(Function.parse("-sin(x)").simplifyMaximum(), Function.parse("cos(x)").derive("x").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("cos(x)"), Function.parse("sin(x)").derive("x").simplifyMaximum(System.out));

      Assert.assertEquals(Function.parse("cos(x)-sin(x)"), Function.parse("cos(x)+sin(x)").derive("x").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("cos(x)+sin(x)"), Function.parse("sin(x)-cos(x)").derive("x").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("cos(2*x)"), Function.parse("cos(x)*sin(x)").derive("x").simplifyMaximum(System.out));

      Assert.assertEquals(Function.parse("cos(1+x)"), Function.parse("sin(1+x)").derive("x").simplifyMaximum(System.out));

      Assert.assertEquals(Function.parse("(cos(x)*sin(x+1)-cos(x+1)*sin(x))/(sin(x+1)*sin(x+1))").simplifyMaximum(System.out), Function.parse("sin(x)/sin(x+1)").derive("x").simplifyMaximum(System.out));

      Assert.assertEquals(Function.parse("2*x").simplifyMaximum(), Function.parse("x*x").derive("x").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("5*x*x*x*x").simplifyMaximum(), Function.parse("x*x*x*x*x").derive("x").simplifyMaximum(System.out));
   }

   /**
    * Test of additions and linked rules
    */
   @Test
   public void testDirectAddition()
   {
      Assert.assertEquals(Constant.ONE, new MinusUnary(Constant.MINUS_ONE).simplifyMaximum(System.out));

      final Variable x = new Variable("X");
      final Variable y = new Variable("Y");
      final Variable z = new Variable("Z");

      Function function1 = Function.createAddition(x, y, z);
      Function function2 = Function.createAddition(z, y, x);

      Assert.assertTrue(function1.equals(function2));

      function1 = Function.createAddition(x, Constant.ONE, y, Constant.ONE, z);
      function2 = Function.createAddition(z, Constant.TWO, y, x);

      Assert.assertEquals(function2.simplifyMaximum(System.out), function1.simplifyMaximum(System.out));
      Assert.assertEquals(Constant.ONE, function1.derive(y).simplifyMaximum(System.out));

      function1 = Function.createAddition(x, z, y, z);
      function2 = Function.createAddition(z, x, y, x);
      Assert.assertFalse(function1.equals(function2));

      function1 = Function.createAddition(x, z, y, z, x);
      function2 = Function.createAddition(z, x, z, y, x);
      Assert.assertTrue(function1.equals(function2));

      function1 = Function.createAddition(new MinusUnary(x), new MinusUnary(y), new MinusUnary(z));
      function2 = new MinusUnary(Function.createAddition(x, z, y));

      Assert.assertEquals(function2.simplifyMaximum(System.out), function1.simplifyMaximum(System.out));

      function1 = new MinusUnary(Function.createAddition(new MinusUnary(x), new MinusUnary(y), new MinusUnary(z)));
      function2 = Function.createAddition(x, z, y);

      Assert.assertEquals(function2.simplifyMaximum(System.out), function1.simplifyMaximum(System.out));

      function1 = new MinusUnary(Function.createAddition(new MinusUnary(x), new MinusUnary(y), new MinusUnary(z), new MinusUnary(Constant.MINUS_ONE)));
      function2 = Function.createAddition(x, Constant.MINUS_ONE, z, y);

      Assert.assertEquals(function2.simplifyMaximum(System.out), function1.simplifyMaximum(System.out));
      function1 = new MinusUnary(Function.createAddition(new MinusUnary(x), new MinusUnary(y), new MinusUnary(z), Constant.ONE));
      function2 = Function.createAddition(x, Constant.MINUS_ONE, z, y);

      Assert.assertEquals(function2, function1.simplifyMaximum(System.out));
   }

   /**
    * Copy test
    */
   @Test
   public void testGetCopy()
   {
      final Function f = Function.parse("x+ln(y+E)-cos(sin(PI+z))*exp(tan(x))+x/z+exp(-y)");
      Assert.assertEquals(f.simplifyMaximum(System.out), f.getCopy().simplifyMaximum(System.out));
      Assert.assertEquals(f.simplifyMaximum(), f.simplifyMaximum().getCopy().simplifyMaximum(System.out));

      final VariableList variableList = new VariableList();
      variableList.add("x", "y", "z");
      Assert.assertEquals(variableList, f.simplifyMaximum().variableList());

      Assert.assertEquals(Function.parse("a+ln(y+E)-cos(sin(PI+z))*exp(tan(a))+a/z+exp(-y)").simplifyMaximum(), f.replace("x", new Variable("a")).simplifyMaximum(System.out));

      variableList.clear();
      variableList.add("a", "y", "z");
      Assert.assertEquals(variableList, f.replace("x", new Variable("a")).simplifyMaximum().variableList());

      variableList.clear();
      variableList.add("X", "x");
      Assert.assertEquals(variableList, Function.parse("x+X").simplifyMaximum().variableList());
   }

   /**
    * 
    Parsing test
    */
   @Test
   public void testParse()
   {
      Assert.assertEquals(Constant.ONE, Function.parse("(1+x)-x").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.ONE, Function.parse("ln(E)").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.MINUS_ONE, Function.parse("cos(PI)").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.ONE, Function.parse("(x+1)-x").simplifyMaximum(System.out));

      Assert.assertEquals(Constant.ZERO, Function.parse("x+y+z+a+b+c+d+e+f-x-y-z-a-b-c-d-e-f").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("x-k").simplifyMaximum(), Function.parse("(x+y+z+a+b+c+d+e+f)-(y+z+a+b+k+c+d+e+f)").simplifyMaximum(System.out));
      Assert.assertEquals(new Variable("x"), Function.parse("x+y+z+a+b+c+d+e+f-(y+z+a+b+c+d+e+f)").simplifyMaximum(System.out));
      Assert.assertEquals(new MinusUnary(new Variable("x")), Function.parse("y+z+a+b+c+d+e+f-(x+y+z+a+b+c+d+e+f)").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.ONE, Function.parse("x*y*z*a*b*c*d*e*f/(x*y*z*a*b*c*d*e*f)").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.ONE, Function.parse("(((((((((x*y*z*a*b*c*d*e*f)/x)/y)/z)/a)/b)/c)/d)/e)/f").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.ONE, Function.parse("((((((((x*y*z*a*b*c*d*e*f/x)/y)/z)/a)/b)/c)/d)/e)/f").simplifyMaximum(System.out));

      Assert.assertEquals(Constant.ZERO, Function.parse("x+y+z+a+b+c+d+e+f-f-e-b-d-c-z-a-y-x").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.ONE, Function.parse("x*y*z*a*b*c*d*e*f/(f*e*b*d*c*z*a*y*x)").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.ONE, Function.parse("(((((((((x*y*z*a*b*c*d*e*f)/f)/e)/b)/d)/c)/z)/a)/y)/x").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.ONE, Function.parse("((((((((x*y*z*a*b*c*d*e*f/f)/e)/b)/d)/c)/z)/a)/y)/x").simplifyMaximum(System.out));

      //

      Assert.assertEquals(Constant.ONE, Function.parse("cos(x)*cos(x)+sin(x)*sin(x)").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.ONE, Function.parse("sin(x)*sin(x)+cos(x)*cos(x)").simplifyMaximum(System.out));

      Assert.assertEquals(Function.parse("1320*a*b*x*y*z").simplifyMaximum(), Function.parse("x*5*3*y*8*z*a*11*b").simplifyMaximum(System.out));
   }

   /**
    * Simplification test
    */
   @Test
   public void testSymplify()
   {
      Assert.assertEquals(Constant.UNDEFINED, Function.parse("x-(y/0)").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.UNDEFINED, Function.parse("(y/0)-x").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.UNDEFINED, Function.parse("7-(y/0)").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.UNDEFINED, Function.parse("(y/0)-3").simplifyMaximum(System.out));
      Assert.assertEquals(new Constant(4), Function.parse("7-3").simplifyMaximum(System.out));
      Assert.assertEquals(new Variable("x"), Function.parse("x-0").simplifyMaximum(System.out));
      Assert.assertEquals(new MinusUnary(new Variable("x")), Function.parse("0-x").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("1+x"), Function.parse("1-(-x)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("-(x-y)").simplifyMaximum(), Function.parse("(-x)-(-y)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("-(x+y)").simplifyMaximum(), Function.parse("(-x)-y").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("x+y"), Function.parse("x-(-y)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("cos(x+y)").simplifyMaximum(), Function.parse("cos(x)*cos(y)-sin(y)*sin(x)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("sin(y-x)").simplifyMaximum(), Function.parse("cos(x)*sin(y)-sin(x)*cos(y)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("sin(y-x)").simplifyMaximum(), Function.parse("sin(y)*cos(x)-sin(x)*cos(y)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("-cos(x+y)").simplifyMaximum(), Function.parse("sin(x)*sin(y)-cos(y)*cos(x)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("-cos(x+y)").simplifyMaximum(), Function.parse("sin(y)*sin(x)-cos(y)*cos(x)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("3*x").simplifyMaximum(), Function.parse("(4*x)-x").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("3*x").simplifyMaximum(), Function.parse("(x*4)-x").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("3*x").simplifyMaximum(), Function.parse("(5*x)-(2*x)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("3*x").simplifyMaximum(), Function.parse("(5*x)-(x*2)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("3*x").simplifyMaximum(), Function.parse("(x*5)-(2*x)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("3*x").simplifyMaximum(), Function.parse("(x*5)-(x*2)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("-3*x").simplifyMaximum(), Function.parse("x-(4*x)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("-3*x").simplifyMaximum(), Function.parse("x-(x*4)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("(X-Z)+(A-Y)").simplifyMaximum(), Function.parse("(X-Y)-(Z-A)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("(X-Z)-Y").simplifyMaximum(), Function.parse("(X-Y)-Z").simplify());
      Assert.assertEquals(Function.parse("2*(y*x)").simplifyMaximum(), Function.parse("(4*(x*y))-(2*(y*x))").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("2*(y*x)").simplifyMaximum(), Function.parse("((x*y)*4)-(2*(y*x))").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("2*(y*x)").simplifyMaximum(), Function.parse("(4*(x*y))-((y*x)*2)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("2*(y*x)").simplifyMaximum(), Function.parse("((x*y)*4)-((y*x)*2)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("2*(y*x)").simplifyMaximum(System.out), Function.parse("((x*y)*3)-(y*x)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("2*(y*x)").simplifyMaximum(), Function.parse("(3*(x*y))-(y*x)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("-2*(y*x)").simplifyMaximum(), Function.parse("(x*y)-(3*(y*x))").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("-2*(y*x)").simplifyMaximum(), Function.parse("(x*y)-((y*x)*3)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("2*(y*x)").simplifyMaximum(), Function.parse("(x*4*y)-(y*2*x)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("2*(y*x)").simplifyMaximum(), Function.parse("(x*3*y)-(y*x)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("-2*(y*x)").simplifyMaximum(), Function.parse("(x*y)-(y*3*x)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("56*(y*x)").simplifyMaximum(), Function.parse("(4*x*4*y*4)-(2*y*2*x*2)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("ln(X/Y)").simplifyMaximum(), Function.parse("ln(X)-ln(Y)").simplifyMaximum(System.out));

      Assert.assertEquals(Function.parse("cos(x)").simplifyMaximum(), Function.parse("cos(-x)").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.UNDEFINED, Function.parse("cos(y/0)").simplifyMaximum(System.out));

      Assert.assertEquals(Function.parse("-sin(x)").simplifyMaximum(), Function.parse("sin(-x)").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.UNDEFINED, Function.parse("sin(y/0)").simplifyMaximum(System.out));

      Assert.assertEquals(Function.parse("x").simplifyMaximum(), Function.parse("ln(exp(x))").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.UNDEFINED, Function.parse("ln(y/0)").simplifyMaximum(System.out));

      Assert.assertEquals(Function.parse("x").simplifyMaximum(), Function.parse("exp(ln(x))").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("1").simplifyMaximum(), Function.parse("exp(0)").simplifyMaximum(System.out));
      Assert.assertEquals(Function.parse("E").simplifyMaximum(), Function.parse("exp(1)").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.UNDEFINED, Function.parse("exp(y/0)").simplifyMaximum(System.out));

      Assert.assertEquals(Function.parse("0").simplifyMaximum(), Function.parse("tan(0)").simplifyMaximum(System.out));
      Assert.assertEquals(Constant.UNDEFINED, Function.parse("tan(y/0)").simplifyMaximum(System.out));

      //

      this.symplifyTest("cos(X) * cos(Y) - sin(X) * sin(Y)", "cos(X + Y)");
      this.symplifyTest("cos(X) * cos(Y) - sin(Y) * sin(X)", "cos(X + Y)");
      this.symplifyTest("cos(X) * sin(Y) - sin(X) * cos(Y)", "sin(Y - X)");
      this.symplifyTest("sin(Y) * cos(X) - cos(Y) * sin(X)", "sin(Y - X)");
      this.symplifyTest("sin(Y) * cos(X) - sin(X) * cos(Y)", "sin(Y - X)");
      this.symplifyTest("sin(X) * sin(Y) - cos(X) * cos(Y)", "-cos(X + Y)");
      this.symplifyTest("sin(Y) * sin(X) - cos(X) * cos(Y)", "-cos(X + Y)");
      this.symplifyTest("(Y/0) * X - Z", "Y/0");
      this.symplifyTest("(5 * X) - (7 * X)", "-2 * X");
      this.symplifyTest("(5 * X) - (7 * X)", "-2 * X");
      this.symplifyTest("(X * 5) - X ", " 4 * X");
      this.symplifyTest("(X * 5) - (7 * X)", "-2 * X");
      this.symplifyTest("(X * 5) - (X * 7)", "-2 * X");
      this.symplifyTest("X - (5 * X)", " -4 * X");
      this.symplifyTest("X - (X * 5)", "-4 * X");

      //

      this.symplifyTest("(Y/0) * X ", " Y/0");
      this.symplifyTest("5*(Y/0) ", " Y/0");
      this.symplifyTest("5*7 ", " 35");
      this.symplifyTest("0*X ", " 0");
      this.symplifyTest("-1*X", "-X");
      this.symplifyTest("5 * (7 * X) ", " 35 * X");
      this.symplifyTest("5 * (X * 7)", "35 * X");
      this.symplifyTest("X*(Y/0)", "Y/0");
      this.symplifyTest("X*0", "0");
      this.symplifyTest("X*(-1)", "-X");
      this.symplifyTest("(X * 5) * 7 ", " 35 * X");
      this.symplifyTest("(-X) * (-Y) ", " X * Y");
      this.symplifyTest("X * (-Y)", " - (X * Y)");
      this.symplifyTest("(X / Y) * (Z / A) ", " (X * Z) / (Y * A)");
      this.symplifyTest("(X / Y) * Z ", "(X * Z) / Y");
      this.symplifyTest("exp(X) * exp(Y) ", " exp(X + Y)");

      //

      this.symplifyTest("(Y/0)+X", "Y/0");
      this.symplifyTest("5+(Y/0)", "Y/0");
      this.symplifyTest("X+(Y/0)", "Y/0");
      this.symplifyTest("ln(X) + ln(Y)", "ln(X * Y)");
      this.symplifyTest("cos(X) * cos(Y) + sin(X) * sin(Y) ", " cos(X - Y)");
      this.symplifyTest("cos(X) * cos(Y) + sin(Y) * sin(X) ", " cos(X - Y)");
      this.symplifyTest("cos(X) * sin(Y) + cos(Y) * sin(X) ", " sin(Y + X)");
      this.symplifyTest("cos(X) * sin(Y) + sin(X) * cos(Y) ", " sin(Y + X)");
      this.symplifyTest("sin(Y) * cos(X) + cos(Y) * sin(X) ", " sin(Y + X)");
      this.symplifyTest("sin(Y) * cos(X) + sin(X) * cos(Y) ", " sin(Y + X)");
      this.symplifyTest("sin(X) * sin(Y) + cos(X) * cos(Y) ", " cos(X - Y)");
      this.symplifyTest("sin(Y) * sin(X) + cos(X) * cos(Y) ", " cos(X - Y)");
      this.symplifyTest("(5 * X) + X ", " 6 * X");
      this.symplifyTest("(5 * X) + (7 * X) ", " 12 * X");
      this.symplifyTest("(5 * X) + (X * 7) ", " 12 * X");
      this.symplifyTest("(X * 5) + X ", " 6 * X");
      this.symplifyTest("(X * 5) + (7 * X) ", " 12 * X");
      this.symplifyTest("(X * 5) + (X * 7) ", " 12 * X");
      this.symplifyTest("(X * 5) + X  ", " 6 * X");
      this.symplifyTest("(5*X ) + X  ", " 6 * X");
      this.symplifyTest("X + (X*5)  ", " 6 * X");
      this.symplifyTest("X + (5*X)  ", " 6 * X");
      this.symplifyTest("(X*Y) + (2*X*2*Y*2) ", " 9 * X*Y");
      this.symplifyTest("(2*X*2*Y*2)+(X*Y) ", " 9 * X*Y");
      this.symplifyTest("(2*X*2*Y*2) + (2*X*2*Y*2)", " 16 * X*Y");
      this.symplifyTest("(X*Y) + (2*2*2)", " 8+ X*Y");
      this.symplifyTest("(2*2*2)+(X*Y)", "8+ X*Y");
      this.symplifyTest("2+X + 2", " 4+ X");

      //

      this.symplifyTest("-(Y/0)", "Y/0");
      this.symplifyTest("-((Y/0)*X)", "Y/0");
      this.symplifyTest("-(X*(Y/0))", "Y/0");
      this.symplifyTest("-(2*X)", "-2*X");
      this.symplifyTest("-(X*2)", "-2*X");
      this.symplifyTest("-((Y/0)/X)", "Y/0");
      this.symplifyTest("-(X/(Y/0))", "Y/0");
      this.symplifyTest("-(2/X)", "-2/X");
      this.symplifyTest("-(X/2)", "X/(-2)");

      //

      this.symplifyTest("(Y/0) / X ", " Y/0");
      this.symplifyTest("5/(Y/0) ", " Y/0");
      this.symplifyTest("4/8 ", " 0.5");
      this.symplifyTest("0/X", "0");
      this.symplifyTest("X/1", "X");
      this.symplifyTest("X/(-1)", "-X");
      this.symplifyTest("(8*X)/4", "2*X");
      this.symplifyTest("(X*8)/4", "2*X");
      this.symplifyTest("X/2", "0.5*X");
      this.symplifyTest("(-X)/(-Y)", "X/Y");
      this.symplifyTest("(-X)/Y", "-(X/Y)");
      this.symplifyTest("X/(-Y)", "-(X/Y)");
      this.symplifyTest("sin(X) / cos(X)", "tan(X)");
      this.symplifyTest("cos(X) / sin(X) ", "1 / tan(X)");
      this.symplifyTest("exp(X) / exp(Y)", "exp(X - Y)");
      this.symplifyTest("(X/Y)/(Z/A)", "(X*A)/(Y*Z)");
      this.symplifyTest("X/(Y/Z)", "(X*Z)/Y");
   }
}