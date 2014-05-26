package jhelp.util.math.fix;

public class MathReal
{
   public static Real absolute(final Real real)
   {
      return new Real(real.sign() * real.value());
   }

   public static Real cos(final Real real)
   {
      return new Real(Math.cos(real.value()));
   }

   public static Real sin(final Real real)
   {
      return new Real(Math.sin(real.value()));
   }

   public static Real square(final Real real)
   {
      return real.multiplication(real, null);
   }

   public static Real squareRoot(final Real real)
   {
      return new Real(Math.sqrt(real.value()));
   }

   public static Real tan(final Real real)
   {
      return new Real(Math.tan(real.value()));
   }
}